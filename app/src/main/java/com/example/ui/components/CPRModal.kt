package com.example.ui.components

import android.content.Context
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.audio.HospitalAudioSynthesizer
import com.example.ui.theme.*

@Composable
fun CPRModal(
    onDismiss: () -> Unit,
    onRoscAchieved: () -> Unit
) {
    val context = LocalContext.current
    var compressionCount by remember { mutableStateOf(0) }
    var currentCycle by remember { mutableStateOf(1) }
    var lastTapTime by remember { mutableStateOf(0L) }
    var currentBpm by remember { mutableStateOf(110) }
    var qualityFeedback by remember { mutableStateOf("BẮT ĐẦU ÉP TIM THEO NHỊP METRONOME") }
    var feedbackColor by remember { mutableStateOf(Color.White) }
    var goodCompressionStreak by remember { mutableStateOf(0) }
    var isRoscTriggered by remember { mutableStateOf(false) }

    // Start CPR Metronome at 110 BPM
    DisposableEffect(Unit) {
        HospitalAudioSynthesizer.startMetronome()
        onDispose {
            HospitalAudioSynthesizer.stopMetronome()
        }
    }

    // Metronome visual pulsation (110 BPM = 545ms)
    val infiniteTransition = rememberInfiniteTransition(label = "metro_pulse")
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 0.92f,
        targetValue = 1.08f,
        animationSpec = infiniteRepeatable(
            animation = tween(272, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "pulse_scale"
    )

    Dialog(onDismissRequest = {
        HospitalAudioSynthesizer.stopMetronome()
        onDismiss()
    }) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF0B132B)),
            border = androidx.compose.foundation.BorderStroke(2.dp, MedicalRed)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Favorite,
                            contentDescription = null,
                            tint = MedicalRed,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "HỒI SINH TIM PHỔI (CPR)",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Black,
                            color = Color.White
                        )
                    }
                    IconButton(onClick = {
                        HospitalAudioSynthesizer.stopMetronome()
                        onDismiss()
                    }) {
                        Icon(Icons.Default.Close, contentDescription = "Đóng", tint = TextSecondary)
                    }
                }

                Text(
                    text = "QUY TRÌNH HỒI SỨC TIM PHỔI NÂNG CAO (ACLS) 30:2",
                    fontSize = 10.sp,
                    color = TextSecondary,
                    fontFamily = FontFamily.Monospace,
                    modifier = Modifier.align(Alignment.Start)
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Stats Row: Cycle, Compressions, Current BPM
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Cycle
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(8.dp))
                            .background(DarkSurfaceVariant)
                            .padding(8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = "CHU KỲ", fontSize = 9.sp, color = TextSecondary)
                        Text(
                            text = "$currentCycle/5",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Black,
                            color = AlertOrange
                        )
                    }

                    // Count 30
                    Column(
                        modifier = Modifier
                            .weight(1.2f)
                            .clip(RoundedCornerShape(8.dp))
                            .background(DarkSurfaceVariant)
                            .padding(8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = "NHỊP ÉP (30:2)", fontSize = 9.sp, color = TextSecondary)
                        Text(
                            text = "$compressionCount / 30",
                            fontSize = 18.sp,
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Black,
                            color = MedicalRedLight
                        )
                    }

                    // Measured BPM
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(8.dp))
                            .background(DarkSurfaceVariant)
                            .padding(8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(text = "TẦN SỐ", fontSize = 9.sp, color = TextSecondary)
                        Text(
                            text = "$currentBpm",
                            fontSize = 16.sp,
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Black,
                            color = if (currentBpm in 100..120) EcgGreen else AlertOrange
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Quality Feedback Pill
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(20.dp))
                        .background(feedbackColor.copy(alpha = 0.15f))
                        .border(1.dp, feedbackColor, RoundedCornerShape(20.dp))
                        .padding(horizontal = 12.dp, vertical = 6.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = qualityFeedback,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = feedbackColor
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Interactive Push Compression Button (with pulsing ring)
                Box(
                    modifier = Modifier
                        .size(130.dp)
                        .scale(pulseScale)
                        .clip(CircleShape)
                        .background(MedicalRed)
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        ) {
                            if (isRoscTriggered) return@clickable
                            val now = System.currentTimeMillis()
                            if (lastTapTime > 0) {
                                val deltaMs = now - lastTapTime
                                if (deltaMs in 200..1500) {
                                    val calcBpm = (60000 / deltaMs).toInt()
                                    currentBpm = calcBpm
                                    if (calcBpm in 100..120) {
                                        qualityFeedback = "CHÍNH XÁC! TẦN SỐ & ĐỘ SÂU ĐẠT CHUẨN 5-6 CM"
                                        feedbackColor = EcgGreen
                                        goodCompressionStreak++
                                    } else if (calcBpm < 100) {
                                        qualityFeedback = "ÉP NHANH HƠN CHÚT NỮA (MỤC TIÊU 100-120 L/P)"
                                        feedbackColor = AlertOrange
                                    } else {
                                        qualityFeedback = "ÉP CHẬM LẠI MỘT CHÚT ĐỂ NGỰC NỞ HOÀN TOÀN"
                                        feedbackColor = AlertOrange
                                    }
                                }
                            }
                            lastTapTime = now

                            // Haptic tap
                            shortVibration(context)

                            compressionCount++
                            if (compressionCount >= 30) {
                                compressionCount = 0
                                currentCycle++
                                qualityFeedback = "BÓP BÓNG 2 NHỊP THÔNG KHÍ (30:2) -> TIẾP TỤC ÉP TIM!"
                                feedbackColor = Spo2Cyan
                            }

                            // If player delivered enough high quality compressions (streak >= 18)
                            if (goodCompressionStreak >= 18 && !isRoscTriggered) {
                                isRoscTriggered = true
                                qualityFeedback = "TIM ĐÃ TỰ ĐẬP LẠI (ROSC THÀNH CÔNG)!"
                                feedbackColor = EcgGreen
                                onRoscAchieved()
                            }
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(
                            imageVector = Icons.Default.Favorite,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(36.dp)
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "CHẠM ÉP TIM",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Black,
                            color = Color.White
                        )
                        Text(
                            text = "(110 BPM)",
                            fontSize = 10.sp,
                            color = Color(0xFFFEF08A)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Standard guidance text
                Text(
                    text = "Hướng dẫn Bộ Y Tế: Ép sâu 5-6 cm ở nửa dưới xương ức, để ngực nở lại hoàn toàn sau mỗi lần ép, tần số 100-120 lần/phút, hạn chế tối đa gián đoạn ép tim (< 10 giây).",
                    fontSize = 10.sp,
                    color = TextSecondary,
                    lineHeight = 14.sp
                )
            }
        }
    }
}

private fun shortVibration(context: Context) {
    try {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val vibratorManager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as? VibratorManager
            vibratorManager?.defaultVibrator?.vibrate(
                VibrationEffect.createOneShot(45, VibrationEffect.DEFAULT_AMPLITUDE)
            )
        } else {
            @Suppress("DEPRECATION")
            val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
            @Suppress("DEPRECATION")
            vibrator?.vibrate(45)
        }
    } catch (_: Exception) {}
}
