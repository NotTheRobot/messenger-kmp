package org.company.app.ui.screns

import androidx.compose.runtime.mutableStateOf
import cafe.adriel.voyager.core.model.ScreenModel
import cafe.adriel.voyager.navigator.Navigator

class LoginScreenModel: ScreenModel {
    val passwordConfirmation = mutableStateOf("")
    val isRegistrationScreenMode = mutableStateOf(false)
    val emailOrUsername = mutableStateOf("")
    val password = mutableStateOf("")
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
            isError.value = !isError.value

        }else{

        }
    }
}

