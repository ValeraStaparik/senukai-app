package com.senukai.app.data.remote.api

import com.senukai.app.data.model.AnimalResponse
import com.senukai.app.testutils.base.BaseServiceTest
import com.senukai.app.testutils.json.JsonFilePaths
import kotlinx.coroutines.test.runTest
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFailsWith
import kotlin.test.assertTrue

class AnimalServiceTest : BaseServiceTest<AnimalService>() {
    override val serviceClass = AnimalService::class.java

    @Test
    fun `getAnimals parses response correctly`() = runTest(mainDispatcherRule.dispatcher) {
        // Given
        val expectedAnimalCount = 3
        val expectedFirstAnimalName = "Lion"
        val expectedFirstAnimalLocation = "Africa, India"
        val expectedAnimals = AnimalResponse.mockList()

        enqueueResponse(JsonFilePaths.ANIMALS_SUCCESS)

        // When
        val result = subject.getAnimals()

        // Then
        assertEquals(expectedAnimalCount, result.size)
        assertEquals(expectedFirstAnimalName, result.first().name)
        assertEquals(expectedFirstAnimalLocation, result.first().location)
        assertEquals(expectedAnimals, result)
    }

    @Test
    fun `getAnimals throws exception on server error`() = runTest(mainDispatcherRule.dispatcher) {
        // Given
        val expectedStatusCodeInMessage = "500"

        enqueueErrorResponse()

        // When
        val exception = assertFailsWith<Exception> {
            subject.getAnimals()
        }

        // Then
        assertTrue(exception.message?.contains(expectedStatusCodeInMessage) ?: false)
    }

    @Test
    fun `getAnimals returns empty list when response is empty`() =
        runTest(mainDispatcherRule.dispatcher) {
            // Given
            enqueueResponse(JsonFilePaths.EMPTY_LIST)

            // When
            val result = subject.getAnimals()

            // Then
            assertTrue(result.isEmpty())
        }
}
