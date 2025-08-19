package com.senukai.app.di

import com.senukai.app.data.repository.AnimalRepositoryImpl
import com.senukai.app.data.source.remote.AnimalDataSource
import com.senukai.app.domain.repository.AnimalRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Singleton
    @Provides
    fun provideAnimalRepository(remoteDataSource: AnimalDataSource): AnimalRepository =
        AnimalRepositoryImpl(remoteDataSource = remoteDataSource)
}