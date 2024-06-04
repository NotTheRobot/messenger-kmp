package org.company.app.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.IntSize

actual class PlatformSpecific {
    actual companion object{
        actual fun getPlatformName(): PlatformName = PlatformName.Android
        @Composable
        actual fun getWindowWidth(): Int {
            return LocalConfiguration.current.screenWidthDp
        }

    }

}
