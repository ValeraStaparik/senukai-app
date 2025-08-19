package com.senukai.app.presentation.features.animal_details.intent

sealed class AnimalDetailsIntent {
    data object LoadAnimalDetails : AnimalDetailsIntent()
}
