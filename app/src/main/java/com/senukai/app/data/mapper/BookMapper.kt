package com.senukai.app.data.mapper

import com.senukai.app.data.entities.BooksEntity
import com.senukai.app.data.model.BookDto
import com.senukai.app.domain.model.Book
import javax.inject.Inject

class BookMapper @Inject constructor() {

    fun toEntity(dto: BookDto): BooksEntity =
        BooksEntity(
            id = dto.id,
            listId = dto.listId,
            title = dto.title,
            image = dto.img
        )


    fun toDomain(entity: BooksEntity): Book =
        Book(
            id = entity.id,
            listId = entity.listId,
            title = entity.title,
            image = entity.image
        )

    fun toDomains(entities: List<BooksEntity>): List<Book> = entities.map(::toDomain)
}

