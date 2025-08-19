package com.senukai.app.data.remote.api

import com.senukai.app.core.utils.ApiUtil
import com.senukai.app.data.model.AnimalResponse
import retrofit2.http.GET

interface AnimalService {
    @GET(ApiUtil.ANIMALS_API)
    suspend fun getAnimals(): List<AnimalResponse>
}
