package com.example.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.ui.theme.MedicalPrimary
import com.example.ui.theme.MedicalPrimaryDark
import com.example.ui.theme.MedicalGreenLight

@Composable
fun HospitalLogo(
    modifier: Modifier = Modifier,
    size: Dp = 80.dp,
    animatedPulse: Boolean = true,
    onClick: (() -> Unit)? = null
) {
    val infiniteTransition = rememberInfiniteTransition(label = "logo_pulse")
    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.85f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(900, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse_alpha"
    )

    Box(
        modifier = modifier
            .size(size)
            .shadow(12.dp, RoundedCornerShape(size * 0.24f), spotColor = MedicalPrimary)
            .clip(RoundedCornerShape(size * 0.24f))
            .background(
                Brush.verticalGradient(
                    listOf(MedicalPrimary, MedicalPrimaryDark)
                )
            )
            .then(
                if (onClick != null) Modifier.clickable { onClick() } else Modifier
            )
            .testTag("hospital_logo"),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val w = this.size.width
            val h = this.size.height

            // 1. Subtle background medical grid lines
            val gridColor = Color(0x25FFFFFF)
            drawLine(gridColor, Offset(w * 0.2f, 0f), Offset(w * 0.2f, h), strokeWidth = 1f)
            drawLine(gridColor, Offset(w * 0.5f, 0f), Offset(w * 0.5f, h), strokeWidth = 1f)
            drawLine(gridColor, Offset(w * 0.8f, 0f), Offset(w * 0.8f, h), strokeWidth = 1f)
            drawLine(gridColor, Offset(0f, h * 0.35f), Offset(w, h * 0.35f), strokeWidth = 1f)
            drawLine(gridColor, Offset(0f, h * 0.65f), Offset(w, h * 0.65f), strokeWidth = 1f)

            // 2. White Medical Cross
            val crossThickness = w * 0.22f
            val crossLength = w * 0.62f
            val crossLeft = (w - crossThickness) / 2f
            val crossTop = (h - crossThickness) / 2f

            // Vertical arm
            drawRoundRect(
                color = Color.White,
                topLeft = Offset(crossLeft, (h - crossLength) / 2f),
                size = Size(crossThickness, crossLength),
                cornerRadius = CornerRadius(4.dp.toPx(), 4.dp.toPx())
            )
            // Horizontal arm
            drawRoundRect(
                color = Color.White,
                topLeft = Offset((w - crossLength) / 2f, crossTop),
                size = Size(crossLength, crossThickness),
                cornerRadius = CornerRadius(4.dp.toPx(), 4.dp.toPx())
            )

            // 3. Electric ECG rhythm wave cutting across center
            val ecgPath = Path().apply {
                val midY = h * 0.5f
                moveTo(0f, midY)
                lineTo(w * 0.22f, midY)
                lineTo(w * 0.30f, midY)
                lineTo(w * 0.35f, midY - h * 0.08f) // small P
                lineTo(w * 0.40f, midY)
                lineTo(w * 0.44f, midY + h * 0.06f) // Q dip
                lineTo(w * 0.50f, midY - h * 0.30f) // Sharp R peak
                lineTo(w * 0.56f, midY + h * 0.22f) // S dip
                lineTo(w * 0.62f, midY)
                lineTo(w * 0.70f, midY - h * 0.12f) // T wave
                lineTo(w * 0.78f, midY)
                lineTo(w, midY)
            }

            // Outer dark teal accent stroke for contrast through the white cross
            drawPath(
                path = ecgPath,
                color = MedicalPrimaryDark.copy(alpha = 0.95f),
                style = Stroke(
                    width = (w * 0.09f),
                    cap = StrokeCap.Round,
                    join = StrokeJoin.Round
                )
            )

            // Inner vibrant emerald pulse line
            drawPath(
                path = ecgPath,
                color = MedicalGreenLight.copy(alpha = if (animatedPulse) pulseAlpha else 1f),
                style = Stroke(
                    width = (w * 0.045f),
                    cap = StrokeCap.Round,
                    join = StrokeJoin.Round
                )
            )
        }
    }
}
