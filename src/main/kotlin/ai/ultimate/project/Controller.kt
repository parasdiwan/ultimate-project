package ai.ultimate.project

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.*

@RestController
class Controller {

    @Autowired
    lateinit var messageProcessor: MessageProcessor

    @GetMapping(name = "/")
    fun helloRequest(): String = "Hello from ultimate project!"

    @PostMapping(
        name = "/bots/{botId}/message",
        consumes = ["application/json"],
        produces = ["application/json"]
    )
    fun processMessage(
        @PathVariable botId: String,
        @RequestBody body: Map<String, String>
    ): String {
        val message = MessageDTO(botId, body.get("message"))
        return messageProcessor.processReplyForMessage(message)
    }
}