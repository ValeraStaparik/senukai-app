package com.senukai.app.data.mapper

import com.senukai.app.data.entities.ReadBookStatusEntity
import com.senukai.app.data.model.ReadStatusDto
import com.senukai.app.domain.model.ReadStatusBook
import javax.inject.Inject

class ReadBookStatusMapper @Inject constructor() {
    fun toDomain(dto: ReadStatusDto): ReadStatusBook =
        ReadStatusBook(id = dto.id, title = dto.title)

    fun toEntity(dto: ReadStatusDto): ReadBookStatusEntity =
        ReadBookStatusEntity(id = dto.id, title = dto.title)

    fun toDomain(entity: ReadBookStatusEntity): ReadStatusBook =
        ReadStatusBook(id = entity.id, title = entity.title)

}

