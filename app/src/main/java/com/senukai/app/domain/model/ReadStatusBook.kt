package com.senukai.app.domain.model

data class ReadStatusBook(
    val id: Int,
    val title: String
) {
    companion object {
        fun mock() = ReadStatusBook(
            id = 1,
            title = "Mystery"
        )

        fun mockList() = listOf(
            ReadStatusBook(
                id = 2,
                title = "Fantasy"
            ),
            ReadStatusBook(
                id = 3,
                title = "Science Fiction"
            )
        )
    }
}