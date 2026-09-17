package net.isora.vpn.compose.screen.isora.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.rememberLottieComposition
import net.isora.vpn.compose.screen.isora.ui.components.IsoraIcons
import net.isora.vpn.compose.screen.isora.ui.theme.AccentGreen
import net.isora.vpn.compose.screen.isora.ui.theme.BgConnectedBot
import net.isora.vpn.compose.screen.isora.ui.theme.BgConnectedMid
import net.isora.vpn.compose.screen.isora.ui.theme.BgConnectedTop
import net.isora.vpn.compose.screen.isora.ui.theme.BgDisconnectedBot
import net.isora.vpn.compose.screen.isora.ui.theme.BgDisconnectedMid
import net.isora.vpn.compose.screen.isora.ui.theme.BgDisconnectedTop
import net.isora.vpn.compose.screen.isora.ui.theme.BgGlowConnected
import net.isora.vpn.compose.screen.isora.ui.theme.BgGlowDisconnected
import net.isora.vpn.compose.screen.isora.ui.theme.CardBackground
import net.isora.vpn.compose.screen.isora.ui.theme.CardBorder
import net.isora.vpn.compose.screen.isora.ui.theme.Ink
import net.isora.vpn.compose.screen.isora.ui.theme.InkDim
import net.isora.vpn.compose.screen.isora.ui.theme.InkFaint
import net.isora.vpn.compose.screen.isora.ui.theme.ManropeFontFamily
import net.isora.vpn.compose.screen.isora.ui.theme.OrbConnectedCore
import net.isora.vpn.compose.screen.isora.ui.theme.OrbConnectedEdge
import net.isora.vpn.compose.screen.isora.ui.theme.OrbConnectedGlowInner
import net.isora.vpn.compose.screen.isora.ui.theme.OrbConnectedGlowOuter
import net.isora.vpn.compose.screen.isora.ui.theme.OrbConnectedMid
import net.isora.vpn.compose.screen.isora.ui.theme.OrbConnectedStroke
import net.isora.vpn.compose.screen.isora.ui.theme.OrbDisconnectedCore
import net.isora.vpn.compose.screen.isora.ui.theme.OrbDisconnectedEdge
import net.isora.vpn.compose.screen.isora.ui.theme.OrbDisconnectedGlowInner
import net.isora.vpn.compose.screen.isora.ui.theme.OrbDisconnectedGlowOuter
import net.isora.vpn.compose.screen.isora.ui.theme.OrbDisconnectedMid
import net.isora.vpn.compose.screen.isora.ui.theme.OrbDisconnectedStroke

enum class ConnectionState { Disconnected, Connecting, Connected }

