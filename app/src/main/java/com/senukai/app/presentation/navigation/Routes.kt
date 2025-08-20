package com.senukai.app.presentation.navigation

import kotlinx.serialization.Serializable

@Serializable
object ReadingStatusBookList

@Serializable
data class BookListRoute(val bookId: Int)

@Serializable
data class BookDetailsRoute(val bookId: Int)
