package org.company.app.ui.screns

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow
import org.company.app.theme.DarkDefaultText
import org.company.app.theme.DarkSurface
import org.company.app.theme.MainAccentBlue
import org.company.app.theme.MainAccentBlueGlow

class LoginScreen : Screen{
    @Composable
    override fun Content() {
        val screenModel = rememberScreenModel { LoginScreenModel() }
        var isRegistrationScreen by screenModel.isRegistrationScreenMode

        val navigator = LocalNavigator.currentOrThrow

        var emailOrUsername by screenModel.username
        var password by screenModel.password
        var passwordConfirmation by screenModel.passwordConfirmation
        var passwordVisibility by screenModel.passwordVisibility
        var isError by screenModel.isError
        var continueButtonText by screenModel.continueButtonText
        var changeScreenButtonText by screenModel.changeScreenButtonText
        var alterName by screenModel.alterName

        Box(
            contentAlignment = Alignment.TopCenter,
            modifier = Modifier
            .background(Color.DarkSurface)
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.safeDrawing)) {
            Column(
                modifier = Modifier.widthIn(min = 240.dp, max = 640.dp)
            ) {
                Spacer(modifier = Modifier.height(64.dp))
                OutlinedTextField(
                    value = emailOrUsername,
                    onValueChange = { emailOrUsername = it },
                    label = { Text("Username") },
                    singleLine = true,
                    isError = isError,
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        textColor = Color.DarkDefaultText,
                        focusedBorderColor = Color.MainAccentBlueGlow,
                        unfocusedBorderColor = Color.MainAccentBlue,
                        focusedLabelColor = Color.MainAccentBlueGlow,
                        unfocusedLabelColor = Color.MainAccentBlue
                    ),
                    modifier = Modifier.fillMaxWidth().padding(16.dp)
                )

                if (isRegistrationScreen) {
                    OutlinedTextField(
                        value = alterName,
                        onValueChange = { alterName = it },
                        label = { Text("Altername") },
                        singleLine = true,
                        isError = isError,
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            textColor = Color.DarkDefaultText,
                            focusedBorderColor = Color.MainAccentBlueGlow,
                            unfocusedBorderColor = Color.MainAccentBlue,
                            focusedLabelColor = Color.MainAccentBlueGlow,
                            unfocusedLabelColor = Color.MainAccentBlue
                        ),
                        modifier = Modifier.fillMaxWidth().padding(16.dp),
                    )
                }

                OutlinedTextField(
                    value = password,
                    onValueChange = { password = it },
                    label = { Text("Password") },
                    singleLine = true,
                    isError = isError,
                    visualTransformation = if (passwordVisibility) VisualTransformation.None else PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth().padding(16.dp),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Password
                    ),
                    trailingIcon = {
                        IconButton(onClick = { passwordVisibility = !passwordVisibility }) {
                            val imageVector = if (passwordVisibility) Icons.Default.Close else Icons.Default.Edit
                            Icon(
                                imageVector,
                                contentDescription = if (passwordVisibility) "Hide password" else "Show password",
                                tint = Color.DarkDefaultText
                            )
                        }
                    },
                    colors = TextFieldDefaults.outlinedTextFieldColors(
                        textColor = Color.DarkDefaultText,
                        focusedBorderColor = Color.MainAccentBlueGlow,
                        unfocusedBorderColor = Color.MainAccentBlue,
                        focusedLabelColor = Color.MainAccentBlueGlow,
                        unfocusedLabelColor = Color.MainAccentBlue
                    ),
                )

                if (isRegistrationScreen) {
                    OutlinedTextField(
                        value = passwordConfirmation,
                        onValueChange = { passwordConfirmation = it },
                        label = { Text("Confirm password") },
                        singleLine = true,
                        isError = isError,
                        visualTransformation = if (passwordVisibility) VisualTransformation.None else PasswordVisualTransformation(),
                        modifier = Modifier.fillMaxWidth().padding(16.dp),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Password
                        ),
                        trailingIcon = {
                            IconButton(onClick = { passwordVisibility = !passwordVisibility }) {
                                val imageVector = if (passwordVisibility) Icons.Default.Close else Icons.Default.Edit
                                Icon(
                                    imageVector,
                                    contentDescription = if (passwordVisibility) "Hide password" else "Show password",
                                    tint = Color.DarkDefaultText
                                )
                            }
                        },
                        colors = TextFieldDefaults.outlinedTextFieldColors(
                            textColor = Color.DarkDefaultText,
                            focusedBorderColor = Color.MainAccentBlueGlow,
                            unfocusedBorderColor = Color.MainAccentBlue,
                            focusedLabelColor = Color.MainAccentBlueGlow,
                            unfocusedLabelColor = Color.MainAccentBlue
                        ),
                    )
                }


                TextButton(
                    onClick = { screenModel.continueButtonClicked(navigator) },
                    modifier = Modifier.fillMaxWidth().padding(16.dp).background(Color.MainAccentBlue)
                ) {
                    Text(
                        continueButtonText,
                        color = Color.DarkDefaultText,
                        modifier = Modifier.background(Color.Transparent),
                    )
                }

                TextButton(
                    onClick = { screenModel.changeScreenMode() },
                    modifier = Modifier.fillMaxWidth().padding(16.dp).background(Color.MainAccentBlue)
                ) {
                    Text(
                        changeScreenButtonText,
                        color = Color.DarkDefaultText,
                        modifier = Modifier.background(Color.Transparent)
                    )
                }
            }
        }
    }
}
