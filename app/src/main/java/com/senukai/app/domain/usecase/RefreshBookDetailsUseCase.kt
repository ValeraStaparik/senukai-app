package com.senukai.app.domain.usecase

import com.senukai.app.core.extensions.resultFlow
import com.senukai.app.core.result.DataResult
import com.senukai.app.di.IoDispatcher
import com.senukai.app.domain.repository.BooksRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

class RefreshBookDetailsUseCase @Inject constructor(
    private val repository: BooksRepository,
    @IoDispatcher private val dispatcher: CoroutineDispatcher

) {
    operator fun invoke(bookId: Int): Flow<DataResult<Unit>> =
        resultFlow { repository.refreshBookDetails(bookId) }.flowOn(dispatcher)
}