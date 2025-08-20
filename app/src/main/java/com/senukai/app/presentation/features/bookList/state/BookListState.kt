package com.senukai.app.presentation.features.bookList.state

import com.senukai.app.domain.model.Book

sealed class BookListState {
    data object Idle: BookListState()
    data object Loading: BookListState()
    data class Success(val readStatusBook: List<Book>) : BookListState()
    data class Error(val message: String?) : BookListState()
}
