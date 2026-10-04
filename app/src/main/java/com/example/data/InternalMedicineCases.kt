package com.example.data

import com.example.model.*

object InternalMedicineCases {

    fun getInternalCases(baseLabListFactory: (
        ecgResult: String, ecgCritical: Boolean,
        tropResult: String, tropCritical: Boolean,
        abgResult: String, abgCritical: Boolean,
        lactateResult: String, lactateCritical: Boolean,
        cbcResult: String, cbcCritical: Boolean,
        bloodSugarResult: String,
        xrayChestResult: String,
        xrayAbdResult: String,
        urinalysisResult: String
    ) -> List<LabTestItem>): List<PatientCase> {
        return listOf(
            createCase8HypertensiveUrgency(baseLabListFactory),
            createCase9Hypoglycemia(baseLabListFactory),
            createCase10RenalColic(baseLabListFactory),
            createCase11AcuteVertigo(baseLabListFactory),
            createCase12Appendicitis(baseLabListFactory),
            createCase13UpperGiBleed(baseLabListFactory)
        )
    }

    // ==========================================
    // CASE 8: CƠN TĂNG HUYẾT ÁP KHẨN CẤP (HYPERTENSIVE URGENCY)
    // ==========================================
    private fun createCase8HypertensiveUrgency(baseLabListFactory: (String, Boolean, String, Boolean, String, Boolean, String, Boolean, String, Boolean, String, String, String, String) -> List<LabTestItem>): PatientCase {
        val labs = baseLabListFactory(
            "Nhịp xoang 96 bpm, dấu hiệu dày thất trái nhẹ (Sokolow-Lyon > 35mm), không ST chênh, không thiếu máu cơ tim cấp.", false,
            "hs-cTnI: 8.5 ng/L (Bình thường < 14 ng/L) - Men tim âm tính, không có tổn thương cơ tim cấp.", false,
            "Khí máu động mạch: pH 7.40, PaO2 96 mmHg, PaCO2 38 mmHg. Bình thường.", false,
            "Lactate máu: 1.1 mmol/L - Bình thường.", false,
            "Hồng cầu: 4.8 T/L, Hb: 142 g/L, Bạch cầu: 6.8 G/L - Bình thường.", false,
            "Đường huyết mao mạch tại giường: 5.6 mmol/L - Bình thường.",
            "X-quang ngực thẳng: Quai động mạch chủ hơi vồng, bóng tim kích thước trong giới hạn bình thường, phế trường sáng.",
            "X-quang bụng: Bình thường.",
            "Tổng phân tích nước tiểu: Protein (-), Hồng cầu (-), không có tổn thương cầu thận cấp."
        )

        return PatientCase(
            id = "case_htn_08",
            patientName = "Trần Thị Mai",
            age = 58,
            gender = "Nữ",
            occupation = "Tiểu thương tại chợ",
            triageLevel = TriageLevel.YELLOW,
            chiefComplaint = "Đau đầu dữ dội vùng gáy chẩm, hoa mắt chóng mặt, buồn nôn, đo huyết áp tại nhà 210 mmHg",
            arrivalTime = "14:20",
            initialVitals = VitalSigns(96, 215, 120, 98, 20, 36.8f, 15, EcgRhythm.NORMAL_SINUS),
            abcde = AbcdeAssessment(
                airwayDesc = "Đường thở thông thoáng, tự thở êm.", airwayIsClear = true,
                breathingDesc = "Nhịp thở 20 lần/phút, phổi êm không ran ẩm rale rít.", breathingIsNormal = true,
                circulationDesc = "Mạch 96 lần/phút căng nảy, HUYẾT ÁP TĂNG CỰC CAO 215/120 mmHg! Tiếng tim T2 đanh ở đáy tim.", circulationIsNormal = false,
                disabilityDesc = "Tỉnh táo GCS 15/15, đau đầu nhiều vùng chẩm, không yếu liệt tay chân, đồng tử 2 bên 2.5mm đều phản xạ tốt.", disabilityIsNormal = true,
                exposureDesc = "Da niêm mạc hồng hào, không phù, không chấn thương.", exposureIsNormal = true
            ),
            historyQuestions = listOf(
                PatientHistoryQA("q1", "Bác bị đau đầu và huyết áp cao từ lúc nào?", "Tôi bị tăng huyết áp 5 năm nay đang uống Amlodipine đều. Nhưng 3 ngày nay bận buôn bán ở chợ hết thuốc nên tôi chưa đi mua, trưa nay đau đầu buốt tận gáy, đo máy tại nhà thấy lên 210!", "Bà Mai"),
                PatientHistoryQA("q2", "Bác có cảm thấy tức ngực, khó thở hay nhìn mờ gì không?", "Không tức ngực bác sĩ ơi, thở vẫn bình thường, chỉ thấy hoa mắt chóng mặt và đau đầu căng như muốn nứt ra!", "Bà Mai")
            ),
            physicalExams = listOf(
                PhysicalExamSystemItem("sys_general", "Toàn thân", "Bệnh nhân tỉnh táo, vẻ mặt lo âu vì đau đầu, không sốt, không phù chân.", false),
                PhysicalExamSystemItem("sys_cardio", "Tim mạch", "T1 T2 rõ, T2 đanh mạnh ở ổ van động mạch chủ, không tiếng thổi bệnh lý. Huyết áp đo 2 tay: Tay phải 215/120 mmHg, tay trái 210/118 mmHg.", true),
                PhysicalExamSystemItem("sys_resp", "Hô hấp", "Thở êm 20 lần/phút, rì rào phế nang rõ 2 phế trường, không có rale ẩm rale nổ ở đáy phổi.", false),
                PhysicalExamSystemItem("sys_neuro", "Thần kinh", "Tỉnh táo hoàn toàn GCS 15. Dấu Babinski âm tính 2 bên, không có yếu liệt nửa người, 12 đôi dây thần kinh sọ chưa phát hiện bất thường.", false),
                PhysicalExamSystemItem("sys_gi", "Tiêu hóa", "Bụng mềm, không chướng, gan lách không to.", false),
                PhysicalExamSystemItem("sys_skin", "Da & Mắt", "Soi đáy mắt: Động mạch võng mạc co nhỏ độ II, không có xuất huyết võng mạc, không phù gai thị.", false)
            ),
            availableLabs = labs,
            goldenDiagnosis = "Cơn tăng huyết áp khẩn cấp (Hypertensive Urgency) do ngưng thuốc điều trị",
            diagnosisKeywords = listOf("tang huyet ap", "hypertensive urgency", "con tang huyet ap", "huyet ap cao", "ngung thuoc"),
            goldenDifferentials = listOf("Cơn tăng huyết áp cấp cứu (Hypertensive Emergency)", "Đột quỵ xuất huyết não", "Cơn thiếu máu não thoáng qua (TIA)", "Hội chứng vành cấp"),
            goldenClinicalReasoning = "Huyết áp tâm thu > 180 mmHg và/hoặc tâm trương > 120 mmHg nhưng KHÔNG CÓ bằng chứng tổn thương cơ quan đích cấp tính (men tim âm tính, đáy mắt không phù gai, thần kinh không khu trú, thận bình thường) -> Chẩn đoán là Cơn tăng huyết áp khẩn cấp (Hypertensive Urgency). Nguyên tắc điều trị: Hạ huyết áp từ từ bằng thuốc đường uống (Amlodipine hoặc Nicardipine/Captopril), hạ HA khoảng 20-25% trong vài giờ, KHÔNG ĐƯỢC hạ áp quá nhanh đột ngột vì sẽ gây thiếu máu não cục bộ!",
            standardOrders = MasterHospitalOrders.getAllMasterOrders(),
            badDelayNarrative = "Do dùng thuốc hạ áp liều cao tĩnh mạch hạ áp quá đột ngột hoặc dùng chất kích thích, áp lực tưới máu não giảm sụp đổ gây thiếu máu não cục bộ!",
            badDelayVitals = VitalSigns(110, 85, 50, 95, 24, 36.8f, 10, EcgRhythm.NORMAL_SINUS),
            successfulRescueNarrative = "Sau khi được dùng thuốc hạ áp đường uống và nghỉ ngơi tại phòng yên tĩnh, huyết áp hạ êm dịu về mức 155/92 mmHg, bệnh nhân hết hẳn đau đầu và hoa mắt!",
            successVitals = VitalSigns(78, 155, 92, 99, 16, 36.8f, 15, EcgRhythm.NORMAL_SINUS),
            clinicalPearls = "Quy tắc vàng: Phân biệt Cơn tăng huyết áp cấp cứu (Emergency - có tổn thương cơ quan đích, cần hạ áp bằng đường tĩnh mạch) và Khẩn cấp (Urgency - không tổn thương cơ quan đích, hạ áp từ từ bằng đường uống, tránh hạ quá 25% trong giờ đầu).",
            gratitudeSpeaker = "Bà Mai",
            gratitudeMessage = "Cảm ơn bác sĩ! Đầu tôi nhẹ nhõm hẳn rồi, huyết áp hạ xuống tôi mừng quá. Từ nay tôi xin chừa, không bao giờ dám tự ý bỏ thuốc huyết áp nữa!"
        )
    }

