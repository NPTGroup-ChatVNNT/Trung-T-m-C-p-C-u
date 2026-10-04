package com.example.ui.steps

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
            .padding(14.dp)
    ) {
        // Section Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "BƯỚC 2: CHỈ ĐỊNH CẬN LÂM SÀNG TOÀN VIỆN",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Black,
                    color = MedicalPrimaryLight
                )
                Text(
                    text = "Bác sĩ tự do chỉ định theo phán đoán lâm sàng • Mọi quyết định đều có hệ quả",
                    fontSize = 11.sp,
                    color = TextSecondary
                )
            }

            // Ordered count & cost badge
            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = "Đã chỉ định: ${orderedLabIds.size} XN",
                    fontSize = 12.sp,
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

        Spacer(modifier = Modifier.height(10.dp))

        // Search Bar
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            placeholder = { Text("Tìm tên xét nghiệm (ECG, Troponin, ABG, CT, X-quang...)", fontSize = 12.sp, color = TextSecondary) },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = TextSecondary) },
            singleLine = true,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = MedicalPrimaryLight,
                unfocusedBorderColor = BorderSubtle,
                focusedContainerColor = DarkSurface,
                unfocusedContainerColor = DarkSurface
            )
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Category filter chips scrollable
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

        Spacer(modifier = Modifier.height(10.dp))

        // List of 23 Lab Tests
        filteredLabs.forEach { lab ->
            val isOrdered = lab.id in orderedLabIds

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                    .clickable { onToggleLab(lab.id) },
                shape = RoundedCornerShape(10.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (isOrdered) DarkSurfaceVariant else DarkSurface
                ),
                border = androidx.compose.foundation.BorderStroke(
                    1.dp,
                    if (isOrdered) if (lab.isCritical) MedicalRed else Spo2Cyan else BorderSubtle
                )
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = lab.name,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = TextPrimary
                            )
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = lab.category,
                                    fontSize = 10.sp,
                                    color = TextSecondary
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "• ${vnFormat.format(lab.costVnd)} VNĐ",
                                    fontSize = 10.sp,
                                    fontFamily = FontFamily.Monospace,
                                    color = Color(0xFFFEF08A)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "• Chờ ${lab.waitTimeMinutes}p",
                                    fontSize = 10.sp,
                                    color = TextSecondary
                                )
                            }
                        }

                        Button(
                            onClick = { onToggleLab(lab.id) },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (isOrdered) MedicalPrimary else DarkSurfaceVariant,
                                contentColor = Color.White
                            ),
                            shape = RoundedCornerShape(8.dp),
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = if (isOrdered) "HỦY" else "CHỈ ĐỊNH",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    // If ordered, show patient test result in detail!
                    if (isOrdered) {
                        Spacer(modifier = Modifier.height(10.dp))
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .background(if (lab.isCritical) MedicalRed.copy(alpha = 0.15f) else Color(0xFF042F2E).copy(alpha = 0.4f))
                                .border(
                                    1.dp,
                                    if (lab.isCritical) MedicalRed.copy(alpha = 0.6f) else MedicalPrimary,
                                    RoundedCornerShape(8.dp)
                                )
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
                                            imageVector = if (lab.isCritical) Icons.Default.Warning else Icons.Default.CheckCircle,
                                            contentDescription = null,
                                            tint = if (lab.isCritical) MedicalRedLight else EcgGreen,
                                            modifier = Modifier.size(16.dp)
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(
                                            text = "KẾT QUẢ: ${lab.resultShort}",
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = if (lab.isCritical) MedicalRedLight else EcgGreen
                                        )
                                    }

                                    Text(
                                        text = "Tham chiếu: ${lab.refRange}",
                                        fontSize = 10.sp,
                                        color = TextSecondary
                                    )
                                }

                                Spacer(modifier = Modifier.height(6.dp))
                                Text(
                                    text = lab.resultDetailed,
                                    fontSize = 12.sp,
                                    color = TextPrimary,
                                    lineHeight = 16.sp,
                                    fontFamily = FontFamily.Monospace
                                )
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // Proceed Button
        Button(
            onClick = onProceedToNextStep,
            enabled = orderedLabIds.isNotEmpty(),
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MedicalPrimary,
                disabledContainerColor = DarkSurfaceVariant,
                contentColor = Color.White,
                disabledContentColor = TextSecondary
            )
        ) {
            Text(
                text = if (orderedLabIds.isNotEmpty()) "XÁC NHẬN KẾT QUẢ -> BƯỚC 3: TỰ NHẬP CHẨN ĐOÁN" else "HÃY CHỈ ĐỊNH ÍT NHẤT 1 XÉT NGHIỆM ĐỂ TIẾP TỤC",
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
