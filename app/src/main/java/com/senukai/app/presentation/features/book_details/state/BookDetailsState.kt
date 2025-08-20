package com.senukai.app.presentation.features.book_details.state

import com.senukai.app.domain.model.BookDetails

sealed class BookDetailsState {
    data object Loading: BookDetailsState()
    data class Success(val bookDetails: BookDetails) : BookDetailsState()
    data class Error(val message: String?) : BookDetailsState()
}
