package com.example.ui.screens

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.SavedShiftData
import com.example.model.PatientCase
import com.example.ui.components.EmsAppLogo
import com.example.ui.components.HospitalLogo
import com.example.ui.theme.*

/**
 * Phong nền mở đầu theo phong cách "Khoa Cấp Cứu_20261006_093905_0000.png":
 * Không gian sảnh cấp cứu hiện đại, biển hiệu LED KHOA CẤP CỨU rực sáng,
 * ánh đèn trần và cửa tự động chuyên nghiệp.
 */
@Composable
fun EmergencyDepartmentBackground(
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "er_bg_ambient")
    val beaconPulse by infiniteTransition.animateFloat(
        initialValue = 0.5f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "beacon_pulse"
    )

    Canvas(modifier = modifier.fillMaxSize()) {
        val w = size.width
        val h = size.height

        // 1. Phông nền chiều sâu: Tông xanh thẫm bệnh viện hiện đại (Clinical Dark Slate)
        drawRect(
            brush = Brush.verticalGradient(
                colors = listOf(
                    Color(0xFF020617), // Trần nhà đêm
                    Color(0xFF0B192C), // Khu vực hành lang
                    Color(0xFF0F2038), // Sảnh khoa cấp cứu
                    Color(0xFF020B18)  // Sàn gạch phản chiếu
                ),
                startY = 0f,
                endY = h
            )
        )

        // 2. Phối cảnh trần hành lang và đèn LED dài cao cấp (Perspective Corridor Lights)
        val ceilingVanishX = w * 0.5f
        val ceilingVanishY = h * 0.18f

        // Đèn trần thanh dài 2 bên (Fluorescent ceiling strips)
        for (i in 0..4) {
            val progress = (i + 1) / 5f
            val stripY = ceilingVanishY + progress * (h * 0.22f)
            val stripLeft = ceilingVanishX - progress * (w * 0.46f)
            val stripRight = ceilingVanishX + progress * (w * 0.46f)
            val stripWidth = 40.dp.toPx() * progress

            // Đèn trái
            drawLine(
                color = Color(0xFFE2E8F0).copy(alpha = 0.25f + progress * 0.2f),
                start = Offset(stripLeft, stripY),
                end = Offset(stripLeft + stripWidth, stripY),
                strokeWidth = 3.dp.toPx() * progress,
                cap = StrokeCap.Round
            )
            // Đèn phải
            drawLine(
                color = Color(0xFFE2E8F0).copy(alpha = 0.25f + progress * 0.2f),
                start = Offset(stripRight - stripWidth, stripY),
                end = Offset(stripRight, stripY),
                strokeWidth = 3.dp.toPx() * progress,
                cap = StrokeCap.Round
            )
        }

        // 3. Khung cửa kính trượt tự động Khoa Cấp Cứu ở trung tâm phía sau
        val doorTop = h * 0.18f
        val doorHeight = h * 0.38f
        val doorWidth = w * 0.76f
        val doorLeft = (w - doorWidth) / 2f

        // Vòm cửa kim loại
        drawRoundRect(
            color = Color(0xFF1E293B),
            topLeft = Offset(doorLeft, doorTop),
            size = Size(doorWidth, doorHeight),
            cornerRadius = androidx.compose.ui.geometry.CornerRadius(12.dp.toPx(), 12.dp.toPx()),
            style = Stroke(width = 3.dp.toPx())
        )

        // Cửa kính trượt kép (hai cánh kính xanh thẫm)
        drawRect(
            brush = Brush.verticalGradient(
                listOf(Color(0x3338BDF8), Color(0x110284C7))
            ),
            topLeft = Offset(doorLeft + 4.dp.toPx(), doorTop + 4.dp.toPx()),
            size = Size(doorWidth - 8.dp.toPx(), doorHeight - 8.dp.toPx())
        )

        // Khe cửa đôi trung tâm
        drawLine(
            color = Color(0x6694A3B8),
            start = Offset(w * 0.5f, doorTop),
            end = Offset(w * 0.5f, doorTop + doorHeight),
            strokeWidth = 2.dp.toPx()
        )

        // Tay vịn cửa / cảm biến quang học
        drawLine(
            color = Color(0xAA38BDF8),
            start = Offset(w * 0.47f, doorTop + doorHeight * 0.4f),
            end = Offset(w * 0.47f, doorTop + doorHeight * 0.65f),
            strokeWidth = 3.dp.toPx(),
            cap = StrokeCap.Round
        )
        drawLine(
            color = Color(0xAA38BDF8),
            start = Offset(w * 0.53f, doorTop + doorHeight * 0.4f),
            end = Offset(w * 0.53f, doorTop + doorHeight * 0.65f),
            strokeWidth = 3.dp.toPx(),
            cap = StrokeCap.Round
        )

        // 4. Biển hiệu sáng "EMERGENCY / KHOA CẤP CỨU" trên đỉnh cửa
        val signTop = doorTop - 24.dp.toPx()
        val signHeight = 20.dp.toPx()
        val signWidth = doorWidth * 0.85f
        val signLeft = (w - signWidth) / 2f

        // Khung hộp đèn đỏ cấp cứu
        drawRoundRect(
            color = Color(0xEE991B1B),
            topLeft = Offset(signLeft, signTop),
            size = Size(signWidth, signHeight),
            cornerRadius = androidx.compose.ui.geometry.CornerRadius(4.dp.toPx(), 4.dp.toPx())
        )
        drawRoundRect(
            color = Color(0xFFEF4444).copy(alpha = beaconPulse),
            topLeft = Offset(signLeft, signTop),
            size = Size(signWidth, signHeight),
            cornerRadius = androidx.compose.ui.geometry.CornerRadius(4.dp.toPx(), 4.dp.toPx()),
            style = Stroke(width = 2.dp.toPx())
        )

        // Ánh hào quang đèn tín hiệu khẩn cấp tỏa ra xung quanh
        drawCircle(
            brush = Brush.radialGradient(
                colors = listOf(
                    Color(0x55EF4444).copy(alpha = beaconPulse * 0.6f),
                    Color(0x00EF4444)
                ),
                center = Offset(w * 0.5f, signTop + signHeight / 2f),
                radius = w * 0.45f
            ),
            radius = w * 0.45f,
            center = Offset(w * 0.5f, signTop + signHeight / 2f)
        )

        // 5. Đường kẻ phân làn xe cáng / chỉ dẫn sàn nhà (Floor Guide Lines)
        // Vạch đỏ cấp cứu (Priority Red Lane) dẫn thẳng vào cửa
        val floorStart = doorTop + doorHeight
        val floorEnd = h

        val leftLaneStart = w * 0.5f - doorWidth * 0.35f
        val rightLaneStart = w * 0.5f + doorWidth * 0.35f

        // Vạch đỏ trung tâm
        drawLine(
            brush = Brush.verticalGradient(
                listOf(Color(0xCCEF4444), Color(0x44EF4444))
            ),
            start = Offset(w * 0.5f, floorStart),
            end = Offset(w * 0.5f, floorEnd),
            strokeWidth = 3.dp.toPx()
        )

        // Làn dẫn hướng 2 bên
        drawLine(
            brush = Brush.verticalGradient(
                listOf(Color(0x6638BDF8), Color(0x1138BDF8))
            ),
            start = Offset(leftLaneStart, floorStart),
            end = Offset(0f, floorEnd * 0.85f),
            strokeWidth = 2.dp.toPx()
        )
        drawLine(
            brush = Brush.verticalGradient(
                listOf(Color(0x6638BDF8), Color(0x1138BDF8))
            ),
            start = Offset(rightLaneStart, floorStart),
            end = Offset(w, floorEnd * 0.85f),
            strokeWidth = 2.dp.toPx()
        )

        // 6. Hiệu ứng sương mờ / ánh sáng khử khuẩn phòng cấp cứu (Atmospheric Vignette)
        drawRect(
            brush = Brush.radialGradient(
                colors = listOf(Color.Transparent, Color(0x99020617)),
                center = Offset(w * 0.5f, h * 0.45f),
                radius = w * 0.8f
            )
        )
    }
}

