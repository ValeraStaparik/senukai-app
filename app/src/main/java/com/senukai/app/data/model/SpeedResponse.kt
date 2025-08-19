package com.senukai.app.data.model

data class SpeedResponse(
    val metric: String?,
    val imperial: String?
) {
    companion object {
        fun mock() = SpeedResponse(
            metric = "56",
            imperial = "35"
        )
    }
}
