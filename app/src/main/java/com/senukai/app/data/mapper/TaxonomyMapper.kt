package com.senukai.app.data.mapper

import com.senukai.app.data.model.TaxonomyResponse
import com.senukai.app.domain.model.Taxonomy

fun TaxonomyResponse.toTaxonomy() = Taxonomy(
    kingdom = kingdom.orEmpty(),
    order = kingdom.orEmpty(),
    family = kingdom.orEmpty(),
)
