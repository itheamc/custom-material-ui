package com.example.fashionhub.ui.scaffold

import android.app.Activity
import androidx.annotation.VisibleForTesting
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.toComposeRect
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.window.layout.WindowMetricsCalculator

enum class ScreenSize { Compact, Medium, Large, Expanded }

/**
 * Remembers the [ScreenSize] class for the window corresponding to the current window metrics.
 */
@Composable
fun Activity.rememberScreenSize(): ScreenSize {
    // Get the size (in pixels) of the window
    val windowSize = rememberSize()

    // Convert the window size to [Dp]
    val windowDpSize = with(LocalDensity.current) {
        windowSize.toDpSize()
    }

    // Calculate the window size class
    return getScreenSize(windowDpSize)
}

/**
 * Remembers the [Size] in pixels of the window corresponding to the current window metrics.
 */
@Composable
private fun Activity.rememberSize(): Size {
    val configuration = LocalConfiguration.current
    // WindowMetricsCalculator implicitly depends on the configuration through the activity,
    // so re-calculate it upon changes.
    val windowMetrics = remember(configuration) {
        WindowMetricsCalculator.getOrCreate().computeCurrentWindowMetrics(this)
    }
    return windowMetrics.bounds.toComposeRect().size
}

/**
 * Partitions a [DpSize] into a enumerated WindowSize class.
 */
@VisibleForTesting
fun getScreenSize(windowDpSize: DpSize): ScreenSize = when {
    windowDpSize.width < 600.dp -> ScreenSize.Compact
    windowDpSize.width < 840.dp -> ScreenSize.Medium
    windowDpSize.width < 1080.dp -> ScreenSize.Large
    else -> ScreenSize.Expanded
}