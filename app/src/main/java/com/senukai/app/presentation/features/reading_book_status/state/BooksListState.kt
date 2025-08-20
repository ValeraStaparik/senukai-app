package com.senukai.app.presentation.features.reading_book_status.state

sealed class BooksListState {
    data object Idle : BooksListState()
    data object Loading : BooksListState()
    data class Success(val bookList: List<ReadStatusUiState>) : BooksListState()
    data class Error(val message: String?) : BooksListState()
}

data class ReadStatusUiState(
    val id: Int,
    val title: String,
    val books: List<BookUi>
)

data class BookUi(
    val id: Int,
    val title: String,
    val image: String
)

