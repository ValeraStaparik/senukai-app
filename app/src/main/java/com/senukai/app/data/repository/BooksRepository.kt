package com.senukai.app.data.repository

import com.senukai.app.data.entities.BookDetailsEntity
import com.senukai.app.data.entities.BooksEntity
import com.senukai.app.data.entities.ReadBookStatusEntity
import com.senukai.app.data.mapper.BookDetailsMapper
import com.senukai.app.data.mapper.ReadBookStatusMapper
import com.senukai.app.data.mapper.BookMapper
import com.senukai.app.data.source.local.BooksLocalDataSource
import com.senukai.app.data.source.remote.BooksRemoteDataSource
import com.senukai.app.domain.repository.BooksRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.emitAll
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.transform
import kotlinx.coroutines.withContext
import javax.inject.Inject

class BooksRepositoryImpl @Inject constructor(
    private val remote: BooksRemoteDataSource,
    private val local: BooksLocalDataSource,
    private val bookMapper: BookMapper,
    private val bookDetailsMapper: BookDetailsMapper,
    private val readBookStatusMapper: ReadBookStatusMapper
) : BooksRepository {

    override fun observeBooks(): Flow<List<BooksEntity>> =
        local.observeBooks().transform { books ->
            if (books.isEmpty()) {
                refreshAll()
            }
            emit(books)
        }

    override fun observeBooksByList(listId: Int): Flow<List<BooksEntity>> =
        local.observeBooksByList(listId).transform { books ->
            if (books.isEmpty()) {
                refreshAll()
            }
            emit(books)
        }

    override fun observeReadStatus(): Flow<List<ReadBookStatusEntity>> =
        local.observeReadStatus().transform { statuses ->
            if (statuses.isEmpty()) {
                val readStatusList = remote.fetchReadStatusList()
                local.upsertLists(readStatusList.map(readBookStatusMapper::toEntity))
            }
            emit(statuses)
        }

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun observeBookDetails(bookId: Int): Flow<BookDetailsEntity?> =
        local.observeBookDetails(bookId).flatMapLatest { details ->
            if (details == null) {
                flow {
                    refreshBookDetails(bookId)
                }
            } else {
                flowOf(details)
            }
        }

    override suspend fun refreshAll() = withContext(Dispatchers.IO) {
        val bookDtos = remote.fetchBooks()
        val readStatusList = remote.fetchReadStatusList()

        val bookEntities = bookDtos.map(bookMapper::toEntity)
        val readBookStatus = readStatusList.map(readBookStatusMapper::toEntity)

        val detailsDtos = coroutineScope {
            bookDtos.map { dto ->
                async { runCatching { remote.fetchBookDetailsById(dto.id) }.getOrNull() }
            }.awaitAll().filterNotNull()
        }
        val detailEntities = detailsDtos.map(bookDetailsMapper::toEntity)

        local.replaceAll(
            books = bookEntities,
            readBookStatusEntityList = readBookStatus,
            details = detailEntities
        )
    }

    override suspend fun refreshBookDetails(bookId: Int) = withContext(Dispatchers.IO) {
        val dto = remote.fetchBookDetailsById(bookId)
        local.upsertBookDetails(bookDetailsMapper.toEntity(dto))
    }
}

