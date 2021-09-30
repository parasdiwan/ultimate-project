package ai.ultimate.project

import ai.ultimate.project.MessageReply.Companion.defaultReply
import ai.ultimate.project.MessageReply.Companion.emptyReply
import ai.ultimate.project.MessageReply.Companion.successfulReply
import ai.ultimate.project.UltimateClientConstants.DEFAULT_CONFIDENCE_THRESHOLD
import ai.ultimate.project.data.ReplyByIntentRepository
import ai.ultimate.project.request.MessageDTO
import org.apache.logging.log4j.util.Strings.isEmpty
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class MessageProcessor {

    @Autowired
    lateinit var intentResolver: IntentResolver
    @Autowired
    lateinit var replyByIntentRepository: ReplyByIntentRepository

    fun processReplyForMessage(messageDetails: MessageDTO): MessageReply {
        val botId = messageDetails.botId
        if (isEmpty(messageDetails.message)) {
            emptyReply(botId)
        }
        val message: String = messageDetails.message.toString()
        // confidence threshold is set to a default value
        // but can be easily configurable for each bot/client
        // as per product changes
        val intent = intentResolver.resolveIntent(message, DEFAULT_CONFIDENCE_THRESHOLD)
        val fetchedReply = intent?.let { fetchReplyForIntent(it) }

        return fetchedReply
            ?.let { successfulReply(botId, it) }
            ?: defaultReply(botId)
    }

    private fun fetchReplyForIntent(intent: String): String? {
        return replyByIntentRepository
            .findByIntent(intent.toLowerCase())
            ?.reply
    }
}