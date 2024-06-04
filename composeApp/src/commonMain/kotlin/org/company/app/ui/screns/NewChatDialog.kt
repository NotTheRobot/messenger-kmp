package org.company.app.ui.screns

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import compose.icons.TablerIcons
import compose.icons.tablericons.CircleMinus
import org.company.app.data.models.network.NewChatDTO
import org.company.app.theme.*

class NewChatDialogScreen(val screenModel: ChatListScreenModel): Screen {

    @Composable
    override fun Content() {
        val navigator = LocalNavigator.currentOrThrow
        val users = remember { mutableStateListOf<String>("") }
        var chatNameText by remember { mutableStateOf("") }

        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .fillMaxSize()
                .background(Color.DarkSurface)
        ) {
            ConstraintLayout(
                modifier = Modifier
                    .clip(RoundedCornerShape(16.dp))
                    .background(Color.DarkContainer)
                    .padding(64.dp)
            ) {
                val (headerText, usersList, createButton, cancelButton, addUserButton, chatNameTF) = createRefs()
                Text(
                    text = "Create new chat",
                    color = Color.DarkDefaultText,
                    modifier = Modifier.constrainAs(headerText) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                        width = Dimension.wrapContent
                        height = Dimension.wrapContent
                    }
                )
                TextButton(
                    onClick = { users.add("") },
                    modifier = Modifier.constrainAs(addUserButton) {
                        top.linkTo(usersList.bottom, 16.dp)
                        start.linkTo(usersList.start)
                        width = Dimension.wrapContent
                        height = Dimension.wrapContent
                    }
                ) {
                    Text("Add user", color = Color.MainAccentBlue)
                }
                TextButton(
                    onClick = { navigator.pop() },
                    modifier = Modifier.constrainAs(cancelButton){
                        top.linkTo(addUserButton.bottom, 32.dp)
                        start.linkTo(addUserButton.start)
                    }
                ) {
                    Text("Cancel", color = Color.MainAccentBlue)
                }
                TextButton(
                    onClick = {
                        screenModel.createChatRequest(NewChatDTO(chatNameText, null, users))
                        navigator.pop()
                    },
                    modifier = Modifier.constrainAs(createButton){
                        top.linkTo(cancelButton.top)
                        end.linkTo(usersList.end)
                    }
                    ) {
                    Text("Create", color = Color.MainAccentBlue)
                }
                OutlinedTextField(
                    value = chatNameText,
                    onValueChange = { chatNameText = it },
                    placeholder = { Text("Chat name", color = Color.DarkSecondaryText) },
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        textColor = Color.DarkDefaultText,
                        focusedBorderColor = Color.DarkDefaultText,
                        unfocusedBorderColor = Color.DarkSecondaryText,
                        errorBorderColor = Color.Transparent,
                        disabledBorderColor = Color.Transparent,
                        backgroundColor = Color.Transparent
                    ),
                    modifier = Modifier.constrainAs(chatNameTF){
                        top.linkTo(headerText.bottom, 32.dp)
                        start.linkTo(parent.start)
                    }
                )
                LazyColumn(
                    modifier = Modifier.constrainAs(usersList){
                        top.linkTo(chatNameTF.bottom, 16.dp)
                        start.linkTo(chatNameTF.start)
                    }
                ) {
                    itemsIndexed(users) { index, username ->
                        Row() {
                            OutlinedTextField(
                                value = username,
                                onValueChange = { users[index] = it },
                                singleLine = true,
                                placeholder = { Text("Username", color = Color.DarkSecondaryText) },
                                colors = TextFieldDefaults.outlinedTextFieldColors(
                                    textColor = Color.DarkDefaultText,
                                    focusedBorderColor = Color.DarkDefaultText,
                                    unfocusedBorderColor = Color.DarkSecondaryText,
                                    errorBorderColor = Color.Transparent,
                                    disabledBorderColor = Color.Transparent,
                                    backgroundColor = Color.Transparent
                                )
                            )
                            Spacer(modifier = Modifier.width(16.dp))
                            IconButton(
                                onClick = { users.removeAt(index) },
                            ) {
                                Icon(
                                    TablerIcons.CircleMinus,
                                    contentDescription = null,
                                    tint = Color.MainAccentBlue
                                )
                            }
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }


            }
        }
    }
}
