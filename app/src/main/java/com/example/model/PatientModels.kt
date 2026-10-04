package com.example.model

enum class TriageLevel(val label: String, val badgeColor: Long) {
    RED("ĐỎ - NGUY KỊCH TÍNH MẠNG", 0xFFDC2626),
    YELLOW("VÀNG - CẤP CỨU KHẨN", 0xFFF59E0B),
    GREEN("XANH - THEO DÕI", 0xFF10B981)
}

enum class EcgRhythm(val title: String) {
    NORMAL_SINUS("Nhịp xoang (Sinus Rhythm)"),
    STEMI("ST chênh lên cấp (STEMI Lead II)"),
    VFIB("Rung thất (Ventricular Fibrillation)"),
    ASYSTOLE("Vô tâm thu (Asystole)")
}

data class VitalSigns(
    val heartRate: Int,
    val bpSys: Int,
    val bpDia: Int,
    val spo2: Int,
    val respRate: Int,
    val temperature: Float,
    val gcs: Int,
    val rhythm: EcgRhythm
)

data class AbcdeAssessment(
    val airwayDesc: String,
    val airwayIsClear: Boolean,
    val breathingDesc: String,
    val breathingIsNormal: Boolean,
    val circulationDesc: String,
    val circulationIsNormal: Boolean,
    val disabilityDesc: String,
    val disabilityIsNormal: Boolean,
    val exposureDesc: String,
    val exposureIsNormal: Boolean
)

data class PatientHistoryQA(
    val id: String,
    val question: String,
    val answer: String,
    val speaker: String
)

data class ChatMessage(
    val id: String,
    val sender: String,
    val text: String,
    val isDoctor: Boolean,
    val timestamp: String = ""
)

data class PhysicalExamSystemItem(
    val systemKey: String,
    val systemName: String,
    val finding: String,
    val isAbnormal: Boolean
)

data class LabTestItem(
    val id: String,
    val name: String,
    val category: String,
    val costVnd: Long,
    val waitTimeMinutes: Int,
    val refRange: String,
    val resultShort: String,
    val resultDetailed: String,
    val isCritical: Boolean,
    val isRecommendedForCase: Boolean
)

enum class OrderCategory(val title: String) {
    AIRWAY("Đường thở & Hô hấp"),
    MEDICATION("Thuốc hồi sức & Dịch"),
    PROCEDURE("Thủ thuật & Can thiệp"),
    CONSULTATION("Hội chẩn & Chuyển khoa")
}

data class EmergencyOrder(
    val id: String,
    val category: OrderCategory,
    val name: String,
    val description: String,
    val costVnd: Long,
    val suppliesUsed: List<String>,
    val isEssential: Boolean,
    val isHarmful: Boolean = false,
    val feedbackOnExecution: String
)

data class PatientCase(
    val id: String,
    val patientName: String,
    val age: Int,
    val gender: String,
    val occupation: String,
    val triageLevel: TriageLevel,
    val chiefComplaint: String,
    val arrivalTime: String,
    val initialVitals: VitalSigns,
    val abcde: AbcdeAssessment,
    val historyQuestions: List<PatientHistoryQA>,
    val physicalExams: List<PhysicalExamSystemItem>,
    val availableLabs: List<LabTestItem>,
    val goldenDiagnosis: String,
    val diagnosisKeywords: List<String>,
    val goldenDifferentials: List<String>,
    val goldenClinicalReasoning: String,
    val standardOrders: List<EmergencyOrder>,
    val timeLimitMinutes: Int = 12,
    // Branching narratives:
    val badDelayNarrative: String,
    val badDelayVitals: VitalSigns,
    val successfulRescueNarrative: String,
    val successVitals: VitalSigns,
    val clinicalPearls: String,
    // Gratitude scene
    val gratitudeSpeaker: String = "Bệnh nhân & Gia đình",
    val gratitudeMessage: String = "Cảm ơn bác sĩ và kíp trực đã giành giật sự sống cho tôi trong gang tấc! Tôi không biết lấy gì đền đáp công ơn của các bác sĩ!"
)