@Composable
fun VpnHomeScreen(
    state: ConnectionState,
    currentServer: net.isora.vpn.compose.screen.isora.data.VpnServer,
    onToggleConnection: () -> Unit,
    onOpenServers: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isConnected = state == ConnectionState.Connected
    val isConnecting = state == ConnectionState.Connecting

    val topBgColor by animateColorAsState(
        targetValue = if (isConnected) BgConnectedTop else BgDisconnectedTop,
        animationSpec = tween(550, easing = FastOutSlowInEasing),
        label = "topBg"
    )
    val midBgColor by animateColorAsState(
        targetValue = if (isConnected) BgConnectedMid else BgDisconnectedMid,
        animationSpec = tween(550, easing = FastOutSlowInEasing),
        label = "midBg"
    )
    val botBgColor by animateColorAsState(
        targetValue = if (isConnected) BgConnectedBot else BgDisconnectedBot,
        animationSpec = tween(550, easing = FastOutSlowInEasing),
        label = "botBg"
    )
    val glowColor by animateColorAsState(
        targetValue = if (isConnected) BgGlowConnected else BgGlowDisconnected,
        animationSpec = tween(550, easing = FastOutSlowInEasing),
        label = "glowColor"
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(topBgColor, midBgColor, botBgColor)))
    ) {
        // Background Canvas: Upper-left radial aura & silky wave ribbons in disconnected mode
        Canvas(modifier = Modifier.fillMaxSize()) {
            val w = size.width
            val h = size.height

            // Radial ambient glow behind top / orb
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(glowColor, Color.Transparent),
                    center = Offset(w * 0.45f, h * 0.28f),
                    radius = w * 0.95f
                ),
                center = Offset(w * 0.45f, h * 0.28f),
                radius = w * 0.95f
            )

            // Luminous fluid wave ribbons in Disconnected state (as visible in the screenshot)
            if (!isConnected) {
                val wave1 = Path().apply {
                    moveTo(w * 0.1f, h * 0.55f)
                    cubicTo(w * 0.45f, h * 0.52f, w * 0.75f, h * 0.68f, w * 1.05f, h * 0.62f)
                    cubicTo(w * 0.8f, h * 0.75f, w * 0.45f, h * 0.72f, w * 0.1f, h * 0.55f)
                    close()
                }
                drawPath(
                    path = wave1,
                    brush = Brush.linearGradient(
                        listOf(
                            Color(0x00204AB0),
                            Color(0x224074E0),
                            Color(0x11204AB0),
                            Color(0x000A143C)
                        ),
                        start = Offset(w * 0.1f, h * 0.55f),
                        end = Offset(w * 1.0f, h * 0.7f)
                    )
                )

                val wave2 = Path().apply {
                    moveTo(0f, h * 0.68f)
                    cubicTo(w * 0.35f, h * 0.64f, w * 0.7f, h * 0.82f, w * 1.05f, h * 0.78f)
                }
                drawPath(
                    path = wave2,
                    brush = Brush.linearGradient(
                        listOf(Color(0x004074E0), Color(0x2E4074E0), Color(0x00204AB0)),
                        start = Offset(0f, h * 0.68f),
                        end = Offset(w, h * 0.78f)
                    ),
                    style = Stroke(width = 1.8.dp.toPx())
                )

                val wave3 = Path().apply {
                    moveTo(w * 0.2f, h * 0.76f)
                    cubicTo(w * 0.55f, h * 0.74f, w * 0.85f, h * 0.88f, w * 1.1f, h * 0.86f)
                }
                drawPath(
                    path = wave3,
                    brush = Brush.linearGradient(
                        listOf(Color(0x004074E0), Color(0x184074E0), Color(0x00204AB0)),
                        start = Offset(w * 0.2f, h * 0.76f),
                        end = Offset(w * 1.1f, h * 0.86f)
                    ),
                    style = Stroke(width = 1.2.dp.toPx())
                )
            }
        }

        // Main Content Column
        Column(
            modifier = Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.statusBars)
        ) {
            // TopBar
            TopBar(
                modifier = Modifier.padding(start = 24.dp, end = 24.dp, top = 12.dp, bottom = 8.dp),
                onPowerClick = onToggleConnection
            )

            Spacer(modifier = Modifier.weight(0.15f))

            // Connection Orb with Soap Bubble Glass Optics
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                contentAlignment = Alignment.Center
            ) {
                ConnectionOrb(
                    state = state,
                    onClick = onToggleConnection
                )
            }

            Spacer(modifier = Modifier.height(26.dp))

            // Status Label ("Подключиться" / "• Подключено")
            StatusLabel(
                state = state,
                onClick = onToggleConnection
            )

            Spacer(modifier = Modifier.weight(0.85f))

            // Server Card
            ServerCard(
                state = state,
                server = currentServer,
                onClick = onOpenServers,
                modifier = Modifier.padding(horizontal = 20.dp)
            )

            // Disconnect Button (Only in Connected state!)
            AnimatedVisibility(
                visible = isConnected,
                enter = fadeIn(tween(350)) + expandVertically(tween(350)),
                exit = fadeOut(tween(300)) + shrinkVertically(tween(300))
            ) {
                Box(modifier = Modifier.padding(start = 20.dp, end = 20.dp, top = 14.dp)) {
                    DisconnectButton(onClick = onToggleConnection)
                }
            }

            Spacer(modifier = Modifier.height(18.dp))
        }
    }
}

/**
 * TopBar: ISORA with wide 4sp tracking on the left, power circle button on the right.
 */
