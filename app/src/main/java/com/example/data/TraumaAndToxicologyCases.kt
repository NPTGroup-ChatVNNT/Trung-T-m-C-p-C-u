package com.example.data

import com.example.model.*

object TraumaAndToxicologyCases {

    fun getNewCases(baseLabListFactory: (
        ecg: String, ecgCrit: Boolean,
        trop: String, tropCrit: Boolean,
        abg: String, abgCrit: Boolean,
        lac: String, lacCrit: Boolean,
        cbc: String, cbcCrit: Boolean,
        bs: String,
        xrChest: String,
        xrAbd: String,
        uri: String
    ) -> List<LabTestItem>): List<PatientCase> {
        return listOf(
            createCase14PolytraumaMva(baseLabListFactory),
            createCase15IatrogenicAnaphylaxis(baseLabListFactory),
            createCase16CarbonMonoxidePoisoning(baseLabListFactory),
            createCase17ParacetamolOverdose(baseLabListFactory),
            createCase18MethanolPoisoning(baseLabListFactory),
            createCase19TbiEpiduralHematoma(baseLabListFactory),
            createCase20SnakeBiteToxicology(baseLabListFactory),
            createCase21FoodAnaphylacticShock(baseLabListFactory),
            createCase22TraumaticHemothorax(baseLabListFactory)
        )
    }

    // =========================================================================
    // CASE 14: ĐA CHẤN THƯƠNG DO TAI NẠN GIAO THÔNG (VỠ LÁCH ĐỘ IV & SỐC MẤT MÁU)
    // ICD-10: S36.0 (Tổn thương vỡ lách) | T79.4 (Sốc chấn thương mất máu)
    // =========================================================================
    private fun createCase14PolytraumaMva(baseLabListFactory: (String, Boolean, String, Boolean, String, Boolean, String, Boolean, String, Boolean, String, String, String, String) -> List<LabTestItem>): PatientCase {
        val labs = baseLabListFactory(
            "Nhịp nhanh xoang 135 l/p, điện thế thấp ngoại vi, không biến đổi ST-T nguyên phát.", false,
            "hs-cTnI: 22 ng/L (bình thường < 14 ng/L) - tăng nhẹ do sốc giảm thể tích tưới máu.", false,
            "pH 7.28, PaO2 78 mmHg, PaCO2 32 mmHg, HCO3- 15 mmol/L, BE -9.5 mEq/L (Toan chuyển hóa mất bù do sốc mất máu).", true,
            "Lactate máu: 5.6 mmol/L (tăng rất cao do thiếu oxy mô trầm trọng).", true,
            "Hồng cầu: 2.1 T/L, Hemoglobin (Hb): 62 g/L (giảm nặng do chảy máu cấp trong ổ bụng), Hct: 19%, WBC: 15.2 G/L.", true,
            "6.2 mmol/L (đường huyết phản ứng stress).",
            "Không thấy tràn khí tràn dịch màng phổi, khung xương sườn hai bên liên tục.",
            "Ổ bụng mờ vùng thấp, các quai ruột dạt sang phải do lượng dịch máu lớn trong ổ phúc mạc.",
            "Nước tiểu màu vàng trong, không có hồng cầu (loại trừ chấn thương thận/bàng quang vỡ)."
        )

        val orders = listOf(
            EmergencyOrder(
                id = "p_iv_access",
                category = OrderCategory.AIRWAY,
                name = "Lập 2 đường truyền tĩnh mạch ngoại vi kim lớn 18G & Thở oxy mask túi 12L/p",
                description = "Thiết lập đường truyền lớn tức thì để bù thể tích tuần hoàn cấp cứu",
                costVnd = 80000L,
                suppliesUsed = listOf("2 kim luồn 18G xanh lá", "Dây truyền dịch", "Mask thở oxy có túi"),
                isEssential = true,
                feedbackOnExecution = "Đã lấy 2 ven 18G to nảy ở hai cẳng tay, gắn mask oxy 12L/p, SpO2 tăng lên 96%."
            ),
            EmergencyOrder(
                id = "o_fluids_nacl",
                category = OrderCategory.MEDICATION,
                name = "Xả nhanh Ringer Lactate 1000ml tĩnh mạch áp lực",
                description = "Hồi sức dịch tinh thể chống sốc giảm thể tích mất máu",
                costVnd = 95000L,
                suppliesUsed = listOf("2 chai Ringer Lactate 500ml", "Dây truyền máu/dịch"),
                isEssential = true,
                feedbackOnExecution = "Đã xả nhanh 1000ml Ringer Lactate, huyết áp nâng tạm thời từ 70/40 lên 85/55 mmHg."
            ),
            EmergencyOrder(
                id = "o_blood_transfusion",
                category = OrderCategory.MEDICATION,
                name = "Báo động đỏ truyền máu tối khẩn: 2 đơn vị Khối hồng cầu O- / Cùng nhóm",
                description = "Chỉ định sống còn bù thể tích và khả năng vận chuyển oxy trong sốc mất máu nặng",
                costVnd = 1200000L,
                suppliesUsed = listOf("2 túi khối hồng cầu 350ml", "Bộ lọc truyền máu", "Phiếu định nhóm máu tại giường"),
                isEssential = true,
                feedbackOnExecution = "Khoa Huyết học truyền máu phát ngay 2 đơn vị hồng cầu, máu chảy thành dòng vào ven."
            ),
            EmergencyOrder(
                id = "p_splint_femur",
                category = OrderCategory.PROCEDURE,
                name = "Nẹp cố định tạm thời gãy xương đùi phải (Nẹp Thomas/gỗ)",
                description = "Bất động xương gãy giảm đau, giảm mất máu thêm vào khoang đùi",
                costVnd = 150000L,
                suppliesUsed = listOf("Bộ nẹp Cramer/Thomas", "Băng cuộn y tế"),
                isEssential = true,
                feedbackOnExecution = "Đã nẹp cố định trục chi đùi phải thẳng, bệnh nhân bớt đau đớn, giảm sốc chấn thương."
            ),
            EmergencyOrder(
                id = "c_surgery_er",
                category = OrderCategory.CONSULTATION,
                name = "Báo động đỏ Hội chẩn Ngoại Tổng quát mổ cấp cứu mở bụng cầm máu",
                description = "Chỉ định phẫu thuật tối khẩn cắt lách cầm máu cứu sống bệnh nhân",
                costVnd = 400000L,
                suppliesUsed = listOf("Hồ sơ bệnh án chuyển mổ cấp cứu", "Phiếu cam kết phẫu thuật"),
                isEssential = true,
                feedbackOnExecution = "Phẫu thuật viên trưởng kíp trực có mặt tại giường, chuyển thẳng bệnh nhân lên phòng mổ!"
            ),
            EmergencyOrder(
                id = "o_harmful_delay_ct",
                category = OrderCategory.CONSULTATION,
                name = "Chuyển bệnh nhân đi chụp CT Scanner ổ bụng cản quang kéo dài",
                description = "Chụp CT khi huyết động không ổn định",
                costVnd = 1200000L,
                suppliesUsed = listOf("Xe đẩy vận chuyển"),
                isEssential = false,
                isHarmful = true,
                feedbackOnExecution = "SAI LẦM CHẾT NGƯỜI: Đưa bệnh nhân sốc mất máu huyết động không ổn định vào phòng CT làm ngưng tim trên bàn chụp!"
            )
        )

        return PatientCase(
            id = "case_trauma_mva_14",
            patientName = "Trần Hoàng Nam",
            age = 26,
            gender = "Nam",
            occupation = "Kỹ sư xây dựng",
            triageLevel = TriageLevel.RED,
            chiefComplaint = "Tai nạn giao thông xe máy va chạm ô tô tải, đau bụng dữ dội, lơ mơ vã mồ hôi, sốc mất máu",
            arrivalTime = "01:15",
            initialVitals = VitalSigns(135, 70, 40, 91, 28, 36.0f, 12, EcgRhythm.NORMAL_SINUS),
            abcde = AbcdeAssessment(
                airwayDesc = "Đường thở thông thoáng, thở nhanh nông, vã mồ hôi lạnh toàn thân",
                airwayIsClear = true,
                breathingDesc = "Thở 28 l/p, SpO2 91%, rì rào phế nang 2 bên đều, không tràn khí ngực",
                breathingIsNormal = false,
                circulationDesc = "Mạch 135 l/p nhỏ khó bắt, HA 70/40 mmHg, CRT > 3 giây, da niêm nhợt nhạt",
                circulationIsNormal = false,
                disabilityDesc = "GCS 12 điểm (E3V4M5), tiếp xúc chậm do sốc thiếu máu não",
                disabilityIsNormal = false,
                exposureDesc = "Bụng chướng căng, đề kháng khắp bụng, gõ đục vùng thấp. Biến dạng lệch trục đùi phải",
                exposureIsNormal = false
            ),
            historyQuestions = listOf(
                PatientHistoryQA("q1", "Hiện trường xảy ra tai nạn thế nào?", "Bệnh nhân đi xe máy bị xe tải đâm ngang sườn trái tốc độ cao, đập bụng vào ghi-đông xe và ngã văng ra đường.", "Người đi đường đưa vào"),
                PatientHistoryQA("q2", "Bệnh nhân đau nhiều nhất ở đâu?", "Đau thắt dữ dội vùng hạ sườn trái và khắp bụng, chân phải gãy không cử động được.", "Bệnh nhân thì thào"),
                PatientHistoryQA("q3", "Tiền sử có bệnh lý hoặc dị ứng gì không?", "Hoàn toàn khỏe mạnh, không có bệnh mạn tính hay dị ứng thuốc.", "Gia đình vừa tới viện")
            ),
            physicalExams = listOf(
                PhysicalExamSystemItem("sys_general", "Toàn thân & Da niêm", "Bệnh nhân lơ mơ, da xanh xao tái nhợt, niêm mạc mắt trắng bệch, vã mồ hôi lạnh, đầu chi tím lạnh, dấu véo da mất chậm.", true),
                PhysicalExamSystemItem("sys_cardio", "Tuần hoàn & Mạch máu", "Tiếng tim nhanh mờ, tần số 135 l/p, mạch quay bắt rất yếu, huyết áp tụt sâu 70/40 mmHg, tĩnh mạch cổ xẹp hoàn toàn.", true),
                PhysicalExamSystemItem("sys_resp", "Hô hấp & Lồng ngực", "Thở nhanh nông 28 l/p, SpO2 91%, lồng ngực di động kém, không có điểm đau chói xương sườn, rì rào phế nang rõ.", true),
                PhysicalExamSystemItem("sys_gi", "Tiêu hóa & Ổ bụng", "Bụng trướng căng, ấn đau chói và có cảm ứng phúc mạc rõ khắp bụng, gõ đục vùng thấp (dấu hiệu tụ máu trong phúc mạc).", true),
                PhysicalExamSystemItem("sys_neuro", "Thần kinh & Cơ xương khớp", "GCS 12 điểm, đồng tử 2 bên 2.5mm đều. Đùi phải sưng nề biến dạng, lạo xạo xương, mạch mu chân phải bắt yếu.", true),
                PhysicalExamSystemItem("sys_urinary", "Thận - Tiết niệu", "Đặt sonde tiểu ra nước tiểu vàng trong, không tiểu máu vi thể.", false)
            ),
            availableLabs = labs,
            goldenDiagnosis = "[S36.0] Tổn thương rách vỡ lách do chấn thương - Sốc mất máu độ IV [T79.4] (ICD-10 CCMS)",
            diagnosisKeywords = listOf("vỡ lách", "s36", "t79", "chấn thương", "mất máu", "sốc"),
            goldenDifferentials = listOf("Vỡ gan độ III", "Thủng tạng rỗng do chấn thương", "Chấn thương khung chậu vỡ mạch máu"),
            goldenClinicalReasoning = "Bệnh nhân chấn thương bụng kín do va chạm trực tiếp hạ sườn trái, hội chứng mất máu cấp tính đe dọa sinh tồn (Hb 62 g/L, HA 70/40, E-FAST dịch tự do ổ bụng rất nhiều). Chỉ định sống còn là hồi sức dịch + truyền máu khẩn cấp O- và chuyển ngay phòng mổ nội soi/mở bụng cắt lách cầm máu. Chống chỉ định chuyển đi chụp CT khi huyết động chưa kiểm soát.",
            standardOrders = orders,
            badDelayNarrative = "Do trì hoãn hồi sức dịch máu và đưa bệnh nhân đi chụp CT khi huyết động không ổn định, bệnh nhân mất thêm 1.500ml máu vào ổ bụng, ngừng tim trên đường vận chuyển.",
            badDelayVitals = VitalSigns(0, 0, 0, 0, 0, 35.0f, 3, EcgRhythm.ASYSTOLE),
            successfulRescueNarrative = "Bác sĩ xử trí chuẩn xác: lập tức lập 2 ven lớn 18G, xả dịch và truyền máu cấp cứu O-, nẹp bất động đùi và kích hoạt Báo động đỏ mổ cấp cứu. Phẫu thuật viên cắt lách thành công, cứu sống người bệnh trong gang tấc!",
            successVitals = VitalSigns(95, 110, 70, 98, 18, 36.5f, 15, EcgRhythm.NORMAL_SINUS),
            clinicalPearls = "Nguyên tắc vàng trong cấp cứu Đa chấn thương: Bệnh nhân chấn thương bụng có sốc mất máu huyết động không ổn định (Unstable Hemodynamics) + E-FAST dương tính là CHỈ ĐỊNH MỔ CẤP CỨU TỐI KHẨN, tuyệt đối KHÔNG đưa đi chụp CT Scanner.",
            gratitudeSpeaker = "Bệnh nhân Trần Hoàng Nam & Mẹ",
            gratitudeMessage = "Cảm ơn bác sĩ cấp cứu đã truyền máu và đưa con tôi lên bàn mổ kịp thời! Các bác sĩ đã sinh ra cháu lần thứ hai!"
        )
    }

