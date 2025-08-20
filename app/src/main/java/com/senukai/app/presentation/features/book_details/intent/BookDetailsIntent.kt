package com.senukai.app.presentation.features.book_details.intent

sealed class BookDetailsIntent {
    data object LoadBookDetails : BookDetailsIntent()
}
