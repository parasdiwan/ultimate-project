package ai.ultimate.project.utils

import ai.ultimate.project.data.ReplyByIntent
import org.bson.types.ObjectId

object TestConstants {
    const val BOT_ID = "C-3PO"
    const val MESSAGE = "BEEP BOOP BOP"
    const val INTENT = "invasion"
    const val CONFIDENCE_THRESHOLD = 0.8
    val REPLY = ReplyByIntent(ObjectId.get(), INTENT, "Thou shall not pass")
    const val INTENT_RESPONSE = """{
    "intents": [
        {
            "confidence": 0.8958161234855652,
            "name": "Promotions"
        },
        {
            "confidence": 0.09742621332406998,
            "name": "Insult"
        },
        {
            "confidence": 0.04477700591087341,
            "name": "Affirmative"
        },
        {
            "confidence": 0.032686229795217514,
            "name": "Reclamation"
        },
        {
            "confidence": 0.009632015600800514,
            "name": "Invoice due date"
        }
    ],
    "entities": []
    }"""
    const val BEST_INTENT = "Promotions"
    const val EMPTY_INTENT_RESPONSE = """{
        "intents": []
    }
    """
}