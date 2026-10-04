package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.audio.HospitalAudioSynthesizer
import com.example.model.EcgRhythm
import com.example.model.VitalSigns
import com.example.ui.theme.*
import kotlin.math.PI
import kotlin.math.sin

@Composable
fun PatientMonitorView(
    vitals: VitalSigns,
    remainingSeconds: Int,
    isSoundMuted: Boolean,
    modifier: Modifier = Modifier
) {
    // Determine critical alert status
    val isCritical = vitals.heartRate < 45 || vitals.heartRate > 130 ||
            vitals.spo2 < 85 || vitals.bpSys < 85 ||
            vitals.rhythm == EcgRhythm.VFIB || vitals.rhythm == EcgRhythm.ASYSTOLE

    val isYellowWarning = !isCritical && (vitals.heartRate > 105 || vitals.spo2 < 93 || vitals.bpSys < 95)

    // Alarm management
    LaunchedEffect(isCritical, isYellowWarning, isSoundMuted) {
        if (!isSoundMuted) {
            if (isCritical) {
                HospitalAudioSynthesizer.startAlarm(isRed = true)
            } else if (isYellowWarning) {
                HospitalAudioSynthesizer.startAlarm(isRed = false)
            } else {
                HospitalAudioSynthesizer.stopAlarm()
            }
        } else {
            HospitalAudioSynthesizer.stopAlarm()
        }
    }

    // Regular heartbeat beep based on SpO2 (if not in cardiac arrest)
    LaunchedEffect(vitals.heartRate, vitals.spo2, isSoundMuted, vitals.rhythm) {
        if (!isSoundMuted && vitals.rhythm != EcgRhythm.ASYSTOLE && vitals.heartRate > 0) {
            val intervalMs = (60000L / vitals.heartRate.coerceIn(30, 200))
            while (true) {
                HospitalAudioSynthesizer.playHeartbeat(vitals.spo2)
                kotlinx.coroutines.delay(intervalMs)
            }
        }
    }

    // Animation progress for continuous sweep line (0f to 1f)
    val sweepTransition = rememberInfiniteTransition(label = "ecg_sweep")
    val sweepProgress by sweepTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 3200, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "sweep_progress"
    )

    // Visual flashing alarm border
    val alertAlpha by sweepTransition.animateFloat(
        initialValue = 0.2f,
        targetValue = 0.9f,
        animationSpec = infiniteRepeatable(
            animation = tween(400, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "alert_flash"
    )

    val borderColor = when {
        isCritical -> MedicalRed.copy(alpha = alertAlpha)
        isYellowWarning -> AlertOrange.copy(alpha = alertAlpha * 0.7f)
        else -> BorderSubtle
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(MonitorBg)
            .border(2.dp, borderColor, RoundedCornerShape(12.dp))
            .padding(10.dp)
    ) {
        // Top status bar of monitor: Patient status banner & Timer
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(10.dp)
                        .clip(RoundedCornerShape(50))
                        .background(if (isCritical) MedicalRed else if (isYellowWarning) AlertOrange else EcgGreen)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "MONITOR CẤP CỨU (LEAD II + PLETH)",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextSecondary,
                    letterSpacing = 1.sp
                )
            }

            // Emergency Countdown Timer
            val minutes = remainingSeconds / 60
            val seconds = remainingSeconds % 60
            val timerColor = if (remainingSeconds < 180) MedicalRed else AlertOrange
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .clip(RoundedCornerShape(6.dp))
                    .background(DarkSurfaceVariant)
                    .padding(horizontal = 8.dp, vertical = 2.dp)
            ) {
                Text(
                    text = "THỜI GIAN VÀNG: ",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = TextSecondary
                )
                Text(
                    text = String.format("%02d:%02d", minutes, seconds),
                    fontSize = 13.sp,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.ExtraBold,
                    color = timerColor
                )
            }
        }

        // Critical Alarm Banner if triggered
        if (isCritical) {
            Spacer(modifier = Modifier.height(4.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(4.dp))
                    .background(MedicalRed.copy(alpha = 0.9f))
                    .padding(horizontal = 8.dp, vertical = 3.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Icon(
                    imageVector = Icons.Default.NotificationsActive,
                    contentDescription = "Báo động",
                    tint = Color.White,
                    modifier = Modifier.size(14.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                val alertMsg = when {
                    vitals.rhythm == EcgRhythm.VFIB -> "CẢNH BÁO ĐỎ: RUNG THẤT (VF) - CẦN SỐC ĐIỆN VÀ ÉP TIM NGAY!"
                    vitals.rhythm == EcgRhythm.ASYSTOLE -> "CẢNH BÁO ĐỎ: VÔ TÂM THU (ASYSTOLE) - NGƯNG TUẦN HOÀN!"
                    vitals.spo2 < 80 -> "CẢNH BÁO ĐỎ: SUY HÔ HẤP THIẾU OXY NẶNG (SpO2 < 80%)"
                    vitals.bpSys < 70 -> "CẢNH BÁO ĐỎ: TRỤY MẠCH SỐC NẶNG (HA < 70 mmHg)"
                    else -> "CẢNH BÁO SINH TỒN NGUY KỊCH!"
                }
                Text(
                    text = alertMsg,
                    color = Color.White,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        // Main Waveforms (ECG green + SpO2 cyan)
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(115.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(Color(0xFF04070E))
                .border(1.dp, MonitorGrid, RoundedCornerShape(8.dp))
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val w = size.width
                val h = size.height

                // Draw background medical monitor grid (15x8 grid)
                val gridCols = 20
                val gridRows = 8
                val cellW = w / gridCols
                val cellH = h / gridRows
                for (i in 0..gridCols) {
                    drawLine(
                        color = Color(0x1838BDF8),
                        start = Offset(i * cellW, 0f),
                        end = Offset(i * cellW, h),
                        strokeWidth = 0.6f
                    )
                }
                for (j in 0..gridRows) {
                    drawLine(
                        color = Color(0x1838BDF8),
                        start = Offset(0f, j * cellH),
                        end = Offset(w, j * cellH),
                        strokeWidth = 0.6f
                    )
                }

                // Partition: Top 60% is ECG, Bottom 40% is SpO2 Pleth
                val ecgBaseY = h * 0.32f
                val ecgMaxAmp = h * 0.28f

                val plethBaseY = h * 0.80f
                val plethMaxAmp = h * 0.16f

                // Sweeping bar position
                val sweepX = w * sweepProgress
                val eraseWidth = 24.dp.toPx()

                // Draw ECG Waveform
                val ecgPath = Path()
                var startedEcg = false

                val stepPx = 2f
                var x = 0f
                while (x < w) {
                    // Erase gap right ahead of sweep bar
                    val isNearSweep = if (x >= sweepX && x <= sweepX + eraseWidth) {
                        true
                    } else if (sweepX + eraseWidth > w && x <= (sweepX + eraseWidth - w)) {
                        true
                    } else false

                    if (!isNearSweep) {
                        val sampleY = calculateEcgSample(
                            x = x,
                            totalWidth = w,
                            rhythm = vitals.rhythm,
                            hr = vitals.heartRate,
                            baseY = ecgBaseY,
                            amp = ecgMaxAmp
                        )
                        if (!startedEcg) {
                            ecgPath.moveTo(x, sampleY)
                            startedEcg = true
                        } else {
                            ecgPath.lineTo(x, sampleY)
                        }
                    } else {
                        startedEcg = false
                    }
                    x += stepPx
                }

                drawPath(
                    path = ecgPath,
                    color = EcgGreen,
                    style = Stroke(width = 2.2f, cap = StrokeCap.Round, join = StrokeJoin.Round)
                )

                // Draw SpO2 Plethysmograph Waveform (bottom lane)
                val plethPath = Path()
                var startedPleth = false
                x = 0f
                while (x < w) {
                    val isNearSweep = if (x >= sweepX && x <= sweepX + eraseWidth) {
                        true
                    } else if (sweepX + eraseWidth > w && x <= (sweepX + eraseWidth - w)) {
                        true
                    } else false

                    if (!isNearSweep) {
                        val sampleY = calculatePlethSample(
                            x = x,
                            totalWidth = w,
                            hr = vitals.heartRate,
                            spo2 = vitals.spo2,
                            baseY = plethBaseY,
                            amp = plethMaxAmp,
                            isCardiacArrest = vitals.rhythm == EcgRhythm.ASYSTOLE || vitals.rhythm == EcgRhythm.VFIB
                        )
                        if (!startedPleth) {
                            plethPath.moveTo(x, sampleY)
                            startedPleth = true
                        } else {
                            plethPath.lineTo(x, sampleY)
                        }
                    } else {
                        startedPleth = false
                    }
                    x += stepPx
                }

                drawPath(
                    path = plethPath,
                    color = Spo2Cyan,
                    style = Stroke(width = 2.0f, cap = StrokeCap.Round, join = StrokeJoin.Round)
                )

                // Draw Vertical Sweep Head Line
                drawLine(
                    color = Color.White.copy(alpha = 0.85f),
                    start = Offset(sweepX, 0f),
                    end = Offset(sweepX, h),
                    strokeWidth = 2.0f
                )
            }

            // Rhythm label overlay
            Text(
                text = "II  " + vitals.rhythm.title,
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold,
                color = EcgGreen.copy(alpha = 0.85f),
                modifier = Modifier
                    .align(Alignment.TopStart)
                    .padding(6.dp)
            )

            Text(
                text = "PLETH",
                fontSize = 9.sp,
                fontWeight = FontWeight.Bold,
                color = Spo2Cyan.copy(alpha = 0.85f),
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(start = 6.dp, bottom = 4.dp)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Numerical Parameter Cards (HR, NIBP, SpO2, RR, Temp, GCS)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            // HR (bpm)
            VitalCard(
                title = "HR",
                unit = "bpm",
                value = if (vitals.rhythm == EcgRhythm.ASYSTOLE) "0" else vitals.heartRate.toString(),
                valueColor = EcgGreen,
                isAlert = vitals.heartRate < 45 || vitals.heartRate > 125,
                modifier = Modifier.weight(1f)
            )

            // NIBP (Sys/Dia)
            VitalCard(
                title = "NIBP",
                unit = "mmHg",
                value = if (vitals.rhythm == EcgRhythm.ASYSTOLE) "--/--" else "${vitals.bpSys}/${vitals.bpDia}",
                valueColor = Color(0xFFF8FAFC),
                isAlert = vitals.bpSys < 85 || vitals.bpSys > 180,
                modifier = Modifier.weight(1.3f)
            )

            // SpO2 (%)
            VitalCard(
                title = "SpO2",
                unit = "%",
                value = if (vitals.rhythm == EcgRhythm.ASYSTOLE) "--" else "${vitals.spo2}",
                valueColor = Spo2Cyan,
                isAlert = vitals.spo2 < 90,
                modifier = Modifier.weight(1f)
            )

            // RR (/min)
            VitalCard(
                title = "RR",
                unit = "/min",
                value = "${vitals.respRate}",
                valueColor = RespYellow,
                isAlert = vitals.respRate < 10 || vitals.respRate > 30,
                modifier = Modifier.weight(0.9f)
            )

            // Temp (°C)
            VitalCard(
                title = "TEMP",
                unit = "°C",
                value = String.format("%.1f", vitals.temperature),
                valueColor = TempPurple,
                isAlert = vitals.temperature > 38.5f || vitals.temperature < 35.5f,
                modifier = Modifier.weight(1f)
            )

            // GCS (/15)
            VitalCard(
                title = "GCS",
                unit = "/15",
                value = "${vitals.gcs}",
                valueColor = AlertOrange,
                isAlert = vitals.gcs <= 8,
                modifier = Modifier.weight(0.9f)
            )
        }
    }
}

@Composable
private fun VitalCard(
    title: String,
    unit: String,
    value: String,
    valueColor: Color,
    isAlert: Boolean,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .clip(RoundedCornerShape(6.dp))
            .background(if (isAlert) MedicalRed.copy(alpha = 0.15f) else DarkSurfaceVariant)
            .border(
                1.dp,
                if (isAlert) MedicalRed else BorderSubtle.copy(alpha = 0.6f),
                RoundedCornerShape(6.dp)
            )
            .padding(horizontal = 4.dp, vertical = 4.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(verticalAlignment = Alignment.Bottom) {
            Text(
                text = title,
                fontSize = 9.sp,
                fontWeight = FontWeight.Bold,
                color = valueColor.copy(alpha = 0.9f)
            )
            Spacer(modifier = Modifier.width(2.dp))
            Text(
                text = unit,
                fontSize = 8.sp,
                color = TextSecondary
            )
        }
        Text(
            text = value,
            fontSize = 14.sp,
            fontFamily = FontFamily.Monospace,
            fontWeight = FontWeight.Black,
            color = if (isAlert) MedicalRedLight else valueColor,
            maxLines = 1
        )
    }
}

// Math generator for realistic clinical Lead II ECG waveform
private fun calculateEcgSample(
    x: Float,
    totalWidth: Float,
    rhythm: EcgRhythm,
    hr: Int,
    baseY: Float,
    amp: Float
): Float {
    if (rhythm == EcgRhythm.ASYSTOLE) {
        // Isoelectric flat line with micro baseline drift
        val drift = sin(x * 0.05f) * 1.5f + (sin(x * 0.2f) * 0.8f)
        return baseY + drift
    }

    if (rhythm == EcgRhythm.VFIB) {
        // Chaotic fibrillation waveform: superposition of random out-of-phase harmonics
        val wave1 = sin(x * 0.07f) * 0.5f
        val wave2 = sin(x * 0.14f + 1.2f) * 0.35f
        val wave3 = sin(x * 0.28f + 2.5f) * 0.25f
        return baseY + (wave1 + wave2 + wave3) * amp * 1.2f
    }

    // Normal Sinus or STEMI
    val safeHr = hr.coerceIn(40, 180)
    // Wave period width in pixels: higher HR = shorter wavelength
    val cycleWidth = (totalWidth / (safeHr / 25f)).coerceIn(80f, 260f)
    val phase = (x % cycleWidth) / cycleWidth // 0.0 to 1.0

    return when {
        // Isoelectric baseline before P wave
        phase < 0.12f -> baseY

        // P wave (atrial depolarization)
        phase in 0.12f..0.22f -> {
            val pNorm = (phase - 0.12f) / 0.10f
            val pWave = sin(pNorm * PI.toFloat()) * 0.18f * amp
            baseY - pWave
        }

        // PR segment
        phase in 0.22f..0.30f -> baseY

        // Q wave (septal depolarization dip)
        phase in 0.30f..0.33f -> {
            val qNorm = (phase - 0.30f) / 0.03f
            val qDip = sin(qNorm * PI.toFloat()) * 0.15f * amp
            baseY + qDip
        }

        // R wave (tall ventricular depolarization spike)
        phase in 0.33f..0.38f -> {
            val rNorm = (phase - 0.33f) / 0.05f
            val rSpike = sin(rNorm * PI.toFloat()) * 1.0f * amp
            baseY - rSpike
        }

        // S wave (deep negative deflection)
        phase in 0.38f..0.42f -> {
            val sNorm = (phase - 0.38f) / 0.04f
            val sDip = sin(sNorm * PI.toFloat()) * 0.32f * amp
            baseY + sDip
        }

        // ST Segment & T wave
        phase in 0.42f..0.72f -> {
            if (rhythm == EcgRhythm.STEMI) {
                // STEMI: Tombstone ST elevation (> 3mm) merging directly into tall T wave!
                val stNorm = (phase - 0.42f) / 0.30f
                val elevatedArch = sin(stNorm * PI.toFloat()) * 0.55f * amp
                baseY - elevatedArch
            } else {
                // Normal Sinus: flat ST segment followed by smooth T wave
                if (phase < 0.48f) {
                    baseY
                } else {
                    val tNorm = (phase - 0.48f) / 0.24f
                    val tWave = sin(tNorm * PI.toFloat()) * 0.28f * amp
                    baseY - tWave
                }
            }
        }

        // TP segment baseline before next cycle
        else -> baseY
    }
}

// Math generator for SpO2 Plethysmograph waveform (Dicrotic notch & pulse wave)
private fun calculatePlethSample(
    x: Float,
    totalWidth: Float,
    hr: Int,
    spo2: Int,
    baseY: Float,
    amp: Float,
    isCardiacArrest: Boolean
): Float {
    if (isCardiacArrest) {
        return baseY + sin(x * 0.03f) * 0.8f
    }
    val safeHr = hr.coerceIn(40, 180)
    val cycleWidth = (totalWidth / (safeHr / 25f)).coerceIn(80f, 260f)
    val phase = (x % cycleWidth) / cycleWidth

    val scale = (spo2 / 100f).coerceIn(0.4f, 1.0f)

    return when {
        // Systolic upstroke (rapid ejection)
        phase in 0.0f..0.22f -> {
            val pNorm = phase / 0.22f
            val rise = sin(pNorm * (PI / 2).toFloat()) * amp * scale
            baseY - rise
        }
        // Systolic decay and dicrotic notch
        phase in 0.22f..0.45f -> {
            val pNorm = (phase - 0.22f) / 0.23f
            val fall = (1f - pNorm) * amp * scale
            // small rebound (dicrotic wave from aortic valve closure)
            val notch = if (pNorm > 0.4f && pNorm < 0.8f) {
                sin((pNorm - 0.4f) / 0.4f * PI.toFloat()) * 0.2f * amp * scale
            } else 0f
            baseY - fall - notch
        }
        // Diastolic runoff
        else -> {
            val pNorm = (phase - 0.45f) / 0.55f
            val runoff = (1f - pNorm) * 0.15f * amp * scale
            baseY - runoff
        }
    }
}
