package com.senukai.app.domain.repository

import com.senukai.app.domain.model.Animal
import com.senukai.app.domain.model.AnimalDetails

interface AnimalRepository {
    suspend fun getAnimals(): List<Animal>
    suspend fun getAnimalByName(name: String): AnimalDetails?
}
