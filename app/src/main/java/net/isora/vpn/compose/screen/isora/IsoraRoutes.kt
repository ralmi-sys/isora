package net.isora.vpn.compose.screen.isora

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import net.isora.vpn.compose.screen.dashboard.DashboardViewModel
import net.isora.vpn.compose.screen.dashboard.groups.GroupsViewModel
import net.isora.vpn.compose.screen.isora.ui.components.CountryCode
import net.isora.vpn.compose.screen.isora.data.DefaultServers
import net.isora.vpn.compose.screen.isora.data.VpnServer
import net.isora.vpn.compose.screen.isora.ui.screens.AccountScreen
import net.isora.vpn.compose.screen.isora.ui.screens.MirageCard
import net.isora.vpn.compose.screen.isora.ui.screens.ConnectionState
import net.isora.vpn.compose.screen.isora.ui.screens.ServersScreen
import net.isora.vpn.compose.screen.isora.ui.screens.VpnHomeScreen
import net.isora.vpn.constant.Status

const val ISORA_SELECTOR_TAG = "ISORA"

private const val ISORA_PREFS = "isora_ui"
private const val KEY_PENDING_SERVER = "pending_server"
private const val KEY_MANUAL_AT = "manual_select_at"
private const val KEY_AUTO_AT = "autoswitch_at"
private const val KEY_FAILS = "fail_streak"
private const val KEY_FAIL_AT = "fail_last_at"

/** Курированный список приложения (владелец, 23.09): только живое. */
fun isCuratedTag(tag: String): Boolean =
    tag.contains("Авто") || tag.contains("Без рекламы") || tag.contains("Mirage") ||
        tag.contains("DE") || tag.contains("SE") ||
        tag == "🇳🇱 NL-Game"

fun savePendingServer(context: android.content.Context, tag: String) {
    context.getSharedPreferences(ISORA_PREFS, android.content.Context.MODE_PRIVATE)
        .edit().putString(KEY_PENDING_SERVER, tag).apply()
}

fun peekPendingServer(context: android.content.Context): String? {
    return context.getSharedPreferences(ISORA_PREFS, android.content.Context.MODE_PRIVATE)
        .getString(KEY_PENDING_SERVER, null)
}

fun takePendingServer(context: android.content.Context): String? {
    val prefs = context.getSharedPreferences(ISORA_PREFS, android.content.Context.MODE_PRIVATE)
    val tag = prefs.getString(KEY_PENDING_SERVER, null)
    if (tag != null) prefs.edit().remove(KEY_PENDING_SERVER).apply()
    return tag
}

fun saveManualAt(context: android.content.Context) {
    context.getSharedPreferences(ISORA_PREFS, android.content.Context.MODE_PRIVATE)
        .edit().putLong(KEY_MANUAL_AT, System.currentTimeMillis()).apply()
}

