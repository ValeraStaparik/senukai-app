package com.senukai.app.data.model

data class AnimalResponse(
    val name: String?,
    val taxonomy: TaxonomyResponse?,
    val location: String?,
    val speed: SpeedResponse?,
    val diet: String?,
    val lifespan: String?,
    val image: String?
) {
    companion object {
        fun mock() = AnimalResponse(
            name = "Lion",
            taxonomy = TaxonomyResponse.mock(),
            location = "Africa, India",
            speed = SpeedResponse.mock(),
            diet = "Carnivore",
            lifespan = "8 - 15 years",
            image = "lion.jpg"
        )

        fun mockList() = listOf(
            mock(),
            AnimalResponse(
                name = "Gorilla",
                taxonomy = TaxonomyResponse(
                    kingdom = "Animalia",
                    order = "Primates",
                    family = "Hominidae"
                ),
                location = "Africa",
                speed = SpeedResponse(
                    metric = "40",
                    imperial = "25"
                ),
                diet = "Herbivore",
                lifespan = "35-50 years",
                image = "gorilla.jpg"
            ),
            AnimalResponse(
                name = "Elephant",
                taxonomy = TaxonomyResponse(
                    kingdom = "Animalia",
                    order = "Proboscidea",
                    family = "Elephantidae"
                ),
                location = "Africa, Middle East, South Asia",
                speed = SpeedResponse(
                    metric = "40",
                    imperial = "25"
                ),
                diet = "Herbivore",
                lifespan = "55 - 70 years",
                image = "elephant.jpg"
            )
        )
    }
}
