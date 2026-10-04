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
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.FlashOn
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
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
fun DefibrillatorModal(
    onDismiss: () -> Unit,
    onShockDelivered: (joules: Int) -> Unit
) {
    val context = LocalContext.current
    val energyLevels = listOf(120, 150, 200, 300, 360)
    var selectedJoules by remember { mutableStateOf(200) }
    var isCharging by remember { mutableStateOf(false) }
    var isChargedAndReady by remember { mutableStateOf(false) }
    var chargeProgress by remember { mutableStateOf(0f) }

    // Animate charge progress
    LaunchedEffect(isCharging) {
        if (isCharging) {
            isChargedAndReady = false
            chargeProgress = 0f
            HospitalAudioSynthesizer.startDefibCharge {
                isCharging = false
                isChargedAndReady = true
                chargeProgress = 1f
            }
            // Linear progress tick
            val startTime = System.currentTimeMillis()
            while (isCharging && chargeProgress < 1f) {
                val elapsed = System.currentTimeMillis() - startTime
                chargeProgress = (elapsed / 2500f).coerceIn(0f, 1f)
                kotlinx.coroutines.delay(40)
            }
        }
    }

    // Flash ready shock button
    val infiniteTransition = rememberInfiniteTransition(label = "shock_flash")
    val shockButtonScale by infiniteTransition.animateFloat(
        initialValue = 0.96f,
        targetValue = 1.04f,
        animationSpec = infiniteRepeatable(
            animation = tween(400, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "shock_scale"
    )

    Dialog(onDismissRequest = {
        HospitalAudioSynthesizer.stopDefibCharge()
        onDismiss()
    }) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(4.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF0F172A)),
            border = androidx.compose.foundation.BorderStroke(2.dp, AlertOrange)
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
                            imageVector = Icons.Default.FlashOn,
                            contentDescription = null,
                            tint = AlertOrange,
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "MÁY SỐC ĐIỆN KHỬ RUNG",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Black,
                            color = Color.White
                        )
                    }
                    IconButton(onClick = {
                        HospitalAudioSynthesizer.stopDefibCharge()
                        onDismiss()
                    }) {
                        Icon(Icons.Default.Close, contentDescription = "Đóng", tint = TextSecondary)
                    }
                }

                Text(
                    text = "DEFIBRILLATOR BIPHASIC MANUAL CONSOLE",
                    fontSize = 10.sp,
                    color = TextSecondary,
                    fontFamily = FontFamily.Monospace,
                    modifier = Modifier.align(Alignment.Start)
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Energy Selection Row
                Text(
                    text = "BƯỚC 1: CHỌN MỨC NĂNG LƯỢNG (JOULES)",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = AlertOrange,
                    modifier = Modifier.align(Alignment.Start)
                )
                Spacer(modifier = Modifier.height(6.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    energyLevels.forEach { joules ->
                        val isSelected = selectedJoules == joules
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (isSelected) AlertOrange else DarkSurfaceVariant)
                                .border(
                                    1.dp,
                                    if (isSelected) Color.White else BorderSubtle,
                                    RoundedCornerShape(8.dp)
                                )
                                .clickable(enabled = !isCharging && !isChargedAndReady) {
                                    selectedJoules = joules
                                }
                                .padding(vertical = 8.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "${joules}J",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isSelected) Color.Black else TextPrimary
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Capacitor Charge Status & Progress
                if (isCharging) {
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = "ĐANG NẠP TỤ ĐIỆN CAO ÁP...",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = AlertOrange
                            )
                            Text(
                                text = "${(chargeProgress * 100).toInt()}%",
                                fontSize = 11.sp,
                                fontFamily = FontFamily.Monospace,
                                color = Color.White
                            )
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        LinearProgressIndicator(
                            progress = { chargeProgress },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(8.dp)
                                .clip(RoundedCornerShape(4.dp)),
                            color = AlertOrange,
                            trackColor = DarkSurfaceVariant
                        )
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                }

                // Safety Warning Announcement Box
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(if (isChargedAndReady) MedicalRed.copy(alpha = 0.2f) else DarkSurfaceVariant)
                        .border(
                            1.dp,
                            if (isChargedAndReady) MedicalRed else BorderSubtle,
                            RoundedCornerShape(8.dp)
                        )
                        .padding(10.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Warning,
                            contentDescription = null,
                            tint = if (isChargedAndReady) MedicalRedLight else AlertOrange,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(
                                text = "CẢNH BÁO AN TOÀN SỐC ĐIỆN:",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isChargedAndReady) MedicalRedLight else AlertOrange
                            )
                            Text(
                                text = "Hô to: 'TÔI ĐỨNG XA, BẠN ĐỨNG XA, TẤT CẢ ĐỨNG XA - KHÔNG CHẠM VÀO BỆNH NHÂN!'",
                                fontSize = 11.sp,
                                color = TextPrimary
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Action Buttons: Charge or Shock
                if (!isChargedAndReady) {
                    Button(
                        onClick = { isCharging = true },
                        enabled = !isCharging,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = AlertOrange,
                            contentColor = Color.Black
                        )
                    ) {
                        Icon(Icons.Default.FlashOn, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (isCharging) "ĐANG NẠP ĐIỆN..." else "BƯỚC 2: NẠP ĐIỆN (CHARGE $selectedJoules J)",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                } else {
                    // Huge Red/Orange Pulsing Shock Button
                    Button(
                        onClick = {
                            HospitalAudioSynthesizer.playShockZap()
                            // Trigger haptic vibration
                            triggerVibration(context)
                            onShockDelivered(selectedJoules)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(64.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MedicalRed,
                            contentColor = Color.White
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Default.FlashOn,
                            contentDescription = null,
                            modifier = Modifier.size(28.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "BƯỚC 3: PHÓNG ĐIỆN NGAY (SHOCK ${selectedJoules}J)!",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Black,
                            letterSpacing = 0.5.sp
                        )
                    }
                }
            }
        }
    }
}

private fun triggerVibration(context: Context) {
    try {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val vibratorManager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as? VibratorManager
            vibratorManager?.defaultVibrator?.vibrate(
                VibrationEffect.createOneShot(350, VibrationEffect.DEFAULT_AMPLITUDE)
            )
        } else {
            @Suppress("DEPRECATION")
            val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as? Vibrator
            @Suppress("DEPRECATION")
            vibrator?.vibrate(350)
        }
    } catch (_: Exception) {}
}
