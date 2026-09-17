package net.isora.vpn.compose.screen.isora.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.path
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import kotlin.math.cos
import kotlin.math.sin

object IsoraIcons {
    val Power: ImageVector
        get() = ImageVector.Builder(
            name = "Power",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(
                stroke = SolidColor(Color.White.copy(alpha = 0.85f)),
                strokeLineWidth = 1.6f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round
            ) {
                moveTo(12f, 3.5f)
                lineTo(12f, 11f)
                moveTo(16.5f, 6.2f)
                curveTo(19.2f, 7.8f, 20.8f, 10.7f, 20.8f, 14f)
                curveTo(20.8f, 18.9f, 16.9f, 22.8f, 12f, 22.8f)
                curveTo(7.1f, 22.8f, 3.2f, 18.9f, 3.2f, 14f)
                curveTo(3.2f, 10.7f, 4.8f, 7.8f, 7.5f, 6.2f)
            }
        }.build()

    val ChevronRight: ImageVector
        get() = ImageVector.Builder(
            name = "ChevronRight",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(
                stroke = SolidColor(Color.White.copy(alpha = 0.45f)),
                strokeLineWidth = 1.8f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round
            ) {
                moveTo(9f, 6f)
                lineTo(15f, 12f)
                lineTo(9f, 18f)
            }
        }.build()

    val Home: ImageVector
        get() = ImageVector.Builder(
            name = "Home",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(
                stroke = SolidColor(Color.White),
                strokeLineWidth = 1.7f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round
            ) {
                moveTo(3.5f, 9.5f)
                lineTo(12f, 2.8f)
                lineTo(20.5f, 9.5f)
                lineTo(20.5f, 20f)
                curveTo(20.5f, 21.1f, 19.6f, 22f, 18.5f, 22f)
                lineTo(5.5f, 22f)
                curveTo(4.4f, 22f, 3.5f, 21.1f, 3.5f, 20f)
                close()
            }
        }.build()

    val Servers: ImageVector
        get() = ImageVector.Builder(
            name = "Servers",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(
                stroke = SolidColor(Color.White),
                strokeLineWidth = 1.6f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round
            ) {
                // Shelf 1
                moveTo(4f, 4.5f)
                lineTo(20f, 4.5f)
                curveTo(21.1f, 4.5f, 22f, 5.4f, 22f, 6.5f)
                lineTo(22f, 8.5f)
                curveTo(22f, 9.6f, 21.1f, 10.5f, 20f, 10.5f)
                lineTo(4f, 10.5f)
                curveTo(2.9f, 10.5f, 2f, 9.6f, 2f, 8.5f)
                lineTo(2f, 6.5f)
                curveTo(2f, 5.4f, 2.9f, 4.5f, 4f, 4.5f)
                close()
                // Shelf 2
                moveTo(4f, 13.5f)
                lineTo(20f, 13.5f)
                curveTo(21.1f, 13.5f, 22f, 14.4f, 22f, 15.5f)
                lineTo(22f, 17.5f)
                curveTo(22f, 18.6f, 21.1f, 19.5f, 20f, 19.5f)
                lineTo(4f, 19.5f)
                curveTo(2.9f, 19.5f, 2f, 18.6f, 2f, 17.5f)
                lineTo(2f, 15.5f)
                curveTo(2f, 14.4f, 2.9f, 13.5f, 4f, 13.5f)
                close()
                // Dots
                moveTo(6f, 7.5f)
                lineTo(6.01f, 7.5f)
                moveTo(6f, 16.5f)
                lineTo(6.01f, 16.5f)
            }
        }.build()

