package com.senukai.app.data.source.remote

import com.senukai.app.data.model.BookDetailsDto
import com.senukai.app.data.model.BookDto
import com.senukai.app.data.model.ReadStatusDto
import com.senukai.app.data.remote.api.ApiService
import javax.inject.Inject

interface BooksRemoteDataSource {
    suspend fun fetchBooks(): List<BookDto>
    suspend fun fetchReadStatusList(): List<ReadStatusDto>
    suspend fun fetchBookDetailsById(bookId: Int): BookDetailsDto
}


class BooksRemoteDataSourceImpl @Inject constructor(
    private val api: ApiService
) : BooksRemoteDataSource {

    override suspend fun fetchBooks(): List<BookDto> = api.getBooks()

    override suspend fun fetchReadStatusList(): List<ReadStatusDto> = api.getLists()

    override suspend fun fetchBookDetailsById(bookId: Int): BookDetailsDto = api.getBookDetails(bookId)
}