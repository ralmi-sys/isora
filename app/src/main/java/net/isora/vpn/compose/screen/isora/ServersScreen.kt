package net.isora.vpn.compose.screen.isora.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import net.isora.vpn.compose.screen.isora.data.DefaultServers
import net.isora.vpn.compose.screen.isora.data.VpnServer
import net.isora.vpn.compose.screen.isora.ui.components.CountryFlagBadge
import net.isora.vpn.compose.screen.isora.ui.components.IsoraIcons
import net.isora.vpn.compose.screen.isora.ui.theme.AccentGreen
import net.isora.vpn.compose.screen.isora.ui.theme.BgDisconnectedBot
import net.isora.vpn.compose.screen.isora.ui.theme.BgDisconnectedMid
import net.isora.vpn.compose.screen.isora.ui.theme.BgDisconnectedTop
import net.isora.vpn.compose.screen.isora.ui.theme.BgGlowDisconnected
import net.isora.vpn.compose.screen.isora.ui.theme.Ink
import net.isora.vpn.compose.screen.isora.ui.theme.InkDim
import net.isora.vpn.compose.screen.isora.ui.theme.InkFaint
import net.isora.vpn.compose.screen.isora.ui.theme.ManropeFontFamily

@Composable
fun ServersScreen(
    currentServer: VpnServer,
    servers: List<VpnServer> = DefaultServers.list,
    onSelectServer: (VpnServer) -> Unit,
    modifier: Modifier = Modifier
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedFilter by remember { mutableStateOf("Все") }

    val filters = listOf("Все", "Рекомендуемые", "Быстрые", "Стриминг")

    val filteredServers = remember(searchQuery, selectedFilter, servers) {
        servers.filter { server ->
            val matchesQuery = searchQuery.isBlank() ||
                server.country.contains(searchQuery, ignoreCase = true) ||
                server.city.contains(searchQuery, ignoreCase = true)

            val matchesFilter = when (selectedFilter) {
                "Рекомендуемые" -> server.isRecommended
                "Быстрые" -> server.isFast || server.pingMs < 40
                "Стриминг" -> server.isStreaming
                else -> true
            }

            matchesQuery && matchesFilter
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(BgDisconnectedTop, BgDisconnectedMid, BgDisconnectedBot)
                )
            )
    ) {
        // Ambient glow at the top
        Canvas(modifier = Modifier.fillMaxSize()) {
            val w = size.width
            val h = size.height
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(BgGlowDisconnected.copy(alpha = 0.6f), Color.Transparent),
                    center = Offset(w * 0.5f, h * 0.15f),
                    radius = w * 0.85f
                ),
                center = Offset(w * 0.5f, h * 0.15f),
                radius = w * 0.85f
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.statusBars)
        ) {
            // Header
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp, vertical = 14.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Серверы",
                        fontFamily = ManropeFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 24.sp,
                        color = Ink,
                        letterSpacing = 0.5.sp,
                        maxLines = 1
                    )
                    Text(
                        text = "Доступно ${servers.size} локаций",
                        fontFamily = ManropeFontFamily,
                        fontWeight = FontWeight.Medium,
                        fontSize = 12.sp,
                        color = InkDim,
                        maxLines = 1
                    )
                }

                // Global ping indicator
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(Color.White.copy(alpha = 0.05f))
                        .border(1.dp, Color.White.copy(alpha = 0.12f), RoundedCornerShape(20.dp))
                        .padding(horizontal = 10.dp, vertical = 5.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(6.dp)
                                .clip(CircleShape)
                                .background(AccentGreen)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Оптимально",
                            fontFamily = ManropeFontFamily,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 11.sp,
                            color = Ink,
                            maxLines = 1
                        )
                    }
                }
            }

            // Search bar
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 8.dp)
                    .height(46.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(Color.White.copy(alpha = 0.05f))
                    .border(1.dp, Color.White.copy(alpha = 0.12f), RoundedCornerShape(14.dp))
                    .padding(horizontal = 14.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Icon(
                        imageVector = IsoraIcons.Search,
                        contentDescription = "Search",
                        tint = InkFaint,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Box(modifier = Modifier.weight(1f)) {
                        if (searchQuery.isEmpty()) {
                            Text(
                                text = "Поиск...",
                                fontFamily = ManropeFontFamily,
                                fontWeight = FontWeight.Normal,
                                fontSize = 13.5.sp,
                                color = InkFaint,
                                maxLines = 1
                            )
                        }
                        BasicTextField(
                            value = searchQuery,
                            onValueChange = { searchQuery = it },
                            textStyle = TextStyle(
                                fontFamily = ManropeFontFamily,
                                fontWeight = FontWeight.Medium,
                                fontSize = 13.5.sp,
                                color = Ink
                            ),
                            cursorBrush = SolidColor(Color(0xFF609BFF)),
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth()
                        )
                    }
                }
            }

            // Filter chips
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 10.dp),
                contentPadding = PaddingValues(horizontal = 20.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(filters) { filter ->
                    val isSelected = filter == selectedFilter
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(
                                if (isSelected) {
                                    Brush.linearGradient(
                                        listOf(
                                            Color(0xFF4074E0).copy(alpha = 0.35f),
                                            Color(0xFF285ADC).copy(alpha = 0.18f)
                                        )
                                    )
                                } else {
                                    SolidColor(Color.White.copy(alpha = 0.035f))
                                }
                            )
                            .border(
                                1.dp,
                                if (isSelected) Color(0xFF609BFF).copy(alpha = 0.5f)
                                else Color.White.copy(alpha = 0.08f),
                                RoundedCornerShape(12.dp)
                            )
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = ripple(color = InkDim),
                                onClick = { selectedFilter = filter }
                            )
                            .padding(horizontal = 14.dp, vertical = 7.dp)
                    ) {
                        Text(
                            text = filter,
                            fontFamily = ManropeFontFamily,
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                            fontSize = 12.sp,
                            color = if (isSelected) Ink else InkDim
                        )
                    }
                }
            }

            // Server items list
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(start = 20.dp, end = 20.dp, top = 4.dp, bottom = 96.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(filteredServers, key = { it.id }) { server ->
                    val isCurrent = server.id == currentServer.id
                    ServerRowItem(
                        server = server,
                        isSelected = isCurrent,
                        onClick = { onSelectServer(server) }
                    )
                }
            }
        }
    }
}

