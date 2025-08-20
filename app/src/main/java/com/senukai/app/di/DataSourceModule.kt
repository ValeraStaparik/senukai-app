package com.senukai.app.di

import com.senukai.app.data.remote.api.ApiService
import com.senukai.app.data.source.local.BooksLocalDataSource
import com.senukai.app.data.source.local.BooksLocalDataSourceImpl
import com.senukai.app.data.source.remote.BooksRemoteDataSource
import com.senukai.app.data.source.remote.BooksRemoteDataSourceImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DataSourceModule {

    @Binds
    @Singleton
    abstract fun bindBooksRemoteDataSource(impl: BooksRemoteDataSourceImpl): BooksRemoteDataSource

    @Binds
    @Singleton
    abstract fun bindBooksLocalDataSource(impl: BooksLocalDataSourceImpl): BooksLocalDataSource
}
