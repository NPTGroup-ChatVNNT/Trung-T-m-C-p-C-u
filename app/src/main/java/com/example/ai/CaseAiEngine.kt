package com.example.ai

import com.example.model.*

data class BranchingEvent(
    val title: String,
    val narrative: String,
    val nurseCallout: String,
    val vitalsDelta: VitalSigns,
    val stabilityDelta: Int, // e.g. +20, -30
    val timePenaltyMinutes: Int = 0,
    val isCriticalWarning: Boolean = false,
    val triggersCardiacArrest: Boolean = false,
    val triggersRosc: Boolean = false
)

data class DiagnosisEvaluation(
    val score: Int, // 0 - 100
    val isCorrect: Boolean,
    val feedback: String,
    val matchedKeywords: List<String>,
    val missedElements: List<String>,
    val riskCommentary: String
)

object CaseAiEngine {

    /**
     * Specialized dynamic AI response when a doctor orders a laboratory or imaging test.
     * Penalizes time and triggers branching deterioration if a time-consuming test is ordered
     * in an unstable emergency patient before life-saving interventions!
     */
    fun evaluateLabOrder(
        caseId: String,
        labId: String,
        currentStability: Int,
        executedOrders: Set<String>
    ): BranchingEvent {
        return when (caseId) {
            // CASE 1: STEMI
            "case_stemi_01" -> {
                when (labId) {
                    "lab_ecg" -> BranchingEvent(
                        title = "Đo ECG 12 chuyển đạo tức thì",
                        narrative = "Kíp trực hoàn thành đo ECG trong 2 phút! Hình ảnh ST chênh lên dạng bia mộ ở V1-V6 khẳng định nhồi máu cơ tim cấp thành trước rộng!",
                        nurseCallout = "Điều dưỡng Thảo: 'Bác sĩ ơi, ECG hiện rõ ST chênh vòm, bệnh nhân đau ngực quằn quại!'",
                        vitalsDelta = VitalSigns(110, 95, 60, 92, 24, 36.8f, 15, EcgRhythm.STEMI),
                        stabilityDelta = +10,
                        timePenaltyMinutes = 2
                    )
                    "lab_ct_chest", "lab_ct_abd" -> BranchingEvent(
                        title = "Trì hoãn đi chụp CT không cần thiết!",
                        narrative = "Chuyển bệnh nhân đau thắt ngực cấp đi chụp CT làm lãng phí 20 phút thời gian vàng tái thông mạch vành! Cơ tim hoại tử lan rộng, xuất hiện cơn nhịp nhanh thất chuyển Rung thất (VF)!",
                        nurseCallout = "Điều dưỡng Tuấn: 'Báo động! Bệnh nhân gồng cứng người, monitor chuyển sang Rung thất hỗn loạn rồi bác sĩ!'",
                        vitalsDelta = VitalSigns(180, 50, 25, 75, 8, 36.5f, 6, EcgRhythm.VFIB),
                        stabilityDelta = -40,
                        timePenaltyMinutes = 5,
                        isCriticalWarning = true,
                        triggersCardiacArrest = true
                    )
                    else -> defaultLabEvent(labId)
                }
            }

            // CASE 2: TENSION PNEUMOTHORAX
            "case_pneumo_02" -> {
                val hasDecompressed = "p_decomp" in executedOrders
                when (labId) {
                    "lab_xray_chest", "lab_ct_chest" -> {
                        if (!hasDecompressed) {
                            BranchingEvent(
                                title = "Chậm trễ giải áp màng phổi vì chờ chụp phim!",
                                narrative = "SAI LẦM KINH ĐIỂN! Trong Tràn khí màng phổi áp lực, chờ chụp X-quang/CT làm áp lực khoang màng phổi tăng tột đỉnh, tĩnh mạch chủ xẹp hoàn toàn gây ngừng tuần hoàn tắc nghẽn (PEA)!",
                                nurseCallout = "Điều dưỡng Thảo hét lớn: 'Bác sĩ ơi! Bệnh nhân không bắt được mạch bẹn nữa, huyết áp tụt về 0/0 mmHg, ngưng tim rồi!'",
                                vitalsDelta = VitalSigns(0, 0, 0, 45, 0, 36.2f, 3, EcgRhythm.ASYSTOLE),
                                stabilityDelta = -60,
                                timePenaltyMinutes = 4,
                                isCriticalWarning = true,
                                triggersCardiacArrest = true
                            )
                        } else {
                            BranchingEvent(
                                title = "Chụp X-quang kiểm tra sau giải áp",
                                narrative = "Sau khi đã chọc kim giải áp an toàn, chụp X-quang tại giường xác nhận phổi đã bắt đầu nở lại, trung thất bớt xô lệch.",
                                nurseCallout = "Kỹ thuật viên X-quang: 'Phim chụp tại giường đã có, phổi phải nở được 60% rồi!'",
                                vitalsDelta = VitalSigns(95, 115, 70, 96, 20, 36.6f, 15, EcgRhythm.NORMAL_SINUS),
                                stabilityDelta = +10,
                                timePenaltyMinutes = 2
                            )
                        }
                    }
                    "lab_efast" -> BranchingEvent(
                        title = "Siêu âm cấp cứu tại giường E-FAST",
                        narrative = "Đầu dò siêu âm tại giường chỉ mất 2 phút: Xác nhận mất dấu trượt màng phổi (Lung Sliding), thấy điểm phổi (Lung Point). Chẩn đoán tràn khí màng phổi khẳng định vững chắc!",
                        nurseCallout = "Bác sĩ siêu âm: 'Thấy rõ Barcode sign trên M-mode, tràn khí màng phổi lượng lớn cần giải áp ngay!'",
                        vitalsDelta = VitalSigns(135, 75, 45, 76, 36, 36.6f, 12, EcgRhythm.NORMAL_SINUS),
                        stabilityDelta = +5,
                        timePenaltyMinutes = 1
                    )
                    else -> defaultLabEvent(labId)
                }
            }

            // CASE 3: SEVERE ASTHMA (SILENT CHEST)
            "case_asthma_03" -> {
                when (labId) {
                    "lab_abg" -> BranchingEvent(
                        title = "Khí máu động mạch khẩn cấp",
                        narrative = "Khí máu động mạch trả về trong 3 phút: pH 7.15, PaCO2 tăng vọt 68 mmHg. Bệnh nhân kiệt cơ hô hấp, toan hô hấp đe dọa ngừng thở!",
                        nurseCallout = "Điều dưỡng Thảo: 'Khí máu toan nặng PaCO2 68 mmHg, bệnh nhân bắt đầu lơ mơ lẫn lộn rồi!'",
                        vitalsDelta = VitalSigns(138, 145, 90, 80, 38, 37.1f, 12, EcgRhythm.NORMAL_SINUS),
                        stabilityDelta = +5,
                        timePenaltyMinutes = 2
                    )
                    "lab_ct_chest" -> BranchingEvent(
                        title = "Đưa bệnh nhân hen ác tính đi chụp CT",
                        narrative = "Di chuyển bệnh nhân đang co thắt phế quản nặng đi chụp CT làm mất oxy liên tục. Bệnh nhân ngạt thở kiệt sức, ngừng thở trên đường vận chuyển!",
                        nurseCallout = "Điều dưỡng Tuấn: 'Bệnh nhân ngừng thở rồi! Cần bóp bóng Ambu và đặt nội khí quản khẩn cấp!'",
                        vitalsDelta = VitalSigns(42, 60, 30, 48, 4, 36.8f, 5, EcgRhythm.ASYSTOLE),
                        stabilityDelta = -50,
                        timePenaltyMinutes = 4,
                        isCriticalWarning = true,
                        triggersCardiacArrest = true
                    )
                    else -> defaultLabEvent(labId)
                }
            }

            // CASE 4: ANAPHYLAXIS
            "case_anaph_04" -> {
                val hasAdrenaline = "ad_epi_im" in executedOrders
                if (!hasAdrenaline && (labId.contains("ct") || labId.contains("xray"))) {
                    BranchingEvent(
                        title = "Lãng phí thời gian khi sốc phản vệ đang diễn tiến tối cấp!",
                        narrative = "Sốc phản vệ độ III không được tiêm Adrenaline mà đi làm xét nghiệm hình ảnh: Phù nề Quinke bít tắc hoàn toàn thanh môn, trụy mạch tụt huyết áp không hồi phục!",
                        nurseCallout = "Điều dưỡng Thảo hốt hoảng: 'Bác sĩ ơi môi lưỡi sưng vù không hít được tí khí nào, huyết áp tụt còn 40/20 mmHg!'",
                        vitalsDelta = VitalSigns(155, 45, 20, 52, 40, 36.2f, 7, EcgRhythm.NORMAL_SINUS),
                        stabilityDelta = -50,
                        timePenaltyMinutes = 3,
                        isCriticalWarning = true
                    )
                } else {
                    defaultLabEvent(labId)
                }
            }

            // CASE 5: ACUTE STROKE
            "case_stroke_05" -> {
                when (labId) {
                    "lab_ct_brain" -> BranchingEvent(
                        title = "Chụp CT sọ não không cản quang đúng chỉ định",
                        narrative = "CT sọ não hoàn thành nhanh trong 10 phút: Không thấy xuất huyết não nội sọ, điểm ASPECTS 8 điểm. ĐỦ ĐIỀU KIỆN TIÊU SỢI HUYẾT rTPA!",
                        nurseCallout = "Bác sĩ CĐHA: 'Đã loại trừ xuất huyết não! Bệnh nhân còn trong giờ vàng, có thể dùng rTPA ngay!'",
                        vitalsDelta = VitalSigns(82, 185, 105, 96, 18, 36.8f, 13, EcgRhythm.NORMAL_SINUS),
                        stabilityDelta = +15,
                        timePenaltyMinutes = 2
                    )
                    "lab_ct_abd", "lab_ct_chest" -> BranchingEvent(
                        title = "Chỉ định chụp ngoài chuyên khoa làm trễ giờ vàng đột quỵ!",
                        narrative = "Chụp CT ngực bụng không cần thiết làm trôi qua mốc 4.5 giờ vàng! Vùng thiếu máu não chuyển thành hoại tử vĩnh viễn không thể cứu vãn.",
                        nurseCallout = "Điều dưỡng Thảo: 'Đã quá 4.5 giờ từ lúc khởi phát rồi, bệnh nhân mất chỉ định tiêu sợi huyết rTPA!'",
                        vitalsDelta = VitalSigns(88, 195, 115, 94, 22, 37.8f, 9, EcgRhythm.NORMAL_SINUS),
                        stabilityDelta = -35,
                        timePenaltyMinutes = 4,
                        isCriticalWarning = true
                    )
                    else -> defaultLabEvent(labId)
                }
            }

            // CASE 6: PERITONITIS
            "case_peri_06" -> {
                when (labId) {
                    "lab_xray_abd" -> BranchingEvent(
                        title = "X-quang bụng đứng không chuẩn bị",
                        narrative = "Phim X-quang chụp tại giường: Thấy rõ liềm hơi dưới cơ hoành 2 bên! Khẳng định 100% thủng tạng rỗng cần mổ cấp cứu.",
                        nurseCallout = "Bác sĩ trực: 'Có liềm hơi dưới hoành rất rõ, cần báo động đỏ Ngoại khoa ngay!'",
                        vitalsDelta = VitalSigns(124, 85, 55, 93, 26, 39.0f, 13, EcgRhythm.NORMAL_SINUS),
                        stabilityDelta = +10,
                        timePenaltyMinutes = 2
                    )
                    "lab_lactate" -> BranchingEvent(
                        title = "Lactate máu động mạch",
                        narrative = "Lactate máu tăng vọt 4.8 mmol/L: Chứng minh sốc nhiễm khuẩn nặng gây giảm tưới máu mô trầm trọng.",
                        nurseCallout = "Điều dưỡng Tuấn: 'Lactate 4.8 mmol/L, cần hồi sức dịch 30ml/kg ngay lập tức!'",
                        vitalsDelta = VitalSigns(128, 80, 50, 91, 28, 39.2f, 13, EcgRhythm.NORMAL_SINUS),
                        stabilityDelta = +5,
                        timePenaltyMinutes = 1
                    )
                    else -> defaultLabEvent(labId)
                }
            }

            // CASE 7: ORGANOPHOSPHATE
            "case_organo_07" -> {
                when (labId) {
                    "lab_che" -> BranchingEvent(
                        title = "Định lượng hoạt độ men Cholinesterase",
                        narrative = "Hoạt độ men Cholinesterase máu giảm cực nặng còn 820 U/L (< 20%). Khẳng định ngộ độc thuốc trừ sâu Phospho hữu cơ mức độ nguy kịch!",
                        nurseCallout = "Khoa Xét nghiệm: 'ChE giảm sâu còn 820 U/L, cần dùng Atropine và Pralidoxime liều tấn công ngay!'",
                        vitalsDelta = VitalSigns(36, 80, 48, 70, 10, 36.2f, 6, EcgRhythm.NORMAL_SINUS),
                        stabilityDelta = +10,
                        timePenaltyMinutes = 2
                    )
                    else -> defaultLabEvent(labId)
                }
            }

            else -> defaultLabEvent(labId)
        }
    }

