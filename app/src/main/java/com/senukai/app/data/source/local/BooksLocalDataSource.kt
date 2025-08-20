package com.senukai.app.data.source.local

import androidx.room.withTransaction
import com.senukai.app.core.AppDatabase
import com.senukai.app.data.dao.BookDao
import com.senukai.app.data.dao.BookDetailsDao
import com.senukai.app.data.dao.ReadStatusDao
import com.senukai.app.data.entities.BookDetailsEntity
import com.senukai.app.data.entities.ReadBookStatusEntity
import com.senukai.app.data.entities.BooksEntity
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

interface BooksLocalDataSource {
    fun observeBooks(): Flow<List<BooksEntity>>
    fun observeBooksByList(listId: Int): Flow<List<BooksEntity>>
    fun observeBookDetails(bookId: Int): Flow<BookDetailsEntity?>
    fun observeReadStatus(): Flow<List<ReadBookStatusEntity>>
    suspend fun getBookDetailsNow(bookId: Int): Flow<BookDetailsEntity?>

    suspend fun upsertBooks(items: List<BooksEntity>)
    suspend fun upsertLists(items: List<ReadBookStatusEntity>)
    suspend fun upsertBookDetails(item: BookDetailsEntity)

    suspend fun replaceAll(
        books: List<BooksEntity>,
        readBookStatusEntityList: List<ReadBookStatusEntity>,
        details: List<BookDetailsEntity> = emptyList()
    )
}

class BooksLocalDataSourceImpl @Inject constructor(
    private val db: AppDatabase,
    private val bookDao: BookDao,
    private val readStatusDao: ReadStatusDao,
    private val bookDetailsDao: BookDetailsDao
) : BooksLocalDataSource {

    override suspend fun getBookDetailsNow(bookId: Int): Flow<BookDetailsEntity?> =
        bookDetailsDao.observeById(bookId)

    override fun observeBooks(): Flow<List<BooksEntity>> = bookDao.observeAll()
    override fun observeBooksByList(listId: Int): Flow<List<BooksEntity>> =
        bookDao.observeByList(listId)

    override fun observeBookDetails(bookId: Int): Flow<BookDetailsEntity?> = bookDetailsDao.observeById(bookId)
    override fun observeReadStatus(): Flow<List<ReadBookStatusEntity>> = readStatusDao.observeAll()

    override suspend fun upsertBooks(items: List<BooksEntity>) = bookDao.upsertAll(items)
    override suspend fun upsertLists(items: List<ReadBookStatusEntity>) = readStatusDao.upsertAll(items)
    override suspend fun upsertBookDetails(item: BookDetailsEntity) = bookDetailsDao.upsert(item)

    override suspend fun replaceAll(
        books: List<BooksEntity>,
        readBookStatusEntityList: List<ReadBookStatusEntity>,
        details: List<BookDetailsEntity>
    ) {
        db.withTransaction {
            readStatusDao.upsertAll(readBookStatusEntityList)
            bookDao.upsertAll(books)
            if (details.isNotEmpty()) bookDetailsDao.upsertAll(details)
        }
    }
}