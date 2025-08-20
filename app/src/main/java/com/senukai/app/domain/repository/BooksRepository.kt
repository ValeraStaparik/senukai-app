package com.senukai.app.domain.repository

import com.senukai.app.data.entities.BookDetailsEntity
import com.senukai.app.data.entities.BooksEntity
import com.senukai.app.data.entities.ReadBookStatusEntity
import kotlinx.coroutines.flow.Flow

interface BooksRepository {
    fun observeBooks(): Flow<List<BooksEntity>>
    fun observeBooksByList(listId: Int): Flow<List<BooksEntity>>
    fun observeReadStatus(): Flow<List<ReadBookStatusEntity>>
    fun observeBookDetails(bookId: Int): Flow<BookDetailsEntity?>

    suspend fun refreshAll()
    suspend fun refreshBookDetails(bookId: Int)
}