package ai.ultimate.project.integration

import ai.ultimate.project.MessageProcessor
import ai.ultimate.project.MessageStatus
import ai.ultimate.project.TestConfig
import ai.ultimate.project.TestDataConfig
import ai.ultimate.project.data.ReplyByIntent
import ai.ultimate.project.data.ReplyByIntentRepository
import ai.ultimate.project.request.InputMessage
import ai.ultimate.project.utils.TestConstants
import ai.ultimate.project.utils.TestConstants.BEST_INTENT
import ai.ultimate.project.utils.TestConstants.INTENT
import ai.ultimate.project.utils.TestConstants.REPLY
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.RecordedRequest
import org.assertj.core.api.Assertions.assertThat
import org.bson.types.ObjectId
import org.junit.jupiter.api.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.data.mongo.AutoConfigureDataMongo
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import javax.annotation.PreDestroy

@SpringBootTest(classes = [TestConfig::class, TestDataConfig::class])
@AutoConfigureDataMongo
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class MessageProcessorIntTest {

    @Autowired
    private lateinit var messageProcessor: MessageProcessor
    @Autowired
    private lateinit var replyByIntentRepository: ReplyByIntentRepository
    @Autowired
    private lateinit var okHttpClient: OkHttpClient
    private lateinit var mockServer: MockWebServer

    @BeforeAll
    fun setUp() {
        mockServer = MockWebServer()
        mockServer.start(8000)
        mockServer.dispatcher = createDispatcher()
    }

    @BeforeEach
    fun setUpEach() {
        replyByIntentRepository.deleteAll()
        val replyByIntent = ReplyByIntent(ObjectId.get(), INTENT, REPLY.reply)
        val returnOfReplyByIntent = ReplyByIntent(ObjectId.get(), "hello", "sample reply")
        replyByIntentRepository.insert(replyByIntent)
        replyByIntentRepository.insert(returnOfReplyByIntent)
    }

    @Test
    fun `processReply whenMissingReply returnFailedReply`() {
        // When
        val reply = messageProcessor.processReplyForMessage(InputMessage("botId", "strange messages"))

        //Then
        assertThat(reply.status).isEqualTo(MessageStatus.FAILED)
        assertThat(reply.message).isEqualTo("Bot could not resolve a reply")
    }

    @Test
    fun `processReply whenReplyExists returnSuccessfulReply`() {
        //Given
        val returnedReply = ReplyByIntent(ObjectId.get(), BEST_INTENT.toLowerCase(), REPLY.reply)
        replyByIntentRepository.insert(returnedReply)

        //When
        val reply = messageProcessor.processReplyForMessage(InputMessage("botId", "hello"))

        //Then
        assertThat(reply.message).isEqualTo(REPLY.reply)
        assertThat(reply.status).isEqualTo(MessageStatus.SUCCESS)
    }

    @AfterAll
    fun onDestroy() {
        mockServer.shutdown()
    }

    private fun createDispatcher() = object : Dispatcher() {
        override fun dispatch(request: RecordedRequest): MockResponse {
            if (request.method == "POST" && request.path == "/api/intents")
                return MockResponse()
                    .setBody(TestConstants.INTENT_RESPONSE)
                    .setResponseCode(200)
                    .setHeader("Content-Type", MediaType.APPLICATION_JSON_VALUE)
            else
                return MockResponse().setResponseCode(404)
        }
    }
}