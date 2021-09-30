package ai.ultimate.project.integration

import ai.ultimate.project.MessageProcessor
import ai.ultimate.project.MessageStatus
import ai.ultimate.project.TestConfig
import ai.ultimate.project.TestDataConfig
import ai.ultimate.project.data.ReplyByIntent
import ai.ultimate.project.data.ReplyByIntentRepository
import ai.ultimate.project.request.InputMessage
import ai.ultimate.project.utils.TestConstants.BEST_INTENT
import ai.ultimate.project.utils.TestConstants.INTENT
import ai.ultimate.project.utils.TestConstants.REPLY
import okhttp3.OkHttpClient
import org.assertj.core.api.Assertions.assertThat
import org.bson.types.ObjectId
import org.junit.jupiter.api.BeforeAll
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.TestInstance
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.data.mongo.AutoConfigureDataMongo
import org.springframework.boot.test.context.SpringBootTest

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

    @BeforeAll
    fun setUp() {
        val replyByIntent = ReplyByIntent(ObjectId.get(), INTENT, REPLY.reply)
        val returnOfReplyByIntent = ReplyByIntent(ObjectId.get(), "hello", "sample reply")
        replyByIntentRepository.deleteAll()
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
}