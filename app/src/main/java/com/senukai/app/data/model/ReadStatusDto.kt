package com.senukai.app.data.model

import kotlinx.serialization.Serializable

@Serializable
data class ReadStatusDto(
    val id: Int,
    val title: String
)