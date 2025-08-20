package com.senukai.app.data.remote.api

import com.senukai.app.data.model.BookDetailsDto
import com.senukai.app.data.model.BookDto
import com.senukai.app.data.model.ReadStatusDto
import retrofit2.http.GET
import retrofit2.http.Path

interface ApiService {
    @GET("book/{id}")
    suspend fun getBookDetails(@Path("id") id: Int): BookDetailsDto

    @GET("books")
    suspend fun getBooks(): List<BookDto>

    @GET("lists")
    suspend fun getLists(): List<ReadStatusDto>

}
