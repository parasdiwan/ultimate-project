package ai.ultimate.project.request

import ai.ultimate.project.MessageProcessor
import ai.ultimate.project.MessageReply
import org.springframework.http.MediaType.APPLICATION_JSON_VALUE
import org.springframework.http.ResponseEntity
import org.springframework.http.ResponseEntity.ok
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/bots",
    consumes = [APPLICATION_JSON_VALUE],
    produces = [APPLICATION_JSON_VALUE])
class BotsController(val messageProcessor: MessageProcessor) {

    @GetMapping("/hello")
    fun hello(): ResponseEntity<String> {
        return ok("hello! This is the ultimate bot :)")
    }

    @PostMapping("/{botId}/message")
    fun processMessage(
        @PathVariable botId: String,
        @RequestBody body: Map<String, String>
    ): ResponseEntity<MessageReply> {
        val message = MessageDTO(botId, body.get("message"))
        val replyForMessage = messageProcessor.processReplyForMessage(message)
        return ok(replyForMessage)
    }
}