    // ==========================================
    // CASE 9: HẠ ĐƯỜNG HUYẾT CẤP Ở BỆNH NHÂN TIỂU ĐƯỜNG (HYPOGLYCEMIA)
    // ==========================================
    private fun createCase9Hypoglycemia(baseLabListFactory: (String, Boolean, String, Boolean, String, Boolean, String, Boolean, String, Boolean, String, String, String, String) -> List<LabTestItem>): PatientCase {
        val labs = baseLabListFactory(
            "Nhịp xoang nhanh 122 bpm do phản ứng cường giao cảm, không ST chênh.", false,
            "hs-cTnI: 9.1 ng/L - Bình thường.", false,
            "Khí máu động mạch: pH 7.38, PaO2 94 mmHg, PaCO2 36 mmHg. Bình thường.", false,
            "Lactate máu: 1.6 mmol/L - Bình thường.", false,
            "Hồng cầu: 4.3 T/L, Hb: 130 g/L, Bạch cầu: 7.2 G/L - Bình thường.", false,
            "ĐƯỜNG HUYẾT MAO MẠCH CẤP CỨU TẠI GIƯỜNG: 1.8 mmol/L (32 mg/dL) -> TỤT ĐƯỜNG HUYẾT NGUY HIỂM TÍNH MẠNG!",
            "X-quang ngực: Bình thường.",
            "X-quang bụng: Bình thường.",
            "Tổng phân tích nước tiểu: Glucose (-), Ceton (-)."
        )

        return PatientCase(
            id = "case_hypo_09",
            patientName = "Nguyễn Văn Hùng",
            age = 64,
            gender = "Nam",
            occupation = "Cán bộ hưu trí",
            triageLevel = TriageLevel.RED,
            chiefComplaint = "Lơ mơ, vã mồ hôi đầm đìa lạnh toát, tay chân run bần bật sau khi tiêm Insulin buổi sáng",
            arrivalTime = "09:40",
            initialVitals = VitalSigns(122, 105, 65, 97, 22, 35.8f, 10, EcgRhythm.NORMAL_SINUS),
            abcde = AbcdeAssessment(
                airwayDesc = "Đường thở nguy cơ hít sặc do tri giác lơ mơ GCS 10.", airwayIsClear = false,
                breathingDesc = "Thở nhanh nông 22 lần/phút, phổi thông khí rõ.", breathingIsNormal = true,
                circulationDesc = "Mạch nhanh 122 lần/phút, huyết áp 105/65 mmHg, chi lạnh ẩm.", circulationIsNormal = false,
                disabilityDesc = "HÔN MÊ - LƠ MƠ GCS 10/15 (Mắt 3, Lời nói 3, Vận động 4). Gọi hỏi trả lời ú ớ, tay chân run lẩy bẩy.", disabilityIsNormal = false,
                exposureDesc = "Toàn thân ướt đẫm mồ hôi dính nhớt như vừa tắm xong, nhiệt độ hạ 35.8°C.", exposureIsNormal = false
            ),
            historyQuestions = listOf(
                PatientHistoryQA("q1", "Bác ấy bị bệnh gì và sáng nay sinh hoạt như thế nào?", "Ông nhà tôi bị tiểu đường 10 năm nay đang tiêm Insulin ngày 2 lần. Sáng nay 7 giờ ông ấy tiêm xong thì có việc bận sang nhà hàng xóm nên quên ăn sáng, đến 9 giờ tôi thấy ông ấy ngồi gục ở bàn, mồ hôi ướt đẫm áo, gọi không biết gì nữa!", "Vợ bệnh nhân"),
                PatientHistoryQA("q2", "Trước đây bác ấy có bao giờ bị như thế này chưa?", "Có đôi lần bị đói run tay nhưng ăn kẹo vào là khỏi, chưa lần nào mê man run giật dữ dội như hôm nay bác sĩ ơi!", "Vợ bệnh nhân")
            ),
            physicalExams = listOf(
                PhysicalExamSystemItem("sys_general", "Toàn thân", "Lơ mơ tiếp xúc chậm, da tái nhợt lạnh ẩm, vã mồ hôi toàn thân đầm đìa.", true),
                PhysicalExamSystemItem("sys_neuro", "Thần kinh", "HỘI CHỨNG THIẾU ĐƯỜNG NÃO: Lơ mơ GCS 10, hai bàn tay run giật biên độ lớn, tăng phản xạ gân xương, không dấu thần kinh khu trú.", true),
                PhysicalExamSystemItem("sys_cardio", "Tim mạch", "Tim nhịp nhanh xoang đều 122 bpm, mạch ngoại vi nảy nhanh yếu do cường giao cảm.", true),
                PhysicalExamSystemItem("sys_resp", "Hô hấp", "Thở nhanh nông 22 lần/phút, không co kéo, không ran.", false),
                PhysicalExamSystemItem("sys_gi", "Tiêu hóa", "Bụng mềm, gan lách không sờ thấy.", false),
                PhysicalExamSystemItem("sys_skin", "Da & Chi", "Da lạnh toát, ẩm ướt mồ hôi dính dấp.", true)
            ),
            availableLabs = labs,
            goldenDiagnosis = "Hạ đường huyết cấp mức độ nặng do dùng Insulin bỏ bữa ăn trên bệnh nhân ĐTĐ type 2",
            diagnosisKeywords = listOf("ha duong huyet", "hypoglycemia", "insulin", "tieu duong", "tut duong"),
            goldenDifferentials = listOf("Đột quỵ nhồi máu não cấp", "Cơn thiếu máu não thoáng qua", "Ngộ độc cấp", "Nhiễm khuẩn huyết"),
            goldenClinicalReasoning = "Tam chứng Whipple kinh điển: 1) Triệu chứng hạ đường huyết (lơ mơ, vã mồ hôi, tim nhanh, run tay); 2) Xét nghiệm đường huyết mao mạch thấp nghiêm trọng 1.8 mmol/L (< 3.9 mmol/L); 3) Triệu chứng hồi phục thần kỳ sau khi được bù Glucose. Xử trí cấp cứu ngay lập tức: Tiêm tĩnh mạch Glucose 30% 50ml, sau đó duy trì truyền Glucose 10% và theo dõi đường huyết mao mạch mỗi 30-60 phút.",
            standardOrders = MasterHospitalOrders.getAllMasterOrders(),
            badDelayNarrative = "Không test đường huyết mao mạch mà chẩn đoán nhầm đột quỵ não rồi đưa đi chụp CT, tế bào não bị thiếu hụt Glucose kéo dài dẫn đến phù não hoại tử không hồi phục và hôn mê sâu!",
            badDelayVitals = VitalSigns(135, 90, 55, 94, 26, 35.5f, 5, EcgRhythm.NORMAL_SINUS),
            successfulRescueNarrative = "Ngay sau khi tiêm bolus tĩnh mạch 50ml Glucose 30%, đường huyết tăng lên 6.8 mmol/L, bệnh nhân mở mắt tỉnh táo hoàn toàn, nhận ra người thân trong sự ngỡ ngàng xúc động!",
            successVitals = VitalSigns(82, 120, 75, 98, 16, 36.6f, 15, EcgRhythm.NORMAL_SINUS),
            clinicalPearls = "Quy tắc vàng: Mọi bệnh nhân vào cấp cứu có rối loạn ý thức, lơ mơ, co giật hoặc vã mồ hôi tim nhanh PHẢI ĐƯỢC TEST ĐƯỜNG HUYẾT MAO MẠCH NGAY LẬP TỨC (trong vòng 60 giây đầu)!",
            gratitudeSpeaker = "Bác Hùng & Vợ",
            gratitudeMessage = "Trời ơi, tôi tưởng tôi đi luôn rồi... Cảm ơn bác sĩ đã tiêm đường cứu sống tôi kịp thời! Tôi sẽ luôn nhớ ăn uống đúng giờ sau tiêm thuốc!"
        )
    }

