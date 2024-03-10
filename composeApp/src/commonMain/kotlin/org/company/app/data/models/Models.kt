package org.company.app.data.models



data class User(
    val userId: String,
    val username: String,
    val alterName: String,
    val email: String,
    val imageRef: String?,
)

data class Chat(
    val chatId: String,
    val chatName: String,
    val imageRef: String?,
)

data class Message(
    val messageId: String,
    val senderUserId: String,
    val receiverChatId: String,
    val message: String,
    val imageRefs: String?,
    val soundRefs: String?,
    val gifRefs: String?,
)

