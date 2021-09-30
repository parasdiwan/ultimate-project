package ai.ultimate.project

import ai.ultimate.project.utils.TestConstants.BEST_INTENT
import ai.ultimate.project.utils.TestConstants.EMPTY_INTENT_RESPONSE
import ai.ultimate.project.utils.TestConstants.INTENT_RESPONSE
import ai.ultimate.project.utils.TestConstants.MESSAGE
import com.fasterxml.jackson.databind.DeserializationFeature
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.KotlinModule
import okhttp3.*
import okhttp3.ResponseBody.Companion.toResponseBody
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito.`when`
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType.APPLICATION_JSON_VALUE

internal class IntentResolverTest {
    private val client: OkHttpClient = mock()
    private val jsonMapper = ObjectMapper()
        .registerModule(KotlinModule())
        .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
    private lateinit var intentResolver: IntentResolver

    private val call: Call = mock()
    private var response: Response? = null

    @BeforeEach
    internal fun setUp() {
        intentResolver = IntentResolver(client, jsonMapper)
        `when`(client.newCall(any())).thenReturn(call)
        `when`(call.execute()).thenReturn(response)
    }

    @Test
    fun `resolveIntent whenSuccessfulResponse returnBestIntent`() {
         //Given
        val response = createSuccessResponse(INTENT_RESPONSE)
        `when`(call.execute()).thenReturn(response)

        //When
        val result = intentResolver.resolveIntent(MESSAGE, 0.8)

        //Then
        assertThat(result).isEqualTo(BEST_INTENT)
    }

    @Test
    fun `resolveIntent whenEmptyResponse returnBestIntent`() {
        //Given
        val response = createSuccessResponse(EMPTY_INTENT_RESPONSE)
        `when`(call.execute()).thenReturn(response)

        //When
        val result = intentResolver.resolveIntent(MESSAGE, 0.8)

        //Then
        assertThat(result).isNull()
    }

    @Test
    fun `resolveIntent whenSuccessfulResponse AndThresholdHigh returnNull`() {
        //Given
        val response = createSuccessResponse(INTENT_RESPONSE)
        `when`(call.execute()).thenReturn(response)

        //When
        val result = intentResolver.resolveIntent(MESSAGE, 0.9)

        //Then
        assertThat(result).isNull()
    }

    @Test
    fun `resolveIntent whenErrorResponse throwException`() {
        //Given
        val response = createErrorResponse(INTENT_RESPONSE)
        `when`(call.execute()).thenReturn(response)

        //When and Then
        assertThrows<RuntimeException> {
            intentResolver.resolveIntent(MESSAGE, 0.8)
        }
    }

    private fun createErrorResponse(body: String): Response {
        val mockRequest = Request.Builder()
            .url("https://some-url.com")
            .build()

        return Response.Builder()
            .protocol(Protocol.HTTP_1_1)
            .message("engine failure")
            .code(500)
            .request(mockRequest)
            .body(body.toResponseBody())
            .addHeader("Content-Type", APPLICATION_JSON_VALUE)
            .build()
    }

    private fun createSuccessResponse(body: String): Response {
        val mockRequest = Request.Builder()
            .url("https://some-url.com")
            .build()

        return Response.Builder()
            .protocol(Protocol.HTTP_1_1)
            .message("hey shawty!")
            .request(mockRequest)
            .body(body.toResponseBody())
            .addHeader("Content-Type", APPLICATION_JSON_VALUE)
            .code(200)
            .build()
    }
}