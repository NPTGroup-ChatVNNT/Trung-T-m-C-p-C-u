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
    var selectedCategory by remember { mutableStateOf(OrderCategory.AIRWAY) }

    // Combine Master Hospital Orders with any case-specific overrides
    val allOrders = remember(patientCase) {
        val masterList = MasterHospitalOrders.getAllMasterOrders()
        val caseMap = patientCase.standardOrders.associateBy { it.id }
        val merged = masterList.map { caseMap[it.id] ?: it }
        val extraCaseOrders = patientCase.standardOrders.filter { co -> masterList.none { it.id == co.id } }
        merged + extraCaseOrders
    }

    val totalRxCost = remember(executedOrderIds, allOrders) {
        allOrders.filter { it.id in executedOrderIds }.sumOf { it.costVnd }
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
                    text = "BƯỚC 4: XỬ TRÍ HỒI SỨC & Y LỆNH CẤP CỨU",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Black,
                    color = MedicalPrimaryLight
                )
                Text(
                    text = "Bác sĩ tự do ra y lệnh • Toàn bộ danh mục thuốc & can thiệp • Có thời giá VNĐ",
                    fontSize = 11.sp,
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

        Spacer(modifier = Modifier.height(12.dp))

        // SPECIAL EMERGENCY TOOLS: MÁY SỐC ĐIỆN & ÉP TIM CPR (WITH VNĐ COST & SUPPLIES)
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Defibrillator Card
            Card(
                modifier = Modifier
                    .weight(1f)
                    .clickable { onOpenDefibrillator() },
                shape = RoundedCornerShape(10.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1E1B4B)),
                border = androidx.compose.foundation.BorderStroke(1.5.dp, AlertOrange)
            ) {
                Column(modifier = Modifier.padding(10.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.FlashOn,
                            contentDescription = null,
                            tint = AlertOrange,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "MÁY SỐC ĐIỆN",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Black,
                            color = Color.White
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = "120J - 360J Khử rung", fontSize = 10.sp, color = TextSecondary)
                    Text(text = "Ước tính: 450.000 đ", fontSize = 10.sp, fontFamily = FontFamily.Monospace, color = Color(0xFFFEF08A))
                    Text(text = "Vật tư: Bản cực, gel dẫn điện", fontSize = 9.sp, color = TextMuted, maxLines = 1)
                }
            }

            // Interactive CPR Card
            Card(
                modifier = Modifier
                    .weight(1f)
                    .clickable { onOpenCpr() },
                shape = RoundedCornerShape(10.dp),
                colors = CardDefaults.cardColors(containerColor = Color(0xFF311018)),
                border = androidx.compose.foundation.BorderStroke(1.5.dp, MedicalRed)
            ) {
                Column(modifier = Modifier.padding(10.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Favorite,
                            contentDescription = null,
                            tint = MedicalRed,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "ÉP TIM CPR",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Black,
                            color = Color.White
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(text = "110 BPM Metronome", fontSize = 10.sp, color = TextSecondary)
                    Text(text = "Ước tính: 250.000 đ", fontSize = 10.sp, fontFamily = FontFamily.Monospace, color = Color(0xFFFEF08A))
                    Text(text = "Vật tư: Bóng Ambu, mask thở", fontSize = 9.sp, color = TextMuted, maxLines = 1)
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Category Tab Row
        ScrollableTabRow(
            selectedTabIndex = OrderCategory.values().indexOf(selectedCategory),
            containerColor = Color.Transparent,
            edgePadding = 0.dp,
            divider = {}
        ) {
            OrderCategory.values().forEach { cat ->
                Tab(
                    selected = selectedCategory == cat,
                    onClick = { selectedCategory = cat },
                    text = {
                        Text(
                            text = cat.title,
                            fontSize = 11.sp,
                            fontWeight = if (selectedCategory == cat) FontWeight.Bold else FontWeight.Normal,
                            color = if (selectedCategory == cat) MedicalPrimaryLight else TextSecondary
                        )
                    }
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Filtered Orders for this category from full master hospital catalog
        val ordersInCat = allOrders.filter { it.category == selectedCategory }

        if (ordersInCat.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Không có y lệnh trong danh mục này cho ca bệnh hiện tại.",
                    fontSize = 12.sp,
                    color = TextSecondary
                )
            }
        } else {
            ordersInCat.forEach { order ->
                val isExecuted = order.id in executedOrderIds

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 4.dp),
                    shape = RoundedCornerShape(10.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isExecuted) DarkSurfaceVariant else DarkSurface
                    ),
                    border = androidx.compose.foundation.BorderStroke(
                        1.dp,
                        if (isExecuted) if (order.isHarmful) MedicalRed else EcgGreen else BorderSubtle
                    )
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.Top
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = order.name,
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = TextPrimary
                                )
                                Text(
                                    text = order.description,
                                    fontSize = 11.sp,
                                    color = TextSecondary
                                )

                                Spacer(modifier = Modifier.height(4.dp))
                                // VNĐ Cost & Supplies Used badge
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = "${vnFormat.format(order.costVnd)} đ",
                                        fontSize = 11.sp,
                                        fontFamily = FontFamily.Monospace,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFFFEF08A)
                                    )
                                    if (order.suppliesUsed.isNotEmpty()) {
                                        Text(
                                            text = " • Vật tư: ${order.suppliesUsed.joinToString(", ")}",
                                            fontSize = 10.sp,
                                            color = TextSecondary,
                                            maxLines = 1
                                        )
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.width(8.dp))

                            Button(
                                onClick = { onExecuteOrder(order) },
                                enabled = !isExecuted,
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = if (isExecuted) MedicalGreen else MedicalPrimary,
                                    disabledContainerColor = DarkSurfaceVariant,
                                    contentColor = Color.White,
                                    disabledContentColor = TextSecondary
                                ),
                                shape = RoundedCornerShape(8.dp),
                                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp)
                            ) {
                                Text(
                                    text = if (isExecuted) "ĐÃ THI HÀNH" else "RA Y LỆNH",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }

                        if (isExecuted) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(if (order.isHarmful) MedicalRed.copy(alpha = 0.15f) else MedicalGreen.copy(alpha = 0.12f))
                                    .border(
                                        1.dp,
                                        if (order.isHarmful) MedicalRed else MedicalGreen,
                                        RoundedCornerShape(6.dp)
                                    )
                                    .padding(8.dp)
                            ) {
                                Row(verticalAlignment = Alignment.Top) {
                                    Icon(
                                        imageVector = if (order.isHarmful) Icons.Default.Warning else Icons.Default.CheckCircle,
                                        contentDescription = null,
                                        tint = if (order.isHarmful) MedicalRedLight else MedicalGreen,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = order.feedbackOnExecution,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = if (order.isHarmful) MedicalRedLight else MedicalGreen
                                    )
                                }
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
            enabled = executedOrderIds.isNotEmpty(),
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
                text = if (executedOrderIds.isNotEmpty()) "HOÀN TẤT XỬ TRÍ -> BƯỚC 5: GIAO BAN KẾT CA TRỰC" else "HÃY RA ÍT NHẤT 1 Y LỆNH ĐỂ TIẾP TỤC",
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
