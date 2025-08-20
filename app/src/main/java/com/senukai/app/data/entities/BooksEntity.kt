package com.senukai.app.data.entities

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "books",
    indices = [
        Index("listId"),
        Index(value = ["title"])
    ]
)
data class BooksEntity(
    @PrimaryKey val id: Int,
    val listId: Int,
    val title: String,
    val image: String
)