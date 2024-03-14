package org.company.app.data.mappers

import db.Chats
import db.Messages
import db.Users
import kotlinx.coroutines.flow.StateFlow
import kotlinx.datetime.*
import org.company.app.ui.screns.ChatInfo
import org.company.app.ui.screns.MessageInfo

fun Chats.toChatInfo(messages: List<Messages>): ChatInfo{
    val newMessages = if(messages.last().senderUserId == "me"){
        0
    }else{
        messages.filter { it.isRead == 0L }.size
    }

    return ChatInfo(
        chatId = this.id,
        chatName = this.chatName,
        imageRef = this.imageRef,
        newMessages = newMessages,
        isRead = messages.last().isRead == 1L,
        time = Instant.fromEpochMilliseconds(messages.last().time).toLocalDateTime(TimeZone.UTC),
        lastSender = messages.last().senderUserId,
        lastMessage = messages.last().message
    )
}

fun Messages.toMessageInfo(user: Users): MessageInfo{
    return MessageInfo(
        id = this.id,
        senderUserId = this.senderUserId,
        senderName = user.alterName,
        senderImageRef = user.alterName,
        receiverChatId = this.receiverChatId,
        message = this.message,
        time = Instant.fromEpochMilliseconds(this.time).toLocalDateTime(TimeZone.UTC),
        isRead = this.isRead,
        imageRef = this.imageRefs,
        soundRef = this.soundRefs,
        gifRef = this.gifRefs
    )
}
