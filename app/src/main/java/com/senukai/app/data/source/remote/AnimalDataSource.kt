package com.senukai.app.data.source.remote

import com.senukai.app.data.model.AnimalResponse
import com.senukai.app.data.remote.api.AnimalService
import javax.inject.Inject

interface AnimalDataSource {
    suspend fun getAnimals(): List<AnimalResponse>
}

class AnimalDataSourceImpl @Inject constructor(
    private val service: AnimalService
) : AnimalDataSource {
    override suspend fun getAnimals(): List<AnimalResponse> = service.getAnimals()
}
