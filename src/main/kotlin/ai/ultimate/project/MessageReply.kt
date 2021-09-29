package ai.ultimate.project

data class MessageReply private constructor(
    val botId: String,
    val message: String,
    val status: MessageStatus
) {
    companion object {
        const val DEFAULT_MESSAGE = "Bot could not resolve a reply"
        fun successfulReply(botId: String, message: String): MessageReply {
            return MessageReply(botId, message, MessageStatus.SUCCESS)
        }
        fun defaultReply(botId: String): MessageReply {
            return MessageReply(botId, DEFAULT_MESSAGE, MessageStatus.DEFAULT)
        }
        fun emptyReply(botId: String): MessageReply {
            return MessageReply(botId, "", MessageStatus.INVALID_INPUT)
        }
    }
}

enum class MessageStatus {
    SUCCESS, DEFAULT, INVALID_INPUT
}