    // =========================================================================
    // CASE 15: SỐC PHẢN VỆ ĐỘ III KỊCH PHÁT DO TIÊM CEFRIAXONE
    // ICD-10: T78.2 (Sốc phản vệ) | T88.6 (Tác dụng phụ bất lợi của thuốc)
    // =========================================================================
    private fun createCase15IatrogenicAnaphylaxis(baseLabListFactory: (String, Boolean, String, Boolean, String, Boolean, String, Boolean, String, Boolean, String, String, String, String) -> List<LabTestItem>): PatientCase {
        val labs = baseLabListFactory(
            "Nhịp nhanh xoang 145 l/p, sóng P cao nhọn, biến đổi thiếu máu cơ tim cấp do tụt huyết áp.", false,
            "hs-cTnI: 35 ng/L (tăng nhẹ do nhịp nhanh và tụt huyết áp mạch vành).", false,
            "pH 7.25, PaO2 55 mmHg, PaCO2 52 mmHg, HCO3- 19 mmol/L (Suy hô hấp cấp hỗn hợp toan hô hấp và toan chuyển hóa).", true,
            "Lactate máu: 4.2 mmol/L (thiếu oxy mô do co thắt thanh quản và tụt huyết áp).", true,
            "WBC 14.8 G/L, Neutrophil 82%, Hb 135 g/L.", false,
            "6.8 mmol/L.",
            "Tăng sáng 2 phế trường do bẫy khí co thắt phế quản, không tổn thương đông đặc.",
            "Không liềm hơi, không chướng dịch.",
            "Bình thường."
        )

        val orders = listOf(
            EmergencyOrder(
                id = "ad_epi_im",
                category = OrderCategory.MEDICATION,
                name = "Adrenaline (Epinephrine) 1mg/1ml tiêm bắp sâu 0.5ml mặt trước ngoài đùi NGAY LẬP TỨC",
                description = "Thuốc số 1 duy nhất đảo ngược giãn mạch và co thắt thanh quản trong sốc phản vệ",
                costVnd = 45000L,
                suppliesUsed = listOf("Ống Adrenaline 1mg", "Bơm tiêm 1ml kim 23G"),
                isEssential = true,
                feedbackOnExecution = "Đã tiêm bắp sâu 0.5ml Adrenaline vào đùi! Mạch và huyết áp bắt đầu hồi phục, co thắt phế quản giảm bớt."
            ),
            EmergencyOrder(
                id = "o_o2_mask",
                category = OrderCategory.AIRWAY,
                name = "Thở oxy qua mask có túi dự trữ 12 - 15 lít/phút",
                description = "Cung cấp oxy nồng độ cao chống hạ oxy máu mô khẩn cấp",
                costVnd = 80000L,
                suppliesUsed = listOf("Mask thở có túi", "Dây oxy"),
                isEssential = true,
                feedbackOnExecution = "Áp mask oxy túi 15L/p kín khít mặt, SpO2 tăng dần từ 76% lên 94%."
            ),
            EmergencyOrder(
                id = "o_fluids_nacl",
                category = OrderCategory.MEDICATION,
                name = "Truyền nhanh NaCl 0.9% 1000ml tĩnh mạch",
                description = "Bù dịch chống giãn mạch ồ ạt và thoát dịch lòng mạch trong phản vệ",
                costVnd = 85000L,
                suppliesUsed = listOf("2 chai NaCl 0.9% 500ml", "Dây truyền dịch"),
                isEssential = true,
                feedbackOnExecution = "Xả nhanh chai NaCl 0.9% dòng chảy thông thoáng, huyết áp cải thiện 95/60 mmHg."
            ),
            EmergencyOrder(
                id = "o_corticoid",
                category = OrderCategory.MEDICATION,
                name = "Methylprednisolone 80mg tiêm tĩnh mạch + Dimedrol 10mg tiêm bắp",
                description = "Thuốc bậc 2 dự phòng phản vệ pha 2 muộn (sau khi đã dùng Adrenaline)",
                costVnd = 85000L,
                suppliesUsed = listOf("2 lọ Methylprednisolone 40mg", "Ống Dimedrol 10mg"),
                isEssential = true,
                feedbackOnExecution = "Đã tiêm tĩnh mạch Methylprednisolone và Dimedrol, các nốt mày đay ban đỏ bắt đầu lặn dần."
            ),
            EmergencyOrder(
                id = "c_internal_ward",
                category = OrderCategory.CONSULTATION,
                name = "Chuyển khoa Hồi sức cấp cứu (ICU) theo dõi sát tối thiểu 24 giờ",
                description = "Theo dõi đề phòng sốc phản vệ pha 2 bùng phát trở lại",
                costVnd = 250000L,
                suppliesUsed = listOf("Hồ sơ bệnh án chuyển ICU"),
                isEssential = true,
                feedbackOnExecution = "Khoa Hồi sức tích cực chuẩn bị sẵn sàng giường monitor theo dõi liên tục 24h."
            ),
            EmergencyOrder(
                id = "o_harmful_bb",
                category = OrderCategory.MEDICATION,
                name = "Tiêm tĩnh mạch thuốc hạ áp chẹn Beta giao cảm Metoprolol",
                description = "Thuốc chẹn beta",
                costVnd = 50000L,
                suppliesUsed = listOf("Ống Metoprolol"),
                isEssential = false,
                isHarmful = true,
                feedbackOnExecution = "CẢNH BÁO TỬ VONG: Chẹn Beta làm bất hoạt hoàn toàn thụ thể Adrenaline, phế quản co thắt nghẹt thở tử vong ngay lập tức!"
            )
        )

        return PatientCase(
            id = "case_anaphylaxis_cef_15",
            patientName = "Lê Thị Bích Thủy",
            age = 32,
            gender = "Nữ",
            occupation = "Giáo viên tiểu học",
            triageLevel = TriageLevel.RED,
            chiefComplaint = "Tiêm kháng sinh Ceftriaxone tại phòng khám tư 5 phút trước, đột ngột nghẹn thở, tím tái, nổi mày đay tụt huyết áp",
            arrivalTime = "14:20",
            initialVitals = VitalSigns(145, 55, 35, 76, 34, 37.0f, 11, EcgRhythm.NORMAL_SINUS),
            abcde = AbcdeAssessment(
                airwayDesc = "Phù nề thanh quản Quinke, thở rít Stridor thanh quản nghe rõ từ xa",
                airwayIsClear = false,
                breathingDesc = "Thở co kéo cơ hô hấp phụ dữ dội, tím môi và đầu chi, SpO2 76%",
                breathingIsNormal = false,
                circulationDesc = "Mạch 145 l/p nhỏ như sợi chỉ, HA 55/35 mmHg, da lạnh ẩm",
                circulationIsNormal = false,
                disabilityDesc = "GCS 11 điểm, hốt hoảng vật vã, kích thích lo âu sắp hôn mê",
                disabilityIsNormal = false,
                exposureDesc = "Mẩn ngứa ban đỏ, phù mạch mi mắt, môi sưng phồng toàn thân",
                exposureIsNormal = false
            ),
            historyQuestions = listOf(
                PatientHistoryQA("q1", "Bệnh nhân vừa tiêm thuốc gì?", "Dạ tiêm Ceftriaxone 1g trị viêm họng ở phòng khám tư, tiêm được 2-3 phút là cô ấy kêu nghẹn thở, mắt sưng húp rồi ngã gục!", "Bác sĩ phòng khám đi cùng"),
                PatientHistoryQA("q2", "Phòng khám đã xử trí gì chưa?", "Dạ phòng khám hoảng quá chỉ cho thở oxy rồi đưa ngay vào cấp cứu, chưa kịp tiêm thuốc gì!", "Y tá phòng khám"),
                PatientHistoryQA("q3", "Trước đây có tiền sử dị ứng thuốc gì không?", "Trước giờ chưa từng tiêm kháng sinh nhóm này bao giờ, chỉ có dị ứng tôm cua nhẹ.", "Chồng bệnh nhân")
            ),
            physicalExams = listOf(
                PhysicalExamSystemItem("sys_general", "Toàn thân & Da niêm", "Toàn thân nổi mày đay dát sẩn đỏ rải rác, phù mạch Quincke mi mắt và môi sưng vù, da lạnh vã mồ hôi, tím tái môi đầu chi.", true),
                PhysicalExamSystemItem("sys_cardio", "Tuần hoàn & Mạch máu", "Nhịp tim nhanh 145 l/p, mạch quay rất nhanh và nhỏ khó bắt, huyết áp tụt sâu còn 55/35 mmHg.", true),
                PhysicalExamSystemItem("sys_resp", "Hô hấp & Đường thở", "Tiếng thở rít thanh quản Stridor rõ khi hít vào, phổi nghe nhiều rale rít rale ngáy cả hai phế trường, co kéo hõm ức cơ liên sườn.", true),
                PhysicalExamSystemItem("sys_gi", "Tiêu hóa & Ổ bụng", "Buồn nôn, nôn khan 1 lần, bụng mềm không chướng, đau bụng quặn từng cơn do co thắt cơ trơn tiêu hóa.", true),
                PhysicalExamSystemItem("sys_neuro", "Thần kinh", "GCS 11 điểm, vật vã lo âu hoảng loạn do thiếu oxy não nặng, không dấu thần kinh khu trú.", true),
                PhysicalExamSystemItem("sys_urinary", "Tiết niệu", "Chưa buồn tiểu, bàng quang xẹp.", false)
            ),
            availableLabs = labs,
            goldenDiagnosis = "[T78.2] Sốc phản vệ độ III (nguy kịch) do thuốc tiêm Ceftriaxone (ICD-10 06/2026/TT-BYT | CCMS)",
            diagnosisKeywords = listOf("sốc phản vệ", "t78", "t88", "ceftriaxone", "dị ứng", "adrenaline"),
            goldenDifferentials = listOf("Cơn hen phế quản ác tính", "Phù thanh quản dị vật đường thở", "Nhồi máu cơ tim cấp gây sốc tim"),
            goldenClinicalReasoning = "Bệnh nhân xuất hiện hội chứng suy hô hấp và suy tuần hoàn tối khẩn cấp trong vòng 5 phút sau tiêm tĩnh mạch Ceftriaxone (thở rít thanh quản Stridor, phù Quincke, HA tụt 55/35 mmHg). Đây là Sốc phản vệ độ III đe dọa tử vong trong vài phút. Thuốc duy nhất cứu mạng đầu tay theo Thông tư 51/2017/TT-BYT là Adrenaline 1mg tiêm bắp sâu 0.5ml ngay lập tức, tuyệt đối không trì hoãn chờ corticoid.",
            standardOrders = orders,
            badDelayNarrative = "Không tiêm Adrenaline ngay mà dùng thuốc khác làm thanh quản phù nề bít tắc hoàn toàn, bệnh nhân ngưng thở và ngừng tuần hoàn không hồi phục.",
            badDelayVitals = VitalSigns(0, 0, 0, 0, 0, 36.5f, 3, EcgRhythm.ASYSTOLE),
            successfulRescueNarrative = "Bác sĩ phản xạ xuất sắc: tiêm bắp ngay 0.5ml Adrenaline vào đùi, áp mask oxy 15L/p và xả dịch tinh thể. Sau 3 phút tiếng rít giảm hẳn, huyết áp nâng lên 100/65 mmHg, bệnh nhân thoát khỏi cửa tử!",
            successVitals = VitalSigns(95, 115, 70, 98, 18, 36.8f, 15, EcgRhythm.NORMAL_SINUS),
            clinicalPearls = "Quy tắc sống còn trong Sốc phản vệ: ADRENALINE LÀ THUỐC ĐẦU TAY DUY NHẤT. Tiêm bắp sâu mặt trước ngoài đùi ngay khi nghi ngờ sốc phản vệ độ II-III. Corticoid và Kháng histamin chỉ là thuốc phụ trợ ngăn ngừa phản vệ pha 2, không có tác dụng cứu sống tức thì.",
            gratitudeSpeaker = "Cô giáo Lê Thị Bích Thủy & Chồng",
            gratitudeMessage = "Tôi tưởng chừng như mình đã nghẹt thở chết rồi, may nhờ có mũi tiêm Adrenaline thần tốc của bác sĩ! Vợ chồng tôi biết ơn bác sĩ suốt đời!"
        )
    }

