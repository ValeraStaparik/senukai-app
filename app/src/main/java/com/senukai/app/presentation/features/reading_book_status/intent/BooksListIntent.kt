package com.senukai.app.presentation.features.reading_book_status.intent

sealed class BooksListIntent {
    data object LoadBooks : BooksListIntent()
}
