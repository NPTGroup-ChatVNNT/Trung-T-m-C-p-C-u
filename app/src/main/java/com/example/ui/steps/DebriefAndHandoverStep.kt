package com.example.ui.steps

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ai.GeminiClinicalService
import com.example.data.Icd10Database
import com.example.model.OrderCategory
import com.example.model.PatientCase
import com.example.ui.theme.*
import kotlinx.coroutines.launch
import java.text.NumberFormat
import java.util.Locale

@Composable
fun DebriefAndHandoverStep(
    patientCase: PatientCase,
    examinedSystems: Set<String>,
    orderedLabIds: Set<String>,
    primaryDx: String,
    diffDxList: List<String> = emptyList(),
    reasoningText: String = "",
    executedOrderIds: Set<String>,
    isCardiacArrest: Boolean,
    isRescued: Boolean,
    onAcceptNextPatient: () -> Unit,
    modifier: Modifier = Modifier
) {
    val vnFormat = remember { NumberFormat.getNumberInstance(Locale("vi", "VN")) }
    val coroutineScope = rememberCoroutineScope()

    // Expandable section states for clean decluttering
    var isChiefDebriefExpanded by remember { mutableStateOf(true) }
    var isFinalVerdictExpanded by remember { mutableStateOf(true) }
    var isScoringExpanded by remember { mutableStateOf(true) }
    var isSurvivalBranchesExpanded by remember { mutableStateOf(true) }
    var isFinancialExpanded by remember { mutableStateOf(false) }

    // State for player clinical defense & AI chief debrief
    var playerDefenseInput by remember { mutableStateOf(reasoningText) }
    var isChiefAnalyzing by remember { mutableStateOf(false) }
    var chiefDebriefResult by remember { mutableStateOf<GeminiClinicalService.ChiefDebriefResult?>(null) }
    var hasSubmittedDefense by remember { mutableStateOf(false) }

    // Initialize baseline evaluation on first composition
    LaunchedEffect(patientCase.id, primaryDx, executedOrderIds) {
        val initialResult = GeminiClinicalService.debriefWithChiefDoctor(
            patientCase = patientCase,
            doctorTypedDx = primaryDx,
            executedOrders = executedOrderIds.toList(),
            playerDefenseText = reasoningText
        )
        chiefDebriefResult = initialResult
    }

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

    val suppliesConsumed = remember(executedOrderIds, allOrders) {
        val list = mutableListOf<String>()
        allOrders.filter { it.id in executedOrderIds }.forEach {
            list.addAll(it.suppliesUsed)
        }
        if (list.isEmpty()) listOf("Bơm tiêm 5ml", "Găng tay y tế", "Bông cồn vô trùng") else list.distinct()
    }

    // 2. Transparent Clinical Scoring Framework (Thang điểm 100 điểm với tiêu chí rõ ràng)
    // - Khám lâm sàng: 20 điểm tối đa
    val examScore = (examinedSystems.size * 3.5).toInt().coerceAtMost(20)

    // - Chẩn đoán xác định: 35 điểm tối đa
    val matchedKeywords = patientCase.diagnosisKeywords.count { kw ->
        primaryDx.contains(kw, ignoreCase = true)
    }
    val dxScore = when {
        matchedKeywords >= 2 -> 35
        matchedKeywords == 1 -> 22
        primaryDx.trim().length >= 4 -> 12
        else -> 0
    }

    // - Xử trí cấp cứu ban đầu (Airway, Meds, Procedures): 25 điểm tối đa
    val initialOrdersInCase = patientCase.standardOrders.filter { it.category != OrderCategory.CONSULTATION }
    val initialEssentialExecuted = initialOrdersInCase.filter { it.isEssential && it.id in executedOrderIds }.size
    val initialRxScore = (initialEssentialExecuted * 10).coerceAtMost(25)

    // - Điều trị chuyên khoa & Chuyển khoa chính xác: 20 điểm tối đa
    val specialtyOrdersInCase = patientCase.standardOrders.filter { it.category == OrderCategory.CONSULTATION }
    val specialtyEssentialExecuted = specialtyOrdersInCase.filter { it.isEssential && it.id in executedOrderIds }.size
    val specialtyScore = if (specialtyOrdersInCase.isEmpty()) {
        if (executedOrderIds.any { it.startsWith("c_") }) 20 else 10
    } else {
        (specialtyEssentialExecuted * 20).coerceAtMost(20)
    }

    // - Điểm trừ an toàn: -25 điểm nếu vi phạm chống chỉ định
    val harmfulExecuted = patientCase.standardOrders.any { it.isHarmful && it.id in executedOrderIds }
    val safetyPenalty = if (harmfulExecuted) 25 else 0

    // - Điểm thưởng hồi sinh tim phổi: +10 điểm
    val bonusRescued = if (isRescued) 10 else 0

    val totalScore = (examScore + dxScore + initialRxScore + specialtyScore - safetyPenalty + bonusRescued).coerceIn(0, 100)

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

    // Determine current patient survival branch
    val currentBranchIndex = when {
        harmfulExecuted -> 3 // Diễn tiến xấu
        totalScore >= 75 || isRescued -> 1 // Hồi phục tối ưu
        else -> 2 // Di chứng / Theo dõi ICU
    }

    // Verdict suitability determination
    val verdictStatus = chiefDebriefResult?.suitabilityStatus ?: when {
        harmfulExecuted -> "PHƯƠNG ÁN ĐIỀU TRỊ CHƯA PHÙ HỢP - NGUY CƠ BIẾN CHỨNG ĐE DỌA TÍNH MẠNG"
        dxScore >= 22 && initialRxScore >= 15 -> "PHƯƠNG ÁN ĐIỀU TRỊ HOÀN TOÀN PHÙ HỢP VÀ CHUẨN XÁC"
        dxScore >= 12 -> "PHƯƠNG ÁN ĐIỀU TRỊ PHÙ HỢP MỘT PHẦN - CẦN RÚT KINH NGHIỆM VỀ TỐC ĐỘ"
        else -> "PHƯƠNG ÁN ĐIỀU TRỊ CHƯA PHÙ HỢP - SAI LỆCH CHẨN ĐOÁN"
    }

    val verdictColor = when {
        verdictStatus.contains("HOÀN TOÀN PHÙ HỢP") -> EcgGreen
        verdictStatus.contains("PHÙ HỢP MỘT PHẦN") -> AlertOrange
        else -> MedicalRedLight
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(12.dp)
    ) {
        // BIÊN BẢN GIAO BAN BANNER (TỔNG KẾT ĐIỂM)
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = DarkSurface),
            border = androidx.compose.foundation.BorderStroke(2.dp, rankColor)
        ) {
            Column(
                modifier = Modifier.padding(14.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "BIÊN BẢN GIAO BAN KẾT CA TRỰC",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Black,
                    color = MedicalPrimaryLight,
                    letterSpacing = 1.sp
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = patientCase.patientName.uppercase(),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Black,
                    color = Color.White
                )

                Spacer(modifier = Modifier.height(6.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "$totalScore",
                        fontSize = 34.sp,
                        fontFamily = FontFamily.Monospace,
                        fontWeight = FontWeight.Black,
                        color = rankColor
                    )
                    Text(
                        text = "/100 ĐIỂM",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextSecondary,
                        modifier = Modifier.padding(start = 4.dp, top = 8.dp)
                    )
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(rankColor.copy(alpha = 0.15f))
                        .border(1.dp, rankColor, RoundedCornerShape(6.dp))
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = ranking,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Black,
                        color = rankColor
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // =========================================================================
        // MỤC: KHUNG TIÊU CHUẨN ĐÁNH GIÁ CHUYÊN MÔN & THANG ĐIỂM (100 ĐIỂM)
        // Hiển thị trực tiếp ngay đầu biên bản giao ban
        // =========================================================================
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { isScoringExpanded = !isScoringExpanded },
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = DarkSurface),
            border = androidx.compose.foundation.BorderStroke(1.2.dp, Spo2Cyan)
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Assessment,
                            contentDescription = null,
                            tint = Spo2Cyan,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(
                                text = "KHUNG TIÊU CHUẨN & THANG ĐIỂM ĐÁNH GIÁ (100 ĐIỂM)",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Black,
                                color = Spo2Cyan
                            )
                            Text(
                                text = "Bộ tiêu chuẩn đánh giá kết ca lâm sàng & an toàn người bệnh",
                                fontSize = 9.sp,
                                color = TextSecondary
                            )
                        }
                    }
                    Icon(
                        imageVector = if (isScoringExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                        contentDescription = null,
                        tint = TextSecondary,
                        modifier = Modifier.size(18.dp)
                    )
                }

                AnimatedVisibility(visible = isScoringExpanded) {
                    Column(modifier = Modifier.padding(top = 12.dp)) {
                        // 4 TIÊU CHUẨN TRỤ CỘT CHUYÊN MÔN
                        ScoringCriteriaRow(
                            title = "1. Tiếp cận & Khám xét lâm sàng toàn diện",
                            detail = "Khám đủ 6 hệ cơ quan (${examinedSystems.size}/6), hỏi bệnh sử & sinh tồn",
                            score = "$examScore / 20",
                            progress = (examScore / 20f).coerceIn(0f, 1f),
                            badgeText = if (examScore >= 18) "TỐI ƯU" else if (examScore >= 12) "ĐẠT" else "THIẾU SÓT",
                            isFull = examScore >= 18
                        )
                        Spacer(modifier = Modifier.height(6.dp))

                        ScoringCriteriaRow(
                            title = "2. Chẩn đoán xác định & Mã hóa chuẩn ICD-10",
                            detail = "Chuẩn mã hóa ICD-10, đúng bệnh danh then chốt & biến chứng",
                            score = "$dxScore / 35",
                            progress = (dxScore / 35f).coerceIn(0f, 1f),
                            badgeText = if (dxScore >= 30) "CHÍNH XÁC" else if (dxScore >= 20) "ĐẠT" else "SAI LỆCH",
                            isFull = dxScore >= 30
                        )
                        Spacer(modifier = Modifier.height(6.dp))

                        ScoringCriteriaRow(
                            title = "3. Xử trí cấp cứu ban đầu khẩn cấp (ABCDE)",
                            detail = "Đường thở, oxy/NKQ, lập tĩnh mạch & dùng thuốc cấp cứu hồi sức",
                            score = "$initialRxScore / 25",
                            progress = (initialRxScore / 25f).coerceIn(0f, 1f),
                            badgeText = if (initialRxScore >= 20) "KỊP THỜI" else if (initialRxScore >= 10) "CÒN CHẬM" else "THIẾU",
                            isFull = initialRxScore >= 20
                        )
                        Spacer(modifier = Modifier.height(6.dp))

                        ScoringCriteriaRow(
                            title = "4. Can thiệp chuyên khoa & Chuyển khoa chính xác",
                            detail = "Hội chẩn đúng chuyên khoa: Phẫu thuật, Cathlab, Đột quỵ hoặc HSTC/ICU",
                            score = "$specialtyScore / 20",
                            progress = (specialtyScore / 20f).coerceIn(0f, 1f),
                            badgeText = if (specialtyScore >= 18) "CHUẨN XÁC" else "CHƯA TỐI ƯU",
                            isFull = specialtyScore >= 18
                        )

                        if (safetyPenalty > 0) {
                            Spacer(modifier = Modifier.height(6.dp))
                            ScoringCriteriaRow(
                                title = "• Vi phạm an toàn người bệnh (Chống chỉ định)",
                                detail = "Ra y lệnh hoặc dùng thuốc nguy hại đến tính mạng bệnh nhân",
                                score = "-$safetyPenalty",
                                progress = 1f,
                                badgeText = "NGUY HIỂM",
                                isNegative = true
                            )
                        }

                        if (bonusRescued > 0) {
                            Spacer(modifier = Modifier.height(6.dp))
                            ScoringCriteriaRow(
                                title = "• Thưởng cấp cứu ngừng tim hồi sinh thành công",
                                detail = "Ép tim CPR / Khử rung sốc điện cứu sống người bệnh nguy kịch",
                                score = "+$bonusRescued",
                                progress = 1f,
                                badgeText = "THƯỞNG",
                                isBonus = true
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))
                        HorizontalDivider(color = BorderSubtle, thickness = 0.5.dp)
                        Spacer(modifier = Modifier.height(8.dp))

                        // BẢNG KHUNG TIÊU CHUẨN XẾP LOẠI NĂNG LỰC
                        Text(
                            text = "Khung tiêu chuẩn xếp loại năng lực lâm sàng:",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            RubricGradeItem("≥ 90", "Xuất sắc", totalScore >= 90, EcgGreen, Modifier.weight(1f))
                            RubricGradeItem("75-89", "Khá", totalScore in 75..89, Spo2Cyan, Modifier.weight(1f))
                            RubricGradeItem("55-74", "Đạt", totalScore in 55..74, AlertOrange, Modifier.weight(1f))
                            RubricGradeItem("< 55", "Rút KN", totalScore < 55, MedicalRedLight, Modifier.weight(1f))
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // =========================================================================
        // PHẦN MỚI 1: GIAO BAN CHUYÊN MÔN - TRƯỞNG KHOA CHẤT VẤN & NGƯỜI CHƠI BIỆN LUẬN
        // =========================================================================
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { isChiefDebriefExpanded = !isChiefDebriefExpanded },
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = DarkSurface),
            border = androidx.compose.foundation.BorderStroke(1.5.dp, MedicalPrimary)
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.MedicalServices,
                            contentDescription = null,
                            tint = MedicalPrimaryLight,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Column {
                            Text(
                                text = "TRƯỞNG KHOA CHẤT VẤN & BÁC SĨ BIỆN LUẬN",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Black,
                                color = MedicalPrimaryLight
                            )
                            Text(
                                text = "TS.BS Nguyễn Hữu Trí - Trưởng Khoa Cấp Cứu Hồi Sức",
                                fontSize = 9.sp,
                                color = TextSecondary
                            )
                        }
                    }
                    Icon(
                        imageVector = if (isChiefDebriefExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                        contentDescription = null,
                        tint = TextSecondary,
                        modifier = Modifier.size(18.dp)
                    )
                }

                AnimatedVisibility(visible = isChiefDebriefExpanded) {
                    Column(modifier = Modifier.padding(top = 12.dp)) {
                        // HỘP THOẠI CÂU HỎI CHẤT VẤN CỦA TRƯỞNG KHOA
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .background(Color(0xFF0F2338))
                                .border(1.dp, MedicalPrimaryLight.copy(alpha = 0.5f), RoundedCornerShape(8.dp))
                                .padding(10.dp)
                        ) {
                            Column {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.QuestionAnswer,
                                        contentDescription = null,
                                        tint = Spo2Cyan,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "TRƯỞNG KHOA ĐẶT CÂU HỎI LÂM SÀNG:",
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Spo2Cyan
                                    )
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "Chào Bác sĩ! Trong ca cấp cứu này, bệnh nhân ${patientCase.patientName} (${patientCase.age} tuổi) vào viện vì \"${patientCase.chiefComplaint}\". Bác sĩ đã ra chẩn đoán xác định là: \"$primaryDx\".\n\n" +
                                            "Bác sĩ hãy giải trình trước kíp giao ban: Dựa trên những triệu chứng lâm sàng và xét nghiệm nào mà bác sĩ đưa ra chẩn đoán như vậy theo mã ICD-10? Phương án xử trí ban đầu và điều trị chuyên khoa của bác sĩ đã tối ưu chưa? Có nguy cơ bỏ sót hay chống chỉ định nào không?",
                                    fontSize = 11.sp,
                                    color = Color.White,
                                    lineHeight = 16.sp
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // KHUNG NGƯỜI CHƠI NHẬP BIỆN LUẬN LÂM SÀNG
                        Text(
                            text = "LỜI BIỆN LUẬN LÂM SÀNG CỦA BÁC SĨ (NGƯỜI CHƠI):",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFFEF08A)
                        )
                        Spacer(modifier = Modifier.height(4.dp))

                        OutlinedTextField(
                            value = playerDefenseInput,
                            onValueChange = { playerDefenseInput = it },
                            placeholder = {
                                Text(
                                    text = "Nhập luận điểm lâm sàng của bạn: Biện luận triệu chứng, chỉ số cận lâm sàng, lý do chọn thuốc và hướng chuyển chuyên khoa...",
                                    fontSize = 11.sp,
                                    color = TextSecondary
                                )
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .heightIn(min = 90.dp)
                                .testTag("input_doctor_defense"),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = MedicalPrimaryLight,
                                unfocusedBorderColor = BorderSubtle,
                                focusedContainerColor = DarkSurfaceVariant,
                                unfocusedContainerColor = DarkSurfaceVariant
                            )
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        // GỢI Ý BIỆN LUẬN NHANH (QUICK ARGUMENT CHIPS)
                        Text(
                            text = "Gợi ý biện luận trọng tâm:",
                            fontSize = 9.sp,
                            color = TextSecondary
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            SuggestionChip(
                                onClick = {
                                    playerDefenseInput = "Căn cứ tam chứng lâm sàng, dấu hiệu sinh tồn vào viện và xét nghiệm chỉ điểm để xác định bệnh danh khẩn cấp và mã hóa ICD-10 phù hợp."
                                },
                                label = { Text("Biện luận chẩn đoán", fontSize = 9.sp) },
                                modifier = Modifier.weight(1f)
                            )
                            SuggestionChip(
                                onClick = {
                                    playerDefenseInput = "Ưu tiên kiểm soát đường thở và tuần hoàn (ABCDE), dùng thuốc cấp cứu đầu tay ổn định huyết động trong giờ vàng."
                                },
                                label = { Text("Biện luận xử trí ABCDE", fontSize = 9.sp) },
                                modifier = Modifier.weight(1f)
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        // NÚT GỬI BIỆN LUẬN ĐẾN TRƯỞNG KHOA
                        Button(
                            onClick = {
                                coroutineScope.launch {
                                    isChiefAnalyzing = true
                                    val result = GeminiClinicalService.debriefWithChiefDoctor(
                                        patientCase = patientCase,
                                        doctorTypedDx = primaryDx,
                                        executedOrders = executedOrderIds.toList(),
                                        playerDefenseText = playerDefenseInput
                                    )
                                    chiefDebriefResult = result
                                    hasSubmittedDefense = true
                                    isChiefAnalyzing = false
                                }
                            },
                            enabled = !isChiefAnalyzing,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(46.dp)
                                .testTag("btn_submit_defense"),
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = MedicalPrimary,
                                contentColor = Color.White
                            )
                        ) {
                            if (isChiefAnalyzing) {
                                CircularProgressIndicator(modifier = Modifier.size(18.dp), color = Color.White, strokeWidth = 2.dp)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Trưởng khoa đang thẩm định biện luận...", fontSize = 12.sp)
                            } else {
                                Icon(Icons.Default.Send, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("GỬI BIỆN LUẬN ĐẾN TRƯỞNG KHOA", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            }
                        }

                        // PHẢN HỒI CỦA TRƯỞNG KHOA SAU KHI BIỆN LUẬN
                        chiefDebriefResult?.let { result ->
                            Spacer(modifier = Modifier.height(10.dp))
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(Color(0xFF0B192C))
                                    .border(1.dp, Spo2Cyan.copy(alpha = 0.7f), RoundedCornerShape(8.dp))
                                    .padding(10.dp)
                            ) {
                                Column {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            imageVector = Icons.Default.RateReview,
                                            contentDescription = null,
                                            tint = Spo2Cyan,
                                            modifier = Modifier.size(16.dp)
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(
                                            text = "Ý KIẾN NHẬN XÉT CỦA TRƯỞNG KHOA VỀ LỜI BIỆN LUẬN:",
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Spo2Cyan
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Text(
                                        text = result.chiefCommentOnDefense,
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

        Spacer(modifier = Modifier.height(10.dp))

        // =========================================================================
        // PHẦN MỚI 2: KẾT LUẬN GIAO BAN CUỐI CÙNG - NHẬN XÉT PHƯƠNG ÁN ĐIỀU TRỊ
        // =========================================================================
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { isFinalVerdictExpanded = !isFinalVerdictExpanded },
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = DarkSurface),
            border = androidx.compose.foundation.BorderStroke(2.dp, verdictColor)
        ) {
            Column(modifier = Modifier.padding(14.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Verified,
                            contentDescription = null,
                            tint = verdictColor,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Column {
                            Text(
                                text = "KẾT LUẬN GIAO BAN CUỐI CÙNG",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Black,
                                color = verdictColor
                            )
                            Text(
                                text = "Nhận xét phương án điều trị có phù hợp hay chưa",
                                fontSize = 9.sp,
                                color = TextSecondary
                            )
                        }
                    }
                    Icon(
                        imageVector = if (isFinalVerdictExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                        contentDescription = null,
                        tint = TextSecondary,
                        modifier = Modifier.size(18.dp)
                    )
                }

                AnimatedVisibility(visible = isFinalVerdictExpanded) {
                    Column(modifier = Modifier.padding(top = 12.dp)) {
                        // HUY HIỆU KẾT LUẬN LỚN (VERDICT BADGE)
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .background(verdictColor.copy(alpha = 0.15f))
                                .border(1.5.dp, verdictColor, RoundedCornerShape(8.dp))
                                .padding(12.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = "ĐÁNH GIÁ CHUNG CỦA HỘI ĐỒNG GIAO BAN",
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextSecondary,
                                    letterSpacing = 1.sp
                                )
                                Spacer(modifier = Modifier.height(3.dp))
                                Text(
                                    text = verdictStatus,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Black,
                                    color = verdictColor,
                                    lineHeight = 17.sp,
                                    textAlign = androidx.compose.ui.text.style.TextAlign.Center
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // 3 MỤC NHẬN XÉT TRỤ CỘT CHI TIẾT
                        VerdictDetailCard(
                            pillarNumber = "1",
                            pillarTitle = "Tính phù hợp của Chẩn đoán & Mã hóa ICD-10",
                            doctorChoice = primaryDx,
                            standardTarget = patientCase.goldenDiagnosis,
                            critique = chiefDebriefResult?.diagnosisCritique ?: if (dxScore >= 22) "Chẩn đoán xác định chuẩn xác." else "Chẩn đoán chưa sát với bệnh danh chính.",
                            isPass = dxScore >= 22,
                            accentColor = if (dxScore >= 22) EcgGreen else AlertOrange
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        VerdictDetailCard(
                            pillarNumber = "2",
                            pillarTitle = "Tính phù hợp của Xử trí cấp cứu ban đầu (ABCDE)",
                            doctorChoice = "Đã thực hiện $initialEssentialExecuted y lệnh cấp cứu",
                            standardTarget = "Kiểm soát đường thở, thở oxy, thiết lập ven lớn & dùng thuốc cấp cứu",
                            critique = chiefDebriefResult?.treatmentCritique ?: if (initialRxScore >= 15) "Xử trí ban đầu khẩn trương, cứu sống người bệnh trong giờ vàng." else "Cần khẩn trương hơn trong kiểm soát sinh hiệu.",
                            isPass = initialRxScore >= 15 && !harmfulExecuted,
                            accentColor = if (initialRxScore >= 15 && !harmfulExecuted) EcgGreen else MedicalRedLight
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        VerdictDetailCard(
                            pillarNumber = "3",
                            pillarTitle = "Tính phù hợp của Điều trị chuyên khoa & Chuyển viện",
                            doctorChoice = "Y lệnh chuyên khoa: ${if (specialtyScore >= 18) "Đúng chuyên khoa" else "Chưa chuyển đúng nơi"}",
                            standardTarget = "Chuyển đúng chuyên khoa can thiệp tối ưu: Ngoại, Cathlab, Đột quỵ hoặc Hồi sức tích cực",
                            critique = if (specialtyScore >= 18) "Điều trị chuyên môn đúng địa chỉ, tiếp tục theo dõi điều trị triệt để." else "Chuyển chuyên khoa chưa đúng hoặc chậm trễ ngoài khung giờ vàng.",
                            isPass = specialtyScore >= 18,
                            accentColor = if (specialtyScore >= 18) EcgGreen else AlertOrange
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        // KÝ TÊN PHÊ DUYỆT CỦA TRƯỞNG KHOA
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .background(DarkSurfaceVariant)
                                .border(1.dp, BorderSubtle, RoundedCornerShape(8.dp))
                                .padding(10.dp)
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column {
                                    Text(
                                        text = "KẾT LUẬN CHÍNH THỨC:",
                                        fontSize = 9.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = TextSecondary
                                    )
                                    Text(
                                        text = chiefDebriefResult?.finalVerdictSummary ?: "Biên bản giao ban ca bệnh đã được hội đồng thông qua.",
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = TextPrimary,
                                        lineHeight = 14.sp
                                    )
                                }
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(4.dp))
                                        .background(MedicalPrimary.copy(alpha = 0.2f))
                                        .border(1.dp, MedicalPrimary, RoundedCornerShape(4.dp))
                                        .padding(horizontal = 6.dp, vertical = 3.dp)
                                ) {
                                    Text(
                                        text = "ĐÃ DUYỆT EMR",
                                        fontSize = 8.sp,
                                        fontWeight = FontWeight.Black,
                                        color = MedicalPrimaryLight
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))



        // ========================================================
        // MỤC 2: GIẢI THÍCH RÕ CÁC PHÂN NHÁNH CỦA HIỆU QUẢ ĐIỀU TRỊ VÀ PHÂN NHÁNH SINH TỒN
        // ========================================================
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { isSurvivalBranchesExpanded = !isSurvivalBranchesExpanded },
            shape = RoundedCornerShape(10.dp),
            colors = CardDefaults.cardColors(containerColor = DarkSurface),
            border = androidx.compose.foundation.BorderStroke(1.dp, BorderSubtle)
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.AccountTree, contentDescription = null, tint = Color(0xFFFEF08A), modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "PHÂN NHÁNH SINH TỒN & HIỆU QUẢ ĐIỀU TRỊ",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                    Icon(
                        imageVector = if (isSurvivalBranchesExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                        contentDescription = null,
                        tint = TextSecondary,
                        modifier = Modifier.size(18.dp)
                    )
                }

                AnimatedVisibility(visible = isSurvivalBranchesExpanded) {
                    Column(modifier = Modifier.padding(top = 10.dp)) {
                        Text(
                            text = "Mô hình diễn tiến bệnh nhân theo 3 phân nhánh sinh tồn lâm sàng:",
                            fontSize = 10.sp,
                            color = TextSecondary
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        // PHÂN NHÁNH 1: HỒI PHỤC TỐI ƯU
                        SurvivalBranchCard(
                            branchNum = 1,
                            title = "Phân nhánh 1: Hồi Phục Tối Ưu (Optimal Survival)",
                            conditions = "Chẩn đoán chính xác trong giờ vàng, xử trí ban đầu khẩn trương, chuyển đúng chuyên khoa chuyên môn.",
                            outcome = "Bảo tồn trọn vẹn chức năng cơ quan đích (tim, não, hô hấp), sinh hiệu ổn định, không để lại di chứng thần kinh hay suy tạng.",
                            isCurrent = currentBranchIndex == 1,
                            accentColor = EcgGreen
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        // PHÂN NHÁNH 2: DI CHỨNG / CẦN THEO DÕI ICU
                        SurvivalBranchCard(
                            branchNum = 2,
                            title = "Phân nhánh 2: Di Chứng / Cần Theo Dõi ICU Kéo Dài",
                            conditions = "Chậm trễ chẩn đoán, bỏ sót y lệnh xử trí ban đầu, hoặc điều trị can thiệp muộn so với khung thời gian vàng.",
                            outcome = "Bệnh nhân giữ được tính mạng nhưng chịu tổn thương thứ phát do thiếu máu mô kéo dài, cần thở máy xâm lấn tại ICU hoặc suy tim/thận mạn.",
                            isCurrent = currentBranchIndex == 2,
                            accentColor = AlertOrange
                        )

                        Spacer(modifier = Modifier.height(6.dp))

                        // PHÂN NHÁNH 3: DIỄN TIẾN XẤU / NGUY CƠ TỬ VONG
                        SurvivalBranchCard(
                            branchNum = 3,
                            title = "Phân nhánh 3: Diễn Tiến Xấu / Nguy Cơ Tử Vong",
                            conditions = "Ban hành y lệnh chống chỉ định nguy hại, không cấp cứu đường thở hoặc khử rung kịp thời khi ngừng tuần hoàn.",
                            outcome = "Sốc tụt huyết áp kháng trị, tổn thương não không hồi phục hoặc ngừng tuần hoàn thứ phát không đáp ứng hồi sinh tim phổi.",
                            isCurrent = currentBranchIndex == 3,
                            accentColor = MedicalRedLight
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        // TỔNG KẾT KẾT CỤC CA TRỰC CỦA BỆNH NHÂN HIỆN TẠI
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (currentBranchIndex == 1) EcgGreen.copy(alpha = 0.15f) else if (currentBranchIndex == 2) AlertOrange.copy(alpha = 0.15f) else MedicalRed.copy(alpha = 0.2f))
                                .border(1.dp, if (currentBranchIndex == 1) EcgGreen else if (currentBranchIndex == 2) AlertOrange else MedicalRed, RoundedCornerShape(8.dp))
                                .padding(10.dp)
                        ) {
                            Column {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = if (currentBranchIndex == 1) Icons.Default.CheckCircle else Icons.Default.Warning,
                                        contentDescription = null,
                                        tint = if (currentBranchIndex == 1) EcgGreen else if (currentBranchIndex == 2) AlertOrange else MedicalRedLight,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "KẾT CỤC LÂM SÀNG CA HIỆN TẠI: PHÂN NHÁNH $currentBranchIndex",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (currentBranchIndex == 1) EcgGreen else if (currentBranchIndex == 2) AlertOrange else MedicalRedLight
                                    )
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                val narrativeText = when (currentBranchIndex) {
                                    1 -> patientCase.successfulRescueNarrative
                                    3 -> "CẢNH BÁO NGUY HIỂM: Bệnh nhân rơi vào phân nhánh nguy kịch do y lệnh chống chỉ định gây trụy mạch và suy hô hấp cấp."
                                    else -> "Bệnh nhân tạm thời qua cơn nguy kịch ban đầu nhưng một số quyết định xử trí còn chậm trễ so với khung thời gian vàng Bộ Y Tế."
                                }
                                Text(
                                    text = narrativeText,
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

        Spacer(modifier = Modifier.height(10.dp))

        // ========================================================
        // MỤC 3: ĐỐI CHIẾU CHẨN ĐOÁN VỚI CHUẨN BỘ Y TẾ (ICD-10 CCMS)
        // ========================================================
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(10.dp),
            colors = CardDefaults.cardColors(containerColor = DarkSurface),
            border = androidx.compose.foundation.BorderStroke(1.dp, BorderSubtle)
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "ĐỐI CHIẾU CHẨN ĐOÁN LÂM SÀNG:",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = AlertOrange
                    )
                    Text(
                        text = Icd10Database.STANDARD_CODE,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        color = Spo2Cyan
                    )
                }
                Spacer(modifier = Modifier.height(6.dp))
                Text(text = "Chẩn đoán bạn đã gõ: \"$primaryDx\"", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Color(0xFFFEF08A))
                Spacer(modifier = Modifier.height(3.dp))
                Text(text = "Chẩn đoán chuẩn CCMS Bộ Y Tế: ${patientCase.goldenDiagnosis}", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = EcgGreen)
                Spacer(modifier = Modifier.height(3.dp))
                Text(text = "Bệnh cần loại trừ: ${patientCase.goldenDifferentials.joinToString(", ")}", fontSize = 10.sp, color = TextSecondary)
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // ========================================================
        // MỤC 4: BẢNG KÊ VIỆN PHÍ & VẬT TƯ TIÊU HAO (CÓ THỂ THU GỌN)
        // ========================================================
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { isFinancialExpanded = !isFinancialExpanded },
            shape = RoundedCornerShape(10.dp),
            colors = CardDefaults.cardColors(containerColor = DarkSurface),
            border = androidx.compose.foundation.BorderStroke(1.dp, BorderSubtle)
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.ReceiptLong, contentDescription = null, tint = Spo2Cyan, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "BẢNG KÊ VIỆN PHÍ (${vnFormat.format(totalHospitalBill)} đ)",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Spo2Cyan
                        )
                    }
                    Icon(
                        imageVector = if (isFinancialExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                        contentDescription = null,
                        tint = TextSecondary,
                        modifier = Modifier.size(18.dp)
                    )
                }

                AnimatedVisibility(visible = isFinancialExpanded) {
                    Column(modifier = Modifier.padding(top = 8.dp)) {
                        AccountingRow("Cận lâm sàng (${orderedLabIds.size} XN):", "${vnFormat.format(labCost)} đ")
                        AccountingRow("Thuốc & Xử trí chuyên khoa:", "${vnFormat.format(rxCost)} đ")
                        HorizontalDivider(color = BorderSubtle, modifier = Modifier.padding(vertical = 4.dp))
                        AccountingRow("TỔNG VIỆN PHÍ ƯỚC TÍNH:", "${vnFormat.format(totalHospitalBill)} đ", isBold = true, highlightColor = Color(0xFFFEF08A))
                        AccountingRow("• BHYT thanh toán (80%):", "-${vnFormat.format(insuranceCovered)} đ", highlightColor = EcgGreen)
                        AccountingRow("• Người bệnh đồng chi trả (20%):", "${vnFormat.format(patientCoPay)} đ", highlightColor = AlertOrange)

                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Vật tư tiêu hao: ${suppliesConsumed.take(5).joinToString(", ")}...",
                            fontSize = 10.sp,
                            color = TextSecondary
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // ========================================================
        // MỤC 5: ĐIỂM NHẤN LÂM SÀNG BỘ Y TẾ (CLINICAL PEARLS)
        // ========================================================
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(10.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF0C2438)),
            border = androidx.compose.foundation.BorderStroke(1.dp, Spo2Cyan.copy(alpha = 0.5f))
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.School, contentDescription = null, tint = Spo2Cyan, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "ĐIỂM NHẤN LÂM SÀNG BỘ Y TẾ:",
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

        Spacer(modifier = Modifier.height(16.dp))

        // Nút bàn giao xong, chuyển ca kế tiếp
        Button(
            onClick = onAcceptNextPatient,
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .testTag("btn_accept_next_patient"),
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MedicalPrimary,
                contentColor = Color.White
            )
        ) {
            Icon(Icons.Default.LocalHospital, contentDescription = null, modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "BÀN GIAO XONG • TIẾP NHẬN CA BỆNH KẾ TIẾP",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.width(6.dp))
            Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null, modifier = Modifier.size(16.dp))
        }
    }
}

@Composable
private fun VerdictDetailCard(
    pillarNumber: String,
    pillarTitle: String,
    doctorChoice: String,
    standardTarget: String,
    critique: String,
    isPass: Boolean,
    accentColor: Color
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(DarkSurfaceVariant)
            .border(1.dp, if (isPass) BorderSubtle else accentColor.copy(alpha = 0.5f), RoundedCornerShape(8.dp))
            .padding(10.dp)
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.weight(1f)) {
                    Box(
                        modifier = Modifier
                            .size(20.dp)
                            .clip(RoundedCornerShape(50))
                            .background(accentColor.copy(alpha = 0.2f))
                            .border(1.dp, accentColor, RoundedCornerShape(50)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = pillarNumber, fontSize = 10.sp, fontWeight = FontWeight.Bold, color = accentColor)
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = pillarTitle,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                }
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(4.dp))
                        .background(if (isPass) EcgGreen.copy(alpha = 0.15f) else AlertOrange.copy(alpha = 0.15f))
                        .border(0.8.dp, if (isPass) EcgGreen else AlertOrange, RoundedCornerShape(4.dp))
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = if (isPass) "PHÙ HỢP" else "CHƯA PHÙ HỢP",
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Black,
                        color = if (isPass) EcgGreen else AlertOrange
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))
            Text(text = "• Bác sĩ đã chọn: $doctorChoice", fontSize = 10.sp, color = TextPrimary)
            Spacer(modifier = Modifier.height(2.dp))
            Text(text = "• Chuẩn hướng dẫn: $standardTarget", fontSize = 10.sp, color = TextSecondary)
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "-> Đánh giá: $critique",
                fontSize = 10.sp,
                fontWeight = FontWeight.Medium,
                color = accentColor,
                lineHeight = 14.sp
            )
        }
    }
}

@Composable
private fun RubricGradeItem(
    range: String,
    label: String,
    isActive: Boolean,
    color: Color,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(6.dp),
        color = if (isActive) color.copy(alpha = 0.2f) else DarkSurfaceVariant,
        border = androidx.compose.foundation.BorderStroke(if (isActive) 1.5.dp else 0.5.dp, if (isActive) color else BorderSubtle)
    ) {
        Column(
            modifier = Modifier.padding(vertical = 4.dp, horizontal = 2.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = range,
                fontSize = 9.sp,
                fontFamily = FontFamily.Monospace,
                fontWeight = FontWeight.Black,
                color = if (isActive) color else TextSecondary
            )
            Text(
                text = label,
                fontSize = 8.sp,
                fontWeight = if (isActive) FontWeight.Bold else FontWeight.Normal,
                color = if (isActive) Color.White else TextMuted
            )
        }
    }
}

@Composable
private fun ScoringCriteriaRow(
    title: String,
    detail: String,
    score: String,
    progress: Float = 0f,
    badgeText: String? = null,
    isFull: Boolean = false,
    isNegative: Boolean = false,
    isBonus: Boolean = false
) {
    val scoreColor = when {
        isNegative -> MedicalRedLight
        isBonus -> Spo2Cyan
        isFull -> EcgGreen
        else -> Color(0xFFFEF08A)
    }

    Surface(
        shape = RoundedCornerShape(8.dp),
        color = DarkSurfaceVariant,
        border = androidx.compose.foundation.BorderStroke(0.5.dp, BorderSubtle),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = title,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary,
                    modifier = Modifier.weight(1f)
                )
                if (badgeText != null) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(scoreColor.copy(alpha = 0.18f))
                            .border(0.5.dp, scoreColor, RoundedCornerShape(4.dp))
                            .padding(horizontal = 5.dp, vertical = 1.dp)
                    ) {
                        Text(
                            text = badgeText,
                            fontSize = 8.sp,
                            fontWeight = FontWeight.Black,
                            color = scoreColor
                        )
                    }
                    Spacer(modifier = Modifier.width(6.dp))
                }
                Text(
                    text = score,
                    fontSize = 12.sp,
                    fontFamily = FontFamily.Monospace,
                    fontWeight = FontWeight.Black,
                    color = scoreColor
                )
            }
            Spacer(modifier = Modifier.height(2.dp))
            Text(text = detail, fontSize = 9.sp, color = TextSecondary, lineHeight = 12.sp)

            if (!isNegative && !isBonus) {
                Spacer(modifier = Modifier.height(4.dp))
                LinearProgressIndicator(
                    progress = { progress },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(3.dp)
                        .clip(RoundedCornerShape(2.dp)),
                    color = scoreColor,
                    trackColor = BorderSubtle.copy(alpha = 0.3f)
                )
            }
        }
    }
}

