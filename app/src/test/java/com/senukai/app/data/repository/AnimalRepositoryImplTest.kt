package com.senukai.app.data.repository

import com.senukai.app.data.mapper.toAnimalDetails
import com.senukai.app.data.mapper.toAnimalList
import com.senukai.app.data.model.AnimalResponse
import com.senukai.app.data.source.remote.AnimalDataSource
import com.senukai.app.domain.repository.AnimalRepository
import com.senukai.app.testutils.rules.MainDispatcherRule
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertNull

class AnimalRepositoryImplTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val remoteDataSource: AnimalDataSource = mockk()
    private lateinit var subject: AnimalRepository

    // region Setup

    @Before
    fun setUp() {
        subject = AnimalRepositoryImpl(
            remoteDataSource = remoteDataSource,
        )
    }

    @After
    fun tearDown() {
        clearAllMocks()
    }

    // endregion Setup

    @Test
    fun `getAnimals returns mapped list from remote source`() =
        runTest(mainDispatcherRule.dispatcher) {
            // Given
            val remoteResponse = AnimalResponse.mockList()
            val expected = remoteResponse.toAnimalList()
            coEvery { remoteDataSource.getAnimals() } returns remoteResponse

            // When
            val result = subject.getAnimals()

            // Then
            assertEquals(expected, result)
            coVerify(exactly = 1) { remoteDataSource.getAnimals() }
        }

    @Test
    fun `getAnimals throws exception when remote source fails`() = runTest(mainDispatcherRule.dispatcher) {
        // Given
        val expectedMessage = "Remote error"
        coEvery { remoteDataSource.getAnimals() } throws RuntimeException(expectedMessage)

        // When
        val thrown = assertFailsWith<RuntimeException> {
            subject.getAnimals()
        }

        // Then
        assertEquals(expectedMessage, thrown.message)
        coVerify(exactly = 1) { remoteDataSource.getAnimals() }
    }

    @Test
    fun `getAnimalByName returns mapped object when found`() =
        runTest(mainDispatcherRule.dispatcher) {
            // Given
            val targetName = "Lion"
            val remoteResponse = AnimalResponse.mockList()
            val expected = remoteResponse.find { it.name.equals(targetName, ignoreCase = true) }
                ?.toAnimalDetails()
            coEvery { remoteDataSource.getAnimals() } returns remoteResponse

            // When
            val result = subject.getAnimalByName(targetName)

            // Then
            assertEquals(expected, result)
            coVerify(exactly = 1) { remoteDataSource.getAnimals() }
        }

    @Test
    fun `getAnimalByName returns null when not found`() = runTest(mainDispatcherRule.dispatcher) {
        // Given
        val unknownName = "Unknown"
        coEvery { remoteDataSource.getAnimals() } returns AnimalResponse.mockList()

        // When
        val result = subject.getAnimalByName(unknownName)

        // Then
        assertNull(result)
        coVerify(exactly = 1) { remoteDataSource.getAnimals() }
    }

    @Test
    fun `getAnimalByName throws exception when remote source fails`() = runTest(mainDispatcherRule.dispatcher) {
        // Given
        val targetName = "Lion"
        val expectedMessage = "Remote error"
        coEvery { remoteDataSource.getAnimals() } throws RuntimeException(expectedMessage)

        // When
        val thrown = assertFailsWith<RuntimeException> {
            subject.getAnimalByName(targetName)
        }

        // Then
        assertEquals(expectedMessage, thrown.message)
        coVerify(exactly = 1) { remoteDataSource.getAnimals() }
    }
}
