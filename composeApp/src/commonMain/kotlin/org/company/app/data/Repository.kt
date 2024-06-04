package org.company.app.data

import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import db.ChatDBO
import db.MessageDBO
import db.QueriesQueries
import db.UserDBO
import io.ktor.client.*
import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.client.plugins.websocket.*
import io.ktor.http.*
import io.ktor.serialization.*
import io.ktor.serialization.kotlinx.*
import io.ktor.serialization.kotlinx.json.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.map
import kotlinx.serialization.json.Json
import org.company.app.data.mappers.toChatInfo
import org.company.app.data.mappers.toMessageInfo
import org.company.app.data.models.network.*
import org.company.app.db.MyDatabase
import org.company.app.network.NetworkSource
import org.company.app.ui.screns.ChatInfo
import org.company.app.ui.screns.MessageInfo

interface Repository{
    fun getChatsInfo(): Flow<List<ChatInfo>>
    fun getMessagesInfo(chatId: String): Flow<List<MessageInfo>>
    fun getCurrentUsername(): String
    suspend fun signIn(username: String, password: String): Boolean
    suspend fun signUp(username: String, alterName: String, password: String, imageRef: String?): Boolean
    suspend fun updateAll()
    suspend fun createNewChat(newChatDTO: NewChatDTO)
    suspend fun sendMessage(newMessage: UnregisteredMessageDTO)
}

class RepositoryDefault(
    val database: MyDatabase
): Repository {
    var username: String = ""
    var password: String = ""
    private var previousChatId = ""
    private var loadsInOneChat = 1L
    private val messagesPerLoad = 20L

    val queries: QueriesQueries = database.queriesQueries
    private val client = HttpClient{
        install(WebSockets){
            contentConverter = KotlinxWebsocketSerializationConverter(Json)
        }
        install(ContentNegotiation){
            json(Json {
                prettyPrint = true
                isLenient = true
            })
        }
    }
    private val networkSource = NetworkSource(client)
    private var messagesWebsocket: DefaultClientWebSocketSession? = null

    fun getChatsDromDB(): Flow<List<ChatDBO>> {
        val result = queries.getChats()
            .asFlow()
            .mapToList(Dispatchers.Default)
        result.map {list ->
            list.forEach { println(it.toString()) }
            }
        return result
    }

    override fun getChatsInfo(): Flow<List<ChatInfo>>{
        return getChatsDromDB()
            .map {list ->
                list.map{
                    val message = getMessagesAsListFromDB(it.id)
                    it.toChatInfo(message, getCurrentUsername())
                }
                    .sortedByDescending { it.time }
            }
    }
    override fun getMessagesInfo(chatId: String): Flow<List<MessageInfo>>{
        return getMessagesFromDB(chatId)
            .map {list ->
                list.map {
                    val user = getUserByIdFromDB(it.senderUsername)
                    it.toMessageInfo(user)
                }
            }
    }
     fun getMessagesFromDB(chatId: String): Flow<List<MessageDBO>> {
        return queries.getMessages(chatId, 20)
            .asFlow()
            .mapToList(Dispatchers.Default)
            .map { it.reversed() }
    }

    fun getMessagesAsListFromDB(id: String): List<MessageDBO> {
        return queries.getMessages(id, 100)
            .executeAsList()
    }


    fun getUserByIdFromDB(id: String): UserDBO {
        return queries.getUser(id).executeAsOne()
    }

    override fun getCurrentUsername(): String {
        return username
    }

    override suspend fun signUp(username: String, alterName: String, password: String, imageRef: String?): Boolean {
        return networkSource.sigUp(username, alterName, password, imageRef)
    }


    override suspend fun signIn(username: String, password: String): Boolean {
        var result = networkSource.signIn(username, password)
        if(result){
            this.username = username
            this.password = password
            val currentUser = networkSource.getUserByUsername(username)
            insertUser(currentUser.toUserDBO())
            startMessagesWebsocket()
            return true
        }else{
            return false
        }
    }

    suspend fun getChatById(chatId: String): ChatDBO {
        return networkSource.getChatById(chatId).toChatDBO()
    }

    suspend fun getUserByUsername(username: String): UserDBO {
        return networkSource.getUserByUsername(username).toUserDBO()
    }

    suspend fun insertChat(chat: ChatDBO){
        queries.insertChat(
            id = chat.id,
            chatName = chat.chatName,
            imageRef = chat.imageRef
        )
    }

    override suspend fun createNewChat(newChatDTO: NewChatDTO){
        val newChatDTOWithMe = NewChatDTO(
            newChatDTO.chatName,
            newChatDTO.imageRef,
            newChatDTO.users + listOf(username)
        )
        networkSource.createNewChat(newChatDTOWithMe)
    }

    suspend fun insertUser(user: UserDBO){
        queries.insertUser(
            username = user.username,
            alterName = user.alterName,
            imageRef = user.imageRef
        )
    }

    suspend fun insertMessage(messageDBO: MessageDBO){
        queries.insertMessage(
            id = messageDBO.id,
            senderUsername = messageDBO.senderUsername,
            receiverChatId = messageDBO.receiverChatId,
            message = messageDBO.message,
            time = messageDBO.time,
            isRead = messageDBO.isRead,
            imageRefs = messageDBO.imageRefs,
            soundRefs = messageDBO.soundRefs,
            gifRefs = messageDBO.gifRefs
        )
    }

    override suspend fun updateAll() {
        loadAllUsers()
        loadAllChatsFromNetwork()
        loadAllMessagesFromNetwork()
    }

    private suspend fun loadAllUsers() {
        networkSource.getAllUsers(username).forEach {
            insertUser(it.toUserDBO())
        }
    }

    private suspend fun loadAllMessagesFromNetwork(){
        val messages = networkSource.getAllMessages(username)
        messages.forEach { list ->
            list.forEach {
                insertMessage(it.toMessageDBO())
            }
        }
    }

    private suspend fun loadAllChatsFromNetwork() {
        networkSource.getAllChats(username).forEach {
            insertChat(it.toChatDBO())
        }
    }

    @OptIn(DelicateCoroutinesApi::class)
    suspend fun startMessagesWebsocket() {
        GlobalScope.launch {
            client.webSocket(
                method = HttpMethod.Get,
                host = networkSource.host,
                port = networkSource.port,
                path = "/$username/${networkSource.secret}/messages"
            ) {
                messagesWebsocket = this
                val job = launch {
                    try {
                        for (frame in incoming) {
                            val message = converter!!.deserialize<RegisteredMessageDTO>(frame)
                            insertMessage(message.toMessageDBO())
                        }
                    }
                    catch (e: Exception){
                        println(e.message)
                    }
                }
                job.join()
            }
        }
    }

    override suspend fun sendMessage(newMessage: UnregisteredMessageDTO){
        messagesWebsocket!!.sendSerialized(newMessage)
    }
}