/** Маппинг тега outbound из подписки (/api/singbox) в карточку сервера. */
fun serverForTag(tag: String, pingMs: Int): VpnServer {
    val (country, city, code) = when {
        tag.contains("Авто") -> Triple("Авто", "Умный выбор", CountryCode.EU)
        tag.contains("Без рекламы") -> Triple("Без рекламы", "Без трекеров", CountryCode.EU)
        tag.contains("Mirage") -> Triple("Mirage", "Новая дверь · тест", CountryCode.SE)
        tag.contains("NL-Game") -> Triple("Нидерланды", "Amsterdam · Game", CountryCode.NL)
        tag.contains("NL") -> Triple("Нидерланды", "Amsterdam", CountryCode.NL)
        tag.contains("FI-Game") -> Triple("Финляндия", "Helsinki · Game", CountryCode.FI)
        tag.contains("FI") -> Triple("Финляндия", "Helsinki", CountryCode.FI)
        tag.contains("SE-Game") -> Triple("Швеция", "Stockholm · Game", CountryCode.SE)
        tag.contains("SE") -> Triple("Швеция", "Stockholm", CountryCode.SE)
        tag.contains("DE-Game") -> Triple("Германия", "Frankfurt · Game", CountryCode.DE)
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
    LaunchedEffect(serviceStatus) {
        groupsViewModel?.updateServiceStatus(serviceStatus)
    }
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

    // Отложенный выбор: тап без коннекта запоминаем, применяем на старте.
    // pending съедаем ТОЛЬКО в момент применения: селектор после коннекта
    // подгружается асинхронно, иначе первое срабатывание (selector=null)
    // молча глотало выбор (баг сборки-10, пойман живьём в Waydroid 23.09).
    val context = androidx.compose.ui.platform.LocalContext.current
    LaunchedEffect(serviceStatus, selTag, selector?.items?.size) {
        if (serviceStatus == Status.Started) {
            val pending = peekPendingServer(context)
            if (pending != null && selTag != pending &&
                selector?.items?.any { it.tag == pending } == true
            ) {
                takePendingServer(context)
                groupsViewModel?.selectGroupItem(ISORA_SELECTOR_TAG, pending)
                groupsViewModel?.sendGlobalEvent(
                    net.isora.vpn.compose.base.UiEvent.ToastMessage("Включил: $pending")
                )
            }
        }
    }

    // Честные глаза: живые пинги селектора — Дозору (троттлинг внутри, 10 мин).
    LaunchedEffect(serviceStatus, selector?.items) {
        if (serviceStatus == Status.Started) {
            val items = selector?.items ?: return@LaunchedEffect
            PingReporter.maybeReport(context, items.associate { it.tag to it.urlTestDelay })
        }
    }

    // Автодобивка: текущий умер (3 замера подряд null с шагом ≥150с),
    // живой кандидат есть — перекидываем сами. Не чаще раза в 30 мин,
    // свежий ручной выбор первые 5 мин не трогаем.
    LaunchedEffect(serviceStatus, selector?.items) {
        if (serviceStatus != Status.Started) return@LaunchedEffect
        val items = selector?.items ?: return@LaunchedEffect
        val prefs = context.getSharedPreferences(ISORA_PREFS, android.content.Context.MODE_PRIVATE)
        val now = System.currentTimeMillis()
        if (now - prefs.getLong(KEY_MANUAL_AT, 0) < 5 * 60 * 1000L) return@LaunchedEffect
        if (now - prefs.getLong(KEY_AUTO_AT, 0) < 30 * 60 * 1000L) return@LaunchedEffect
        val cur = selTag ?: return@LaunchedEffect
        val delays = items.filter { isCuratedTag(it.tag) }.associate { it.tag to it.urlTestDelay }
        if ((delays[cur] ?: 0) > 0) {
            prefs.edit().putInt(KEY_FAILS, 0).apply()
            return@LaunchedEffect
        }
        val best = delays.filter { it.key != cur && it.value > 0 }.minByOrNull { it.value }
            ?: return@LaunchedEffect
        if (now - prefs.getLong(KEY_FAIL_AT, 0) < 150 * 1000L) return@LaunchedEffect
        val streak = prefs.getInt(KEY_FAILS, 0) + 1
        prefs.edit().putInt(KEY_FAILS, streak).putLong(KEY_FAIL_AT, now).apply()
        if (streak >= 3) {
            prefs.edit().putInt(KEY_FAILS, 0).putLong(KEY_AUTO_AT, now)
                .putLong(KEY_MANUAL_AT, now).apply()
            groupsViewModel?.selectGroupItem(ISORA_SELECTOR_TAG, best.key)
            val name = serverForTag(best.key, best.value).country
            groupsViewModel?.sendGlobalEvent(
                net.isora.vpn.compose.base.UiEvent.ToastMessage("$cur умер, включил: $name")
            )
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
        upText = dash.uplink,
        downText = dash.downlink,
    )
}

@Composable
fun IsoraServersRoute(
    groupsViewModel: GroupsViewModel?,
    serviceStatus: Status,
    onSelectDone: () -> Unit,
) {
    // Без статуса команды стоят: клиент групп коннектится только на Started.
    LaunchedEffect(serviceStatus) {
        groupsViewModel?.updateServiceStatus(serviceStatus)
    }
    val groups = groupsViewModel?.uiState?.collectAsState()?.value
    val selector = groups?.groups?.find { it.tag == ISORA_SELECTOR_TAG }

    LaunchedEffect(selector?.tag) {
        if (selector != null) groupsViewModel?.urlTestGroup(selector.tag)
    }

    val servers =
        if (selector != null) {
            // В селекторе бывают дубли (NL-Game идёт и первым, и в tags) —
            // без distinctBy LazyColumn падает с duplicate key.
            // Курированный список: только живое (Авто, Без рекламы, DE, SE, NL-Game).
            val items = selector.items.distinctBy { it.tag }.filter { isCuratedTag(it.tag) }
            if (items.isNotEmpty()) {
                items.map { serverForTag(it.tag, it.urlTestDelay.takeIf { d -> d > 0 } ?: 0) }
            } else {
                DefaultServers.list
            }
        } else {
            DefaultServers.list
        }
    val current =
        selector?.selected?.let { serverForTag(it, 0) } ?: servers.first()
    val context = androidx.compose.ui.platform.LocalContext.current

    ServersScreen(
        currentServer = current,
        servers = servers,
        onSelectServer = { s ->
            saveManualAt(context)
            if (selector != null && isCuratedTag(s.id)) {
                groupsViewModel?.selectGroupItem(selector.tag, s.id)
            } else {
                // VPN выключен: выбор запоминаем, применится сам на коннекте.
                savePendingServer(context, s.id)
                groupsViewModel?.sendGlobalEvent(
                    net.isora.vpn.compose.base.UiEvent.ToastMessage(
                        "Запомнил: ${s.country}. Включу при подключении"
                    )
                )
            }
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
    onLoggedIn: () -> Unit,
    onOpenServers: () -> Unit,
) {
    val dash by dashboardViewModel.uiState.collectAsState()
    // Профиля нет — вход через Telegram вместо ручного импорта.
    if (dash.profiles.isEmpty()) {
        androidx.compose.foundation.layout.Column(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    androidx.compose.ui.graphics.Brush.verticalGradient(
                        listOf(
                            net.isora.vpn.compose.screen.isora.ui.theme.BgDisconnectedTop,
                            net.isora.vpn.compose.screen.isora.ui.theme.BgDisconnectedMid,
                            net.isora.vpn.compose.screen.isora.ui.theme.BgDisconnectedBot,
                        )
                    )
                )
                .windowInsetsPadding(androidx.compose.foundation.layout.WindowInsets.statusBars)
                .padding(horizontal = 20.dp),
        ) {
            androidx.compose.material3.Text(
                text = "Аккаунт",
                fontFamily = net.isora.vpn.compose.screen.isora.ui.theme.HeadFontFamily,
                fontWeight = androidx.compose.ui.text.font.FontWeight.SemiBold,
                fontSize = 24.sp,
                color = net.isora.vpn.compose.screen.isora.ui.theme.Ink,
                modifier = Modifier.padding(vertical = 14.dp),
            )
            IsoraLoginCard(onLoggedIn = onLoggedIn)
            androidx.compose.foundation.layout.Spacer(modifier = Modifier.height(12.dp))
            MirageCard()
            androidx.compose.foundation.layout.Spacer(modifier = Modifier.height(12.dp))
            androidx.compose.foundation.layout.Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .clip(androidx.compose.foundation.shape.RoundedCornerShape(14.dp))
                    .background(androidx.compose.ui.graphics.Color.White.copy(alpha = 0.05f))
                    .border(
                        1.dp,
                        androidx.compose.ui.graphics.Color.White.copy(alpha = 0.12f),
                        androidx.compose.foundation.shape.RoundedCornerShape(14.dp)
                    )
                    .clickable(
                        interactionSource = remember { androidx.compose.foundation.interaction.MutableInteractionSource() },
                        indication = androidx.compose.material3.ripple(
                            color = net.isora.vpn.compose.screen.isora.ui.theme.InkDim
                        ),
                        onClick = onManageSubscription
                    ),
                contentAlignment = Alignment.Center
            ) {
                androidx.compose.material3.Text(
                    text = "Вставить ссылку вручную",
                    fontFamily = net.isora.vpn.compose.screen.isora.ui.theme.ManropeFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.5.sp,
                    color = net.isora.vpn.compose.screen.isora.ui.theme.Ink
                )
            }
        }
        return
    }
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
    val protocolName = when {
        selTag == null -> "Авто"
        selTag.contains("Mirage") -> "Mirage · новая дверь"
        selTag.contains("Game") -> "Game · Hysteria2"
        selTag.contains("Авто") -> "Авто · умный выбор"
        else -> selTag
    }
    AccountScreen(
        userEmail = profileName,
        planTitle = "ISORA VPN",
        planBadge = "PRO",
        statusLine1 = status1,
        statusLine2 = status2,
        versionBadge = "",
        protocolName = protocolName,
        onManageSubscription = onManageSubscription,
        onOpenServers = onOpenServers,
    )
}
