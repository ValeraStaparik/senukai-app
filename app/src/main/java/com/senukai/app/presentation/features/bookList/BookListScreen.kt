package com.senukai.app.presentation.features.bookList

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.senukai.app.core.extensions.safeSend
import com.senukai.app.domain.model.Book
import com.senukai.app.presentation.features.bookList.intent.BookListIntent
import com.senukai.app.presentation.features.bookList.state.BookListState

@Composable
fun BookListScreen(
    onNavigateBack: () -> Unit,
    onNavigateToBook: (Int) -> Unit,
) {
    val viewModel: BookListViewModel = hiltViewModel()
    val uiState = viewModel.state.value

    LaunchedEffect(Unit) {
        viewModel.onInit()

    }

    BookListScreen(
        uiState = uiState,
        onNavigateBack = onNavigateBack,
        onRetry = {
            viewModel.uiIntentChannel.safeSend(BookListIntent.LoadBookList)
        },
        onNavigateToDetails = onNavigateToBook
    )
}

@Composable
private fun BookListScreen(
    uiState: BookListState,
    onNavigateBack: () -> Unit,
    onRetry: () -> Unit,
    onNavigateToDetails: (Int) -> Unit
) {
    BookListContent(
        uiState = uiState,
        onNavigateBack = onNavigateBack,
        onRetry = onRetry,
        onNavigateToDetails = onNavigateToDetails
    )
}

@Preview
@Composable
private fun IdlePreview() {
    BookListScreen(
        uiState = BookListState.Idle,
        onNavigateBack = {},
        onRetry = {},
        onNavigateToDetails = {}
    )
}

@Preview
@Composable
private fun SuccessPreview() {
    BookListScreen(
        uiState = BookListState.Success(readStatusBook = Book.mockList()),
        onNavigateBack = {},
        onRetry = {},
        onNavigateToDetails = {}
    )
}

@Preview
@Composable
private fun LoadingPreview() {
    BookListScreen(
        uiState = BookListState.Loading,
        onNavigateBack = {},
        onRetry = {},
        onNavigateToDetails = {}
    )
}