    // =========================================================================
    // CASE 16: NGỘ ĐỘC CẤP KHÍ CARBON MONOXIDE (CO) DO ĐỐT THAN SƯỞI ẤM
    // ICD-10: T58 (Ngộ độc khí Carbon Monoxide)
    // =========================================================================
    private fun createCase16CarbonMonoxidePoisoning(baseLabListFactory: (String, Boolean, String, Boolean, String, Boolean, String, Boolean, String, Boolean, String, String, String, String) -> List<LabTestItem>): PatientCase {
        val labs = baseLabListFactory(
            "Nhịp nhanh xoang 120 l/p, đoạn ST chênh xuống nhẹ ở V4-V6 phản ánh tình trạng thiếu máu cơ tim do khí CO chiếm giữ Hemoglobin.", false,
            "hs-cTnI: 68 ng/L (tăng phản ánh tổn thương cơ tim do thiếu oxy mô).", true,
            "pH 7.21, PaO2 95 mmHg (đo áp lực hòa tan giả tạo bình thường), PaCO2 28 mmHg, HCO3- 11 mmol/L, COHb đo bằng Co-oximetry: 38% (bình thường < 2%).", true,
            "Lactate máu: 6.8 mmol/L (tăng cực cao do tế bào bị ngạt, ức chế chuỗi hô hấp tế bào cytochrom oxidase).", true,
            "WBC 11.5 G/L, Hb 140 g/L, Hct 42%.", false,
            "5.5 mmol/L.",
            "Phổi sáng, không thâm nhiễm đông đặc.",
            "Không bất thường.",
            "Bình thường."
        )

        val orders = listOf(
            EmergencyOrder(
                id = "o_o2_mask",
                category = OrderCategory.AIRWAY,
                name = "Thở oxy 100% nồng độ cao qua mask có túi dự trữ 15 lít/phút",
                description = "Tăng đào thải COHb khẩn cấp, rút ngắn thời gian bán hủy CO từ 320 phút xuống 70 phút",
                costVnd = 80000L,
                suppliesUsed = listOf("Mask có túi dự trữ", "Bình oxy áp lực"),
                isEssential = true,
                feedbackOnExecution = "Đã áp mask oxy 100% dòng 15L/p kín khít, oxy tinh khiết cạnh tranh đẩy khí CO ra khỏi Hemoglobin."
            ),
            EmergencyOrder(
                id = "o_intubation",
                category = OrderCategory.AIRWAY,
                name = "Đặt nội khí quản thở máy bảo vệ đường thở (nếu GCS ≤ 8)",
                description = "Chỉ định kiểm soát đường thở và thông khí nhân tạo",
                costVnd = 650000L,
                suppliesUsed = listOf("Ống NKQ số 7.0", "Đèn soi thanh quản", "Máy thở"),
                isEssential = true,
                feedbackOnExecution = "Đặt NKQ thành công, kết nối máy thở FiO2 100%, bảo vệ đường thở tuyệt đối."
            ),
            EmergencyOrder(
                id = "o_fluids_nacl",
                category = OrderCategory.MEDICATION,
                name = "Truyền tĩnh mạch NaCl 0.9% 500ml duy trì huyết động",
                description = "Hỗ trợ tưới máu vi tuần hoàn",
                costVnd = 85000L,
                suppliesUsed = listOf("Chai NaCl 0.9% 500ml", "Dây truyền dịch"),
                isEssential = false,
                feedbackOnExecution = "Đã truyền NaCl 0.9% duy trì huyết áp ổn định."
            ),
            EmergencyOrder(
                id = "c_poison_icu",
                category = OrderCategory.CONSULTATION,
                name = "Chuyển gấp Trung tâm Chống độc / ICU điều trị Oxy Cao Áp (HBOT)",
                description = "Chỉ định vàng điều trị ngộ độc CO nặng ngăn ngừa di chứng thần kinh muộn (DNS)",
                costVnd = 500000L,
                suppliesUsed = listOf("Hồ sơ hội chẩn chống độc", "Xe vận chuyển có máy thở"),
                isEssential = true,
                feedbackOnExecution = "Trung tâm Chống độc tiếp nhận khẩn cấp bệnh nhân vào buồng Oxy cao áp (HBOT)!"
            ),
            EmergencyOrder(
                id = "o_harmful_sedative",
                category = OrderCategory.MEDICATION,
                name = "Tiêm thuốc an thần Diazepam tĩnh mạch",
                description = "Thuốc an thần",
                costVnd = 35000L,
                suppliesUsed = listOf("Ống Diazepam"),
                isEssential = false,
                isHarmful = true,
                feedbackOnExecution = "CẢNH BÁO NGUY HIỂM: Bệnh nhân đang hôn mê thiếu oxy não, tiêm an thần làm suy hô hấp ngừng thở hoàn toàn!"
            )
        )

        return PatientCase(
            id = "case_co_poisoning_16",
            patientName = "Hoàng Thị Mai Lan",
            age = 24,
            gender = "Nữ",
            occupation = "Nhân viên văn phòng",
            triageLevel = TriageLevel.RED,
            chiefComplaint = "Đốt than sưởi ấm trong phòng kín mùa đông, được phát hiện hôn mê sâu, da môi đỏ như quả anh đào, thở nhanh nông",
            arrivalTime = "06:40",
            initialVitals = VitalSigns(120, 100, 65, 96, 26, 36.2f, 8, EcgRhythm.NORMAL_SINUS),
            abcde = AbcdeAssessment(
                airwayDesc = "Tăng tiết đờm dãi, tụt lưỡi gây ngáy tắc nghẽn đường thở trên",
                airwayIsClear = false,
                breathingDesc = "Thở nhanh nông 26 l/p, SpO2 đo máy thường 96% (sai số giả do COHb gắn ánh sáng)",
                breathingIsNormal = false,
                circulationDesc = "Mạch nhanh 120 l/p, HA 100/65 mmHg, môi và móng tay màu đỏ hồng cherry-red",
                circulationIsNormal = false,
                disabilityDesc = "Hôn mê GCS 8 điểm (E2V2M4), đồng tử 2 bên 3mm phản xạ ánh sáng yếu",
                disabilityIsNormal = false,
                exposureDesc = "Toàn thân da đỏ hồng bất thường, mùi khói than nồng nặc quanh quần áo",
                exposureIsNormal = false
            ),
            historyQuestions = listOf(
                PatientHistoryQA("q1", "Hoàn cảnh phát hiện bệnh nhân?", "Trời lạnh quá, tối qua 2 mẹ con đốt lò than hoa để sưởi trong phòng ngủ đóng kín cửa. Sáng nay người nhà gọi không ai thưa, phá cửa vào thì thấy cả hai mẹ con hôn mê!", "Người nhà bệnh nhân"),
                PatientHistoryQA("q2", "Bệnh nhân hôn mê bao lâu rồi?", "Chắc phải ngạt khói than suốt từ nửa đêm tới sáng, khoảng 5-6 tiếng rồi bác sĩ!", "Hàng xóm đưa vào"),
                PatientHistoryQA("q3", "Người mẹ đi cùng hiện thế nào?", "Người mẹ cũng đang cấp cứu ở phòng bên cạnh, tình trạng rất nặng!", "Điều dưỡng báo cáo")
            ),
            physicalExams = listOf(
                PhysicalExamSystemItem("sys_general", "Toàn thân & Da niêm", "Bệnh nhân hôn mê sâu, dấu hiệu kinh điển: môi, niêm mạc miệng và các đầu ngón tay màu đỏ như quả anh đào (cherry-red color), da ẩm lạnh.", true),
                PhysicalExamSystemItem("sys_cardio", "Tuần hoàn", "Nhịp tim nhanh 120 l/p, mạch quay căng, huyết áp 100/65 mmHg, nghe tim T1 T2 rõ.", true),
                PhysicalExamSystemItem("sys_resp", "Hô hấp", "Thở nhanh nông 26 l/p, có tiếng rít thanh quản do ứ đọng đờm và tụt lưỡi, phổi nghe rale ẩm rải rác.", true),
                PhysicalExamSystemItem("sys_gi", "Tiêu hóa", "Bụng mềm, không chướng, tăng tiết nước bọt khoang miệng.", false),
                PhysicalExamSystemItem("sys_neuro", "Thần kinh", "Hôn mê GCS 8 điểm, giảm trương lực cơ toàn thân, phản xạ gân xương giảm, dấu Babinski âm tính.", true),
                PhysicalExamSystemItem("sys_urinary", "Tiết niệu", "Sonde tiểu ra 150ml nước tiểu sẫm màu.", false)
            ),
            availableLabs = labs,
            goldenDiagnosis = "[T58] Ngộ độc khí Carbon Monoxide (CO) mức độ nặng có toan chuyển hóa [G93.1] (ICD-10 CCMS)",
            diagnosisKeywords = listOf("carbon monoxide", "t58", "co", "ngộ độc", "khí than", "toan chuyển hóa"),
            goldenDifferentials = listOf("Ngộ độc khí Cyanide", "Ngộ độc thuốc an thần gây ngủ", "Đột quỵ xuất huyết não cấp"),
            goldenClinicalReasoning = "Bệnh nhân có tiền sử đốt than sưởi phòng kín, hôn mê sâu GCS 8đ, da niêm đỏ cherry-red, toan chuyển hóa tăng acid lactic nặng (pH 7.21, Lactate 6.8). Khí CO kết hợp ái lực cực mạnh với Hb gấp 250 lần oxy tạo Carboxyhemoglobin (COHb = 38%). Chỉ định bắt buộc là thở oxy 100% nồng độ cao ngay và chuyển buồng Oxy Cao Áp (HBOT) để phòng ngừa hội chứng tổn thương thần kinh muộn (DNS). Lưu ý máy đo SpO2 thông thường không phân biệt được OxyHb và COHb nên cho kết quả giả bình thường.",
            standardOrders = orders,
            badDelayNarrative = "Không cho thở oxy 100% nồng độ cao và không chuyển buồng oxy cao áp làm tổn thương não thiếu oxy lan tỏa, bệnh nhân chuyển sang trạng thái sống thực vật vĩnh viễn.",
            badDelayVitals = VitalSigns(110, 90, 60, 92, 20, 36.5f, 4, EcgRhythm.NORMAL_SINUS),
            successfulRescueNarrative = "Bác sĩ nhận định chính xác hội chứng ngộ độc CO, lập tức đặt NKQ thở oxy 100% và chuyển thẳng Trung tâm Chống độc vào buồng Oxy Cao Áp (HBOT). Nồng độ COHb giảm nhanh về 3%, bệnh nhân tỉnh táo hoàn toàn không di chứng não!",
            successVitals = VitalSigns(78, 115, 75, 99, 16, 36.8f, 15, EcgRhythm.NORMAL_SINUS),
            clinicalPearls = "Bẫy lâm sàng nguy hiểm trong ngộ độc CO: Máy đo SpO2 kẹp ngón tay cho kết quả giả tạo bình thường vì máy không phân biệt được Carboxyhemoglobin và Oxyhemoglobin. Cần làm khí máu có Co-oximetry đo trực tiếp %COHb. Điều trị đặc hiệu duy nhất là OXY 100% VÀ OXY CAO ÁP (HBOT).",
            gratitudeSpeaker = "Bệnh nhân Hoàng Thị Mai Lan & Bố",
            gratitudeMessage = "Con tôi được bác sĩ cứu sống kịp thời và chuyển vào buồng oxy cao áp, cháu đã tỉnh táo và nói chuyện bình thường! Gia đình mang ơn các bác sĩ nhiều lắm!"
        )
    }

