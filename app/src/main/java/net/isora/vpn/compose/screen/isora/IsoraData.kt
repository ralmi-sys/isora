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
    Game("Game", "Hysteria2 для игр")
}

object DefaultServers {
    val list = listOf(
        VpnServer(
            id = "auto",
            country = "Авто",
            city = "Умный выбор",
            pingMs = 0,
            countryCode = CountryCode.EU,
            loadPercent = 0,
            isRecommended = true,
            isFast = true
        ),
        VpnServer(
            id = "nl_ams_01",
            country = "Нидерланды",
            city = "Amsterdam",
            pingMs = 24,
            countryCode = CountryCode.NL,
            loadPercent = 34,
            isRecommended = true,
            isFast = true,
            isStreaming = true
        ),
        VpnServer(
            id = "de_fra_01",
            country = "Германия",
            city = "Frankfurt",
            pingMs = 28,
            countryCode = CountryCode.DE,
            loadPercent = 42,
            isRecommended = true,
            isFast = true
        ),
        VpnServer(
            id = "fi_hel_01",
            country = "Финляндия",
            city = "Helsinki",
            pingMs = 35,
            countryCode = CountryCode.FI,
            loadPercent = 40,
            isFast = true
        ),
        VpnServer(
            id = "fr_par_01",
            country = "Франция",
            city = "Paris",
            pingMs = 60,
            countryCode = CountryCode.FR,
            loadPercent = 70
        )
    )
}
