package ai.ultimate.project

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class Controller {

    @GetMapping(name = "/")
    fun helloRequest(): String = "Hello from ultimate project!"

}