    // =========================================================================
    // CASE 17: NGỘ ĐỘC CẤP PARACETAMOL LIỀU CAO (15G) TỰ TỬ
    // ICD-10: T39.1 (Ngộ độc dẫn xuất 4-Aminophenol / Paracetamol)
    // =========================================================================
    private fun createCase17ParacetamolOverdose(baseLabListFactory: (String, Boolean, String, Boolean, String, Boolean, String, Boolean, String, Boolean, String, String, String, String) -> List<LabTestItem>): PatientCase {
        val labs = baseLabListFactory(
            "Nhịp xoang 95 l/p, đều, không rối loạn dẫn truyền.", false,
            "hs-cTnI: 10 ng/L (bình thường).", false,
            "pH 7.33, PaO2 90 mmHg, PaCO2 35 mmHg, HCO3- 18 mmol/L, Lactate 3.2 mmol/L (Toan chuyển hóa nhẹ do hoại tử tế bào gan).", true,
            "Lactate: 3.2 mmol/L.", true,
            "WBC 8.5 G/L, Hb 138 g/L, Tiểu cầu 190 G/L.", false,
            "5.2 mmol/L.",
            "X-quang ngực thẳng bình thường.",
            "Không liềm hơi, không chướng hơi bất thường.",
            "Bình thường."
        )

        val orders = listOf(
            EmergencyOrder(
                id = "o_nac_infusion",
                category = OrderCategory.MEDICATION,
                name = "N-Acetylcysteine (NAC) truyền tĩnh mạch theo phác đồ 21 giờ giải độc Paracetamol",
                description = "Thuốc giải độc đặc hiệu phục hồi Glutathione dự trữ của gan, trung hòa chất độc NAPQI",
                costVnd = 450000L,
                suppliesUsed = listOf("Lọ dung dịch NAC truyền tĩnh mạch", "Chai Glucose 5% 500ml", "Dây truyền dịch"),
                isEssential = true,
                feedbackOnExecution = "Bắt đầu truyền NAC liều tải 150 mg/kg trong 60 phút, bảo vệ tế bào gan tối đa."
            ),
            EmergencyOrder(
                id = "op_gastric_lavage",
                category = OrderCategory.PROCEDURE,
                name = "Đặt sonde Faucher rửa dạ dày cấp cứu & bơm than hoạt tính 50g",
                description = "Loại bỏ lượng thuốc Paracetamol còn tồn dư trong dạ dày",
                costVnd = 420000L,
                suppliesUsed = listOf("Ống Faucher", "Bình nước rửa 5L", "Chai than hoạt tính 50g"),
                isEssential = true,
                feedbackOnExecution = "Rửa dạ dày ra nhiều bột thuốc màu trắng đục, đã bơm than hoạt tính hấp phụ độc chất."
            ),
            EmergencyOrder(
                id = "o_fluids_nacl",
                category = OrderCategory.MEDICATION,
                name = "Truyền tĩnh mạch Glucose 5% 500ml + NaCl 0.9% 500ml",
                description = "Bù dịch hỗ trợ chuyển hóa và tưới máu gan thận",
                costVnd = 90000L,
                suppliesUsed = listOf("Chai Glucose 5%", "Chai NaCl 0.9%"),
                isEssential = false,
                feedbackOnExecution = "Đã thiết lập đường truyền dịch tĩnh mạch ổn định."
            ),
            EmergencyOrder(
                id = "c_poison_icu",
                category = OrderCategory.CONSULTATION,
                name = "Chuyển khoa Hồi sức cấp cứu / Trung tâm Chống độc theo dõi men gan và đông máu",
                description = "Theo dõi nguy cơ suy gan tối cấp và hội chứng não gan",
                costVnd = 300000L,
                suppliesUsed = listOf("Hồ sơ chuyển viện chống độc"),
                isEssential = true,
                feedbackOnExecution = "Khoa Chống độc tiếp nhận bệnh nhân, lên lịch xét nghiệm lại AST, ALT, INR mỗi 12h."
            ),
            EmergencyOrder(
                id = "o_harmful_paracetamol",
                category = OrderCategory.MEDICATION,
                name = "Tiêm thêm thuốc hạ sốt Paracetamol 1g tĩnh mạch",
                description = "Thuốc giảm đau hạ sốt",
                costVnd = 50000L,
                suppliesUsed = listOf("Chai Paracetamol 1g"),
                isEssential = false,
                isHarmful = true,
                feedbackOnExecution = "THẢM HỌA: Bệnh nhân đang ngộ độc Paracetamol quá liều mà truyền thêm Paracetamol gây hoại tử tế bào gan ồ ạt, tử vong do suy gan cấp!"
            )
        )

        return PatientCase(
            id = "case_paracetamol_17",
            patientName = "Đỗ Minh Quân",
            age = 19,
            gender = "Nam",
            occupation = "Sinh viên đại học",
            triageLevel = TriageLevel.YELLOW,
            chiefComplaint = "Uống 30 viên Paracetamol 500mg (15g) cách 4 tiếng tự tử, buồn nôn, đau tức vùng hạ sườn phải",
            arrivalTime = "20:30",
            initialVitals = VitalSigns(95, 110, 70, 98, 18, 36.7f, 15, EcgRhythm.NORMAL_SINUS),
            abcde = AbcdeAssessment(
                airwayDesc = "Đường thở thông thoáng, tự thở êm",
                airwayIsClear = true,
                breathingDesc = "Thở 18 l/p, SpO2 98%, rì rào phế nang rõ 2 bên",
                breathingIsNormal = true,
                circulationDesc = "Mạch 95 l/p đều rõ, HA 110/70 mmHg, đầu chi ấm hồng",
                circulationIsNormal = true,
                disabilityDesc = "Tỉnh táo GCS 15 điểm, tâm lý bi quan, buồn bã",
                disabilityIsNormal = true,
                exposureDesc = "Đau tức vùng thượng vị và hạ sườn phải, không ban da, không xuất huyết",
                exposureIsNormal = true
            ),
            historyQuestions = listOf(
                PatientHistoryQA("q1", "Bạn đã uống bao nhiêu viên và lúc mấy giờ?", "Em uống 30 viên Panadol xanh loại 500mg, tổng cộng 15 gam, uống lúc 16h30 chiều sau khi cãi nhau với bạn gái...", "Bệnh nhân tự thú nhận"),
                PatientHistoryQA("q2", "Sau khi uống bạn có nôn ra được viên nào không?", "Em có buồn nôn nhưng không nôn ra được, bụng bắt đầu cồn cào đau tức nên em sợ quá gọi bạn đưa đi viện.", "Bệnh nhân kể"),
                PatientHistoryQA("q3", "Bạn có uống kèm theo rượu bia hoặc thuốc ngủ gì không?", "Dạ không, em chỉ uống thuốc với nước lọc thôi ạ.", "Bệnh nhân khai")
            ),
            physicalExams = listOf(
                PhysicalExamSystemItem("sys_general", "Toàn thân", "Bệnh nhân tỉnh táo, tiếp xúc tốt, da niêm mạc bình thường, chưa thấy vàng da hay xuất huyết dưới da.", false),
                PhysicalExamSystemItem("sys_cardio", "Tuần hoàn", "Nhịp tim đều rõ 95 l/p, HA 110/70 mmHg, mạch quay nảy tốt.", false),
                PhysicalExamSystemItem("sys_resp", "Hô hấp", "Thở 18 l/p, SpO2 98%, không co kéo, phổi trong không rale.", false),
                PhysicalExamSystemItem("sys_gi", "Tiêu hóa & Gan mật", "Bụng mềm, ấn tức nhẹ vùng hạ sườn phải và thượng vị, gan lách chưa sờ chạm, không có phản ứng thành bụng.", true),
                PhysicalExamSystemItem("sys_neuro", "Thần kinh", "GCS 15 điểm, không rối loạn định hướng, không run vẫy Flapping tremor (chưa có hội chứng não gan).", false),
                PhysicalExamSystemItem("sys_urinary", "Tiết niệu", "Tiểu tiện bình thường, nước tiểu vàng trong.", false)
            ),
            availableLabs = labs,
            goldenDiagnosis = "[T39.1] Ngộ độc cấp tính dẫn xuất 4-Aminophenol (Paracetamol) liều cao 15g [K71.1] (ICD-10 CCMS)",
            diagnosisKeywords = listOf("paracetamol", "t39.1", "acetaminophen", "ngộ độc", "nac", "gan"),
            goldenDifferentials = listOf("Viêm gan virus cấp", "Ngộ độc thuốc an thần nhóm Benzodiazepine", "Viêm dạ dày cấp tính"),
            goldenClinicalReasoning = "Bệnh nhân uống liều độc Paracetamol 15g (> 150 mg/kg), chất chuyển hóa độc NAPQI làm cạn kiệt Glutathione nội sinh của gan, gây hoại tử tế bào gan cấp tính. Chỉ định vàng tối khẩn là dùng chất giải độc đặc hiệu N-ACETYLCYSTEINE (NAC) truyền tĩnh mạch theo phác đồ 21 giờ trong vòng 8 giờ đầu để ngăn ngừa 100% nguy cơ suy gan cấp.",
            standardOrders = orders,
            badDelayNarrative = "Không dùng thuốc giải độc NAC kịp thời trong 8 giờ đầu, chất độc NAPQI phá hủy toàn bộ tế bào gan, bệnh nhân rơi vào suy gan tối cấp, rối loạn đông máu nặng và tử vong sau 4 ngày.",
            badDelayVitals = VitalSigns(130, 80, 50, 93, 28, 38.5f, 9, EcgRhythm.NORMAL_SINUS),
            successfulRescueNarrative = "Bác sĩ chỉ định truyền ngay thuốc giải độc đặc hiệu NAC theo phác đồ chuẩn 21h và bơm than hoạt tính. Nồng độ Glutathione được bảo tồn, men gan không tăng, bệnh nhân hồi phục hoàn toàn không di chứng gan!",
            successVitals = VitalSigns(75, 120, 75, 99, 16, 36.6f, 15, EcgRhythm.NORMAL_SINUS),
            clinicalPearls = "Quy tắc vàng điều trị ngộ độc Paracetamol: Thời gian là tính mạng. Dùng N-Acetylcysteine (NAC) càng sớm càng tốt, hiệu quả bảo vệ gan gần như 100% nếu dùng trong vòng 8 giờ đầu sau uống. Không đợi kết quả men gan tăng mới dùng NAC vì khi men gan tăng thì gan đã bị hoại tử tế bào.",
            gratitudeSpeaker = "Bệnh nhân Đỗ Minh Quân & Bố Mẹ",
            gratitudeMessage = "Em dại dột quá, may nhờ bác sĩ đã tận tình rửa ruột và truyền thuốc giải độc cứu sống lá gan của em. Em xin hứa từ nay sẽ trân trọng cuộc sống!"
        )
    }

    // =========================================================================
    // CASE 18: NGỘ ĐỘC CẤP RƯỢU CỒN CÔNG NGHIỆP METHANOL NẶNG
    // ICD-10: T51.1 (Ngộ độc Methanol) | H46 (Viêm dây thần kinh thị giác)
    // =========================================================================
    private fun createCase18MethanolPoisoning(baseLabListFactory: (String, Boolean, String, Boolean, String, Boolean, String, Boolean, String, Boolean, String, String, String, String) -> List<LabTestItem>): PatientCase {
        val labs = baseLabListFactory(
            "Nhịp nhanh xoang 115 l/p, biến đổi ST chênh xuống thiếu máu cơ tim do toan chuyển hóa nặng.", false,
            "hs-cTnI: 42 ng/L (tăng nhẹ).", false,
            "pH 7.05 (toan máu cực nặng đe dọa ngừng tim), PaO2 95 mmHg, PaCO2 18 mmHg (thở bù Kussmaul), HCO3- 5.0 mmol/L, Khoảng trống Anion (Anion Gap) 32 mEq/L (tăng vọt do Acid Formic tích tụ).", true,
            "Lactate máu: 5.8 mmol/L.", true,
            "WBC 12.0 G/L, Hb 145 g/L.", false,
            "4.8 mmol/L.",
            "Phổi sáng, không thâm nhiễm đông đặc.",
            "Không bất thường.",
            "Nước tiểu toan tính mạnh, pH 5.0."
        )

        val orders = listOf(
            EmergencyOrder(
                id = "o_bicarbonate",
                category = OrderCategory.MEDICATION,
                name = "Natri Bicarbonat 8.4% 100ml - 200ml truyền tĩnh mạch nâng pH máu > 7.30",
                description = "Chống toan máu nặng đe dọa ngừng tim và kiềm hóa máu giảm chuyển Formic vào mô não/mắt",
                costVnd = 120000L,
                suppliesUsed = listOf("2 chai Natri Bicarbonat 8.4% 100ml", "Dây truyền dịch"),
                isEssential = true,
                feedbackOnExecution = "Bắt đầu truyền nhanh Natri Bicarbonat 8.4%, pH máu nâng lên 7.28, nhịp thở bớt co kéo."
            ),
            EmergencyOrder(
                id = "o_ethanol_antidote",
                category = OrderCategory.MEDICATION,
                name = "Ethanol 20% uống hoặc truyền qua sonde dạ dày (Chất giải độc cạnh tranh men ADH)",
                description = "Ức chế cạnh tranh men Alcohol Dehydrogenase ngăn Methanol chuyển hóa thành Acid Formic độc hại",
                costVnd = 180000L,
                suppliesUsed = listOf("Dung dịch Ethanol y tế 20%", "Ống sonde dạ dày"),
                isEssential = true,
                feedbackOnExecution = "Đã bơm Ethanol qua sonde dạ dày, ức chế men ADH, ngăn chặn việc sản sinh thêm Acid Formic."
            ),
            EmergencyOrder(
                id = "o_o2_mask",
                category = OrderCategory.AIRWAY,
                name = "Thở oxy qua mask có túi dự trữ 10 lít/phút",
                description = "Hỗ trợ hô hấp trong tình trạng thở Kussmaul gắng sức toan chuyển hóa",
                costVnd = 80000L,
                suppliesUsed = listOf("Mask thở có túi"),
                isEssential = true,
                feedbackOnExecution = "Đã hỗ trợ oxy mask túi, giảm bớt kiệt sức cơ hô hấp."
            ),
            EmergencyOrder(
                id = "c_poison_icu",
                category = OrderCategory.CONSULTATION,
                name = "Báo động đỏ chuyển khẩn cấp Lọc Máu Thẩm Tách (HD) tại Khoa Hồi Sức Tích Cực (ICU)",
                description = "Biện pháp sống còn số 1 đào thải nhanh Methanol và Acid Formic, cứu sống thị lực và tính mạng",
                costVnd = 1500000L,
                suppliesUsed = listOf("Hồ sơ hội chẩn lọc máu cấp cứu", "Bộ dây lọc máu HD"),
                isEssential = true,
                feedbackOnExecution = "Kíp lọc máu ICU tiếp nhận bệnh nhân ngay tại giường, bắt đầu chạy thận nhân tạo HD cấp cứu!"
            ),
            EmergencyOrder(
                id = "o_harmful_delay_ward",
                category = OrderCategory.CONSULTATION,
                name = "Chuyển bệnh nhân lên khoa Khám mắt hoặc khoa Nội tổng hợp theo dõi thông thường",
                description = "Chuyển khoa thường khi toan máu nặng",
                costVnd = 100000L,
                suppliesUsed = listOf("Phiếu chuyển viện"),
                isEssential = false,
                isHarmful = true,
                feedbackOnExecution = "SAI LẦM CHẾT NGƯỜI: Chậm trễ lọc máu làm Acid Formic phá hủy vĩnh viễn dây thần kinh thị giác gây mù mắt và phù não tử vong!"
            )
        )

        return PatientCase(
            id = "case_methanol_18",
            patientName = "Vũ Đình Trọng",
            age = 48,
            gender = "Nam",
            occupation = "Thợ xây dựng",
            triageLevel = TriageLevel.RED,
            chiefComplaint = "Uống rượu trắng không rõ nguồn gốc 1 ngày trước, vào viện vì nhìn mờ như có bão tuyết, đau đầu dữ dội, thở nhanh sâu Kussmaul",
            arrivalTime = "08:15",
            initialVitals = VitalSigns(115, 105, 65, 95, 30, 36.4f, 12, EcgRhythm.NORMAL_SINUS),
            abcde = AbcdeAssessment(
                airwayDesc = "Đường thở thông thoáng, thở sâu nhanh Kussmaul mùi rượu chua nồng",
                airwayIsClear = true,
                breathingDesc = "Thở sâu nhanh Kussmaul 30 l/p bù toan máu, SpO2 95%",
                breathingIsNormal = false,
                circulationDesc = "Mạch 115 l/p, HA 105/65 mmHg, đầu chi ẩm",
                circulationIsNormal = false,
                disabilityDesc = "GCS 12 điểm (E3V4M5), kích thích, thị lực giảm nặng chỉ còn đếm ngón tay 1 mét",
                disabilityIsNormal = false,
                exposureDesc = "Đồng tử hai bên giãn 5mm, phản xạ ánh sáng kém (dấu hiệu tổn thương thần kinh thị giác)",
                exposureIsNormal = false
            ),
            historyQuestions = listOf(
                PatientHistoryQA("q1", "Bệnh nhân đã uống rượu gì và khi nào?", "Trưa hôm qua đi ăn giỗ có uống khoảng nửa lít rượu trắng mua ở quán tạp hóa không có nhãn mác. Sáng nay ngủ dậy kêu mắt mờ tịt không thấy đường rồi thở hổn hển!", "Vợ bệnh nhân"),
                PatientHistoryQA("q2", "Mắt bệnh nhân nhìn mờ thế nào?", "Bác sĩ ơi, trước mắt tôi trắng xóa như có bão tuyết rơi, không nhìn rõ mặt bác sĩ, đầu đau như búa bổ!", "Bệnh nhân kêu cứu"),
                PatientHistoryQA("q3", "Có ai cùng uống rượu bị triệu chứng như vậy không?", "Nghe nói có 1 người cùng bàn tiệc sáng nay cũng vừa phải nhập viện cấp cứu ở huyện!", "Vợ bệnh nhân bổ sung")
            ),
            physicalExams = listOf(
                PhysicalExamSystemItem("sys_general", "Toàn thân", "Bệnh nhân kích thích vật vã, hơi thở mùi rượu nồng nặc kèm mùi ceton chua, da lạnh ẩm, vẻ mặt hốt hoảng sợ hãi.", true),
                PhysicalExamSystemItem("sys_cardio", "Tuần hoàn", "Nhịp tim nhanh 115 l/p, HA 105/65 mmHg, tiếng tim rõ, mạch nhanh.", true),
                PhysicalExamSystemItem("sys_resp", "Hô hấp", "Kiểu thở kinh điển Kussmaul: thở rất sâu và nhanh 30 l/p nhằm đào thải CO2 bù trừ toan chuyển hóa nặng, phổi trong không rale.", true),
                PhysicalExamSystemItem("sys_gi", "Tiêu hóa", "Đau tức thượng vị, buồn nôn, bụng mềm.", false),
                PhysicalExamSystemItem("sys_neuro", "Thần kinh & Thị giác", "GCS 12 điểm. Khám mắt: Đồng tử hai bên giãn 5mm, phản xạ ánh sáng rất yếu, thị lực suy giảm nghiêm trọng (Snowfield vision), soi đáy mắt phù gai thị cấp.", true),
                PhysicalExamSystemItem("sys_urinary", "Tiết niệu", "Nước tiểu vàng đậm, pH nước tiểu toan tính.", false)
            ),
            availableLabs = labs,
            goldenDiagnosis = "[T51.1] Ngộ độc cấp tính Methanol (Cồn công nghiệp) có toan chuyển hóa tăng Anion Gap nặng [H46] (ICD-10 CCMS)",
            diagnosisKeywords = listOf("methanol", "t51.1", "cồn công nghiệp", "ngộ độc rượu", "toan chuyển hóa", "mù mắt"),
            goldenDifferentials = listOf("Ngộ độc rượu thông thường Ethanol", "Đái tháo đường nhiễm toan Ceton (DKA)", "Ngộ độc Ethylene Glycol"),
            goldenClinicalReasoning = "Bệnh nhân uống rượu không rõ nguồn gốc, biểu hiện tam chứng kinh điển ngộ độc Methanol: Rối loạn thị giác nhìn mờ như bão tuyết (Snowfield vision) + Thở sâu Kussmaul + Toan chuyển hóa tăng khoảng trống Anion cực nặng (pH 7.05, HCO3- 5 mmol/L, Anion Gap 32). Methanol được men ADH chuyển thành Acid Formic độc hại gây hủy hoại thần kinh thị giác và phù não. Chỉ định sống còn bắt buộc: Kiềm hóa máu bằng Natri Bicarbonat + Dùng Ethanol ức chế men ADH + Báo động đỏ LỌC MÁU CẤP CỨU (HD) ngay để cứu tính mạng và giữ lại thị lực.",
            standardOrders = orders,
            badDelayNarrative = "Không lọc máu cấp cứu mà chuyển khoa theo dõi thường, Acid Formic phá hủy hoàn toàn dây thần kinh thị giác gây mù hai mắt vĩnh viễn và phù não tử vong sau 12 giờ.",
            badDelayVitals = VitalSigns(135, 75, 45, 88, 36, 38.0f, 6, EcgRhythm.NORMAL_SINUS),
            successfulRescueNarrative = "Bác sĩ xử trí cực kỳ chuẩn xác: truyền ngay Natri Bicarbonat chống toan, bơm Ethanol qua sonde và kích hoạt lọc máu ngắt quãng HD tối khẩn. Acid Formic được loại bỏ hoàn toàn, thị lực bệnh nhân phục hồi 10/10, xuất viện khỏe mạnh!",
            successVitals = VitalSigns(80, 120, 75, 98, 16, 36.7f, 15, EcgRhythm.NORMAL_SINUS),
            clinicalPearls = "Ngộ độc rượu Methanol có thời gian ủ bệnh 12-24h trước khi bùng phát triệu chứng rầm rộ. Dấu hiệu chỉ điểm quý giá nhất là RỐI LOẠN THỊ GIÁC (nhìn mờ, ám điểm tuyết rơi). LỌC MÁU THẨM TÁCH (HD) LÀ PHƯƠNG PHÁP HIỆU QUẢ NHẤT để đào thải độc chất và cứu thị lực người bệnh.",
            gratitudeSpeaker = "Bệnh nhân Vũ Đình Trọng & Vợ",
            gratitudeMessage = "Tôi tưởng hai mắt mình đã bị mù vĩnh viễn và không qua khỏi, nhờ các bác sĩ lọc máu kịp thời mà mắt tôi lại sáng tỏ, nhìn thấy được vợ con! Xin đội ơn bác sĩ!"
        )
    }

