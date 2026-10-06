package com.example.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.BeVietnamProFontFamily

/**
 * LOGO CHÍNH THỨC CỦA ỨNG DỤNG THEO THIẾT KẾ ĐƯỢC TẢI LÊN:
 * Hình: EMS_20261006_092816_0000.png
 * - Nền: Tím pastel / Lavender (#E2C4FF)
 * - Chữ trên: "NPT Med"
 * - Biểu tượng: Chữ thập y tế kết hợp ống nghe stethoscope (Cánh trên-phải đỏ #EE2737, các cánh còn lại tím #AF4BDE)
 * - Chữ dưới: "EMS" (EM xanh đen navy #181829, S xanh cobalt #2B35AF)
 */
@Composable
fun EmsAppLogo(
    modifier: Modifier = Modifier,
    size: Dp = 100.dp,
    showText: Boolean = true,
    onClick: (() -> Unit)? = null
) {
    val infiniteTransition = rememberInfiniteTransition(label = "logo_pulse")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 0.98f,
        targetValue = 1.02f,
        animationSpec = infiniteRepeatable(
            animation = tween(1400, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse_scale"
    )

    val baseModifier = modifier
        .size(size)
        .scale(pulseScale)
        .clip(RoundedCornerShape(size * 0.22f))
        .background(Color(0xFFE2C4FF))
        .border(1.5.dp, Color(0xFFC084FC), RoundedCornerShape(size * 0.22f))
        .then(if (onClick != null) Modifier.clickable { onClick() } else Modifier)

    Box(
        modifier = baseModifier,
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize().padding(horizontal = size * 0.08f, vertical = size * 0.06f)
        ) {
            if (showText) {
                // Chữ trên: NPT Med
                Text(
                    text = "NPT Med",
                    fontFamily = BeVietnamProFontFamily,
                    fontSize = (size.value * 0.11f).sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White,
                    letterSpacing = 1.2.sp,
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(size * 0.02f))
            }

            // Canvas vẽ biểu tượng chữ thập kết hợp ống nghe (Đỏ + Tím)
            val emblemSize = if (showText) size * 0.52f else size * 0.72f
            Canvas(modifier = Modifier.size(emblemSize)) {
                val w = this.size.width
                val h = this.size.height
                val strokeW = w * 0.14f
                val redColor = Color(0xFFEE2737)
                val purpleColor = Color(0xFFAF4BDE)

                // 1. Cánh trên-phải của chữ thập (Màu đỏ tươi)
                val redPath = Path().apply {
                    // Nhánh dọc trên phía phải
                    moveTo(w * 0.5f, h * 0.12f)
                    cubicTo(w * 0.5f, h * 0.04f, w * 0.68f, h * 0.04f, w * 0.68f, h * 0.12f)
                    lineTo(w * 0.68f, h * 0.38f)
                    // Nhánh ngang phải
                    lineTo(w * 0.90f, h * 0.38f)
                    cubicTo(w * 0.98f, h * 0.38f, w * 0.98f, h * 0.56f, w * 0.90f, h * 0.56f)
                }
                drawPath(
                    path = redPath,
                    color = redColor,
                    style = Stroke(width = strokeW, cap = StrokeCap.Round, join = StrokeJoin.Round)
                )

                // 2. Nhánh dưới, trái và quai ống nghe (Màu tím)
                val purplePath = Path().apply {
                    // Bắt đầu từ nhánh ngang trái
                    moveTo(w * 0.12f, h * 0.56f)
                    cubicTo(w * 0.04f, h * 0.56f, w * 0.04f, h * 0.38f, w * 0.12f, h * 0.38f)
                    lineTo(w * 0.34f, h * 0.38f)
                    lineTo(w * 0.34f, h * 0.12f)
                    cubicTo(w * 0.34f, h * 0.04f, w * 0.50f, h * 0.04f, w * 0.50f, h * 0.12f)
                    
                    // Đi xuống nhánh dưới
                    moveTo(w * 0.34f, h * 0.56f)
                    lineTo(w * 0.34f, h * 0.88f)
                    cubicTo(w * 0.34f, h * 0.98f, w * 0.68f, h * 0.98f, w * 0.68f, h * 0.88f)
                    lineTo(w * 0.68f, h * 0.56f)
                }
                drawPath(
                    path = purplePath,
                    color = purpleColor,
                    style = Stroke(width = strokeW, cap = StrokeCap.Round, join = StrokeJoin.Round)
                )

                // 3. Ống nghe Stethoscope uốn lượn qua thân chữ thập
                val stethPath = Path().apply {
                    moveTo(w * 0.22f, h * 0.28f)
                    cubicTo(w * 0.12f, h * 0.35f, w * 0.25f, h * 0.50f, w * 0.45f, h * 0.50f)
                    cubicTo(w * 0.65f, h * 0.50f, w * 0.92f, h * 0.58f, w * 0.80f, h * 0.72f)
                    cubicTo(w * 0.70f, h * 0.82f, w * 0.55f, h * 0.65f, w * 0.62f, h * 0.52f)
                }
                drawPath(
                    path = stethPath,
                    color = purpleColor,
                    style = Stroke(width = strokeW * 0.45f, cap = StrokeCap.Round, join = StrokeJoin.Round)
                )

                // Tai nghe (Earpieces / Bell)
                drawCircle(color = purpleColor, radius = strokeW * 0.45f, center = Offset(w * 0.22f, h * 0.28f))
                drawCircle(color = purpleColor, radius = strokeW * 0.40f, center = Offset(w * 0.27f, h * 0.20f))
                drawCircle(color = purpleColor, radius = strokeW * 0.60f, center = Offset(w * 0.76f, h * 0.52f))
            }

            if (showText) {
                Spacer(modifier = Modifier.height(size * 0.02f))
                // Chữ dưới: EMS
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "EM",
                        fontFamily = BeVietnamProFontFamily,
                        fontSize = (size.value * 0.17f).sp,
                        fontWeight = FontWeight.Black,
                        color = Color(0xFF16162B),
                        letterSpacing = 1.5.sp
                    )
                    Text(
                        text = "S",
                        fontFamily = BeVietnamProFontFamily,
                        fontSize = (size.value * 0.17f).sp,
                        fontWeight = FontWeight.Black,
                        color = Color(0xFF2B35AF),
                        letterSpacing = 1.5.sp
                    )
                }
            }
        }
    }
}
