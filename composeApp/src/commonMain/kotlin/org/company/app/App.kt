package org.company.app

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.navigator.Navigator
import org.company.app.theme.AppTheme
import org.company.app.ui.screns.LoginScreen

@Composable
internal fun App() = AppTheme() {
    Navigator(LoginScreen())
}

internal expect fun openUrl(url: String?)