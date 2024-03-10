package org.company.app.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.platform.LocalWindowInfo

actual class PlatformSpecific {
    actual companion object{
        actual fun getPlatformName(): PlatformName = PlatformName.IOS
        @OptIn(ExperimentalComposeUiApi::class)
        @Composable
        actual fun getWindowWidth(): Int {
            return LocalWindowInfo.current.containerSize.width
        }
    }
}