    // =========================================================================
    // CASE 19: CHẤN THƯƠNG SỌ NÃO DO TAI NẠN GIAO THÔNG (TỤ MÁU NGOÀI MÀNG CỨNG - KHOẢNG TỈNH)
    // ICD-10: S06.4 (Tụ máu ngoài màng cứng do chấn thương) | ICD-10 06/2026/TT-BYT | CCMS
    // =========================================================================
    private fun createCase19TbiEpiduralHematoma(baseLabListFactory: (String, Boolean, String, Boolean, String, Boolean, String, Boolean, String, Boolean, String, String, String, String) -> List<LabTestItem>): PatientCase {
        val labs = baseLabListFactory(
            "Nhịp chậm xoang 52 l/p (Dấu hiệu phản xạ Cushing tăng áp lực nội sọ), không biến đổi ST-T.", true,
            "hs-cTnI: 10 ng/L (Bình thường < 14 ng/L).", false,
            "pH 7.36, PaO2 85 mmHg, PaCO2 44 mmHg (Ứ trệ CO2 do giảm thông khí trung tâm thần kinh).", false,
            "Lactate máu: 2.1 mmol/L.", false,
            "Hồng cầu 4.2 T/L, Hb 128 g/L, Hct 38%, WBC 12.5 G/L (Bạch cầu tăng phản ứng sau chấn thương).", false,
            "6.8 mmol/L (Đường huyết tăng phản ứng stress chấn thương).",
            "X-quang ngực thẳng: Phế trường sáng, bóng tim bình thường.",
            "X-quang bụng không chuẩn bị: Bình thường.",
            "Nước tiểu vàng trong, không tiểu máu."
        )

        val orders = listOf(
            EmergencyOrder(
                id = "p_intubate_tbi",
                category = OrderCategory.AIRWAY,
                name = "Đặt nội khí quản bảo vệ đường thở & Thở máy kiểm soát PaCO2 35 mmHg",
                description = "Chỉ định bắt buộc khi GCS <= 8 điểm và có nguy cơ hít sặc do hôn mê nôn vọt",
                costVnd = 350000L,
                suppliesUsed = listOf("Ống nội khí quản cuffed số 7.5", "Đèn soi thanh quản lưỡi cong", "Máy thở xâm lấn"),
                isEssential = true,
                feedbackOnExecution = "Đã đặt nội khí quản thành công thì đầu, kiểm soát thông khí PaCO2 ở mức 35-38 mmHg để co mạch não giảm áp lực nội sọ."
            ),
            EmergencyOrder(
                id = "o_elevate_head",
                category = OrderCategory.PROCEDURE,
                name = "Nằm đầu cao 30 độ, giữ trục đầu cổ thẳng trung gian",
                description = "Tạo điều kiện thuận lợi nhất cho hồi lưu tĩnh mạch não giảm áp lực trong sọ",
                costVnd = 0L,
                suppliesUsed = listOf("Giường hồi sức nâng góc"),
                isEssential = true,
                feedbackOnExecution = "Đã nâng đầu giường 30 độ, cố định cổ trung gian không vặn xoắn."
            ),
            EmergencyOrder(
                id = "o_mannitol",
                category = OrderCategory.MEDICATION,
                name = "Truyền nhanh Mannitol 20% liều 1g/kg tĩnh mạch trong 20 phút",
                description = "Liệu pháp thẩm thấu rút nước khỏi mô não chống tụt kẹt não cấp cứu",
                costVnd = 110000L,
                suppliesUsed = listOf("2 chai Mannitol 20% 250ml", "Dây truyền dịch"),
                isEssential = true,
                feedbackOnExecution = "Mannitol đang xả nhanh, đồng tử bên phải bắt đầu co nhẹ từ 5mm về 3.5mm."
            ),
            EmergencyOrder(
                id = "c_ct_brain_er",
                category = OrderCategory.PROCEDURE,
                name = "Chụp CT Scanner sọ não không tiêm cản quang cấp cứu",
                description = "Hình ảnh học tiêu chuẩn vàng xác định khối máu tụ ngoài màng cứng",
                costVnd = 950000L,
                suppliesUsed = listOf("Xe cáng vận chuyển có monitor theo dõi"),
                isEssential = true,
                feedbackOnExecution = "Hình ảnh CT sọ não: Thấu kính hai mặt lồi tăng tỷ trọng vùng thái dương - đỉnh phải dày 25mm, đè sụp não thất bên, đường giữa lệch trái 9mm!"
            ),
            EmergencyOrder(
                id = "c_neurosurg_red_alert",
                category = OrderCategory.CONSULTATION,
                name = "Báo động đỏ Hội chẩn Ngoại Thần kinh mở nắp sọ cấp cứu lấy máu tụ",
                description = "Chỉ định phẫu thuật tối khẩn cứu sống bệnh nhân trước khi tụt kẹt não tử vong",
                costVnd = 500000L,
                suppliesUsed = listOf("Hồ sơ bệnh án phẫu thuật cấp cứu"),
                isEssential = true,
                feedbackOnExecution = "Bác sĩ Ngoại Thần kinh trực viện có mặt ngay, đẩy thẳng bệnh nhân vào phòng mổ mở nắp sọ giải áp!"
            ),
            EmergencyOrder(
                id = "o_harmful_sedative_alone",
                category = OrderCategory.MEDICATION,
                name = "Tiêm thuốc an thần liều cao mà không đặt nội khí quản",
                description = "Ức chế hô hấp trên bệnh nhân chấn thương sọ não hôn mê",
                costVnd = 60000L,
                suppliesUsed = listOf("Bơm tiêm 5ml"),
                isEssential = false,
                isHarmful = true,
                feedbackOnExecution = "NGUY HIỂM: Ức chế trung tâm hô hấp làm ứ đọng PaCO2 khiến não phù nặng thêm, tụt kẹt hạnh nhân tiểu não tử vong!"
            )
        )

        return PatientCase(
            id = "case_tbi_edh_19",
            patientName = "Nguyễn Văn Hùng",
            age = 32,
            gender = "Nam",
            occupation = "Lái xe giao hàng",
            triageLevel = TriageLevel.RED,
            chiefComplaint = "Tai nạn giao thông ngã đập đầu, có khoảng tỉnh, đau đầu dữ dội nôn vọt rồi hôn mê sâu",
            arrivalTime = "21:30",
            initialVitals = VitalSigns(52, 175, 100, 93, 12, 37.0f, 8, EcgRhythm.NORMAL_SINUS),
            abcde = AbcdeAssessment(
                airwayDesc = "Tăng tiết đờm dãi hầu họng, ứ đọng chất nôn, có tiếng thở ngáy",
                airwayIsClear = false,
                breathingDesc = "Thở chậm không đều 12 l/p (nhịp thở kiểu Cheyne-Stokes), SpO2 93%",
                breathingIsNormal = false,
                circulationDesc = "Mạch chậm 52 l/p nảy căng, Huyết áp tăng vọt 175/100 mmHg (Tam chứng Cushing)",
                circulationIsNormal = false,
                disabilityDesc = "GCS tụt còn 8 điểm (E2V2M4), đồng tử phải giãn 5mm mất phản xạ ánh sáng, đồng tử trái 2.5mm",
                disabilityIsNormal = false,
                exposureDesc = "Vết rách da đầu bầm tím tụ máu lớn vùng thái dương phải, liệt nửa người bên trái",
                exposureIsNormal = false
            ),
            historyQuestions = listOf(
                PatientHistoryQA("q1", "Tai nạn giao thông xảy ra như thế nào?", "Anh ấy đi xe máy đâm vào dải phân cách lúc 19h tối, đập đầu bên phải xuống đường, lúc đó vẫn tỉnh dậy nói chuyện bình thường!", "Bạn đi cùng kể"),
                PatientHistoryQA("q2", "Diễn biến sau tai nạn ra sao?", "Về nhà được hơn 1 tiếng thì kêu đầu đau buốt như nứt sọ, nôn vọt ra thức ăn 3 lần liên tiếp rồi lơ mơ gọi không biết gì nữa!", "Người nhà hốt hoảng"),
                PatientHistoryQA("q3", "Có tiền sử bệnh tật gì không?", "Hoàn toàn khỏe mạnh, không có bệnh tim mạch hay dị ứng gì.", "Người nhà")
            ),
            physicalExams = listOf(
                PhysicalExamSystemItem("sys_general", "Toàn thân", "Bệnh nhân hôn mê, kích thích đau chỉ gạt tay yếu ớt, thở ngáy khò khè, da niêm hồng nhạt.", true),
                PhysicalExamSystemItem("sys_cardio", "Tuần hoàn", "Mạch chậm 52 l/p, huyết áp tăng rất cao 175/100 mmHg (Phản xạ Cushing điển hình do áp lực nội sọ tăng vọt).", true),
                PhysicalExamSystemItem("sys_resp", "Hô hấp", "Thở chậm không đều, biên độ thở nông sâu xen kẽ, có nguy cơ tụt lưỡi và hít sặc chất nôn.", true),
                PhysicalExamSystemItem("sys_gi", "Tiêu hóa & Bụng", "Bụng mềm, không trướng, không điểm đau chói.", false),
                PhysicalExamSystemItem("sys_neuro", "Thần kinh & Sọ não", "GCS 8 điểm. DẤU THẦN KINH KHU TRÚ: Đồng tử bên phải giãn to 5mm mất phản xạ ánh sáng (liệt dây III bên tổn thương do tụt kẹt thùy thái dương), liệt vận động nửa người bên trái (đối bên tổn thương). Khối máu tụ dưới da đầu vùng thái dương phải sưng nề lớn.", true),
                PhysicalExamSystemItem("sys_urinary", "Tiết niệu", "Sonde tiểu ra nước tiểu vàng trong.", false)
            ),
            availableLabs = labs,
            goldenDiagnosis = "[S06.4] Tụ máu ngoài màng cứng cấp tính do chấn thương sọ não (Khoảng tỉnh, tụt não) (ICD-10 06/2026/TT-BYT | CCMS)",
            diagnosisKeywords = listOf("s06.4", "ngoài màng cứng", "khoảng tỉnh", "tụt não", "chấn thương sọ não", "s06"),
            goldenDifferentials = listOf("Tụ máu dưới màng cứng cấp tính [S06.5]", "Xuất huyết dưới nhện do chấn thương [S06.6]", "Chấn động não [S06.0]"),
            goldenClinicalReasoning = "Bệnh nhân tai nạn giao thông có 'Khoảng tỉnh' kinh điển (Lucid Interval): tỉnh táo sau chấn thương rồi hôn mê tiến triển nhanh kèm Tam chứng Cushing (HA tăng vọt + Mạch chậm) và Dấu hiệu đồng tử bên phải giãn 5mm liệt nửa người trái. Đây là triệu chứng chỉ điểm của rách động mạch màng não giữa gây khối máu tụ ngoài màng cứng thái dương phải đè ép tụt kẹt não cấp tính. Chỉ định tối khẩn: Đặt nội khí quản bảo vệ đường thở + Mannitol chống tụt kẹt não + Chụp CT Scanner sọ não và BÁO ĐỘNG ĐỎ MỔ CẤP CỨU MỞ NẮP SỌ GIẢI ÁP.",
            standardOrders = orders,
            badDelayNarrative = "Chần chừ không đặt nội khí quản và không hội chẩn mổ cấp cứu, khối máu tụ tiếp tục tăng thể tích làm tụt kẹt hạnh nhân tiểu não vào lỗ chẩm, bệnh nhân ngừng tim tử vong.",
            badDelayVitals = VitalSigns(0, 0, 0, 0, 0, 36.0f, 3, EcgRhythm.ASYSTOLE),
            successfulRescueNarrative = "Bác sĩ cấp cứu hành động chuẩn mực: lập tức đặt ống nội khí quản, xả Mannitol chống tụt kẹt, chụp CT và báo động đỏ phòng mổ. Phẫu thuật viên mở nắp sọ hút sạch 60ml máu tụ đông, cầm máu động mạch màng não giữa. Bệnh nhân hồi tỉnh hoàn toàn sau mổ, GCS đạt 15 điểm!",
            successVitals = VitalSigns(78, 125, 80, 99, 16, 36.8f, 15, EcgRhythm.NORMAL_SINUS),
            clinicalPearls = "Trong Chấn thương sọ não, 'Khoảng tỉnh' (Lucid Interval) và Giãn đồng tử một bên là dấu hiệu báo động đỏ cực kỳ nguy hiểm của TỤ MÁU NGOÀI MÀNG CỨNG CẤP TÍNH. Mỗi phút trì hoãn phẫu thuật đều làm tăng nguy cơ tổn thương thân não vĩnh viễn.",
            gratitudeSpeaker = "Bệnh nhân Nguyễn Văn Hùng & Vợ",
            gratitudeMessage = "Nếu không có các bác sĩ cấp cứu nhận định nhanh và đưa anh ấy vào phòng mổ trong đêm, chồng em chắc đã không thể qua khỏi! Gia đình xin ngàn lần cảm ơn kíp trực!"
        )
    }

