package com.senukai.app.data.mapper

import com.senukai.app.data.entities.BookDetailsEntity
import com.senukai.app.data.model.BookDetailsDto
import com.senukai.app.domain.model.BookDetails
import com.senukai.app.domain.model.toOffsetDateTimeOrNull
import javax.inject.Inject

class BookDetailsMapper @Inject constructor() {
    fun toEntity(dto: BookDetailsDto): BookDetailsEntity =
        BookDetailsEntity(
            id = dto.id,
            listId = dto.listId,
            title = dto.title,
            image = dto.imageUrl,
            isbn = dto.isbn,
            author = dto.author,
            description = dto.description,
            publicationDateIso = dto.publicationDateIso
        )

    fun toDomain(entity: BookDetailsEntity): BookDetails =
        BookDetails(
            id = entity.id,
            listId = entity.listId,
            isbn = entity.isbn,
            publicationDate = entity.publicationDateIso?.toOffsetDateTimeOrNull(),
            author = entity.author,
            title = entity.title,
            image = entity.image,
            description = entity.description
        )

}

