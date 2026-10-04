package com.example.ui.steps

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Emergency
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.PatientCase
import com.example.ui.theme.*

@Composable
fun ProtocolTriageStep(
    patientCase: PatientCase,
    checkedAbcde: Set<String>,
    onToggleAbcde: (String) -> Unit,
    onProceedToNextStep: () -> Unit,
    modifier: Modifier = Modifier
) {
    val allChecked = setOf("A", "B", "C", "D", "E").all { it in checkedAbcde }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(14.dp)
    ) {
        // Triage Identification Banner
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = DarkSurface),
            border = androidx.compose.foundation.BorderStroke(1.dp, BorderSubtle)
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = patientCase.patientName.uppercase(),
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Black,
                            color = Color.White
                        )
                        Text(
                            text = "${patientCase.age} tuổi • ${patientCase.gender} • ${patientCase.occupation}",
                            fontSize = 12.sp,
                            color = TextSecondary
                        )
                    }

                    // Triage Level Badge
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(Color(patientCase.triageLevel.badgeColor))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = patientCase.triageLevel.label,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Black,
                            color = Color.White
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))
                HorizontalDivider(color = BorderSubtle, thickness = 0.8.dp)
                Spacer(modifier = Modifier.height(10.dp))

                Row(verticalAlignment = Alignment.Top) {
                    Icon(
                        imageVector = Icons.Default.Emergency,
                        contentDescription = null,
                        tint = MedicalRedLight,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Column {
                        Text(
                            text = "LÝ DO VÀO VIỆN (CHIEF COMPLAINT):",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = MedicalRedLight
                        )
                        Text(
                            text = patientCase.chiefComplaint,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.White
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Section Title: ABCDE Checklist
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "BƯỚC 1: ĐÁNH GIÁ BAN ĐẦU - QUY TRÌNH ABCDE",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Black,
                    color = MedicalRedLight
                )
                Text(
                    text = "Phải khám và tích đủ cả 5 mục A-B-C-D-E để mở khóa bước tiếp theo",
                    fontSize = 11.sp,
                    color = TextSecondary
                )
            }
            Text(
                text = "${checkedAbcde.size}/5",
                fontSize = 14.sp,
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Black,
                color = if (allChecked) EcgGreen else AlertOrange
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // A - Airway
        AbcdeCard(
            letter = "A",
            title = "AIRWAY - ĐƯỜNG THỞ",
            subtitle = "Kiểm tra sự thông thoáng, đờm dãi, phù nề thanh môn, dị vật",
            findingDesc = patientCase.abcde.airwayDesc,
            isNormal = patientCase.abcde.airwayIsClear,
            isChecked = "A" in checkedAbcde,
            onToggle = { onToggleAbcde("A") }
        )

        Spacer(modifier = Modifier.height(8.dp))

        // B - Breathing
        AbcdeCard(
            letter = "B",
            title = "BREATHING - HÔ HẤP",
            subtitle = "Tần số thở, di động lồng ngực, co kéo cơ hô hấp phụ, nghe rì rào phế nang",
            findingDesc = patientCase.abcde.breathingDesc,
            isNormal = patientCase.abcde.breathingIsNormal,
            isChecked = "B" in checkedAbcde,
            onToggle = { onToggleAbcde("B") }
        )

        Spacer(modifier = Modifier.height(8.dp))

        // C - Circulation
        AbcdeCard(
            letter = "C",
            title = "CIRCULATION - TUẦN HOÀN",
            subtitle = "Mạch ngoại vi, thời gian hồi phục mao mạch CRT, huyết áp, tĩnh mạch cổ",
            findingDesc = patientCase.abcde.circulationDesc,
            isNormal = patientCase.abcde.circulationIsNormal,
            isChecked = "C" in checkedAbcde,
            onToggle = { onToggleAbcde("C") }
        )

        Spacer(modifier = Modifier.height(8.dp))

        // D - Disability
        AbcdeCard(
            letter = "D",
            title = "DISABILITY - THẦN KINH",
            subtitle = "Tri giác, thang điểm Glasgow (GCS), kích thước và phản xạ đồng tử",
            findingDesc = patientCase.abcde.disabilityDesc,
            isNormal = patientCase.abcde.disabilityIsNormal,
            isChecked = "D" in checkedAbcde,
            onToggle = { onToggleAbcde("D") }
        )

        Spacer(modifier = Modifier.height(8.dp))

        // E - Exposure
        AbcdeCard(
            letter = "E",
            title = "EXPOSURE - BỘC LỘ TOÀN THÂN",
            subtitle = "Nhiệt độ cơ thể, tìm kiếm ban xuất huyết, vết mổ, chấn thương thành ngực/bụng",
            findingDesc = patientCase.abcde.exposureDesc,
            isNormal = patientCase.abcde.exposureIsNormal,
            isChecked = "E" in checkedAbcde,
            onToggle = { onToggleAbcde("E") }
        )

        Spacer(modifier = Modifier.height(18.dp))

        // Proceed Button
        Button(
            onClick = onProceedToNextStep,
            enabled = allChecked,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MedicalRed,
                disabledContainerColor = DarkSurfaceVariant,
                contentColor = Color.White,
                disabledContentColor = TextSecondary
            )
        ) {
            Text(
                text = if (allChecked) "HOÀN TẤT ABCDE -> CHUYỂN BƯỚC 2: HỎI BỆNH & KHÁM LÂM SÀNG" else "CẦN TÍCH ĐỦ CẢ 5 MỤC ABCDE ĐỂ TIẾP TỤC",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold
            )
            if (allChecked) {
                Spacer(modifier = Modifier.width(6.dp))
                Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null, modifier = Modifier.size(18.dp))
            }
        }
    }
}

