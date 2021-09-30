package ai.ultimate.project

import ai.ultimate.project.data.ReplyByIntent
import ai.ultimate.project.data.ReplyByIntentRepository
import ai.ultimate.project.request.InputMessage
import org.assertj.core.api.Assertions.assertThat
import org.bson.types.ObjectId
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.`when`
import org.mockito.kotlin.mock

internal class MessageProcessorTest {
    private val intentResolver: IntentResolver = mock()
    private val replyByIntentRepository: ReplyByIntentRepository = mock()
    private lateinit var messageProcessor: MessageProcessor

    companion object {
        private const val BOT_ID = "C-3PO"
        private const val MESSAGE = "BEEP BOOP BOP"
        private const val INTENT = "invasion"
        private const val CONFIDENCE_THRESHOLD = 0.8
        private val REPLY = ReplyByIntent(ObjectId.get(), INTENT, "Thou shall not pass")
    }
    @BeforeEach
    internal fun setUp() {
        messageProcessor = MessageProcessor(intentResolver, replyByIntentRepository)
    }

    @Test
    fun `processReplyForMessage whenEmptyMessage shouldReturnEmptyReply`() {
        val inputMessage = InputMessage(BOT_ID, "")
        val messageReply = messageProcessor.processReplyForMessage(inputMessage)

        assertThat(messageReply.botId).isEqualTo(BOT_ID)
        assertThat(messageReply.status).isEqualTo(MessageStatus.INVALID_INPUT)
    }

    @Test
    fun `processReplyForMessage whenNoIntentFetched shouldReturnUnresolvedReply`() {
        val inputMessage = InputMessage(BOT_ID, MESSAGE)
        val messageReply = messageProcessor.processReplyForMessage(inputMessage)

        assertThat(messageReply.botId).isEqualTo(BOT_ID)
        assertThat(messageReply.status).isEqualTo(MessageStatus.FAILED)
    }

    @Test
    fun `processReplyForMessage whenNoReplyForIntent shouldReturnUnresolvedReply`() {
        val inputMessage = InputMessage(BOT_ID, MESSAGE)
        `when`(intentResolver.resolveIntent(MESSAGE, CONFIDENCE_THRESHOLD)).thenReturn(INTENT)
        val messageReply = messageProcessor.processReplyForMessage(inputMessage)

        assertThat(messageReply.botId).isEqualTo(BOT_ID)
        assertThat(messageReply.status).isEqualTo(MessageStatus.FAILED)
    }

    @Test
    fun `processReplyForMessage whenReplyAndIntentPresent shouldReturnSuccessfulReply`() {
        val inputMessage = InputMessage(BOT_ID, MESSAGE)
        `when`(intentResolver.resolveIntent(MESSAGE, CONFIDENCE_THRESHOLD)).thenReturn(INTENT)
        `when`(replyByIntentRepository.findByIntent(INTENT)).thenReturn(REPLY)

        val messageReply = messageProcessor.processReplyForMessage(inputMessage)

        assertThat(messageReply.botId).isEqualTo(BOT_ID)
        assertThat(messageReply.status).isEqualTo(MessageStatus.SUCCESS)
        assertThat(messageReply.message).isEqualTo(REPLY.reply)
    }
}