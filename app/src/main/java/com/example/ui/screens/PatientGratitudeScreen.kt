package com.example.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.VolunteerActivism
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.PatientCase
import com.example.ui.theme.*
import kotlin.math.sin

@Composable
fun PatientGratitudeScreen(
    patientCase: PatientCase,
    onContinueToDebrief: () -> Unit
) {
    // Floating particles & warm sunlight pulse animation
    val infiniteTransition = rememberInfiniteTransition(label = "gratitude_anim")
    val sunbeamAlpha by infiniteTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 0.8f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "sunbeam_alpha"
    )

    val floatingOffset by infiniteTransition.animateFloat(
        initialValue = -6f,
        targetValue = 6f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "floating_patient"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF061826))
    ) {
        // ANIME BACKGROUND CANVAS: WARM SUNLIGHT & RECOVERED PATIENT ROOM
        Canvas(modifier = Modifier.fillMaxSize()) {
            val w = size.width
            val h = size.height

            // 1. Warm hospital room morning background
            drawRect(
                brush = Brush.verticalGradient(
                    colors = listOf(Color(0xFF0F3048), Color(0xFF081C2C)),
                    startY = 0f,
                    endY = h
                )
            )

            // Warm volumetric golden sunbeams streaming through hospital window
            val sunbeamPath = Path().apply {
                moveTo(w * 0.9f, 0f)
                lineTo(w, 0f)
                lineTo(w * 0.35f, h)
                lineTo(0f, h)
                close()
            }
            drawPath(
                path = sunbeamPath,
                brush = Brush.linearGradient(
                    colors = listOf(
                        Color(0x55FEF08A).copy(alpha = sunbeamAlpha * 0.6f),
                        Color(0x33FDE047).copy(alpha = sunbeamAlpha * 0.4f),
                        Color(0x00FEF08A)
                    ),
                    start = Offset(w * 0.9f, 0f),
                    end = Offset(w * 0.2f, h)
                )
            )

            // Floating warm light orbs/particles
            val particleCount = 14
            for (i in 0 until particleCount) {
                val px = (w * (0.15f + (i * 0.06f))) % w
                val py = (h * 0.3f + sin(i * 1.5f) * 120f + floatingOffset * 4f)
                drawCircle(
                    color = Color(0x66FEF08A),
                    radius = (3f + (i % 4) * 2f),
                    center = Offset(px, py)
                )
            }

            // 2. ANIME STYLE RECOVERED PATIENT & FAMILY ILLUSTRATION
            val centerX = w * 0.5f
            val patientY = h * 0.46f + floatingOffset

            // Hospital Bed
            drawRoundRect(
                color = Color(0xFF1E293B),
                topLeft = Offset(centerX - w * 0.42f, patientY + 40.dp.toPx()),
                size = Size(w * 0.84f, 130.dp.toPx()),
                cornerRadius = CornerRadius(16.dp.toPx(), 16.dp.toPx())
            )
            // Crisp White Bed Sheet
            drawRoundRect(
                color = Color(0xFFF1F5F9),
                topLeft = Offset(centerX - w * 0.38f, patientY + 55.dp.toPx()),
                size = Size(w * 0.76f, 100.dp.toPx()),
                cornerRadius = CornerRadius(12.dp.toPx(), 12.dp.toPx())
            )

            // Patient Head (Awake, smiling, healthy blush on cheeks)
            val headCenter = Offset(centerX - 35.dp.toPx(), patientY - 20.dp.toPx())
            val headR = 36.dp.toPx()

            // Hair
            drawCircle(color = Color(0xFF334155), radius = headR * 1.1f, center = headCenter)
            // Healthy face
            drawCircle(color = Color(0xFFFDE68A), radius = headR, center = headCenter)
            // Pink cheeks (Recovered perfusion!)
            drawCircle(color = Color(0x66F43F5E), radius = 8.dp.toPx(), center = Offset(headCenter.x - 14.dp.toPx(), headCenter.y + 8.dp.toPx()))
            drawCircle(color = Color(0x66F43F5E), radius = 8.dp.toPx(), center = Offset(headCenter.x + 14.dp.toPx(), headCenter.y + 8.dp.toPx()))

            // Smiling eyes (Anime crescent curves)
            val leftEye = Path().apply {
                moveTo(headCenter.x - 18.dp.toPx(), headCenter.y - 2.dp.toPx())
                cubicTo(
                    headCenter.x - 14.dp.toPx(), headCenter.y - 8.dp.toPx(),
                    headCenter.x - 8.dp.toPx(), headCenter.y - 8.dp.toPx(),
                    headCenter.x - 4.dp.toPx(), headCenter.y - 2.dp.toPx()
                )
            }
            drawPath(path = leftEye, color = Color(0xFF0F172A), style = androidx.compose.ui.graphics.drawscope.Stroke(width = 3.dp.toPx()))

            val rightEye = Path().apply {
                moveTo(headCenter.x + 4.dp.toPx(), headCenter.y - 2.dp.toPx())
                cubicTo(
                    headCenter.x + 8.dp.toPx(), headCenter.y - 8.dp.toPx(),
                    headCenter.x + 14.dp.toPx(), headCenter.y - 8.dp.toPx(),
                    headCenter.x + 18.dp.toPx(), headCenter.y - 2.dp.toPx()
                )
            }
            drawPath(path = rightEye, color = Color(0xFF0F172A), style = androidx.compose.ui.graphics.drawscope.Stroke(width = 3.dp.toPx()))

            // Warm Smile
            val smile = Path().apply {
                moveTo(headCenter.x - 10.dp.toPx(), headCenter.y + 16.dp.toPx())
                cubicTo(
                    headCenter.x - 5.dp.toPx(), headCenter.y + 24.dp.toPx(),
                    headCenter.x + 5.dp.toPx(), headCenter.y + 24.dp.toPx(),
                    headCenter.x + 10.dp.toPx(), headCenter.y + 16.dp.toPx()
                )
            }
            drawPath(path = smile, color = Color(0xFFBE123C), style = androidx.compose.ui.graphics.drawscope.Stroke(width = 2.5.dp.toPx()))

            // Family Member Standing beside (Hands clasped in gratitude)
            val famCenter = Offset(centerX + 55.dp.toPx(), patientY - 35.dp.toPx())
            val famR = 30.dp.toPx()
            drawCircle(color = Color(0xFF1E293B), radius = famR * 1.1f, center = famCenter)
            drawCircle(color = Color(0xFFFEF08A), radius = famR, center = famCenter)

            // Family member eyes closed with tears of relief/joy
            drawLine(Color(0xFF0F172A), Offset(famCenter.x - 12.dp.toPx(), famCenter.y), Offset(famCenter.x - 2.dp.toPx(), famCenter.y), strokeWidth = 2.5f)
            drawLine(Color(0xFF0F172A), Offset(famCenter.x + 2.dp.toPx(), famCenter.y), Offset(famCenter.x + 12.dp.toPx(), famCenter.y), strokeWidth = 2.5f)
            // Tear drop
            drawCircle(color = Color(0xFF38BDF8), radius = 3.dp.toPx(), center = Offset(famCenter.x - 14.dp.toPx(), famCenter.y + 6.dp.toPx()))

            // Family smile
            drawArc(
                color = Color(0xFFBE123C),
                startAngle = 0f,
                sweepAngle = 180f,
                useCenter = false,
                topLeft = Offset(famCenter.x - 6.dp.toPx(), famCenter.y + 10.dp.toPx()),
                size = Size(12.dp.toPx(), 8.dp.toPx()),
                style = androidx.compose.ui.graphics.drawscope.Stroke(width = 2f)
            )

            // Clasped hands in prayer/gratitude
            drawCircle(color = Color(0xFFFDE68A), radius = 10.dp.toPx(), center = Offset(famCenter.x - 8.dp.toPx(), famCenter.y + 45.dp.toPx()))
        }

        // TOP BANNER
        Column(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 54.dp, start = 20.dp, end = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
                    .background(Color(0xCC065F46))
                    .border(1.dp, EcgGreen, RoundedCornerShape(20.dp))
                    .padding(horizontal = 16.dp, vertical = 8.dp)
            ) {
                Icon(Icons.Default.VolunteerActivism, contentDescription = null, tint = EcgGreen, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "CỨU SỐNG THÀNH CÔNG NGOẠN MỤC!",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Black,
                    color = Color.White,
                    letterSpacing = 0.5.sp
                )
            }
        }

        // BOTTOM DIALOGUE & ACTION CARD
        Card(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .padding(20.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xEE0F172A)),
            border = androidx.compose.foundation.BorderStroke(1.5.dp, EcgGreen)
        ) {
            Column(
                modifier = Modifier.padding(18.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Favorite, contentDescription = null, tint = MedicalRedLight, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = patientCase.gratitudeSpeaker.uppercase(),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Black,
                        color = Color(0xFFFEF08A)
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                Text(
                    text = "\"${patientCase.gratitudeMessage}\"",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.White,
                    textAlign = TextAlign.Center,
                    lineHeight = 19.sp
                )

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = onContinueToDebrief,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = EcgGreen,
                        contentColor = Color.Black
                    )
                ) {
                    Text(
                        text = "TIẾN HÀNH GIAO BAN & BÀN GIAO CA",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null, modifier = Modifier.size(18.dp))
                }
            }
        }
    }
}
