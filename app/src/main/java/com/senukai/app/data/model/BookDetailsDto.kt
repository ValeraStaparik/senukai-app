package com.senukai.app.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class BookDetailsDto(
    val id: Int,
    @SerialName("list_id") val listId: Int,
    val isbn: String? = null,
    @SerialName("publication_date") val publicationDateIso: String? = null,
    val author: String? = null,
    val title: String,
    @SerialName("img") val imageUrl: String,
    val description: String? = null
)