    // ==========================================
    // CASE 10: CƠN ĐAU QUẶN THẬN CẤP DO SỎI NIỆU QUẢN (RENAL COLIC)
    // ==========================================
    private fun createCase10RenalColic(baseLabListFactory: (String, Boolean, String, Boolean, String, Boolean, String, Boolean, String, Boolean, String, String, String, String) -> List<LabTestItem>): PatientCase {
        val labs = baseLabListFactory(
            "Nhịp tim nhanh xoang 108 bpm do đau đớn dữ dội, không thiếu máu cơ tim.", false,
            "hs-cTnI: 5.4 ng/L - Bình thường.", false,
            "Khí máu động mạch: Bình thường.", false,
            "Lactate máu: 1.2 mmol/L - Bình thường.", false,
            "Bạch cầu 9.5 G/L (tăng nhẹ do stress đau), Hb 145 g/L - Bình thường.", false,
            "Đường huyết: 5.4 mmol/L - Bình thường.",
            "X-quang ngực: Bình thường.",
            "X-quang hệ tiết niệu (KUB): Thấy hình cản quang kích thước 6x8mm ngang mức mỏm ngang đốt sống thắt lưng L3 bên phải, nghi sỏi niệu quản.",
            "Tổng phân tích nước tiểu: HỒNG CẦU (ERY): +++ (Dày đặc vi thể), Bạch cầu: (-), Nitrit: (-)."
        )

        return PatientCase(
            id = "case_renal_10",
            patientName = "Lê Hoàng Nam",
            age = 38,
            gender = "Nam",
            occupation = "Kỹ sư công trình",
            triageLevel = TriageLevel.YELLOW,
            chiefComplaint = "Đau quặn dữ dội hông lưng phải lan xuống bẹn, vật vã lăn lộn trên cáng, tiểu buốt tiểu rắt",
            arrivalTime = "16:45",
            initialVitals = VitalSigns(108, 145, 90, 99, 22, 37.0f, 15, EcgRhythm.NORMAL_SINUS),
            abcde = AbcdeAssessment(
                airwayDesc = "Đường thở thông suốt, rên la vì đau đớn.", airwayIsClear = true,
                breathingDesc = "Thở nhanh nông 22 lần/phút do đau quặn bụng.", breathingIsNormal = true,
                circulationDesc = "Mạch 108 lần/phút nhanh, huyết áp tăng phản ứng 145/90 mmHg do đau.", circulationIsNormal = false,
                disabilityDesc = "Tỉnh táo GCS 15, đau đớn tột cùng điểm đau 9/10, vật vã đổi mọi tư thế không đỡ.", disabilityIsNormal = true,
                exposureDesc = "Vã mồ hôi hột vì đau, hố thắt lưng phải căng tức.", exposureIsNormal = true
            ),
            historyQuestions = listOf(
                PatientHistoryQA("q1", "Anh đau từ lúc nào và tính chất cơn đau như thế nào?", "Khoảng 2 tiếng trước tôi đang ở công trường thì bị đau nhói thắt lưng phải rồi đau quặn lên từng cơn dữ dội, đau buốt lan dọc xuống tận bẹn và bìu phải, không nằm yên được!", "Anh Nam"),
                PatientHistoryQA("q2", "Anh đi tiểu có gì bất thường không?", "Buồn tiểu liên tục mà đi chỉ ra vài giọt buốt rát, nước tiểu hơi đục và có màu hồng như nước rửa thịt!", "Anh Nam")
            ),
            physicalExams = listOf(
                PhysicalExamSystemItem("sys_general", "Toàn thân", "Bệnh nhân vật vã lăn lộn ôm hông lưng phải, toát mồ hôi hột, không sốt.", true),
                PhysicalExamSystemItem("sys_gi", "Tiêu hóa & Tiết niệu", "Bụng mềm, không có phản ứng thành bụng. Chạm thận phải (+), Rung thận phải DƯƠNG TÍNH RẤT RÕ (đau chói giật nảy người), ấn điểm niệu quản trên và giữa bên phải đau chói.", true),
                PhysicalExamSystemItem("sys_cardio", "Tim mạch", "Tim nhịp nhanh đều 108 bpm, T1 T2 rõ, không tiếng thổi.", false),
                PhysicalExamSystemItem("sys_resp", "Hô hấp", "Thở 22 lần/phút, phổi thông khí tốt không ran.", false),
                PhysicalExamSystemItem("sys_neuro", "Thần kinh", "Tỉnh táo, định hướng không gian thời gian chuẩn xác.", false),
                PhysicalExamSystemItem("sys_skin", "Da", "Da ẩm mồ hôi, không ban đỏ, không phù.", false)
            ),
            availableLabs = labs,
            goldenDiagnosis = "Cơn đau quặn thận phải cấp do sỏi niệu quản 1/3 trên gây tắc nghẽn (Acute Renal Colic)",
            diagnosisKeywords = listOf("con dau quan than", "soi nieu quan", "soi than", "renal colic", "tac nghen nieu quan"),
            goldenDifferentials = listOf("Viêm ruột thừa cấp", "Thủng tạng rỗng", "Tắc mạch mạc treo", "Bóc tách động mạch chủ bụng"),
            goldenClinicalReasoning = "Lâm sàng điển hình của Cơn đau quặn thận: Đau quặn dữ dội thắt lưng lan xuống bẹn bìu, vật vã không có tư thế giảm đau, rung thận (+), tiểu máu vi thể (hồng cầu +++ trong nước tiểu). X-quang và siêu âm xác nhận sỏi niệu quản gây ứ nước đài bể thận. Xử trí cấp cứu: Thuốc giảm đau đầu tay là NSAID (Ketorolac tiêm) ức chế tổng hợp prostaglandin làm giảm co thắt niệu quản và giảm áp lực đài bể thận, kết hợp thuốc giãn cơ trơn (Drotaverine/Spasfon) và hội chẩn Ngoại Tiết niệu.",
            standardOrders = MasterHospitalOrders.getAllMasterOrders(),
            badDelayNarrative = "Không cho thuốc giảm đau chống viêm mà truyền dịch ồ ạt 2000ml, nước tiểu ứ tắc làm căng vỡ đài bể thận gây viêm phúc mạc niệu!",
            badDelayVitals = VitalSigns(125, 160, 100, 97, 26, 37.8f, 14, EcgRhythm.NORMAL_SINUS),
            successfulRescueNarrative = "Sau khi tiêm Ketorolac và Drotaverine, cơn co thắt niệu quản dịu đi nhanh chóng, bệnh nhân thở phào nhẹ nhõm, hết hẳn cơn đau buốt quằn quại!",
            successVitals = VitalSigns(76, 120, 75, 99, 16, 36.8f, 15, EcgRhythm.NORMAL_SINUS),
            clinicalPearls = "Quy tắc vàng: Trong cơn đau quặn thận cấp, NSAID (Ketorolac/Diclofenac) có hiệu quả giảm đau vượt trội và bền vững hơn cả Morphin vì đánh trúng cơ chế giảm áp lực trong lòng đài bể thận!",
            gratitudeSpeaker = "Anh Nam",
            gratitudeMessage = "Ôi trời ơi cảm ơn bác sĩ! Lúc nãy tôi đau như chết đi sống lại, tiêm xong mũi thuốc của bác sĩ tôi êm ru liền. Đúng là thuốc tiên!"
        )
    }

