package com.example.ui.steps

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.ChatMessage
import com.example.model.PatientCase
import com.example.ui.theme.*

@Composable
fun PatientHistoryAndPhysicalExamStep(
    patientCase: PatientCase,
    chatMessages: List<ChatMessage>,
    isAiResponding: Boolean,
    onSendDoctorQuestion: (String) -> Unit,
    examinedSystems: Set<String>,
    onExamSystem: (String) -> Unit,
    onProceedToNextStep: () -> Unit,
    modifier: Modifier = Modifier
) {
    var activeSubTab by remember { mutableStateOf(0) } // 0 = Hỏi bệnh sử, 1 = Khám thực thể
    var inputQuestion by remember { mutableStateOf("") }
    var isTriageExpanded by remember { mutableStateOf(true) }

    val quickQuestions = remember(patientCase) {
        listOf(
            "Bác cảm thấy đau và khó chịu nhất ở đâu?",
            "Cơn khó thở/đau ngực xuất hiện từ khi nào?",
            "Trước đây bác đã từng bị cơn như thế này chưa?",
            "Bác có tiền sử bệnh lý mạn tính gì không?",
            "Bác có dị ứng với thuốc hay thức ăn gì không?",
            "Gần đây bác có vừa uống hay tiêm thuốc gì không?"
        )
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(12.dp)
    ) {
        // MỤC LỚN 1: ĐÁNH GIÁ TOÀN TRẠNG LÚC NHẬP VIỆN (CÓ THỂ THU GỌN / MỞ RỘNG)
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { isTriageExpanded = !isTriageExpanded },
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
                        Box(
                            modifier = Modifier
                                .size(8.dp)
                                .clip(RoundedCornerShape(50))
                                .background(Color(patientCase.triageLevel.badgeColor))
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "ĐÁNH GIÁ TOÀN TRẠNG NHẬP VIỆN",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.White
                        )
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = patientCase.arrivalTime,
                            fontSize = 10.sp,
                            color = TextSecondary
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(
                            imageVector = if (isTriageExpanded) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                            contentDescription = null,
                            tint = TextSecondary,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }

                AnimatedVisibility(visible = isTriageExpanded) {
                    Column(modifier = Modifier.padding(top = 8.dp)) {
                        Text(
                            text = "• Lý do vào viện: ${patientCase.chiefComplaint}",
                            fontSize = 11.sp,
                            color = Color(0xFFFEF08A)
                        )
                        Spacer(modifier = Modifier.height(3.dp))
                        Text(
                            text = "• Tình trạng đường thở: ${patientCase.abcde.airwayDesc}",
                            fontSize = 11.sp,
                            color = TextSecondary
                        )
                        Spacer(modifier = Modifier.height(3.dp))
                        Text(
                            text = "• Sinh hiệu: GCS ${patientCase.initialVitals.gcs}/15 • HA ${patientCase.initialVitals.bpSys}/${patientCase.initialVitals.bpDia} mmHg • SpO2 ${patientCase.initialVitals.spo2}% • Mạch ${patientCase.initialVitals.heartRate} l/p",
                            fontSize = 11.sp,
                            color = TextSecondary
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // MỤC LỚN 2: CHUYỂN ĐỔI GỌN GÀNG GIỮA HỎI BỆNH & KHÁM THỰC THỂ
        TabRow(
            selectedTabIndex = activeSubTab,
            containerColor = DarkSurface,
            contentColor = TextPrimary,
            divider = { HorizontalDivider(color = BorderSubtle) }
        ) {
            Tab(
                selected = activeSubTab == 0,
                onClick = { activeSubTab = 0 },
                text = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Default.Forum,
                            contentDescription = null,
                            tint = if (activeSubTab == 0) Spo2Cyan else TextSecondary,
                            modifier = Modifier.size(15.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Hỏi Bệnh Sử",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            )
            Tab(
                selected = activeSubTab == 1,
                onClick = { activeSubTab = 1 },
                text = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            Icons.Default.MedicalInformation,
                            contentDescription = null,
                            tint = if (activeSubTab == 1) MedicalPrimaryLight else TextSecondary,
                            modifier = Modifier.size(15.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Khám 6 Hệ Cơ Quan (${examinedSystems.size}/6)",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        if (activeSubTab == 0) {
            // PHẦN 1: HỎI BỆNH SỬ TƯƠNG TÁC
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 260.dp)
            ) {
                // Khung hội thoại
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp),
                    shape = RoundedCornerShape(10.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF090D18)),
                    border = androidx.compose.foundation.BorderStroke(1.dp, BorderSubtle)
                ) {
                    LazyColumn(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(8.dp),
                        reverseLayout = true
                    ) {
                        items(chatMessages.reversed()) { msg ->
                            val isDoctor = msg.isDoctor
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 3.dp),
                                horizontalArrangement = if (isDoctor) Arrangement.End else Arrangement.Start
                            ) {
                                Box(
                                    modifier = Modifier
                                        .widthIn(max = 280.dp)
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(if (isDoctor) ScrubTealDark else Color(0xFF1E293B))
                                        .padding(horizontal = 10.dp, vertical = 6.dp)
                                ) {
                                    Column {
                                        Text(
                                            text = if (isDoctor) "Bác sĩ" else msg.sender,
                                            fontSize = 9.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = if (isDoctor) Spo2Cyan else Color(0xFFFEF08A)
                                        )
                                        Spacer(modifier = Modifier.height(2.dp))
                                        Text(
                                            text = msg.text,
                                            fontSize = 12.sp,
                                            color = Color.White,
                                            lineHeight = 16.sp
                                        )
                                    }
                                }
                            }
                        }

                        if (chatMessages.isEmpty()) {
                            item {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(
                                        text = "Bác sĩ hãy đặt câu hỏi cho bệnh nhân hoặc người nhà ở bên dưới...",
                                        fontSize = 11.sp,
                                        color = TextSecondary
                                    )
                                }
                            }
                        }
                    }
                }

                // AI responding indicator
                if (isAiResponding) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.padding(top = 4.dp, start = 8.dp)
                    ) {
                        CircularProgressIndicator(modifier = Modifier.size(12.dp), strokeWidth = 1.5.dp, color = Spo2Cyan)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Bệnh nhân/người nhà đang trả lời...", fontSize = 10.sp, color = Spo2Cyan)
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Gợi ý câu hỏi lâm sàng nhanh
                Text(
                    text = "Gợi ý câu hỏi lâm sàng:",
                    fontSize = 10.sp,
                    color = TextSecondary
                )
                Spacer(modifier = Modifier.height(4.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    quickQuestions.take(2).forEach { chip ->
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(6.dp))
                                .background(DarkSurfaceVariant)
                                .clickable { onSendDoctorQuestion(chip) }
                                .padding(horizontal = 6.dp, vertical = 6.dp)
                        ) {
                            Text(text = chip, fontSize = 10.sp, color = TextPrimary, maxLines = 1)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Ô nhập câu hỏi: Yêu cầu "Hỏi bệnh nhân.."
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = inputQuestion,
                        onValueChange = { inputQuestion = it },
                        placeholder = { Text("Hỏi bệnh nhân...", fontSize = 12.sp, color = TextSecondary) },
                        singleLine = true,
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp)
                            .testTag("input_patient_question"),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = Spo2Cyan,
                            unfocusedBorderColor = BorderSubtle,
                            focusedContainerColor = DarkSurface,
                            unfocusedContainerColor = DarkSurface
                        )
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    IconButton(
                        onClick = {
                            if (inputQuestion.isNotBlank()) {
                                onSendDoctorQuestion(inputQuestion.trim())
                                inputQuestion = ""
                            }
                        },
                        modifier = Modifier
                            .size(46.dp)
                            .clip(CircleShape)
                            .background(Spo2Cyan)
                            .testTag("btn_send_patient_question")
                    ) {
                        Icon(Icons.AutoMirrored.Filled.Send, contentDescription = "Gửi", tint = Color.Black, modifier = Modifier.size(18.dp))
                    }
                }
            }
        } else {
            // PHẦN 2: KHÁM THỰC THỂ 6 HỆ CƠ QUAN
            // Bác sĩ tự đọc triệu chứng và chẩn đoán - KHÔNG hiển thị BẤT THƯỜNG / BÌNH THƯỜNG
            patientCase.physicalExams.forEach { item ->
                val isExamined = item.systemKey in examinedSystems
                val icon = when (item.systemKey) {
                    "sys_general" -> Icons.Default.Person
                    "sys_cardio" -> Icons.Default.Favorite
                    "sys_resp" -> Icons.Default.Air
                    "sys_gi" -> Icons.Default.Restaurant
                    "sys_neuro" -> Icons.Default.Psychology
                    else -> Icons.Default.Healing
                }

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 3.dp)
                        .clickable { onExamSystem(item.systemKey) }
                        .testTag("exam_${item.systemKey}"),
                    shape = RoundedCornerShape(8.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isExamined) DarkSurfaceVariant else DarkSurface
                    ),
                    border = androidx.compose.foundation.BorderStroke(
                        1.dp,
                        if (isExamined) ScrubTealLight.copy(alpha = 0.6f) else BorderSubtle
                    )
                ) {
                    Column(modifier = Modifier.padding(10.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = icon,
                                    contentDescription = null,
                                    tint = if (isExamined) Spo2Cyan else TextSecondary,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = item.systemName,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }

                            if (!isExamined) {
                                TextButton(
                                    onClick = { onExamSystem(item.systemKey) },
                                    colors = ButtonDefaults.textButtonColors(contentColor = Spo2Cyan)
                                ) {
                                    Text("Khám ngay", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                }
                            } else {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.Check, contentDescription = null, tint = ScrubTealLight, modifier = Modifier.size(14.dp))
                                    Spacer(modifier = Modifier.width(3.dp))
                                    Text(
                                        text = "Đã khám",
                                        fontSize = 10.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = ScrubTealLight
                                    )
                                }
                            }
                        }

                        if (isExamined) {
                            Spacer(modifier = Modifier.height(4.dp))
                            // Triệu chứng khách quan - Bác sĩ tự đọc và phân tích
                            Text(
                                text = item.finding,
                                fontSize = 11.sp,
                                color = TextPrimary,
                                lineHeight = 15.sp
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Nút chuyển tiếp gọn gàng - Loại bỏ chỉ mục "bước 2"
        Button(
            onClick = onProceedToNextStep,
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .testTag("btn_proceed_to_labs"),
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = MedicalPrimary,
                contentColor = Color.White
            )
        ) {
            Text(
                text = "TIẾP TỤC: CẬN LÂM SÀNG",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.width(6.dp))
            Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null, modifier = Modifier.size(18.dp))
        }
    }
}
