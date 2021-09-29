package ai.ultimate.project.data

import org.springframework.data.mongodb.repository.MongoRepository

interface ReplyByIntentRepository: MongoRepository<ReplyByIntent, String> {
}
