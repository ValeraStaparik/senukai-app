package com.senukai.app.presentation.features.bookList.intent

sealed class BookListIntent {
    data object LoadBookList : BookListIntent()
}
