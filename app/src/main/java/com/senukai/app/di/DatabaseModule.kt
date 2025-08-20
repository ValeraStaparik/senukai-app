package com.senukai.app.di

import android.content.Context
import androidx.room.Room
import com.senukai.app.core.AppDatabase
import com.senukai.app.data.dao.BookDao
import com.senukai.app.data.dao.BookDetailsDao
import com.senukai.app.data.dao.ReadStatusDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): AppDatabase =
        Room.databaseBuilder(context, AppDatabase::class.java, "app.db")
            .build()

    @Provides fun provideBookDao(db: AppDatabase): BookDao = db.bookDao()
    @Provides fun provideBookListDao(db: AppDatabase): ReadStatusDao = db.bookListDao()
    @Provides fun provideBookDetailsDao(db: AppDatabase): BookDetailsDao = db.bookDetailsDao()

}