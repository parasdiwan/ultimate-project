package ai.ultimate.project.data

import org.bson.types.ObjectId
import org.springframework.data.mongodb.repository.MongoRepository
import org.springframework.stereotype.Component

    interface ReplyByIntentRepository: MongoRepository<ReplyByIntent, String> {
    fun findOneById(id: ObjectId): ReplyByIntent
}
