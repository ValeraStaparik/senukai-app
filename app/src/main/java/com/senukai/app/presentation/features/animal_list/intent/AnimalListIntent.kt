package com.senukai.app.presentation.features.animal_list.intent

sealed class AnimalListIntent {
    data object LoadAnimals : AnimalListIntent()
}
