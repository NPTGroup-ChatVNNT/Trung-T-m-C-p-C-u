package com.example.ai

import com.example.BuildConfig
import com.example.model.ChatMessage
import com.example.model.PatientCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.util.concurrent.TimeUnit

object GeminiClinicalService {
    private const val BASE_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-3.5-flash:generateContent"

    private val client = OkHttpClient.Builder()
        .connectTimeout(15, TimeUnit.SECONDS)
        .readTimeout(20, TimeUnit.SECONDS)
        .writeTimeout(15, TimeUnit.SECONDS)
        .build()

    /**
     * Interactive Patient AI: The player (doctor) talks directly to the patient/family member,
     * and the AI responds in real-time in character with emotional, symptomatic authenticity.
     */
    suspend fun chatWithPatient(
        patientCase: PatientCase,
        doctorQuestion: String,
        chatHistory: List<ChatMessage>
    ): Pair<String, String> = withContext(Dispatchers.IO) { // Returns (speaker, replyText)
        val apiKey = try {
            BuildConfig.GEMINI_API_KEY
        } catch (_: Exception) {
            ""
        }

        // Determine if patient can speak or if family/paramedic speaks
        val isUnconsciousOrGasping = patientCase.initialVitals.gcs <= 10 ||
                patientCase.id == "case_anaph_04" ||
                patientCase.id == "case_organo_07" ||
                patientCase.id == "case_stroke_05"

        val defaultSpeaker = if (isUnconsciousOrGasping) "Người nhà bệnh nhân" else patientCase.patientName

        // Local smart conversational fallback
        val localMatch = patientCase.historyQuestions.find {
            val qLower = doctorQuestion.lowercase()
            it.question.lowercase().split(" ").any { word -> word.length > 3 && qLower.contains(word) }
        }

        if (apiKey.isBlank() || apiKey == "MY_GEMINI_API_KEY") {
            if (localMatch != null) {
                return@withContext Pair(localMatch.speaker, localMatch.answer)
            }
            val fallbackAnswer = when {
                doctorQuestion.contains("đau", ignoreCase = true) ->
                    "Dạ đau thắt dữ dội lắm bác sĩ, đau lan cả ra sau lưng với lên cổ, khó thở không chịu nổi!"
                doctorQuestion.contains("khi nào", ignoreCase = true) || doctorQuestion.contains("bao lâu", ignoreCase = true) ->
                    "Bị đột ngột khoảng hơn 1 tiếng trước lúc đang làm việc bác sĩ ơi!"
                doctorQuestion.contains("thuốc", ignoreCase = true) || doctorQuestion.contains("tiền sử", ignoreCase = true) ->
                    "Bình thường thỉnh thoảng có uống thuốc huyết áp với đau dạ dày mua ngoài hiệu thuốc thôi ạ."
                doctorQuestion.contains("dị ứng", ignoreCase = true) ->
                    "Chưa thấy dị ứng thức ăn gì nặng, nhưng lần này vừa tiêm/uống xong là bị liền!"
                else ->
                    "Tôi mệt và nghẹt thở lắm bác sĩ ơi, xin bác sĩ cứu tôi với!"
            }
            return@withContext Pair(defaultSpeaker, fallbackAnswer)
        }

        try {
            val systemInstruction = """
                Bạn đang nhập vai bệnh nhân hoặc người nhà trong phòng cấp cứu bệnh viện Việt Nam:
                - Bệnh nhân: ${patientCase.patientName}, ${patientCase.age} tuổi, ${patientCase.gender}.
                - Nghề nghiệp: ${patientCase.occupation}.
                - Triệu chứng cấp cứu: ${patientCase.chiefComplaint}.
                - Bệnh cảnh: ${patientCase.goldenDiagnosis}.
                - Nếu bệnh nhân khó thở nặng, hôn mê hoặc thất ngôn, hãy để người nhà hoặc nhân viên 115 trả lời thay.
                Hãy trả lời bác sĩ bằng tiếng Việt tự nhiên, khoảng 1-2 câu ngắn, xúc động, thể hiện đúng nỗi đau và hoàn cảnh, cung cấp manh mối lâm sàng chân thật.
            """.trimIndent()

            val prompt = "$systemInstruction\n\nBác sĩ hỏi: \"$doctorQuestion\"\nHãy trả lời theo định dạng: [Tên người trả lời]: [Câu trả lời]"

            val jsonBody = JSONObject().apply {
                val contentsArr = JSONArray().apply {
                    val contentObj = JSONObject().apply {
                        val partsArr = JSONArray().apply {
                            val partObj = JSONObject().apply {
                                put("text", prompt)
                            }
                            put(partObj)
                        }
                        put("parts", partsArr)
                    }
                    put(contentObj)
                }
                put("contents", contentsArr)
            }

            val request = Request.Builder()
                .url("$BASE_URL?key=$apiKey")
                .post(jsonBody.toString().toRequestBody("application/json".toMediaType()))
                .build()

            val response = client.newCall(request).execute()
            if (response.isSuccessful) {
                val respStr = response.body?.string() ?: ""
                val respJson = JSONObject(respStr)
                val text = respJson.optJSONArray("candidates")
                    ?.optJSONObject(0)
                    ?.optJSONObject("content")
                    ?.optJSONArray("parts")
                    ?.optJSONObject(0)
                    ?.optString("text", "")

                if (!text.isNullOrBlank()) {
                    val cleanText = text.trim()
                    if (cleanText.contains(":")) {
                        val parts = cleanText.split(":", limit = 2)
                        val speaker = parts[0].replace("[", "").replace("]", "").trim()
                        val answer = parts[1].trim()
                        return@withContext Pair(speaker, answer)
                    } else {
                        return@withContext Pair(defaultSpeaker, cleanText)
                    }
                }
            }

            Pair(defaultSpeaker, localMatch?.answer ?: "Dạ đau và nghẹt thở lắm bác sĩ ơi, xin bác sĩ xem giúp tôi với!")
        } catch (_: Exception) {
            Pair(defaultSpeaker, localMatch?.answer ?: "Bác sĩ ơi, tôi đau ngực nghẹn thở dữ dội từ lúc trưa tới giờ!")
        }
    }

