package com.senukai.app.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BookDto(
    val id: Int,
    @SerialName("list_id") val listId: Int,
    val title: String,
    @SerialName("img") val img: String
)