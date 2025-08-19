package com.senukai.app.presentation

import com.senukai.app.core.ResourceProvider
import com.senukai.app.core.result.DataResult
import com.senukai.app.domain.model.Animal
import com.senukai.app.domain.usecase.GetAnimalsUseCase
import com.senukai.app.presentation.features.animal_list.AnimalListViewModel
import com.senukai.app.presentation.features.animal_list.intent.AnimalListIntent
import com.senukai.app.presentation.features.animal_list.state.AnimalListState
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

class AnimalListViewModelTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val resourceProvider: ResourceProvider = mockk()
    private val getAnimalsUseCase: GetAnimalsUseCase = mockk()
    private lateinit var subject: AnimalListViewModel

    // region Setup

    @Before
    fun setUp() {
        subject = AnimalListViewModel(
            resourceProvider = resourceProvider,
            getAnimalsUseCase = getAnimalsUseCase
        )
    }

    @After
    fun tearDown() {
        clearAllMocks()
    }

    // endregion Setup

    @Test
    fun `onInit should emit Idle state`() =
        runTest(mainDispatcherRule.dispatcher) {
            // Given
            val expected = AnimalListState.Idle
            coEvery { getAnimalsUseCase.invoke() } returns flowOf(DataResult.Loading)

            // When
            subject.onInit()

            // Then
            assertEquals(expected, subject.state.value)
        }

    @Test
    fun `on LoadAnimals intent should emit Loading state`() =
        runTest(mainDispatcherRule.dispatcher) {
            // Given
            subject.onInit()
            val expected = AnimalListState.Loading
            coEvery { getAnimalsUseCase.invoke() } returns flowOf(DataResult.Loading)

            // When
            sendIntentAndAwait(subject.uiIntentChannel, AnimalListIntent.LoadAnimals)

            // Then
            assertEquals(expected, subject.state.value)
        }

    @Test
    fun `on LoadAnimals intent should emit Loading and then Success state`() =
        runTest(mainDispatcherRule.dispatcher) {
            // Given
            subject.onInit()
            val animals = Animal.mockList()
            val flow = flowOf(
                DataResult.Loading,
                DataResult.Success(animals)
            )
            val expected = AnimalListState.Success(animals)
            coEvery { getAnimalsUseCase.invoke() } returns flow

            // When
            sendIntentAndAwait(subject.uiIntentChannel, AnimalListIntent.LoadAnimals)

            // Then
            assertEquals(expected, subject.state.value)
        }

    @Test
    fun `on LoadAnimals intent should emit Error state when use case fails`() =
        runTest(mainDispatcherRule.dispatcher) {
            // Given
            subject.onInit()
            val errorMessage = "Unexpected error"
            val exception = RuntimeException(errorMessage)
            val flow = flowOf(
                DataResult.Loading,
                DataResult.Failure(exception)
            )
            val expected = AnimalListState.Error(errorMessage)
            coEvery { getAnimalsUseCase.invoke() } returns flow

            // When
            sendIntentAndAwait(subject.uiIntentChannel, AnimalListIntent.LoadAnimals)

            // Then
            assertEquals(expected, subject.state.value)
        }

    @Test
    fun `on LoadAnimals intent should emit fallback error message when message is null`() =
        runTest(mainDispatcherRule.dispatcher) {
            // Given
            subject.onInit()
            val errorMessage = "Unexpected error"
            val exception = RuntimeException()
            val flow = flowOf(
                DataResult.Loading,
                DataResult.Failure(exception)
            )
            val expected = AnimalListState.Error(errorMessage)
            coEvery { getAnimalsUseCase.invoke() } returns flow
            every { resourceProvider.getString(any()) } returns errorMessage

            // When
            sendIntentAndAwait(subject.uiIntentChannel, AnimalListIntent.LoadAnimals)

            // Then
            assertEquals(expected, subject.state.value)
        }
}
