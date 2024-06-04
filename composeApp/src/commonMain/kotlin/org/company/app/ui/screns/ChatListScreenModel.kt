package org.company.app.ui.screns

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import kotlinx.datetime.*
import org.company.app.data.Repository
import org.company.app.data.models.network.NewChatDTO
import org.company.app.data.models.network.UnregisteredMessageDTO
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class ChatListScreenModel: ScreenModel, KoinComponent {
    val repository: Repository by inject()
    val currentChatId = mutableStateOf("")
    val messages: MutableState<StateFlow<List<MessageInfo>>> = mutableStateOf(loadMessages())
    var listOfChats: MutableState<StateFlow<List<ChatInfo>>> = mutableStateOf(loadChats())
    val currentFrame = mutableStateOf(CurrentFrame.Left)
    init {
        updateFromNetwork()
    }
    val messageTF = mutableStateOf("")

    fun updateFromNetwork(){
        screenModelScope.launch {
            repository.updateAll()
        }
    }

    fun loadChats(): StateFlow<List<ChatInfo>>{
        return repository
            .getChatsInfo()
            .stateIn(
                scope = screenModelScope,
                started = SharingStarted.Lazily,
                initialValue = listOf()
            )
    }

    fun loadMessages(): StateFlow<List<MessageInfo>> {
        return repository
            .getMessagesInfo(currentChatId.value)
            .stateIn(
                scope = screenModelScope,
                started = SharingStarted.Lazily,
                initialValue = listOf()
            )
    }



    fun onChatClick(chatId: String) {
        messageTF.value = ""
        currentChatId.value = chatId
        messages.value = loadMessages()
        currentFrame.value = CurrentFrame.Right
    }


    fun createChatRequest(newChatDTO: NewChatDTO){
        screenModelScope.launch {
            repository.createNewChat(newChatDTO)
        }
    }

    fun onSendClick(imageRef: String? = null, soundRef: String? = null, gifRef: String? = null){
        val time = Clock.System.now().toEpochMilliseconds()
        screenModelScope.launch {
            repository.sendMessage(UnregisteredMessageDTO(currentChatId.value, messageTF.value, time, imageRef, soundRef, gifRef))
            messageTF.value = ""
        }
    }

    fun getCurrentUsername(): String {
        return repository.getCurrentUsername()
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

enum class CurrentFrame{
    Left,
    Right
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
    val senderUsername: String,
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
