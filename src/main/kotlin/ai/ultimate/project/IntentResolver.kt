package ai.ultimate.project

import ai.ultimate.project.UltimateClientConstants.BOT_KEY
import ai.ultimate.project.UltimateClientConstants.DEFAULT_BOT_ID
import ai.ultimate.project.UltimateClientConstants.INTENTS_API_PATH
import ai.ultimate.project.UltimateClientConstants.MESSAGE_KEY
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.HttpMethod.POST
import org.springframework.stereotype.Service

@Service
class IntentResolver (
    private val ultimateAIClient: OkHttpClient,
    private val jsonMapper: ObjectMapper,
    @Value("\${intents-service.host}") private val intentsServiceHost: String,
    @Value("\${intents-service.port}") private val intentsServicePort: String,
) {

    fun resolveIntent(message: String, confidenceThreshold: Double): String? {

        val intents: List<IntentDetails> = fetchIntentsForMessage(message)
        val finalIntent = intents.asIterable()
            .filter { it.confidence > confidenceThreshold }
            .maxByOrNull { it.confidence }
            ?.name

        return finalIntent
    }

    private fun fetchIntentsForMessage(message: String): List<IntentDetails> {
        val requestMap = mapOf(
            BOT_KEY to DEFAULT_BOT_ID,
            MESSAGE_KEY to message
        )
        val requestBody = jsonMapper
            .writeValueAsString(requestMap)
            .toRequestBody()

        val request = Request.Builder()
            .method(POST.toString(), requestBody)
            .url("$intentsServiceHost:$intentsServicePort$INTENTS_API_PATH")
            .build()

        val response = ultimateAIClient.newCall(request)
            .execute()

        if (response.isSuccessful) {
            val responseString = response.body?.string() ?: ""
            val intentsResponse: IntentsResponse = jsonMapper.readValue(responseString)
            return intentsResponse.intents
        } else {
             throw RuntimeException("Error fetching intents")
        }
    }
}

data class IntentsResponse (
    val intents: List<IntentDetails>
)

object UltimateClientConstants {
    const val INTENTS_API_PATH = "/api/intents"
    const val BOT_KEY = "botId"
    const val MESSAGE_KEY = "message"
    const val DEFAULT_BOT_ID = "5f74865056d7bb000fcd39ff"
    const val DEFAULT_CONFIDENCE_THRESHOLD = 0.8
}
