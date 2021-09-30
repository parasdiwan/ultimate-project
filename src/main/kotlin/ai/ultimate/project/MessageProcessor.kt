package ai.ultimate.project

import ai.ultimate.project.MessageReply.Companion.failedDefaultReply
import ai.ultimate.project.MessageReply.Companion.emptyReply
import ai.ultimate.project.MessageReply.Companion.successfulReply
import ai.ultimate.project.UltimateClientConstants.DEFAULT_CONFIDENCE_THRESHOLD
import ai.ultimate.project.data.ReplyByIntentRepository
import ai.ultimate.project.request.InputMessage
import org.apache.logging.log4j.util.Strings.isEmpty
import org.springframework.stereotype.Service

@Service
class MessageProcessor(
    private val intentResolver: IntentResolver,
    private val replyByIntentRepository: ReplyByIntentRepository
) {

    fun processReplyForMessage(inputMessage: InputMessage): MessageReply {
        val botId = inputMessage.botId
        if (isEmpty(inputMessage.message)) {
            return emptyReply(botId)
        }
        val message: String = inputMessage.message.toString()
        // confidence threshold is set to a default value
        // but can be easily configurable for each bot/client
        // as per product changes
        val intent = intentResolver.resolveIntent(message, DEFAULT_CONFIDENCE_THRESHOLD)
        val fetchedReply = intent?.let { fetchReplyForIntent(it) }

        return fetchedReply
            ?.let { successfulReply(botId, it) }
            ?: failedDefaultReply(botId)
    }

    private fun fetchReplyForIntent(intent: String): String? {
        return replyByIntentRepository
            .findByIntent(intent.toLowerCase())
            ?.reply
    }
}