package com.senukai.app.data.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.senukai.app.data.entities.BookDetailsEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface BookDetailsDao {
    @Upsert
    suspend fun upsertAll(books: List<BookDetailsEntity>)
    @Upsert
    suspend fun upsert(book: BookDetailsEntity)

    @Query("SELECT * FROM book_details ORDER BY title")
    fun observeAll(): Flow<List<BookDetailsEntity>>

    @Query("SELECT * FROM book_details WHERE listId = :listId ORDER BY title")
    fun observeByList(listId: Int): Flow<List<BookDetailsEntity>>

    @Query("SELECT * FROM book_details WHERE id = :id")
    fun observeById(id: Int): Flow<BookDetailsEntity?>
}