package org.company.app

import androidx.compose.foundation.layout.*
import androidx.compose.material.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import cafe.adriel.voyager.navigator.Navigator
import org.company.app.theme.AppTheme
import org.company.app.ui.screns.LoginScreen

@Composable
internal fun App() = AppTheme {
    Box(modifier = Modifier.windowInsetsPadding(WindowInsets.safeDrawing)) {
        Navigator(
            screen = LoginScreen(),
            onBackPressed = null
        )
    }
}

internal expect fun openUrl(url: String?)
