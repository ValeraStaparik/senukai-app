package com.senukai.app.presentation.features.reading_book_status

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.senukai.R
import com.senukai.app.core.ResourceProvider
import com.senukai.app.core.extensions.dataOrEmptyList
import com.senukai.app.core.extensions.exceptionOrNull
import com.senukai.app.core.extensions.isFailure
import com.senukai.app.core.extensions.isSuccess
import com.senukai.app.domain.model.Book
import com.senukai.app.domain.model.ReadStatusBook
import com.senukai.app.domain.usecase.ObserveBooksListUseCase
import com.senukai.app.domain.usecase.ObserveReadStatusBookUseCase
import com.senukai.app.presentation.features.reading_book_status.intent.BooksListIntent
import com.senukai.app.presentation.features.reading_book_status.state.BookUi
import com.senukai.app.presentation.features.reading_book_status.state.BooksListState
import com.senukai.app.presentation.features.reading_book_status.state.ReadStatusUiState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BooksListViewModel @Inject constructor(
    private val resourceProvider: ResourceProvider,
    private val observeReadStatusBookUseCase: ObserveReadStatusBookUseCase,
    private val observeBooksListUseCase: ObserveBooksListUseCase
) : ViewModel() {

    val uiIntentChannel = Channel<BooksListIntent>(Channel.BUFFERED)


    private val _state = mutableStateOf<BooksListState>(BooksListState.Idle)
    val state: State<BooksListState> = _state

    fun onInit() {
        viewModelScope.launch {
            collectIntents()
        }
        viewModelScope.launch {
            uiIntentChannel.send(BooksListIntent.LoadBooks)
        }
    }

    private suspend fun collectIntents() {
        uiIntentChannel.consumeAsFlow()
            .catch { e -> _state.value = BooksListState.Error(e.message) }
            .collect { intent ->
                when (intent) {
                    is BooksListIntent.LoadBooks -> loadBooks()
                }
            }
    }

    private suspend fun loadBooks() {
        combine(
            observeReadStatusBookUseCase(),
            observeBooksListUseCase()
        ) { readStatusResult, booksResult ->
            if (readStatusResult.isSuccess && booksResult.isSuccess) {
                val readStatuses = readStatusResult.dataOrEmptyList()
                val books = booksResult.dataOrEmptyList()
                BooksListState.Success(
                    makeBookReadStatusWithDetails(readStatuses, books)
                )
            } else if (readStatusResult.isFailure) {
                BooksListState.Error(
                    readStatusResult.exceptionOrNull?.message
                        ?: resourceProvider.getString(R.string.unknown_error)
                )
            } else if (booksResult.isFailure) {
                BooksListState.Error(
                    booksResult.exceptionOrNull?.message
                        ?: resourceProvider.getString(R.string.unknown_error)
                )
            } else {
                BooksListState.Loading
            }
        }.collect { newState ->
            _state.value = newState
        }
    }

    private fun makeBookReadStatusWithDetails(
        readStatusList: List<ReadStatusBook>,
        bookList: List<Book>
    ): List<ReadStatusUiState> {
        return readStatusList.map { readStatus ->
            val relatedBooks = bookList
                .filter { it.listId == readStatus.id }
                .take(5)

            ReadStatusUiState(
                id = readStatus.id,
                title = readStatus.title,
                books = relatedBooks.map { book ->
                    BookUi(
                        id = book.id,
                        title = book.title,
                        image = book.image
                    )
                }
            )
        }
    }
}
