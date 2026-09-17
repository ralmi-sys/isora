package net.isora.vpn.compose.screen.isora.ui.screens

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
fun AccountScreen(
    userEmail: String,
    planTitle: String = "ISORA",
    planBadge: String = "PRO",
    statusLine1: String = "",
    statusLine2: String = "",
    versionBadge: String = "",
    protocolName: String = "Авто",
    onManageSubscription: () -> Unit = {},
    onOpenServers: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(BgDisconnectedTop, BgDisconnectedMid, BgDisconnectedBot)
                )
            )
    ) {
        // Ambient background glow
        Canvas(modifier = Modifier.fillMaxSize()) {
            val w = size.width
            val h = size.height
            drawCircle(
                brush = Brush.radialGradient(
                    colors = listOf(BgGlowDisconnected.copy(alpha = 0.55f), Color.Transparent),
                    center = Offset(w * 0.5f, h * 0.12f),
                    radius = w * 0.85f
                ),
                center = Offset(w * 0.5f, h * 0.12f),
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
                Text(
                    text = "Аккаунт",
                    fontFamily = ManropeFontFamily,
                    fontWeight = FontWeight.Bold,
                    fontSize = 24.sp,
                    color = Ink,
                    letterSpacing = 0.5.sp
                )

                // Version badge (скрыт, если пуст — версию форка не светим)
                if (versionBadge.isNotBlank()) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color.White.copy(alpha = 0.05f))
                            .border(1.dp, Color.White.copy(alpha = 0.12f), RoundedCornerShape(12.dp))
                            .padding(horizontal = 10.dp, vertical = 5.dp)
                    ) {
                        Text(
                            text = versionBadge,
                            fontFamily = ManropeFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp,
                            color = InkDim,
                            maxLines = 1
                        )
                    }
                }
            }

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(start = 20.dp, end = 20.dp, top = 4.dp, bottom = 96.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Profile & Subscription Card
                item {
                    UserProfileCard(
                        userEmail = userEmail,
                        planTitle = planTitle,
                        planBadge = planBadge,
                        statusLine1 = statusLine1,
                        statusLine2 = statusLine2
                    )
                }

                // Traffic & Security Stats
                item {
                    PlanHighlightsCard()
                }

                // Security Section
                item {
                    SettingsSectionTitle(title = "БЕЗОПАСНОСТЬ И СЕТЬ")
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(18.dp))
                            .background(Color.White.copy(alpha = 0.045f))
                            .border(1.dp, Color.White.copy(alpha = 0.09f), RoundedCornerShape(18.dp))
                    ) {
                        // Kill Switch — системный: открывается экран настроек Android.
                        SettingsClickableRow(
                            title = "Kill Switch",
                            subtitle = "Блокировка без VPN — включается в настройках Android",
                            icon = IsoraIcons.Shield,
                            onClick = {
                                runCatching {
                                    context.startActivity(
                                        android.content.Intent(android.provider.Settings.ACTION_VPN_SETTINGS)
                                    )
                                }
                            }
                        )

                        SettingsDivider()

                        // Протокол — показывает текущий, меняется во вкладке Серверы.
                        SettingsClickableRow(
                            title = "Протокол",
                            subtitle = protocolName,
                            icon = IsoraIcons.Servers,
                            onClick = onOpenServers
                        )
                    }
                }

                // General / Support Section
                item {
                    SettingsSectionTitle(title = "ПОДДЕРЖКА И СЕРВИС")
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(18.dp))
                            .background(Color.White.copy(alpha = 0.045f))
                            .border(1.dp, Color.White.copy(alpha = 0.09f), RoundedCornerShape(18.dp))
                    ) {
                        SettingsClickableRow(
                            title = "Служба поддержки 24/7",
                            subtitle = "Быстрый ответ оператора в чате",
                            icon = IsoraIcons.User,
                            onClick = {
                                runCatching {
                                    context.startActivity(
                                        android.content.Intent(
                                            android.content.Intent.ACTION_VIEW,
                                            android.net.Uri.parse("https://t.me/IsoraSupport_bot")
                                        )
                                    )
                                }
                            }
                        )

                        SettingsDivider()

                        SettingsClickableRow(
                            title = "Политика без логов (No-Logs)",
                            subtitle = "Подтверждено независимым аудитом 2026",
                            icon = IsoraIcons.Shield,
                            onClick = {
                                runCatching {
                                    context.startActivity(
                                        android.content.Intent(
                                            android.content.Intent.ACTION_VIEW,
                                            android.net.Uri.parse("https://t.me/Isora_Official")
                                        )
                                    )
                                }
                            }
                        )
                    }
                }

                // Sign Out / Manage Subscription Button
                item {
                    Spacer(modifier = Modifier.height(6.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .clip(RoundedCornerShape(14.dp))
                            .background(Color.White.copy(alpha = 0.05f))
                            .border(1.dp, Color.White.copy(alpha = 0.12f), RoundedCornerShape(14.dp))
                            .clickable(
                                interactionSource = remember { MutableInteractionSource() },
                                indication = ripple(color = InkDim),
                                onClick = onManageSubscription
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Управление подпиской",
                            fontFamily = ManropeFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.5.sp,
                            color = Ink
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun UserProfileCard(
    userEmail: String,
    planTitle: String = "ISORA",
    planBadge: String = "PRO",
    statusLine1: String = "",
    statusLine2: String = ""
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(
                Brush.verticalGradient(
                    listOf(
                        Color(0xFF4074E0).copy(alpha = 0.16f),
                        Color(0xFF285ADC).copy(alpha = 0.06f)
                    )
                )
            )
            .border(
                1.dp,
                Brush.verticalGradient(
                    listOf(
                        Color(0xFF609BFF).copy(alpha = 0.40f),
                        Color(0xFF285ADC).copy(alpha = 0.15f)
                    )
                ),
                RoundedCornerShape(20.dp)
            )
            .padding(18.dp)
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Avatar with glowing gradient
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .clip(CircleShape)
                        .background(
                            Brush.linearGradient(
                                listOf(Color(0xFF4074E0), Color(0xFF1E3A8A))
                            )
                        )
                        .border(1.5.dp, Color(0xFF609BFF).copy(alpha = 0.6f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = userEmail.take(1).uppercase(),
                        fontFamily = ManropeFontFamily,
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 20.sp,
                        color = Color.White
                    )
                }

                Spacer(modifier = Modifier.width(14.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = planTitle,
                            fontFamily = ManropeFontFamily,
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 15.sp,
                            color = Ink
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        // PRO Badge
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(
                                    Brush.linearGradient(
                                        listOf(Color(0xFFFFB800), Color(0xFFFF8800))
                                    )
                                )
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = planBadge,
                                fontFamily = ManropeFontFamily,
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 9.sp,
                                color = Color.Black
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(2.dp))

                    Text(
                        text = userEmail,
                        fontFamily = ManropeFontFamily,
                        fontWeight = FontWeight.Medium,
                        fontSize = 12.sp,
                        color = InkDim
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Sub status pill
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color.White.copy(alpha = 0.05f))
                    .padding(horizontal = 12.dp, vertical = 9.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(7.dp)
                                .clip(CircleShape)
                                .background(AccentGreen)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = statusLine1,
                            fontFamily = ManropeFontFamily,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp,
                            color = Ink,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = statusLine2,
                        fontFamily = ManropeFontFamily,
                        fontWeight = FontWeight.Medium,
                        fontSize = 11.5.sp,
                        color = InkDim,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }
            }
        }
    }
}

@Composable
fun PlanHighlightsCard() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .background(Color.White.copy(alpha = 0.045f))
            .border(1.dp, Color.White.copy(alpha = 0.09f), RoundedCornerShape(18.dp))
            .padding(horizontal = 16.dp, vertical = 14.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            HighlightItem(title = "Протокол", value = "Авто")
            Box(modifier = Modifier.width(1.dp).height(24.dp).background(Color.White.copy(alpha = 0.08f)))
            HighlightItem(title = "Серверы", value = "4 страны")
            Box(modifier = Modifier.width(1.dp).height(24.dp).background(Color.White.copy(alpha = 0.08f)))
            HighlightItem(title = "Шифр", value = "Reality")
        }
    }
}

