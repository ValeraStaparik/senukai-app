package com.senukai.app.presentation.features.book_details

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.senukai.app.core.extensions.safeSend
import com.senukai.app.domain.model.BookDetails
import com.senukai.app.presentation.features.book_details.intent.BookDetailsIntent
import com.senukai.app.presentation.features.book_details.state.BookDetailsState

@Composable
fun BookDetailsScreen(
    onNavigateBack: () -> Unit,
) {
    val viewModel: BookDetailsViewModel = hiltViewModel()
    val uiState = viewModel.state.value

    LaunchedEffect(Unit) {
        viewModel.onInit()
    }

    BookDetailsScreen(
        uiState = uiState,
        onNavigateBack = onNavigateBack,
        onRetry = {
            viewModel.uiIntentChannel.safeSend(BookDetailsIntent.LoadBookDetails)
        }
    )
}

@Composable
private fun BookDetailsScreen(
    uiState: BookDetailsState,
    onNavigateBack: () -> Unit,
    onRetry: () -> Unit
) {
    BookDetailsContent(
        uiState = uiState,
        onNavigationBack = onNavigateBack,
        onRetry = onRetry
    )
}

@Preview
@Composable
private fun SuccessPreview() {
    BookDetailsContent(
        uiState = BookDetailsState.Success(BookDetails.mock()),
        onNavigationBack = {},
        onRetry = {}
    )
}

@Preview
@Composable
private fun LoadingPreview() {
    BookDetailsContent(
        uiState = BookDetailsState.Loading,
        onNavigationBack = {},
        onRetry = {}
    )
}

@Preview
@Composable
private fun ErrorPreview() {
    BookDetailsContent(
        uiState = BookDetailsState.Error(null),
        onNavigationBack = {},
        onRetry = {}
    )
}
