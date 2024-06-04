package org.company.app.ui.screns

import androidx.compose.runtime.mutableStateOf
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.core.model.screenModelScope
import cafe.adriel.voyager.navigator.Navigator
import kotlinx.coroutines.launch
import org.company.app.data.Repository
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

class LoginScreenModel: ScreenModel, KoinComponent {
    val repository: Repository by inject()
    val isRegistrationScreenMode = mutableStateOf(false)
    val username = mutableStateOf("kekw")
    val password = mutableStateOf("228322")
    val alterName = mutableStateOf("admin")
    val passwordConfirmation = mutableStateOf("")
    val passwordVisibility = mutableStateOf(false)
    var continueButtonText = mutableStateOf("Sign in")
    var changeScreenButtonText  = mutableStateOf("Sign up")
    var isError = mutableStateOf(false)

    fun changeScreenMode(){
        isRegistrationScreenMode.value = !isRegistrationScreenMode.value
        if(isRegistrationScreenMode.value){
            continueButtonText.value = "Sign up"
            changeScreenButtonText.value = "Sign in"
        }else{
            continueButtonText.value = "Sign in"
            changeScreenButtonText.value = "Sign up"
        }
    }

    fun continueButtonClicked(navigator: Navigator) {
        if(isRegistrationScreenMode.value){
            screenModelScope.launch {
                val result = repository.signUp(username.value, alterName.value, password.value, null)
            }
        }else{
            screenModelScope.launch {
                val result = repository.signIn(username.value, password.value)
                if(result){
                    navigator.push(ChatListScreen())
                }
                else{
                    isError.value = true
                }
            }
        }
    }
}