    val User: ImageVector
        get() = ImageVector.Builder(
            name = "User",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(
                stroke = SolidColor(Color.White),
                strokeLineWidth = 1.6f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round
            ) {
                moveTo(12f, 11.5f)
                curveTo(14.2f, 11.5f, 16f, 9.7f, 16f, 7.5f)
                curveTo(16f, 5.3f, 14.2f, 3.5f, 12f, 3.5f)
                curveTo(9.8f, 3.5f, 8f, 5.3f, 8f, 7.5f)
                curveTo(8f, 9.7f, 9.8f, 11.5f, 12f, 11.5f)
                close()
                moveTo(4.5f, 20.5f)
                curveTo(4.5f, 16.8f, 7.8f, 14.8f, 12f, 14.8f)
                curveTo(16.2f, 14.8f, 19.5f, 16.8f, 19.5f, 20.5f)
            }
        }.build()

    val ArrowDown: ImageVector
        get() = ImageVector.Builder(
            name = "ArrowDown",
            defaultWidth = 14.dp,
            defaultHeight = 14.dp,
            viewportWidth = 14f,
            viewportHeight = 14f
        ).apply {
            path(
                stroke = SolidColor(Color.White),
                strokeLineWidth = 1.8f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round
            ) {
                moveTo(7f, 2f)
                lineTo(7f, 12f)
                moveTo(3f, 8f)
                lineTo(7f, 12f)
                lineTo(11f, 8f)
            }
        }.build()

    val ArrowUp: ImageVector
        get() = ImageVector.Builder(
            name = "ArrowUp",
            defaultWidth = 14.dp,
            defaultHeight = 14.dp,
            viewportWidth = 14f,
            viewportHeight = 14f
        ).apply {
            path(
                stroke = SolidColor(Color.White),
                strokeLineWidth = 1.8f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round
            ) {
                moveTo(7f, 12f)
                lineTo(7f, 2f)
                moveTo(3f, 6f)
                lineTo(7f, 2f)
                lineTo(11f, 6f)
            }
        }.build()

    val Search: ImageVector
        get() = ImageVector.Builder(
            name = "Search",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(
                stroke = SolidColor(Color.White),
                strokeLineWidth = 1.8f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round
            ) {
                moveTo(10.5f, 18f)
                curveTo(14.64f, 18f, 18f, 14.64f, 18f, 10.5f)
                curveTo(18f, 6.36f, 14.64f, 3f, 10.5f, 3f)
                curveTo(6.36f, 3f, 3f, 6.36f, 3f, 10.5f)
                curveTo(3f, 14.64f, 6.36f, 18f, 10.5f, 18f)
                close()
                moveTo(16f, 16f)
                lineTo(21f, 21f)
            }
        }.build()

    val Check: ImageVector
        get() = ImageVector.Builder(
            name = "Check",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(
                stroke = SolidColor(Color.White),
                strokeLineWidth = 2.2f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round
            ) {
                moveTo(4f, 12.5f)
                lineTo(9.5f, 18f)
                lineTo(20f, 6.5f)
            }
        }.build()

    val Shield: ImageVector
        get() = ImageVector.Builder(
            name = "Shield",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(
                stroke = SolidColor(Color.White),
                strokeLineWidth = 1.8f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round
            ) {
                moveTo(12f, 2.5f)
                lineTo(20f, 6f)
                curveTo(20f, 14f, 16f, 19.5f, 12f, 21.5f)
                curveTo(8f, 19.5f, 4f, 14f, 4f, 6f)
                close()
            }
        }.build()

    val Zap: ImageVector
        get() = ImageVector.Builder(
            name = "Zap",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(
                stroke = SolidColor(Color.White),
                strokeLineWidth = 1.8f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round
            ) {
                moveTo(13f, 2f)
                lineTo(4f, 13f)
                lineTo(11f, 13f)
                lineTo(10f, 22f)
                lineTo(20f, 10f)
                lineTo(13f, 10f)
                close()
            }
        }.build()