    // ==========================================
    // CASE 11: RỐI LOẠN TIỀN ĐÌNH CẤP / BPPV (ACUTE PERIPHERAL VERTIGO)
    // ==========================================
    private fun createCase11AcuteVertigo(baseLabListFactory: (String, Boolean, String, Boolean, String, Boolean, String, Boolean, String, Boolean, String, String, String, String) -> List<LabTestItem>): PatientCase {
        val labs = baseLabListFactory(
            "Điện tâm đồ: Nhịp xoang 92 bpm, bình thường.", false,
            "hs-cTnI: 4.8 ng/L - Bình thường.", false,
            "Khí máu: Bình thường.", false,
            "Lactate: 1.0 mmol/L - Bình thường.", false,
            "Công thức máu: Hb 135 g/L, Bạch cầu 6.5 G/L - Bình thường.", false,
            "Đường huyết mao mạch: 5.2 mmol/L - Bình thường.",
            "X-quang ngực: Bình thường.",
            "X-quang bụng: Bình thường.",
            "Tổng phân tích nước tiểu: Bình thường."
        )

        return PatientCase(
            id = "case_vertigo_11",
            patientName = "Vũ Thị Thanh",
            age = 44,
            gender = "Nữ",
            occupation = "Nhân viên văn phòng",
            triageLevel = TriageLevel.GREEN,
            chiefComplaint = "Chóng mặt dữ dội, nhà cửa đồ vật quay cuồng đảo lộn, nôn thốc tháo liên tục khi thay đổi tư thế đầu",
            arrivalTime = "08:15",
            initialVitals = VitalSigns(92, 130, 80, 98, 18, 36.7f, 15, EcgRhythm.NORMAL_SINUS),
            abcde = AbcdeAssessment(
                airwayDesc = "Đường thở thông, buồn nôn nhiều nôn ra dịch trong.", airwayIsClear = true,
                breathingDesc = "Thở đều 18 lần/phút không khó thở.", breathingIsNormal = true,
                circulationDesc = "Mạch 92 lần/phút, HA 130/80 mmHg.", circulationIsNormal = true,
                disabilityDesc = "Tỉnh táo GCS 15. Nhắm nghiền mắt không dám mở vì nhìn nhà cửa quay cuồng, rung giật nhãn cầu (Nystagmus) ngang xoay khi quay đầu.", disabilityIsNormal = false,
                exposureDesc = "Không chấn thương, không liệt thần kinh sọ.", exposureIsNormal = true
            ),
            historyQuestions = listOf(
                PatientHistoryQA("q1", "Chị bị chóng mặt xuất hiện trong hoàn cảnh nào?", "Sáng nay lúc thức dậy vừa quay đầu nghiêng sang phải thì bỗng nhiên trời đất quay tít như chong chóng, đồ đạc bay lượn, tôi nôn thốc nôn tháo dịch dạ dày ra sàn!", "Chị Thanh"),
                PatientHistoryQA("q2", "Chị có bị ù tai, điếc tai hay tê yếu tay chân không?", "Tai nghe vẫn bình thường không ù, tay chân cử động bình thường, chỉ cần giữ nguyên đầu nhắm mắt thì đỡ quay, hễ nhúc nhích đầu là quay cuồng nôn thốc!", "Chị Thanh")
            ),
            physicalExams = listOf(
                PhysicalExamSystemItem("sys_general", "Toàn thân", "Tỉnh táo, nằm nhắm nghiền mắt, mệt mỏi do nôn nhiều lần.", false),
                PhysicalExamSystemItem("sys_neuro", "Thần kinh & Tiền đình", "Khám nghiệm pháp Dix-Hallpike bên phải (+): Xuất hiện cơn chóng mặt kịch phát kèm rung giật nhãn cầu (Nystagmus) hướng lên và xoay, có thời gian tiềm tàng 5 giây, thoái lui sau 30 giây. Không liệt vận động, nghiệm pháp ngón tay chỉ mũi chuẩn xác khi nằm yên.", true),
                PhysicalExamSystemItem("sys_cardio", "Tim mạch", "Tim đều 92 bpm, T1 T2 rõ.", false),
                PhysicalExamSystemItem("sys_resp", "Hô hấp", "Phổi trong, không ran.", false),
                PhysicalExamSystemItem("sys_gi", "Tiêu hóa", "Bụng mềm, nôn dịch trong dạ dày.", false),
                PhysicalExamSystemItem("sys_skin", "Da", "Bình thường, hơi tái nhẹ do nôn.", false)
            ),
            availableLabs = labs,
            goldenDiagnosis = "Chóng mặt kịch phát tư thế lành tính (BPPV) ống bán khuyên sau bên phải",
            diagnosisKeywords = listOf("chong mat", "bppv", "tien dinh", "roi loan tien dinh", "dix hallpike"),
            goldenDifferentials = listOf("Đột quỵ nhồi máu tiểu não/thân não", "Viêm thần kinh tiền đình", "Bệnh Meniere", "Hạ đường huyết"),
            goldenClinicalReasoning = "Triệu chứng khởi phát đột ngột khi thay đổi tư thế đầu, cơn chóng mặt ngắn (< 1 phút), không có triệu chứng thần kinh khu trú của đột quỵ não trung ương. Nghiệm pháp Dix-Hallpike dương tính điển hình với Nystagmus xoay có thời gian tiềm tàng và tự thoái lui -> Chẩn đoán BPPV ống bán khuyên sau. Xử trí: Thuốc chống nôn (Metoclopramide), thuốc làm dịu tiền đình (Tanganil/Piracetam) và thực hiện nghiệm pháp tái định vị sỏi tai Epley để đưa hạt sỏi trở về xoang nang.",
            standardOrders = MasterHospitalOrders.getAllMasterOrders(),
            badDelayNarrative = "Chẩn đoán nhầm đột quỵ cho chụp CT ngực bụng cản quang tốn kém và dùng thuốc an thần liều cao khiến bệnh nhân ngủ li bì!",
            badDelayVitals = VitalSigns(88, 115, 70, 97, 14, 36.7f, 13, EcgRhythm.NORMAL_SINUS),
            successfulRescueNarrative = "Sau khi tiêm thuốc chống nôn và bác sĩ thực hiện nghiệm pháp Epley xoay đầu tái định vị sỏi tai, cơn chóng mặt biến mất hoàn toàn, bệnh nhân mở mắt ngồi dậy tươi tỉnh!",
            successVitals = VitalSigns(74, 118, 75, 99, 16, 36.7f, 15, EcgRhythm.NORMAL_SINUS),
            clinicalPearls = "Quy tắc vàng: Phân biệt chóng mặt ngoại biên (BPPV - Nystagmus có thời gian tiềm tàng, kiệt sức, ức chế khi nhìn định điểm) và chóng mặt trung ương do đột quỵ tiểu não (Nystagmus đa hướng liên tục, thất điều, cần chụp MRI sọ não).",
            gratitudeSpeaker = "Chị Thanh",
            gratitudeMessage = "Kỳ diệu quá bác sĩ ơi! Lúc sáng tôi tưởng tôi bị tai biến mạch máu não sắp chết rồi, nhờ bác sĩ xoay đầu đúng mấy động tác mà hết sạch chóng mặt!"
        )
    }

