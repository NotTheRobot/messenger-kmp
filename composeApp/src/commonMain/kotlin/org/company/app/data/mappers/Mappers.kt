package org.company.app.data.mappers

import db.ChatDBO
import db.MessageDBO
import db.UserDBO
import kotlinx.datetime.*
import org.company.app.ui.screns.ChatInfo
import org.company.app.ui.screns.MessageInfo

fun ChatDBO.toChatInfo(messages: List<MessageDBO>, currentUsername: String): ChatInfo{
    val lastMessage: MessageDBO? = if(messages.isEmpty()) null else messages.first()

    val newMessages = if(lastMessage?.senderUsername == currentUsername){
        0
    }else{
        messages.filter { it.isRead == 0L }.size
    }

    return ChatInfo(
        chatId = this.id,
        chatName = this.chatName,
        imageRef = this.imageRef,
        newMessages = newMessages,
        isRead = lastMessage?.isRead == 1L,
        time = Instant.fromEpochMilliseconds(lastMessage?.time ?: 0).toLocalDateTime(TimeZone.UTC),
        lastSender = lastMessage?.senderUsername ?: "",
        lastMessage = lastMessage?.message ?: ""
    )
}

fun MessageDBO.toMessageInfo(user: UserDBO): MessageInfo{
    return MessageInfo(
        id = this.id,
        senderUsername = this.senderUsername,
        senderName = user.alterName,
        senderImageRef = user.alterName,
        receiverChatId = this.receiverChatId,
        message = this.message,
        time = Instant.fromEpochMilliseconds(this.time).toLocalDateTime(TimeZone.currentSystemDefault()),
        isRead = this.isRead,
        imageRef = this.imageRefs,
        soundRef = this.soundRefs,
        gifRef = this.gifRefs
    )
}

fun LocalDateTime.differenceToString(): String{
    val currentDateTime = Clock.System.now()
    val difference = currentDateTime - this.toInstant(TimeZone.currentSystemDefault())

    if(difference.inWholeDays > 0){
        return difference.inWholeDays.toString() + " D"
    }else if(difference.inWholeHours > 0){
        return difference.inWholeHours.toString() + " H"
    }else{
        return difference.inWholeMinutes.toString() + " M"
    }
}

fun LocalDateTime.toCustomString(): String{
    val currentDateTime = Clock.System.now().toLocalDateTime(TimeZone.currentSystemDefault())
    if(currentDateTime.year > this.year){
        return this.year.toString() + this.monthNumber.toString() + "." + this.dayOfMonth.toString()
    } else if(currentDateTime.dayOfYear > this.dayOfYear){
        val day = if(this.dayOfMonth <= 9){
            "0${this.dayOfMonth}"
        }else{
            this.dayOfMonth.toString()
        }
        return this.monthNumber.toString() + "." + day
    }else if(currentDateTime.dayOfYear == this.dayOfYear + 1){
        return "Yesterday"
    }else{
        val minute = if(this.minute <= 9){
            "0${this.minute}"
        }else{
            this.minute.toString()
        }
        return this.hour.toString() + ":" + minute
    }
}
