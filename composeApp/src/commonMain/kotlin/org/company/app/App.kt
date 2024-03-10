package org.company.app

import androidx.compose.runtime.Composable
import cafe.adriel.voyager.navigator.Navigator
import org.company.app.data.repository.Repository
import org.company.app.theme.AppTheme
import org.company.app.ui.screns.ChatListScreen
import org.company.app.ui.screns.ConstraintPreview
import org.company.app.ui.screns.LoginScreen
import org.koin.core.context.startKoin
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

@Composable
internal fun App() = AppTheme() {
    Navigator(LoginScreen())
}

internal expect fun openUrl(url: String?)
