package com.senukai.app.data.mapper

import com.senukai.app.data.model.AnimalResponse
import com.senukai.app.domain.model.Animal
import com.senukai.app.domain.model.AnimalDetails

fun List<AnimalResponse>.toAnimalList(): List<Animal> = map { it.toAnimal() }

fun AnimalResponse.toAnimal(): Animal = Animal(
    name = name.orEmpty(),
    location = location.orEmpty(),
    image = image.orEmpty()
)

fun AnimalResponse.toAnimalDetails() = AnimalDetails(
    name = name.orEmpty(),
    taxonomy = taxonomy?.toTaxonomy(),
    location = location.orEmpty(),
    speed = speed?.toSpeed(),
    diet = diet.orEmpty(),
    lifespan = lifespan.orEmpty(),
    image = image.orEmpty()
)
