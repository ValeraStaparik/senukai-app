package com.senukai.app.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "read_status_book")
data class ReadBookStatusEntity(
    @PrimaryKey
    val id: Int,
    val title: String
)