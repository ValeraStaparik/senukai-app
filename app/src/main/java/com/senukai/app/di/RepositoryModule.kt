package com.senukai.app.di

import com.senukai.app.data.repository.BooksRepositoryImpl
import com.senukai.app.domain.repository.BooksRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    @Binds
    @Singleton
    abstract fun bindBooksRepository(impl: BooksRepositoryImpl): BooksRepository
}