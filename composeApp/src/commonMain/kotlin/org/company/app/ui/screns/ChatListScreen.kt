package org.company.app.ui.screns

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.unit.Constraints
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen
import compose.icons.TablerIcons
import compose.icons.tablericons.Check
import compose.icons.tablericons.Checks
import firstmultiplatfporm.composeapp.generated.resources.Res
import firstmultiplatfporm.composeapp.generated.resources.mcqueen
import org.jetbrains.compose.resources.painterResource

class ChatListScreen: Screen {

    @Composable
    override fun Content() {
        val screenModel = rememberScreenModel { ChatListScreenModel() }
        val chats by screenModel.listOfChats


        LazyColumn(
            modifier = Modifier.fillMaxHeight()
                .windowInsetsPadding(WindowInsets.safeDrawing)
                .padding(start = 8.dp, top = 8.dp, end = 8.dp)
        ) {
            items(chats) {
                Container(it)
                Spacer(modifier = Modifier.height(8.dp))
            }
        }
    }


    @OptIn(ExperimentalComposeUiApi::class)
    @Composable
    fun Container(chatInfo: ChatInfo) {
        val messageWidth = (LocalWindowInfo.current.containerSize.height * 0.6).toInt()
        Constraints()
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp)
        ) {
            val defaultImg = painterResource(Res.drawable.mcqueen)
            Image(
                chatInfo.imageRef ?: defaultImg,
                " ",
                alignment = Alignment.Center,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(64.dp)
                    .clip(RoundedCornerShape(100)))

            Spacer(modifier = Modifier.width(8.dp))

            Column(verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .height(64.dp)
                    .defaultMinSize(minWidth = 400.dp)
            ){
                Text(chatInfo.chatName)
                Spacer(modifier = Modifier.height(4.dp))
                Row(){
                    Text(chatInfo.lastSender + ": ", color = Color.Gray)
                    Text(chatInfo.lastMessage, color = Color.LightGray)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(chatInfo.time.toString(), color = Color.DarkGray)
                }
            }
            if(chatInfo.newMessages < 1){
                Icon(
                    if(chatInfo.isRead) TablerIcons.Checks else TablerIcons.Check,
                    null,
                    tint = Color.Blue
                )
            } else{
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(4.dp))
                        .background(Color.Gray)
                        .defaultMinSize(minWidth = 40.dp)
                ){
                    Text(chatInfo.newMessages.toString(), modifier = Modifier.padding(6.dp))
                }
            }
        }
    }
}