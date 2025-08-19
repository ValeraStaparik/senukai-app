package com.senukai.app.data.model

data class TaxonomyResponse(
    val kingdom: String?,
    val order: String?,
    val family: String?
) {
    companion object {
        fun mock() = TaxonomyResponse(
            kingdom = "Animalia",
            order = "Carnivora",
            family = "Felidae"
        )
    }
}