@Composable
private fun SurvivalBranchCard(
    branchNum: Int,
    title: String,
    conditions: String,
    outcome: String,
    isCurrent: Boolean,
    accentColor: Color
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(if (isCurrent) accentColor.copy(alpha = 0.12f) else DarkSurfaceVariant)
            .border(
                if (isCurrent) 1.5.dp else 0.5.dp,
                if (isCurrent) accentColor else BorderSubtle,
                RoundedCornerShape(8.dp)
            )
            .padding(8.dp)
    ) {
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = title,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isCurrent) accentColor else TextPrimary
                )
                if (isCurrent) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(accentColor)
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = "CA NÀY RƠI VÀO ĐÂY",
                            fontSize = 8.sp,
                            fontWeight = FontWeight.Black,
                            color = Color.Black
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(2.dp))
            Text(text = "• Điều kiện: $conditions", fontSize = 10.sp, color = TextSecondary, lineHeight = 13.sp)
            Spacer(modifier = Modifier.height(2.dp))
            Text(text = "• Kết cục: $outcome", fontSize = 10.sp, color = TextMuted, lineHeight = 13.sp)
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
        Text(text = label, fontSize = 10.sp, fontWeight = if (isBold) FontWeight.Bold else FontWeight.Normal, color = TextPrimary)
        Text(text = value, fontSize = 10.sp, fontFamily = FontFamily.Monospace, fontWeight = if (isBold) FontWeight.Black else FontWeight.Medium, color = highlightColor)
    }
}