    // ==========================================
    // CASE 12: VIÊM RUỘT THỪA CẤP (ACUTE APPENDICITIS)
    // ==========================================
    private fun createCase12Appendicitis(baseLabListFactory: (String, Boolean, String, Boolean, String, Boolean, String, Boolean, String, Boolean, String, String, String, String) -> List<LabTestItem>): PatientCase {
        val labs = baseLabListFactory(
            "Nhịp xoang 98 bpm đều.", false,
            "hs-cTnI: 4.2 ng/L - Bình thường.", false,
            "Khí máu: Bình thường.", false,
            "Lactate: 1.4 mmol/L - Bình thường.", false,
            "TỔNG PHÂN TÍCH TẾ BÀO MÁU: BẠCH CẦU (WBC) TĂNG CAO 14.8 G/L, Tỷ lệ Neutrophil chiếm 84% -> Hội chứng nhiễm trùng cấp tính rõ rệt!", true,
            "Đường huyết: 5.8 mmol/L - Bình thường.",
            "X-quang ngực: Bình thường.",
            "X-quang bụng không chuẩn bị: Không thấy liềm hơi dưới hoành, không có mức nước - hơi.",
            "Tổng phân tích nước tiểu: Bình thường, không có hồng cầu vi thể (loại trừ sỏi niệu quản)."
        )

        return PatientCase(
            id = "case_appendicitis_12",
            patientName = "Đỗ Tuấn Anh",
            age = 21,
            gender = "Nam",
            occupation = "Sinh viên đại học",
            triageLevel = TriageLevel.YELLOW,
            chiefComplaint = "Đau bụng âm ỉ quanh rốn sau đó khu trú đau nhói liên tục vùng hố chậu phải, sốt nhẹ 38.2°C",
            arrivalTime = "11:30",
            initialVitals = VitalSigns(98, 115, 70, 99, 18, 38.2f, 15, EcgRhythm.NORMAL_SINUS),
            abcde = AbcdeAssessment(
                airwayDesc = "Đường thở thông suốt.", airwayIsClear = true,
                breathingDesc = "Thở 18 lần/phút, phổi thông khí tốt.", breathingIsNormal = true,
                circulationDesc = "Mạch 98 lần/phút, HA 115/70 mmHg.", circulationIsNormal = true,
                disabilityDesc = "Tỉnh táo GCS 15.", disabilityIsNormal = true,
                exposureDesc = "Sốt nhẹ 38.2°C, hố chậu phải ấn đau chói.", exposureIsNormal = false
            ),
            historyQuestions = listOf(
                PatientHistoryQA("q1", "Cơn đau bụng của em bắt đầu từ khi nào và diễn biến ra sao?", "Khoảng từ đêm qua em thấy đau âm ỉ khó chịu ở vùng thượng vị quanh rốn kèm buồn nôn nhẹ, đến sáng nay thì cơn đau chạy hẳn xuống vùng hố chậu phải, đau nhói liên tục đi lại cũng thốn!", "Tuấn Anh"),
                PatientHistoryQA("q2", "Em có uống thuốc gì hay ăn phải thức ăn lạ không?", "Em không ăn đồ lạ, sáng nay đau quá có uống 1 viên Paracetamol nhưng không đỡ đau bụng tí nào!", "Tuấn Anh")
            ),
            physicalExams = listOf(
                PhysicalExamSystemItem("sys_general", "Toàn thân", "Bệnh nhân sốt 38.2°C, môi hơi khô, vẻ mặt nhiễm trùng nhẹ.", true),
                PhysicalExamSystemItem("sys_gi", "Tiêu hóa & Khám bụng", "Bụng không chướng, di động theo nhịp thở. Điểm McBurney ẤN ĐAU CHÓI (+), Dấu hiệu Blumberg (Phản ứng dội) (+), Dấu hiệu Rovsing (+) ấn hố chậu trái đau sang hố chậu phải, Đề kháng thành bụng nhẹ vùng hố chậu phải.", true),
                PhysicalExamSystemItem("sys_cardio", "Tim mạch", "Tim nhịp nhanh xoang 98 bpm, T1 T2 rõ.", false),
                PhysicalExamSystemItem("sys_resp", "Hô hấp", "Thở êm 18 lần/phút, phổi không ran.", false),
                PhysicalExamSystemItem("sys_neuro", "Thần kinh", "Tỉnh táo bình thường.", false),
                PhysicalExamSystemItem("sys_skin", "Da", "Nóng ẩm do sốt, không có ban dị ứng.", false)
            ),
            availableLabs = labs,
            goldenDiagnosis = "Viêm ruột thừa cấp giờ thứ 16 chưa vỡ mủ (Acute Appendicitis)",
            diagnosisKeywords = listOf("viem ruot thua", "appendicitis", "mcburney", "ho chau phai", "ruot thua"),
            goldenDifferentials = listOf("Viêm hạch mạc treo", "Sỏi niệu quản phải", "Viêm túi thừa Meckel", "Viêm hồi tràng đoạn cuối"),
            goldenClinicalReasoning = "Diễn biến đau bụng kinh điển theo trình tự di chuyển (từ quanh rốn chuyển khu trú hố chậu phải). Khám lâm sàng điểm McBurney đau chói, phản ứng dội (+) và phản ứng thành bụng hố chậu phải. Xét nghiệm máu bạch cầu tăng cao 14.8 G/L ưu thế Neutrophil. Xử trí chuẩn: Nhịn ăn uống hoàn toàn, lập đường truyền tĩnh mạch NaCl 0.9%, dùng kháng sinh dự phòng phổ rộng trước mổ (Ceftriaxone) và hội chẩn bác sĩ Ngoại khoa mổ nội soi cắt ruột thừa cấp cứu trước khi vỡ gây viêm phúc mạc!",
            standardOrders = MasterHospitalOrders.getAllMasterOrders(),
            badDelayNarrative = "Không hội chẩn ngoại khoa mổ mà cho bệnh nhân uống thuốc giảm đau nhuận tràng, ruột thừa căng mủ vỡ toang gây viêm phúc mạc toàn thể nhiễm độc!",
            badDelayVitals = VitalSigns(128, 90, 55, 93, 28, 39.5f, 13, EcgRhythm.NORMAL_SINUS),
            successfulRescueNarrative = "Kíp trực đã thiết lập đường truyền, tiêm kháng sinh dự phòng và hội chẩn kíp Ngoại phẫu thuật nội soi cắt ruột thừa thành công an toàn!",
            successVitals = VitalSigns(80, 118, 72, 99, 16, 37.4f, 15, EcgRhythm.NORMAL_SINUS),
            clinicalPearls = "Quy tắc vàng: Trong đau bụng cấp nghi viêm ruột thừa, TUYỆT ĐỐI KHÔNG dùng thuốc nhuận tràng hoặc thụt tháo. Chẩn đoán sớm và phẫu thuật nội soi trong 24 giờ đầu giúp người bệnh hồi phục thần tốc.",
            gratitudeSpeaker = "Tuấn Anh & Bố",
            gratitudeMessage = "Cảm ơn bác sĩ cấp cứu đã chẩn đoán chính xác viêm ruột thừa và chuyển mổ kịp thời, bác sĩ phẫu thuật bảo suýt chút nữa là ruột thừa vỡ mủ!"
        )
    }

