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
import com.example.data.MasterHospitalOrders
import com.example.model.EmergencyOrder
import com.example.model.OrderCategory
import com.example.model.PatientCase
import com.example.ui.theme.*
import java.text.NumberFormat
import java.util.Locale

@Composable
fun EmergencyInterventionsStep(
    patientCase: PatientCase,
    executedOrderIds: Set<String>,
    onExecuteOrder: (EmergencyOrder) -> Unit,
    onOpenDefibrillator: () -> Unit,
    onOpenCpr: () -> Unit,
    isCardiacArrest: Boolean,
    isRescued: Boolean,
    onProceedToNextStep: () -> Unit,
    modifier: Modifier = Modifier
) {
    val vnFormat = remember { NumberFormat.getNumberInstance(Locale("vi", "VN")) }
    // 0 = Xử Trí Ban Đầu (Emergency Initial Resuscitation)
    // 1 = Điều Trị Chuyên Khoa (Specialized Definitive Treatment & Department Transfer)
    var activeMainSection by remember { mutableStateOf(0) }
    var subCategoryInitial by remember { mutableStateOf<OrderCategory?>(null) } // null = Tất cả

    // Combine Master Hospital Orders with any case-specific overrides
    val allOrders = remember(patientCase) {
        val masterList = MasterHospitalOrders.getAllMasterOrders()
        val caseMap = patientCase.standardOrders.associateBy { it.id }
        val merged = masterList.map { caseMap[it.id] ?: it }
        val extraCaseOrders = patientCase.standardOrders.filter { co -> masterList.none { it.id == co.id } }
        merged + extraCaseOrders
    }

    // Orders categorized into the 2 requested major divisions:
    val initialOrders = remember(allOrders) {
        allOrders.filter { it.category != OrderCategory.CONSULTATION }
    }
    val specialtyOrders = remember(allOrders) {
        allOrders.filter { it.category == OrderCategory.CONSULTATION }
    }

    val totalRxCost = remember(executedOrderIds, allOrders) {
        allOrders.filter { it.id in executedOrderIds }.sumOf { it.costVnd }
    }

    val initialExecutedCount = initialOrders.count { it.id in executedOrderIds }
    val specialtyExecutedCount = specialtyOrders.count { it.id in executedOrderIds }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(12.dp)
    ) {
        // TIÊU ĐỀ GỌN GÀNG (Xóa bỏ chỉ mục "bước 4" để tiết kiệm diện tích)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "XỬ TRÍ CẤP CỨU & ĐIỀU TRỊ",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Black,
                    color = MedicalPrimaryLight
                )
                Text(
                    text = "Phân biệt rõ xử trí ban đầu và điều trị chuyển khoa chuyên môn",
                    fontSize = 10.sp,
                    color = TextSecondary
                )
            }

            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = "Đã ra: ${executedOrderIds.size} y lệnh",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFFFEF08A)
                )
                Text(
                    text = "${vnFormat.format(totalRxCost)} đ",
                    fontSize = 11.sp,
                    fontFamily = FontFamily.Monospace,
                    color = Spo2Cyan
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // TÁCH THÀNH 2 MỤC LỚN RÕ RỆT (THEO YÊU CẦU CỦA BÁC SĨ)
        TabRow(
            selectedTabIndex = activeMainSection,
            containerColor = DarkSurface,
            contentColor = TextPrimary,
            divider = { HorizontalDivider(color = BorderSubtle) }
        ) {
            Tab(
                selected = activeMainSection == 0,
                onClick = { activeMainSection = 0 },
                text = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Default.Healing,
                            contentDescription = null,
                            tint = if (activeMainSection == 0) Spo2Cyan else TextSecondary,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Xử Trí Ban Đầu ($initialExecutedCount)",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (activeMainSection == 0) Spo2Cyan else TextSecondary
                        )
                    }
                }
            )
            Tab(
                selected = activeMainSection == 1,
                onClick = { activeMainSection = 1 },
                text = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Default.LocalHospital,
                            contentDescription = null,
                            tint = if (activeMainSection == 1) MedicalPrimaryLight else TextSecondary,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Điều Trị Chuyên Khoa ($specialtyExecutedCount)",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (activeMainSection == 1) MedicalPrimaryLight else TextSecondary
                        )
                    }
                }
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        if (activeMainSection == 0) {
            // ==========================================
            // MỤC 1: XỬ TRÍ BAN ĐẦU (HỒI SỨC & CẤP CỨU TẠI CHỖ)
            // ==========================================

            // CÔNG CỤ CẤP CỨU TỐI KHẨN: MÁY SỐC ĐIỆN & ÉP TIM CPR
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Máy sốc điện
                Card(
                    modifier = Modifier
                        .weight(1f)
                        .clickable { onOpenDefibrillator() }
                        .testTag("btn_open_defib_card"),
                    shape = RoundedCornerShape(10.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1B4B)),
                    border = androidx.compose.foundation.BorderStroke(1.2.dp, AlertOrange)
                ) {
                    Column(modifier = Modifier.padding(10.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.FlashOn, contentDescription = null, tint = AlertOrange, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(text = "MÁY SỐC ĐIỆN", fontSize = 11.sp, fontWeight = FontWeight.Black, color = Color.White)
                        }
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(text = "Khử rung / Đồng bộ", fontSize = 10.sp, color = TextSecondary)
                        Text(text = "450.000 đ", fontSize = 10.sp, fontFamily = FontFamily.Monospace, color = Color(0xFFFEF08A))
                    }
                }

                // Ép tim CPR
                Card(
                    modifier = Modifier
                        .weight(1f)
                        .clickable { onOpenCpr() }
                        .testTag("btn_open_cpr_card"),
                    shape = RoundedCornerShape(10.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF311018)),
                    border = androidx.compose.foundation.BorderStroke(1.2.dp, MedicalRed)
                ) {
                    Column(modifier = Modifier.padding(10.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Favorite, contentDescription = null, tint = MedicalRed, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(text = "ÉP TIM CPR", fontSize = 11.sp, fontWeight = FontWeight.Black, color = Color.White)
                        }
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(text = "Hồi sinh tim phổi", fontSize = 10.sp, color = TextSecondary)
                        Text(text = "250.000 đ", fontSize = 10.sp, fontFamily = FontFamily.Monospace, color = Color(0xFFFEF08A))
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Sub-category Filter Chips for Initial Resuscitation
            val initialCategories = listOf(
                null to "Tất cả xử trí ban đầu",
                OrderCategory.AIRWAY to "Đường thở & Thở",
                OrderCategory.MEDICATION to "Thuốc & Dịch truyền",
                OrderCategory.PROCEDURE to "Thủ thuật cấp cứu"
            )

            ScrollableTabRow(
                selectedTabIndex = initialCategories.indexOfFirst { it.first == subCategoryInitial }.coerceAtLeast(0),
                containerColor = Color.Transparent,
                edgePadding = 0.dp,
                divider = {}
            ) {
                initialCategories.forEach { (cat, title) ->
                    Tab(
                        selected = subCategoryInitial == cat,
                        onClick = { subCategoryInitial = cat },
                        text = {
                            Text(
                                text = title,
                                fontSize = 10.sp,
                                fontWeight = if (subCategoryInitial == cat) FontWeight.Bold else FontWeight.Normal,
                                color = if (subCategoryInitial == cat) Spo2Cyan else TextSecondary
                            )
                        }
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            val displayInitialOrders = if (subCategoryInitial == null) {
                initialOrders
            } else {
                initialOrders.filter { it.category == subCategoryInitial }
            }

            displayInitialOrders.forEach { order ->
                OrderCardItem(
                    order = order,
                    isExecuted = order.id in executedOrderIds,
                    onExecute = { onExecuteOrder(order) },
                    vnFormat = vnFormat
                )
            }
        } else {
            // ==========================================
            // MỤC 2: ĐIỀU TRỊ CHUYÊN KHOA (CHUYỂN KHOA NỘI / NGOẠI / ICU / CAN THIỆP)
            // ==========================================
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(8.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF0F1E2E)),
                border = androidx.compose.foundation.BorderStroke(1.dp, MedicalPrimaryLight.copy(alpha = 0.4f))
            ) {
                Row(
                    modifier = Modifier.padding(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(Icons.Default.Info, contentDescription = null, tint = MedicalPrimaryLight, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Quyết định điều trị chuyên khoa: Bệnh nội khoa chuyển về khoa nội chuyên môn, bệnh ngoại khoa chuyển về khoa ngoại phẫu thuật hoặc can thiệp.",
                        fontSize = 11.sp,
                        color = Color.White,
                        lineHeight = 15.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            specialtyOrders.forEach { order ->
                // Visual badge for specialty: Nội khoa / Ngoại khoa / ICU / DSA
                val badgeText = when {
                    order.id.contains("surgery") || order.id.contains("urology") -> "NGOẠI KHOA PHẪU THUẬT"
                    order.id.contains("cathlab") -> "TIM MẠCH CAN THIỆP (DSA)"
                    order.id.contains("stroke") -> "THẦN KINH ĐỘT QUỴ"
                    order.id.contains("icu") || order.id.contains("poison") -> "HỒI SỨC TÍCH CỰC (ICU)"
                    order.id.contains("internal") -> "NỘI KHOA CHUYÊN MÔN"
                    else -> "ĐIỀU TRỊ NGOẠI TRÚ"
                }

                val badgeColor = when {
                    badgeText.contains("NGOẠI KHOA") -> AlertOrange
                    badgeText.contains("TIM MẠCH") -> MedicalRedLight
                    badgeText.contains("THẦN KINH") -> Spo2Cyan
                    badgeText.contains("ICU") -> Color(0xFFFEF08A)
                    else -> EcgGreen
                }

                OrderCardItem(
                    order = order,
                    isExecuted = order.id in executedOrderIds,
                    onExecute = { onExecuteOrder(order) },
                    vnFormat = vnFormat,
                    specialtyBadge = badgeText,
                    specialtyBadgeColor = badgeColor
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Nút chuyển sang giao ban kết ca (Loại bỏ "bước 5")
        Button(
            onClick = onProceedToNextStep,
            enabled = executedOrderIds.isNotEmpty(),
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .testTag("btn_proceed_to_debrief"),
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MedicalPrimary,
                disabledContainerColor = DarkSurfaceVariant,
                contentColor = Color.White,
                disabledContentColor = TextSecondary
            )
        ) {
            Text(
                text = if (executedOrderIds.isNotEmpty()) "HOÀN TẤT XỬ TRÍ -> GIAO BAN KẾT CA" else "HÃY RA ÍT NHẤT 1 Y LỆNH ĐỂ TIẾP TỤC",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold
            )
            if (executedOrderIds.isNotEmpty()) {
                Spacer(modifier = Modifier.width(6.dp))
                Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null, modifier = Modifier.size(18.dp))
            }
        }
    }
}

@Composable
private fun OrderCardItem(
    order: EmergencyOrder,
    isExecuted: Boolean,
    onExecute: () -> Unit,
    vnFormat: NumberFormat,
    specialtyBadge: String? = null,
    specialtyBadgeColor: Color = Spo2Cyan
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 3.dp)
            .clickable { onExecute() }
            .testTag("order_item_${order.id}"),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (isExecuted) DarkSurfaceVariant else DarkSurface
        ),
        border = androidx.compose.foundation.BorderStroke(
            1.dp,
            if (isExecuted) EcgGreen.copy(alpha = 0.7f) else BorderSubtle
        )
    ) {
        Column(modifier = Modifier.padding(10.dp)) {
            if (specialtyBadge != null) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(4.dp))
                        .background(specialtyBadgeColor.copy(alpha = 0.15f))
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = specialtyBadge,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Black,
                        color = specialtyBadgeColor
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = order.name,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = TextPrimary
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = order.description,
                        fontSize = 10.sp,
                        color = TextSecondary,
                        lineHeight = 14.sp
                    )

                    Spacer(modifier = Modifier.height(4.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "${vnFormat.format(order.costVnd)} đ",
                            fontSize = 10.sp,
                            fontFamily = FontFamily.Monospace,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFFFEF08A)
                        )
                        if (order.suppliesUsed.isNotEmpty()) {
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "• ${order.suppliesUsed.firstOrNull() ?: ""}",
                                fontSize = 9.sp,
                                color = TextMuted,
                                maxLines = 1
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.width(8.dp))

                Button(
                    onClick = onExecute,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isExecuted) EcgGreen else DarkSurfaceVariant,
                        contentColor = if (isExecuted) Color.Black else Color.White
                    ),
                    shape = RoundedCornerShape(6.dp),
                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                    modifier = Modifier.testTag("btn_execute_${order.id}")
                ) {
                    Text(
                        text = if (isExecuted) "ĐÃ RA LỆNH" else "RA Y LỆNH",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            // Phản hồi lâm sàng khi y lệnh được thực thi
            if (isExecuted) {
                Spacer(modifier = Modifier.height(6.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(6.dp))
                        .background(Color(0xFF062E3B))
                        .padding(8.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.CheckCircle, contentDescription = null, tint = EcgGreen, modifier = Modifier.size(14.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = order.feedbackOnExecution,
                            fontSize = 10.sp,
                            color = TextPrimary,
                            lineHeight = 14.sp
                        )
                    }
                }
            }
        }
    }
}
