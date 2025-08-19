package com.senukai.app.testutils.base

import com.senukai.app.testutils.json.JsonFilePaths
import com.senukai.app.testutils.rules.MainDispatcherRule
import com.senukai.app.testutils.rules.MockWebServerRule
import com.senukai.app.testutils.util.readJsonFromResource
import com.google.gson.GsonBuilder
import kotlinx.coroutines.ExperimentalCoroutinesApi
import okhttp3.HttpUrl
import okhttp3.mockwebserver.MockResponse
import org.junit.Before
import org.junit.Rule
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@OptIn(ExperimentalCoroutinesApi::class)
abstract class BaseServiceTest<T : Any> {
    @get:Rule
    val mockWebServerRule = MockWebServerRule()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    protected lateinit var subject: T

    abstract val serviceClass: Class<T>

    // region Setup

    @Before
    fun setUp() {
        subject = createRetrofit(mockWebServerRule.url()).create(serviceClass)
    }

    // endregion Setup

    protected fun enqueueResponse(
        fileName: String,
        code: Int = 200
    ) {
        val body = readJsonFromResource(fileName)
        mockWebServerRule.server.enqueue(
            MockResponse()
                .setResponseCode(code)
                .setBody(body)
        )
    }

    protected fun enqueueErrorResponse(
        fileName: String = JsonFilePaths.ERROR_GENERIC,
        code: Int = 500
    ) {
        val body = readJsonFromResource(fileName)
        mockWebServerRule.server.enqueue(
            MockResponse()
                .setResponseCode(code)
                .setBody(body)
        )
    }

    private fun createRetrofit(baseUrl: HttpUrl): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .build()
    }
}
