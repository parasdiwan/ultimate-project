package ai.ultimate.project

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class Controller {

    @Autowired
    lateinit var messageProcessor: MessageProcessor

    @GetMapping(name = "/")
    fun helloRequest(): String = "Hello from ultimate project!"

    @PostMapping(
        name = "/bot/:botId/message",
        consumes = ["application/json"],
        produces = ["application/json"]
    )
    fun processMessage(): String = ""
}