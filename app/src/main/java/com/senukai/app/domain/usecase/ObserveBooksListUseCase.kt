package com.senukai.app.domain.usecase

import com.senukai.app.core.extensions.toDataResult
import com.senukai.app.core.result.DataResult
import com.senukai.app.data.entities.BooksEntity
import com.senukai.app.data.mapper.BookMapper
import com.senukai.app.di.IoDispatcher
import com.senukai.app.domain.model.Book
import com.senukai.app.domain.repository.BooksRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ObserveBooksListUseCase @Inject constructor(
    private val repository: BooksRepository,
    private val mapper: BookMapper,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) {
    operator fun invoke(): Flow<DataResult<List<Book>>> =
        repository.observeBooks()
            .map { mapper.toDomains(it) }
            .toDataResult()
            .flowOn(dispatcher)
}