    val Crown: ImageVector
        get() = ImageVector.Builder(
            name = "Crown",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(
                stroke = SolidColor(Color.White),
                strokeLineWidth = 1.8f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round
            ) {
                moveTo(3f, 19f)
                lineTo(21f, 19f)
                moveTo(4f, 16f)
                lineTo(3f, 7f)
                lineTo(8.5f, 12f)
                lineTo(12f, 5f)
                lineTo(15.5f, 12f)
                lineTo(21f, 7f)
                lineTo(20f, 16f)
                close()
            }
        }.build()

    val Lock: ImageVector
        get() = ImageVector.Builder(
            name = "Lock",
            defaultWidth = 24.dp,
            defaultHeight = 24.dp,
            viewportWidth = 24f,
            viewportHeight = 24f
        ).apply {
            path(
                stroke = SolidColor(Color.White),
                strokeLineWidth = 1.8f,
                strokeLineCap = StrokeCap.Round,
                strokeLineJoin = StrokeJoin.Round
            ) {
                moveTo(6f, 10f)
                lineTo(18f, 10f)
                curveTo(19.1f, 10f, 20f, 10.9f, 20f, 12f)
                lineTo(20f, 20f)
                curveTo(20f, 21.1f, 19.1f, 22f, 18f, 22f)
                lineTo(6f, 22f)
                curveTo(4.9f, 22f, 4f, 21.1f, 4f, 20f)
                lineTo(4f, 12f)
                curveTo(4f, 10.9f, 4.9f, 10f, 6f, 10f)
                close()
                moveTo(8f, 10f)
                lineTo(8f, 7f)
                curveTo(8f, 4.8f, 9.8f, 3f, 12f, 3f)
                curveTo(14.2f, 3f, 16f, 4.8f, 16f, 7f)
                lineTo(16f, 10f)
            }
        }.build()
}

/**
 * High-fidelity vector rendering of the ISORA brand logo:
 * 3 aerodynamic, stacked curved blades / wings with subtle gradient and silky specular edge.
 */
@Composable
fun IsoraBrandLogo(
    modifier: Modifier = Modifier,
    tintColor: Color = Color.White
) {
    Canvas(modifier = modifier) {
        val w = size.width
        val h = size.height

        // Top blade
        val topPath = Path().apply {
            moveTo(w * 0.42f, h * 0.28f)
            cubicTo(w * 0.48f, h * 0.22f, w * 0.65f, h * 0.18f, w * 0.78f, h * 0.24f)
            cubicTo(w * 0.72f, h * 0.35f, w * 0.58f, h * 0.44f, w * 0.45f, h * 0.42f)
            close()
        }

        // Middle blade
        val midPath = Path().apply {
            moveTo(w * 0.32f, h * 0.42f)
            cubicTo(w * 0.40f, h * 0.35f, w * 0.62f, h * 0.34f, w * 0.74f, h * 0.45f)
            cubicTo(w * 0.65f, h * 0.58f, w * 0.48f, h * 0.63f, w * 0.35f, h * 0.58f)
            close()
        }

        // Bottom blade
        val botPath = Path().apply {
            moveTo(w * 0.26f, h * 0.58f)
            cubicTo(w * 0.35f, h * 0.52f, w * 0.52f, h * 0.52f, w * 0.62f, h * 0.65f)
            cubicTo(w * 0.54f, h * 0.78f, w * 0.38f, h * 0.82f, w * 0.28f, h * 0.74f)
            close()
        }

        val bladeBrush = Brush.linearGradient(
            colors = listOf(
                tintColor.copy(alpha = 0.95f),
                tintColor.copy(alpha = 0.75f),
                tintColor.copy(alpha = 0.45f)
            ),
            start = Offset(w * 0.2f, h * 0.2f),
            end = Offset(w * 0.8f, h * 0.8f)
        )

        drawPath(topPath, brush = bladeBrush, style = Fill)
        drawPath(midPath, brush = bladeBrush, style = Fill)
        drawPath(botPath, brush = bladeBrush, style = Fill)
    }
}

