package org.company.app.ui.screns

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import cafe.adriel.voyager.core.model.rememberScreenModel
import cafe.adriel.voyager.core.screen.Screen
import cafe.adriel.voyager.navigator.LocalNavigator
import cafe.adriel.voyager.navigator.currentOrThrow

class LoginScreen : Screen{
    @Composable
    override fun Content() {
        val screenModel = rememberScreenModel { LoginScreenModel() }
        var isRegistrationScreen by screenModel.isRegistrationScreenMode

        val navigator = LocalNavigator.currentOrThrow

        var emailOrUsername by screenModel.emailOrUsername
        var password by screenModel.password
        var passwordConfirmation by screenModel.passwordConfirmation
        var passwordVisibility by screenModel.passwordVisibility
        var isError by screenModel.isError
        var continueButtonText by screenModel.continueButtonText
        var changeScreenButtonText by screenModel.changeScreenButtonText

        Column(modifier = Modifier
            .fillMaxSize()
            .windowInsetsPadding(WindowInsets.safeDrawing)
        ) {

            OutlinedTextField(
                value = emailOrUsername,
                onValueChange = { emailOrUsername = it },
                label = { Text("Email") },
                singleLine = true,
                isError = isError,
                modifier = Modifier.fillMaxWidth().padding(16.dp)
            )

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
                        Icon(imageVector, contentDescription = if (passwordVisibility) "Hide password" else "Show password")
                    }
                }
            )

            if(isRegistrationScreen){
                OutlinedTextField(
                    value = passwordConfirmation,
                    onValueChange = { passwordConfirmation = it },
                    label = { Text("Repeat password") },
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
                            Icon(imageVector, contentDescription = if (passwordVisibility) "Hide password" else "Show password")
                        }
                    }
                )
            }

            Button(
                onClick = { screenModel.continueButtonClicked(navigator) },
                modifier = Modifier.fillMaxWidth().padding(16.dp)
            ) {
                Text(continueButtonText)
            }

            Button(
                onClick = {screenModel.changeScreenMode()},
                modifier = Modifier.fillMaxWidth().padding(16.dp)
            ){
                Text(changeScreenButtonText)
            }
        }
    }

}