@Composable
fun ServerRowItem(
    server: VpnServer,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(
                if (isSelected) {
                    Brush.verticalGradient(
                        listOf(
                            Color(0xFF4074E0).copy(alpha = 0.16f),
                            Color(0xFF285ADC).copy(alpha = 0.07f)
                        )
                    )
                } else {
                    Brush.verticalGradient(
                        listOf(
                            Color.White.copy(alpha = 0.055f),
                            Color.White.copy(alpha = 0.035f)
                        )
                    )
                }
            )
            .border(
                1.dp,
                if (isSelected) {
                    Brush.verticalGradient(
                        listOf(
                            Color(0xFF609BFF).copy(alpha = 0.50f),
                            Color(0xFF285ADC).copy(alpha = 0.25f)
                        )
                    )
                } else {
                    Brush.verticalGradient(
                        listOf(
                            Color.White.copy(alpha = 0.12f),
                            Color.White.copy(alpha = 0.06f)
                        )
                    )
                },
                RoundedCornerShape(16.dp)
            )
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = ripple(color = InkDim),
                onClick = onClick
            )
            .padding(horizontal = 16.dp, vertical = 13.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Flag badge
            CountryFlagBadge(code = server.countryCode, badgeSize = 24.dp)

            Spacer(modifier = Modifier.width(14.dp))

            // Country & City
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = server.country,
                        fontFamily = ManropeFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 14.5.sp,
                        color = Ink,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f, fill = false)
                    )
                    if (server.isRecommended) {
                        Spacer(modifier = Modifier.width(8.dp))
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(Color(0xFF4074E0).copy(alpha = 0.25f))
                                .padding(horizontal = 5.dp, vertical = 1.dp)
                        ) {
                            Text(
                                text = "TOP",
                                fontFamily = ManropeFontFamily,
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 9.sp,
                                color = Color(0xFF82B1FF)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(1.dp))

                Text(
                    text = server.city,
                    fontFamily = ManropeFontFamily,
                    fontWeight = FontWeight.Medium,
                    fontSize = 12.sp,
                    color = InkDim,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            // Load and Ping
            Column(
                horizontalAlignment = Alignment.End,
                modifier = Modifier.padding(end = 12.dp)
            ) {
                Text(
                    text = "${server.pingMs} ms",
                    fontFamily = ManropeFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.5.sp,
                    color = when {
                        server.pingMs < 45 -> AccentGreen
                        server.pingMs < 90 -> Color(0xFFFFB84D)
                        else -> InkDim
                    }
                )
                Spacer(modifier = Modifier.height(2.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "${server.loadPercent}%",
                        fontFamily = ManropeFontFamily,
                        fontWeight = FontWeight.Medium,
                        fontSize = 10.5.sp,
                        color = InkFaint
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    // Mini load dot
                    Box(
                        modifier = Modifier
                            .size(5.dp)
                            .clip(CircleShape)
                            .background(
                                when {
                                    server.loadPercent < 50 -> AccentGreen
                                    server.loadPercent < 75 -> Color(0xFFFFB84D)
                                    else -> Color(0xFFFF5252)
                                }
                            )
                    )
                }
            }

            // Selection indicator (Checkmark or empty ring)
            Box(
                modifier = Modifier
                    .size(22.dp)
                    .clip(CircleShape)
                    .background(
                        if (isSelected) Color(0xFF4074E0)
                        else Color.White.copy(alpha = 0.04f)
                    )
                    .border(
                        1.2.dp,
                        if (isSelected) Color(0xFF609BFF)
                        else Color.White.copy(alpha = 0.18f),
                        CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                if (isSelected) {
                    Icon(
                        imageVector = IsoraIcons.Check,
                        contentDescription = "Selected",
                        tint = Color.White,
                        modifier = Modifier.size(13.dp)
                    )
                }
            }
        }
    }
}
