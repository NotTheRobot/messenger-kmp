package org.company.app.utils

import androidx.compose.runtime.Composable
import androidx.compose.ui.unit.IntSize

enum class PlatformSize{
    Small,
    Large
}
enum class PlatformName{
    Android,
    IOS,
    Desktop,
    js
}
expect class PlatformSpecific {
    companion object{
        fun getPlatformName(): PlatformName
        @Composable
        fun getWindowWidth(): Int
    }
}
