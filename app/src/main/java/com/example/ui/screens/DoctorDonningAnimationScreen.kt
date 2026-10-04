package com.example.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*
import kotlinx.coroutines.delay

@Composable
fun DoctorDonningAnimationScreen(
    onAnimationFinished: () -> Unit
) {
    // Stage progresses automatically: 1 -> 2 -> 3 -> Auto-finish
    var stage by remember { mutableStateOf(1) }

    // Fully automated background progression
    LaunchedEffect(Unit) {
        delay(900)
        stage = 2
        delay(1000)
        stage = 3
        delay(1200)
        onAnimationFinished() // Automatically enters emergency bay!
    }

    // Door sliding animation (0f to 1f)
    val doorOpenProgress by animateFloatAsState(
        targetValue = if (stage == 3) 1f else 0f,
        animationSpec = tween(durationMillis = 1000, easing = FastOutSlowInEasing),
        label = "door_open_progress"
    )

    // Ambient light glow animation
    val infiniteTransition = rememberInfiniteTransition(label = "ambient_light")
    val lightPulse by infiniteTransition.animateFloat(
        initialValue = 0.7f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(700, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "light_pulse"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground)
    ) {
        // AUTOMATED 2D BACKGROUND ANIMATION CANVAS
        Canvas(modifier = Modifier.fillMaxSize()) {
            val w = size.width
            val h = size.height

            // 1. Emergency Trauma Room Lighting Background
            drawRect(
                brush = Brush.verticalGradient(
                    colors = listOf(Color(0xFF0F172A), Color(0xFF050814)),
                    startY = 0f,
                    endY = h
                )
            )

            // Volumetric light beams bursting through sliding doors
            if (stage == 3 && doorOpenProgress > 0.05f) {
                val lightPath = Path().apply {
                    moveTo(w * 0.5f, h * 0.15f)
                    lineTo(w * (0.5f - 0.55f * doorOpenProgress), h)
                    lineTo(w * (0.5f + 0.55f * doorOpenProgress), h)
                    close()
                }
                drawPath(
                    path = lightPath,
                    brush = Brush.radialGradient(
                        colors = listOf(
                            Color(0xDD38BDF8).copy(alpha = lightPulse * 0.8f),
                            Color(0x66FFFFFF).copy(alpha = lightPulse * 0.5f),
                            Color(0x000284C7)
                        ),
                        center = Offset(w * 0.5f, h * 0.38f),
                        radius = w * 0.75f
                    )
                )
            }

            // 2. SLIDING GLASS DOUBLE DOORS
            val doorWidth = w * 0.48f
            val doorHeight = h * 0.65f
            val doorTop = h * 0.18f
            val doorOffset = doorWidth * doorOpenProgress

            // Left Door
            val leftDoorX = (w * 0.5f - doorWidth) - doorOffset
            drawRoundRect(
                color = Color(0x3338BDF8),
                topLeft = Offset(leftDoorX, doorTop),
                size = Size(doorWidth, doorHeight),
                cornerRadius = CornerRadius(8.dp.toPx(), 8.dp.toPx())
            )
            drawRoundRect(
                color = Color(0x8894A3B8),
                topLeft = Offset(leftDoorX, doorTop),
                size = Size(doorWidth, doorHeight),
                cornerRadius = CornerRadius(8.dp.toPx(), 8.dp.toPx()),
                style = Stroke(width = 3.dp.toPx())
            )
            drawCircle(
                color = MedicalRed.copy(alpha = (1f - doorOpenProgress * 0.7f)),
                radius = 24.dp.toPx(),
                center = Offset(leftDoorX + doorWidth - 40.dp.toPx(), doorTop + doorHeight * 0.45f)
            )

            // Right Door
            val rightDoorX = (w * 0.5f) + doorOffset
            drawRoundRect(
                color = Color(0x3338BDF8),
                topLeft = Offset(rightDoorX, doorTop),
                size = Size(doorWidth, doorHeight),
                cornerRadius = CornerRadius(8.dp.toPx(), 8.dp.toPx())
            )
            drawRoundRect(
                color = Color(0x8894A3B8),
                topLeft = Offset(rightDoorX, doorTop),
                size = Size(doorWidth, doorHeight),
                cornerRadius = CornerRadius(8.dp.toPx(), 8.dp.toPx()),
                style = Stroke(width = 3.dp.toPx())
            )
            drawCircle(
                color = MedicalRed.copy(alpha = (1f - doorOpenProgress * 0.7f)),
                radius = 24.dp.toPx(),
                center = Offset(rightDoorX + 40.dp.toPx(), doorTop + doorHeight * 0.45f)
            )

            // 3. 2D DOCTOR FIGURE (ANIME/GAME STYLING)
            val docCenterX = w * 0.5f
            val docCenterY = h * 0.54f

            // Head & Hair
            val headRadius = 40.dp.toPx()
            val headCenter = Offset(docCenterX, docCenterY - 110.dp.toPx())

            drawCircle(color = Color(0xFF1E293B), radius = headRadius * 1.15f, center = headCenter)
            drawCircle(color = Color(0xFFFDE68A), radius = headRadius, center = headCenter)

            // Focused anime eyes
            drawCircle(color = Color(0xFF0F172A), radius = 5.dp.toPx(), center = Offset(docCenterX - 14.dp.toPx(), headCenter.y - 4.dp.toPx()))
            drawCircle(color = Color(0xFF0F172A), radius = 5.dp.toPx(), center = Offset(docCenterX + 14.dp.toPx(), headCenter.y - 4.dp.toPx()))
            drawCircle(color = Color.White, radius = 2.dp.toPx(), center = Offset(docCenterX - 15.dp.toPx(), headCenter.y - 6.dp.toPx()))
            drawCircle(color = Color.White, radius = 2.dp.toPx(), center = Offset(docCenterX + 13.dp.toPx(), headCenter.y - 6.dp.toPx()))

            // Blue Medical Mask
            val maskPath = Path().apply {
                moveTo(headCenter.x - headRadius * 0.85f, headCenter.y + 4.dp.toPx())
                lineTo(headCenter.x + headRadius * 0.85f, headCenter.y + 4.dp.toPx())
                lineTo(headCenter.x + headRadius * 0.65f, headCenter.y + headRadius * 0.95f)
                lineTo(headCenter.x - headRadius * 0.65f, headCenter.y + headRadius * 0.95f)
                close()
            }
            drawPath(path = maskPath, color = Color(0xFF0284C7))

            // Body: Scrubs & White Lab Coat
            val torsoTop = headCenter.y + headRadius * 0.85f
            val torsoWidth = 110.dp.toPx()
            val torsoHeight = 160.dp.toPx()

            // Scrubs Base
            drawRoundRect(
                color = ScrubTealDark,
                topLeft = Offset(docCenterX - torsoWidth / 2f, torsoTop),
                size = Size(torsoWidth, torsoHeight),
                cornerRadius = CornerRadius(16.dp.toPx(), 16.dp.toPx())
            )

            // Stage 1+: White Lab Coat gliding on shoulders & Badge
            if (stage >= 1) {
                val coatWidth = torsoWidth * 1.25f
                val coatHeight = torsoHeight * 1.15f
                val coatLeft = docCenterX - coatWidth / 2f

                drawRoundRect(
                    color = Color(0xFFF8FAFC),
                    topLeft = Offset(coatLeft, torsoTop - 6.dp.toPx()),
                    size = Size(coatWidth, coatHeight),
                    cornerRadius = CornerRadius(20.dp.toPx(), 20.dp.toPx())
                )
                // Coat lapels
                drawLine(
                    color = Color(0xFF94A3B8),
                    start = Offset(docCenterX - 22.dp.toPx(), torsoTop - 4.dp.toPx()),
                    end = Offset(docCenterX - 8.dp.toPx(), torsoTop + 60.dp.toPx()),
                    strokeWidth = 3f
                )
                drawLine(
                    color = Color(0xFF94A3B8),
                    start = Offset(docCenterX + 22.dp.toPx(), torsoTop - 4.dp.toPx()),
                    end = Offset(docCenterX + 8.dp.toPx(), torsoTop + 60.dp.toPx()),
                    strokeWidth = 3f
                )

                // Red Emergency Name Tag Badge
                val badgeX = docCenterX - coatWidth * 0.35f
                val badgeY = torsoTop + 35.dp.toPx()
                drawRoundRect(
                    color = MedicalRed,
                    topLeft = Offset(badgeX, badgeY),
                    size = Size(28.dp.toPx(), 16.dp.toPx()),
                    cornerRadius = CornerRadius(3.dp.toPx(), 3.dp.toPx())
                )
                drawRoundRect(
                    color = Color.White,
                    topLeft = Offset(badgeX + 2.dp.toPx(), badgeY + 2.dp.toPx()),
                    size = Size(24.dp.toPx(), 5.dp.toPx()),
                    cornerRadius = CornerRadius(1.dp.toPx(), 1.dp.toPx())
                )
            }

            // Stage 2+: Stethoscope draped over neck & Blue Nitrile Gloves
            if (stage >= 2) {
                val stethPath = Path().apply {
                    moveTo(docCenterX - 26.dp.toPx(), torsoTop)
                    cubicTo(
                        docCenterX - 30.dp.toPx(), torsoTop + 65.dp.toPx(),
                        docCenterX + 30.dp.toPx(), torsoTop + 65.dp.toPx(),
                        docCenterX + 26.dp.toPx(), torsoTop
                    )
                }
                drawPath(
                    path = stethPath,
                    color = Color(0xFF1E293B),
                    style = Stroke(width = 5.dp.toPx(), cap = StrokeCap.Round)
                )
                drawCircle(
                    color = Color(0xFF94A3B8),
                    radius = 9.dp.toPx(),
                    center = Offset(docCenterX + 2.dp.toPx(), torsoTop + 68.dp.toPx())
                )

                // Blue Nitrile Gloves
                val handY = torsoTop + torsoHeight * 0.72f
                val gloveColor = Color(0xFF0284C7)
                drawRoundRect(
                    color = gloveColor,
                    topLeft = Offset(docCenterX - torsoWidth * 0.72f, handY),
                    size = Size(26.dp.toPx(), 36.dp.toPx()),
                    cornerRadius = CornerRadius(10.dp.toPx(), 10.dp.toPx())
                )
                drawRoundRect(
                    color = gloveColor,
                    topLeft = Offset(docCenterX + torsoWidth * 0.44f, handY),
                    size = Size(26.dp.toPx(), 36.dp.toPx()),
                    cornerRadius = CornerRadius(10.dp.toPx(), 10.dp.toPx())
                )
            }
        }

        // TOP CINEMATIC OVERLAY
        Column(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 54.dp, start = 20.dp, end = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val statusText = when (stage) {
                1 -> "Bác sĩ khoác áo blouse trắng, cài bảng tên ca trực..."
                2 -> "Quàng ống nghe Littmann, đeo găng tay y tế vô khuẩn..."
                else -> "Cửa đôi cấp cứu tự động mở! Đang tiến vào ca trực..."
            }

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color(0xCC0F172A))
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Text(
                    text = statusText,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }
        }

        // BOTTOM QUICK SKIP BUTTON
        Button(
            onClick = onAnimationFinished,
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 36.dp, start = 32.dp, end = 32.dp)
                .fillMaxWidth()
                .height(48.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color(0xAA1E293B),
                contentColor = Color.White
            )
        ) {
            Text("VÀO CA NGAY", fontSize = 13.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.width(6.dp))
            Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null, modifier = Modifier.size(16.dp))
        }
    }
}
