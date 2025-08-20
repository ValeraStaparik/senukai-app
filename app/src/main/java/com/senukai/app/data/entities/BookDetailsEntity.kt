package com.senukai.app.data.entities

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "book_details",
    indices = [Index("listId"), Index(value = ["title"])]
)
data class BookDetailsEntity(
    @PrimaryKey val id: Int,
    val listId: Int,
    val title: String,
    val image: String,
    val isbn: String? = null,
    val author: String? = null,
    val description: String? = null,
    val publicationDateIso: String? = null
)
