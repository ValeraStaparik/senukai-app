package com.senukai.app.domain.usecase

import com.senukai.app.core.extensions.toDataResult
import com.senukai.app.core.result.DataResult
import com.senukai.app.data.mapper.ReadBookStatusMapper
import com.senukai.app.di.IoDispatcher
import com.senukai.app.domain.model.ReadStatusBook
import com.senukai.app.domain.repository.BooksRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ObserveReadStatusBookUseCase @Inject constructor(
    private val repository: BooksRepository,
    private val mapper: ReadBookStatusMapper,
    @IoDispatcher private val dispatcher: CoroutineDispatcher
) {
    operator fun invoke(): Flow<DataResult<List<ReadStatusBook>>> =
        repository.observeReadStatus()
            .map { entities -> entities.map(mapper::toDomain) }
            .toDataResult()
            .flowOn(dispatcher)
}