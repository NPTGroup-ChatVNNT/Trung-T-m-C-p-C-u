package com.example.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.ui.theme.MedicalNavy
import com.example.ui.theme.MedicalPrimary
import com.example.ui.theme.MedicalPrimaryDark
import com.example.ui.theme.MedicalPrimaryLight
import com.example.ui.theme.MedicalRed

/**
 * EMS Emergency Medical Service Badge Logo (Star of Life + Rod of Asclepius + Vital Wave)
 * Represents "EMS_20261006_092816_0000.png" uploaded by user.
 */
@Composable
fun HospitalLogo(
    modifier: Modifier = Modifier,
    size: Dp = 96.dp,
    animatedPulse: Boolean = true,
    onClick: (() -> Unit)? = null
) {
    val infiniteTransition = rememberInfiniteTransition(label = "logo_pulse")
    val pulseGlow by infiniteTransition.animateFloat(
        initialValue = 0.85f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(1100, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse_glow"
    )

    Box(
        modifier = modifier
            .size(size)
            .shadow(16.dp, CircleShape, spotColor = Color(0xFF2563EB))
            .clip(CircleShape)
            .background(
                Brush.radialGradient(
                    colors = listOf(Color(0xFF1E293B), Color(0xFF0F172A), Color(0xFF020617))
                )
            )
            .border(2.5.dp, Brush.sweepGradient(listOf(Color(0xFFF59E0B), Color(0xFF38BDF8), Color(0xFF2563EB), Color(0xFFF59E0B))), CircleShape)
            .then(
                if (onClick != null) Modifier.clickable { onClick() } else Modifier
            )
            .testTag("hospital_logo"),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize().padding(size * 0.08f)) {
            val w = this.size.width
            val h = this.size.height
            val center = Offset(w / 2f, h / 2f)

            // 1. Concentric gold outer ring
            drawCircle(
                color = Color(0xFFF59E0B).copy(alpha = 0.35f),
                radius = w * 0.48f,
                style = Stroke(width = w * 0.02f)
            )

            // 2. EMS Star of Life (6-pointed cross, 3 rectangles rotated by 0, 60, 120 deg)
            val armWidth = w * 0.22f
            val armLength = w * 0.86f
            val armCorner = w * 0.04f
            val blueBrush = Brush.verticalGradient(
                listOf(Color(0xFF3B82F6), Color(0xFF1D4ED8), Color(0xFF1E3A8A))
            )

            for (angle in listOf(0f, 60f, 120f)) {
                rotate(degrees = angle, pivot = center) {
                    drawRoundRect(
                        brush = blueBrush,
                        topLeft = Offset(center.x - armWidth / 2f, center.y - armLength / 2f),
                        size = Size(armWidth, armLength),
                        cornerRadius = CornerRadius(armCorner, armCorner)
                    )
                    // White border highlight around arms
                    drawRoundRect(
                        color = Color.White.copy(alpha = 0.7f),
                        topLeft = Offset(center.x - armWidth / 2f, center.y - armLength / 2f),
                        size = Size(armWidth, armLength),
                        cornerRadius = CornerRadius(armCorner, armCorner),
                        style = Stroke(width = 1.5.dp.toPx())
                    )
                }
            }

            // 3. Central Red Emergency Cross
            val redCrossThick = w * 0.16f
            val redCrossLen = w * 0.44f
            drawRoundRect(
                color = Color(0xFFEF4444),
                topLeft = Offset(center.x - redCrossThick / 2f, center.y - redCrossLen / 2f),
                size = Size(redCrossThick, redCrossLen),
                cornerRadius = CornerRadius(3.dp.toPx(), 3.dp.toPx())
            )
            drawRoundRect(
                color = Color(0xFFEF4444),
                topLeft = Offset(center.x - redCrossLen / 2f, center.y - redCrossThick / 2f),
                size = Size(redCrossLen, redCrossThick),
                cornerRadius = CornerRadius(3.dp.toPx(), 3.dp.toPx())
            )

            // 4. Rod of Asclepius (Golden staff in center)
            val staffTop = center.y - w * 0.32f
            val staffBottom = center.y + w * 0.32f
            drawLine(
                color = Color(0xFFFBBF24),
                start = Offset(center.x, staffTop),
                end = Offset(center.x, staffBottom),
                strokeWidth = w * 0.045f,
                cap = StrokeCap.Round
            )
            // Staff knob top
            drawCircle(
                color = Color(0xFFFBBF24),
                radius = w * 0.04f,
                center = Offset(center.x, staffTop)
            )

            // 5. Coiled Serpent around staff (White & Gold)
            val snakePath = Path().apply {
                val amp = w * 0.11f
                moveTo(center.x - amp * 0.6f, center.y - w * 0.22f)
                cubicTo(
                    center.x + amp * 1.2f, center.y - w * 0.18f,
                    center.x + amp * 1.2f, center.y - w * 0.08f,
                    center.x, center.y - w * 0.04f
                )
                cubicTo(
                    center.x - amp * 1.2f, center.y,
                    center.x - amp * 1.2f, center.y + w * 0.10f,
                    center.x, center.y + w * 0.14f
                )
                cubicTo(
                    center.x + amp * 1.1f, center.y + w * 0.18f,
                    center.x - amp * 0.4f, center.y + w * 0.24f,
                    center.x, center.y + w * 0.28f
                )
            }
            drawPath(
                path = snakePath,
                color = Color.White,
                style = Stroke(
                    width = w * 0.045f,
                    cap = StrokeCap.Round,
                    join = StrokeJoin.Round
                )
            )

            // 6. Glowing Electric ECG Rhythm wave across lower section
            val ecgPath = Path().apply {
                val waveY = center.y + w * 0.28f
                moveTo(center.x - w * 0.44f, waveY)
                lineTo(center.x - w * 0.24f, waveY)
                lineTo(center.x - w * 0.16f, waveY)
                lineTo(center.x - w * 0.10f, waveY - w * 0.08f) // P
                lineTo(center.x - w * 0.04f, waveY)
                lineTo(center.x, waveY + w * 0.08f) // Q dip
                lineTo(center.x + w * 0.06f, waveY - w * 0.22f) // R spike
                lineTo(center.x + w * 0.12f, waveY + w * 0.12f) // S dip
                lineTo(center.x + w * 0.18f, waveY)
                lineTo(center.x + w * 0.26f, waveY - w * 0.06f) // T
                lineTo(center.x + w * 0.32f, waveY)
                lineTo(center.x + w * 0.44f, waveY)
            }
            drawPath(
                path = ecgPath,
                color = Color(0xFF0284C7).copy(alpha = 0.9f),
                style = Stroke(width = w * 0.05f, cap = StrokeCap.Round, join = StrokeJoin.Round)
            )
            drawPath(
                path = ecgPath,
                color = Color(0xFF38BDF8).copy(alpha = if (animatedPulse) pulseGlow.coerceIn(0.7f, 1f) else 1f),
                style = Stroke(width = w * 0.025f, cap = StrokeCap.Round, join = StrokeJoin.Round)
            )
        }
    }
}
