package net.isora.vpn.compose.screen.isora

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import net.isora.vpn.compose.screen.dashboard.DashboardViewModel
import net.isora.vpn.compose.screen.dashboard.groups.GroupsViewModel
import net.isora.vpn.compose.screen.isora.ui.components.CountryCode
import net.isora.vpn.compose.screen.isora.data.DefaultServers
import net.isora.vpn.compose.screen.isora.data.VpnServer
import net.isora.vpn.compose.screen.isora.ui.screens.AccountScreen
import net.isora.vpn.compose.screen.isora.ui.screens.ConnectionState
import net.isora.vpn.compose.screen.isora.ui.screens.ServersScreen
import net.isora.vpn.compose.screen.isora.ui.screens.VpnHomeScreen
import net.isora.vpn.constant.Status

const val ISORA_SELECTOR_TAG = "ISORA"

/** Маппинг тега outbound из подписки (/api/singbox) в карточку сервера. */
fun serverForTag(tag: String, pingMs: Int): VpnServer {
    val (country, city, code) = when {
        tag.contains("Авто") -> Triple("Авто", "Умный выбор", CountryCode.EU)
        tag.contains("NL-Game") -> Triple("Нидерланды", "Amsterdam · Game", CountryCode.NL)
        tag.contains("NL") -> Triple("Нидерланды", "Amsterdam", CountryCode.NL)
        tag.contains("FI-Game") -> Triple("Финляндия", "Helsinki · Game", CountryCode.FI)
        tag.contains("FI") -> Triple("Финляндия", "Helsinki", CountryCode.FI)
        tag.contains("DE") -> Triple("Германия", "Frankfurt", CountryCode.DE)
        tag.contains("FR") -> Triple("Франция", "Paris", CountryCode.FR)
        else -> Triple(tag, "", CountryCode.EU)
    }
    val isAuto = tag.contains("Авто")
    return VpnServer(
        id = tag,
        country = country,
        city = city,
        pingMs = pingMs,
        countryCode = code,
        loadPercent = 0,
        isRecommended = isAuto || tag.contains("NL"),
        isFast = !tag.contains("FR"),
        isStreaming = isAuto || tag.contains("NL"),
    )
}

fun Status.toConnectionState(): ConnectionState =
    when (this) {
        Status.Started -> ConnectionState.Connected
        Status.Starting, Status.Stopping -> ConnectionState.Connecting
        Status.Stopped -> ConnectionState.Disconnected
    }

@Composable
fun IsoraHomeRoute(
    dashboardViewModel: DashboardViewModel,
    groupsViewModel: GroupsViewModel?,
    serviceStatus: Status,
    onOpenServers: () -> Unit,
    onOpenAccount: () -> Unit,
) {
    val dash by dashboardViewModel.uiState.collectAsState()
    val groups = groupsViewModel?.uiState?.collectAsState()?.value
    val selector = groups?.groups?.find { it.tag == ISORA_SELECTOR_TAG }
    val selTag = selector?.selected
    val selPing = selector?.items?.find { it.tag == selTag }?.urlTestDelay?.takeIf { it > 0 } ?: 0
    val current = if (selTag != null) serverForTag(selTag, selPing) else DefaultServers.list.first()
    val hasProfile = dash.profiles.isNotEmpty()

    // Один профиль = наша подписка: выбираем автоматически, руками не трогаем.
    LaunchedEffect(dash.profiles, dash.selectedProfileId) {
        if (dash.selectedProfileId == -1L && dash.profiles.isNotEmpty()) {
            dashboardViewModel.selectProfile(dash.profiles.first().id)
        }
    }

    VpnHomeScreen(
        state = serviceStatus.toConnectionState(),
        currentServer = current,
        // Без профиля нечего запускать — ведём в Аккаунт за ссылкой,
        // а не в диалог «Пустая конфигурация».
        onToggleConnection = {
            if (hasProfile) dashboardViewModel.toggleService() else onOpenAccount()
        },
        onOpenServers = onOpenServers,
    )
}

@Composable
fun IsoraServersRoute(
    groupsViewModel: GroupsViewModel?,
    onSelectDone: () -> Unit,
) {
    val groups = groupsViewModel?.uiState?.collectAsState()?.value
    val selector = groups?.groups?.find { it.tag == ISORA_SELECTOR_TAG }

    LaunchedEffect(selector?.tag) {
        if (selector != null) groupsViewModel?.urlTestGroup(selector.tag)
    }

    val servers =
        if (selector != null) {
            selector.items.map { serverForTag(it.tag, it.urlTestDelay.takeIf { d -> d > 0 } ?: 0) }
        } else {
            DefaultServers.list
        }
    val current =
        selector?.selected?.let { serverForTag(it, 0) } ?: servers.first()

    ServersScreen(
        currentServer = current,
        servers = servers,
        onSelectServer = { s ->
            if (selector != null) groupsViewModel?.selectGroupItem(selector.tag, s.id)
            onSelectDone()
        },
    )
}

@Composable
fun IsoraAccountRoute(
    dashboardViewModel: DashboardViewModel,
    groupsViewModel: GroupsViewModel?,
    serviceStatus: Status,
    onManageSubscription: () -> Unit,
) {
    val dash by dashboardViewModel.uiState.collectAsState()
    val groups = groupsViewModel?.uiState?.collectAsState()?.value
    val selTag = groups?.groups?.find { it.tag == ISORA_SELECTOR_TAG }?.selected
    val profileName = dash.selectedProfileName ?: "ISORA"
    val status1 =
        when (serviceStatus) {
            Status.Started -> "Подключено"
            Status.Starting -> "Подключение…"
            Status.Stopping -> "Отключение…"
            Status.Stopped -> "Отключено"
        }
    val status2 = if (selTag != null) "Сервер: $selTag" else "Профиль: $profileName"
    AccountScreen(
        userEmail = profileName,
        planTitle = "ISORA VPN",
        planBadge = "PRO",
        statusLine1 = status1,
        statusLine2 = status2,
        versionBadge = "",
        onManageSubscription = onManageSubscription,
    )
}