@Composable
fun HighlightItem(title: String, value: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = value,
            fontFamily = ManropeFontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 12.sp,
            color = Ink,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        Spacer(modifier = Modifier.height(2.dp))
        Text(
            text = title,
            fontFamily = ManropeFontFamily,
            fontWeight = FontWeight.Medium,
            fontSize = 10.5.sp,
            color = InkFaint,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
    }
}

@Composable
fun SettingsSectionTitle(title: String) {
    Text(
        text = title,
        fontFamily = ManropeFontFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 11.sp,
        color = InkFaint,
        letterSpacing = 1.sp,
        modifier = Modifier.padding(start = 4.dp, bottom = 8.dp)
    )
}

@Composable
fun SettingsToggleRow(
    title: String,
    subtitle: String,
    icon: ImageVector,
    checked: Boolean,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = ripple(color = InkDim),
                onClick = { onCheckedChange(!checked) }
            )
            .padding(horizontal = 16.dp, vertical = 13.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(34.dp)
                .clip(RoundedCornerShape(9.dp))
                .background(Color.White.copy(alpha = 0.06f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = if (checked) Color(0xFF609BFF) else InkDim,
                modifier = Modifier.size(17.dp)
            )
        }

        Spacer(modifier = Modifier.width(13.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                fontFamily = ManropeFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 13.5.sp,
                color = Ink,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(1.dp))
            Text(
                text = subtitle,
                fontFamily = ManropeFontFamily,
                fontWeight = FontWeight.Medium,
                fontSize = 11.sp,
                color = InkDim,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }

        Spacer(modifier = Modifier.width(8.dp))

        Switch(
            checked = checked,
            onCheckedChange = onCheckedChange,
            colors = SwitchDefaults.colors(
                checkedThumbColor = Color.White,
                checkedTrackColor = Color(0xFF4074E0),
                uncheckedThumbColor = Color(0xFFB0B7C6),
                uncheckedTrackColor = Color.White.copy(alpha = 0.12f),
                uncheckedBorderColor = Color.Transparent
            )
        )
    }
}

@Composable
fun SettingsClickableRow(
    title: String,
    subtitle: String,
    icon: ImageVector,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = ripple(color = InkDim),
                onClick = onClick
            )
            .padding(horizontal = 16.dp, vertical = 13.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(34.dp)
                .clip(RoundedCornerShape(9.dp))
                .background(Color.White.copy(alpha = 0.06f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = InkDim,
                modifier = Modifier.size(17.dp)
            )
        }

        Spacer(modifier = Modifier.width(13.dp))

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                fontFamily = ManropeFontFamily,
                fontWeight = FontWeight.Bold,
                fontSize = 13.5.sp,
                color = Ink,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Spacer(modifier = Modifier.height(1.dp))
            Text(
                text = subtitle,
                fontFamily = ManropeFontFamily,
                fontWeight = FontWeight.Medium,
                fontSize = 11.sp,
                color = InkDim,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )
        }

        Icon(
            imageVector = IsoraIcons.ChevronRight,
            contentDescription = null,
            tint = Color.White.copy(alpha = 0.35f),
            modifier = Modifier.size(16.dp)
        )
    }
}

@Composable
fun SettingsDivider() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 62.dp)
            .height(1.dp)
            .background(Color.White.copy(alpha = 0.06f))
    )
}
