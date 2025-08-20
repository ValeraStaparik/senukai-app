package com.senukai.app.core.extensions

import com.senukai.app.core.result.DataResult
import kotlinx.coroutines.flow.*

fun <T> Flow<T>.toDataResult(): Flow<DataResult<T>> =
    this
        .map<T, DataResult<T>> { DataResult.Success(it) }
        .onStart { emit(DataResult.Loading) }
        .catch { t -> emit(DataResult.Failure(t as? Exception ?: Exception(t))) }

inline fun <T> resultFlow(crossinline block: suspend () -> T): Flow<DataResult<T>> =
    flow {
        emit(DataResult.Loading)
        emit(DataResult.Success(block()))
    }.catch { t -> emit(DataResult.Failure(t as? Exception ?: Exception(t))) }