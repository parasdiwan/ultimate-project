package ai.ultimate.project.data

import org.bson.types.ObjectId
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.Document

@Document("repliesByIntent")
data class ReplyByIntent(
    @Id
    val id: ObjectId,
    val intent: String,
    val reply: String
)