package org.company.app.ui.screns

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.layoutId
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintSet
import androidx.constraintlayout.compose.Dimension
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import compose.icons.TablerIcons
import compose.icons.tablericons.*
import firstmultiplatfporm.composeapp.generated.resources.Res
import firstmultiplatfporm.composeapp.generated.resources.mcqueen
import org.company.app.data.mappers.differenceToString
import org.company.app.data.mappers.toCustomString
import org.company.app.theme.*
import org.jetbrains.compose.resources.painterResource

class ChatListScreen: Screen {

    @Composable
    override fun Content() {
        val screenModel = rememberScreenModel { ChatListScreenModel() }
        val chats = screenModel.listOfChats.value.collectAsState(initial = listOf()).value
        BoxWithConstraints {
            if (maxWidth > 720.dp){
                Row(modifier = Modifier){
                    leftFrame(
                        chats = chats,
                        screenModel = screenModel,
                        modifier = Modifier.width(360.dp)
                    )
                    rightFrame(screenModel)
                }
            }else if(screenModel.currentFrame.value == CurrentFrame.Left){
                leftFrame(
                    chats = chats,
                    screenModel = screenModel,
                    modifier = Modifier
                        .fillMaxWidth()
                )
            }else if(screenModel.currentFrame.value == CurrentFrame.Right){
                rightFrame(
                    screenModel
                )
            }
        }
    }

