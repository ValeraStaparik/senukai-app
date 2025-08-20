package com.senukai.app.domain.model

import java.time.OffsetDateTime
import java.time.format.DateTimeParseException


data class BookDetails(
    val id: Int,
    val listId: Int,
    val isbn: String?,
    val publicationDate: OffsetDateTime?,
    val author: String?,
    val title: String,
    val image: String,
    val description: String?
){
    companion object {
        fun mock() = BookDetails(
            id = 1,
            listId = 2,
            isbn = "0684801221",
            publicationDate = "1995-05-05T00:00:00+03:00".toOffsetDateTimeOrNull(),
            author = "Ernest Hemingway",
            title = "The Old Man and the Sea",
            image = "https://covers.openlibrary.org/b/id/7884851-L.jpg",
            description = "The Old Man and the Sea is one of Hemingway's most enduring works."
        )

        fun mockList() = listOf(
            mock(),
            BookDetails(
                id = 2,
                listId = 3,
                isbn = "0743273567",
                publicationDate = "2004-09-30T00:00:00+03:00".toOffsetDateTimeOrNull(),
                author = "F. Scott Fitzgerald",
                title = "The Great Gatsby",
                image = "https://covers.openlibrary.org/b/id/9367345-L.jpg",
                description = "A portrait of the Jazz Age and the elusive American Dream."
            ),
            BookDetails(
                id = 12,
                listId = 1,
                isbn = "0441172717",
                publicationDate = "1965-08-01T00:00:00+03:00".toOffsetDateTimeOrNull(),
                author = "Frank Herbert",
                title = "Dune",
                image = "https://covers.openlibrary.org/b/id/12181264-L.jpg",
                description = "Epic sci-fi saga of politics, ecology, and prophecy on Arrakis."
            )
        )
    }
}

fun String.toOffsetDateTimeOrNull(): OffsetDateTime? =
    try { OffsetDateTime.parse(this) } catch (_: DateTimeParseException) { null }
