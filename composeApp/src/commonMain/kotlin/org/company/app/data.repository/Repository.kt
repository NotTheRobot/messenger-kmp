package org.company.app.data.repository

import org.company.app.data.models.Chat
import org.company.app.ui.screns.Message

interface Repository{
    val chats: List<Chat>
    val messages: List<Message>

    fun loadTwentyMessages(): Unit
    fun loadTwentyChats(): Unit
}
class RepositoryImpl() {

}
