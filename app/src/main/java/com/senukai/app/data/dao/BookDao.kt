package com.senukai.app.data.dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.senukai.app.data.entities.BookDetailsEntity
import com.senukai.app.data.entities.BooksEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface BookDao {
    @Upsert
    suspend fun upsertAll(books: List<BooksEntity>)

    @Query("SELECT * FROM books ORDER BY title")
    fun observeAll(): Flow<List<BooksEntity>>

    @Query("SELECT * FROM books WHERE listId = :listId ORDER BY title")
    fun observeByList(listId: Int): Flow<List<BooksEntity>>

    @Query("SELECT * FROM books WHERE id = :id")
    fun observeById(id: Int): Flow<BooksEntity?>
}