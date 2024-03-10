package org.company.app.ui.screns

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalWindowInfo
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ChainStyle
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import androidx.constraintlayout.compose.Dimension
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen
import compose.icons.TablerIcons
import compose.icons.tablericons.Check
import compose.icons.tablericons.Checks
import compose.icons.tablericons.Send
import firstmultiplatfporm.composeapp.generated.resources.Res
import firstmultiplatfporm.composeapp.generated.resources.mcqueen
import org.company.app.utils.PlatformSpecific
import org.jetbrains.compose.resources.painterResource

class ChatListScreen: Screen {

    @OptIn(ExperimentalComposeUiApi::class)
    @Composable
    override fun Content() {
        val screenModel = rememberScreenModel { ChatListScreenModel() }
        val chats by screenModel.listOfChats
        BoxWithConstraints {
            if (maxWidth > 720.dp){
                Row(){
                    leftFrame(chats, modifier = Modifier.width(360.dp))
                    rightFrame(screenModel)
                }
            }else{
                leftFrame(chats, modifier = Modifier.fillMaxWidth())
            }
        }

    }

    @Composable
    fun leftFrame(chats: MutableList<ChatInfo>, modifier: Modifier){
        LazyColumn(
            modifier = modifier
                .windowInsetsPadding(WindowInsets.safeDrawing)
                .fillMaxHeight()
                .padding(start = 8.dp, top = 8.dp, end = 8.dp)

        ) {
            items(chats) {
                Container(it)
            }
        }
    }
    @Composable
    fun Container(chatInfo: ChatInfo) {
        Row(modifier = Modifier, verticalAlignment = Alignment.CenterVertically) {
            val defaultImg = painterResource(Res.drawable.mcqueen)
            Image(
                chatInfo.imageRef ?: defaultImg,
                " ",
                alignment = Alignment.Center,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .padding(end = 8.dp)
                    .size(64.dp)
                    .clip(RoundedCornerShape(100))
            )
            ConstraintLayout(modifier = Modifier.fillMaxWidth()) {
                val (
                    chatName,
                    message,
                    stateMessage,
                    time,
                    senderName
                ) = createRefs()
                /*
                    val chatNameTimeChain = createHorizontalChain(chatName, time, chainStyle = ChainStyle.SpreadInside)
                    val senderNameStateMessageChain = createHorizontalChain(senderName, stateMessage, chainStyle = ChainStyle.SpreadInside)*/
                val textBarrier = createStartBarrier(time, stateMessage)

                Text(
                    textAlign = TextAlign.Start,
                    text = chatInfo.chatName,
                    fontSize = 16.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .constrainAs(chatName) {
                            top.linkTo(parent.top, 8.dp)
                            start.linkTo(parent.start, 8.dp)
                            end.linkTo(textBarrier, 4.dp)
                            width = Dimension.fillToConstraints
                        }
                        .fillMaxWidth()
                )

                Text(
                    chatInfo.lastSender, color = Color.LightGray, fontSize = 12.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .constrainAs(senderName) {
                            top.linkTo(chatName.bottom,)
                            start.linkTo(chatName.start)
                            end.linkTo(textBarrier, 4.dp)
                            width = Dimension.fillToConstraints
                        }
                )
                Text(
                    chatInfo.lastMessage, color = Color.Gray, fontSize = 12.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier
                        .constrainAs(message) {
                            top.linkTo(senderName.bottom,)
                            start.linkTo(senderName.start)
                            end.linkTo(textBarrier, 4.dp)
                            width = Dimension.fillToConstraints
                        }
                )

                Text(
                    chatInfo.time.toString(), color = Color.DarkGray, fontSize = 12.sp,
                    modifier = Modifier.constrainAs(time) {
                        top.linkTo(parent.top, 8.dp)
                        end.linkTo(parent.end, 8.dp)
                    }
                )
                if (chatInfo.newMessages < 1) {
                    Icon(
                        if (chatInfo.isRead) TablerIcons.Checks else TablerIcons.Check,
                        null,
                        tint = Color.Blue,
                        modifier = Modifier.constrainAs(stateMessage) {
                            bottom.linkTo(parent.bottom, 8.dp)
                            end.linkTo(parent.end, 8.dp)
                        }
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(Color.Gray)
                            .defaultMinSize(minWidth = 40.dp)
                            .constrainAs(stateMessage) {
                                bottom.linkTo(parent.bottom, 8.dp)
                                end.linkTo(parent.end, 8.dp)
                            }
                    ) {
                        Text(chatInfo.newMessages.toString(), modifier = Modifier.padding(6.dp))
                    }
                }


            }
        }
    }
    @Composable
    private fun rightFrame(screenModel: ChatListScreenModel) {
        var textField by screenModel.textField
        val messagesList by screenModel.messages
        ConstraintLayout(modifier = Modifier.fillMaxSize()) {
            val (messages, editField, sendButton) = createRefs()
            LazyColumn(
                state = rememberLazyListState(Int.MAX_VALUE),
                modifier = Modifier
                    .constrainAs(messages){
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        bottom.linkTo(editField.top)
                        width = Dimension.fillToConstraints
                        height = Dimension.fillToConstraints
                    }
            ) {
                items(messagesList){
                    MessageBlock(it)
                }
            }
            TextField(
                value = textField,
                onValueChange = { textField = it },
                label = { Text("Сообщение...") },
                modifier = Modifier
                    .constrainAs(editField){
                        bottom.linkTo(parent.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(sendButton.start)
                        top.linkTo(sendButton.top)
                        width = Dimension.fillToConstraints
                        height = Dimension.fillToConstraints
                    }
            )
            Button(
                onClick = {},
                shape = RoundedCornerShape(0),
                modifier = Modifier
                    .constrainAs(sendButton){
                        end.linkTo(parent.end)
                        bottom.linkTo(parent.bottom)
                    }
            ){
                Icon(
                    TablerIcons.Send,
                    contentDescription = null,
                    modifier = Modifier
                        .size(48.dp)

                )
            }
        }
    }

    @Composable
    fun MessageBlock(item: Message) {
        val constraints = getConstraintsFor(item.sender)
        val defaultImg = painterResource(Res.drawable.mcqueen)

        ConstraintLayout(
            constraintSet = constraints,
            modifier = Modifier.fillMaxWidth(),
            ) {
            Image(
                defaultImg,
                null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .layoutId("imageProfile")
                    .size(64.dp)
                    .clip(RoundedCornerShape(100))
            )
            Text(
                item.sender,
                modifier = Modifier.layoutId("sender")
            )
            Box(modifier = Modifier
                .layoutId("message")
                .widthIn(max = 640.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(Color.Blue)) {
                Text(
                    item.message,
                    color = Color.LightGray,
                    modifier = Modifier
                        .padding(16.dp)
                )
            }
            Text(
                item.time.toString(),
                color = Color.Gray,
                modifier = Modifier.layoutId("time")
            )
        }
    }

    private fun getConstraintsFor(sender: String): ConstraintSet {
        return ConstraintSet{
            val imageProfile = createRefFor("imageProfile")
            val senderRef = createRefFor("sender")
            val message = createRefFor("message")
            val time = createRefFor("time")
            if(sender == "me"){
                constrain(imageProfile){
                    top.linkTo(parent.top, 8.dp)
                    end.linkTo(parent.end, 8.dp)
                    width = Dimension.fillToConstraints
                }
                constrain(senderRef){
                    end.linkTo(imageProfile.start, 8.dp)
                    bottom.linkTo(imageProfile.bottom)
                }
                constrain(message){
                    top.linkTo(imageProfile.bottom)
                    end.linkTo(parent.end, 8.dp)
                    width = Dimension.fillToConstraints
                }
                constrain(time){
                    top.linkTo(message.bottom)
                    start.linkTo(message.start)
                }
            }else{
                constrain(imageProfile){
                    top.linkTo(parent.top, 8.dp)
                    start.linkTo(parent.start, 8.dp)
                    width = Dimension.fillToConstraints
                }
                constrain(senderRef){
                    start.linkTo(imageProfile.end, 8.dp)
                    bottom.linkTo(imageProfile.bottom)
                }
                constrain(message){
                    top.linkTo(imageProfile.bottom)
                    start.linkTo(parent.start, 8.dp)
                    width = Dimension.fillToConstraints
                }
                constrain(time){
                    top.linkTo(message.bottom)
                    end.linkTo(message.end)
                }
            }
        }
    }

}
