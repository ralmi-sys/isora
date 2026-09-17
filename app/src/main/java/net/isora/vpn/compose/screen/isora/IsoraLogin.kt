package net.isora.vpn.compose.screen.isora

import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.ripple
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import net.isora.vpn.compose.screen.isora.IsoraLoginViewModel
import net.isora.vpn.compose.screen.isora.ui.theme.AccentGreen
import net.isora.vpn.compose.screen.isora.ui.theme.Ink
import net.isora.vpn.compose.screen.isora.ui.theme.InkDim
import net.isora.vpn.compose.screen.isora.ui.theme.ManropeFontFamily

/** Карточка входа: без профиля вместо кнопки подписки. */
@Composable
fun IsoraLoginCard(
    onLoggedIn: () -> Unit,
    loginViewModel: IsoraLoginViewModel = viewModel(),
    modifier: Modifier = Modifier,
) {
    val state by loginViewModel.uiState.collectAsState()
    val context = LocalContext.current

    LaunchedEffect(state.phase) {
        if (state.phase == IsoraLoginViewModel.Phase.Done) onLoggedIn()
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(
                androidx.compose.ui.graphics.Brush.verticalGradient(
                    listOf(
                        Color(0xFF4074E0).copy(alpha = 0.16f),
                        Color(0xFF285ADC).copy(alpha = 0.06f)
                    )
                )
            )
            .border(
                1.dp,
                androidx.compose.ui.graphics.Brush.verticalGradient(
                    listOf(
                        Color(0xFF609BFF).copy(alpha = 0.40f),
                        Color(0xFF285ADC).copy(alpha = 0.15f)
                    )
                ),
                RoundedCornerShape(20.dp)
            )
            .padding(18.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(
                text = "Вход через Telegram",
                fontFamily = ManropeFontFamily,
                fontWeight = FontWeight.ExtraBold,
                fontSize = 17.sp,
                color = Ink
            )
            Text(
                text = when (state.phase) {
                    IsoraLoginViewModel.Phase.Waiting -> "Открой бота, нажми Start — и возвращайся, дальше само"
                    IsoraLoginViewModel.Phase.Error -> state.error ?: "Не вышло"
                    else -> "Подписка подтянется сама, ничего вставлять не надо"
                },
                fontFamily = ManropeFontFamily,
                fontWeight = FontWeight.Medium,
                fontSize = 13.sp,
                color = InkDim,
                textAlign = TextAlign.Center
            )

            if (state.phase == IsoraLoginViewModel.Phase.Waiting && state.botUrl == null) {
                CircularProgressIndicator(color = AccentGreen)
            }

            if (state.phase == IsoraLoginViewModel.Phase.Idle ||
                state.phase == IsoraLoginViewModel.Phase.Error
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(AccentGreen)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = ripple(color = Color.White),
                            onClick = { loginViewModel.startLogin() }
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = if (state.phase == IsoraLoginViewModel.Phase.Error) "Попробовать снова" else "Войти",
                        fontFamily = ManropeFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = Color(0xFF05281E)
                    )
                }
            }

            if (state.phase == IsoraLoginViewModel.Phase.Waiting && state.botUrl != null) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(AccentGreen)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = ripple(color = Color.White),
                            onClick = {
                                runCatching {
                                    context.startActivity(
                                        Intent(Intent.ACTION_VIEW, Uri.parse(state.botUrl))
                                    )
                                }
                            }
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Открыть бота",
                        fontFamily = ManropeFontFamily,
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = Color(0xFF05281E)
                    )
                }
                Box(
                    modifier = Modifier
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null,
                            onClick = { loginViewModel.cancel() }
                        )
                        .padding(vertical = 4.dp)
                ) {
                    Text(
                        text = "Отмена",
                        fontFamily = ManropeFontFamily,
                        fontWeight = FontWeight.Medium,
                        fontSize = 13.sp,
                        color = InkDim
                    )
                }
            }

            Spacer(modifier = Modifier.height(2.dp))
        }
    }
}
