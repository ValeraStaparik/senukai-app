package com.senukai.app.domain.usecase

import com.senukai.app.core.result.DataResult
import com.senukai.app.domain.model.AnimalDetails
import com.senukai.app.domain.repository.AnimalRepository
import com.senukai.app.testutils.rules.MainDispatcherRule
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals

class GetAnimalByNameUseCaseImplTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val repository: AnimalRepository = mockk()
    private lateinit var subject: GetAnimalByNameUseCase

    private val expectedLoading = DataResult.Loading

    // region Setup

    @Before
    fun setUp() {
        subject = GetAnimalByNameUseCaseImpl(
            repository = repository,
            dispatcher = mainDispatcherRule.dispatcher
        )
    }

    @After
    fun tearDown() {
        clearAllMocks()
    }

    // endregion Setup

    @Test
    fun `invoke should emit Loading and Success when repository returns animal by name`() =
        runTest(mainDispatcherRule.dispatcher) {
            // Given
            val name = "Lion"
            val animalDetails = AnimalDetails.mock()
            val params = GetAnimalByNameUseCase.Params(name)
            val expected = DataResult.Success(animalDetails)
            val expectedEmissionCount = 2
            coEvery { repository.getAnimalByName(name) } returns animalDetails

            // When
            val result = subject.invoke(params).toList()

            // Then
            assertEquals(expectedEmissionCount, result.size)
            assertEquals(expectedLoading, result.first())
            assertEquals(expected, result.last())
            coVerify(exactly = 1) { repository.getAnimalByName(name) }
        }

    @Test
    fun `invoke should emit Loading and Failure when repository throws exception`() =
        runTest(mainDispatcherRule.dispatcher) {
            // Given
            val name = "Unknown"
            val exception = RuntimeException("Animal not found")
            val expected = DataResult.Failure(exception)
            val params = GetAnimalByNameUseCase.Params(name)
            val expectedEmissionCount = 2
            coEvery { repository.getAnimalByName(name) } throws exception

            // When
            val result = subject.invoke(params).toList()

            // Then
            assertEquals(expectedEmissionCount, result.size)
            assertEquals(expectedLoading, result.first())
            assertEquals(expected, result.last())
            coVerify(exactly = 1) { repository.getAnimalByName(name) }
        }
}
