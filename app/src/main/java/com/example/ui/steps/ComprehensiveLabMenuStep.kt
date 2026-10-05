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
import com.example.model.LabTestItem
import com.example.model.PatientCase
import com.example.ui.theme.*
import java.text.NumberFormat
import java.util.Locale

@Composable
fun ComprehensiveLabMenuStep(
    patientCase: PatientCase,
    orderedLabIds: Set<String>,
    onToggleLab: (String) -> Unit,
    onProceedToNextStep: () -> Unit,
    modifier: Modifier = Modifier
) {
    val vnFormat = remember { NumberFormat.getNumberInstance(Locale("vi", "VN")) }
    var selectedCategoryFilter by remember { mutableStateOf("TẤT CẢ") }
    var searchQuery by remember { mutableStateOf("") }

    val categories = remember {
        listOf("TẤT CẢ", "Huyết học - Đông máu", "Sinh hóa - Miễn dịch", "Khí máu - Hồi sức", "Chẩn đoán hình ảnh", "Thăm dò chức năng", "Độc chất")
    }

    // Filtered labs
    val filteredLabs = remember(selectedCategoryFilter, searchQuery, patientCase.availableLabs) {
        patientCase.availableLabs.filter { item ->
            val matchCat = when (selectedCategoryFilter) {
                "TẤT CẢ" -> true
                "Huyết học - Đông máu" -> item.category.contains("Huyết học") || item.category.contains("Đông máu")
                "Sinh hóa - Miễn dịch" -> item.category.contains("Sinh hóa") || item.category.contains("Miễn dịch")
                "Khí máu - Hồi sức" -> item.category.contains("Khí máu") || item.category.contains("Hồi sức")
                "Chẩn đoán hình ảnh" -> item.category.contains("Chẩn đoán hình ảnh") || item.category.contains("hình ảnh")
                "Thăm dò chức năng" -> item.category.contains("Thăm dò") || item.category.contains("E-FAST") || item.category.contains("ECG")
                "Độc chất" -> item.category.contains("Độc chất") || item.category.contains("nước tiểu")
                else -> item.category.contains(selectedCategoryFilter)
            }
            val matchSearch = searchQuery.isBlank() || item.name.contains(searchQuery, ignoreCase = true) || item.category.contains(searchQuery, ignoreCase = true)
            matchCat && matchSearch
        }
    }

    val totalCost = remember(orderedLabIds, patientCase.availableLabs) {
        patientCase.availableLabs.filter { it.id in orderedLabIds }.sumOf { it.costVnd }
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(12.dp)
    ) {
        // TIÊU ĐỀ GỌN GÀNG (Xóa bỏ chỉ mục "bước 2" để tiết kiệm diện tích)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "CHỈ ĐỊNH CẬN LÂM SÀNG",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Black,
                    color = MedicalPrimaryLight
                )
                Text(
                    text = "Tham chiếu mã chuẩn lâm sàng",
                    fontSize = 10.sp,
                    color = Spo2Cyan,
                    fontWeight = FontWeight.Bold
                )
            }

            // Đã chỉ định & Viện phí cận lâm sàng
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = "Đã chọn: ${orderedLabIds.size} xét nghiệm",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = Spo2Cyan
                )
                Text(
                    text = "${vnFormat.format(totalCost)} đ",
                    fontSize = 11.sp,
                    fontFamily = FontFamily.Monospace,
                    color = Color(0xFFFEF08A)
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Thanh tìm kiếm nhanh
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            placeholder = { Text("Tìm xét nghiệm (ECG, Troponin, Khí máu, X-quang, CT...)", fontSize = 11.sp, color = TextSecondary) },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = TextSecondary, modifier = Modifier.size(16.dp)) },
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .height(46.dp)
                .testTag("input_search_lab"),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MedicalPrimaryLight,
                unfocusedBorderColor = BorderSubtle,
                focusedContainerColor = DarkSurface,
                unfocusedContainerColor = DarkSurface
            )
        )

        Spacer(modifier = Modifier.height(6.dp))

        // Danh mục nhóm lớn (Gôm gọn theo chuyên mục để thao tác nhanh)
        ScrollableTabRow(
            selectedTabIndex = categories.indexOf(selectedCategoryFilter).coerceAtLeast(0),
            containerColor = Color.Transparent,
            edgePadding = 0.dp,
            divider = {}
        ) {
            categories.forEach { cat ->
                Tab(
                    selected = selectedCategoryFilter == cat,
                    onClick = { selectedCategoryFilter = cat },
                    text = {
                        Text(
                            text = cat,
                            fontSize = 11.sp,
                            fontWeight = if (selectedCategoryFilter == cat) FontWeight.Bold else FontWeight.Normal,
                            color = if (selectedCategoryFilter == cat) MedicalPrimaryLight else TextSecondary
                        )
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Danh sách các xét nghiệm cận lâm sàng
        filteredLabs.forEach { lab ->
            val isOrdered = lab.id in orderedLabIds

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 3.dp)
                    .clickable { onToggleLab(lab.id) }
                    .testTag("lab_item_${lab.id}"),
                shape = RoundedCornerShape(8.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (isOrdered) DarkSurfaceVariant else DarkSurface
                ),
                border = androidx.compose.foundation.BorderStroke(
                    1.dp,
                    if (isOrdered) Spo2Cyan.copy(alpha = 0.6f) else BorderSubtle
                )
            ) {
                Column(modifier = Modifier.padding(10.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = lab.name,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                            val (serviceCode, icd10Ref) = getLabIcd10Reference(lab.id)
                            Spacer(modifier = Modifier.height(2.dp))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(3.dp))
                                        .background(MedicalPrimaryDark)
                                        .padding(horizontal = 4.dp, vertical = 1.dp)
                                ) {
                                    Text(
                                        text = "Mã DV: $serviceCode",
                                        fontSize = 8.sp,
                                        fontFamily = FontFamily.Monospace,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )
                                }
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "ICD-10 CCMS: $icd10Ref",
                                    fontSize = 9.sp,
                                    color = Spo2Cyan,
                                    maxLines = 1
                                )
                            }
                            Spacer(modifier = Modifier.height(2.dp))
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = lab.category,
                                    fontSize = 10.sp,
                                    color = TextSecondary
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "• ${vnFormat.format(lab.costVnd)} đ",
                                    fontSize = 10.sp,
                                    fontFamily = FontFamily.Monospace,
                                    color = Color(0xFFFEF08A)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "• ${lab.waitTimeMinutes} phút",
                                    fontSize = 10.sp,
                                    color = TextSecondary
                                )
                            }
                        }

                        Button(
                            onClick = { onToggleLab(lab.id) },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (isOrdered) ScrubTealDark else DarkSurfaceVariant,
                                contentColor = Color.White
                            ),
                            shape = RoundedCornerShape(6.dp),
                            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                            modifier = Modifier.testTag("btn_order_${lab.id}")
                        ) {
                            Text(
                                text = if (isOrdered) "HỦY" else "CHỈ ĐỊNH",
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    // KHI ĐÃ CHỈ ĐỊNH: HIỂN THỊ PHIẾU KẾT QUẢ KHÁCH QUAN
                    // (Bác sĩ tự đọc số liệu & hình ảnh, KHÔNG có nhãn "Bất thường" hay "Bình thường")
                    if (isOrdered) {
                        Spacer(modifier = Modifier.height(8.dp))
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(6.dp))
                                .background(Color(0xFF0F172A))
                                .border(1.dp, BorderSubtle, RoundedCornerShape(6.dp))
                                .padding(10.dp)
                        ) {
                            Column {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            imageVector = Icons.Default.Description,
                                            contentDescription = null,
                                            tint = Spo2Cyan,
                                            modifier = Modifier.size(14.dp)
                                        )
                                        Spacer(modifier = Modifier.width(5.dp))
                                        Text(
                                            text = "PHIẾU KẾT QUẢ",
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = Spo2Cyan
                                        )
                                    }

                                    Text(
                                        text = "Khoảng tham chiếu: ${lab.refRange}",
                                        fontSize = 10.sp,
                                        color = TextSecondary
                                    )
                                }

                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = lab.resultDetailed,
                                    fontSize = 11.sp,
                                    color = TextPrimary,
                                    lineHeight = 15.sp,
                                    fontFamily = FontFamily.Monospace
                                )
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Nút chuyển bước gọn gàng (Loại bỏ "bước 3: ...")
        Button(
            onClick = onProceedToNextStep,
            enabled = orderedLabIds.isNotEmpty(),
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .testTag("btn_proceed_to_diagnosis"),
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MedicalPrimary,
                disabledContainerColor = DarkSurfaceVariant,
                contentColor = Color.White,
                disabledContentColor = TextSecondary
            )
        ) {
            Text(
                text = if (orderedLabIds.isNotEmpty()) "TIẾP TỤC: CHẨN ĐOÁN" else "HÃY CHỈ ĐỊNH ÍT NHẤT 1 XÉT NGHIỆM ĐỂ TIẾP TỤC",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold
            )
            if (orderedLabIds.isNotEmpty()) {
                Spacer(modifier = Modifier.width(6.dp))
                Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null, modifier = Modifier.size(18.dp))
            }
        }
    }
}

