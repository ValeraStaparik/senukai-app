package com.senukai.app.di

import com.senukai.app.domain.repository.AnimalRepository
import com.senukai.app.domain.usecase.GetAnimalByNameUseCase
import com.senukai.app.domain.usecase.GetAnimalByNameUseCaseImpl
import com.senukai.app.domain.usecase.GetAnimalsUseCase
import com.senukai.app.domain.usecase.GetAnimalsUseCaseImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object UseCaseModule {
    @Singleton
    @Provides
    fun provideGetAnimalsUseCase(repository: AnimalRepository): GetAnimalsUseCase =
        GetAnimalsUseCaseImpl(repository = repository)

    @Singleton
    @Provides
    fun provideGetAnimalByNameUseCase(repository: AnimalRepository): GetAnimalByNameUseCase =
        GetAnimalByNameUseCaseImpl(repository = repository)
}
