package com.example.ui.steps

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.PatientCase
import com.example.ui.theme.*

@Composable
fun DifferentialDiagnosisStep(
    patientCase: PatientCase,
    primaryDx: String,
    onPrimaryDxChange: (String) -> Unit,
    diffDxList: List<String>,
    onAddDiffDx: (String) -> Unit,
    onRemoveDiffDx: (Int) -> Unit,
    reasoningText: String,
    onReasoningChange: (String) -> Unit,
    aiComment: String?,
    isAiAnalyzing: Boolean,
    onConsultAi: () -> Unit,
    onProceedToNextStep: () -> Unit,
    modifier: Modifier = Modifier
) {
    var newDiffInput by remember { mutableStateOf("") }
    val isReady = primaryDx.trim().length >= 3

    Column(
        modifier = modifier
            .fillMaxWidth()
            .padding(14.dp)
    ) {
        // Section Header
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Default.Psychology,
                contentDescription = null,
                tint = MedicalPrimaryLight,
                modifier = Modifier.size(24.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Column {
                Text(
                    text = "BƯỚC 3: TỰ GÕ CHẨN ĐOÁN & HỘI CHẨN AI",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Black,
                    color = MedicalPrimaryLight
                )
                Text(
                    text = "Bác sĩ tự gõ văn bản - Cơ chế AI lâm sàng sẽ đối chiếu và phân tích rủi ro",
                    fontSize = 11.sp,
                    color = TextSecondary
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // FIELD 1: CHẨN ĐOÁN XÁC ĐỊNH (TỰ GÕ)
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(10.dp),
            colors = CardDefaults.cardColors(containerColor = DarkSurface),
            border = androidx.compose.foundation.BorderStroke(1.dp, BorderSubtle)
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = "1. CHẨN ĐOÁN XÁC ĐỊNH (BẮT BUỘC TỰ GÕ):",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = MedicalPrimaryLight
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Gõ tên bệnh cụ thể, thể lâm sàng, giai đoạn (VD: Nhồi máu cơ tim cấp ST chênh lên thành trước rộng, Tràn khí màng phổi áp lực...)",
                    fontSize = 10.sp,
                    color = TextSecondary
                )
                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = primaryDx,
                    onValueChange = onPrimaryDxChange,
                    placeholder = { Text("Nhập chẩn đoán xác định của bạn...", fontSize = 13.sp, color = TextSecondary) },
                    singleLine = false,
                    maxLines = 3,
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = MedicalRed,
                        unfocusedBorderColor = BorderSubtle,
                        focusedContainerColor = DarkSurfaceVariant,
                        unfocusedContainerColor = DarkSurfaceVariant
                    )
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // FIELD 2: CÁC CHẨN ĐOÁN PHÂN BIỆT (TỰ GÕ THÊM DÒNG)
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(10.dp),
            colors = CardDefaults.cardColors(containerColor = DarkSurface),
            border = androidx.compose.foundation.BorderStroke(1.dp, BorderSubtle)
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = "2. CHẨN ĐOÁN PHÂN BIỆT (DIFFERENTIAL DIAGNOSES):",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = AlertOrange
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Những bệnh cảnh cấp cứu khác cần loại trừ ở ca này:",
                    fontSize = 10.sp,
                    color = TextSecondary
                )
                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = newDiffInput,
                        onValueChange = { newDiffInput = it },
                        placeholder = { Text("Gõ 1 chẩn đoán phân biệt...", fontSize = 12.sp, color = TextSecondary) },
                        singleLine = true,
                        modifier = Modifier
                            .weight(1f)
                            .height(50.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = AlertOrange,
                            unfocusedBorderColor = BorderSubtle,
                            focusedContainerColor = DarkSurfaceVariant,
                            unfocusedContainerColor = DarkSurfaceVariant
                        )
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = {
                            if (newDiffInput.isNotBlank()) {
                                onAddDiffDx(newDiffInput.trim())
                                newDiffInput = ""
                            }
                        },
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = AlertOrange,
                            contentColor = Color.Black
                        ),
                        modifier = Modifier.height(50.dp)
                    ) {
                        Icon(Icons.Default.Add, contentDescription = "Thêm")
                    }
                }

                if (diffDxList.isNotEmpty()) {
                    Spacer(modifier = Modifier.height(10.dp))
                    diffDxList.forEachIndexed { index, dx ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 2.dp)
                                .clip(RoundedCornerShape(6.dp))
                                .background(DarkSurfaceVariant)
                                .padding(horizontal = 10.dp, vertical = 6.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "${index + 1}. $dx",
                                fontSize = 12.sp,
                                color = TextPrimary,
                                modifier = Modifier.weight(1f)
                            )
                            IconButton(
                                onClick = { onRemoveDiffDx(index) },
                                modifier = Modifier.size(28.dp)
                            ) {
                                Icon(Icons.Default.Delete, contentDescription = "Xóa", tint = TextSecondary, modifier = Modifier.size(16.dp))
                            }
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // FIELD 3: BIỆN LUẬN LÂM SÀNG
        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(10.dp),
            colors = CardDefaults.cardColors(containerColor = DarkSurface),
            border = androidx.compose.foundation.BorderStroke(1.dp, BorderSubtle)
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    text = "3. BIỆN LUẬN LÂM SÀNG (CLINICAL REASONING):",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = Spo2Cyan
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Giải thích ngắn gọn cơ sở chẩn đoán dựa trên ABCDE, triệu chứng cơ năng, thực thể và các cận lâm sàng đã có:",
                    fontSize = 10.sp,
                    color = TextSecondary
                )
                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = reasoningText,
                    onValueChange = onReasoningChange,
                    placeholder = { Text("Viết lập luận lâm sàng của bạn...", fontSize = 12.sp, color = TextSecondary) },
                    minLines = 3,
                    maxLines = 6,
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = Spo2Cyan,
                        unfocusedBorderColor = BorderSubtle,
                        focusedContainerColor = DarkSurfaceVariant,
                        unfocusedContainerColor = DarkSurfaceVariant
                    )
                )

                Spacer(modifier = Modifier.height(10.dp))

                // AI Clinical Consultation Trigger
                Button(
                    onClick = onConsultAi,
                    enabled = isReady && !isAiAnalyzing,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = ScrubTealDark,
                        contentColor = Color.White
                    )
                ) {
                    if (isAiAnalyzing) {
                        CircularProgressIndicator(modifier = Modifier.size(16.dp), color = Color.White, strokeWidth = 2.dp)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("BÁC SĨ AI ĐANG PHÂN TÍCH CHẨN ĐOÁN...", fontSize = 12.sp)
                    } else {
                        Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = Color(0xFFFEF08A), modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("HỘI CHẨN BÁC SĨ TRƯỞNG KHOA AI (GEMINI)", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                    }
                }

                // AI Response Card if available
                if (aiComment != null) {
                    Spacer(modifier = Modifier.height(10.dp))
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(Color(0xFF062E3B))
                            .border(1.dp, Spo2Cyan, RoundedCornerShape(8.dp))
                            .padding(10.dp)
                    ) {
                        Column {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.AutoAwesome, contentDescription = null, tint = Spo2Cyan, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("NHẬN XÉT CỦA BÁC SĨ TRƯỞNG KHOA AI:", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = Spo2Cyan)
                            }
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(text = aiComment, fontSize = 11.sp, color = TextPrimary, lineHeight = 15.sp)
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // Proceed Button
        Button(
            onClick = onProceedToNextStep,
            enabled = isReady,
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
                text = if (isReady) "XÁC NHẬN CHẨN ĐOÁN -> BƯỚC 4: RA LỆNH CẤP CỨU" else "HÃY TỰ GÕ CHẨN ĐOÁN XÁC ĐỊNH ĐỂ TIẾP TỤC",
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
