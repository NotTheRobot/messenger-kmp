package org.company.app.ui.screns

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.painter.Painter
import cafe.adriel.voyager.core.model.ScreenModel
import kotlinx.datetime.DateTimeUnit
import kotlin.random.Random


class ChatListScreenModel: ScreenModel {
    val listOfChats = mutableStateOf(generateCharts(10))

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
                    Random.nextInt().toString()
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