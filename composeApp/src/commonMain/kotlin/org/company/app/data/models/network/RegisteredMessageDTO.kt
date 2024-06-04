package org.company.app.data.models.network

import db.MessageDBO
import kotlinx.serialization.Serializable
import org.company.app.ui.screns.MessageInfo

@Serializable
data class RegisteredMessageDTO(
    val id: String,
    val receiverChatId: String,
    val senderUsername: String,
    val message: String,
    val isRead: Long,
    val time: Long,
    val imageRefs: String?,
    val soundRefs: String?,
    val gifRefs: String?,
)

fun RegisteredMessageDTO.toMessageDBO() = MessageDBO(
    id = this.id,
    receiverChatId = this.receiverChatId,
    senderUsername = this.senderUsername,
    message = this.message,
    isRead = this.isRead,
    time = this.time,
    imageRefs = this.imageRefs,
    soundRefs = this.soundRefs,
    gifRefs = this.gifRefs,
)

//fun RegisteredMessageDTO.toMessageInfo() = MessageInfo(
//)