@Composable
fun StartScreen(
    savedShift: SavedShiftData?,
    patientsTreated: Int,
    patientsSaved: Int,
    reputationScore: Int,
    isSoundMuted: Boolean,
    availableCases: List<PatientCase> = emptyList(),
    onToggleSound: () -> Unit,
    onStartNewShift: () -> Unit,
    onResumeShift: () -> Unit,
    onRestartCurrentCase: () -> Unit,
    onSelectCase: (String) -> Unit = {}
) {
    var showCaseCatalogDialog by remember { mutableStateOf(false) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground)
    ) {
        // PHÔNG NỀN KHOA CẤP CỨU (THEO PHONG CÁCH HÌNH KHOA CẤP CỨU_20261006_093905_0000.png)
        EmergencyDepartmentBackground(
            modifier = Modifier.fillMaxSize()
        )

        // Sound toggle icon in top-right corner
        IconButton(
            onClick = onToggleSound,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(top = 40.dp, end = 16.dp)
                .testTag("btn_toggle_sound")
        ) {
            Icon(
                imageVector = if (isSoundMuted) Icons.Default.VolumeOff else Icons.Default.VolumeUp,
                contentDescription = "Âm thanh",
                tint = if (isSoundMuted) TextSecondary else Color(0xFFFEF08A)
            )
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Biểu tượng EMS NPT Med (Logo của App từ ảnh EMS_20261006_092816_0000.png)
            EmsAppLogo(
                size = 110.dp,
                showText = true,
                onClick = {
                    if (savedShift != null) onResumeShift() else onStartNewShift()
                }
            )

            Spacer(modifier = Modifier.height(18.dp))

            // Bảng hiệu Khoa Cấp Cứu rực rỡ
            Surface(
                shape = RoundedCornerShape(8.dp),
                color = Color(0xDDDC2626),
                border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFFCA5A5)),
                modifier = Modifier.padding(horizontal = 12.dp)
            ) {
                Text(
                    text = "KHOA CẤP CỨU • EMERGENCY DEPT",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Black,
                    color = Color.White,
                    letterSpacing = 1.5.sp,
                    modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Main Title
            Text(
                text = "TRUNG TÂM CẤP CỨU",
                fontSize = 28.sp,
                fontWeight = FontWeight.Black,
                color = Color.White,
                letterSpacing = 1.sp,
                textAlign = TextAlign.Center
            )

            Text(
                text = "HỆ THỐNG MÔ PHỎNG LÂM SÀNG CẤP CỨU",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = MedicalPrimaryLight,
                letterSpacing = 1.sp
            )

            Spacer(modifier = Modifier.height(28.dp))

            // RESUME SAVED SHIFT CARD (IF ACTIVE)
            if (savedShift != null) {
                Card(
                    modifier = Modifier.fillMaxWidth().testTag("card_saved_shift"),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = DarkSurface.copy(alpha = 0.92f)),
                    border = androidx.compose.foundation.BorderStroke(1.5.dp, MedicalPrimary)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(8.dp)
                                        .clip(RoundedCornerShape(50))
                                        .background(MedicalPrimary)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "CA ĐANG XỬ TRÍ DỞ",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MedicalPrimaryLight
                                )
                            }
                            Text(
                                text = "BƯỚC ${savedShift.stepIndex}/6",
                                fontSize = 11.sp,
                                fontFamily = FontFamily.Monospace,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // NÚT XANH: TIẾP TỤC XỬ TRÍ CA NÀY
                        Button(
                            onClick = onResumeShift,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp)
                                .testTag("btn_resume_shift"),
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MedicalPrimary,
                                contentColor = Color.White
                            )
                        ) {
                            Icon(Icons.Default.PlayArrow, contentDescription = null)
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "TIẾP TỤC XỬ TRÍ CA NÀY",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        // NÚT MÀU XANH LÁ "BẮT ĐẦU LẠI"
                        Button(
                            onClick = onRestartCurrentCase,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp)
                                .testTag("btn_restart_case"),
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MedicalGreen,
                                contentColor = Color.White
                            )
                        ) {
                            Icon(Icons.Default.Refresh, contentDescription = null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "BẮT ĐẦU LẠI",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            TextButton(
                                onClick = { showCaseCatalogDialog = true },
                                colors = ButtonDefaults.textButtonColors(contentColor = MedicalPrimaryLight)
                            ) {
                                Icon(Icons.Default.ListAlt, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("Đổi ca bệnh khác", fontSize = 12.sp)
                            }

                            TextButton(
                                onClick = onStartNewShift,
                                colors = ButtonDefaults.textButtonColors(contentColor = TextSecondary)
                            ) {
                                Text("Vào ca ngẫu nhiên", fontSize = 12.sp)
                            }
                        }
                    }
                }
            } else {
                // PRIMARY GAME START BUTTON
                Button(
                    onClick = onStartNewShift,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(58.dp)
                        .testTag("btn_start_new_shift"),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MedicalPrimary,
                        contentColor = Color.White
                    ),
                    elevation = ButtonDefaults.buttonElevation(defaultElevation = 6.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.MedicalServices,
                        contentDescription = null,
                        modifier = Modifier.size(24.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "VÀO CA TRỰC CẤP CỨU",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Black,
                        letterSpacing = 1.sp
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                // BUTTON TO BROWSE & PICK CASES
                OutlinedButton(
                    onClick = { showCaseCatalogDialog = true },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .testTag("btn_select_case_catalog"),
                    shape = RoundedCornerShape(12.dp),
                    border = androidx.compose.foundation.BorderStroke(1.2.dp, MedicalPrimaryLight.copy(alpha = 0.8f)),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = MedicalPrimaryLight)
                ) {
                    Icon(Icons.Default.MenuBook, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "DANH SÁCH BỆNH ÁN (13 CA)",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // COMPACT DISCLAIMER
            Text(
                text = "Mô phỏng ngẫu nhiên theo phác đồ Bộ Y Tế • Không dùng thông tin bệnh án thật",
                fontSize = 10.sp,
                color = TextMuted,
                textAlign = TextAlign.Center
            )
        }

        // CASE CATALOG DIALOG (SHOWING ALL 13 CASES)
        if (showCaseCatalogDialog) {
            Dialog(onDismissRequest = { showCaseCatalogDialog = false }) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .fillMaxHeight(0.85f),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = DarkSurface),
                    border = androidx.compose.foundation.BorderStroke(1.dp, MedicalPrimaryLight)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = "DANH SÁCH BỆNH ÁN",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Black,
                                    color = Color.White
                                )
                                Text(
                                    text = "13 Ca bệnh: Cấp cứu tối khẩn & Nội khoa thông thường",
                                    fontSize = 11.sp,
                                    color = TextSecondary
                                )
                            }
                            IconButton(onClick = { showCaseCatalogDialog = false }) {
                                Icon(Icons.Default.Close, contentDescription = "Đóng", tint = TextSecondary)
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        LazyColumn(
                            modifier = Modifier.weight(1f),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(availableCases) { c ->
                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable {
                                            showCaseCatalogDialog = false
                                            onSelectCase(c.id)
                                        },
                                    shape = RoundedCornerShape(10.dp),
                                    colors = CardDefaults.cardColors(containerColor = DarkSurfaceVariant),
                                    border = androidx.compose.foundation.BorderStroke(1.dp, BorderSubtle)
                                ) {
                                    Row(
                                        modifier = Modifier.padding(12.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Box(
                                            modifier = Modifier
                                                .clip(RoundedCornerShape(6.dp))
                                                .background(Color(c.triageLevel.badgeColor))
                                                .padding(horizontal = 6.dp, vertical = 3.dp)
                                        ) {
                                            Text(
                                                text = c.triageLevel.name,
                                                fontSize = 9.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = Color.White
                                            )
                                        }

                                        Spacer(modifier = Modifier.width(10.dp))

                                        Column(modifier = Modifier.weight(1f)) {
                                            Text(
                                                text = "${c.patientName} (${c.age}t) - ${c.chiefComplaint}",
                                                fontSize = 13.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = TextPrimary
                                            )
                                            Text(
                                                text = "${c.occupation}, ${c.gender} • Giờ vào: ${c.arrivalTime} • ${c.chiefComplaint}",
                                                fontSize = 11.sp,
                                                color = TextSecondary,
                                                maxLines = 2
                                            )
                                        }

                                        Icon(
                                            imageVector = Icons.Default.ChevronRight,
                                            contentDescription = null,
                                            tint = MedicalPrimaryLight
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
