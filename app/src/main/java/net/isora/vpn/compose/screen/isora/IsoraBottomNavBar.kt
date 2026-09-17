package net.isora.vpn.compose.screen.isora.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import net.isora.vpn.compose.screen.isora.data.ScreenTab
import net.isora.vpn.compose.screen.isora.ui.theme.Ink
import net.isora.vpn.compose.screen.isora.ui.theme.InkFaint
import net.isora.vpn.compose.screen.isora.ui.theme.ManropeFontFamily

@Composable
fun IsoraBottomNavBar(
    currentTab: ScreenTab,
    onTabSelected: (ScreenTab) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(Color(0xFF06080F).copy(alpha = 0.92f))
    ) {
        // Subtle divider line
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(Color.White.copy(alpha = 0.08f))
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .windowInsetsPadding(WindowInsets.navigationBars)
                .height(64.dp)
                .padding(horizontal = 20.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            NavBarTabItem(
                tab = ScreenTab.Home,
                icon = IsoraIcons.Home,
                isSelected = currentTab == ScreenTab.Home,
                onClick = { onTabSelected(ScreenTab.Home) }
            )

            NavBarTabItem(
                tab = ScreenTab.Servers,
                icon = IsoraIcons.Servers,
                isSelected = currentTab == ScreenTab.Servers,
                onClick = { onTabSelected(ScreenTab.Servers) }
            )

            NavBarTabItem(
                tab = ScreenTab.Account,
                icon = IsoraIcons.User,
                isSelected = currentTab == ScreenTab.Account,
                onClick = { onTabSelected(ScreenTab.Account) }
            )
        }
    }
}

@Composable
private fun NavBarTabItem(
    tab: ScreenTab,
    icon: ImageVector,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val textColor by animateColorAsState(
        targetValue = if (isSelected) Ink else InkFaint,
        animationSpec = tween(250),
        label = "tabTextColor"
    )

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable(
            interactionSource = remember { MutableInteractionSource() },
            indication = null,
            onClick = onClick
        )
    ) {
        if (isSelected) {
            Box(
                modifier = Modifier
                    .size(width = 54.dp, height = 30.dp)
                    .clip(RoundedCornerShape(15.dp))
                    .background(
                        Brush.linearGradient(
                            listOf(
                                Color(0xFF4074E0).copy(alpha = 0.28f),
                                Color(0xFF285ADC).copy(alpha = 0.12f)
                            )
                        )
                    )
                    .border(
                        1.dp,
                        Color(0xFF609BFF).copy(alpha = 0.32f),
                        RoundedCornerShape(15.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = tab.title,
                    tint = Ink,
                    modifier = Modifier.size(19.dp)
                )
            }
        } else {
            Box(
                modifier = Modifier.size(width = 54.dp, height = 30.dp),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = tab.title,
                    tint = InkFaint,
                    modifier = Modifier.size(20.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(3.dp))

        Text(
            text = tab.title,
            fontFamily = ManropeFontFamily,
            fontWeight = FontWeight.Bold,
            fontSize = 10.sp,
            color = textColor
        )
    }
}
