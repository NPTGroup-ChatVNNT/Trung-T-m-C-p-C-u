package com.example.ui.steps

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
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
import java.text.NumberFormat
import java.util.Locale

@Composable
fun DebriefAndHandoverStep(
    patientCase: PatientCase,
    examinedSystems: Set<String>,
    orderedLabIds: Set<String>,
    primaryDx: String,
    diffDxList: List<String>,
    reasoningText: String,
    executedOrderIds: Set<String>,
    isCardiacArrest: Boolean,
    isRescued: Boolean,
    onAcceptNextPatient: () -> Unit,
    modifier: Modifier = Modifier
) {
    val vnFormat = remember { NumberFormat.getNumberInstance(Locale("vi", "VN")) }

    // 1. Calculate Financial Accounting
    val labCost = remember(orderedLabIds, patientCase.availableLabs) {
        patientCase.availableLabs.filter { it.id in orderedLabIds }.sumOf { it.costVnd }
    }
    val allOrders = remember(patientCase) {
        (com.example.data.MasterHospitalOrders.getAllMasterOrders() + patientCase.standardOrders).distinctBy { it.id }
    }
    val rxCost = remember(executedOrderIds, allOrders) {
        allOrders.filter { it.id in executedOrderIds }.sumOf { it.costVnd }
    }
    val totalHospitalBill = labCost + rxCost
    val insuranceCovered = (totalHospitalBill * 0.80).toLong()
    val patientCoPay = totalHospitalBill - insuranceCovered

    // 2. Consumed Medical Supplies
    val suppliesConsumed = remember(executedOrderIds, allOrders) {
        val list = mutableListOf<String>()
        allOrders.filter { it.id in executedOrderIds }.forEach {
            list.addAll(it.suppliesUsed)
        }
        if (list.isEmpty()) listOf("Bơm tiêm 5ml", "Găng tay y tế", "Bông cồn vô trùng") else list.distinct()
    }

    // 3. Clinical Scoring
    val examScore = (examinedSystems.size * 3).coerceAtMost(20)
    val matchedKeywords = patientCase.diagnosisKeywords.count { kw ->
        primaryDx.contains(kw, ignoreCase = true)
    }
    val dxScore = if (matchedKeywords >= 2) 35 else if (matchedKeywords == 1) 20 else 10
    val essentialCount = patientCase.standardOrders.filter { it.isEssential && it.id in executedOrderIds }.size
    val harmfulExecuted = patientCase.standardOrders.any { it.isHarmful && it.id in executedOrderIds }
    val rxScore = (essentialCount * 12).coerceAtMost(35) - (if (harmfulExecuted) 25 else 0)

    val totalScore = (examScore + dxScore + rxScore + (if (isRescued) 10 else 0)).coerceIn(0, 100)

    val ranking = when {
        totalScore >= 90 -> "XUẤT SẮC - BÁC SĨ CHUYÊN GIA CẤP CỨU"
        totalScore >= 75 -> "KHÁ - XỬ TRÍ CHUẨN XÁC, AN TOÀN"
        totalScore >= 55 -> "ĐẠT - HOÀN THÀNH CA TRỰC"
        else -> "CẦN RÚT KINH NGHIỆM LÂM SÀNG NGHIÊM TÚC"
    }

    val rankColor = when {
        totalScore >= 90 -> EcgGreen
        totalScore >= 75 -> Spo2Cyan
        totalScore >= 55 -> AlertOrange
        else -> MedicalRedLight
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(14.dp)
    ) {
        // Debrief Banner
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = DarkSurface),
            border = androidx.compose.foundation.BorderStroke(2.dp, rankColor)
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "BIÊN BẢN GIAO BAN KẾT CA (CLINICAL SHIFT HANDOVER)",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Black,
                    color = MedicalRedLight,
                    letterSpacing = 1.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = patientCase.patientName.uppercase(),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Black,
                    color = Color.White
                )

                Spacer(modifier = Modifier.height(8.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "$totalScore",
                        fontSize = 36.sp,
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Black,
                        color = rankColor
                    )
                    Text(
                        text = "/100 ĐIỂM",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextSecondary,
                        modifier = Modifier.padding(start = 4.dp, top = 10.dp)
                    )
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(rankColor.copy(alpha = 0.2f))
                        .border(1.dp, rankColor, RoundedCornerShape(6.dp))
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = ranking,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Black,
                        color = rankColor
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // 1. TỔNG KẾT CHI PHÍ VIỆN PHÍ & BẢO HIỂM Y TẾ (FINANCIAL ACCOUNTING)
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(10.dp),
            colors = CardDefaults.cardColors(containerColor = DarkSurface),
            border = androidx.compose.foundation.BorderStroke(1.dp, Spo2Cyan)
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.ReceiptLong, contentDescription = null, tint = Spo2Cyan, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "BẢNG KÊ VIỆN PHÍ & VẬT TƯ TIÊU HAO:",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Spo2Cyan
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))

                AccountingRow("Cận lâm sàng & CĐHA (${orderedLabIds.size} XN):", "${vnFormat.format(labCost)} đ")
                AccountingRow("Thuốc cấp cứu & Hồi sức dịch:", "${vnFormat.format(rxCost)} đ")
                HorizontalDivider(color = BorderSubtle, modifier = Modifier.padding(vertical = 4.dp))
                AccountingRow("TỔNG VIỆN PHÍ ƯỚC TÍNH:", "${vnFormat.format(totalHospitalBill)} đ", isBold = true, highlightColor = Color(0xFFFEF08A))
                AccountingRow("• BHYT thanh toán (80%):", "-${vnFormat.format(insuranceCovered)} đ", highlightColor = EcgGreen)
                AccountingRow("• Người bệnh đồng chi trả (20%):", "${vnFormat.format(patientCoPay)} đ", highlightColor = AlertOrange)

                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Vật tư tiêu hao xuất kho: ${suppliesConsumed.take(6).joinToString(", ")}...",
                    fontSize = 10.sp,
                    color = TextSecondary,
                    lineHeight = 14.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // 2. HIỆU QUẢ ĐIỀU TRỊ & DIỄN BIẾN PHÂN NHÁNH
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(10.dp),
            colors = CardDefaults.cardColors(containerColor = DarkSurface),
            border = androidx.compose.foundation.BorderStroke(1.dp, BorderSubtle)
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = if (isRescued || (!harmfulExecuted && totalScore >= 60)) Icons.Default.CheckCircle else Icons.Default.Warning,
                        contentDescription = null,
                        tint = if (isRescued || (!harmfulExecuted && totalScore >= 60)) EcgGreen else MedicalRedLight,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "HIỆU QUẢ ĐIỀU TRỊ & PHÂN NHÁNH SINH TỒN:",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
                Spacer(modifier = Modifier.height(6.dp))
                val outcomeText = if (harmfulExecuted) {
                    "CẢNH BÁO NGUY HIỂM: Y lệnh chống chỉ định đã được ban hành khiến bệnh nhân bị sốc tụt huyết áp và suy đa tạng!"
                } else if (isRescued || totalScore >= 75) {
                    patientCase.successfulRescueNarrative
                } else {
                    "Bệnh nhân tạm thời qua cơn nguy kịch ban đầu nhưng một số quyết định xử trí còn chậm trễ so với khung thời gian vàng Bộ Y Tế."
                }
                Text(
                    text = outcomeText,
                    fontSize = 12.sp,
                    color = TextPrimary,
                    lineHeight = 16.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // 3. ĐỐI CHIẾU CHẨN ĐOÁN VỚI CHUẨN BỘ Y TẾ
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(10.dp),
            colors = CardDefaults.cardColors(containerColor = DarkSurface),
            border = androidx.compose.foundation.BorderStroke(1.dp, BorderSubtle)
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = "ĐỐI CHIẾU CHẨN ĐOÁN LÂM SÀNG:",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    color = AlertOrange
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(text = "Chẩn đoán bạn đã tự gõ: \"$primaryDx\"", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFEF08A))
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = "Chẩn đoán chuẩn Bộ Y Tế: ${patientCase.goldenDiagnosis}", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = EcgGreen)
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = "Phân biệt cần loại trừ: ${patientCase.goldenDifferentials.joinToString(", ")}", fontSize = 10.sp, color = TextSecondary)
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // 4. BÀI HỌC KINH NGHIỆM LÂM SÀNG (CLINICAL PEARLS)
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(10.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF0C2438)),
            border = androidx.compose.foundation.BorderStroke(1.dp, Spo2Cyan)
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.School, contentDescription = null, tint = Spo2Cyan, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "ĐIỂM NHẤN LÂM SÀNG BỘ Y TẾ (CLINICAL PEARLS):",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Spo2Cyan
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = patientCase.clinicalPearls,
                    fontSize = 11.sp,
                    color = Color.White,
                    lineHeight = 15.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Button to accept next patient
        Button(
            onClick = onAcceptNextPatient,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MedicalPrimary,
                contentColor = Color.White
            )
        ) {
            Icon(Icons.Default.LocalHospital, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "BÀN GIAO XONG • TIẾP NHẬN CA BỆNH KẾ TIẾP",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.width(6.dp))
            Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null)
        }
    }
}

@Composable
private fun AccountingRow(
    label: String,
    value: String,
    isBold: Boolean = false,
    highlightColor: Color = TextPrimary
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = label, fontSize = 11.sp, fontWeight = if (isBold) FontWeight.Bold else FontWeight.Normal, color = TextPrimary)
        Text(text = value, fontSize = 11.sp, fontFamily = FontFamily.Monospace, fontWeight = if (isBold) FontWeight.Black else FontWeight.Medium, color = highlightColor)
    }
}
