package com.senukai.app.domain.usecase

import com.senukai.app.core.extensions.toDataResult
import com.senukai.app.core.result.DataResult
import com.senukai.app.domain.model.BookDetails
import com.senukai.app.data.mapper.BookDetailsMapper
import com.senukai.app.di.IoDispatcher
import com.senukai.app.domain.repository.BooksRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ObserveBookDetailsUseCase @Inject constructor(
    private val repository: BooksRepository,
    private val mapper: BookDetailsMapper,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) {
    operator fun invoke(bookId: Int): Flow<DataResult<BookDetails?>> =
        repository.observeBookDetails(bookId)
            .map { it?.let(mapper::toDomain) }
            .toDataResult()
            .flowOn(dispatcher)
}
