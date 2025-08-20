package com.senukai.app.presentation.features.reading_book_status

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.senukai.app.core.extensions.safeSend
import com.senukai.app.presentation.features.reading_book_status.intent.BooksListIntent
import com.senukai.app.presentation.features.reading_book_status.state.BooksListState

@Composable
fun BooksListScreen(
    onNavigateToBookList: (Int) -> Unit,
    onNavigateToBookDetails: (Int) -> Unit
) {
    val viewModel: BooksListViewModel = hiltViewModel()
    val uiState = viewModel.state.value

    LaunchedEffect(Unit) {
        viewModel.onInit()
    }

    BooksListContent(
        uiState = uiState,
        onRetry = { viewModel.uiIntentChannel.safeSend(BooksListIntent.LoadBooks) },
        onNavigateToDetails = onNavigateToBookDetails,
        onNavigateToFullList = onNavigateToBookList
    )
}


@Composable
private fun BooksListScreen(
    uiState: BooksListState,
    onRetry: () -> Unit,
    onNavigateToDetails: (Int) -> Unit
) {
    BooksListContent(
        uiState = uiState,
        onRetry = onRetry,
        onNavigateToDetails = onNavigateToDetails,
        onNavigateToFullList = {}
    )
}

@Preview
@Composable
private fun IdlePreview() {
    BooksListScreen(
        uiState = BooksListState.Idle,
        onRetry = {},
        onNavigateToDetails = {}
    )
}

@Preview
@Composable
private fun LoadingPreview() {
    BooksListScreen(
        uiState = BooksListState.Loading,
        onRetry = {},
        onNavigateToDetails = {}
    )
}
