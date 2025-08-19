package com.senukai.app.testutils.rules

import okhttp3.HttpUrl
import okhttp3.mockwebserver.MockWebServer
import org.junit.rules.TestWatcher
import org.junit.runner.Description

class MockWebServerRule : TestWatcher() {
    lateinit var server: MockWebServer
        private set

    override fun starting(description: Description) {
        super.starting(description)
        server = MockWebServer()
        server.start()
    }

    override fun finished(description: Description) {
        super.finished(description)
        server.shutdown()
    }

    fun url(path: String = "/"): HttpUrl = server.url(path)
}
