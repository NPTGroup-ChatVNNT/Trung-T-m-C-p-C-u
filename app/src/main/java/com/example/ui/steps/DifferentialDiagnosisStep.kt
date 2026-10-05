package com.example.ui.steps

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.LocalHospital
import androidx.compose.material.icons.filled.Search
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
import com.example.data.Icd10Database
import com.example.data.Icd10Item
import com.example.model.PatientCase
import com.example.ui.theme.*

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun DifferentialDiagnosisStep(
    patientCase: PatientCase,
    primaryDx: String,
    onPrimaryDxChange: (String) -> Unit,
    diffDxList: List<String> = emptyList(),
    onAddDiffDx: (String) -> Unit = {},
    onRemoveDiffDx: (Int) -> Unit = {},
    reasoningText: String = "",
    onReasoningChange: (String) -> Unit = {},
    aiComment: String?,
    isAiAnalyzing: Boolean,
    onConsultAi: () -> Unit,
    onProceedToNextStep: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isReady = primaryDx.trim().length >= 3

    // State cho ô tìm kiếm tra cứu mã ICD-10
    var searchQuery by remember { mutableStateOf("") }
    var showAllSuggestions by remember { mutableStateOf(false) }

    // Tự động phân tích danh sách các mã ICD-10 đang được chọn từ chuỗi primaryDx
    val selectedCodes = remember(primaryDx) {
        Icd10Database.parseMultipleCodes(primaryDx)
    }

    // Tra cứu đề xuất thời gian thực dựa trên searchQuery hoặc từ khóa chẩn đoán
    val searchResults = remember(searchQuery) {
        if (searchQuery.isBlank()) {
            Icd10Database.allCodes.take(8)
        } else {
            Icd10Database.search(searchQuery)
        }
    }

    // Hàm tiện ích: Bật/Tắt chọn mã ICD-10 (hỗ trợ chọn nhiều mã cùng lúc)
    fun toggleCodeSelection(item: Icd10Item) {
        val alreadySelected = selectedCodes.any { it.code.equals(item.code, ignoreCase = true) }
        val updatedList = if (alreadySelected) {
            selectedCodes.filterNot { it.code.equals(item.code, ignoreCase = true) }
        } else {
            selectedCodes + item
        }

        if (updatedList.isEmpty()) {
            onPrimaryDxChange("")
        } else {
            val formatted = Icd10Database.formatMultipleCodes(updatedList)
            onPrimaryDxChange(formatted)
        }
    }

    // Hàm tiện ích: Thêm mã trực tiếp từ chuỗi nhập liền kề nhiều mã
    fun addAdjacentCodes(input: String) {
        val parsed = Icd10Database.parseMultipleCodes(input)
        if (parsed.isNotEmpty()) {
            val currentCodes = selectedCodes.toMutableList()
            parsed.forEach { p ->
                if (currentCodes.none { it.code.equals(p.code, ignoreCase = true) }) {
                    currentCodes.add(p)
                }
            }
            onPrimaryDxChange(Icd10Database.formatMultipleCodes(currentCodes))
            searchQuery = ""
        }
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(12.dp)
    ) {
        // =========================================================================
        // PHẦN: CHẨN ĐOÁN XÁC ĐỊNH & MÃ HÓA ICD-10 (CHO PHÉP CHỌN & NHẬP NHIỀU MÃ)
        // =========================================================================
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
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.LocalHospital,
                            contentDescription = null,
                            tint = MedicalPrimaryLight,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "CHẨN ĐOÁN XÁC ĐỊNH & MÃ HÓA ICD-10",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Black,
                            color = MedicalPrimaryLight
                        )
                    }
                    if (selectedCodes.isNotEmpty()) {
                        Text(
                            text = "Đã chọn: ${selectedCodes.size} mã",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = EcgGreen
                        )
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Cho phép chọn nhiều mã kết hợp (bệnh chính, biến chứng, kèm theo) hoặc gõ liền kề (VD: I21, I49 hoặc S06.4, S27.1).",
                    fontSize = 10.sp,
                    color = TextSecondary,
                    lineHeight = 14.sp
                )

                // HIỂN THỊ CÁC MÃ ĐÃ CHỌN DƯỚI DẠNG CHIPS TƯƠNG TÁC
                if (selectedCodes.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Danh sách mã ICD-10 đã chọn:",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    FlowRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        selectedCodes.forEach { item ->
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = MedicalPrimary.copy(alpha = 0.25f),
                                border = androidx.compose.foundation.BorderStroke(1.dp, MedicalPrimaryLight)
                            ) {
                                Row(
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = item.code,
                                        fontSize = 10.sp,
                                        fontFamily = FontFamily.Monospace,
                                        fontWeight = FontWeight.Black,
                                        color = Spo2Cyan
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = item.nameVi,
                                        fontSize = 10.sp,
                                        color = Color.White,
                                        maxLines = 1
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Icon(
                                        imageVector = Icons.Default.Close,
                                        contentDescription = "Xóa mã",
                                        tint = AlertOrange,
                                        modifier = Modifier
                                            .size(14.dp)
                                            .clickable { toggleCodeSelection(item) }
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Ô NHẬP VĂN BẢN CHẨN ĐOÁN CHÍNH (ĐẦY ĐỦ)
                Text(
                    text = "Văn bản chẩn đoán vào viện:",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = TextSecondary
                )
                Spacer(modifier = Modifier.height(4.dp))
                OutlinedTextField(
                    value = primaryDx,
                    onValueChange = onPrimaryDxChange,
                    placeholder = {
                        Text(
                            text = "Nhập văn bản chẩn đoán hoặc chọn mã bên dưới...",
                            fontSize = 11.sp,
                            color = TextSecondary
                        )
                    },
                    singleLine = false,
                    maxLines = 3,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("input_primary_diagnosis"),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MedicalPrimaryLight,
                        unfocusedBorderColor = BorderSubtle,
                        focusedContainerColor = DarkSurfaceVariant,
                        unfocusedContainerColor = DarkSurfaceVariant
                    )
                )

                // Ô TRA CỨU & NHẬP NHANH MÃ ICD-10 (TÌM MÃ KHÔNG GIỚI HẠN)
                Spacer(modifier = Modifier.height(10.dp))
                Text(
                    text = "Tra cứu hoặc nhập liền kề nhiều mã (VD: I21.0, I49.0, S06, T78):",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = Spo2Cyan
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        placeholder = {
                            Text(
                                text = "Gõ mã (I21, S06...) hoặc tên bệnh không dấu (nhoi mau, phan ve, tran khi)...",
                                fontSize = 11.sp,
                                color = TextSecondary
                            )
                        },
                        singleLine = true,
                        leadingIcon = {
                            Icon(
                                Icons.Default.Search,
                                contentDescription = null,
                                tint = MedicalPrimaryLight,
                                modifier = Modifier.size(16.dp)
                            )
                        },
                        trailingIcon = {
                            if (searchQuery.isNotBlank()) {
                                IconButton(onClick = { searchQuery = "" }) {
                                    Icon(
                                        Icons.Default.Close,
                                        contentDescription = "Xóa tìm kiếm",
                                        tint = TextSecondary,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            }
                        },
                        modifier = Modifier
                            .weight(1f)
                            .testTag("input_search_icd10"),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Spo2Cyan,
                            unfocusedBorderColor = BorderSubtle,
                            focusedContainerColor = DarkSurfaceVariant,
                            unfocusedContainerColor = DarkSurfaceVariant
                        )
                    )

                    // Nút Thêm nhanh nếu người dùng gõ chuỗi nhiều mã trong ô tìm kiếm
                    if (searchQuery.contains(",") || searchQuery.contains(";") || searchQuery.contains(" ")) {
                        Spacer(modifier = Modifier.width(6.dp))
                        Button(
                            onClick = { addAdjacentCodes(searchQuery) },
                            colors = ButtonDefaults.buttonColors(containerColor = ScrubBlueDark),
                            shape = RoundedCornerShape(6.dp),
                            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp)
                        ) {
                            Text("Thêm", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }

                // DANH SÁCH GỢI Ý & TRA CỨU ĐỀ XUẤT MÃ ICD-10
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (searchQuery.isBlank()) "Đề xuất cấp cứu thường gặp:" else "Kết quả tra cứu (${searchResults.size}):",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextSecondary
                    )
                    TextButton(
                        onClick = { showAllSuggestions = !showAllSuggestions },
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        Text(
                            text = if (showAllSuggestions) "Thu gọn" else "Xem thêm mã",
                            fontSize = 10.sp,
                            color = MedicalPrimaryLight
                        )
                    }
                }

                // Danh sách item đề xuất
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 2.dp)
                ) {
                    val displayList = searchResults.take(if (showAllSuggestions) 10 else 4)

                    displayList.forEach { item ->
                        val isSelected = selectedCodes.any { it.code.equals(item.code, ignoreCase = true) }

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 2.dp)
                                .clip(RoundedCornerShape(6.dp))
                                .background(if (isSelected) MedicalPrimary.copy(alpha = 0.22f) else DarkSurfaceVariant)
                                .border(
                                    0.8.dp,
                                    if (isSelected) MedicalPrimaryLight else BorderSubtle,
                                    RoundedCornerShape(6.dp)
                                )
                                .clickable { toggleCodeSelection(item) }
                                .padding(horizontal = 8.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Badge Mã ICD-10
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(4.dp))
                                    .background(if (isSelected) EcgGreen else MedicalPrimary)
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = item.code,
                                    fontSize = 10.sp,
                                    fontFamily = FontFamily.Monospace,
                                    fontWeight = FontWeight.Black,
                                    color = Color.White
                                )
                            }

                            Spacer(modifier = Modifier.width(8.dp))

                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = item.nameVi,
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isSelected) Color(0xFFFEF08A) else TextPrimary,
                                    maxLines = 1
                                )
                                Text(
                                    text = "${item.chapter} • ${item.emrGroup}",
                                    fontSize = 9.sp,
                                    color = TextSecondary,
                                    maxLines = 1
                                )
                            }

                            if (isSelected) {
                                Icon(
                                    Icons.Default.Check,
                                    contentDescription = "Đã chọn",
                                    tint = EcgGreen,
                                    modifier = Modifier.size(16.dp)
                                )
                            } else {
                                Icon(
                                    Icons.Default.Add,
                                    contentDescription = "Chọn mã này",
                                    tint = TextSecondary,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }
                    }

                    // Nếu người dùng nhập mã chưa có trong kết quả nhanh nhưng thuộc khung ICD-10 chuẩn
                    val verifiedOfficialItem = remember(searchQuery) {
                        if (searchQuery.trim().length >= 3) {
                            Icd10Database.getByCode(searchQuery) ?: Icd10Database.resolveOfficialIcd10(searchQuery)
                        } else null
                    }

                    if (verifiedOfficialItem != null && searchResults.none { it.code.equals(verifiedOfficialItem.code, ignoreCase = true) }) {
                        Spacer(modifier = Modifier.height(4.dp))
                        OutlinedButton(
                            onClick = {
                                toggleCodeSelection(verifiedOfficialItem)
                                searchQuery = ""
                            },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(6.dp),
                            contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Icon(Icons.Default.Add, contentDescription = null, tint = Spo2Cyan, modifier = Modifier.size(14.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Thêm mã chuẩn ICD-10: [${verifiedOfficialItem.code}] ${verifiedOfficialItem.nameVi}",
                                fontSize = 10.sp,
                                color = Spo2Cyan,
                                fontWeight = FontWeight.Bold,
                                maxLines = 1
                            )
                        }
                    } else if (searchQuery.isNotBlank() && searchResults.isEmpty() && verifiedOfficialItem == null) {
                        Spacer(modifier = Modifier.height(6.dp))
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(6.dp))
                                .background(DarkSurfaceVariant)
                                .border(0.5.dp, BorderSubtle, RoundedCornerShape(6.dp))
                                .padding(8.dp)
                        ) {
                            Text(
                                text = "Mã \"${searchQuery.trim()}\" không thuộc khung mã chuẩn ICD-10 của WHO & Bộ Y Tế. Vui lòng nhập đúng mã danh mục (VD: I21, J45, K35, M54, S06) hoặc tìm kiếm theo tên bệnh / triệu chứng lâm sàng.",
                                fontSize = 10.sp,
                                color = AlertOrange,
                                lineHeight = 13.sp
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // =========================================================================
        // PHẦN 2: CHẨN ĐOÁN PHÂN BIỆT (DIFF DX)
        // =========================================================================
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(10.dp),
            colors = CardDefaults.cardColors(containerColor = DarkSurface),
            border = androidx.compose.foundation.BorderStroke(1.dp, BorderSubtle)
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = "CHẨN ĐOÁN PHÂN BIỆT (CÁC KHẢ NĂNG CẦN LOẠI TRỪ):",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Thêm các bệnh danh phân biệt cần loại trừ trong cấp cứu:",
                    fontSize = 10.sp,
                    color = TextSecondary
                )
                Spacer(modifier = Modifier.height(6.dp))

                var newDiffDxInput by remember { mutableStateOf("") }
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = newDiffDxInput,
                        onValueChange = { newDiffDxInput = it },
                        placeholder = { Text("Nhập chẩn đoán phân biệt...", fontSize = 11.sp, color = TextSecondary) },
                        singleLine = true,
                        modifier = Modifier.weight(1f),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = MedicalPrimaryLight,
                            unfocusedBorderColor = BorderSubtle,
                            focusedContainerColor = DarkSurfaceVariant,
                            unfocusedContainerColor = DarkSurfaceVariant
                        )
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Button(
                        onClick = {
                            if (newDiffDxInput.isNotBlank()) {
                                onAddDiffDx(newDiffDxInput.trim())
                                newDiffDxInput = ""
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = MedicalPrimary),
                        shape = RoundedCornerShape(6.dp),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Text("Thêm", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }

                if (diffDxList.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(6.dp))
                    diffDxList.forEachIndexed { index, dx ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 2.dp)
                                .clip(RoundedCornerShape(4.dp))
                                .background(DarkSurfaceVariant)
                                .padding(horizontal = 8.dp, vertical = 4.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(text = "• $dx", fontSize = 11.sp, color = TextPrimary)
                            IconButton(
                                onClick = { onRemoveDiffDx(index) },
                                modifier = Modifier.size(20.dp)
                            ) {
                                Icon(Icons.Default.Close, contentDescription = "Xóa", tint = AlertOrange, modifier = Modifier.size(14.dp))
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // =========================================================================
        // PHẦN 3: HỘI CHẨN BÁC SĨ TRƯỞNG KHOA
        // =========================================================================
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(10.dp),
            colors = CardDefaults.cardColors(containerColor = DarkSurface),
            border = androidx.compose.foundation.BorderStroke(1.dp, BorderSubtle)
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = "HỘI CHẨN BÁC SĨ TRƯỞNG KHOA:",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = Spo2Cyan
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Bác sĩ Trưởng khoa Cấp cứu sẽ góp ý chuyên môn, rà soát các mã ICD-10 và nhắc nhở an toàn.",
                    fontSize = 10.sp,
                    color = TextSecondary
                )
                Spacer(modifier = Modifier.height(8.dp))

                Button(
                    onClick = onConsultAi,
                    enabled = isReady && !isAiAnalyzing,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(46.dp)
                        .testTag("btn_consult_chief_doctor"),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = ScrubBlueDark,
                        contentColor = Color.White
                    )
                ) {
                    if (isAiAnalyzing) {
                        CircularProgressIndicator(modifier = Modifier.size(16.dp), color = Color.White, strokeWidth = 2.dp)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("BÁC SĨ TRƯỞNG KHOA ĐANG HỘI CHẨN...", fontSize = 11.sp)
                    } else {
                        Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = Color(0xFFFEF08A), modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("HỘI CHẨN BÁC SĨ TRƯỞNG KHOA", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                }

                // Nhận xét của Bác sĩ Trưởng khoa nếu có
                if (aiComment != null) {
                    Spacer(modifier = Modifier.height(10.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xFF06182E))
                            .border(1.dp, MedicalPrimaryLight.copy(alpha = 0.5f), RoundedCornerShape(8.dp))
                            .padding(10.dp)
                    ) {
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = Spo2Cyan, modifier = Modifier.size(15.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Ý KIẾN BÁC SĨ TRƯỞNG KHOA:", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Spo2Cyan)
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(text = aiComment, fontSize = 11.sp, color = TextPrimary, lineHeight = 15.sp)
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Nút chuyển tiếp sang Xử trí cấp cứu ban đầu
        Button(
            onClick = onProceedToNextStep,
            enabled = isReady,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .testTag("btn_proceed_to_interventions"),
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MedicalPrimary,
                disabledContainerColor = DarkSurfaceVariant,
                contentColor = Color.White,
                disabledContentColor = TextSecondary
            )
        ) {
            Text(
                text = if (isReady) "TIẾP TỤC: XỬ TRÍ BAN ĐẦU" else "HÃY CHỌN MÃ ICD-10 ĐỂ TIẾP TỤC",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold
            )
            if (isReady) {
                Spacer(modifier = Modifier.width(6.dp))
                Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null, modifier = Modifier.size(18.dp))
            }
        }
    }
}