    private fun defaultLabEvent(labId: String): BranchingEvent {
        return BranchingEvent(
            title = "Thực hiện xét nghiệm",
            narrative = "Kết quả xét nghiệm đã được cập nhật vào bệnh án điện tử.",
            nurseCallout = "Điều dưỡng: 'Đã nhận kết quả xét nghiệm từ phòng lab.'",
            vitalsDelta = VitalSigns(80, 120, 80, 97, 18, 37.0f, 15, EcgRhythm.NORMAL_SINUS),
            stabilityDelta = +2,
            timePenaltyMinutes = 1
        )
    }

    /**
     * Specialized dynamic AI response when a doctor orders a medical intervention.
     */
    fun evaluateTreatmentOrder(
        patientCase: PatientCase,
        order: EmergencyOrder,
        currentStability: Int
    ): BranchingEvent {
        val cid = patientCase.id
        val oid = order.id

        // 1. DANGEROUS CONTRAINDICATED COMBINATIONS (PHÂN NHÁNH XẤU - NGUY HIỂM)
        val isHarmfulForCase = when {
            order.isHarmful -> true
            cid == "case_hypo_09" && oid == "o_harmful_insulin" -> true
            cid == "case_htn_08" && oid == "ad_epi_im" -> true
            cid == "case_asthma_03" && (oid == "o_harmful_bb" || oid == "o_harmful_sedative") -> true
            cid == "case_gib_13" && (oid == "o_nsaid_pain" || oid == "o_dapt") -> true
            cid == "case_stemi_01" && oid == "ad_epi_im" -> true
            else -> false
        }

        if (isHarmfulForCase) {
            val harmfulDesc = when {
                cid == "case_hypo_09" && oid == "o_harmful_insulin" -> "THẢM HỌA: Bệnh nhân đang hạ đường huyết 1.8 mmol/L lại bị tiêm thêm Insulin! Đường huyết tụt về 0, hôn mê mất não không thể hồi phục!"
                cid == "case_htn_08" && oid == "ad_epi_im" -> "THẢM HỌA: Tiêm Adrenaline cho bệnh nhân đang có cơn tăng huyết áp 215/120 mmHg làm huyết áp vọt lên 260/150 mmHg, vỡ mạch máu não xuất huyết ồ ạt!"
                cid == "case_asthma_03" && oid == "o_harmful_bb" -> "THẢM HỌA: Thuốc chẹn Beta giao cảm làm co thắt phế quản cấp tính tột đỉnh, ngạt thở ngừng tuần hoàn hô hấp!"
                cid == "case_asthma_03" && oid == "o_harmful_sedative" -> "THẢM HỌA: Thuốc an thần ức chế trung tâm hô hấp đang kiệt cơ, bệnh nhân ngừng thở hoàn toàn!"
                cid == "case_gib_13" && (oid == "o_nsaid_pain" || oid == "o_dapt") -> "NGUY HIỂM: Dùng thuốc kháng viêm/chống đông ở bệnh nhân đang xuất huyết tiêu hóa làm ổ loét chảy máu ồ ạt tụt huyết áp sốc!"
                else -> order.feedbackOnExecution
            }

            return BranchingEvent(
                title = "Y LỆNH CHỐNG CHỈ ĐỊNH NGUY HIỂM: ${order.name}",
                narrative = harmfulDesc,
                nurseCallout = "Điều dưỡng Thảo hốt hoảng: 'Bác sĩ ơi! Y lệnh này làm bệnh nhân trở nặng nguy kịch, monitor báo động đỏ rồi!'",
                vitalsDelta = patientCase.badDelayVitals,
                stabilityDelta = -45,
                timePenaltyMinutes = 0,
                isCriticalWarning = true,
                triggersCardiacArrest = true
            )
        }

        // 2. TARGETED ESSENTIAL COMBINATIONS (PHÂN NHÁNH TỐT - CỨU SỐNG)
        val isTargetedEssential = when (cid) {
            "case_stemi_01" -> oid in listOf("o_dapt", "o_heparin", "c_cathlab", "o_o2_cannula")
            "case_pneumo_02" -> oid in listOf("p_decomp", "p_chest_tube", "o_o2_mask")
            "case_asthma_03" -> oid in listOf("o_aerosol_broncho", "o_corticoid", "o_o2_mask")
            "case_anaph_04" -> oid in listOf("ad_epi_im", "o_fluids_nacl", "o_corticoid", "o_o2_mask")
            "case_stroke_05" -> oid in listOf("c_code_stroke", "o_o2_cannula")
            "case_peri_06" -> oid in listOf("o_antibiotic_broad", "o_fluids_nacl", "p_ng_tube", "c_surgery_er")
            "case_organo_07" -> oid in listOf("o_atropine", "op_pam", "op_gastric_lavage", "o_suction_airway")
            "case_htn_08" -> oid in listOf("o_anti_htn_oral", "c_internal_ward", "c_outpatient")
            "case_hypo_09" -> oid in listOf("o_iv_glucose")
            "case_renal_10" -> oid in listOf("o_nsaid_pain", "o_antispasmodic", "c_urology")
            "case_vertigo_11" -> oid in listOf("o_antiemetic", "o_vertigo_med", "p_epley", "c_outpatient")
            "case_appendicitis_12" -> oid in listOf("o_antibiotic_broad", "o_fluids_nacl", "c_surgery_er")
            "case_gib_13" -> oid in listOf("o_ppi", "o_fluids_nacl", "p_iv_access", "c_internal_ward")
            else -> order.isEssential
        }

        if (isTargetedEssential) {
            val targetedFeedback = when {
                cid == "case_hypo_09" && oid == "o_iv_glucose" -> "KỲ DIỆU: Tiêm tĩnh mạch 50ml Glucose 30%, đường huyết vọt lên 6.8 mmol/L, bệnh nhân mở mắt tỉnh táo nói cười bình thường!"
                cid == "case_htn_08" && oid == "o_anti_htn_oral" -> "ĐIỀU TRỊ CHUẨN: Bệnh nhân uống Amlodipine hạ áp từ từ, huyết áp giảm êm dịu về 155/92 mmHg, hết hẳn đau đầu căng gáy!"
                cid == "case_renal_10" && (oid == "o_nsaid_pain" || oid == "o_antispasmodic") -> "GIẢM ĐAU HIỆU QUẢ: Ketorolac và Drotaverine làm giãn cơ trơn niệu quản, cơn đau quặn buốt thắt lưng dịu đi thần tốc!"
                cid == "case_vertigo_11" && (oid == "o_antiemetic" || oid == "p_epley" || oid == "o_vertigo_med") -> "ĐÁP ỨNG XUẤT SẮC: Thuốc và nghiệm pháp Epley làm dịu tiền đình, nhà cửa hết quay cuồng, bệnh nhân hết nôn ói!"
                cid == "case_appendicitis_12" && (oid == "c_surgery_er" || oid == "o_antibiotic_broad") -> "XỬ TRÍ KỊP THỜI: Đã truyền dịch, tiêm kháng sinh dự phòng và kíp Ngoại tiếp nhận mổ nội soi cắt ruột thừa an toàn!"
                cid == "case_gib_13" && (oid == "o_ppi" || oid == "o_fluids_nacl" || oid == "p_iv_access") -> "HỒI SỨC HUYẾT ĐỘNG TỐT: Lập đường truyền xả dịch và tiêm PPI liều cao, huyết áp ổn định, kíp nội soi sẵn sàng kẹp clip cầm máu!"
                else -> order.feedbackOnExecution
            }

            return BranchingEvent(
                title = "XỬ TRÍ ĐÚNG QUY TRÌNH BỘ Y TẾ: ${order.name}",
                narrative = targetedFeedback,
                nurseCallout = "Điều dưỡng Tuấn: 'Đã thực hiện y lệnh! Sinh hiệu bệnh nhân đang hồi phục tích cực, SpO2 và huyết áp cải thiện rõ!'",
                vitalsDelta = patientCase.successVitals,
                stabilityDelta = +30,
                timePenaltyMinutes = 0,
                triggersRosc = true
            )
        }

        // 3. SUPPORTIVE / GENERAL MEDICAL ORDER (ỔN ĐỊNH NHẸ)
        return BranchingEvent(
            title = "Thực hiện y lệnh: ${order.name}",
            narrative = order.feedbackOnExecution,
            nurseCallout = "Điều dưỡng: 'Đã thực hiện xong y lệnh của bác sĩ cho bệnh nhân.'",
            vitalsDelta = patientCase.initialVitals,
            stabilityDelta = +5,
            timePenaltyMinutes = 0
        )
    }