    // =========================================================================
    // CASE 20: NHIỄM ĐỘC NỌC RẮN ĐỘC CẮN (RẮN LỤC ĐUÔI ĐỎ - RỐI LOẠN ĐÔNG MÁU)
    // ICD-10: T63.0 (Nhiễm độc nọc rắn) | ICD-10 06/2026/TT-BYT | CCMS
    // =========================================================================
    private fun createCase20SnakeBiteToxicology(baseLabListFactory: (String, Boolean, String, Boolean, String, Boolean, String, Boolean, String, Boolean, String, String, String, String) -> List<LabTestItem>): PatientCase {
        val labs = baseLabListFactory(
            "Nhịp nhanh xoang 108 l/p do đau đớn và lo lắng, không ST chênh.", false,
            "hs-cTnI: 9 ng/L (Bình thường).", false,
            "pH 7.38, PaO2 95 mmHg, PaCO2 38 mmHg. Bình thường.", false,
            "Lactate máu: 1.6 mmol/L.", false,
            "Hồng cầu 3.4 T/L, Hb 98 g/L (giảm do mất máu rỉ rả), Tiểu cầu: 45 G/L (giảm nặng), Đông máu 20WBCT: MÁU HOÀN TOÀN KHÔNG ĐÔNG sau 20 phút! PT kéo dài > 100s, Fibrinogen giảm nặng còn 0.4 g/L.", true,
            "5.4 mmol/L.",
            "X-quang ngực: Bình thường.",
            "X-quang cẳng chân phải: Không gãy xương, mô mềm sưng nề dày đặc.",
            "Tổng phân tích nước tiểu: Hồng cầu (+++), nước tiểu màu đỏ sẫm do tiểu máu vi thể và tán huyết."
        )

        val orders = listOf(
            EmergencyOrder(
                id = "p_immobilize_limb",
                category = OrderCategory.PROCEDURE,
                name = "Bất động chi bị cắn bằng nẹp mềm ở tư thế cơ năng ngang tim",
                description = "Hạn chế vận động cơ bắp làm chậm quá trình nọc độc hấp thu vào hệ tuần hoàn",
                costVnd = 50000L,
                suppliesUsed = listOf("Bộ nẹp mềm", "Băng thun co giãn"),
                isEssential = true,
                feedbackOnExecution = "Đã nẹp cố định cẳng bàn chân phải, dặn bệnh nhân tuyệt đối không cử động chi."
            ),
            EmergencyOrder(
                id = "o_clean_wound",
                category = OrderCategory.PROCEDURE,
                name = "Rửa sạch vết thương bằng Natri Clorid 0.9% vô trùng, băng ép nhẹ",
                description = "Vệ sinh vết thương, chống nhiễm khuẩn thứ phát",
                costVnd = 40000L,
                suppliesUsed = listOf("Chai Natri Clorid 0.9% 500ml", "Gạc vô trùng"),
                isEssential = true,
                feedbackOnExecution = "Đã rửa sạch hai vết móc độc, băng phủ gạc mềm, không rạch chích."
            ),
            EmergencyOrder(
                id = "o_antivenom_green_pit",
                category = OrderCategory.MEDICATION,
                name = "Truyền tĩnh mạch Huyết thanh kháng nọc rắn lục đặc hiệu (10 lọ)",
                description = "Thuốc giải độc đặc hiệu trung hòa nọc rắn, đảo ngược rối loạn đông máu tiêu sợi huyết",
                costVnd = 2800000L,
                suppliesUsed = listOf("10 lọ Huyết thanh kháng nọc rắn lục", "Chai NaCl 0.9% 100ml", "Dây truyền dịch"),
                isEssential = true,
                feedbackOnExecution = "Đã bắt đầu truyền huyết thanh kháng nọc, theo dõi sát phản ứng phản vệ trong 15 phút đầu."
            ),
            EmergencyOrder(
                id = "o_tetanus_toxoid",
                category = OrderCategory.MEDICATION,
                name = "Tiêm vắc xin uốn ván (VAT) và Huyết thanh kháng uốn ván (SAT)",
                description = "Dự phòng uốn ván bắt buộc cho mọi vết thương động vật cắn",
                costVnd = 120000L,
                suppliesUsed = listOf("1 ống VAT", "1 ống SAT 1500UI", "Bơm tiêm 1ml"),
                isEssential = true,
                feedbackOnExecution = "Đã tiêm bắp SAT và VAT ở hai vị trí khác nhau an toàn."
            ),
            EmergencyOrder(
                id = "c_toxicology_icu",
                category = OrderCategory.CONSULTATION,
                name = "Chuyển Trung tâm Chống độc / Khoa Hồi sức Tích cực (ICU)",
                description = "Theo dõi sát chức năng đông máu, tiểu cầu và nguy cơ hội chứng chèn ép khoang",
                costVnd = 300000L,
                suppliesUsed = listOf("Phiếu bàn giao người bệnh ICU"),
                isEssential = true,
                feedbackOnExecution = "Khoa Hồi sức tiếp nhận bệnh nhân, chuẩn bị xét nghiệm đông máu lại sau 2 giờ."
            ),
            EmergencyOrder(
                id = "o_harmful_tourniquet_cut",
                category = OrderCategory.PROCEDURE,
                name = "Buộc garo thật chặt chi hoặc rạch rạch vết cắn nặn máu",
                description = "Sai lầm dân gian nguy hại",
                costVnd = 0L,
                suppliesUsed = listOf("Dây garo"),
                isEssential = false,
                isHarmful = true,
                feedbackOnExecution = "CHỐNG CHỈ ĐỊNH NGUY HIỂM: Garo chặt làm thiếu máu nuôi gây hoại tử toàn bộ cẳng chân và kích hoạt đông máu rải rác!"
            )
        )

        return PatientCase(
            id = "case_snake_bite_20",
            patientName = "Lê Thị Mai",
            age = 48,
            gender = "Nữ",
            occupation = "Làm vườn",
            triageLevel = TriageLevel.YELLOW,
            chiefComplaint = "Bị rắn lục cắn mu chân phải, sưng nề bầm tím lan nhanh, chảy máu chân răng và vết cắn không cầm",
            arrivalTime = "16:45",
            initialVitals = VitalSigns(108, 95, 60, 96, 20, 36.8f, 15, EcgRhythm.NORMAL_SINUS),
            abcde = AbcdeAssessment(
                airwayDesc = "Đường thở thông thoáng, niêm mạc miệng rỉ máu chân răng",
                airwayIsClear = true,
                breathingDesc = "Thở 20 l/p, êm, SpO2 96%, phổi không rale",
                breathingIsNormal = true,
                circulationDesc = "Mạch 108 l/p, HA 95/60 mmHg, CRT 2s, đầu chi ấm",
                circulationIsNormal = false,
                disabilityDesc = "GCS 15 điểm, tỉnh táo, đau buốt dữ dội vùng chân bị cắn",
                disabilityIsNormal = true,
                exposureDesc = "Mu chân phải có 2 vết răng độc cách nhau 1cm, sưng phù bầm tím tím tái lan lên tới khớp gối, chảy máu liên tục",
                exposureIsNormal = false
            ),
            historyQuestions = listOf(
                PatientHistoryQA("q1", "Bác bị con gì cắn và hình dáng thế nào?", "Tôi đang cắt tỉa cành cây thì bị cắn ở mu bàn chân. Con rắn màu xanh lá cây, đầu hình tam giác và cái đuôi màu đỏ tươi!", "Bệnh nhân kể"),
                PatientHistoryQA("q2", "Sau khi cắn bác đã xử trí gì chưa?", "Ở nhà hàng xóm có bảo buộc garo bằng dây cao su nhưng đau quá tôi vừa tháo ra trước khi tới viện!", "Bệnh nhân"),
                PatientHistoryQA("q3", "Bác có thấy chảy máu ở đâu khác không?", "Tôi thấy ngậm miệng nhổ ra nước bọt đầy máu đỏ tươi, vết cắn băng gạc thấm đẫm máu không đông!", "Bệnh nhân lo sợ")
            ),
            physicalExams = listOf(
                PhysicalExamSystemItem("sys_general", "Toàn thân & Da niêm", "Bệnh nhân tỉnh, vẻ mặt đau đớn lo âu. Chảy máu chân răng tự nhiên, xuất hiện các chấm xuất huyết dưới da rải rác ở hai cẳng tay.", true),
                PhysicalExamSystemItem("sys_cardio", "Tuần hoàn", "Nhịp tim nhanh 108 l/p, HA 95/60 mmHg, tiếng tim đều rõ.", false),
                PhysicalExamSystemItem("sys_resp", "Hô hấp", "Thở đều 20 l/p, phổi thông khí tốt hai bên.", false),
                PhysicalExamSystemItem("sys_gi", "Tiêu hóa", "Bụng mềm, không nôn ra máu, không đau bụng.", false),
                PhysicalExamSystemItem("sys_neuro", "Thần kinh & Cơ xương khớp", "Tỉnh táo 15 điểm. KHÁM VẾT THƯƠNG: Mu chân phải có 2 vết răng móc độc, phù nề bầm tím lan lên 1/3 dưới đùi, sờ căng tức, mạch mu chân và chày sau còn bắt được nhưng yếu do phù nề.", true),
                PhysicalExamSystemItem("sys_urinary", "Tiết niệu", "Nước tiểu màu đỏ sẫm (tiểu máu đại thể do rối loạn đông máu).", true)
            ),
            availableLabs = labs,
            goldenDiagnosis = "[T63.0] Nhiễm độc nọc rắn độc cắn cấp tính gây rối loạn đông máu nặng (ICD-10 06/2026/TT-BYT | CCMS)",
            diagnosisKeywords = listOf("t63.0", "rắn cắn", "nhiễm độc", "đông máu", "huyết thanh kháng nọc", "rắn lục"),
            goldenDifferentials = listOf("Rắn hổ mang cắn gây liệt cơ và hoại tử [T63.0]", "Côn trùng đốt nhiễm trùng mô mềm [L03]"),
            goldenClinicalReasoning = "Bệnh nhân bị rắn lục đuôi đỏ cắn có biểu hiện lâm sàng kinh điển: Vết răng móc độc + Sưng nề hoại tử bầm tím lan nhanh + Rối loạn đông máu nặng nề (máu chảy không cầm, xét nghiệm đông máu toàn bộ 20WBCT không đông sau 20 phút, tiểu cầu và Fibrinogen tụt sâu). Nọc rắn lục chứa enzym tiêu sợi huyết và phá hủy nội mô mạch máu. Chỉ định điều trị đặc hiệu duy nhất là HUYẾT THANH KHÁNG NỌC RẮN LỤC ĐẶC HIỆU (Antivenom) tĩnh mạch, kết hợp bất động chi và tiêm phòng uốn ván. Tuyệt đối chống chỉ định garo chặt hay rạch hút nọc.",
            standardOrders = orders,
            badDelayNarrative = "Không dùng huyết thanh kháng nọc mà chuyển đi mổ rạch vết thương, bệnh nhân chảy máu ồ ạt không đông gây sốc mất máu tử vong.",
            badDelayVitals = VitalSigns(140, 60, 30, 89, 28, 36.2f, 9, EcgRhythm.NORMAL_SINUS),
            successfulRescueNarrative = "Bác sĩ xử trí cực kỳ chuẩn xác: bất động chi bằng nẹp, truyền ngay 10 lọ Huyết thanh kháng nọc rắn lục đặc hiệu và tiêm SAT. Sau 4 giờ truyền, xét nghiệm 20WBCT máu đã đông lại bình thường, vết cắn ngừng chảy máu hoàn toàn, bệnh nhân bảo tồn trọn vẹn chi!",
            successVitals = VitalSigns(82, 115, 75, 98, 17, 36.7f, 15, EcgRhythm.NORMAL_SINUS),
            clinicalPearls = "Trong cấp cứu rắn độc cắn: Test đông máu 20 phút tại giường (20WBCT) là xét nghiệm đơn giản và nhạy nhất để phát hiện rối loạn đông máu do rắn lục. HUYẾT THANH KHÁNG NỌC RẮN ĐẶC HIỆU LÀ VŨ KHÍ CỨU CÁNH DUY NHẤT. Cấm tiệt garo chặt và rạch hút vết thương.",
            gratitudeSpeaker = "Bệnh nhân Lê Thị Mai & Chồng",
            gratitudeMessage = "Chân tôi lúc đó sưng đen sì và chảy máu không ngừng tưởng phải cưa chân, may nhờ bác sĩ có huyết thanh giải độc cứu chữa kịp thời! Cảm ơn bác sĩ nhiều lắm!"
        )
    }

