package com.senukai.app.presentation

import androidx.lifecycle.SavedStateHandle
import com.senukai.app.core.ResourceProvider
import com.senukai.app.core.result.DataResult
import com.senukai.app.domain.model.AnimalDetails
import com.senukai.app.domain.usecase.GetAnimalByNameUseCase
import com.senukai.app.presentation.features.animal_details.AnimalDetailsViewModel
import com.senukai.app.presentation.features.animal_details.intent.AnimalDetailsIntent
import com.senukai.app.presentation.features.animal_details.state.AnimalDetailsState
import com.senukai.app.presentation.navigation.AnimalDetailsArgs
import com.senukai.app.testutils.extensions.sendIntentAndAwait
import com.senukai.app.testutils.rules.MainDispatcherRule
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals

class AnimalDetailsViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val resourceProvider: ResourceProvider = mockk()
    private val getAnimalByNameUseCase: GetAnimalByNameUseCase = mockk()
    private lateinit var subject: AnimalDetailsViewModel

    private val animalName = "Lion"
    private val params = GetAnimalByNameUseCase.Params(animalName)

    // region Setup

    @Before
    fun setUp() {
        subject = AnimalDetailsViewModel(
            savedStateHandle = SavedStateHandle(mapOf(AnimalDetailsArgs.NAME to animalName)),
            resourceProvider = resourceProvider,
            getAnimalByNameUseCase = getAnimalByNameUseCase
        )
    }

    @After
    fun tearDown() {
        clearAllMocks()
    }

    // endregion Setup

    @Test
    fun `onInit should emit Loading state`() =
        runTest(mainDispatcherRule.dispatcher) {
            // Given
            val expected = AnimalDetailsState.Loading
            coEvery { getAnimalByNameUseCase.invoke(params) } returns flowOf(DataResult.Loading)

            // When
            subject.onInit()

            // Then
            assertEquals(expected, subject.state.value)
        }

    @Test
    fun `on LoadAnimalDetails intent should emit Loading state`() =
        runTest(mainDispatcherRule.dispatcher) {
            // Given
            subject.onInit()
            val expected = AnimalDetailsState.Loading
            coEvery { getAnimalByNameUseCase.invoke(params) } returns flowOf(DataResult.Loading)

            // When
            sendIntentAndAwait(subject.uiIntentChannel, AnimalDetailsIntent.LoadAnimalDetails)

            // Then
            assertEquals(expected, subject.state.value)
        }

    @Test
    fun `on LoadAnimalDetails intent should emit Loading and then Success state`() =
        runTest(mainDispatcherRule.dispatcher) {
            // Given
            subject.onInit()
            val animalDetails = AnimalDetails.mock()
            val flow = flowOf(
                DataResult.Loading,
                DataResult.Success(animalDetails)
            )
            val expected = AnimalDetailsState.Success(animalDetails)
            coEvery { getAnimalByNameUseCase.invoke(params) } returns flow

            // When
            sendIntentAndAwait(subject.uiIntentChannel, AnimalDetailsIntent.LoadAnimalDetails)

            // Then
            assertEquals(expected, subject.state.value)
        }

    @Test
    fun `on LoadAnimalDetails intent should emit Error state when use case fails`() =
        runTest(mainDispatcherRule.dispatcher) {
            // Given
            subject.onInit()
            val errorMessage = "Animal not found"
            val exception = RuntimeException(errorMessage)
            val flow = flowOf(
                DataResult.Loading,
                DataResult.Failure(exception)
            )
            val expected = AnimalDetailsState.Error(errorMessage)
            coEvery { getAnimalByNameUseCase.invoke(params) } returns flow

            // When
            sendIntentAndAwait(subject.uiIntentChannel, AnimalDetailsIntent.LoadAnimalDetails)

            // Then
            assertEquals(expected, subject.state.value)
        }

    @Test
    fun `on LoadAnimalDetails intent should emit fallback error message when message is null`() =
        runTest(mainDispatcherRule.dispatcher) {
            // Given
            subject.onInit()
            val errorMessage = "Unexpected error"
            val exception = RuntimeException()
            val flow = flowOf(
                DataResult.Loading,
                DataResult.Failure(exception)
            )
            val expected = AnimalDetailsState.Error(errorMessage)
            coEvery { getAnimalByNameUseCase.invoke(params) } returns flow
            every { resourceProvider.getString(any()) } returns errorMessage

            // When
            sendIntentAndAwait(subject.uiIntentChannel, AnimalDetailsIntent.LoadAnimalDetails)

            // Then
            assertEquals(expected, subject.state.value)
        }
}
