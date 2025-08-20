package com.senukai.app.domain.model

data class Book(
    val id: Int,
    val listId: Int,
    val title: String,
    val image: String
) {
    companion object {
        fun mock() = Book(
            id = 1,
            listId = 2,
            title = "The old man and the sea",
            image = "https://covers.openlibrary.org/b/id/7884851-L.jpg"
        )

        fun mockList() = listOf(
            Book(1, 2, "The old man and the sea", "https://covers.openlibrary.org/b/id/7884851-L.jpg"),
            Book(2, 3, "The Great Gatsby", "https://covers.openlibrary.org/b/id/9367345-L.jpg"),
            Book(3, 3, "Moby Dick", "https://covers.openlibrary.org/b/id/11681548-L.jpg"),
            Book(4, 2, "The grapes of wrath", "https://covers.openlibrary.org/b/id/9292315-L.jpg"),
            Book(5, 3, "Invisible Man", "https://covers.openlibrary.org/b/id/9367108-L.jpg"),
            Book(6, 2, "The Lord of the Rings", "https://covers.openlibrary.org/b/id/8314545-L.jpg"),
            Book(7, 3, "A Clockwork Orange", "https://covers.openlibrary.org/b/id/8401469-L.jpg"),
            Book(8, 2, "Of Mice and Men", "https://covers.openlibrary.org/b/id/8465280-L.jpg"),
            Book(9, 2, "The Stand", "https://covers.openlibrary.org/b/id/8579743-L.jpg"),
            Book(10, 1, "The Hitchhikers Guide to the Galaxy", "https://covers.openlibrary.org/b/id/11464688-L.jpg"),
            Book(11, 2, "Harry Potter and the Philosopher's Stone", "https://covers.openlibrary.org/b/id/12376736-L.jpg"),
            Book(12, 1, "Dune", "https://covers.openlibrary.org/b/id/12181264-L.jpg")
        )
    }
}

