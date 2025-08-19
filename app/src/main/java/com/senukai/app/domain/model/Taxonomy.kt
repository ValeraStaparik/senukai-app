package com.senukai.app.domain.model

data class Taxonomy(
    val kingdom: String,
    val order: String,
    val family: String
) {
    companion object {
        fun mock() = Taxonomy(
            kingdom = "Animalia",
            order = "Carnivora",
            family = "Felidae"
        )
    }
}
