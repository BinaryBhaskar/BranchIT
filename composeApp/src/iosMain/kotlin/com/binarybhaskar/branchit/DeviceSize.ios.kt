package com.binarybhaskar.branchit

import androidx.compose.runtime.Composable
import platform.UIKit.UIScreen
import kotlinx.cinterop.ExperimentalForeignApi

@OptIn(ExperimentalForeignApi::class)
@Composable
actual fun isLargeScreen(): Boolean {
    val width = UIScreen.mainScreen.bounds.size
    return width >= 600.0
}
