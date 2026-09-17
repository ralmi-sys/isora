package net.isora.vpn.compat

import androidx.window.core.layout.WindowSizeClass

object WindowSizeClassCompat {
    const val WIDTH_DP_MEDIUM_LOWER_BOUND = 600
    const val WIDTH_DP_EXPANDED_LOWER_BOUND = 840
}

fun WindowSizeClass.isWidthAtLeastBreakpointCompat(breakpointDp: Int): Boolean = isWidthAtLeastBreakpoint(breakpointDp)
