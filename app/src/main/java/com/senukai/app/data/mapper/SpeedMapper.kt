package com.senukai.app.data.mapper

import com.senukai.app.data.model.SpeedResponse
import com.senukai.app.domain.model.Speed

fun SpeedResponse.toSpeed() = Speed(
    metric = metric.orEmpty(),
    imperial = imperial.orEmpty()
)
