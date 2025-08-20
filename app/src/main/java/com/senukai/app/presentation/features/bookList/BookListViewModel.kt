package com.senukai.app.presentation.features.bookList

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.senukai.R
import com.senukai.app.core.ResourceProvider
import com.senukai.app.core.extensions.dataOrEmptyList
import com.senukai.app.core.extensions.exceptionOrNull
import com.senukai.app.core.extensions.isFailure
import com.senukai.app.core.extensions.isLoading
import com.senukai.app.core.extensions.isSuccess
import com.senukai.app.domain.usecase.ObserveBooksByListUseCase
import com.senukai.app.presentation.features.bookList.intent.BookListIntent
import com.senukai.app.presentation.features.bookList.state.BookListState
import com.senukai.app.presentation.navigation.BookListRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookListViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val resourceProvider: ResourceProvider,
    private val getBookListUseCase: ObserveBooksByListUseCase
) : ViewModel() {

    val uiIntentChannel = Channel<BookListIntent>(Channel.BUFFERED)

    private val _state = mutableStateOf<BookListState>(BookListState.Idle)
    val state: State<BookListState> = _state

    private val route = savedStateHandle.toRoute<BookListRoute>()

    fun onInit() {
        viewModelScope.launch {
            collectIntents()
        }
        viewModelScope.launch {
            uiIntentChannel.send(BookListIntent.LoadBookList)
        }
    }

    private suspend fun collectIntents() {
        uiIntentChannel.consumeAsFlow()
            .catch { e -> _state.value = BookListState.Error(e.message) }
            .collect { intent ->
                when (intent) {
                    is BookListIntent.LoadBookList -> loadBooks()
                }
            }
    }

    private suspend fun loadBooks() {
        getBookListUseCase(route.bookId).collect { result ->
            _state.value = when {
                result.isLoading -> BookListState.Loading
                result.isSuccess -> BookListState.Success(result.dataOrEmptyList())
                result.isFailure -> BookListState.Error(
                    result.exceptionOrNull?.message
                        ?: resourceProvider.getString(R.string.unknown_error)
                )

                else -> BookListState.Idle
            }
        }
    }
}
