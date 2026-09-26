package net.isora.vpn.compose.screen.isora.data

import net.isora.vpn.compose.screen.isora.ui.components.CountryCode

data class VpnServer(
    val id: String,
    val country: String,
    val city: String,
    val pingMs: Int,
    val countryCode: CountryCode,
    val loadPercent: Int,
    val isRecommended: Boolean = false,
    val isFast: Boolean = false,
    val isStreaming: Boolean = false
)

enum class ScreenTab(val title: String) {
    Home("Главная"),
    Servers("Серверы"),
    Account("Аккаунт")
}

enum class VpnProtocol(val displayName: String, val description: String) {
    Auto("Авто", "Умный выбор сервера"),
    Xhttp("XHTTP", "Стабильно через TCP"),
    Game("Game", "Hysteria2 для игр"),
    Mirage("Mirage", "Новая дверь · тест")
}

object DefaultServers {
    // Заглушки БЕЗ подписки/коннекта: id = НАСТОЯЩИЕ теги выдачи,
    // чтобы тап офлайн сохранялся как отложенный выбор (isCuratedTag их пропускает).
    // Пинги/нагрузка нули — рендерятся прочерком, не врём.
    val list = listOf(
        VpnServer(
            id = "🇪🇺 Авто",
            country = "Авто",
            city = "Умный выбор",
            pingMs = 0,
            countryCode = CountryCode.EU,
            loadPercent = 0,
            isRecommended = true,
            isFast = true
        ),
        VpnServer(
            id = "🇩🇪 DE",
            country = "Германия",
            city = "Frankfurt",
            pingMs = 0,
            countryCode = CountryCode.DE,
            loadPercent = 0,
            isRecommended = true,
            isFast = true
        ),
        VpnServer(
            id = "🇩🇪 DE-Game",
            country = "Германия",
            city = "Frankfurt · Game",
            pingMs = 0,
            countryCode = CountryCode.DE,
            loadPercent = 0,
            isFast = true
        ),
        VpnServer(
            id = "🇸🇪 SE",
            country = "Швеция",
            city = "Stockholm",
            pingMs = 0,
            countryCode = CountryCode.SE,
            loadPercent = 0,
            isRecommended = true,
            isFast = true
        ),
        VpnServer(
            id = "🇸🇪 SE-Game",
            country = "Швеция",
            city = "Stockholm · Game",
            pingMs = 0,
            countryCode = CountryCode.SE,
            loadPercent = 0,
            isFast = true
        ),
        VpnServer(
            id = "🇳🇱 NL-Game",
            country = "Нидерланды",
            city = "Amsterdam · Game",
            pingMs = 0,
            countryCode = CountryCode.NL,
            loadPercent = 0,
            isRecommended = true,
            isFast = true,
            isStreaming = true
        )
    )
}