@Composable
fun TopBar(
    modifier: Modifier = Modifier,
    onPowerClick: () -> Unit
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "ISORA",
            fontFamily = ManropeFontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp,
            color = Ink,
            letterSpacing = 4.sp
        )

        Box(
            modifier = Modifier
                .size(32.dp)
                .clip(CircleShape)
                .background(Color.White.copy(alpha = 0.035f))
                .border(1.dp, Color.White.copy(alpha = 0.16f), CircleShape)
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = ripple(color = InkDim),
                    onClick = onPowerClick
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = IsoraIcons.Power,
                contentDescription = "Power",
                tint = Color.White.copy(alpha = 0.75f),
                modifier = Modifier.size(16.dp)
            )
        }
    }
}

/**
 * 3D Glass / Soap Bubble Orb with glowing refraction rim, specular highlights, and the 3-blade logo.
 */
@Composable
fun ConnectionOrb(
    state: ConnectionState,
    onClick: () -> Unit
) {
    val isConnected = state == ConnectionState.Connected
    val isConnecting = state == ConnectionState.Connecting

    val coreColor by animateColorAsState(
        if (isConnected) OrbConnectedCore else OrbDisconnectedCore,
        tween(550, easing = FastOutSlowInEasing),
        label = "coreColor"
    )
    val midColor by animateColorAsState(
        if (isConnected) OrbConnectedMid else OrbDisconnectedMid,
        tween(550, easing = FastOutSlowInEasing),
        label = "midColor"
    )
    val edgeColor by animateColorAsState(
        if (isConnected) OrbConnectedEdge else OrbDisconnectedEdge,
        tween(550, easing = FastOutSlowInEasing),
        label = "edgeColor"
    )
    val strokeColor by animateColorAsState(
        if (isConnected) OrbConnectedStroke else OrbDisconnectedStroke,
        tween(550, easing = FastOutSlowInEasing),
        label = "strokeColor"
    )
    val glowOuter by animateColorAsState(
        if (isConnected) OrbConnectedGlowOuter else OrbDisconnectedGlowOuter,
        tween(550, easing = FastOutSlowInEasing),
        label = "glowOuter"
    )
    val glowInner by animateColorAsState(
        if (isConnected) OrbConnectedGlowInner else OrbDisconnectedGlowInner,
        tween(550, easing = FastOutSlowInEasing),
        label = "glowInner"
    )

    val infiniteTransition = rememberInfiniteTransition(label = "orbMotion")

    val breathingScale by infiniteTransition.animateFloat(
        initialValue = 1.0f,
        targetValue = 1.03f,
        animationSpec = infiniteRepeatable(
            animation = tween(3000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "breathingScale"
    )

    val pulsingScale by infiniteTransition.animateFloat(
        initialValue = 0.98f,
        targetValue = 1.06f,
        animationSpec = infiniteRepeatable(
            animation = tween(750, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulsingScale"
    )

    val scale = when {
        isConnecting -> pulsingScale
        isConnected -> breathingScale
        else -> 1.0f
    }

    Box(
        modifier = Modifier
            .size(208.dp)
            .scale(scale)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick
            ),
        contentAlignment = Alignment.Center
    ) {
        // Multi-layered Canvas for realistic glowing glass bubble optics
        Canvas(modifier = Modifier.fillMaxSize()) {
            val d = size.minDimension
            val center = Offset(size.width / 2f, size.height / 2f)
            val radius = d / 2f

            // Layer 1: Diffuse Outer Glow (Soft Halo)
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(glowOuter.copy(alpha = 0.45f), glowInner.copy(alpha = 0.15f), Color.Transparent),
                    center = center,
                    radius = radius * 1.45f
                ),
                center = center,
                radius = radius * 1.45f
            )

            // Layer 2: Deep Sphere Volumetric Core (darker inside, luminous near edge)
            val coreOffset = Offset(center.x - radius * 0.28f, center.y - radius * 0.35f)
            drawCircle(
                brush = Brush.radialGradient(
                    0.0f to Color.White.copy(alpha = 0.15f),
                    0.35f to midColor.copy(alpha = 0.35f),
                    0.70f to edgeColor.copy(alpha = 0.55f),
                    0.95f to coreColor.copy(alpha = 0.85f),
                    center = coreOffset,
                    radius = radius * 1.25f
                ),
                center = center,
                radius = radius - 1.dp.toPx()
            )

            // Layer 3: Inner Caustic Crescent Glow (Top / Top-Left)
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(coreColor.copy(alpha = 0.90f), Color.Transparent),
                    center = Offset(center.x - radius * 0.30f, center.y - radius * 0.40f),
                    radius = radius * 0.70f
                ),
                center = Offset(center.x - radius * 0.30f, center.y - radius * 0.40f),
                radius = radius * 0.70f
            )

            // Layer 4: Secondary Lower-Right Reflection
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(midColor.copy(alpha = 0.65f), Color.Transparent),
                    center = Offset(center.x + radius * 0.32f, center.y + radius * 0.35f),
                    radius = radius * 0.65f
                ),
                center = Offset(center.x + radius * 0.32f, center.y + radius * 0.35f),
                radius = radius * 0.65f
            )

            // Layer 5: Luminous Soap Bubble Refraction Ring (The distinctive bright caustic rim)
            drawCircle(
                brush = Brush.sweepGradient(
                    colors = listOf(
                        coreColor.copy(alpha = 0.95f),
                        midColor.copy(alpha = 0.45f),
                        strokeColor.copy(alpha = 0.80f),
                        coreColor.copy(alpha = 0.95f)
                    ),
                    center = center
                ),
                center = center,
                radius = radius - 1.5.dp.toPx(),
                style = Stroke(width = 2.2.dp.toPx())
            )

            // Layer 6: Subtle Outer Glass Stroke
            drawCircle(
                color = strokeColor.copy(alpha = 0.40f),
                center = center,
                radius = radius - 0.5.dp.toPx(),
                style = Stroke(width = 1.dp.toPx())
            )
        }

        // Живая утка Lottie в центре (состояние -> ассет)
        val duckAsset = when (state) {
            ConnectionState.Disconnected -> "ducks/hey.json"
            ConnectionState.Connecting -> "ducks/think.json"
            ConnectionState.Connected -> "ducks/like.json"
        }
        val duckComposition by rememberLottieComposition(LottieCompositionSpec.Asset(duckAsset))
        LottieAnimation(
            composition = duckComposition,
            iterations = LottieConstants.IterateForever,
            modifier = Modifier.size(84.dp)
        )
    }
}