@Composable
private fun AbcdeCard(
    letter: String,
    title: String,
    subtitle: String,
    findingDesc: String,
    isNormal: Boolean,
    isChecked: Boolean,
    onToggle: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onToggle() },
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isChecked) DarkSurfaceVariant else DarkSurface
        ),
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            if (isChecked) if (!isNormal) MedicalRed else EcgGreen else BorderSubtle
        )
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(28.dp)
                            .clip(RoundedCornerShape(6.dp))
                            .background(if (isChecked) if (!isNormal) MedicalRed else EcgGreen else BorderSubtle),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = letter,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Black,
                            color = Color.White
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = title,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = TextPrimary
                        )
                        Text(
                            text = subtitle,
                            fontSize = 10.sp,
                            color = TextSecondary,
                            maxLines = 1
                        )
                    }
                }

                // Check indicator
                Checkbox(
                    checked = isChecked,
                    onCheckedChange = { onToggle() },
                    colors = CheckboxDefaults.colors(
                        checkedColor = if (!isNormal) MedicalRed else EcgGreen,
                        uncheckedColor = BorderSubtle
                    )
                )
            }

            // If checked, reveal patient specific clinical findings
            if (isChecked) {
                Spacer(modifier = Modifier.height(8.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(6.dp))
                        .background(if (!isNormal) MedicalRed.copy(alpha = 0.12f) else EcgGreen.copy(alpha = 0.1f))
                        .border(
                            1.dp,
                            if (!isNormal) MedicalRed.copy(alpha = 0.4f) else EcgGreen.copy(alpha = 0.3f),
                            RoundedCornerShape(6.dp)
                        )
                        .padding(8.dp)
                ) {
                    Row(verticalAlignment = Alignment.Top) {
                        Icon(
                            imageVector = if (!isNormal) Icons.Default.Warning else Icons.Default.CheckCircle,
                            contentDescription = null,
                            tint = if (!isNormal) MedicalRedLight else EcgGreen,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Column {
                            Text(
                                text = if (!isNormal) "GHI NHẬN BẤT THƯỜNG CẤP CỨU:" else "GHI NHẬN BÌNH THƯỜNG:",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (!isNormal) MedicalRedLight else EcgGreen
                            )
                            Text(
                                text = findingDesc,
                                fontSize = 11.sp,
                                color = TextPrimary,
                                lineHeight = 15.sp
                            )
                        }
                    }
                }
            }
        }
    }
}