    // =========================================================================
    // CASE 21: SỐC PHẢN VỆ ĐỘ IV NGỪNG TUẦN HOÀN DO DỊ ỨNG HẢI SẢN (TÔM CUA BIỂN)
    // ICD-10: T78.0 (Sốc phản vệ do thức ăn) | ICD-10 06/2026/TT-BYT | CCMS
    // =========================================================================
    private fun createCase21FoodAnaphylacticShock(baseLabListFactory: (String, Boolean, String, Boolean, String, Boolean, String, Boolean, String, Boolean, String, String, String, String) -> List<LabTestItem>): PatientCase {
        val labs = baseLabListFactory(
            "Rung thất hỗn loạn chuyển sang Hoạt động điện vô mạch (PEA) tần số 35 l/p!", true,
            "hs-cTnI: 45 ng/L (tăng do thiếu máu cơ tim trong ngừng tim).", false,
            "pH 7.12, PaO2 45 mmHg, PaCO2 62 mmHg, HCO3- 16 mmol/L (Toan hô hấp kết hợp toan chuyển hóa cực nặng).", true,
            "Lactate máu: 7.8 mmol/L (thiếu oxy mô toàn thân).", true,
            "Hồng cầu 4.5 T/L, Hb 145 g/L, Hct 46% (Cô đặc máu do thoát huyết tương ồ ạt qua thành mao mạch).", true,
            "7.2 mmol/L.",
            "X-quang ngực tại giường: Ứ khí hai phổi, phù mô kẽ nhẹ.",
            "X-quang bụng: Bình thường.",
            "Chưa lấy được nước tiểu do vô niệu cấp trong ngừng tuần hoàn."
        )

        val orders = listOf(
            EmergencyOrder(
                id = "p_adrenaline_im_er",
                category = OrderCategory.MEDICATION,
                name = "Tiêm bắp ngay Adrenaline 1mg/1ml liều 1/2 ống (0.5mg) vào mặt trước ngoài đùi",
                description = "Thuốc sống còn đầu tay đảo ngược giãn mạch, co thắt thanh quản và trụy mạch",
                costVnd = 25000L,
                suppliesUsed = listOf("1 ống Adrenaline 1mg/1ml", "Bơm tiêm 1ml"),
                isEssential = true,
                feedbackOnExecution = "Đã tiêm bắp ngay 0.5mg Adrenaline vào đùi phải, chuẩn bị nhắc lại sau 3-5 phút nếu chưa tái lập tuần hoàn."
            ),
            EmergencyOrder(
                id = "p_cpr_acls_er",
                category = OrderCategory.PROCEDURE,
                name = "Khởi động ngay CPR ép tim liên tục & Bóp bóng Ambu gắn oxy 100%",
                description = "Hồi sinh tim phổi cơ bản và nâng cao trong ngừng tuần hoàn do sốc phản vệ",
                costVnd = 200000L,
                suppliesUsed = listOf("Bóng Ambu kèm mask và túi dự trữ oxy"),
                isEssential = true,
                feedbackOnExecution = "Kíp trực thay phiên ép tim chuẩn 100-120 lần/phút, độ sâu 5cm, lồng ngực nảy nở hoàn toàn."
            ),
            EmergencyOrder(
                id = "p_intubate_anaph",
                category = OrderCategory.AIRWAY,
                name = "Đặt nội khí quản khẩn cấp (Sẵn sàng mở màng nhẫn giáp nếu phù thanh môn)",
                description = "Kiểm soát đường thở trước khi phù nề hạ họng đóng kín hoàn toàn",
                costVnd = 350000L,
                suppliesUsed = listOf("Ống nội khí quản số 7.0", "Bộ mở màng nhẫn giáp cấp cứu"),
                isEssential = true,
                feedbackOnExecution = "Thanh môn phù nề nặng nề, bác sĩ đặt luồn thành công ống nội khí quản số 7.0 qua khe thanh môn hẹp!"
            ),
            EmergencyOrder(
                id = "o_iv_adrenaline_drip",
                category = OrderCategory.MEDICATION,
                name = "Lập 2 đường truyền lớn xả Natri Clorid 0.9% 1000ml & Truyền Adrenaline tĩnh mạch liên tục",
                description = "Bù thể tích lòng mạch do thoát dịch và duy trì trương lực mạch máu",
                costVnd = 150000L,
                suppliesUsed = listOf("2 chai NaCl 0.9% 500ml", "Bơm tiêm điện", "Dây truyền dịch"),
                isEssential = true,
                feedbackOnExecution = "Mạch cảnh bắt đầu nảy lại sau 4 phút cấp cứu! Huyết áp đo được 90/50 mmHg!"
            ),
            EmergencyOrder(
                id = "o_steroid_antihistamine",
                category = OrderCategory.MEDICATION,
                name = "Tiêm tĩnh mạch Methylprednisolone 80mg + Diphenhydramine 50mg",
                description = "Điều trị hỗ trợ ngăn ngừa phản vệ pha hai tái phát",
                costVnd = 95000L,
                suppliesUsed = listOf("2 lọ Methylprednisolone 40mg", "1 ống Diphenhydramine"),
                isEssential = true,
                feedbackOnExecution = "Đã tiêm tĩnh mạch corticoid và kháng histamin, các ban mày đay trên da bắt đầu lặn bớt."
            ),
            EmergencyOrder(
                id = "o_harmful_delay_for_records",
                category = OrderCategory.MEDICATION,
                name = "Chờ làm hồ sơ bệnh án và thử test dị ứng trước khi tiêm Adrenaline",
                description = "Trì hoãn tiêm thuốc cứu mạng trong sốc phản vệ",
                costVnd = 0L,
                suppliesUsed = listOf("Giấy tờ bệnh án"),
                isEssential = false,
                isHarmful = true,
                feedbackOnExecution = "SAI LẦM CHẾT NGƯỜI: Chậm trễ tiêm Adrenaline vài phút trong phản vệ độ IV sẽ khiến não hoại tử không thể hồi phục!"
            )
        )

        return PatientCase(
            id = "case_anaph_food_21",
            patientName = "Đặng Minh Tuấn",
            age = 21,
            gender = "Nam",
            occupation = "Sinh viên đại học",
            triageLevel = TriageLevel.RED,
            chiefComplaint = "Ăn lẩu tôm cua biển, đột ngột nghẹn thở tím tái, nổi ban phù mặt rồi ngất xỉu ngừng tim",
            arrivalTime = "19:20",
            initialVitals = VitalSigns(0, 0, 0, 50, 0, 36.2f, 3, EcgRhythm.ASYSTOLE),
            abcde = AbcdeAssessment(
                airwayDesc = "Phù Quincke toàn bộ môi lưỡi, lưỡi gà sưng to chèn kín hầu họng, không khí không vào phổi",
                airwayIsClear = false,
                breathingDesc = "Ngừng thở hoàn toàn, tím tái toàn thân",
                breathingIsNormal = false,
                circulationDesc = "Mất mạch cảnh và mạch bẹn hoàn toàn, HA 0/0 mmHg, ngừng tim",
                circulationIsNormal = false,
                disabilityDesc = "GCS 3 điểm, hôn mê sâu, đồng tử hai bên giãn 4.5mm",
                disabilityIsNormal = false,
                exposureDesc = "Mày đay toàn thân dạng mảng đỏ gồ lên khắp ngực bụng và mặt, phù nề mi mắt hai bên",
                exposureIsNormal = false
            ),
            historyQuestions = listOf(
                PatientHistoryQA("q1", "Trước khi bị bệnh nhân ăn uống gì?", "Chúng em đang ăn lẩu cua biển được 15 phút thì bạn ấy kêu ngứa cổ họng dữ dội, tức ngực không thở được rồi gục xuống bàn!", "Bạn bè đi cùng kể"),
                PatientHistoryQA("q2", "Bạn ấy có tiền sử dị ứng bao giờ chưa?", "Có lần ăn tôm bị nổi mề đay nhẹ nhưng lần này ăn cua biển thì bị bùng phát dữ dội ngất lịm đi luôn!", "Bạn thân"),
                PatientHistoryQA("q3", "Từ lúc ngất tới khi vào viện mất bao lâu?", "Khoảng 5-7 phút đi xe máy gấp vào thẳng cấp cứu!", "Người đưa vào")
            ),
            physicalExams = listOf(
                PhysicalExamSystemItem("sys_general", "Toàn thân", "Bệnh nhân ngừng thở, tím tái toàn thân, da nổi ban mày đay phù nề dạng mảng lớn toàn thân.", true),
                PhysicalExamSystemItem("sys_cardio", "Tuần hoàn", "MẤT MẠCH TRUNG TÂM HOÀN TOÀN, không bắt được mạch cảnh, huyết áp 0/0 mmHg, monitor hiện vô tâm thu / PEA sóng thưa.", true),
                PhysicalExamSystemItem("sys_resp", "Hô hấp", "Ngừng thở, lồng ngực không di động, phù thanh môn cấp gây tắc nghẽn đường hô hấp trên hoàn toàn.", true),
                PhysicalExamSystemItem("sys_gi", "Tiêu hóa", "Bụng trướng nhẹ do nuốt khí khi cố thở.", false),
                PhysicalExamSystemItem("sys_neuro", "Thần kinh & Đầu mặt", "GCS 3 điểm. Môi, lưỡi, mi mắt sưng phù nề biến dạng (Phù mạch Angioedema / Quincke), đồng tử hai bên giãn 4.5mm phản xạ ánh sáng mất.", true),
                PhysicalExamSystemItem("sys_urinary", "Tiết niệu", "Không có nước tiểu do tụt huyết áp ngừng tuần hoàn.", false)
            ),
            availableLabs = labs,
            goldenDiagnosis = "[T78.0] Sốc phản vệ độ IV (Ngừng tuần hoàn hô hấp) do dị ứng thức ăn hải sản (ICD-10 06/2026/TT-BYT | CCMS)",
            diagnosisKeywords = listOf("t78.0", "sốc phản vệ", "ngừng tuần hoàn", "hải sản", "adrenaline", "dị ứng thức ăn", "t78"),
            goldenDifferentials = listOf("Dị vật đường thở cấp tính [T17]", "Cơn hen phế quản ác tính ngừng thở [J45.0]", "Ngộ độc thực phẩm cấp [A05]"),
            goldenClinicalReasoning = "Bệnh nhân có tiền sử cơ địa dị ứng, tiếp xúc dị nguyên thức ăn (hải sản tôm cua) xuất hiện bệnh cảnh sốc phản vệ nguy kịch độ IV tối cấp (ngừng tuần hoàn - hô hấp trong vòng vài phút do phù nề thanh môn gây tắc thở và giãn mạch toàn thân thoát dịch sập tuần hoàn). Đây là tình huống TỐI KHẨN: Chỉ định tiêm bắp Adrenaline 0.5mg ngay lập tức + Hồi sinh tim phổi CPR liên tục + Đặt ống nội khí quản bảo vệ đường thở + Xả dịch tinh thể bù thể tích lòng mạch. Mọi sự chậm trễ tiêm Adrenaline đều dẫn tới tử vong không thể cứu vãn.",
            standardOrders = orders,
            badDelayNarrative = "Không tiêm Adrenaline ngay mà đi tìm máy hút đờm và đo huyết áp, bệnh nhân thiếu oxy não quá 5 phút, tổn thương não chết não không hồi phục.",
            badDelayVitals = VitalSigns(0, 0, 0, 0, 0, 35.5f, 3, EcgRhythm.ASYSTOLE),
            successfulRescueNarrative = "Kíp trực phản xạ xuất sắc: tiêm ngay Adrenaline vào bắp đùi, ép tim nhịp nhàng, đặt ống nội khí quản qua thanh môn phù nề và xả nhanh dịch tinh thể. Sau 4 phút cấp cứu, tim đập lại, mạch cảnh nảy mạnh, huyết áp 110/70 mmHg, bệnh nhân được cứu sống ngoạn mục!",
            successVitals = VitalSigns(98, 115, 70, 97, 18, 36.6f, 14, EcgRhythm.NORMAL_SINUS),
            clinicalPearls = "Trong Sốc phản vệ độ III và IV: ADRENALINE TIÊM BẮP LÀ LIỀU THUỐC CỨU SINH SỐ 1, BẮT BUỘC DÙNG NGAY KHÔNG CẦN CHỜ ĐỢI. Vị trí tiêm bắp tối ưu nhất là MẶT TRƯỚC NGOÀI ĐÙI vì có mạng lưới mạch máu phong phú giúp thuốc hấp thu nhanh nhất.",
            gratitudeSpeaker = "Bệnh nhân Đặng Minh Tuấn & Bố mẹ",
            gratitudeMessage = "Con tôi ngừng tim tím tái ngắt hơi, các bác sĩ đã giành lại mạng sống cho cháu từ tay tử thần! Ơn cứu mạng này cả đời gia đình tôi không bao giờ quên!"
        )
    }