    @Composable
    fun leftFrame(
        chats: List<ChatInfo>,
        screenModel: ChatListScreenModel,
        modifier: Modifier
    ){
        val navigator = LocalNavigator.currentOrThrow
        Column(modifier = modifier.background(Color.DarkSurface)) {
            Row(
                modifier = Modifier
                    .height(56.dp)
                    .fillMaxWidth()
                    .background(Color.DarkContainer)
            ) {
                IconButton(
                    onClick = { screenModel.updateFromNetwork() }
                ) {
                    Icon(TablerIcons.Refresh, null, tint = Color.MainAccentBlue)
                }
                IconButton(
                    onClick = { navigator.push(NewChatDialogScreen(screenModel)) },
                ){
                    Icon(TablerIcons.MessagePlus, contentDescription = null, tint = Color.MainAccentBlue)
                }
            }
            LazyColumn(
                modifier = modifier
                    .windowInsetsPadding(WindowInsets.safeDrawing)
                    .fillMaxHeight()
                    .padding(start = 8.dp, top = 8.dp, end = 8.dp)

            ) {
                items(chats) {
                    ChatContainer(it, screenModel)
                }
            }
        }
    }
    @Composable
    fun ChatContainer(chatInfo: ChatInfo, screenModel: ChatListScreenModel) {
        Button(
            onClick = {
                screenModel.onChatClick(chatInfo.chatId)
            },
            colors = ButtonDefaults.buttonColors(
                backgroundColor = Color.DarkContainer
            ),
            elevation = ButtonDefaults.elevation(defaultElevation = 0.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
            ) {
                val image = painterResource(Res.drawable.mcqueen)
                Image(
                    image,
                    null,
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
                    val textBarrier = createStartBarrier(time, stateMessage)

                    Text(
                        textAlign = TextAlign.Start,
                        color = Color.DarkDefaultText,
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
                        chatInfo.lastSender,
                        fontSize = 12.sp,
                        color = Color.DarkTertiaryText,
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
                        chatInfo.lastMessage,
                        color = Color.DarkSecondaryText,
                        fontSize = 12.sp,
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
                        chatInfo.time.toCustomString(),
                        color = Color.DarkTertiaryText,
                        fontSize = 12.sp,
                        modifier = Modifier.constrainAs(time) {
                            top.linkTo(parent.top, 8.dp)
                            end.linkTo(parent.end, 8.dp)
                        }
                    )
                    if (chatInfo.newMessages < 1) {
                        Icon(
                            if (chatInfo.isRead) TablerIcons.Checks else TablerIcons.Check,
                            null,
                            tint = Color.MainAccentBlue,
                            modifier = Modifier.constrainAs(stateMessage) {
                                bottom.linkTo(parent.bottom, 8.dp)
                                end.linkTo(parent.end, 8.dp)
                            }
                        )
                    } else {
                        Box(
                            contentAlignment = Alignment.Center,
                            modifier = Modifier
                                .clip(RoundedCornerShape(4.dp))
                                .background(Color.MainAccentBlue)
                                .defaultMinSize(minWidth = 40.dp)
                                .constrainAs(stateMessage) {
                                    bottom.linkTo(parent.bottom, 8.dp)
                                    end.linkTo(parent.end, 8.dp)
                                }
                        ) {
                            Text(
                                chatInfo.newMessages.toString(),
                                textAlign = TextAlign.Center,
                                color = Color.DarkDefaultText,
                                modifier = Modifier.padding(6.dp))
                        }
                    }


                }
            }
        }
    }
    @Composable
    private fun rightFrame(screenModel: ChatListScreenModel) {
        var messageTF by screenModel.messageTF
        val messagesFlow by screenModel.messages
        val messagesList = messagesFlow.collectAsState(initial = listOf()).value
        val currentChatId by screenModel.currentChatId
        val frameMode by screenModel.currentFrame
        val lazyColumnState = LazyListState(firstVisibleItemIndex = Int.MAX_VALUE)

        ConstraintLayout(modifier = Modifier.fillMaxSize().background(Color.DarkSurface)) {
            val (messages, editField, sendButton, topBar) = createRefs()
            if(frameMode == CurrentFrame.Left){ }
            else if(messagesList.isEmpty()){
                Text(
                    text = "Напишите что-нибудь",
                    fontSize = 20.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier
                        .constrainAs(messages){
                            top.linkTo(parent.top)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                            bottom.linkTo(editField.top)
                            width = Dimension.matchParent
                            height = Dimension.matchParent
                        }
                )
            }else {
                Row(modifier = Modifier
                    .background(Color.DarkContainer)
                    .height(56.dp)
                    .constrainAs(topBar){
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        width = Dimension.fillToConstraints
                    }
                ){
                    IconButton(
                        onClick = { screenModel.currentFrame.value = CurrentFrame.Left }
                    ){
                        Icon(TablerIcons.ArrowLeft, null, tint = Color.MainAccentBlue)
                    }
                }
                LazyColumn(
                    state = lazyColumnState,
                    modifier = Modifier
                        .constrainAs(messages) {
                            top.linkTo(topBar.bottom)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                            bottom.linkTo(editField.top)
                            width = Dimension.fillToConstraints
                            height = Dimension.fillToConstraints
                        }
                ) {
                    items(messagesList) {
                        MessageBlock(it, screenModel)
                    }
                }
            }
            if(screenModel.currentFrame.value == CurrentFrame.Right) {
                TextField(
                    value = messageTF,
                    onValueChange = { messageTF = it },
                    placeholder = { Text("Сообщение...", color = Color.DarkSecondaryText) },
                    colors = TextFieldDefaults.textFieldColors(
                        textColor = Color.DarkDefaultText,
                        backgroundColor = Color.DarkContainer,
                        focusedIndicatorColor = Color.Transparent,
                        errorIndicatorColor = Color.Transparent,
                        disabledIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent
                    ),
                    modifier = Modifier
                        .heightIn(max = 172.dp)
                        .constrainAs(editField) {
                            bottom.linkTo(parent.bottom)
                            start.linkTo(parent.start)
                            end.linkTo(sendButton.start)
                            width = Dimension.fillToConstraints
                            height = Dimension.wrapContent
                        }
                )
                Button(
                    onClick = { screenModel.onSendClick() },
                    shape = RectangleShape,
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = Color.DarkSurface
                    ),
                    elevation = ButtonDefaults.elevation(defaultElevation = 0.dp),
                    modifier = Modifier
                        .constrainAs(sendButton) {
                            top.linkTo(editField.top)
                            end.linkTo(parent.end)
                            bottom.linkTo(parent.bottom)
                            width = Dimension.wrapContent
                            height = Dimension.fillToConstraints
                        }
                ) {
                    Icon(
                        TablerIcons.Send,
                        contentDescription = null,
                        tint = Color.MainAccentBlue,
                        modifier = Modifier
                            .size(48.dp)
                    )
                }
            }
        }
    }

    @Composable
    fun MessageBlock(item: MessageInfo, screenModel: ChatListScreenModel) {
        val constraints = getConstraintsFor(item.senderUsername, screenModel.getCurrentUsername())
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
                item.senderName,
                color = Color.DarkDefaultText,
                modifier = Modifier.layoutId("sender")
            )
            Box(modifier = Modifier
                .layoutId("message")
                .widthIn(max = 640.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(Color.DarkContainer)) {
                Text(
                    item.message,
                    color = Color.DarkDefaultText,
                    modifier = Modifier
                        .padding(16.dp)
                )
            }
            Text(
                item.time.toCustomString(),
                color = Color.DarkTertiaryText,
                modifier = Modifier.layoutId("time")
            )
        }
    }

    private fun getConstraintsFor(sender: String, currentUsername: String): ConstraintSet {
        return ConstraintSet{
            val imageProfile = createRefFor("imageProfile")
            val senderRef = createRefFor("sender")
            val message = createRefFor("message")
            val time = createRefFor("time")
            if(sender == currentUsername){
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
                    end.linkTo(message.end)
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
                    start.linkTo(message.start)
                }
            }
        }
    }

}
