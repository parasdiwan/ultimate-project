package ai.ultimate.project.request

import ai.ultimate.project.MessageProcessor
import ai.ultimate.project.MessageReply
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType.APPLICATION_JSON_VALUE
import org.springframework.http.ResponseEntity
import org.springframework.http.ResponseEntity.ok
import org.springframework.web.bind.annotation.*

@RestController
class Controller {

    @Autowired
    lateinit var messageProcessor: MessageProcessor

    @GetMapping(name = "/")
    fun helloRequest(): String = "Hello from ultimate project!"

    @PostMapping(
        name = "/bots/{botId}/message",
        consumes = [APPLICATION_JSON_VALUE],
        produces = [APPLICATION_JSON_VALUE]
    )
    fun processMessage(
        @PathVariable botId: String,
        @RequestBody body: Map<String, String>
    ): ResponseEntity<MessageReply> {
        val message = MessageDTO(botId, body.get("message"))
        val replyForMessage = messageProcessor.processReplyForMessage(message)
        return ok(replyForMessage)
    }
}