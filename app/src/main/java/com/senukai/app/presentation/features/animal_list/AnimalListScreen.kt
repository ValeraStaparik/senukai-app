package com.senukai.app.presentation.features.animal_list

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.senukai.app.core.extensions.navigateIfNotCurrent
import com.senukai.app.core.extensions.popBackStackOrFinish
import com.senukai.app.core.extensions.safeSend
import com.senukai.app.domain.model.Animal
import com.senukai.app.presentation.features.animal_list.intent.AnimalListIntent
import com.senukai.app.presentation.features.animal_list.state.AnimalListState
import com.senukai.app.presentation.navigation.Screens

@Composable
fun AnimalListScreen(navController: NavController) {
    val viewModel: AnimalListViewModel = hiltViewModel()
    val uiState = viewModel.state.value

    // Triggered only once when the Composable is first composed
    LaunchedEffect(Unit) {
        viewModel.onInit()
    }

    AnimalListScreen(
        uiState = uiState,
        onNavigateBack = {
            navController.popBackStackOrFinish()
        },
        onRetry = {
            viewModel.uiIntentChannel.safeSend(AnimalListIntent.LoadAnimals)
        },
        onNavigateToDetails = { animalName ->
            navController.navigateIfNotCurrent(Screens.AnimalDetails.routeWithArgs(animalName))
        }
    )
}

@Composable
fun AnimalListScreen(
    uiState: AnimalListState,
    onNavigateBack: () -> Unit,
    onRetry: () -> Unit,
    onNavigateToDetails: (String) -> Unit
) {
    AnimalListContent(
        uiState = uiState,
        onNavigateBack = onNavigateBack,
        onRetry = onRetry,
        onNavigateToDetails = onNavigateToDetails
    )
}

// region Previews

@Preview
@Composable
private fun IdlePreview() {
    AnimalListScreen(
        uiState = AnimalListState.Idle,
        onNavigateBack = {},
        onRetry = {},
        onNavigateToDetails = {}
    )
}

@Preview
@Composable
private fun SuccessPreview() {
    AnimalListScreen(
        uiState = AnimalListState.Success(animalList = Animal.mockList()),
        onNavigateBack = {},
        onRetry = {},
        onNavigateToDetails = {}
    )
}

@Preview
@Composable
private fun LoadingPreview() {
    AnimalListScreen(
        uiState = AnimalListState.Loading,
        onNavigateBack = {},
        onRetry = {},
        onNavigateToDetails = {}
    )
}

// endregion Previews
