package net.isora.vpn.compose.navigation

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.TextSnippet
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Storage
import androidx.compose.material.icons.filled.SwapVert
import androidx.compose.material.icons.filled.Terminal
import androidx.compose.ui.graphics.vector.ImageVector
import net.isora.vpn.R

sealed class Screen(val route: String, @StringRes val titleRes: Int, val icon: ImageVector) {
    object Dashboard : Screen(
        route = "dashboard",
        titleRes = R.string.title_dashboard,
        icon = Icons.Default.Dashboard,
    )

    object Log : Screen(
        route = "log",
        titleRes = R.string.title_log,
        icon = Icons.AutoMirrored.Default.TextSnippet,
    )

    object Groups : Screen(
        route = "groups",
        titleRes = R.string.title_groups,
        icon = Icons.Default.Folder,
    )

    object Connections : Screen(
        route = "connections",
        titleRes = R.string.title_connections,
        icon = Icons.Default.SwapVert,
    )

    object Tools : Screen(
        route = "tools",
        titleRes = R.string.title_tools,
        icon = Icons.Default.Terminal,
    )

    object Settings : Screen(
        route = "settings",
        titleRes = R.string.title_settings,
        icon = Icons.Default.Settings,
    )

    object Home : Screen(
        route = "home",
        titleRes = R.string.title_isora_home,
        icon = Icons.Default.Home,
    )

    object Servers : Screen(
        route = "servers",
        titleRes = R.string.title_isora_servers,
        icon = Icons.Default.Storage,
    )

    object Account : Screen(
        route = "account",
        titleRes = R.string.title_isora_account,
        icon = Icons.Default.Person,
    )
}

val bottomNavigationScreens =
    listOf(
        Screen.Home,
        Screen.Servers,
        Screen.Account,
    )
