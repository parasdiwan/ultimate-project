package ai.ultimate.project.request

import ai.ultimate.project.MessageProcessor
import ai.ultimate.project.MessageReply
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.Mockito.`when`
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.springframework.http.HttpStatus

internal class BotsControllerTest {

    val messageProcessor: MessageProcessor = mock()
    private lateinit var botsController: BotsController

    @BeforeEach
    internal fun setUp() {
        botsController = BotsController(messageProcessor)
    }

    @Test
    fun `hello endpoint returns hello reponse`() {
        //When
        val response = botsController.hello()

        //Then
        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(response.body).isEqualTo("hello! This is the ultimate bot :)")
    }

    @Test
    fun `processMessage returns reply of message processor`() {
        //Given
        val botId = "C-3PO"
        val hello = "hello"
        val reply = "Hello from the other side"
        val successfulReply = MessageReply.successfulReply(botId, reply)
        `when`(messageProcessor.processReplyForMessage(any())).thenReturn(successfulReply)

        //When
        val response = botsController.processMessage(botId, mapOf("message" to hello))

        //Then
        assertThat(response.statusCode).isEqualTo(HttpStatus.OK)
        assertThat(response.body).isEqualTo(successfulReply)
    }
}