package org.company.app.ui.screns

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.datetime.*
import org.company.app.data.mappers.toChatInfo
import org.company.app.data.mappers.toMessageInfo
import org.company.app.data.Repository
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class ChatListScreenModel: ScreenModel, KoinComponent {
    val repository: Repository by inject()

    val currentChatId = mutableStateOf("")
    val messages: MutableState<List<MessageInfo>> = mutableStateOf(listOf())
    init {
        updateMessages()
    }
    var listOfChats: StateFlow<List<ChatInfo>> = loadChats()
    val textField = mutableStateOf("")

    fun updateMessages() {
        messages.value = repository.loadMessages(currentChatId.value).map {
            val user = repository.getUserById(it.senderUserId)
            it.toMessageInfo(user)
        }
    }
    fun loadChats(): StateFlow<List<ChatInfo>> {
        return repository
            .loadTwentyChats()
            .map {list ->
                list.map{
                    val message = repository.getMessagesAsList(it.id)
                    it.toChatInfo(message)
                }
            }
            .stateIn(
                scope = screenModelScope,
                started = SharingStarted.Lazily,
                initialValue = listOf()
            )
    }


    fun tapOnChat(chatId: String){
        currentChatId.value = chatId
        textField.value = ""
    }

    suspend fun generateChatWithMessage(){
        screenModelScope.launch {
            repository.generateMessage(true)
        }
    }

    fun generateChatWithoutMessage(){
        screenModelScope.launch {
            repository.generateMessage(false)
        }
    }
}

public fun <T> Flow<T>.mutableStateIn(
    scope: CoroutineScope,
    initialValue: T
): MutableStateFlow<T>{
    val flow = MutableStateFlow(initialValue)
    scope.launch {
        this@mutableStateIn.collect(flow)
    }
    return flow
}

data class ChatInfo(
    val chatId: String,
    val chatName: String,
    val imageRef: String?,
    val newMessages: Int,
    val isRead: Boolean,
    val time: LocalDateTime,
    val lastSender: String,
    val lastMessage: String
)

data class MessageInfo(
    val id: String,
    val senderUserId: String,
    val senderName: String,
    val senderImageRef: String,
    val receiverChatId: String,
    val message: String,
    val time : LocalDateTime,
    val isRead: Long,
    val imageRef: String?,
    val soundRef: String?,
    val gifRef: String?,
)