private fun getLabIcd10Reference(labId: String): Pair<String, String> {
    return when (labId) {
        "lab_ecg" -> "03.0012.01" to "I20-I25, I44-I49 (Bệnh tim mạch)"
        "lab_trop" -> "04.0256.03" to "I21, I20.0 (Nhồi máu cơ tim, Đau thắt ngực)"
        "lab_abg" -> "04.0189.02" to "J96, E11.0, R57 (Suy hô hấp, Toan kiềm)"
        "lab_lactate" -> "04.0195.01" to "R57, A41.9 (Sốc, Nhiễm khuẩn huyết)"
        "lab_cbc" -> "01.0023.01" to "D72, D64, J18 (Nhiễm trùng, Mất máu)"
        "lab_coag" -> "01.0115.02" to "D68, I63, K25 (Rối loạn đông máu, Đột quỵ)"
        "lab_ddimer" -> "01.0128.01" to "I26, I80, I71 (Huyết khối, Thuyên tắc phổi)"
        "lab_lytes" -> "04.0102.01" to "E87, I50, N17 (Rối loạn điện giải)"
        "lab_liver_panc" -> "04.0135.02" to "K85, K72 (Viêm tụy cấp, Tổn thương gan)"
        "lab_renal" -> "04.0118.01" to "N17, N18, I10 (Suy thận cấp, Tăng huyết áp)"
        "lab_sugar" -> "04.0089.01" to "E11, E16.2 (Đái tháo đường, Hạ đường huyết)"
        "lab_che" -> "04.0289.01" to "T60.0 (Ngộ độc Phospho hữu cơ)"
        "lab_pct" -> "04.0298.01" to "A41.9, R57.2 (Nhiễm khuẩn huyết nặng)"
        "lab_xray_chest" -> "02.0045.01" to "J93, J18, I50.1 (Tràn khí, Viêm phổi, Phù phổi)"
        "lab_xray_abd" -> "02.0051.01" to "K25.5, K56 (Thủng tạng rỗng, Tắc ruột)"
        "lab_efast" -> "02.0112.04" to "S36, T79, R57 (Tràn dịch/khí chấn thương cấp)"
        "lab_focus_echo" -> "02.0125.01" to "I21, I50, R57.0 (Chức năng tim, Rối loạn vận động)"
        "lab_ct_brain" -> "02.0201.01" to "I63, I61, G40 (Đột quỵ não, Xuất huyết sọ)"
        "lab_ct_chest" -> "02.0225.02" to "I71.0, I26.0 (Bóc tách ĐMC, Thuyên tắc phổi)"
        "lab_ct_abd" -> "02.0232.02" to "K85, K35, K25.5 (Viêm tụy, Ruột thừa, Thủng tạng)"
        "lab_blood_cult" -> "05.0010.01" to "A41, R57.2 (Cấy máu tìm vi khuẩn)"
        "lab_urinalysis" -> "04.0045.01" to "N39, N20, E11 (Nhiễm trùng tiểu, Sỏi niệu)"
        "lab_drug_screen" -> "04.0315.01" to "T50, T60, F19 (Test nhanh ngộ độc chất)"
        else -> "04.0001.01" to "R69 (Cận lâm sàng cấp cứu)"
    }
}
