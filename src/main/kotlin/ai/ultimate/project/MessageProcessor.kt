package ai.ultimate.project

import ai.ultimate.project.data.ReplyByIntent
import ai.ultimate.project.data.ReplyByIntentRepository
import ai.ultimate.project.request.MessageDTO
import org.apache.logging.log4j.util.Strings.isEmpty
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class MessageProcessor {

    private val INVALID_MESSAGE_REPLY = "Insert Invalid message"
    private val DEFAULT_REPLY = "Insert default reply"

    @Autowired
    lateinit var intentResolver: IntentResolver
    @Autowired
    lateinit var replyByIntentRepository: ReplyByIntentRepository

    fun processReplyForMessage(messageDetails: MessageDTO): String {
        if (isEmpty(messageDetails.message)) {
            return INVALID_MESSAGE_REPLY
        }
        val message: String = messageDetails.message.toString()
        val intent = intentResolver.resolveIntent(message)
        return getReplyForIntent(intent)
    }

    private fun getReplyForIntent(intent: String): String {
        val replyByIntent: ReplyByIntent = replyByIntentRepository
            .findById(intent)
            .orElse(ReplyByIntent(intent, DEFAULT_REPLY))
        return replyByIntent.reply
    }
}