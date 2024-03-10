package org.company.app.ui.screns

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.painter.Painter
import cafe.adriel.voyager.core.model.ScreenModel
import kotlinx.datetime.DateTimeUnit
import org.company.app.utils.PlatformSpecific
import kotlin.random.Random


class ChatListScreenModel: ScreenModel {
    val listOfChats = mutableStateOf(generateCharts(20))
    val textField = mutableStateOf("")
    val messages = mutableStateOf(generateMessages(20))



    private fun generateMessages(amount: Int): MutableList<Message> {
        val piece = "ну здарова нахуй"
        val messages = mutableListOf<Message>()
        messages.add(Message(
            sender = "me",
            message = piece,
            DateTimeUnit.HOUR
        ))

        for(i in 1..amount) {
            val sender = if (Random.nextInt() % 2 == 0) "me" else "not me"
            val pieces = mutableListOf<String>()
            repeat(Random.nextInt(from = 50, until = 100)) {
                pieces.add(piece)
            }
            messages.add(Message(
                sender,
                pieces.joinToString(separator = "") { it },
                DateTimeUnit.HOUR * Random.nextInt(from = 1, until = 24)
                ))
        }
        return messages
    }

    fun generateCharts(amount: Int): MutableList<ChatInfo> {
        val charts = mutableListOf<ChatInfo>()
        for(i in 1..amount){
            charts.add(
                ChatInfo(
                    Random.nextInt().toString(),
                    Random.nextInt().toString(),
                    null,
                    Random.nextInt(from = -500, until = 999),
                    Random.nextInt() % 2 == 0,
                    DateTimeUnit.HOUR * Random.nextInt(from = 1, until = 24),
                    Random.nextInt().toString(),
                    "lkjdsflah kljdflk jsldkfh slkfhslkd hsfjlk jsklf jsdlkf jskj"
                )
            )
        }
        return charts
    }
}

data class ChatInfo(
    val chatId: String,
    val chatName: String,
    val imageRef: Painter?,
    val newMessages: Int,
    val isRead: Boolean,
    val time: DateTimeUnit,
    val lastSender: String,
    val lastMessage: String
)

data class Message(
    val sender: String,
    val message: String,
    val time: DateTimeUnit
)
