package com.senukai.app.di

import com.senukai.app.data.remote.api.AnimalService
import com.senukai.app.data.source.remote.AnimalDataSource
import com.senukai.app.data.source.remote.AnimalDataSourceImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DataSourceModule {
    @Singleton
    @Provides
    fun provideAnimalDataSource(service: AnimalService): AnimalDataSource =
        AnimalDataSourceImpl(service = service)
}
