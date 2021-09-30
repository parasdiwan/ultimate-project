package ai.ultimate.project.data

import org.springframework.data.mongodb.repository.MongoRepository

interface ReplyByIntentRepository: MongoRepository<ReplyByIntent, String> {
    fun findByIntent(intent: String): ReplyByIntent?
}
