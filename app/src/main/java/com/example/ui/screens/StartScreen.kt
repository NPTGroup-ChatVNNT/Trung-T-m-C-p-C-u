package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.SavedShiftData
import com.example.model.PatientCase
import com.example.ui.components.HospitalLogo
import com.example.ui.theme.*

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
            // Refined Hospital Logo - Clickable for fast action & pulse
            HospitalLogo(
                size = 92.dp,
                onClick = {
                    if (savedShift != null) onResumeShift() else onStartNewShift()
                }
            )

            Spacer(modifier = Modifier.height(18.dp))

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
                text = "EMERGENCY DOCTOR RPG",
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                color = MedicalPrimaryLight,
                letterSpacing = 2.sp
            )

            Spacer(modifier = Modifier.height(28.dp))

            // RESUME SAVED SHIFT CARD (IF ACTIVE)
            if (savedShift != null) {
                Card(
                    modifier = Modifier.fillMaxWidth().testTag("card_saved_shift"),
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = DarkSurface),
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
                                text = "BƯỚC ${savedShift.stepIndex}/5",
                                fontSize = 11.sp,
                                fontFamily = FontFamily.Monospace,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // NÚT XANH TEAL: TIẾP TỤC XỬ TRÍ CA NÀY
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

                        // NÚT MÀU XANH LÁ "BẮT ĐẦU LẠI" (YÊU CẦU 1)
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
                        text = "VÀO CA TRỰC",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Black,
                        letterSpacing = 1.sp
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                // BUTTON TO BROWSE & PICK FROM ALL 13 MEDICAL & EMERGENCY CASES
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

            Spacer(modifier = Modifier.height(28.dp))

            // SLEEK 3-STAT CAREER HUD
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                QuickStatCard(
                    title = "Ca tiếp nhận",
                    value = "$patientsTreated",
                    icon = Icons.Default.Person,
                    color = Spo2Cyan,
                    modifier = Modifier.weight(1f)
                )
                QuickStatCard(
                    title = "Cứu sống",
                    value = "$patientsSaved",
                    icon = Icons.Default.Favorite,
                    color = EcgGreen,
                    modifier = Modifier.weight(1f)
                )
                QuickStatCard(
                    title = "Điểm uy tín",
                    value = "$reputationScore",
                    icon = Icons.Default.Star,
                    color = Color(0xFFFEF08A),
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // COMPACT DISCLAIMER
            Text(
                text = "Mô phỏng ngẫu nhiên theo phác đồ Bộ Y Tế • Không dùng thông tin bệnh án thật",
                fontSize = 10.sp,
                color = TextMuted,
                textAlign = TextAlign.Center
            )
        }

        // CASE CATALOG DIALOG (SHOWING ALL 13 CASES: CRITICAL EMERGENCY & INTERNAL MEDICINE)
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

@Composable
private fun QuickStatCard(
    title: String,
    value: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    color: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(containerColor = DarkSurface),
        border = androidx.compose.foundation.BorderStroke(1.dp, BorderSubtle.copy(alpha = 0.5f))
    ) {
        Column(
            modifier = Modifier.padding(vertical = 10.dp, horizontal = 6.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = color,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = value,
                fontSize = 17.sp,
                fontWeight = FontWeight.Black,
                color = Color.White
            )
            Text(
                text = title,
                fontSize = 9.sp,
                color = TextSecondary,
                maxLines = 1
            )
        }
    }
}