    /**
     * Ask Gemini AI to clinically analyze the doctor's typed diagnosis and reasoning
     */
    suspend fun analyzeDoctorDecision(
        patientCase: PatientCase,
        doctorTypedDx: String,
        doctorReasoning: String,
        executedOrders: List<String>
    ): String = withContext(Dispatchers.IO) {
        val apiKey = try {
            BuildConfig.GEMINI_API_KEY
        } catch (_: Exception) {
            ""
        }

        if (apiKey.isBlank() || apiKey == "MY_GEMINI_API_KEY") {
            val eval = CaseAiEngine.evaluateDoctorDiagnosis(doctorTypedDx, doctorReasoning, patientCase)
            return@withContext "${eval.feedback}\n\n${eval.riskCommentary}"
        }

        try {
            val prompt = """
                Bạn là Bác sĩ Trưởng khoa Cấp cứu Hồi sức tích cực tại Việt Nam.
                Hãy nhận xét ngắn gọn (khoảng 3-4 câu, tiếng Việt chuyên môn) về quyết định của bác sĩ trẻ:
                - Ca bệnh: ${patientCase.patientName} (${patientCase.chiefComplaint})
                - Chẩn đoán vàng Bộ Y Tế: ${patientCase.goldenDiagnosis}
                - Bác sĩ trẻ tự gõ chẩn đoán: "$doctorTypedDx"
                - Biện luận của bác sĩ: "$doctorReasoning"
                - Các y lệnh đã ra: ${executedOrders.joinToString(", ")}
                
                Nội dung nhận xét:
                1. Đánh giá tính chính xác của chẩn đoán.
                2. Cảnh báo nguy cơ nếu chẩn đoán sai hoặc bỏ sót y lệnh tối khẩn.
                3. Lời khuyên lâm sàng ngắn gọn theo hướng dẫn Bộ Y Tế.
            """.trimIndent()

            val jsonBody = JSONObject().apply {
                val contentsArr = JSONArray().apply {
                    val contentObj = JSONObject().apply {
                        val partsArr = JSONArray().apply {
                            val partObj = JSONObject().apply {
                                put("text", prompt)
                            }
                            put(partObj)
                        }
                        put("parts", partsArr)
                    }
                    put(contentObj)
                }
                put("contents", contentsArr)
            }

            val request = Request.Builder()
                .url("$BASE_URL?key=$apiKey")
                .post(jsonBody.toString().toRequestBody("application/json".toMediaType()))
                .build()

            val response = client.newCall(request).execute()
            if (response.isSuccessful) {
                val respStr = response.body?.string() ?: ""
                val respJson = JSONObject(respStr)
                val text = respJson.optJSONArray("candidates")
                    ?.optJSONObject(0)
                    ?.optJSONObject("content")
                    ?.optJSONArray("parts")
                    ?.optJSONObject(0)
                    ?.optString("text", "")
                if (!text.isNullOrBlank()) {
                    return@withContext text.trim()
                }
            }

            val eval = CaseAiEngine.evaluateDoctorDiagnosis(doctorTypedDx, doctorReasoning, patientCase)
            "${eval.feedback}\n\n${eval.riskCommentary}"
        } catch (_: Exception) {
            val eval = CaseAiEngine.evaluateDoctorDiagnosis(doctorTypedDx, doctorReasoning, patientCase)
            "${eval.feedback}\n\n${eval.riskCommentary}"
        }
    }
}
