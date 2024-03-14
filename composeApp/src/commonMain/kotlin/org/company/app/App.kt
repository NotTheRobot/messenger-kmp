package org.company.app

import androidx.compose.runtime.*
import cafe.adriel.voyager.navigator.Navigator
import org.company.app.theme.AppTheme
import org.company.app.ui.screns.LoginScreen

@Composable
internal fun App() = AppTheme() {
    var isOpen by remember{ mutableStateOf(false) }
    if(!isOpen){
        isOpen = true

    }
    Navigator(LoginScreen())
}

internal expect fun openUrl(url: String?)
