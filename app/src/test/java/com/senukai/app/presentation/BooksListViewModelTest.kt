package com.senukai.app.presentation

import androidx.lifecycle.SavedStateHandle
import com.senukai.R
import com.senukai.app.core.ResourceProvider
import com.senukai.app.core.result.DataResult
import com.senukai.app.domain.model.Book
import com.senukai.app.domain.model.ReadStatusBook
import com.senukai.app.domain.usecase.ObserveBooksListUseCase
import com.senukai.app.domain.usecase.ObserveReadStatusBookUseCase
import com.senukai.app.presentation.features.reading_book_status.BooksListViewModel
import com.senukai.app.presentation.features.reading_book_status.intent.BooksListIntent
import com.senukai.app.presentation.features.reading_book_status.state.BooksListState
import com.senukai.app.testutils.extensions.sendIntentAndAwait
import com.senukai.app.testutils.rules.MainDispatcherRule
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals

class BooksListViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val resourceProvider: ResourceProvider = mockk()
    private val observeReadStatusBookUseCase: ObserveReadStatusBookUseCase = mockk()
    private val observeBooksListUseCase: ObserveBooksListUseCase = mockk()

    private lateinit var subject: BooksListViewModel

    @Before
    fun setUp() {
        subject = BooksListViewModel(
            resourceProvider = resourceProvider,
            observeReadStatusBookUseCase = observeReadStatusBookUseCase,
            observeBooksListUseCase = observeBooksListUseCase
        )
    }

    @After
    fun tearDown() {
        clearAllMocks()
    }

    @Test
    fun `onInit should emit Loading state`() = runTest(mainDispatcherRule.dispatcher) {
        // Given
        coEvery { observeReadStatusBookUseCase() } returns flowOf(DataResult.Loading)
        coEvery { observeBooksListUseCase() } returns flowOf(DataResult.Loading)

        // When
        subject.onInit()
        sendIntentAndAwait(subject.uiIntentChannel, BooksListIntent.LoadBooks)

        // Then
        assertEquals(BooksListState.Loading, subject.state.value)
    }

    @Test
    fun `on LoadBooks intent should emit Success state`() = runTest(mainDispatcherRule.dispatcher) {
        // Given
        val readStatuses = listOf(ReadStatusBook(1, "Favorites"))
        val books = listOf(Book(10, listId = 1, title = "Test Book", image = "url"))

        coEvery { observeReadStatusBookUseCase() } returns flowOf(DataResult.Success(readStatuses))
        coEvery { observeBooksListUseCase() } returns flowOf(DataResult.Success(books))

        // When
        subject.onInit()
        sendIntentAndAwait(subject.uiIntentChannel, BooksListIntent.LoadBooks)

        // Then
        val state = subject.state.value as BooksListState.Success
        assertEquals(1, state.bookList.size)
        assertEquals("Favorites", state.bookList.first().title)
        assertEquals("Test Book", state.bookList.first().books.first().title)
    }

    @Test
    fun `on LoadBooks intent should emit Error state when read statuses fail`() = runTest(mainDispatcherRule.dispatcher) {
        // Given
        val errorMessage = "Failed to load read statuses"
        coEvery { observeReadStatusBookUseCase() } returns flowOf(DataResult.Failure(RuntimeException(errorMessage)))
        coEvery { observeBooksListUseCase() } returns flowOf(DataResult.Loading)

        every { resourceProvider.getString(any()) } returns errorMessage

        // When
        subject.onInit()
        sendIntentAndAwait(subject.uiIntentChannel, BooksListIntent.LoadBooks)

        // Then
        assertEquals(BooksListState.Error(errorMessage), subject.state.value)
    }

    @Test
    fun `on LoadBooks intent should emit Error state when books fail`() = runTest(mainDispatcherRule.dispatcher) {
        // Given
        val errorMessage = "Failed to load books"
        coEvery { observeReadStatusBookUseCase() } returns flowOf(DataResult.Loading)
        coEvery { observeBooksListUseCase() } returns flowOf(DataResult.Failure(RuntimeException(errorMessage)))

        every { resourceProvider.getString(any()) } returns errorMessage

        // When
        subject.onInit()
        sendIntentAndAwait(subject.uiIntentChannel, BooksListIntent.LoadBooks)

        // Then
        assertEquals(BooksListState.Error(errorMessage), subject.state.value)
    }
}
