package com.senukai.app.data.repository

import com.senukai.app.data.mapper.toAnimalDetails
import com.senukai.app.data.mapper.toAnimalList
import com.senukai.app.data.source.remote.AnimalDataSource
import com.senukai.app.domain.model.Animal
import com.senukai.app.domain.model.AnimalDetails
import com.senukai.app.domain.repository.AnimalRepository
import javax.inject.Inject

class AnimalRepositoryImpl @Inject constructor(
    private val remoteDataSource: AnimalDataSource
) : AnimalRepository {
    override suspend fun getAnimals(): List<Animal> = remoteDataSource.getAnimals().toAnimalList()
    override suspend fun getAnimalByName(name: String): AnimalDetails? =
        remoteDataSource.getAnimals().find {
            it.name.equals(name, ignoreCase = true)
        }?.toAnimalDetails()
}