/**
 * Status Label: "Подключиться" or green dot + "Подключено"
 */
@Composable
fun StatusLabel(
    state: ConnectionState,
    onClick: () -> Unit
) {
    val isConnected = state == ConnectionState.Connected

    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onClick
            )
            .padding(vertical = 4.dp)
    ) {
        if (isConnected) {
            // Glowing green indicator dot
            Box(
                modifier = Modifier
                    .padding(end = 8.dp)
                    .size(7.dp)
                    .clip(CircleShape)
                    .background(AccentGreen)
                    .drawBehind {
                        drawCircle(
                            brush = Brush.radialGradient(
                                listOf(AccentGreen.copy(alpha = 0.65f), Color.Transparent),
                                radius = 12.dp.toPx()
                            ),
                            radius = 12.dp.toPx()
                        )
                    }
            )
        }

        Text(
            text = when (state) {
                ConnectionState.Disconnected -> "Подключиться"
                ConnectionState.Connecting -> "Подключение..."
                ConnectionState.Connected -> "Подключено"
            },
            fontFamily = ManropeFontFamily,
            fontWeight = FontWeight.SemiBold,
            fontSize = 16.sp,
            color = Ink.copy(alpha = 0.94f)
        )
    }
}

/**
 * Server Card: Glassmorphic container showing Location + Ping (Disconnected)
 * or Location + 3-column stats for Ping, Download, Upload (Connected).
 */
