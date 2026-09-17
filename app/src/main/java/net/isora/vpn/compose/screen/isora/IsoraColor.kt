package net.isora.vpn.compose.screen.isora.ui.theme

import androidx.compose.ui.graphics.Color

val Ink = Color(0xFFF5F7FB)
val InkDim = Ink.copy(alpha = 0.55f)
val InkFaint = Ink.copy(alpha = 0.32f)

val CardBackground = Color(0xFFFFFFFF).copy(alpha = 0.045f)
val CardBorder = Color(0xFFFFFFFF).copy(alpha = 0.10f)

val AccentGreen = Color(0xFF3FE0A5)

// Orbit disconnected colors
val OrbDisconnectedCore = Color(0xFFC3E3FF).copy(alpha = 0.95f)
val OrbDisconnectedMid = Color(0xFF609BFF).copy(alpha = 0.55f)
val OrbDisconnectedEdge = Color(0xFF204AB0).copy(alpha = 0.40f)
val OrbDisconnectedFade = Color(0xFF0A143C).copy(alpha = 0f)
val OrbDisconnectedStroke = Color(0xFFACcFFF).copy(alpha = 0.35f)
val OrbDisconnectedGlowOuter = Color(0xFF468CFF).copy(alpha = 0.35f)
val OrbDisconnectedGlowInner = Color(0xFF285ADC).copy(alpha = 0.15f)

// Orbit connected colors
val OrbConnectedCore = Color(0xFFCBFFEC).copy(alpha = 0.95f)
val OrbConnectedMid = Color(0xFF4AE3B2).copy(alpha = 0.55f)
val OrbConnectedEdge = Color(0xFF107C5D).copy(alpha = 0.40f)
val OrbConnectedFade = Color(0xFF05281E).copy(alpha = 0f)
val OrbConnectedStroke = Color(0xFF98FFD9).copy(alpha = 0.35f)
val OrbConnectedGlowOuter = Color(0xFF3CDCA0).copy(alpha = 0.35f)
val OrbConnectedGlowInner = Color(0xFF1EB482).copy(alpha = 0.15f)

// Background colors
val BgDisconnectedTop = Color(0xFF0A0D17)
val BgDisconnectedMid = Color(0xFF05060B)
val BgDisconnectedBot = Color(0xFF030407)
val BgGlowDisconnected = Color(0xFF4074E0).copy(alpha = 0.32f)

val BgConnectedTop = Color(0xFF080D0E)
val BgConnectedMid = Color(0xFF05090A)
val BgConnectedBot = Color(0xFF030405)
val BgGlowConnected = Color(0xFF2AD698).copy(alpha = 0.26f)
