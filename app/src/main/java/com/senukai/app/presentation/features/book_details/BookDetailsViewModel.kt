package com.senukai.app.presentation.features.book_details

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.senukai.R
import com.senukai.app.core.ResourceProvider
import com.senukai.app.core.extensions.dataOrNull
import com.senukai.app.core.extensions.exceptionOrNull
import com.senukai.app.core.extensions.isLoading
import com.senukai.app.core.extensions.isSuccess
import com.senukai.app.core.extensions.safeSend
import com.senukai.app.domain.usecase.ObserveBookDetailsUseCase
import com.senukai.app.presentation.features.book_details.intent.BookDetailsIntent
import com.senukai.app.presentation.features.book_details.state.BookDetailsState
import com.senukai.app.presentation.navigation.BookDetailsRoute
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class BookDetailsViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val resourceProvider: ResourceProvider,
    private val getBookDetailsUseCase: ObserveBookDetailsUseCase
) : ViewModel() {

    private val route = savedStateHandle.toRoute<BookDetailsRoute>()

    val uiIntentChannel = Channel<BookDetailsIntent>(Channel.BUFFERED)

    private val _state = mutableStateOf<BookDetailsState>(BookDetailsState.Loading)
    val state: State<BookDetailsState> = _state

    fun onInit() {
        viewModelScope.launch {
            collectIntents()
        }

        uiIntentChannel.safeSend(BookDetailsIntent.LoadBookDetails)
    }

    private suspend fun collectIntents() {
        uiIntentChannel.consumeAsFlow()
            .catch { e -> _state.value = BookDetailsState.Error(e.message) }
            .collect { intent ->
                when (intent) {
                    is BookDetailsIntent.LoadBookDetails -> loadBookDetails()
                }
            }
    }

    private suspend fun loadBookDetails() {
        getBookDetailsUseCase(route.bookId).collect { result ->
            _state.value = when {
                result.isLoading -> BookDetailsState.Loading
                result.isSuccess -> {
                    result.dataOrNull?.let {
                        BookDetailsState.Success(it)
                    } ?: BookDetailsState.Error(null)
                }

                else -> BookDetailsState.Error(
                    result.exceptionOrNull?.message
                        ?: resourceProvider.getString(R.string.unknown_error)
                )
            }
        }
    }
}
