package com.binarybhaskar.branchit

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalConfiguration

@Composable
actual fun isLargeScreen(): Boolean {
    // Use configuration to check for large screens (tablets, foldables)
    val configuration = LocalConfiguration.current
    val screenWidthDp = configuration.screenWidthDp
    return screenWidthDp >= 600
}