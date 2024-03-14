package org.company.app.data

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import db.Chats
import db.Messages
import db.QueriesQueries
import db.Users
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.datetime.Clock
import org.company.app.db.MyDatabase

interface Repository{
     fun loadMessages(chatId: String): List<Messages>
     fun loadTwentyChats(): Flow<List<Chats>>
     fun getMessages(chatId: String): Flow<List<Messages>>
    fun getMessagesAsList(id: String): List<Messages>
     fun getUserById(id: String): Users
    suspend fun generateMessage(hasMessage: Boolean = false)
}

class RepositoryImpl(val database: MyDatabase ): Repository {
    private var previousChatId = ""
    private var loadsInOneChat = 1L
    private val messagesPerLoad = 20L

    val queries: QueriesQueries = database.queriesQueries

    override fun loadMessages(chatId: String): List<Messages> {
        if(previousChatId == chatId){
            loadsInOneChat++
        }else{
            loadsInOneChat = 1L
        }
        return queries.getMessages(chatId, loadsInOneChat * messagesPerLoad)
            .executeAsList()
    }

    override fun loadTwentyChats(): Flow<List<Chats>> {
        return queries.getChats()
            .asFlow()
            .mapToList(Dispatchers.Default)
            .map { it }
    }

    override fun getMessages(chatId: String): Flow<List<Messages>> {
        return queries.getMessages(chatId, 20)
            .asFlow()
            .mapToList(Dispatchers.Default)
            .map { it }
    }

    override fun getMessagesAsList(id: String): List<Messages> {
        return queries.getMessages(id, 100)
            .executeAsList()
    }


    override fun getUserById(id: String): Users {
        return queries.getUser(id).executeAsOne()
    }


    override suspend fun generateMessage(hasMessage: Boolean){
        val userId =  Clock.System.now().toEpochMilliseconds().toString()
        val chatId = (Clock.System.now().toEpochMilliseconds() + 5).toString()
        val messageId = (Clock.System.now().toEpochMilliseconds() + 10).toString()
        insertUser(
            Users(
                id = userId,
                alterName = "Test user name",
                imageRef = null
            )
        )
        insertChat(
            Chats(
                id = chatId,
                chatName = "Test chat name",
                imageRef = null
            )
        )
        if(hasMessage){
            insertMessage(
                Messages(
                    id = messageId,
                    senderUserId = userId,
                    receiverChatId = chatId,
                    message = "Test Message",
                    time = Clock.System.now().toEpochMilliseconds(),
                    isRead = 1L,
                    gifRefs = null,
                    imageRefs = null,
                    soundRefs = null
                )
            )
        }
    }
    suspend fun insertChat(chat: Chats){
        queries.insertChat(
            id = chat.id,
            chatName = chat.chatName,
            imageRef = chat.imageRef
        )
    }

    suspend fun insertUser(user: Users){
        queries.insertUser(
            id = user.id,
            alterName = user.alterName,
            imageRef = user.imageRef
        )
    }

    suspend fun insertMessage(messages: Messages){
        queries.insertMessage(
            id = messages.id,
            senderUserId = messages.senderUserId,
            receiverChatId = messages.receiverChatId,
            message = messages.message,
            time = messages.time,
            isRead = messages.isRead,
            imageRefs = messages.imageRefs,
            soundRefs = messages.soundRefs,
            gifRefs = messages.gifRefs
        )
    }
}