    // =========================================================================
    // CASE 22: ĐA CHẤN THƯƠNG NGỰC DO TAI NẠN GIAO THÔNG (TRÀN MÁU MÀNG PHỔI & DẬP PHỔI)
    // ICD-10: S27.1 (Tràn máu màng phổi chấn thương) | S27.3 (Dập phổi) | ICD-10 06/2026/TT-BYT | CCMS
    // =========================================================================
    private fun createCase22TraumaticHemothorax(baseLabListFactory: (String, Boolean, String, Boolean, String, Boolean, String, Boolean, String, Boolean, String, String, String, String) -> List<LabTestItem>): PatientCase {
        val labs = baseLabListFactory(
            "Nhịp nhanh xoang 128 l/p, trục lệch phải, dấu hiệu quá tải thất phải cấp do chèn ép phổi lồng ngực.", false,
            "hs-cTnI: 35 ng/L (tăng nhẹ do dập cơ tim / đụng dập lồng ngực).", false,
            "pH 7.26, PaO2 58 mmHg, PaCO2 48 mmHg, SaO2 86% (Suy hô hấp giảm oxy máu và tăng CO2 cấp do xẹp phổi và mất máu).", true,
            "Lactate máu: 4.5 mmol/L (tăng cao do sốc mất máu kết hợp thiếu oxy mô).", true,
            "Hồng cầu 2.5 T/L, Hemoglobin (Hb): 75 g/L (giảm nặng do tràn máu khoang màng phổi), Hct 23%, Bạch cầu 14.8 G/L.", true,
            "5.8 mmol/L.",
            "X-quang ngực thẳng tại giường: Mờ đồng nhất 2/3 dưới phế trường trái, mất góc sườn hoành, trung thất và khí quản bị đẩy lệch sang phải, gãy xương sườn số 4, 5, 6 bên trái.",
            "X-quang bụng: Không thấy liềm hơi dưới hoành.",
            "Nước tiểu vàng trong, không tiểu máu."
        )

        val orders = listOf(
            EmergencyOrder(
                id = "p_o2_high_flow",
                category = OrderCategory.AIRWAY,
                name = "Thở oxy qua mask có túi dự trữ lưu lượng cao 12-15 lít/phút",
                description = "Cung cấp nồng độ oxy FiO2 cao cải thiện tình trạng thiếu máu não và mô",
                costVnd = 70000L,
                suppliesUsed = listOf("Mask thở oxy có túi dự trữ", "Dây oxy"),
                isEssential = true,
                feedbackOnExecution = "Đã gắn mask túi 15L/p, SpO2 cải thiện từ 86% lên 93%."
            ),
            EmergencyOrder(
                id = "p_chest_tube_drainage",
                category = OrderCategory.PROCEDURE,
                name = "Mở màng phổi tối thiểu đặt ống dẫn lưu ngực kín (Chest Tube 32F) khoang liên sườn 5 đường nách giữa",
                description = "Chỉ định sống còn giải áp khoang màng phổi, dẫn lưu máu và nở lại nhu mô phổi",
                costVnd = 850000L,
                suppliesUsed = listOf("Bộ mở màng phổi vô trùng", "Ống dẫn lưu ngực Silicon 32F", "Bình dẫn lưu một chiều hút áp lực âm"),
                isEssential = true,
                feedbackOnExecution = "Máu đen sẫm ào ra bình dẫn lưu ngay 1.100ml! Phổi trái nở ra, bệnh nhân thở êm hơn rõ rệt, bớt vật vã tím tái."
            ),
            EmergencyOrder(
                id = "o_iv_access_and_blood",
                category = OrderCategory.MEDICATION,
                name = "Lập 2 ven lớn 18G, truyền dịch tinh thể ấm & Báo động truyền 2 đơn vị Khối hồng cầu O-",
                description = "Bù thể tích tuần hoàn và lượng máu đã mất vào khoang màng phổi",
                costVnd = 1250000L,
                suppliesUsed = listOf("2 kim luồn 18G", "2 túi khối hồng cầu 350ml", "Dây truyền máu"),
                isEssential = true,
                feedbackOnExecution = "Đang xả 2 đường ven lớn truyền máu và dịch tinh thể ấm, huyết áp nâng lên 105/65 mmHg."
            ),
            EmergencyOrder(
                id = "o_analgesia_multimodal",
                category = OrderCategory.MEDICATION,
                name = "Giảm đau toàn thân tích cực: Fentanyl tĩnh mạch / Paracetamol truyền tĩnh mạch",
                description = "Giảm đau giúp bệnh nhân dám thở sâu, hạn chế xẹp phổi và suy hô hấp",
                costVnd = 80000L,
                suppliesUsed = listOf("1 ống Fentanyl 0.1mg", "1 chai Paracetamol truyền 1g"),
                isEssential = true,
                feedbackOnExecution = "Đã tiêm giảm đau, bệnh nhân đỡ đau ngực, hợp tác hít thở tốt."
            ),
            EmergencyOrder(
                id = "c_thoracic_surgery_consult",
                category = OrderCategory.CONSULTATION,
                name = "Hội chẩn khẩn Bác sĩ Ngoại Lồng ngực (Chuẩn bị mổ mở ngực nếu máu dẫn lưu > 200ml/giờ)",
                description = "Theo dõi tiêu chuẩn chỉ định phẫu thuật mở ngực cầm máu cấp cứu",
                costVnd = 400000L,
                suppliesUsed = listOf("Hồ sơ bệnh án chuyển Ngoại Lồng ngực"),
                isEssential = true,
                feedbackOnExecution = "Bác sĩ Ngoại Lồng ngực túc trực theo dõi lượng máu dẫn lưu mỗi 30 phút tại giường bệnh."
            ),
            EmergencyOrder(
                id = "o_harmful_tight_chest_wrap",
                category = OrderCategory.PROCEDURE,
                name = "Băng ép chặt vòng quanh lồng ngực bằng băng thun",
                description = "Sai lầm nghiêm trọng hạn chế biên độ hô hấp",
                costVnd = 30000L,
                suppliesUsed = listOf("Băng thun"),
                isEssential = false,
                isHarmful = true,
                feedbackOnExecution = "CHỐNG CHỈ ĐỊNH: Băng ép ngực làm hạn chế di động lồng ngực, gây xẹp phổi toàn bộ và suy hô hấp tử vong!"
            )
        )

        return PatientCase(
            id = "case_trauma_hemothorax_22",
            patientName = "Bùi Quốc Bảo",
            age = 29,
            gender = "Nam",
            occupation = "Công nhân cơ khí",
            triageLevel = TriageLevel.RED,
            chiefComplaint = "Tai nạn giao thông ngã xe máy đập ngực trái vào dải phân cách, khó thở tím tái, đau ngực dữ dội, sốc mất máu",
            arrivalTime = "22:15",
            initialVitals = VitalSigns(128, 80, 50, 86, 32, 36.3f, 13, EcgRhythm.NORMAL_SINUS),
            abcde = AbcdeAssessment(
                airwayDesc = "Đường thở thông thoáng, không dị vật, có đờm lẫn ít bọt máu",
                airwayIsClear = true,
                breathingDesc = "Thở nhanh nông 32 l/p, SpO2 86%, ngực trái di động kém, co kéo cơ liên sườn",
                breathingIsNormal = false,
                circulationDesc = "Mạch 128 l/p nhanh nhỏ khó bắt, HA 80/50 mmHg, tĩnh mạch cổ xẹp, da lạnh ẩm",
                circulationIsNormal = false,
                disabilityDesc = "GCS 13 điểm, tiếp xúc chậm, bứt rứt vật vã vì ngạt thở",
                disabilityIsNormal = false,
                exposureDesc = "Mảng bầm tím xây xát lớn vùng ngực trái, ấn đau chói sườn 4-6, gõ đục toàn bộ phổi trái",
                exposureIsNormal = false
            ),
            historyQuestions = listOf(
                PatientHistoryQA("q1", "Cơ chế tai nạn thế nào?", "Anh ấy lái xe máy bị trượt ngã tốc độ khoảng 50km/h, đập thẳng ngực trái vào thanh chắn hộ lan bằng sắt bên đường!", "Người đi đường đưa vào"),
                PatientHistoryQA("q2", "Bệnh nhân có ngất đi sau tai nạn không?", "Không ngất, nhưng kêu đau ngực nghẹn thở dữ dội, không hít vào được, nói ngắt quãng từng từ!", "Người đưa vào"),
                PatientHistoryQA("q3", "Tiền sử bệnh lý gì không?", "Hoàn toàn khỏe mạnh bình thường.", "Người nhà vừa tới")
            ),
            physicalExams = listOf(
                PhysicalExamSystemItem("sys_general", "Toàn thân & Da niêm", "Bệnh nhân vật vã, da xanh tái nhợt, vã mồ hôi trán, đầu chi lạnh, niêm mạc mắt nhạt màu.", true),
                PhysicalExamSystemItem("sys_cardio", "Tuần hoàn", "Nhịp tim nhanh 128 l/p, huyết áp tụt sâu 80/50 mmHg, tiếng tim mờ, tĩnh mạch cổ xẹp (loại trừ chèn ép tim cấp).", true),
                PhysicalExamSystemItem("sys_resp", "Hô hấp & Lồng ngực", "Thở nhanh 32 l/p, co kéo cơ hô hấp phụ. Khám ngực trái: Ấn đau chói có lạo xạo xương sườn 4-6. GÕ ĐỤC TOÀN BỘ PHẾ TRƯỜNG TRÁI, RÌ RÀO PHẾ NANG MẤT HOÀN TOÀN.", true),
                PhysicalExamSystemItem("sys_gi", "Tiêu hóa & Ổ bụng", "Bụng mềm, không trướng, không đau, siêu âm bụng không thấy dịch tự do.", false),
                PhysicalExamSystemItem("sys_neuro", "Thần kinh & Cơ xương khớp", "GCS 13 điểm, đồng tử 2 bên đều 2.5mm. Các chi không gãy xương lớn.", false),
                PhysicalExamSystemItem("sys_urinary", "Tiết niệu", "Đặt sonde tiểu ra nước tiểu vàng trong, lượng ít.", false)
            ),
            availableLabs = labs,
            goldenDiagnosis = "[S27.1] Tràn máu màng phổi chấn thương do tai nạn giao thông - Dập phổi [S27.3] (ICD-10 06/2026/TT-BYT | CCMS)",
            diagnosisKeywords = listOf("s27.1", "tràn máu", "màng phổi", "dập phổi", "s27.3", "dẫn lưu", "tai nạn giao thông"),
            goldenDifferentials = listOf("Tràn khí màng phổi áp lực [J93.0]", "Vỡ cơ hoành chấn thương [S27.8]", "Chèn ép tim cấp Tamponade [I31.9]"),
            goldenClinicalReasoning = "Bệnh nhân chấn thương ngực kín do tai nạn giao thông có hội chứng tràn dịch khoang màng phổi cấp tính (Gõ đục + Mất rì rào phế nang phổi trái) kết hợp hội chứng mất máu cấp đe dọa tính mạng (HA 80/50, Hb 75 g/L, X-quang mờ đồng nhất phổi trái). Đây là TRÀN MÁU MÀNG PHỔI LƯỢNG LỚN DO CHẤN THƯƠNG. Chỉ định tối khẩn: Thở oxy lưu lượng cao + DẪN LƯU MÀNG PHỔI KÍN (Chest Tube 32F) liên tục ngay lập tức để giải phóng phổi xẹp và theo dõi lượng máu mất + Hồi sức truyền máu và dịch tinh thể ấm + Sẵn sàng phẫu thuật mở ngực nếu máu chảy tiếp diễn.",
            standardOrders = orders,
            badDelayNarrative = "Không dẫn lưu màng phổi mà cho bệnh nhân nằm chờ chụp CT, lượng máu tràn ép sập phổi và cản trở hồi lưu tĩnh mạch về tim gây ngưng tim trên giường bệnh.",
            badDelayVitals = VitalSigns(0, 0, 0, 0, 0, 35.8f, 3, EcgRhythm.ASYSTOLE),
            successfulRescueNarrative = "Bác sĩ thực hiện thủ thuật mở màng phổi đặt dẫn lưu ngực cực nhanh và chính xác: 1.100ml máu đen được tháo ra, phổi nở lại, kết hợp truyền 2 đơn vị máu O-. Huyết áp phục hồi 115/70 mmHg, SpO2 lên 98%, cứu sống người bệnh trong giờ vàng!",
            successVitals = VitalSigns(86, 115, 72, 98, 18, 36.6f, 15, EcgRhythm.NORMAL_SINUS),
            clinicalPearls = "Trong Đa chấn thương ngực do tai nạn giao thông: Tràn máu màng phổi lượng nhiều gây nguy hiểm kép vừa mất máu cấp vừa xẹp phổi suy hô hấp. ĐẶT ỐNG DẪN LƯU MÀNG PHỔI KÍN (CHEST TUBE) LÀ THỦ THUẬT CỨU MẠNG HÀNG ĐẦU. Cần theo dõi sát: nếu máu dẫn lưu ra ngay > 1.500ml hoặc > 200ml/giờ trong 3-4 giờ liên tục là CHỈ ĐỊNH PHẪU THUẬT MỞ NGỰC CẤP CỨU.",
            gratitudeSpeaker = "Bệnh nhân Bùi Quốc Bảo & Mẹ",
            gratitudeMessage = "Tôi như vừa được sống lại sau cơn ngạt thở kinh hoàng, nhờ các bác sĩ đặt ống hút máu trong ngực ra kịp thời! Gia đình tôi muôn vàn cảm tạ các y bác sĩ!"
        )
    }
}
