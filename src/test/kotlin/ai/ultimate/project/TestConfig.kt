package ai.ultimate.project

import ai.ultimate.project.utils.TestConstants.INTENT_RESPONSE
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.RecordedRequest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Primary
import org.springframework.context.annotation.Profile
import org.springframework.http.MediaType.APPLICATION_JSON_VALUE
import javax.annotation.PreDestroy

@TestConfiguration
@Profile("test")
class TestConfig {

    private lateinit var mockServer: MockWebServer

    @Primary
    @Bean
    fun okHttpClient(): OkHttpClient {
        mockServer = MockWebServer()
        mockServer.start(8000)
        mockServer.dispatcher = createDispatcher()
        return OkHttpClient()
    }

    private fun createDispatcher() = object : Dispatcher() {
        override fun dispatch(request: RecordedRequest): MockResponse {
            if (request.method == "POST" && request.path == "/api/intents")
                return MockResponse()
                    .setBody(INTENT_RESPONSE)
                    .setResponseCode(200)
                    .setHeader("Content-Type", APPLICATION_JSON_VALUE)
            else
                return MockResponse().setResponseCode(404)
        }
    }

    @PreDestroy
    fun onDestroy() {
        mockServer.shutdown()
    }
}