    /**
     * Evaluates doctor's typed diagnosis against the golden standard.
     */
    fun evaluateDoctorDiagnosis(
        userPrimaryDx: String,
        userReasoning: String,
        patientCase: PatientCase
    ): DiagnosisEvaluation {
        val lowerDx = userPrimaryDx.lowercase()
        val matched = patientCase.diagnosisKeywords.filter { kw ->
            lowerDx.contains(kw.lowercase())
        }
        val missed = patientCase.diagnosisKeywords.filter { kw ->
            !lowerDx.contains(kw.lowercase())
        }

        val matchRatio = matched.size.toFloat() / patientCase.diagnosisKeywords.size.coerceAtLeast(1)
        val hasReasoning = userReasoning.trim().length > 20

        val score = ((matchRatio * 70) + (if (hasReasoning) 30 else 10)).toInt().coerceIn(10, 100)
        val isCorrect = matchRatio >= 0.5f

        val feedback = if (isCorrect) {
            "Chẩn đoán xuất sắc! Bạn đã nhận diện chính xác bệnh cảnh cốt lõi '${patientCase.goldenDiagnosis}'."
        } else {
            "Chẩn đoán chưa sát với bệnh cảnh chuẩn. Cần chú ý thêm các dấu hiệu lâm sàng và kết quả cận lâm sàng trọng điểm."
        }

        val commentary = if (!isCorrect) {
            "CẢNH BÁO LÂM SÀNG: Chẩn đoán nhầm có thể dẫn đến việc dùng sai thuốc (ví dụ: dùng thuốc hạ áp/ức chế tim trong sốc tim, hoặc quên Adrenaline trong phản vệ) khiến bệnh nhân tử vong nhanh chóng!"
        } else {
            "Tư duy cấp cứu chuẩn xác giúp định hướng can thiệp đúng trọng tâm ngay trong thời gian vàng."
        }

        return DiagnosisEvaluation(
            score = score,
            isCorrect = isCorrect,
            feedback = feedback,
            matchedKeywords = matched,
            missedElements = missed,
            riskCommentary = commentary
        )
    }
}