enum class CountryCode {
    NL, DE, US, GB, SG, JP, FR, CA, CH, SE, FI, EU
}

/**
 * Realistic glossy rounded flag pill for multiple countries with micro-glass sheen
 */
@Composable
fun CountryFlagBadge(
    code: CountryCode,
    modifier: Modifier = Modifier,
    badgeSize: Dp = 24.dp
) {
    Box(
        modifier = modifier
            .size(width = badgeSize * 1.35f, height = badgeSize)
            .clip(RoundedCornerShape(badgeSize * 0.35f))
            .border(
                1.dp,
                Brush.linearGradient(
                    listOf(Color.White.copy(alpha = 0.35f), Color.White.copy(alpha = 0.10f))
                ),
                RoundedCornerShape(badgeSize * 0.35f)
            )
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val w = this.size.width
            val h = this.size.height

            when (code) {
                CountryCode.NL -> {
                    val sh = h / 3f
                    drawRect(Color(0xFFA81C28), Offset(0f, 0f), Size(w, sh))
                    drawRect(Color(0xFFEEEEEE), Offset(0f, sh), Size(w, sh))
                    drawRect(Color(0xFF1E478A), Offset(0f, sh * 2f), Size(w, sh))
                }
                CountryCode.DE -> {
                    val sh = h / 3f
                    drawRect(Color(0xFF1B1C1E), Offset(0f, 0f), Size(w, sh))
                    drawRect(Color(0xFFD62A2B), Offset(0f, sh), Size(w, sh))
                    drawRect(Color(0xFFFFCC00), Offset(0f, sh * 2f), Size(w, sh))
                }
                CountryCode.US -> {
                    // Stripes
                    val sh = h / 7f
                    for (i in 0..6) {
                        drawRect(if (i % 2 == 0) Color(0xFFB22234) else Color(0xFFFFFFFF), Offset(0f, sh * i), Size(w, sh))
                    }
                    // Blue canton
                    drawRect(Color(0xFF1E3A8A), Offset(0f, 0f), Size(w * 0.45f, sh * 4f))
                }
                CountryCode.GB -> {
                    drawRect(Color(0xFF012169), Offset(0f, 0f), Size(w, h))
                    // Diagonal and Cross
                    drawLine(Color(0xFFFFFFFF), Offset(0f, 0f), Offset(w, h), strokeWidth = 3.5.dp.toPx())
                    drawLine(Color(0xFFFFFFFF), Offset(w, 0f), Offset(0f, h), strokeWidth = 3.5.dp.toPx())
                    drawLine(Color(0xFFC8102E), Offset(0f, 0f), Offset(w, h), strokeWidth = 1.8.dp.toPx())
                    drawLine(Color(0xFFC8102E), Offset(w, 0f), Offset(0f, h), strokeWidth = 1.8.dp.toPx())
                    // St George Cross
                    drawRect(Color(0xFFFFFFFF), Offset(w * 0.38f, 0f), Size(w * 0.24f, h))
                    drawRect(Color(0xFFFFFFFF), Offset(0f, h * 0.35f), Size(w, h * 0.30f))
                    drawRect(Color(0xFFC8102E), Offset(w * 0.43f, 0f), Size(w * 0.14f, h))
                    drawRect(Color(0xFFC8102E), Offset(0f, h * 0.40f), Size(w, h * 0.20f))
                }
                CountryCode.SG -> {
                    val sh = h / 2f
                    drawRect(Color(0xFFED2939), Offset(0f, 0f), Size(w, sh))
                    drawRect(Color(0xFFFFFFFF), Offset(0f, sh), Size(w, sh))
                    // Crescent on red
                    drawCircle(Color.White, radius = sh * 0.35f, center = Offset(w * 0.25f, sh * 0.5f))
                    drawCircle(Color(0xFFED2939), radius = sh * 0.30f, center = Offset(w * 0.28f, sh * 0.5f))
                }
                CountryCode.JP -> {
                    drawRect(Color(0xFFFFFFFF), Offset(0f, 0f), Size(w, h))
                    drawCircle(Color(0xFFBC002D), radius = h * 0.28f, center = Offset(w / 2f, h / 2f))
                }
                CountryCode.FR -> {
                    val sw = w / 3f
                    drawRect(Color(0xFF002395), Offset(0f, 0f), Size(sw, h))
                    drawRect(Color(0xFFFFFFFF), Offset(sw, 0f), Size(sw, h))
                    drawRect(Color(0xFFED2939), Offset(sw * 2f, 0f), Size(sw, h))
                }
                CountryCode.CA -> {
                    val sw = w * 0.25f
                    drawRect(Color(0xFFFF0000), Offset(0f, 0f), Size(sw, h))
                    drawRect(Color(0xFFFFFFFF), Offset(sw, 0f), Size(w * 0.5f, h))
                    drawRect(Color(0xFFFF0000), Offset(sw + w * 0.5f, 0f), Size(sw, h))
                    drawCircle(Color(0xFFFF0000), radius = h * 0.22f, center = Offset(w / 2f, h / 2f))
                }
                CountryCode.CH -> {
                    drawRect(Color(0xFFD52B1E), Offset(0f, 0f), Size(w, h))
                    // White cross
                    drawRect(Color(0xFFFFFFFF), Offset(w * 0.42f, h * 0.20f), Size(w * 0.16f, h * 0.60f))
                    drawRect(Color(0xFFFFFFFF), Offset(w * 0.25f, h * 0.40f), Size(w * 0.50f, h * 0.20f))
                }
                CountryCode.SE -> {
                    drawRect(Color(0xFF006AA7), Offset(0f, 0f), Size(w, h))
                    // Yellow cross
                    drawRect(Color(0xFFFECC00), Offset(w * 0.32f, 0f), Size(w * 0.16f, h))
                    drawRect(Color(0xFFFECC00), Offset(0f, h * 0.40f), Size(w, h * 0.20f))
                }
                CountryCode.FI -> {
                    drawRect(Color(0xFFFFFFFF), Offset(0f, 0f), Size(w, h))
                    // Blue Nordic cross
                    drawRect(Color(0xFF002F6C), Offset(w * 0.32f, 0f), Size(w * 0.16f, h))
                    drawRect(Color(0xFF002F6C), Offset(0f, h * 0.40f), Size(w, h * 0.20f))
                }
                CountryCode.EU -> {
                    drawRect(Color(0xFF003399), Offset(0f, 0f), Size(w, h))
                    // 12 yellow stars in a circle
                    val cx = w / 2f
                    val cy = h / 2f
                    val orbit = h * 0.30f
                    val starR = h * 0.055f
                    for (i in 0 until 12) {
                        val angle = (Math.PI * 2.0 * i / 12.0 - Math.PI / 2.0).toFloat()
                        drawCircle(
                            Color(0xFFFFDD00),
                            radius = starR,
                            center = Offset(cx + cos(angle) * orbit, cy + sin(angle) * orbit)
                        )
                    }
                }
            }

            // Gloss shine on top half for Apple-like premium finish
            drawRect(
                brush = Brush.verticalGradient(
                    listOf(Color.White.copy(alpha = 0.35f), Color.Transparent),
                    startY = 0f,
                    endY = h * 0.6f
                ),
                topLeft = Offset(0f, 0f),
                size = Size(w, h * 0.6f)
            )
        }
    }
}

/**
 * Backward compatibility wrapper for Netherlands flag
 */
@Composable
fun NetherlandsFlagBadge(
    modifier: Modifier = Modifier,
    badgeSize: Dp = 24.dp
) {
    CountryFlagBadge(code = CountryCode.NL, modifier = modifier, badgeSize = badgeSize)
}
