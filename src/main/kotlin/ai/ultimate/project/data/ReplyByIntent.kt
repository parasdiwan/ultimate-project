package ai.ultimate.project.data

import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document
data class ReplyByIntent(
    @Id
    val id: String,
    val reply: String
)