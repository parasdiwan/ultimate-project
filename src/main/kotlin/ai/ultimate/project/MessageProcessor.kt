package ai.ultimate.project

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class MessageProcessor {

    @Autowired
    lateinit var intentResolver: IntentResolver

    fun processReplyForMessage(message: MessageDTO): String = ""
}
