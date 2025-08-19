package com.senukai.app.presentation.features.animal_list.state

import com.senukai.app.domain.model.Animal

sealed class AnimalListState {
    data object Idle: AnimalListState()
    data object Loading: AnimalListState()
    data class Success(val animalList: List<Animal>) : AnimalListState()
    data class Error(val message: String?) : AnimalListState()
}