@Composable
fun ServerCard(
    state: ConnectionState,
    server: net.isora.vpn.compose.screen.isora.data.VpnServer,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isConnected = state == ConnectionState.Connected

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .background(
                Brush.verticalGradient(
                    listOf(
                        Color.White.copy(alpha = 0.055f),
                        Color.White.copy(alpha = 0.035f)
                    )
                )
            )
            .border(
                1.dp,
                Brush.verticalGradient(
                    listOf(
                        Color.White.copy(alpha = 0.12f),
                        Color.White.copy(alpha = 0.06f)
                    )
                ),
                RoundedCornerShape(18.dp)
            )
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = ripple(color = InkDim),
                onClick = onClick
            )
            .padding(horizontal = 16.dp, vertical = 14.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            // Top Row: Flag + Location details + Chevron
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                net.isora.vpn.compose.screen.isora.ui.components.CountryFlagBadge(code = server.countryCode, badgeSize = 24.dp)

                Spacer(modifier = Modifier.width(14.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = server.country,
                        fontFamily = ManropeFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.sp,
                        color = Ink
                    )

                    Spacer(modifier = Modifier.height(1.dp))

                    Text(
                        text = server.city,
                        fontFamily = ManropeFontFamily,
                        fontWeight = FontWeight.Medium,
                        fontSize = 12.sp,
                        color = InkDim
                    )

                    // Ping in green, only shown in Disconnected mode
                    if (!isConnected) {
                        Spacer(modifier = Modifier.height(3.dp))
                        Text(
                            text = if (server.pingMs > 0) "${server.pingMs} ms" else "—",
                            fontFamily = ManropeFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.5.sp,
                            color = AccentGreen
                        )
                    }
                }

                Icon(
                    imageVector = IsoraIcons.ChevronRight,
                    contentDescription = "Select server",
                    tint = Color.White.copy(alpha = 0.40f),
                    modifier = Modifier.size(16.dp)
                )
            }

            // Stats section for Connected mode
            AnimatedVisibility(
                visible = isConnected,
                enter = fadeIn(tween(300)) + expandVertically(tween(300)),
                exit = fadeOut(tween(250)) + shrinkVertically(tween(250))
            ) {
                Column {
                    // Thin horizontal divider
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 14.dp, bottom = 12.dp)
                            .height(1.dp)
                            .background(Color.White.copy(alpha = 0.08f))
                    )

                    // 3 columns: Ping | Download | Upload
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        StatItem(
                            label = "Пинг",
                            value = "${server.pingMs} ms",
                            icon = null
                        )

                        VerticalDivider()

                        StatItem(
                            label = "Скачать",
                            value = "—",
                            icon = IsoraIcons.ArrowDown
                        )

                        VerticalDivider()

                        StatItem(
                            label = "Загрузить",
                            value = "—",
                            icon = IsoraIcons.ArrowUp
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun VerticalDivider() {
    Box(
        modifier = Modifier
            .width(1.dp)
            .height(24.dp)
            .background(Color.White.copy(alpha = 0.08f))
    )
}

@Composable
fun StatItem(
    label: String,
    value: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector?
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.padding(horizontal = 4.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            if (icon != null) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = Ink,
                    modifier = Modifier
                        .size(11.dp)
                        .padding(end = 3.dp)
                )
            }
            Text(
                text = value,
                fontFamily = ManropeFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 13.5.sp,
                color = Ink
            )
        }
        Spacer(modifier = Modifier.height(3.dp))
        Text(
            text = label,
            fontFamily = ManropeFontFamily,
            fontWeight = FontWeight.SemiBold,
            fontSize = 10.5.sp,
            color = InkFaint
        )
    }
}

/**
 * Disconnect Button: Clean glassmorphic rectangle with "Отключить"
 */
@Composable
fun DisconnectButton(onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(52.dp)
            .clip(RoundedCornerShape(14.dp))
            .background(Color.White.copy(alpha = 0.06f))
            .border(1.dp, Color.White.copy(alpha = 0.14f), RoundedCornerShape(14.dp))
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = ripple(color = InkDim),
                onClick = onClick
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Отключить",
            fontFamily = ManropeFontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 15.sp,
            color = Ink
        )
    }
}
