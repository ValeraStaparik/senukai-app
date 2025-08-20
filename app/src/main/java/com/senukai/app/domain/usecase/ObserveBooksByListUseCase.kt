package com.senukai.app.domain.usecase

import com.senukai.app.core.extensions.toDataResult
import com.senukai.app.core.result.DataResult
import com.senukai.app.domain.model.Book
import com.senukai.app.data.mapper.BookMapper
import com.senukai.app.di.IoDispatcher
import com.senukai.app.domain.repository.BooksRepository
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class ObserveBooksByListUseCase @Inject constructor(
    private val repository: BooksRepository,
    private val mapper: BookMapper,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) {
    operator fun invoke(listId: Int): Flow<DataResult<List<Book>>> =
        repository.observeBooksByList(listId)
            .map { mapper.toDomains(it) }
            .toDataResult()
            .flowOn(dispatcher)
}