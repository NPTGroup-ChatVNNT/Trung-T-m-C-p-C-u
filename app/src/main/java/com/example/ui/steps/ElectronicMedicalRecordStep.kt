package com.example.ui.steps

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AssignmentTurnedIn
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.LocalHospital
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
import com.example.model.OrderCategory
import com.example.model.PatientCase
import com.example.ui.theme.*
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun ElectronicMedicalRecordStep(
    patientCase: PatientCase,
    primaryDx: String,
    examinedSystems: Set<String>,
    orderedLabIds: Set<String>,
    executedOrderIds: Set<String>,
    onProceedToDebrief: () -> Unit,
    modifier: Modifier = Modifier
) {
    var isSigned by remember { mutableStateOf(false) }
    val currentDate = remember { SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(Date()) }

    // Auto-generate clinical summary
    val initialSummary = remember(patientCase, primaryDx) {
        "Bệnh nhân ${patientCase.patientName}, ${patientCase.age} tuổi, ${patientCase.gender}, nhập viện cấp cứu lúc ${patientCase.arrivalTime} vì ${patientCase.chiefComplaint}. " +
        "Qua hỏi bệnh và thăm khám ghi nhận: Sinh hiệu lúc vào HA ${patientCase.initialVitals.bpSys}/${patientCase.initialVitals.bpDia} mmHg, Mạch ${patientCase.initialVitals.heartRate} l/p, SpO2 ${patientCase.initialVitals.spo2}%, GCS ${patientCase.initialVitals.gcs} điểm. " +
        "Đã thực hiện khám các hệ cơ quan liên quan và hoàn thành các cận lâm sàng cấp cứu cần thiết."
    }

    var summaryText by remember { mutableStateOf(initialSummary) }
    var prognosisText by remember { mutableStateOf("Tiên lượng nặng, cần theo dõi sát dấu hiệu sinh tồn và đáp ứng điều trị chuyên khoa.") }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(12.dp)
    ) {
        // TIÊU ĐỀ BỆNH ÁN ĐIỆN TỬ
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Description, contentDescription = null, tint = MedicalPrimaryLight, modifier = Modifier.size(20.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Column {
                    Text(
                        text = "HỒ SƠ BỆNH ÁN CẤP CỨU ĐIỆN TỬ (EMR)",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Black,
                        color = MedicalPrimaryLight
                    )
                    Text(
                        text = "Khung chuẩn Thông tư 46/2018/TT-BYT Bộ Y Tế Việt Nam",
                        fontSize = 9.sp,
                        color = TextSecondary
                    )
                }
            }

            // Trạng thái ký số
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(4.dp))
                    .background(if (isSigned) EcgGreen.copy(alpha = 0.2f) else AlertOrange.copy(alpha = 0.2f))
                    .border(1.dp, if (isSigned) EcgGreen else AlertOrange, RoundedCornerShape(4.dp))
                    .padding(horizontal = 6.dp, vertical = 2.dp)
            ) {
                Text(
                    text = if (isSigned) "ĐÃ KÝ ĐIỆN TỬ" else "CHỜ KÝ XÁC NHẬN",
                    fontSize = 9.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isSigned) EcgGreen else AlertOrange
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // BẢNG BỆNH ÁN ĐIỆN TỬ CHUẨN BỘ Y TẾ
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(10.dp),
            colors = CardDefaults.cardColors(containerColor = DarkSurface),
            border = androidx.compose.foundation.BorderStroke(1.2.dp, MedicalPrimary.copy(alpha = 0.6f))
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                // QUỐC HIỆU & TIÊU NGỮ CHUẨN VIỆT NAM
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "CỘNG HÒA XÃ HỘI CHỦ NGHĨA VIỆT NAM",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        letterSpacing = 0.5.sp
                    )
                    Text(
                        text = "Độc lập - Tự do - Hạnh phúc",
                        fontSize = 9.sp,
                        color = TextSecondary
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    HorizontalDivider(color = BorderSubtle, thickness = 0.8.dp, modifier = Modifier.width(100.dp))
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "BỆNH ÁN CẤP CỨU",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Black,
                        color = Color(0xFFFEF08A),
                        letterSpacing = 1.sp
                    )
                    Text(
                        text = "Mã BAĐT: BA-CC-${patientCase.id.uppercase()} • Thời điểm: $currentDate",
                        fontSize = 9.sp,
                        fontFamily = FontFamily.Monospace,
                        color = TextSecondary
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))
                HorizontalDivider(color = BorderSubtle)
                Spacer(modifier = Modifier.height(10.dp))

                // I. HÀNH CHÍNH
                EmrSectionHeader("I. PHẦN HÀNH CHÍNH")
                EmrRow("Họ và tên người bệnh:", patientCase.patientName.uppercase(), isBold = true)
                EmrRow("Tuổi:", "${patientCase.age} tuổi • Giới tính: ${patientCase.gender}")
                EmrRow("Nghề nghiệp:", patientCase.occupation)
                EmrRow("Thời điểm vào cấp cứu:", "${patientCase.arrivalTime} ngày $currentDate")
                EmrRow("Mức độ ưu tiên cấp cứu:", patientCase.triageLevel.label, highlightColor = Color(patientCase.triageLevel.badgeColor))

                Spacer(modifier = Modifier.height(8.dp))

                // II. LÝ DO VÀO VIỆN
                EmrSectionHeader("II. LÝ DO VÀO VIỆN")
                Text(
                    text = patientCase.chiefComplaint,
                    fontSize = 11.sp,
                    color = Color.White,
                    modifier = Modifier.padding(start = 8.dp)
                )

                Spacer(modifier = Modifier.height(8.dp))

                // III. BỆNH SỬ CẤP CỨU
                EmrSectionHeader("III. BỆNH SỬ")
                Text(
                    text = "Bệnh nhân khởi phát triệu chứng cấp tính trước nhập viện. Vào viện cấp cứu trong tình trạng ${patientCase.chiefComplaint.lowercase()}, tri giác GCS ${patientCase.initialVitals.gcs}/15.",
                    fontSize = 11.sp,
                    color = TextPrimary,
                    lineHeight = 15.sp,
                    modifier = Modifier.padding(start = 8.dp)
                )

                Spacer(modifier = Modifier.height(8.dp))

                // IV. KHÁM BỆNH KHI VÀO VIỆN
                EmrSectionHeader("IV. KHÁM BỆNH KHI VÀO VIỆN")
                EmrRow("1. Dấu hiệu sinh tồn:", "HA: ${patientCase.initialVitals.bpSys}/${patientCase.initialVitals.bpDia} mmHg • Mạch: ${patientCase.initialVitals.heartRate} l/p • SpO2: ${patientCase.initialVitals.spo2}% • GCS: ${patientCase.initialVitals.gcs}đ")
                EmrRow("2. Đường thở & Hô hấp:", patientCase.abcde.airwayDesc)
                EmrRow("3. Các cơ quan đã thăm khám:", "${examinedSystems.size}/6 hệ cơ quan đã được kiểm tra ghi nhận vào hồ sơ.")

                Spacer(modifier = Modifier.height(8.dp))

                // V. CẬN LÂM SÀNG CỐT LÕI
                EmrSectionHeader("V. CẬN LÂM SÀNG ĐÃ THỰC HIỆN")
                val orderedLabs = patientCase.availableLabs.filter { it.id in orderedLabIds }
                if (orderedLabs.isEmpty()) {
                    Text(text = "Chưa có chỉ định cận lâm sàng", fontSize = 10.sp, color = TextMuted, modifier = Modifier.padding(start = 8.dp))
                } else {
                    orderedLabs.forEach { lab ->
                        Text(
                            text = "• ${lab.name}: ${lab.resultDetailed}",
                            fontSize = 10.sp,
                            color = TextSecondary,
                            lineHeight = 14.sp,
                            modifier = Modifier.padding(start = 8.dp, bottom = 2.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // VI. CHẨN ĐOÁN XÁC ĐỊNH THEO ICD-10
                EmrSectionHeader("VI. CHẨN ĐOÁN VÀO VIỆN (THEO BẢN MÃ ICD-10)")
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(6.dp))
                        .background(Color(0xFF0F1E36))
                        .border(1.dp, MedicalPrimaryLight.copy(alpha = 0.6f), RoundedCornerShape(6.dp))
                        .padding(8.dp)
                ) {
                    Column {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "BỆNH CHÍNH & PHỐI HỢP (ICD-10):",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = MedicalPrimaryLight
                            )
                            Text(
                                text = "Chuẩn mã hóa ICD-10",
                                fontSize = 8.sp,
                                color = Spo2Cyan,
                                fontWeight = FontWeight.Bold
                            )
                        }
                        Text(
                            text = primaryDx.ifBlank { "[Chưa nhập chẩn đoán xác định theo mã ICD-10]" },
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Black,
                            color = Color(0xFFFEF08A)
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "Bệnh kèm theo / Biến chứng: Theo dõi suy hô hấp cấp, nguy cơ rối loạn huyết động.",
                            fontSize = 10.sp,
                            color = TextSecondary
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // VII. TÓM TẮT BỆNH ÁN (HỘI CHỨNG VÀ TRIỆU CHỨNG CHÍNH)
                EmrSectionHeader("VII. TÓM TẮT BỆNH ÁN & HỘI CHỨNG CHÍNH")
                OutlinedTextField(
                    value = summaryText,
                    onValueChange = { summaryText = it },
                    minLines = 3,
                    maxLines = 5,
                    modifier = Modifier.fillMaxWidth().testTag("input_emr_summary"),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MedicalPrimaryLight,
                        unfocusedBorderColor = BorderSubtle,
                        focusedContainerColor = DarkSurfaceVariant,
                        unfocusedContainerColor = DarkSurfaceVariant
                    ),
                    textStyle = LocalTextStyle.current.copy(fontSize = 11.sp, lineHeight = 15.sp)
                )

                Spacer(modifier = Modifier.height(8.dp))

                // VIII. XỬ TRÍ CẤP CỨU & KẾ HOẠCH ĐIỀU TRỊ CHUYÊN KHOA
                EmrSectionHeader("VIII. XỬ TRÍ CẤP CỨU & HƯỚNG ĐIỀU TRỊ TIẾP THEO")
                val masterOrders = com.example.data.MasterHospitalOrders.getAllMasterOrders() + patientCase.standardOrders
                val executedOrders = masterOrders.filter { it.id in executedOrderIds }.distinctBy { it.id }
                if (executedOrders.isEmpty()) {
                    Text(text = "Chưa có y lệnh xử trí nào được thực hiện.", fontSize = 10.sp, color = TextMuted, modifier = Modifier.padding(start = 8.dp))
                } else {
                    executedOrders.forEach { order ->
                        val prefix = if (order.category == OrderCategory.CONSULTATION) "-> [CHUYÊN KHOA]" else "• [XỬ TRÍ BAN ĐẦU]"
                        Text(
                            text = "$prefix ${order.name}",
                            fontSize = 10.sp,
                            color = if (order.category == OrderCategory.CONSULTATION) MedicalPrimaryLight else TextPrimary,
                            modifier = Modifier.padding(start = 8.dp, bottom = 2.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // IX. TIÊN LƯỢNG
                EmrSectionHeader("IX. TIÊN LƯỢNG")
                OutlinedTextField(
                    value = prognosisText,
                    onValueChange = { prognosisText = it },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth().testTag("input_emr_prognosis"),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MedicalPrimaryLight,
                        unfocusedBorderColor = BorderSubtle,
                        focusedContainerColor = DarkSurfaceVariant,
                        unfocusedContainerColor = DarkSurfaceVariant
                    ),
                    textStyle = LocalTextStyle.current.copy(fontSize = 11.sp)
                )

                Spacer(modifier = Modifier.height(14.dp))
                HorizontalDivider(color = BorderSubtle)
                Spacer(modifier = Modifier.height(10.dp))

                // CHỮ KÝ ĐIỆN TỬ CỦA BÁC SĨ ĐIỀU TRỊ
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(text = "BÁC SĨ ĐIỀU TRỊ", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = TextSecondary)
                        Text(text = "Kíp trực Cấp cứu Hồi sức", fontSize = 10.sp, color = TextMuted)
                    }

                    if (isSigned) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(EcgGreen.copy(alpha = 0.15f))
                                .border(1.dp, EcgGreen, RoundedCornerShape(6.dp))
                                .padding(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.CheckCircle, contentDescription = null, tint = EcgGreen, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Column {
                                    Text(text = "ĐÃ KÝ XÁC THỰC SỐ", fontSize = 9.sp, fontWeight = FontWeight.Bold, color = EcgGreen)
                                    Text(text = "BS. CẤP CỨU • $currentDate", fontSize = 8.sp, color = TextSecondary)
                                }
                            }
                        }
                    } else {
                        Button(
                            onClick = { isSigned = true },
                            colors = ButtonDefaults.buttonColors(containerColor = MedicalPrimary),
                            shape = RoundedCornerShape(6.dp),
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                            modifier = Modifier.testTag("btn_sign_emr")
                        ) {
                            Icon(Icons.Default.AssignmentTurnedIn, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(text = "KÝ BỆNH ÁN ĐIỆN TỬ", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // NÚT CHUYỂN SANG BƯỚC GIAO BAN
        Button(
            onClick = onProceedToDebrief,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .testTag("btn_proceed_to_debrief_from_emr"),
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MedicalPrimary,
                contentColor = Color.White
            )
        ) {
            Icon(Icons.Default.LocalHospital, contentDescription = null, modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "HOÀN TẤT BỆNH ÁN -> CHUYỂN SANG GIAO BAN",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.width(6.dp))
            Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null, modifier = Modifier.size(18.dp))
        }
    }
}

@Composable
private fun EmrSectionHeader(title: String) {
    Text(
        text = title,
        fontSize = 11.sp,
        fontWeight = FontWeight.Black,
        color = MedicalPrimaryLight,
        modifier = Modifier.padding(vertical = 3.dp)
    )
}

@Composable
private fun EmrRow(
    label: String,
    value: String,
    isBold: Boolean = false,
    highlightColor: Color = TextPrimary
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 1.dp, horizontal = 8.dp)
    ) {
        Text(text = label, fontSize = 10.sp, color = TextSecondary, modifier = Modifier.width(130.dp))
        Text(
            text = value,
            fontSize = 10.sp,
            fontWeight = if (isBold) FontWeight.Bold else FontWeight.Normal,
            color = highlightColor,
            modifier = Modifier.weight(1f)
        )
    }
}
