package org.company.app.data.models.network

import db.ChatDBO
import kotlinx.serialization.Serializable

@Serializable
data class ChatDTO(
    val id: String,
    val chatName: String,
    val imageRef: String?
)

fun ChatDTO.toChatDBO() = ChatDBO(
    id = id,
    chatName = chatName,
    imageRef = imageRef
)
