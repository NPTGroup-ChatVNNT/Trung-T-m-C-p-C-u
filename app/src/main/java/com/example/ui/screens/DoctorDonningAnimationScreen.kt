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

/**
 * Hiệu ứng bác sĩ tung áo blouse trắng bước vào cửa khoa cấp cứu:
 * - Bác sĩ sải bước tiến về phía cửa khoa cấp cứu.
 * - Áo blouse tung bay hào hùng, vạt áo bay trong gió.
 * - Cửa tự động trượt mở đón bác sĩ vào khu cấp cứu tối khẩn.
 * - Ánh đèn cấp cứu phản chiếu hoành tráng.
 */
@Composable
fun DoctorDonningAnimationScreen(
    onAnimationFinished: () -> Unit
) {
    // Stage progresses automatically: 1 (Khoác áo sải bước) -> 2 (Tung vạt áo blouse) -> 3 (Cửa mở bừng sáng & bước vào) -> Kết thúc
    var stage by remember { mutableStateOf(1) }

    LaunchedEffect(Unit) {
        delay(900)
        stage = 2
        delay(1100)
        stage = 3
        delay(1300)
        onAnimationFinished()
    }

    // Door sliding animation (0f to 1f)
    val doorOpenProgress by animateFloatAsState(
        targetValue = if (stage == 3) 1f else 0f,
        animationSpec = tween(durationMillis = 1100, easing = FastOutSlowInEasing),
        label = "door_open_progress"
    )

    // Doctor walking stride transition (scale & position moving forward)
    val walkProgress by animateFloatAsState(
        targetValue = when (stage) {
            1 -> 0.15f
            2 -> 0.55f
            else -> 1.0f
        },
        animationSpec = tween(durationMillis = 1000, easing = LinearOutSlowInEasing),
        label = "walk_progress"
    )

    // Dynamic coat flare & cape fluttering wave
    val infiniteTransition = rememberInfiniteTransition(label = "coat_flutter")
    val flutterWave by infiniteTransition.animateFloat(
        initialValue = -1f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(400, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "flutter_wave"
    )

    val lightPulse by infiniteTransition.animateFloat(
        initialValue = 0.7f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(650, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "light_pulse"
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground)
    ) {
        // CINEMATIC CANVAS: BÁC SĨ TUNG ÁO BLOUSE BƯỚC VÀO CỬA KHOA CẤP CỨU
        Canvas(modifier = Modifier.fillMaxSize()) {
            val w = size.width
            val h = size.height

            // 1. Phông nền sảnh cấp cứu chiều sâu
            drawRect(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF030712), // Trần tối
                        Color(0xFF0F172A), // Không gian hành lang
                        Color(0xFF091428), // Sàn gạch bệnh viện
                        Color(0xFF020617)
                    ),
                    startY = 0f,
                    endY = h
                )
            )

            // 2. Dải đèn trần dẫn lối phối cảnh hội tụ về cửa
            val vanishPoint = Offset(w * 0.5f, h * 0.32f)
            for (i in 1..4) {
                val frac = i / 4f
                val lightY = vanishPoint.y + frac * (h * 0.22f)
                val leftX = vanishPoint.x - frac * (w * 0.44f)
                val rightX = vanishPoint.x + frac * (w * 0.44f)
                drawLine(
                    color = Color(0x3338BDF8),
                    start = Offset(leftX, lightY),
                    end = Offset(leftX + 30.dp.toPx() * frac, lightY),
                    strokeWidth = 2.5.dp.toPx() * frac
                )
                drawLine(
                    color = Color(0x3338BDF8),
                    start = Offset(rightX - 30.dp.toPx() * frac, lightY),
                    end = Offset(rightX, lightY),
                    strokeWidth = 2.5.dp.toPx() * frac
                )
            }

            // 3. CỬA ĐÔI KHOA CẤP CỨU (Kính trượt tự động)
            val doorWidth = w * 0.52f
            val doorHeight = h * 0.44f
            val doorTop = h * 0.16f
            val doorOffset = doorWidth * doorOpenProgress

            // Biển hiệu KHOA CẤP CỨU rực đỏ trên cửa
            val signWidth = doorWidth * 0.9f
            val signHeight = 22.dp.toPx()
            val signLeft = (w - signWidth) / 2f
            val signTop = doorTop - 26.dp.toPx()

            drawRoundRect(
                color = Color(0xFFDC2626),
                topLeft = Offset(signLeft, signTop),
                size = Size(signWidth, signHeight),
                cornerRadius = CornerRadius(4.dp.toPx(), 4.dp.toPx())
            )
            // Đèn khẩn cấp chớp sáng
            drawCircle(
                color = Color(0xFFEF4444).copy(alpha = lightPulse * 0.4f),
                radius = 35.dp.toPx(),
                center = Offset(w * 0.5f, signTop + signHeight / 2f)
            )

            // Luồng ánh sáng cứu sinh ùa ra khi cửa mở
            if (stage == 3 && doorOpenProgress > 0.05f) {
                val lightBeam = Path().apply {
                    moveTo(w * 0.5f, doorTop + 20.dp.toPx())
                    lineTo(w * (0.5f - 0.65f * doorOpenProgress), h)
                    lineTo(w * (0.5f + 0.65f * doorOpenProgress), h)
                    close()
                }
                drawPath(
                    path = lightBeam,
                    brush = Brush.radialGradient(
                        colors = listOf(
                            Color(0xCC38BDF8).copy(alpha = lightPulse * 0.7f),
                            Color(0x66FFFFFF).copy(alpha = lightPulse * 0.4f),
                            Color.Transparent
                        ),
                        center = Offset(w * 0.5f, doorTop + doorHeight * 0.4f),
                        radius = w * 0.85f
                    )
                )
            }

            // Khung cửa trượt bên trái
            val leftDoorX = (w * 0.5f - doorWidth) - doorOffset
            drawRoundRect(
                brush = Brush.horizontalGradient(listOf(Color(0x2238BDF8), Color(0x440284C7))),
                topLeft = Offset(leftDoorX, doorTop),
                size = Size(doorWidth, doorHeight),
                cornerRadius = CornerRadius(6.dp.toPx(), 6.dp.toPx())
            )
            drawRoundRect(
                color = Color(0x8894A3B8),
                topLeft = Offset(leftDoorX, doorTop),
                size = Size(doorWidth, doorHeight),
                cornerRadius = CornerRadius(6.dp.toPx(), 6.dp.toPx()),
                style = Stroke(width = 2.5.dp.toPx())
            )

            // Khung cửa trượt bên phải
            val rightDoorX = (w * 0.5f) + doorOffset
            drawRoundRect(
                brush = Brush.horizontalGradient(listOf(Color(0x440284C7), Color(0x2238BDF8))),
                topLeft = Offset(rightDoorX, doorTop),
                size = Size(doorWidth, doorHeight),
                cornerRadius = CornerRadius(6.dp.toPx(), 6.dp.toPx())
            )
            drawRoundRect(
                color = Color(0x8894A3B8),
                topLeft = Offset(rightDoorX, doorTop),
                size = Size(doorWidth, doorHeight),
                cornerRadius = CornerRadius(6.dp.toPx(), 6.dp.toPx()),
                style = Stroke(width = 2.5.dp.toPx())
            )

            // Vạch dẫn đường xe cấp cứu đỏ dẫn vào cửa
            drawLine(
                color = Color(0xCCEF4444),
                start = Offset(w * 0.5f, doorTop + doorHeight),
                end = Offset(w * 0.5f, h),
                strokeWidth = 3.dp.toPx()
            )

            // 4. BÁC SĨ TUNG ÁO BLOUSE TRẮNG SẢI BƯỚC TIẾN VÀO CỬA CẤP CỨU
            // Tỷ lệ bác sĩ tiến dần vào cửa (bước đi từ tiền cảnh hướng về cánh cửa)
            val scaleFactor = 1.0f + walkProgress * 0.25f
            val docCenterX = w * 0.5f
            val docCenterY = (h * 0.62f) - (walkProgress * 40.dp.toPx())

            // Sải bước chân (Chân trái & Chân phải đang bước)
            val legWidth = 20.dp.toPx() * scaleFactor
            val legHeight = 70.dp.toPx() * scaleFactor
            val stepOffset = if (stage >= 2) 18.dp.toPx() * scaleFactor else 8.dp.toPx()

            // Quần scrubs xanh thẫm
            drawRoundRect(
                color = Color(0xFF0F2B48),
                topLeft = Offset(docCenterX - legWidth - 4.dp.toPx(), docCenterY + 70.dp.toPx() * scaleFactor),
                size = Size(legWidth, legHeight + stepOffset * 0.5f),
                cornerRadius = CornerRadius(8.dp.toPx(), 8.dp.toPx())
            )
            drawRoundRect(
                color = Color(0xFF0A1E33),
                topLeft = Offset(docCenterX + 4.dp.toPx(), docCenterY + 70.dp.toPx() * scaleFactor - stepOffset * 0.3f),
                size = Size(legWidth, legHeight - stepOffset * 0.2f),
                cornerRadius = CornerRadius(8.dp.toPx(), 8.dp.toPx())
            )

            // Giày y tế chuyên dụng
            drawRoundRect(
                color = Color.White,
                topLeft = Offset(docCenterX - legWidth - 6.dp.toPx(), docCenterY + 70.dp.toPx() * scaleFactor + legHeight),
                size = Size(legWidth + 8.dp.toPx(), 14.dp.toPx() * scaleFactor),
                cornerRadius = CornerRadius(4.dp.toPx(), 4.dp.toPx())
            )
            drawRoundRect(
                color = Color.White,
                topLeft = Offset(docCenterX + 2.dp.toPx(), docCenterY + 70.dp.toPx() * scaleFactor + legHeight - stepOffset * 0.2f),
                size = Size(legWidth + 8.dp.toPx(), 14.dp.toPx() * scaleFactor),
                cornerRadius = CornerRadius(4.dp.toPx(), 4.dp.toPx())
            )

            // 5. VAT ÁO BLOUSE TRẮNG TUNG BAY HÀO HÙNG (Dramatic Fluttering White Coat)
            val coatFlareSpread = when (stage) {
                1 -> 40.dp.toPx() * scaleFactor
                2 -> 88.dp.toPx() * scaleFactor + (flutterWave * 8.dp.toPx())
                else -> 105.dp.toPx() * scaleFactor + (flutterWave * 12.dp.toPx())
            }

            val coatTop = docCenterY - 45.dp.toPx() * scaleFactor
            val coatBottom = docCenterY + 80.dp.toPx() * scaleFactor

            // Vạt áo blouse trắng tung bay bên trái
            val leftCoatPath = Path().apply {
                moveTo(docCenterX - 28.dp.toPx() * scaleFactor, coatTop + 15.dp.toPx())
                cubicTo(
                    docCenterX - 45.dp.toPx() * scaleFactor - coatFlareSpread * 0.4f, coatTop + 40.dp.toPx(),
                    docCenterX - 40.dp.toPx() * scaleFactor - coatFlareSpread, coatBottom - 20.dp.toPx(),
                    docCenterX - 30.dp.toPx() * scaleFactor - coatFlareSpread * 0.85f, coatBottom
                )
                lineTo(docCenterX - 10.dp.toPx() * scaleFactor, coatBottom - 10.dp.toPx())
                lineTo(docCenterX - 18.dp.toPx() * scaleFactor, coatTop + 20.dp.toPx())
                close()
            }
            // Bóng đổ vạt áo
            drawPath(
                path = leftCoatPath,
                color = Color(0xFFCBD5E1)
            )
            // Lớp áo chính màu trắng tinh
            drawPath(
                path = leftCoatPath,
                brush = Brush.horizontalGradient(
                    listOf(Color(0xFFE2E8F0), Color.White, Color(0xFFF8FAFC))
                )
            )

            // Vạt áo blouse trắng tung bay bên phải
            val rightCoatPath = Path().apply {
                moveTo(docCenterX + 28.dp.toPx() * scaleFactor, coatTop + 15.dp.toPx())
                cubicTo(
                    docCenterX + 45.dp.toPx() * scaleFactor + coatFlareSpread * 0.4f, coatTop + 40.dp.toPx(),
                    docCenterX + 40.dp.toPx() * scaleFactor + coatFlareSpread, coatBottom - 20.dp.toPx(),
                    docCenterX + 30.dp.toPx() * scaleFactor + coatFlareSpread * 0.85f, coatBottom
                )
                lineTo(docCenterX + 10.dp.toPx() * scaleFactor, coatBottom - 10.dp.toPx())
                lineTo(docCenterX + 18.dp.toPx() * scaleFactor, coatTop + 20.dp.toPx())
                close()
            }
            drawPath(
                path = rightCoatPath,
                color = Color(0xFFCBD5E1)
            )
            drawPath(
                path = rightCoatPath,
                brush = Brush.horizontalGradient(
                    listOf(Color(0xFFF8FAFC), Color.White, Color(0xFFE2E8F0))
                )
            )

            // Thân áo blouse & áo scrubs bên trong
            val torsoWidth = 62.dp.toPx() * scaleFactor
            val torsoHeight = 90.dp.toPx() * scaleFactor
            drawRoundRect(
                brush = Brush.verticalGradient(listOf(Color.White, Color(0xFFF1F5F9))),
                topLeft = Offset(docCenterX - torsoWidth / 2f, coatTop),
                size = Size(torsoWidth, torsoHeight),
                cornerRadius = CornerRadius(14.dp.toPx(), 14.dp.toPx())
            )

            // Cổ áo chữ V của áo Scrubs xanh bên trong
            val vScrubsPath = Path().apply {
                moveTo(docCenterX - 14.dp.toPx() * scaleFactor, coatTop)
                lineTo(docCenterX, coatTop + 24.dp.toPx() * scaleFactor)
                lineTo(docCenterX + 14.dp.toPx() * scaleFactor, coatTop)
                close()
            }
            drawPath(path = vScrubsPath, color = Color(0xFF0284C7))

            // Bảng tên bác sĩ cấp cứu (Emergency badge)
            drawRoundRect(
                color = Color(0xFFEF4444),
                topLeft = Offset(docCenterX - 24.dp.toPx() * scaleFactor, coatTop + 26.dp.toPx() * scaleFactor),
                size = Size(18.dp.toPx() * scaleFactor, 10.dp.toPx() * scaleFactor),
                cornerRadius = CornerRadius(2.dp.toPx(), 2.dp.toPx())
            )

            // Ống nghe Littmann vắt qua vai
            val stethPath = Path().apply {
                moveTo(docCenterX - 18.dp.toPx() * scaleFactor, coatTop + 2.dp.toPx())
                cubicTo(
                    docCenterX - 22.dp.toPx() * scaleFactor, coatTop + 45.dp.toPx() * scaleFactor,
                    docCenterX + 22.dp.toPx() * scaleFactor, coatTop + 45.dp.toPx() * scaleFactor,
                    docCenterX + 18.dp.toPx() * scaleFactor, coatTop + 2.dp.toPx()
                )
            }
            drawPath(
                path = stethPath,
                color = Color(0xFF0F172A),
                style = Stroke(width = 3.5.dp.toPx() * scaleFactor, cap = StrokeCap.Round)
            )
            // Quả chuông nghe kim loại
            drawCircle(
                color = Color(0xFF94A3B8),
                radius = 6.dp.toPx() * scaleFactor,
                center = Offset(docCenterX + 4.dp.toPx(), coatTop + 48.dp.toPx() * scaleFactor)
            )

            // Đầu và tóc bác sĩ nhìn về phía cửa
            val headRadius = 24.dp.toPx() * scaleFactor
            val headCenter = Offset(docCenterX, coatTop - headRadius * 0.85f)

            // Tóc đen ngắn chỉnh tề
            drawCircle(color = Color(0xFF0F172A), radius = headRadius * 1.12f, center = headCenter)
            // Gáy & cổ
            drawCircle(color = Color(0xFFFED7AA), radius = headRadius, center = headCenter)
            // Cổ áo blouse ôm gáy
            drawArc(
                color = Color.White,
                startAngle = 180f,
                sweepAngle = 180f,
                useCenter = true,
                topLeft = Offset(headCenter.x - headRadius * 0.9f, headCenter.y + 4.dp.toPx()),
                size = Size(headRadius * 1.8f, headRadius * 0.8f)
            )

            // Cánh tay vung mạnh khi bước vào (Găng tay y tế xanh vô khuẩn)
            val armY = coatTop + 35.dp.toPx() * scaleFactor
            val gloveColor = Color(0xFF0284C7)
            // Tay trái
            drawRoundRect(
                color = gloveColor,
                topLeft = Offset(docCenterX - torsoWidth * 0.75f - coatFlareSpread * 0.3f, armY + 20.dp.toPx()),
                size = Size(16.dp.toPx() * scaleFactor, 26.dp.toPx() * scaleFactor),
                cornerRadius = CornerRadius(6.dp.toPx(), 6.dp.toPx())
            )
            // Tay phải
            drawRoundRect(
                color = gloveColor,
                topLeft = Offset(docCenterX + torsoWidth * 0.55f + coatFlareSpread * 0.3f, armY + 14.dp.toPx()),
                size = Size(16.dp.toPx() * scaleFactor, 26.dp.toPx() * scaleFactor),
                cornerRadius = CornerRadius(6.dp.toPx(), 6.dp.toPx())
            )
        }

        // TOP CINEMATIC CAPTION
        Column(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(top = 54.dp, start = 20.dp, end = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val statusText = when (stage) {
                1 -> "Bác sĩ khoác áo blouse trắng, sải bước dứt khoát..."
                2 -> "Tung áo blouse trắng kiêu hãnh, chỉnh trang ống nghe..."
                else -> "Cửa khoa cấp cứu bật mở! Bước vào giành giật sự sống..."
            }

            Surface(
                shape = RoundedCornerShape(20.dp),
                color = Color(0xCC0F172A),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0x6638BDF8)),
                shadowElevation = 8.dp
            ) {
                Text(
                    text = statusText,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    modifier = Modifier.padding(horizontal = 18.dp, vertical = 8.dp)
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
                containerColor = Color(0xDD1E293B),
                contentColor = Color.White
            ),
            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0x8838BDF8))
        ) {
            Text("VÀO CA CẤP CỨU NGAY", fontSize = 13.sp, fontWeight = FontWeight.Bold)
            Spacer(modifier = Modifier.width(6.dp))
            Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null, modifier = Modifier.size(16.dp))
        }
    }
}