    // ==========================================
    // CASE 13: XUẤT HUYẾT TIÊU HÓA TRÊN DO LOÉT DẠ DÀY TÁ TRÀNG (UPPER GI BLEED)
    // ==========================================
    private fun createCase13UpperGiBleed(baseLabListFactory: (String, Boolean, String, Boolean, String, Boolean, String, Boolean, String, Boolean, String, String, String, String) -> List<LabTestItem>): PatientCase {
        val labs = baseLabListFactory(
            "Nhịp xoang nhanh 112 bpm do bù trừ mất máu.", false,
            "hs-cTnI: 5.8 ng/L - Bình thường.", false,
            "Khí máu: pH 7.37, PaO2 90 mmHg, PaCO2 35 mmHg, SaO2 96%.", false,
            "Lactate máu: 2.4 mmol/L (Tăng nhẹ do giảm tưới máu tổ chức).", false,
            "CÔNG THỨC MÁU: HỒNG CẦU GIẢM 2.8 T/L, HEMOGLOBIN (Hb) GIẢM CÒN 82 g/L, Hematocrit 26% -> Thiếu máu mức độ trung bình cấp tính!", true,
            "Đường huyết: 6.2 mmol/L - Bình thường.",
            "X-quang ngực: Bình thường.",
            "X-quang bụng đứng: Không thấy liềm hơi dưới hoành (chưa thủng).",
            "Tổng phân tích nước tiểu: Bình thường."
        )

        return PatientCase(
            id = "case_gib_13",
            patientName = "Phạm Quang Dũng",
            age = 52,
            gender = "Nam",
            occupation = "Tài xế đường dài",
            triageLevel = TriageLevel.RED,
            chiefComplaint = "Nôn ra máu bã cà phê, đi ngoài phân đen sệt như hắc ín mùi khắm, hoa mắt choáng váng đứng không vững",
            arrivalTime = "21:10",
            initialVitals = VitalSigns(112, 98, 62, 96, 22, 36.9f, 15, EcgRhythm.NORMAL_SINUS),
            abcde = AbcdeAssessment(
                airwayDesc = "Đường thở thông, có nguy cơ hít sặc khi nôn máu.", airwayIsClear = true,
                breathingDesc = "Thở nhanh 22 lần/phút bù trừ thiếu máu.", breathingIsNormal = true,
                circulationDesc = "MẠCH NHANH 112 bpm, HUYẾT ÁP TỤT 98/62 mmHg, chi lạnh ẩm, dấu véo da đàn hồi chậm.", circulationIsNormal = false,
                disabilityDesc = "Tỉnh táo GCS 15, hoa mắt chóng mặt nhiều khi ngồi dậy (dấu hiệu tụt HA tư thế).", disabilityIsNormal = true,
                exposureDesc = "Da niêm mạc nhợt nhạt, lòng bàn tay trắng bệch, móng tay mất màu hồng.", exposureIsNormal = false
            ),
            historyQuestions = listOf(
                PatientHistoryQA("q1", "Bác bị nôn ra máu và đi ngoài phân đen từ bao giờ?", "Chiều nay tôi buồn nôn rồi nôn 2 lần ra khoảng 1 bát con dịch máu đen như bã cà phê, sau đó đi vệ sinh ra phân đen nhánh dính như nhựa đường mùi khắm lịm. Đứng lên là tối sầm mặt mày!", "Bác Dũng"),
                PatientHistoryQA("q2", "Bác có tiền sử dạ dày hay gần đây có uống thuốc gì không?", "Tôi hay uống rượu bia và bị đau dạ dày lâu rồi. Đợt này đau khớp gối nên 2 tuần nay tôi tự mua thuốc giảm đau khớp chống viêm (NSAID) uống liên tục mỗi ngày!", "Bác Dũng")
            ),
            physicalExams = listOf(
                PhysicalExamSystemItem("sys_general", "Toàn thân", "Thiếu máu cấp tính: Da xanh xao, niêm mạc mắt nhợt nhạt, đầu chi lạnh ẩm, mạch nhanh.", true),
                PhysicalExamSystemItem("sys_gi", "Tiêu hóa & Thăm trực tràng", "Bụng mềm, ấn tức nhẹ vùng thượng vị, không có đề kháng thành bụng. Thăm trực tràng (DRE): Thấy phân đen dính như hắc ín theo găng, mùi khắm đặc trưng của máu thoái hóa.", true),
                PhysicalExamSystemItem("sys_cardio", "Tim mạch", "Tim nhịp nhanh 112 bpm, T1 T2 rõ, huyết áp 98/62 mmHg (nằm), tụt còn 85/50 mmHg khi ngồi dậy.", true),
                PhysicalExamSystemItem("sys_resp", "Hô hấp", "Thở 22 lần/phút, phổi thông khí rõ.", false),
                PhysicalExamSystemItem("sys_neuro", "Thần kinh", "Tỉnh táo, chóng mặt khi thay đổi tư thế.", false),
                PhysicalExamSystemItem("sys_skin", "Da", "Da niêm mạc nhợt nhạt trắng bệch.", true)
            ),
            availableLabs = labs,
            goldenDiagnosis = "Xuất huyết tiêu hóa trên mức độ trung bình do loét dạ dày tá tràng sau dùng NSAID",
            diagnosisKeywords = listOf("xuat huyet tieu hoa", "loet da day", "loet da day ta trang", "upper gi bleed", "non ra mau"),
            goldenDifferentials = listOf("Xuất huyết do vỡ giãn tĩnh mạch thực quản (xơ gan)", "Hội chứng Mallory-Weiss", "Ung thư dạ dày", "Chảy máu cam nuốt máu"),
            goldenClinicalReasoning = "Hội chứng xuất huyết tiêu hóa trên kinh điển: Nôn ra máu bã cà phê, tiêu phân đen hắc ín, thiếu máu cấp tính (Hb 82 g/L) trên bệnh nhân dùng NSAID kéo dài. Huyết động bắt đầu bất ổn (mạch 112, HA 98/62). Xử trí cấp cứu: 1) Lập ngay 2 đường truyền tĩnh mạch lớn kim 18G; 2) Bù dịch tuần hoàn NaCl 0.9% hoặc Ringer Lactate khẩn trương; 3) Ức chế bơm proton liều cao (Esomeprazole 80mg tiêm TM bolus); 4) Đặt sonde dạ dày theo dõi; 5) Hội chẩn khoa Nội soi tiêu hóa can thiệp cầm máu trong vòng 12-24h.",
            standardOrders = MasterHospitalOrders.getAllMasterOrders(),
            badDelayNarrative = "Không lập đường truyền bù dịch mà tiếp tục cho uống thuốc giảm đau, ổ loét dạ dày xói mòn thủng mạch máu lớn gây sốc mất máu trụy mạch ngưng tim!",
            badDelayVitals = VitalSigns(145, 55, 30, 90, 30, 36.0f, 8, EcgRhythm.NORMAL_SINUS),
            successfulRescueNarrative = "Nhờ lập 2 đường truyền xả dịch bù thể tích và tiêm PPI liều cao, huyết áp bệnh nhân ổn định lên 115/75 mmHg, kíp nội soi đã kẹp clip cầm máu ổ loét thành công!",
            successVitals = VitalSigns(84, 115, 75, 98, 18, 36.8f, 15, EcgRhythm.NORMAL_SINUS),
            clinicalPearls = "Quy tắc vàng: Trong xuất huyết tiêu hóa, ưu tiên số 1 luôn là HỒI SỨC HUYẾT ĐỘNG (bù dịch tinh thể qua đường truyền lớn) trước khi chuyển đi nội soi can thiệp.",
            gratitudeSpeaker = "Bác Dũng",
            gratitudeMessage = "Cảm ơn các bác sĩ cấp cứu đã truyền dịch và tiêm thuốc cầm máu kịp thời! Bác sĩ nội soi bảo ổ loét đang phun máu suýt nguy tính mạng, may mà được cấp cứu chuẩn xác!"
        )
    }
}
