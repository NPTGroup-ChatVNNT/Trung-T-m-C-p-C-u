package com.example.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.EcgRhythm
import com.example.model.PatientCase
import com.example.model.VitalSigns
import com.example.ui.theme.*

@Composable
fun TraumaBayBedCard(
    patientCase: PatientCase,
    vitals: VitalSigns,
    stability: Int,
    nurseCallout: String,
    modifier: Modifier = Modifier
) {
    val isArrest = vitals.rhythm == EcgRhythm.ASYSTOLE || vitals.rhythm == EcgRhythm.VFIB || stability <= 15
    val isCritical = stability < 40 || vitals.spo2 < 85

    val stabilityColor = when {
        stability >= 70 -> EcgGreen
        stability >= 40 -> AlertOrange
        else -> MedicalRedLight
    }

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF0F172A)),
        border = androidx.compose.foundation.BorderStroke(1.dp, if (isArrest) MedicalRed else BorderSubtle)
    ) {
        Column(modifier = Modifier.padding(10.dp)) {
            // Row 1: Patient Avatar, Info & Stability Health Bar
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // 2D Patient Bed Avatar
                Box(
                    modifier = Modifier
                        .size(46.dp)
                        .clip(CircleShape)
                        .background(if (isArrest) Color(0xFF450A0A) else if (isCritical) Color(0xFF1E293B) else Color(0xFF134E4A))
                        .border(2.dp, stabilityColor, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Canvas(modifier = Modifier.size(34.dp)) {
                        val w = size.width
                        val h = size.height

                        // Skin tone varies with hypoxia
                        val skinColor = when {
                            isArrest -> Color(0xFF94A3B8) // Ashen pale
                            vitals.spo2 < 80 -> Color(0xFF93C5FD) // Cyanotic blue
                            isCritical -> Color(0xFFFED7AA) // Pale sweat
                            else -> Color(0xFFFDE68A) // Normal healthy
                        }

                        // Face
                        drawCircle(color = skinColor, radius = w * 0.42f, center = Offset(w * 0.5f, h * 0.5f))

                        // Hair
                        drawArc(
                            color = Color(0xFF1E293B),
                            startAngle = 180f,
                            sweepAngle = 180f,
                            useCenter = true,
                            topLeft = Offset(w * 0.08f, h * 0.08f),
                            size = androidx.compose.ui.geometry.Size(w * 0.84f, h * 0.7f)
                        )

                        // Eyes: Closed if arrest, squinting in pain if critical, open if stable
                        if (isArrest) {
                            drawLine(Color(0xFF334155), Offset(w * 0.32f, h * 0.48f), Offset(w * 0.44f, h * 0.48f), strokeWidth = 2.5f)
                            drawLine(Color(0xFF334155), Offset(w * 0.56f, h * 0.48f), Offset(w * 0.68f, h * 0.48f), strokeWidth = 2.5f)
                        } else if (isCritical) {
                            // Pain expression
                            drawLine(Color(0xFF0F172A), Offset(w * 0.32f, h * 0.45f), Offset(w * 0.44f, h * 0.50f), strokeWidth = 2.5f)
                            drawLine(Color(0xFF0F172A), Offset(w * 0.68f, h * 0.45f), Offset(w * 0.56f, h * 0.50f), strokeWidth = 2.5f)
                        } else {
                            // Awake
                            drawCircle(Color(0xFF0F172A), radius = 2.5f, center = Offset(w * 0.38f, h * 0.48f))
                            drawCircle(Color(0xFF0F172A), radius = 2.5f, center = Offset(w * 0.62f, h * 0.48f))
                        }

                        // Oxygen mask or cannula indicator if on respiratory support
                        if (vitals.spo2 >= 90) {
                            drawCircle(Color(0x8838BDF8), radius = w * 0.18f, center = Offset(w * 0.5f, h * 0.68f))
                        }
                    }
                }

                Spacer(modifier = Modifier.width(10.dp))

                // Patient Details & Stability Gauge
                Column(modifier = Modifier.weight(1f)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = patientCase.patientName.uppercase(),
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Black,
                            color = Color.White
                        )
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(4.dp))
                                .background(Color(patientCase.triageLevel.badgeColor))
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = patientCase.triageLevel.name,
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(4.dp))

                    // RPG Stability Gauge (Health Bar)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Favorite,
                                contentDescription = null,
                                tint = stabilityColor,
                                modifier = Modifier.size(12.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "CHỈ SỐ SINH TỒN:",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextSecondary
                            )
                        }
                        Text(
                            text = "$stability%",
                            fontSize = 11.sp,
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Black,
                            color = stabilityColor
                        )
                    }

                    Spacer(modifier = Modifier.height(2.dp))

                    // Animated Progress Bar
                    val animatedStability by animateFloatAsState(
                        targetValue = stability / 100f,
                        animationSpec = tween(400, easing = FastOutSlowInEasing),
                        label = "stability_progress"
                    )

                    LinearProgressIndicator(
                        progress = { animatedStability },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(6.dp)
                            .clip(RoundedCornerShape(3.dp)),
                        color = stabilityColor,
                        trackColor = DarkSurfaceVariant
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Row 2: Live Nurse Speech Bubble Dialog
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color(0xFF091224))
                    .border(1.dp, if (isArrest || isCritical) MedicalRed.copy(alpha = 0.5f) else ScrubTeal.copy(alpha = 0.4f), RoundedCornerShape(8.dp))
                    .padding(horizontal = 10.dp, vertical = 6.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = if (isArrest) Icons.Default.Warning else Icons.Default.Campaign,
                        contentDescription = null,
                        tint = if (isArrest) MedicalRedLight else Spo2Cyan,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = nurseCallout,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium,
                        color = if (isArrest) MedicalRedLight else TextPrimary,
                        lineHeight = 15.sp
                    )
                }
            }
        }
    }
}
