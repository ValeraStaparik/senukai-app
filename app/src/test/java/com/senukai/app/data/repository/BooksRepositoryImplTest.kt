package com.senukai.app.data.repository

import com.senukai.app.data.entities.BookDetailsEntity
import com.senukai.app.data.entities.BooksEntity
import com.senukai.app.data.entities.ReadBookStatusEntity
import com.senukai.app.data.mapper.BookDetailsMapper
import com.senukai.app.data.mapper.BookMapper
import com.senukai.app.data.mapper.ReadBookStatusMapper
import com.senukai.app.data.model.BookDetailsDto
import com.senukai.app.data.model.BookDto
import com.senukai.app.data.model.ReadStatusDto
import com.senukai.app.data.source.local.BooksLocalDataSource
import com.senukai.app.data.source.remote.BooksRemoteDataSource
import io.mockk.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test

class BooksRepositoryImplTest {

    private val remote: BooksRemoteDataSource = mockk()
    private val local: BooksLocalDataSource = mockk()
    private val bookMapper: BookMapper = mockk()
    private val bookDetailsMapper: BookDetailsMapper = mockk()
    private val readBookStatusMapper: ReadBookStatusMapper = mockk()

    private lateinit var subject: BooksRepositoryImpl

    @Before
    fun setUp() {
        subject =
            BooksRepositoryImpl(remote, local, bookMapper, bookDetailsMapper, readBookStatusMapper)
    }

    @After
    fun tearDown() {
        clearAllMocks()
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `observeBooks should refresh when local is empty`() = runTest {
        // Given
        val dto = BookDto(id = 1, title = "Book", listId = 10, img = "img")
        val entity = BooksEntity(id = 1, title = "Book", listId = 10, image = "img")

        coEvery { local.observeBooks() } returns flowOf(emptyList())
        coEvery { remote.fetchBooks() } returns listOf(dto)
        coEvery { remote.fetchReadStatusList() } returns emptyList()
        every { bookMapper.toEntity(dto) } returns entity
        every { readBookStatusMapper.toEntity(any()) } returns mockk()
        coEvery { local.replaceAll(any(), any(), any()) } just Runs

        // When
        subject.observeBooks().collect { }

        // Then
        coVerify { remote.fetchBooks() }
        coVerify { local.replaceAll(listOf(entity), any(), any()) }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `observeBooksByList should refresh when local is empty`() = runTest {
        val listId = 10
        coEvery { local.observeBooksByList(listId) } returns flowOf(emptyList())
        coEvery { remote.fetchBooks() } returns emptyList()
        coEvery { remote.fetchReadStatusList() } returns emptyList()
        coEvery { local.replaceAll(any(), any(), any()) } just Runs

        subject.observeBooksByList(listId).collect { }

        coVerify { remote.fetchBooks() }
        coVerify { remote.fetchReadStatusList() }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `observeReadStatus should fetch from remote when empty`() = runTest {
        val dto = ReadStatusDto(id = 1, title = "List")
        val entity = ReadBookStatusEntity(id = 1, title = "List")

        coEvery { local.observeReadStatus() } returns flowOf(emptyList())
        coEvery { remote.fetchReadStatusList() } returns listOf(dto)
        every { readBookStatusMapper.toEntity(dto) } returns entity
        coEvery { local.upsertLists(listOf(entity)) } just Runs

        subject.observeReadStatus().collect { }

        coVerify { remote.fetchReadStatusList() }
        coVerify { local.upsertLists(listOf(entity)) }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `observeBookDetails should refresh when not cached`() = runTest {
        val bookId = 1
        val dto = BookDetailsDto(
            id = 1, listId = 10,
            title = "Book",
            imageUrl = "img",
            description = "desc"
        )
        val entity = BookDetailsEntity(
            id = 1, listId = 10,
            title = "Book",
            image = "img",
            description = "desc"
        )

        coEvery { local.observeBookDetails(bookId) } returns flowOf(null)
        coEvery { remote.fetchBookDetailsById(bookId) } returns dto
        every { bookDetailsMapper.toEntity(dto) } returns entity
        coEvery { local.upsertBookDetails(entity) } just Runs

        subject.observeBookDetails(bookId).collect { }

        coVerify { remote.fetchBookDetailsById(bookId) }
        coVerify { local.upsertBookDetails(entity) }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `refreshAll should fetch remote and update local`() = runTest {
        val dto = BookDto(id = 1, title = "Book", listId = 10, img = "img")
        val entity = BooksEntity(id = 1, title = "Book", listId = 10, image = "img")

        val readDto = ReadStatusDto(id = 1, title = "List")
        val readEntity = ReadBookStatusEntity(id = 1, title = "List")

        val detailsDto = BookDetailsDto(
            id = 1, listId = 10,
            title = "Book",
            imageUrl = "img",
            description = "desc"
        )
        val detailsEntity = BookDetailsEntity(
            id = 1, listId = 10,
            title = "Book",
            image = "img",
            description = "desc"
        )

        coEvery { remote.fetchBooks() } returns listOf(dto)
        coEvery { remote.fetchReadStatusList() } returns listOf(readDto)
        coEvery { remote.fetchBookDetailsById(dto.id) } returns detailsDto

        every { bookMapper.toEntity(dto) } returns entity
        every { readBookStatusMapper.toEntity(readDto) } returns readEntity
        every { bookDetailsMapper.toEntity(detailsDto) } returns detailsEntity

        coEvery {
            local.replaceAll(
                listOf(entity),
                listOf(readEntity),
                listOf(detailsEntity)
            )
        } just Runs

        subject.refreshAll()

        coVerify { local.replaceAll(listOf(entity), listOf(readEntity), listOf(detailsEntity)) }
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    @Test
    fun `refreshBookDetails should fetch remote and update local`() = runTest {
        val bookId = 1
        val dto = BookDetailsDto(
            id = 1,
            listId = 10,
            title = "Book",
            imageUrl = "img",
            description = "desc"
        )
        val entity = BookDetailsEntity(
            id = 1, listId = 10,
            title = "Book",
            image = "img",
            description = "desc"
        )

        coEvery { remote.fetchBookDetailsById(bookId) } returns dto
        every { bookDetailsMapper.toEntity(dto) } returns entity
        coEvery { local.upsertBookDetails(entity) } just Runs

        subject.refreshBookDetails(bookId)

        coVerify { local.upsertBookDetails(entity) }
    }
}
