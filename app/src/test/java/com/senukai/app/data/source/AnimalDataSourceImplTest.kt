package com.senukai.app.data.source

import com.senukai.app.data.model.AnimalResponse
import com.senukai.app.data.remote.api.AnimalService
import com.senukai.app.data.source.remote.AnimalDataSource
import com.senukai.app.data.source.remote.AnimalDataSourceImpl
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

class AnimalDataSourceImplTest {
    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private val service: AnimalService = mockk()
    private lateinit var subject: AnimalDataSource

    // region Setup

    @Before
    fun setUp() {
        subject = AnimalDataSourceImpl(service = service)
    }

    @After
    fun tearDown() {
        clearAllMocks()
    }

    // endregion Setup

    @Test
    fun `getAnimals should return list from service`() = runTest(mainDispatcherRule.dispatcher) {
        // Given
        val expected = AnimalResponse.mockList()
        coEvery { service.getAnimals() } returns expected

        // When
        val result = subject.getAnimals()

        // Then
        assertEquals(expected, result)
        coVerify(exactly = 1) { service.getAnimals() }
    }

    @Test
    fun `getAnimals should throw when service fails`() = runTest(mainDispatcherRule.dispatcher) {
        // Given
        val exceptionMessage = "API error"
        val exception = RuntimeException(exceptionMessage)
        coEvery { service.getAnimals() } throws exception

        // When
        val thrown = assertFailsWith<RuntimeException> {
            subject.getAnimals()
        }

        // Then
        assertEquals(exceptionMessage, thrown.message)
        coVerify(exactly = 1) { service.getAnimals() }
    }
}
