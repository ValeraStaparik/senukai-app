package com.senukai.app.core

import androidx.room.Database
import androidx.room.RoomDatabase
import com.senukai.app.data.dao.BookDao
import com.senukai.app.data.dao.BookDetailsDao
import com.senukai.app.data.dao.ReadStatusDao
import com.senukai.app.data.entities.BookDetailsEntity
import com.senukai.app.data.entities.BooksEntity
import com.senukai.app.data.entities.ReadBookStatusEntity

@Database(
    entities = [
        BooksEntity::class,
        ReadBookStatusEntity::class,
        BookDetailsEntity::class
    ],
    version = 1,
    exportSchema = true
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun bookDao(): BookDao
    abstract fun bookListDao(): ReadStatusDao
    abstract fun bookDetailsDao(): BookDetailsDao
}