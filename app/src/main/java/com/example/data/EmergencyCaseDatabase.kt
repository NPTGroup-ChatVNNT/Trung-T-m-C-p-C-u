package com.example.data

import com.example.model.*

object EmergencyCaseDatabase {

    fun getAllCases(): List<PatientCase> {
        val criticalCases = listOf(
            createCase1Stemi(),
            createCase2Pneumothorax(),
            createCase3SevereAsthma(),
            createCase4Anaphylaxis(),
            createCase5AcuteStroke(),
            createCase6Peritonitis(),
            createCase7Organophosphate()
        )
        val internalCases = InternalMedicineCases.getInternalCases { ecg, ecgCrit, trop, tropCrit, abg, abgCrit, lac, lacCrit, cbc, cbcCrit, bs, xrChest, xrAbd, uri ->
            createBaseLabList(
                ecgResult = ecg, ecgCritical = ecgCrit,
                tropResult = trop, tropCritical = tropCrit,
                abgResult = abg, abgCritical = abgCrit,
                lactateResult = lac, lactateCritical = lacCrit,
                cbcResult = cbc, cbcCritical = cbcCrit,
                bloodSugarResult = bs,
                xrayChestResult = xrChest,
                xrayAbdResult = xrAbd,
                urinalysisResult = uri
            )
        }
        return criticalCases + internalCases
    }

    // ==========================================
    // CASE 1: NHỒI MÁU CƠ TIM CẤP STEMI THÀNH TRƯỚC RỘNG
    // ==========================================
    private fun createCase1Stemi(): PatientCase {
        val labs = createBaseLabList(
            ecgResult = "ST chênh lên vòm dạng bia mộ 4-6mm ở V1-V6, DI, aVL kèm ST chênh xuống soi gương ở DII, DIII, aVF. Nguy cơ biến chứng Rung thất (VF) rất cao!",
            ecgCritical = true, ecgRecommended = true,
            tropResult = "hs-cTnI: 3,420 ng/L (Bình thường < 14 ng/L) - Tăng vọt gấp hàng trăm lần!",
            tropCritical = true, tropRecommended = true,
            abgResult = "pH: 7.34, PaO2: 82 mmHg, PaCO2: 36 mmHg, HCO3-: 21 mmol/L. Giảm oxy máu nhẹ.",
            lactateResult = "Lactate: 2.8 mmol/L (Tham chiếu < 2.0 mmol/L) - Tưới máu mô suy giảm.",
            xrayChestResult = "Bóng tim hơi to, sung huyết rốn phổi nhẹ, không thấy tràn khí hay tràn dịch màng phổi.",
            focusEchoResult = "Vô động toàn bộ vùng mỏm tim, thành trước và vách liên thất. Phân suất tống máu LVEF giảm nặng còn 35%. Không tràn dịch màng ngoài tim."
        )

        val orders = listOf(
            EmergencyOrder(
                id = "o_o2",
                category = OrderCategory.AIRWAY,
                name = "Thở oxy qua cannula 3 lít/phút",
                description = "Duy trì SpO2 từ 94% - 98%",
                costVnd = 60000L,
                suppliesUsed = listOf("Cannula thở oxy 2 nhánh", "Dây oxy kết nối bình dưỡng khí"),
                isEssential = true,
                feedbackOnExecution = "SpO2 cải thiện lên 97%, bệnh nhân bớt cảm giác ngột ngạt."
            ),
            EmergencyOrder(
                id = "o_dapt",
                category = OrderCategory.MEDICATION,
                name = "Kháng kết tập tiểu cầu kép (Aspirin 300mg + Ticagrelor 180mg uống nhai)",
                description = "Y lệnh chuẩn Bộ Y Tế trong hội chứng vành cấp",
                costVnd = 45000L,
                suppliesUsed = listOf("Cốc đựng thuốc viên", "Nước đun sôi để nguội"),
                isEssential = true,
                feedbackOnExecution = "Đã cho bệnh nhân nhai nuốt ngay Aspirin và Ticagrelor."
            ),
            EmergencyOrder(
                id = "o_heparin",
                category = OrderCategory.MEDICATION,
                name = "Enoxaparin (Heparin TLPT thấp) 30mg tiêm TM bolus + 1mg/kg TDD",
                description = "Chống đông nền tảng khẩn cấp",
                costVnd = 185000L,
                suppliesUsed = listOf("Bơm tiêm nạp sẵn thuốc 1ml", "Bông cồn vô trùng", "Gạc băng tiêm"),
                isEssential = true,
                feedbackOnExecution = "Đã tiêm tĩnh mạch bolus Enoxaparin."
            ),
            EmergencyOrder(
                id = "o_morphine",
                category = OrderCategory.MEDICATION,
                name = "Morphine Sulfat 2-4mg tiêm tĩnh mạch chậm",
                description = "Giảm đau ngực dữ dội, giảm tiền gánh và kích thích giao cảm",
                costVnd = 35000L,
                suppliesUsed = listOf("Bơm tiêm 5ml", "Nước cất pha tiêm 5ml", "Kim tiêm vô trùng"),
                isEssential = true,
                feedbackOnExecution = "Cơn đau thắt ngực dịu dần, bệnh nhân bớt hoảng loạn."
            ),
            EmergencyOrder(
                id = "o_nitro",
                category = OrderCategory.MEDICATION,
                name = "Nitroglycerin ngậm dưới lưỡi",
                description = "Dãn mạch vành giảm đau",
                costVnd = 30000L,
                suppliesUsed = listOf("Viên ngậm Nitroglycerin"),
                isEssential = false,
                feedbackOnExecution = "Bớt đau ngực nhưng cần theo dõi sát huyết áp."
            ),
            EmergencyOrder(
                id = "o_wrong_antiarr",
                category = OrderCategory.MEDICATION,
                name = "Diltiazem / Verapamil tiêm TM liều cao",
                description = "Ức chế nút nhĩ thất (Chống chỉ định trong suy tim cấp/STEMI trước rộng)",
                costVnd = 65000L,
                suppliesUsed = listOf("Bơm tiêm 10ml"),
                isEssential = false,
                isHarmful = true,
                feedbackOnExecution = "NGUY HIỂM! Huyết áp tụt sâu còn 60/40 mmHg, cơ tim co bóp yếu kiệt dẫn tới sốc tim!"
            ),
            EmergencyOrder(
                id = "o_dsa_cath",
                category = OrderCategory.CONSULTATION,
                name = "Báo động đỏ Tim mạch - Chuyển phòng Can thiệp mạch vành khẩn cấp (Cath-Lab / DSA)",
                description = "Tái tưới máu tiên phát (PPCI) trong vòng 90 phút - Thời gian là cơ tim!",
                costVnd = 45000000L,
                suppliesUsed = listOf("Stent phủ thuốc động mạch vành", "Bộ dây dẫn can thiệp Guiding Catheter", "Dây dẫn Guide-wire", "Thuốc cản quang"),
                isEssential = true,
                feedbackOnExecution = "Kíp DSA đã sẵn sàng, bệnh nhân được chuyển thẳng phòng can thiệp tái thông động mạch vành LAD tắc hoàn toàn!"
            )
        )

        return PatientCase(
            id = "case_stemi_01",
            patientName = "Nguyễn Văn Hưng",
            age = 58,
            gender = "Nam",
            occupation = "Tài xế xe tải đường dài",
            triageLevel = TriageLevel.RED,
            chiefComplaint = "Đau thắt ngực trái bóp nghẹt lan lên cằm và tay trái, vã mồ hôi hột, khó thở",
            arrivalTime = "14:15",
            initialVitals = VitalSigns(
                heartRate = 112,
                bpSys = 95,
                bpDia = 60,
                spo2 = 91,
                respRate = 26,
                temperature = 36.8f,
                gcs = 15,
                rhythm = EcgRhythm.STEMI
            ),
            abcde = AbcdeAssessment(
                airwayDesc = "Đường thở thông thoáng, không ứ đọng dị vật.",
                airwayIsClear = true,
                breathingDesc = "Thở nhanh nông 26 lần/phút, phổi đáy nghe rale ẩm rải rác.",
                breathingIsNormal = false,
                circulationDesc = "Mạch nhanh nhỏ 112 lần/phút, da đầu chi tái lạnh vã mồ hôi, CRT 3.5 giây, HA 95/60 mmHg.",
                circulationIsNormal = false,
                disabilityDesc = "GCS 15/15 (Mắt 4, Lời nói 5, Vận động 6), lo âu sợ chết, đồng tử 2.5mm đều 2 bên phản xạ ánh sáng tốt.",
                disabilityIsNormal = true,
                exposureDesc = "Toàn thân vã mồ hôi đầm đìa lạnh ngắt, không có vết thương ngoại khoa.",
                exposureIsNormal = false
            ),
            historyQuestions = listOf(
                PatientHistoryQA("q1", "Bác đau ngực từ lúc nào và cảm giác đau như thế nào?", "Tôi đau khoảng 2 tiếng trước, cảm giác như có tảng đá hàng trăm cân đè nát lồng ngực, lan nhói lên hàm dưới và chạy dọc cánh tay trái, khó thở lắm bác sĩ ơi!", "Bệnh nhân"),
                PatientHistoryQA("q2", "Trước đây bác từng bị cơn đau như thế này chưa?", "Thỉnh thoảng đi bộ nhanh tôi có hơi tức ngực nhưng nghỉ ngơi là hết. Lần này đau dữ dội liên tục không đỡ chút nào.", "Bệnh nhân"),
                PatientHistoryQA("q3", "Bác có tiền sử bệnh lý mạn tính gì không?", "Tôi hút thuốc lá 30 năm nay (mỗi ngày 1 bao), bị tăng huyết áp và rối loạn mỡ máu nhưng hay quên uống thuốc.", "Vợ bệnh nhân"),
                PatientHistoryQA("q4", "Bác có dị ứng với thuốc hay thức ăn gì không?", "Không dị ứng gì cả bác sĩ ạ.", "Vợ bệnh nhân"),
                PatientHistoryQA("q5", "Bữa ăn gần nhất của bác là lúc nào?", "Bác ăn tô phở bò lúc 11 giờ trưa nay.", "Vợ bệnh nhân")
            ),
            physicalExams = listOf(
                PhysicalExamSystemItem("sys_general", "Toàn thân", "Thể trạng thừa cân (BMI 27.5), vẻ mặt đau đớn dữ dội, vã mồ hôi lạnh toàn thân, đầu chi tái nhợt.", true),
                PhysicalExamSystemItem("sys_cardio", "Tim mạch", "Tiếng tim T1 T2 mờ, nhịp tim nhanh 112 lần/phút, nghe có tiếng ngựa phi T3 ở mỏm tim, không âm thổi bệnh lý, tĩnh mạch cổ nổi nhẹ.", true),
                PhysicalExamSystemItem("sys_resp", "Hô hấp", "Thở nhanh 26 l/p, rì rào phế nang giảm nhẹ 2 đáy, nghe ít rale ẩm nhỏ hạt ở đáy phổi 2 bên (Killip II).", true),
                PhysicalExamSystemItem("sys_gi", "Tiêu hóa", "Bụng mềm, không chướng, gan lách không sờ chạm, không có phản ứng thành bụng.", false),
                PhysicalExamSystemItem("sys_neuro", "Thần kinh", "Tỉnh táo, tiếp xúc tốt, định hướng không gian thời gian chuẩn, không dấu thần kinh khu trú, không liệt.", false),
                PhysicalExamSystemItem("sys_skin", "Da & Chấn thương", "Da lạnh ẩm, vã mồ hôi nhiều, không phát ban mề đay, không có dấu hiệu chấn thương ngực.", true)
            ),
            availableLabs = labs,
            goldenDiagnosis = "Nhồi máu cơ tim cấp ST chênh lên thành trước rộng giờ thứ 2 - Killip II - Biến chứng Rung thất (STEMI Anterior)",
            diagnosisKeywords = listOf("nhoi mau co tim", "stemi", "st chenh len", "thanh truoc", "rung that", "killip"),
            goldenDifferentials = listOf("Phình bóc tách động mạch chủ ngực cấp", "Thuyên tắc động mạch phổi cấp", "Viêm màng ngoài tim cấp", "Tràn khí màng phổi tự phát"),
            goldenClinicalReasoning = "Bệnh nhân nam 58 tuổi, yếu tố nguy cơ tim mạch cao. Nhập viện vì cơn đau thắt ngực điển hình kiểu động mạch vành kéo dài > 30 phút, vã mồ hôi, rale ẩm đáy phổi. ECG 12 chuyển đạo có ST chênh lên vòm dạng bia mộ ở V1-V6 kèm Troponin I tăng cao xác nhận STEMI thành trước rộng.",
            standardOrders = MasterHospitalOrders.getAllMasterOrders(),
            badDelayNarrative = "Do chậm trễ không sốc điện khử rung kịp thời khi monitor chuyển sang Rung thất (VF), tưới máu não ngưng trệ hoàn toàn! Bệnh nhân mất tri giác, tim ngừng đập chuyển thành vô tâm thu (Asystole).",
            badDelayVitals = VitalSigns(0, 0, 0, 0, 0, 36.0f, 3, EcgRhythm.ASYSTOLE),
            successfulRescueNarrative = "Bác sĩ đã nhanh chóng phát hiện Rung thất, sạc máy sốc điện 200J và phóng điện chính xác kèm ép tim CPR chất lượng cao! Nhịp xoang đã phục hồi (ROSC), huyết áp cải thiện 115/75 mmHg. Kíp can thiệp mạch vành đã đưa bệnh nhân lên phòng DSA đặt stent LAD thành công ngoạn mục!",
            successVitals = VitalSigns(86, 118, 76, 98, 18, 36.8f, 15, EcgRhythm.NORMAL_SINUS),
            clinicalPearls = "Quy tắc vàng Bộ Y Tế: Bệnh nhân đau ngực nghi ngờ hội chứng vành cấp phải đo ECG 12 chuyển đạo trong vòng 10 phút đầu tiên! Nhồi máu cơ tim thành trước rất dễ biến chứng rung thất (VF) trong những giờ đầu. Khi có VF, sốc điện khử rung sớm là yếu tố sống còn số 1!",
            gratitudeSpeaker = "Bác Hưng & Vợ",
            gratitudeMessage = "Bác sĩ ơi, tôi như vừa bước từ cõi chết trở về! Cảm ơn bác sĩ và kíp trực đã sốc điện cứu tim tôi đập lại và nong thông mạch vành kịp thời!"
        )
    }

    // ==========================================
    // CASE 2: TRÀN KHÍ MÀNG PHỔI ÁP LỰC
    // ==========================================
    private fun createCase2Pneumothorax(): PatientCase {
        val labs = createBaseLabList(
            ecgResult = "Nhịp xoang nhanh 135 ck/p, trục lệch phải, điện thế ngoại vi thấp do khí màng phổi chèn ép.",
            ecgCritical = false, ecgRecommended = true,
            abgResult = "pH: 7.22, PaO2: 52 mmHg (Giảm oxy nặng), PaCO2: 55 mmHg (Ứ khí toan hô hấp cấp), HCO3-: 22 mmol/L.",
            abgCritical = true, abgRecommended = true,
            xrayChestResult = "Tràn khí toàn bộ phế trường phải lượng lớn, mất hoàn toàn vân phổi bên phải, nhu mô phổi xẹp dúm về rốn phổi. Trung thất và khí quản bị đẩy lệch mạnh sang trái. Vòm hoành phải bị đẩy hạ thấp!",
            xrayChestCritical = true, xrayChestRecommended = false,
            efastResult = "Siêu âm phổi phải: Mất dấu trượt màng phổi (Lung Sliding), mất đường B-line, xuất hiện điểm phổi (Lung Point) và dấu hiệu mã vạch (Stratosphere sign / Barcode sign) xác định tràn khí màng phổi.",
            efastCritical = true, efastRecommended = true
        )

        val orders = listOf(
            EmergencyOrder(
                id = "p_decomp",
                category = OrderCategory.PROCEDURE,
                name = "CHỌC HÚT KIM GIẢI ÁP CẤP CỨU (Needle Thoracocentesis)",
                description = "Dùng kim luồn 14-16G chọc khoang liên sườn II đường trung đòn (hoặc khoang LS V đường nách trước) bên phải giải áp tức thì",
                costVnd = 350000L,
                suppliesUsed = listOf("Kim luồn 14G chuyên dụng", "Van một chiều Heimlich", "Săng lỗ vô khuẩn", "Bơm tiêm 50ml"),
                isEssential = true,
                feedbackOnExecution = "TIẾNG XÌ HƠI LỚN BẬT RA! Khí áp lực thoát ồ ạt, lồng ngực nở lại, tĩnh mạch cổ xẹp xuống, huyết áp vọt lên ngay 110/70 mmHg!"
            ),
            EmergencyOrder(
                id = "p_chest_tube",
                category = OrderCategory.PROCEDURE,
                name = "Đặt ống dẫn lưu màng phổi kín (Chest Tube thoracostomy)",
                description = "Ống dẫn lưu 28-32Fr khoang liên sườn V đường nách giữa nối bình hút âm liên tục",
                costVnd = 850000L,
                suppliesUsed = listOf("Ống dẫn lưu màng phổi 28Fr", "Bình dẫn lưu áp lực âm 3 ngăn", "Chỉ khâu Silk 2.0", "Lưỡi dao mổ số 11", "Kìm kẹp Trocar"),
                isEssential = true,
                feedbackOnExecution = "Ống dẫn lưu hoạt động tốt, khí sủi bọt liên tục theo nhịp thở."
            ),
            EmergencyOrder(
                id = "p_o2_high",
                category = OrderCategory.AIRWAY,
                name = "Thở oxy qua mặt nạ có túi dự trữ (Non-rebreather mask) 12-15 L/phút",
                description = "Cung cấp FiO2 tối đa chống thiếu oxy não khẩn cấp",
                costVnd = 120000L,
                suppliesUsed = listOf("Mặt nạ thở oxy có túi dự trữ", "Dây oxy áp lực cao"),
                isEssential = true,
                feedbackOnExecution = "SpO2 nhích lên 94% sau khi kết hợp giải áp lồng ngực."
            ),
            EmergencyOrder(
                id = "p_wrong_delay",
                category = OrderCategory.PROCEDURE,
                name = "Chuyển bệnh nhân đi chụp CT Scanner Ngực toàn diện trước khi xử trí",
                description = "Trì hoãn giải áp khẩn cấp để chụp phim",
                costVnd = 1250000L,
                suppliesUsed = listOf("Phim chụp CT Scanner", "Thuốc cản quang"),
                isEssential = false,
                isHarmful = true,
                feedbackOnExecution = "SAI LẦM CHÍ MẠNG! Bệnh nhân trụy mạch ngay trên cáng chụp CT do trung thất bị chèn ép ép nghẽn tĩnh mạch chủ, ngừng tuần hoàn hô hấp!"
            )
        )

        return PatientCase(
            id = "case_pneumo_02",
            patientName = "Trần Đình Trọng",
            age = 24,
            gender = "Nam",
            occupation = "Sinh viên đại học",
            triageLevel = TriageLevel.RED,
            chiefComplaint = "Khó thở dữ dội, tím tái môi đầu chi, đau ngực phải chói sau cú va chạm xe máy",
            arrivalTime = "15:40",
            initialVitals = VitalSigns(
                heartRate = 138,
                bpSys = 70,
                bpDia = 40,
                spo2 = 74,
                respRate = 38,
                temperature = 36.6f,
                gcs = 12,
                rhythm = EcgRhythm.NORMAL_SINUS
            ),
            abcde = AbcdeAssessment(
                airwayDesc = "Khí quản bị đẩy lệch rõ rệt sang bên trái. Họng không dị vật.",
                airwayIsClear = false,
                breathingDesc = "Thở ngực nghịch ngôn 38 lần/phút, co kéo hõm ức dữ dội, lồng ngực phải căng phồng bất động, gõ vang trống toàn bộ phổi phải, rì rào phế nang phổi phải mất hoàn toàn.",
                breathingIsNormal = false,
                circulationDesc = "Mạch quay nhanh yếu khó bắt 138 lần/phút, huyết áp tụt sâu 70/40 mmHg, tĩnh mạch cổ nổi căng phồng tự nhiên.",
                circulationIsNormal = false,
                disabilityDesc = "GCS 12/15, kích thích vật vã vì thiếu oxy não, vã mồ hôi đầm đìa.",
                disabilityIsNormal = false,
                exposureDesc = "Có vết bầm tím xây xát ở thành ngực bên phải do tay lái xe máy đập vào.",
                exposureIsNormal = false
            ),
            historyQuestions = listOf(
                PatientHistoryQA("q1", "Anh bị tai nạn như thế nào?", "Cậu ấy đi xe máy bị xe ba gác quệt phải, ngực phải va đập mạnh vào tay lái, sau đó ôm ngực thở không ra hơi, tím tái ngất lịm đi!", "Bạn đi cùng"),
                PatientHistoryQA("q2", "Trước tai nạn bạn ấy có bệnh phổi gì không?", "Cậu ấy cao gầy (1m82 nặng 56kg), thỉnh thoảng có hút thuốc lá điện tử, chưa từng nằm viện.", "Bạn đi cùng")
            ),
            physicalExams = listOf(
                PhysicalExamSystemItem("sys_general", "Toàn thân", "Tím tái toàn thân, môi xanh xao, vã mồ hôi lạnh, vật vã hốt hoảng.", true),
                PhysicalExamSystemItem("sys_resp", "Hô hấp", "Tam chứng Galliard điển hình ở phổi phải: Rung thanh mất, Gõ vang trống, Rì rào phế nang mất hoàn toàn. Khí quản lệch trái rõ rệt.", true),
                PhysicalExamSystemItem("sys_cardio", "Tim mạch", "Tĩnh mạch cổ nổi căng phồng. Tiếng tim nhanh mờ, bị đẩy lệch sang trái, mạch nhanh nhỏ huyết áp tụt 70/40 mmHg.", true),
                PhysicalExamSystemItem("sys_gi", "Tiêu hóa", "Bụng mềm, không chướng, không phản ứng phúc mạc.", false),
                PhysicalExamSystemItem("sys_neuro", "Thần kinh", "Lơ mơ kích thích do thiếu máu não và tụt oxy, đồng tử 3mm đều 2 bên.", true),
                PhysicalExamSystemItem("sys_skin", "Da & Chấn thương", "Bầm tím diện tích 8x5cm vùng liên sườn IV-V bên phải, tràn khí dưới da nhẹ.", true)
            ),
            availableLabs = labs,
            goldenDiagnosis = "Tràn khí màng phổi áp lực bên phải cấp tính sau chấn thương ngực kín (Tension Pneumothorax)",
            diagnosisKeywords = listOf("tran khi mang phoi", "ap luc", "tension pneumothorax", "phoi phai", "chan thuong nguc"),
            goldenDifferentials = listOf("Chèn ép tim cấp (Cardiac Tamponade)", "Tràn máu màng phổi lượng lớn", "Thủng vỡ phế quản gốc", "Cơn hen phế quản ác tính"),
            goldenClinicalReasoning = "Bệnh nhân nam trẻ tuổi sau chấn thương ngực có triệu chứng suy hô hấp cấp nặng kèm tụt huyết áp sốc tắc nghẽn. Khám lâm sàng thấy tam chứng Galliard phổi phải, khí quản lệch trái và tĩnh mạch cổ nổi căng phồng. Đây là CẤP CỨU TỐI KHẨN Tràn khí màng phổi áp lực cần chọc kim giải áp ngay tại giường!",
            standardOrders = MasterHospitalOrders.getAllMasterOrders(),
            badDelayNarrative = "Do chờ đợi làm các xét nghiệm hình ảnh mà không giải áp màng phổi cấp cứu, áp lực trong lồng ngực tăng vọt làm xẹp hoàn toàn tĩnh mạch chủ, ngừng tuần hoàn hô hấp!",
            badDelayVitals = VitalSigns(0, 0, 0, 0, 0, 36.4f, 3, EcgRhythm.ASYSTOLE),
            successfulRescueNarrative = "Bác sĩ đã dũng cảm chọc kim 14G giải áp khoang LS II bên phải ngay tại giường! Khí xì ồ ạt, áp lực lồng ngực giải tỏa, huyết áp tăng vọt lên 118/72 mmHg, SpO2 lên 97%. Cứu sống bệnh nhân ngoạn mục!",
            successVitals = VitalSigns(88, 120, 75, 98, 18, 36.7f, 15, EcgRhythm.NORMAL_SINUS),
            clinicalPearls = "Ghi nhớ quy tắc Bộ Y Tế: TRÀN KHÍ MÀNG PHỔI ÁP LỰC là chẩn đoán lâm sàng - KHÔNG ĐƯỢC CHỜ X-QUANG ĐỂ XỬ TRÍ! Chọc kim giải áp tức thì cứu mạng trong vòng 30 giây.",
            gratitudeSpeaker = "Bệnh nhân Trọng & Bạn thân",
            gratitudeMessage = "Bác sĩ ơi, khoảnh khắc mũi kim cắm vào xì hơi ra, em như được tái sinh vậy! Em cảm ơn bác sĩ nhiều lắm!"
        )
    }

    // ==========================================
    // CASE 3: CƠN HEN PHẾ QUẢN ÁC TÍNH
    // ==========================================
    private fun createCase3SevereAsthma(): PatientCase {
        val labs = createBaseLabList(
            abgResult = "pH: 7.15, PaCO2: 68 mmHg (Tăng CO2 nặng do kiệt cơ hô hấp), PaO2: 50 mmHg, HCO3-: 23 mmol/L. Toan hô hấp mất bù đe dọa tính mạng!",
            abgCritical = true, abgRecommended = true,
            cbcResult = "Bạch cầu (WBC): 14.2 G/L, Bạch cầu ái toan (Eosinophil) tăng 8.5%, Hemoglobin: 142 g/L.",
            xrayChestResult = "Hai phế trường tăng sáng ứ khí, các khoang liên sườn dãn rộng nằm ngang, vòm hoành hạ thấp dẹt 2 bên, rốn phổi đậm."
        )

        val orders = listOf(
            EmergencyOrder(
                id = "a_salbutamol",
                category = OrderCategory.AIRWAY,
                name = "Khí dung Salbutamol 5mg + Ipratropium 0.5mg liên tục qua máy thở khí dung oxy",
                description = "Thuốc dãn phế quản tác dụng nhanh liều cao phối hợp",
                costVnd = 85000L,
                suppliesUsed = listOf("Bầu khí dung", "Mặt nạ khí dung người lớn", "Dây thở oxy"),
                isEssential = true,
                feedbackOnExecution = "Đang khí dung liên tục với lưu lượng oxy 8 lít/phút."
            ),
            EmergencyOrder(
                id = "a_corticoid",
                category = OrderCategory.MEDICATION,
                name = "Methylprednisolone 80mg - 120mg tiêm tĩnh mạch",
                description = "Corticoid toàn thân chống viêm đường thở khẩn cấp",
                costVnd = 55000L,
                suppliesUsed = listOf("Bơm tiêm 5ml", "Kim tiêm 18G", "Dung môi pha tiêm"),
                isEssential = true,
                feedbackOnExecution = "Đã tiêm tĩnh mạch Methylprednisolone."
            ),
            EmergencyOrder(
                id = "a_mgso4",
                category = OrderCategory.MEDICATION,
                name = "Magnesi Sulfat 2g pha 100ml NaCl 0.9% truyền tĩnh mạch trong 20 phút",
                description = "Dãn cơ trơn phế quản hiệu quả trong cơn hen phế quản đe dọa tính mạng",
                costVnd = 40000L,
                suppliesUsed = listOf("Chai NaCl 0.9% 100ml", "Dây truyền dịch có khóa điều chỉnh"),
                isEssential = true,
                feedbackOnExecution = "Đang truyền tĩnh mạch Magnesi Sulfat."
            ),
            EmergencyOrder(
                id = "a_adren_sc",
                category = OrderCategory.MEDICATION,
                name = "Adrenaline 0.3mg tiêm dưới da (hoặc tiêm bắp)",
                description = "Dãn phế quản mạnh mẽ khi không đáp ứng với khí dung",
                costVnd = 25000L,
                suppliesUsed = listOf("Bơm tiêm tiểu đường 1ml", "Bông cồn sát khuẩn"),
                isEssential = true,
                feedbackOnExecution = "Đã tiêm Adrenaline, lồng ngực bắt đầu có tiếng thông khí rale rít trở lại."
            ),
            EmergencyOrder(
                id = "a_wrong_sedative",
                category = OrderCategory.MEDICATION,
                name = "Tiêm Seduxen (Diazepam) để an thần cho bệnh nhân bớt hoảng loạn",
                description = "Thuốc an thần ức chế hô hấp (TUYỆT ĐỐI CHỐNG CHỈ ĐỊNH)",
                costVnd = 30000L,
                suppliesUsed = listOf("Bơm tiêm 3ml"),
                isEssential = false,
                isHarmful = true,
                feedbackOnExecution = "THẢM HỌA! Diazepam ức chế trung tâm hô hấp làm bệnh nhân ngừng thở hoàn toàn, hôn mê sâu SpO2 tụt còn 45%!"
            )
        )

        return PatientCase(
            id = "case_asthma_03",
            patientName = "Lê Thị Thảo",
            age = 32,
            gender = "Nữ",
            occupation = "Công nhân may mặc",
            triageLevel = TriageLevel.RED,
            chiefComplaint = "Khó thở dữ dội, không nói được trọn câu, vã mồ hôi, tím môi, tiền sử hen phế quản lâu năm",
            arrivalTime = "16:20",
            initialVitals = VitalSigns(
                heartRate = 135,
                bpSys = 150,
                bpDia = 95,
                spo2 = 82,
                respRate = 36,
                temperature = 37.1f,
                gcs = 13,
                rhythm = EcgRhythm.NORMAL_SINUS
            ),
            abcde = AbcdeAssessment(
                airwayDesc = "Khí đạo thông, dịch tiết đờm dính quánh, co thắt dữ dội đường thở dưới.",
                airwayIsClear = true,
                breathingDesc = "Thở 36 lần/phút, lồng ngực căng phồng ứ khí. ĐẶC BIỆT: Nghe phổi im lặng hoàn toàn (PHỔI CÂM - SILENT CHEST)!",
                breathingIsNormal = false,
                circulationDesc = "Mạch nhanh 135 lần/phút, mạch nghịch thường. Huyết áp 150/95 mmHg.",
                circulationIsNormal = false,
                disabilityDesc = "GCS 13, lú lẫn kiệt sức, chỉ gật đầu không nói nổi từng từ.",
                disabilityIsNormal = false,
                exposureDesc = "Toàn thân vã mồ hôi đầm đìa, tím nhẹ ở môi và đầu ngón tay.",
                exposureIsNormal = false
            ),
            historyQuestions = listOf(
                PatientHistoryQA("q1", "Chị bị hen lâu chưa và cơn khó thở này bắt đầu từ khi nào?", "Chị ấy bị hen từ nhỏ. Chiều nay quét bụi xưởng may về thì lên cơn khó thở, xịt Ventolin 6 nhát không thấy đỡ mà ngày càng ngạt thở nghẹn họng!", "Chồng bệnh nhân"),
                PatientHistoryQA("q2", "Chị ấy có từng phải đặt ống thở vì hen bao giờ chưa?", "Cách đây 2 năm từng nằm cấp cứu hồi sức tích cực 1 lần vì cơn hen ác tính!", "Chồng bệnh nhân")
            ),
            physicalExams = listOf(
                PhysicalExamSystemItem("sys_general", "Toàn thân", "Ngồi chồm ra phía trước (Tripod position), vã mồ hôi, môi tím tái, kiệt sức thở.", true),
                PhysicalExamSystemItem("sys_resp", "Hô hấp", "Lồng ngực căng phồng hình thùng. Đặt ống nghe: PHỔI CÂM (Silent Chest) - dấu hiệu đường thở co thắt gần như tắc nghẽn hoàn toàn!", true),
                PhysicalExamSystemItem("sys_cardio", "Tim mạch", "Tiếng tim nhanh T1 T2 đều rõ 135 lần/phút, mạch nghịch thường, huyết áp 150/95 mmHg.", true),
                PhysicalExamSystemItem("sys_gi", "Tiêu hóa", "Bụng mềm, gan lách không to.", false),
                PhysicalExamSystemItem("sys_neuro", "Thần kinh", "Lơ mơ lẫn lộn do toan hô hấp tăng CO2 máu nặng (PaCO2 68 mmHg).", true),
                PhysicalExamSystemItem("sys_skin", "Da & Chấn thương", "Da lạnh vã mồ hôi, tím tái ngoại vi.", true)
            ),
            availableLabs = labs,
            goldenDiagnosis = "Cơn hen phế quản ác tính đe dọa tính mạng - Dấu hiệu Phổi câm (Severe Acute Life-threatening Asthma / Silent Chest)",
            diagnosisKeywords = listOf("hen phe quan", "ac tinh", "phoi cam", "silent chest", "suy ho hap"),
            goldenDifferentials = listOf("Dị vật đường thở bỏ quên", "Tràn khí màng phổi tự phát", "Phù phổi cấp huyết động", "Sốc phản vệ"),
            goldenClinicalReasoning = "Bệnh nhân hen nặng có dấu hiệu 'Phổi câm' (Silent Chest) - cấp cứu tối khẩn chứng tỏ tắc nghẽn phế quản tối đa đe dọa ngưng thở. Cần phối hợp Salbutamol khí dung liên tục, Corticoid tiêm mạch, Magnesi Sulfat và Adrenaline tiêm bắp.",
            standardOrders = MasterHospitalOrders.getAllMasterOrders(),
            badDelayNarrative = "Do dùng thuốc an thần hoặc chậm trễ xử trí tích cực, bệnh nhân kiệt cơ hô hấp hoàn toàn, ngừng thở và chuyển sang ngừng tim!",
            badDelayVitals = VitalSigns(0, 0, 0, 0, 0, 36.5f, 3, EcgRhythm.ASYSTOLE),
            successfulRescueNarrative = "Nhờ phối hợp nhanh chóng Salbutamol khí dung liên tục, Corticoid tiêm mạch, Magnesi Sulfat và tiêm bắp Adrenaline, luồng khí đã thông qua phế quản! Bệnh nhân thoát khỏi nguy cơ đặt nội khí quản!",
            successVitals = VitalSigns(92, 125, 78, 97, 20, 37.0f, 15, EcgRhythm.NORMAL_SINUS),
            clinicalPearls = "Nhận định kinh điển: Trong cơn hen nặng, nghe phổi 'không có tiếng rale rít' KHÔNG PHẢI LÀ KHỎI MÀ LÀ 'PHỔI CÂM' (SILENT CHEST) - dấu hiệu tử vong cận kề! Tuyệt đối không bao giờ được dùng thuốc an thần cho bệnh nhân hen đang khó thở.",
            gratitudeSpeaker = "Chị Thảo & Chồng",
            gratitudeMessage = "Bác sĩ ơi, lúc nãy ngực em nghẹn cứng tưởng như chết ngạt rồi! Nhờ bác sĩ cho thuốc kịp thời em mới thở lại được bình thường!"
        )
    }

    // ==========================================
    // CASE 4: SỐC PHẢN VỆ ĐỘ III
    // ==========================================
    private fun createCase4Anaphylaxis(): PatientCase {
        val labs = createBaseLabList(
            abgResult = "pH: 7.28, PaO2: 65 mmHg, PaCO2: 44 mmHg, Lactate: 3.8 mmol/L (Toan chuyển hóa tăng acid lactic do tụt huyết áp phân bố).",
            abgCritical = true, abgRecommended = true,
            cbcResult = "WBC: 11.5 G/L, Hemoglobin: 155 g/L (cô đặc máu do thoát huyết tương ra gian bào)."
        )

        val orders = listOf(
            EmergencyOrder(
                id = "ad_epi_im",
                category = OrderCategory.MEDICATION,
                name = "ADRENALINE 1mg/1ml TIÊM BẮP NGAY 1/2 ỐNG (0.5mg) MẶT TRƯỚC NGOÀI ĐÙI",
                description = "THUỐC THIẾT YẾU SỐ 1 TRONG SỐC PHẢN VỆ - Tiêm bắp mặt trước ngoài đùi, nhắc lại sau mỗi 3-5 phút!",
                costVnd = 25000L,
                suppliesUsed = listOf("Bơm tiêm 1ml", "Kim tiêm bắp 23G", "Bông cồn tiêm đùi"),
                isEssential = true,
                feedbackOnExecution = "ĐÃ TIÊM ADRENALINE 0.5mg TIÊM BẮP ĐÙI! Sau 3 phút: Huyết áp hồi phục vọt lên 105/65 mmHg, tiếng rít thanh quản dịu bớt, đường thở mở lại!"
            ),
            EmergencyOrder(
                id = "ad_fluid",
                category = OrderCategory.MEDICATION,
                name = "Truyền nhanh dung dịch Natri Clorid 0.9% hoặc Ringer Lactate 1000ml tĩnh mạch",
                description = "Bù dịch chống sốc phân bố thoát quản",
                costVnd = 80000L,
                suppliesUsed = listOf("Chai Ringer Lactate 500ml (x2)", "Dây truyền dịch lớn 15 giọt", "Kim luồn tĩnh mạch 16G"),
                isEssential = true,
                feedbackOnExecution = "Đã cắm 2 đường truyền tĩnh mạch lớn xả nhanh dịch."
            ),
            EmergencyOrder(
                id = "ad_corticoid",
                category = OrderCategory.MEDICATION,
                name = "Methylprednisolone 80mg tiêm TM + Diphenhydramine 50mg tiêm bắp",
                description = "Thuốc hàng hai (sau Adrenaline) phòng phản vệ pha 2",
                costVnd = 60000L,
                suppliesUsed = listOf("Bơm tiêm 5ml", "Kim tiêm vô khuẩn"),
                isEssential = true,
                feedbackOnExecution = "Đã tiêm phối hợp Corticoid và Kháng Histamin H1."
            ),
            EmergencyOrder(
                id = "ad_wrong_delay",
                category = OrderCategory.MEDICATION,
                name = "Chỉ cho tiêm Kháng Histamin và Corticoid, theo dõi thêm rồi mới tính Adrenaline",
                description = "Sai lầm trì hoãn Adrenaline",
                costVnd = 40000L,
                suppliesUsed = listOf("Bơm tiêm 2ml"),
                isEssential = false,
                isHarmful = true,
                feedbackOnExecution = "SAI LẦM CHÍ MẠNG! Corticoid và Kháng histamin cần 4-6 giờ mới có tác dụng! Phù Quinke bít kín thanh môn, huyết áp tụt còn 0/0, bệnh nhân tử vong vì ngạt thở tắc đường thở!"
            )
        )

        return PatientCase(
            id = "case_anaph_04",
            patientName = "Hoàng Kim Ngân",
            age = 28,
            gender = "Nữ",
            occupation = "Dược sĩ",
            triageLevel = TriageLevel.RED,
            chiefComplaint = "Sau tiêm kháng sinh Ceftriaxone 10 phút: Nổi mẩn đỏ ngứa toàn thân, nghẹn họng khó thở dữ dội, hoa mắt chóng mặt ngất lịm",
            arrivalTime = "17:10",
            initialVitals = VitalSigns(
                heartRate = 142,
                bpSys = 65,
                bpDia = 35,
                spo2 = 78,
                respRate = 34,
                temperature = 36.5f,
                gcs = 11,
                rhythm = EcgRhythm.NORMAL_SINUS
            ),
            abcde = AbcdeAssessment(
                airwayDesc = "PHÙ NỀ QUINCKE: Môi và lưỡi sưng vù, giọng khàn đặc, nghe tiếng thở rít thanh quản (Stridor).",
                airwayIsClear = false,
                breathingDesc = "Thở nhanh nông 34 lần/phút, co kéo hõm ức, phổi nghe rale rít lan tỏa 2 bên.",
                breathingIsNormal = false,
                circulationDesc = "Mạch quay rất nhỏ nhanh 142 lần/phút, huyết áp tụt sâu còn 65/35 mmHg, da tái lạnh vã mồ hôi.",
                circulationIsNormal = false,
                disabilityDesc = "GCS 11/15, lơ mơ sắp trụy mạch ngất sâu do tụt huyết áp não.",
                disabilityIsNormal = false,
                exposureDesc = "Toàn thân nổi ban mề đay phù mạch đỏ rực từ mặt, ngực đến chân tay, ngứa dữ dội.",
                exposureIsNormal = false
            ),
            historyQuestions = listOf(
                PatientHistoryQA("q1", "Sự việc diễn ra như thế nào?", "Cô ấy bị viêm phế quản, vừa tiêm 1 mũi Ceftriaxone tại phòng khám tư được 10 phút thì kêu ngứa ran lòng bàn tay, mắt sưng húp, khó thở thở khè khè rồi ngã quỵ xuống đất!", "Đồng nghiệp đưa vào"),
                PatientHistoryQA("q2", "Trước đây cô ấy có dị ứng gì không?", "Từng bị dị ứng nổi mày đay khi ăn tôm cua biển, chưa bao giờ tiêm kháng sinh nhóm Cephalosporin.", "Đồng nghiệp đưa vào")
            ),
            physicalExams = listOf(
                PhysicalExamSystemItem("sys_general", "Toàn thân", "Mặt phù nề biến dạng, môi vều to, ban mề đay dát sần đỏ rải rác toàn thân.", true),
                PhysicalExamSystemItem("sys_resp", "Hô hấp", "Tiếng thở rít Stridor thanh quản nghe rõ từ xa, rale rít rale ngáy cả 2 phế trường.", true),
                PhysicalExamSystemItem("sys_cardio", "Tim mạch", "Tiếng tim nhanh mờ, mạch đập chỉ còn cảm nhận lờ mờ ở bẹn, huyết áp 65/35 mmHg.", true),
                PhysicalExamSystemItem("sys_gi", "Tiêu hóa", "Đau quặn bụng, buồn nôn, vã mồ hôi.", true),
                PhysicalExamSystemItem("sys_neuro", "Thần kinh", "Lơ mơ tri giác suy giảm do tụt huyết áp nặng.", true),
                PhysicalExamSystemItem("sys_skin", "Da & Chấn thương", "Ban mề đay phù mạch toàn thân dạng bản lớn gồ trên mặt da.", true)
            ),
            availableLabs = labs,
            goldenDiagnosis = "Sốc phản vệ độ III (nguy kịch) do kháng sinh Ceftriaxone (Anaphylactic Shock Grade III)",
            diagnosisKeywords = listOf("soc phan ve", "anaphylaxis", "do iii", "ceftriaxone", "adrenaline"),
            goldenDifferentials = listOf("Phù mạch di truyền do thiếu C1-INH", "Cơn hen phế quản cấp", "Sốc tim do nhồi máu cơ tim", "Hạ huyết áp tư thế"),
            goldenClinicalReasoning = "Đạt chuẩn chẩn đoán Sốc phản vệ độ III theo Thông tư 51/2017/TT-BYT của Bộ Y Tế. Xử trí duy nhất và quyết định sống còn là TIÊM BẮP ADRENALINE NGAY LẬP TỨC ở mặt trước ngoài đùi.",
            standardOrders = MasterHospitalOrders.getAllMasterOrders(),
            badDelayNarrative = "Không tiêm Adrenaline ngay dẫn tới phù nề thanh môn bít tắc hoàn toàn khí quản, thiếu oxy não không thể hồi phục và ngừng tim!",
            badDelayVitals = VitalSigns(0, 0, 0, 0, 0, 36.0f, 3, EcgRhythm.ASYSTOLE),
            successfulRescueNarrative = "Nhát tiêm Adrenaline 0.5mg tiêm bắp đùi kịp thời đã cứu sống bệnh nhân trong gang tấc! Thanh môn mở rộng trở lại, huyết áp vọt lên 110/70 mmHg, bệnh nhân tỉnh táo hoàn toàn!",
            successVitals = VitalSigns(90, 115, 72, 98, 18, 36.8f, 15, EcgRhythm.NORMAL_SINUS),
            clinicalPearls = "Quy định bất di bất dịch Bộ Y Tế (Thông tư 51): ADRENALINE là thuốc thiết yếu duy nhất cứu mạng sốc phản vệ! Đường tiêm bắp (mặt trước ngoài đùi) hấp thu nhanh gấp nhiều lần tiêm dưới da. Có chỉ định là tiêm ngay, không chần chừ!",
            gratitudeSpeaker = "Chị Ngân & Mẹ",
            gratitudeMessage = "Bác sĩ ơi, con gái tôi vừa được bác sĩ tiêm mũi thuốc cứu sống thần kỳ! Cả nhà tôi xin cúi đầu cảm ơn bác sĩ đã cứu lấy mạng sống cháu!"
        )
    }

    // ==========================================
    // CASE 5: ĐỘT QUỴ THIẾU MÁU NÃO CẤP GIỜ VÀNG
    // ==========================================
    private fun createCase5AcuteStroke(): PatientCase {
        val labs = createBaseLabList(
            ctBrainResult = "CT sọ não không cản quang: KHÔNG THẤY XUẤT HUYẾT NÃO. Có dấu hiệu mờ rãnh vỏ não nhẹ, điểm ASPECTS 8 điểm. ĐỦ ĐIỀU KIỆN TIÊU SỢI HUYẾT ĐƯỜNG TĨNH MẠCH!",
            ctBrainCritical = true, ctBrainRecommended = true,
            bloodSugarResult = "Đường huyết mao mạch tại giường: 6.2 mmol/L (Bình thường, loại trừ hạ đường huyết).",
            coagResult = "PT: 12.4s, INR: 1.05, aPTT: 28s, Fibrinogen: 3.2 g/L (Đông máu hoàn toàn bình thường)."
        )

        val orders = listOf(
            EmergencyOrder(
                id = "st_rtpa",
                category = OrderCategory.MEDICATION,
                name = "TIÊU SỢI HUYẾT Alteplase (rTPA) liều 0.9 mg/kg (10% bolus, 90% truyền tĩnh mạch 1 giờ)",
                description = "Thuốc cứu não giờ vàng số 1 theo khuyến cáo Bộ Y Tế",
                costVnd = 12500000L,
                suppliesUsed = listOf("Lọ Alteplase 50mg bột đông khô", "Dung môi pha loãng chuyên dụng", "Bơm tiêm điện tự động", "Dây truyền bơm tiêm điện"),
                isEssential = true,
                feedbackOnExecution = "ĐÃ TIÊM BOLUS VÀ BẮT ĐẦU TRUYỀN rTPA! Cục huyết khối bắt đầu tan, tưới máu vùng não phục hồi!"
            ),
            EmergencyOrder(
                id = "st_bp_control",
                category = OrderCategory.MEDICATION,
                name = "Kiểm soát huyết áp Nicardipine truyền tĩnh mạch duy trì < 180/105 mmHg",
                description = "Đảm bảo mức huyết áp an toàn trước và trong khi dùng thuốc tiêu sợi huyết",
                costVnd = 350000L,
                suppliesUsed = listOf("Ống Nicardipine 10mg", "Chai NaCl 0.9% 100ml", "Dây truyền chống ánh sáng"),
                isEssential = true,
                feedbackOnExecution = "Huyết áp hạ êm dịu từ 190/110 về mức 160/90 mmHg an toàn."
            ),
            EmergencyOrder(
                id = "st_dsa_thrombectomy",
                category = OrderCategory.CONSULTATION,
                name = "Báo động Code Stroke can thiệp lấy huyết khối cơ học (Endovascular Thrombectomy)",
                description = "Phối hợp lấy huyết khối động mạch lớn não giữa M1",
                costVnd = 65000000L,
                suppliesUsed = listOf("Bộ dụng cụ hút huyết khối cơ học Penumbra", "Ống thông Guiding catheter", "Stent retriever Solitaire"),
                isEssential = true,
                feedbackOnExecution = "Phòng can thiệp mạch não đã kích hoạt, can thiệp lấy trọn cục máu đông thành công!"
            ),
            EmergencyOrder(
                id = "st_wrong_delay",
                category = OrderCategory.MEDICATION,
                name = "Cho bệnh nhân uống Aspirin 300mg ngay trước khi chụp phim CT sọ não",
                description = "Uống kháng kết tập tiểu cầu trước khi loại trừ xuất huyết não (CHỐNG CHỈ ĐỊNH)",
                costVnd = 20000L,
                suppliesUsed = listOf("Cốc uống thuốc"),
                isEssential = false,
                isHarmful = true,
                feedbackOnExecution = "NGUY HIỂM! Nếu là xuất huyết não thì Aspirin sẽ làm máu chảy ồ ạt tử vong! Ngoài ra nếu dùng rTPA sau đó thì Aspirin bị chống chỉ định trong 24h đầu!"
            )
        )

        return PatientCase(
            id = "case_stroke_05",
            patientName = "Vũ Mạnh Hùng",
            age = 64,
            gender = "Nam",
            occupation = "Cán bộ hưu trí",
            triageLevel = TriageLevel.RED,
            chiefComplaint = "Đột ngột méo miệng bên phải, liệt hoàn toàn nửa người phải, ú ớ không nói được lúc 15:00 (giờ thứ 1.5)",
            arrivalTime = "16:30",
            initialVitals = VitalSigns(
                heartRate = 84,
                bpSys = 192,
                bpDia = 110,
                spo2 = 96,
                respRate = 20,
                temperature = 36.8f,
                gcs = 13,
                rhythm = EcgRhythm.NORMAL_SINUS
            ),
            abcde = AbcdeAssessment(
                airwayDesc = "Đường thở thông thoáng, nuốt sặc nhẹ thức ăn dở.",
                airwayIsClear = true,
                breathingDesc = "Thở đều 20 lần/phút, phổi trong.",
                breathingIsNormal = true,
                circulationDesc = "Mạch 84 lần/phút đều rõ, huyết áp tăng cao 192/110 mmHg.",
                circulationIsNormal = false,
                disabilityDesc = "THANG ĐIỂM NIHSS 14: Liệt mặt trung ương bên phải (méo miệng), liệt vận động tay phải cơ lực 0/5, chân phải 1/5, thất ngôn Broca.",
                disabilityIsNormal = false,
                exposureDesc = "Không có vết thương, không sốt.",
                exposureIsNormal = true
            ),
            historyQuestions = listOf(
                PatientHistoryQA("q1", "Người bệnh bị lúc mấy giờ chính xác?", "Chính xác là 15 giờ 00 phút đang ngồi xem tivi thì rơi chén trà, nghiêng người méo xệch mặt, chúng tôi gọi cấp cứu 115 chở vào ngay!", "Con gái bệnh nhân"),
                PatientHistoryQA("q2", "Bác có đang dùng thuốc chống đông máu hay có tiền sử phẫu thuật gì gần đây không?", "Bác bị tăng huyết áp và rung nhĩ, có uống thuốc huyết áp nhưng KHÔNG dùng thuốc chống đông máu, 3 tháng qua không mổ xẻ hay chấn thương sọ não gì.", "Con gái bệnh nhân")
            ),
            physicalExams = listOf(
                PhysicalExamSystemItem("sys_general", "Toàn thân", "Tỉnh táo, vẻ mặt hoảng sợ vì không nói được và liệt nửa người.", false),
                PhysicalExamSystemItem("sys_neuro", "Thần kinh", "Dấu thần kinh khu trú rõ rệt: Liệt dây VII trung ương phải, mất vận động nửa người phải (cơ lực tay 0/5, chân 1/5), phản xạ Babinski bên phải dương tính, thất ngôn Broca (NIHSS 14 điểm).", true),
                PhysicalExamSystemItem("sys_cardio", "Tim mạch", "Tiếng tim loạn nhịp hoàn toàn (Rung nhĩ), huyết áp 192/110 mmHg.", true),
                PhysicalExamSystemItem("sys_resp", "Hô hấp", "Thở êm 20 l/p, phổi không rale.", false),
                PhysicalExamSystemItem("sys_gi", "Tiêu hóa", "Bụng mềm, không chướng.", false),
                PhysicalExamSystemItem("sys_skin", "Da & Chấn thương", "Da ấm hồng, không bầm dập chấn thương sọ não.", false)
            ),
            availableLabs = labs,
            goldenDiagnosis = "Đột quỵ thiếu máu não cấp bán cầu trái giờ thứ 2 - Tắc động mạch não giữa (MCA) - Rung nhĩ - Giờ vàng điều trị tiêu sợi huyết (Acute Ischemic Stroke)",
            diagnosisKeywords = listOf("dot quy", "thieu mau nao", "nhoi mau nao", "gio vang", "tieu soi huyet", "rtpa", "alteplase"),
            goldenDifferentials = listOf("Xuất huyết não nội sọ", "Hạ đường huyết cấp", "Cơn thoáng thiếu máu não (TIA)", "Liệt Bell dây VII ngoại biên"),
            goldenClinicalReasoning = "Bệnh nhân có triệu chứng FAST điển hình trong vòng 4.5 giờ vàng. CT sọ não không cản quang loại trừ hoàn toàn xuất huyết não và thang điểm ASPECTS 8 điểm. Đạt đầy đủ tiêu chuẩn chỉ định dùng thuốc tiêu sợi huyết Alteplase (rTPA).",
            standardOrders = MasterHospitalOrders.getAllMasterOrders(),
            badDelayNarrative = "Do trì hoãn quá mốc 4.5 giờ vàng, cửa sổ điều trị tiêu sợi huyết đã khép lại! Vùng não hoại tử lan rộng, bệnh nhân liệt nửa người vĩnh viễn và phù não chèn ép tử vong!",
            badDelayVitals = VitalSigns(80, 185, 105, 95, 22, 38.2f, 8, EcgRhythm.NORMAL_SINUS),
            successfulRescueNarrative = "Nhờ dùng thuốc tiêu sợi huyết rTPA chuẩn giờ vàng kèm kiểm soát huyết áp, mạch máu não tắc nghẽn đã được tái thông ngoạn mục! Sau 2 giờ, tay chân bệnh nhân cử động lại được (cơ lực 4/5), nói lại được tròn vành rõ chữ!",
            successVitals = VitalSigns(78, 135, 82, 98, 16, 36.8f, 15, EcgRhythm.NORMAL_SINUS),
            clinicalPearls = "Quy tắc vàng Bộ Y Tế: 'TIME IS BRAIN - THỜI GIAN LÀ NÃO'! Mỗi phút trôi qua mất 1.9 triệu tế bào thần kinh. Cửa sổ giờ vàng rTPA là 4.5 giờ từ lúc khởi phát. Tuyệt đối không để việc chờ đợi xét nghiệm làm chậm trễ chụp CT và dùng thuốc!",
            gratitudeSpeaker = "Bác Hùng & Con gái",
            gratitudeMessage = "Bác sĩ ơi, tôi cử động lại được tay chân rồi, nói được rồi! Con gái tôi khóc nấc vì mừng rỡ, cảm ơn bác sĩ đã cứu bố con tôi!"
        )
    }

    // ==========================================
    // CASE 6: THỦNG TẠNG RỖNG - VIÊM PHÚC MẠC
    // ==========================================
    private fun createCase6Peritonitis(): PatientCase {
        val labs = createBaseLabList(
            xrayAbdResult = "X-quang bụng đứng không chuẩn bị: HƠI TỰ DO DƯỚI CƠ HOÀNH HÌNH LIỀM SÁNG RÕ 2 BÊN (Liềm hơi dưới hoành) - Dấu hiệu khẳng định 100% thủng tạng rỗng!",
            xrayAbdCritical = true, xrayAbdRecommended = true,
            cbcResult = "WBC: 21.5 G/L (Tăng rất cao), Neutrophil: 89% (Chuyển trái nặng), Hemoglobin: 110 g/L.",
            cbcCritical = true, cbcRecommended = true,
            lactateResult = "Lactate máu: 4.8 mmol/L (Tăng cao > 4 mmol/L) - Sốc nhiễm khuẩn (Septic Shock) thiếu máu mô nặng!",
            lactateCritical = true, lactateRecommended = true,
            pctResult = "Procalcitonin (PCT): 18.5 ng/ml (Nhiễm khuẩn huyết và nhiễm trùng ổ bụng nặng nề)."
        )

        val orders = listOf(
            EmergencyOrder(
                id = "p_anti_iv",
                category = OrderCategory.MEDICATION,
                name = "Kháng sinh phổ rộng tĩnh mạch trong 1 giờ đầu (Ceftriaxone 2g + Metronidazole 500mg truyền TM)",
                description = "Kiểm soát nhiễm khuẩn ổ bụng gram âm và kỵ khí khẩn cấp",
                costVnd = 220000L,
                suppliesUsed = listOf("Lọ Ceftriaxone 1g (x2)", "Lọ Metronidazole 500ml", "Dây truyền dịch tĩnh mạch"),
                isEssential = true,
                feedbackOnExecution = "Đã truyền kháng sinh phổ rộng ngay trong giờ đầu tiếp nhận."
            ),
            EmergencyOrder(
                id = "p_fluid_resus",
                category = OrderCategory.MEDICATION,
                name = "Hồi sức dịch tích cực Ringer Lactate 30 ml/kg trong 3 giờ đầu",
                description = "Chống sốc nhiễm khuẩn theo khuyến cáo Surviving Sepsis Campaign",
                costVnd = 150000L,
                suppliesUsed = listOf("Chai Ringer Lactate 1000ml (x2)", "Dây truyền dịch áp lực", "Kim luồn 18G"),
                isEssential = true,
                feedbackOnExecution = "Bù dịch tĩnh mạch tích cực, lượng nước tiểu bắt đầu tăng."
            ),
            EmergencyOrder(
                id = "p_ng_tube",
                category = OrderCategory.PROCEDURE,
                name = "Đặt sonde dạ dày hút dịch giải áp liên tục + Đặt sonde tiểu theo dõi lượng nước tiểu",
                description = "Giảm bớt thức ăn và dịch tiêu hóa tràn vào ổ phúc mạc",
                costVnd = 180000L,
                suppliesUsed = listOf("Sonde dạ dày Levin số 16", "Túi chứa dịch thải", "Gel bôi trơn Lidocaine", "Sonde tiểu Foley 2 nhánh", "Túi nước tiểu vô trùng"),
                isEssential = true,
                feedbackOnExecution = "Hút ra 500ml dịch dạ dày bẩn, bụng bớt căng chướng."
            ),
            EmergencyOrder(
                id = "p_surg_urgent",
                category = OrderCategory.CONSULTATION,
                name = "Báo động đỏ Ngoại khoa - Mời phẫu thuật cấp cứu khâu lỗ thủng và rửa ổ bụng",
                description = "Xử trí triệt căn nguồn nhiễm trùng (Source Control)",
                costVnd = 18500000L,
                suppliesUsed = listOf("Bộ dụng cụ phẫu thuật mở bụng", "Chỉ khâu PDS 3.0", "Hệ thống hút rửa áp lực cao", "Dẫn lưu ổ bụng"),
                isEssential = true,
                feedbackOnExecution = "Kíp phẫu thuật Ngoại tổng quát đã tiếp nhận, đưa bệnh nhân lên phòng mổ cấp cứu khâu lỗ thủng dạ dày 1cm thành công!"
            ),
            EmergencyOrder(
                id = "p_wrong_delay",
                category = OrderCategory.MEDICATION,
                name = "Cho bệnh nhân uống thuốc giảm đau Paracetamol và thuốc nhuận tràng để chờ chụp MRI",
                description = "Cho uống thuốc qua đường miệng khi thủng tạng rỗng (CHỐNG CHỈ ĐỊNH)",
                costVnd = 25000L,
                suppliesUsed = listOf("Cốc uống thuốc"),
                isEssential = false,
                isHarmful = true,
                feedbackOnExecution = "NGUY HIỂM! Cho uống thuốc qua miệng làm dịch và hóa chất tràn thêm vào ổ bụng, viêm phúc mạc hóa mủ toàn thể sốc nhiễm trùng tử vong!"
            )
        )

        return PatientCase(
            id = "case_peri_06",
            patientName = "Đặng Văn Lâm",
            age = 52,
            gender = "Nam",
            occupation = "Thợ xây",
            triageLevel = TriageLevel.RED,
            chiefComplaint = "Đau bụng dữ dội như dao đâm khởi phát đột ngột, bụng cứng như gỗ, sốt cao rét run, tụt huyết áp",
            arrivalTime = "18:00",
            initialVitals = VitalSigns(
                heartRate = 126,
                bpSys = 80,
                bpDia = 50,
                spo2 = 92,
                respRate = 28,
                temperature = 39.2f,
                gcs = 13,
                rhythm = EcgRhythm.NORMAL_SINUS
            ),
            abcde = AbcdeAssessment(
                airwayDesc = "Đường thở thông, nôn ra ít dịch nâu đen.",
                airwayIsClear = true,
                breathingDesc = "Thở nhanh nông 28 lần/phút, thở chủ yếu bằng lồng ngực vì cơ bụng co cứng.",
                breathingIsNormal = false,
                circulationDesc = "Mạch nhanh nhỏ 126 l/p, da nổi vân tím ở đầu gối, CRT > 4s, huyết áp tụt 80/50 mmHg.",
                circulationIsNormal = false,
                disabilityDesc = "GCS 13/15, li bì vẻ mặt nhiễm trùng nhiễm độc nặng.",
                disabilityIsNormal = false,
                exposureDesc = "Bụng co cứng toàn bộ như tấm ván gỗ (BỤNG GỖ).",
                exposureIsNormal = false
            ),
            historyQuestions = listOf(
                PatientHistoryQA("q1", "Bác đau bụng từ bao giờ và cảm giác thế nào?", "Trưa nay đang ăn cơm thì đột ngột bị đau thắt dữ dội ở vùng trên rốn như có ai lấy dao nhọn đâm thủng bụng, sau đó đau lan khắp cả bụng!", "Bệnh nhân"),
                PatientHistoryQA("q2", "Trước đây bác có hay đau dạ dày hay uống rượu bia không?", "Bác bị viêm loét dạ dày tá tràng mười năm nay, hay uống rượu trắng và thường xuyên tự mua thuốc giảm đau khớp uống!", "Vợ bệnh nhân")
            ),
            physicalExams = listOf(
                PhysicalExamSystemItem("sys_general", "Toàn thân", "Vẻ mặt nhiễm trùng nhiễm độc: Môi khô, lưỡi bẩn hôi, da tái lạnh, sốt cao 39.2°C.", true),
                PhysicalExamSystemItem("sys_gi", "Tiêu hóa", "DẤU HIỆU NGOẠI KHOA ĐIỂN HÌNH: Co cứng thành bụng toàn thể ('bụng cứng như gỗ'), Cảm ứng phúc mạc (Blumberg) dương tính, Gõ mất vùng đục trước gan.", true),
                PhysicalExamSystemItem("sys_cardio", "Tim mạch", "Nhịp tim nhanh 126 ck/p, mạch yếu, huyết áp 80/50 mmHg.", true),
                PhysicalExamSystemItem("sys_resp", "Hô hấp", "Thở nông 28 l/p, hai đáy phổi thông khí giảm do cơ hoành bị hạn chế di động.", true),
                PhysicalExamSystemItem("sys_neuro", "Thần kinh", "Tiếp xúc chậm chạp, lờ đờ do độc tố vi khuẩn.", true),
                PhysicalExamSystemItem("sys_skin", "Da & Chấn thương", "Da lạnh nổi vân tím, không vết thương xuyên thấu thành bụng.", true)
            ),
            availableLabs = labs,
            goldenDiagnosis = "Thủng ổ loét dạ dày - Viêm phúc mạc toàn thể - Sốc nhiễm khuẩn (Perforated Peptic Ulcer / Peritonitis / Septic Shock)",
            diagnosisKeywords = listOf("thung tang rong", "thung da day", "viem phuc mac", "soc nhiem khuan", "septic shock", "bung go", "liem hoi"),
            goldenDifferentials = listOf("Viêm tụy cấp thể hoại tử nặng", "Nhồi máu mạc treo ruột cấp", "Viêm ruột thừa vỡ mủ", "Nhồi máu cơ tim thành dưới"),
            goldenClinicalReasoning = "Đau bụng đột ngột dữ dội như dao đâm kèm hội chứng viêm phúc mạc: Co cứng thành bụng như gỗ, cảm ứng phúc mạc dương tính, liềm hơi dưới cơ hoành trên X-quang bụng. Sốc nhiễm khuẩn cần hồi sức dịch cấp tốc và chuyển mổ cấp cứu.",
            standardOrders = MasterHospitalOrders.getAllMasterOrders(),
            badDelayNarrative = "Do trì hoãn hồi sức và phẫu thuật, nhiễm trùng ổ bụng bùng phát dẫn tới sốc nhiễm khuẩn kháng trị, suy đa tạng tử vong!",
            badDelayVitals = VitalSigns(145, 55, 30, 85, 35, 39.8f, 7, EcgRhythm.NORMAL_SINUS),
            successfulRescueNarrative = "Nhờ hồi sức dịch tích cực, dùng kháng sinh phổ rộng trong giờ đầu và hội chẩn Ngoại mổ cấp cứu kịp thời, lỗ thủng dạ dày 1cm đã được khâu kín và rửa sạch ổ phúc mạc! Bệnh nhân qua khỏi cơn sốc nhiễm trùng!",
            successVitals = VitalSigns(88, 115, 75, 97, 18, 37.2f, 15, EcgRhythm.NORMAL_SINUS),
            clinicalPearls = "Quy tắc vàng Bộ Y Tế: Bụng cứng như gỗ + Mất vùng đục trước gan + Liềm hơi dưới hoành là bộ ba kinh điển của Thủng tạng rỗng. Tuyệt đối không được cho bệnh nhân ăn uống và phải phẫu thuật cấp cứu càng sớm càng tốt!",
            gratitudeSpeaker = "Bác Lâm & Gia đình",
            gratitudeMessage = "Bác sĩ ơi, cái đau đớn như dao đâm thấu ruột gan của tôi đã hết rồi! Tôi và gia đình mang ơn cứu mạng của các bác sĩ suốt đời!"
        )
    }

    // ==========================================
    // CASE 7: NGỘ ĐỘC CẤP THUỐC TRỪ SÂU PHOSPHO HỮU CƠ
    // ==========================================
    private fun createCase7Organophosphate(): PatientCase {
        val labs = createBaseLabList(
            cheResult = "Hoạt độ men Cholinesterase (ChE) trong máu: 820 U/L (Bình thường 4,000 - 12,000 U/L) - GIẢM NẶNG CÒN DƯỚI 20%, khẳng định ngộ độc Phospho hữu cơ mức độ nặng!",
            cheCritical = true, cheRecommended = true,
            abgResult = "pH: 7.20, PaO2: 54 mmHg, PaCO2: 58 mmHg (Suy hô hấp ứ trệ nặng do tăng tiết phế quản và co thắt cơ trơn).",
            abgCritical = true, abgRecommended = true,
            ecgResult = "Nhịp chậm xoang 38 lần/phút, khoảng QT kéo dài (QTc 510ms). Nguy cơ cao ngừng tim vô tâm thu!"
        )

        val orders = listOf(
            EmergencyOrder(
                id = "op_atropine",
                category = OrderCategory.MEDICATION,
                name = "ATROPINE SULFATE 2mg - 5mg TIÊM TĨNH MẠCH MỖI 5-10 PHÚT ĐẠT MỤC TIÊU ATROPIN HÓA",
                description = "Thuốc kháng Muscarinic đối kháng tác dụng độc chất Acetylcholine - Dùng đến khi hết rale ẩm phổi, miệng khô, đồng tử dãn!",
                costVnd = 45000L,
                suppliesUsed = listOf("Ống Atropine Sulfat 0.25mg (x12 ống)", "Bơm tiêm 10ml pha loãng", "Kim tiêm vô trùng"),
                isEssential = true,
                feedbackOnExecution = "ĐÃ TIÊM ATROPINE 3mg TIÊM TĨNH MẠCH! Phổi bớt đờm dãi, nhịp tim từ 38 nâng lên 78 ck/p, đồng tử dãn từ 1mm lên 3.5mm!"
            ),
            EmergencyOrder(
                id = "op_pralidoxime",
                category = OrderCategory.MEDICATION,
                name = "Pralidoxime (PAM) 1g - 2g pha truyền tĩnh mạch chậm trong 30 phút",
                description = "Thuốc giải độc đặc hiệu phục hồi men Cholinesterase",
                costVnd = 650000L,
                suppliesUsed = listOf("Lọ PAM 1g", "Chai Glucose 5% 100ml", "Dây truyền tĩnh mạch"),
                isEssential = true,
                feedbackOnExecution = "Đang truyền Pralidoxime tái hoạt hóa enzym Cholinesterase."
            ),
            EmergencyOrder(
                id = "op_gastric_lavage",
                category = OrderCategory.PROCEDURE,
                name = "Đặt nội khí quản bảo vệ đường thở + Rửa dạ dày với than hoạt (Activated Charcoal)",
                description = "Loại bỏ độc chất còn sót lại trong dạ dày",
                costVnd = 420000L,
                suppliesUsed = listOf("Ống Faucher rửa dạ dày cỡ lớn", "Bình nước cất rửa 5 lít", "Chai than hoạt tính 50g", "Túi chứa dịch thải"),
                isEssential = true,
                feedbackOnExecution = "Hút ra nhiều dịch dạ dày nồng nặc mùi hôi hóa chất tỏi/thuốc trừ sâu, đã bơm 50g Than hoạt."
            ),
            EmergencyOrder(
                id = "op_wrong_succinyl",
                category = OrderCategory.MEDICATION,
                name = "Tiêm Succinylcholine để dãn cơ đặt nội khí quản",
                description = "Thuốc dãn cơ chuyển hóa qua men Cholinesterase (TUYỆT ĐỐI CHỐNG CHỈ ĐỊNH)",
                costVnd = 90000L,
                suppliesUsed = listOf("Bơm tiêm 5ml"),
                isEssential = false,
                isHarmful = true,
                feedbackOnExecution = "THẢM HỌA! Do men Cholinesterase đã bị bất hoạt, Succinylcholine làm liệt cơ hô hấp kéo dài hàng ngày và ngừng tim vĩnh viễn!"
            )
        )

        return PatientCase(
            id = "case_organo_07",
            patientName = "Bùi Thị Mai",
            age = 44,
            gender = "Nữ",
            occupation = "Nông dân trồng chè",
            triageLevel = TriageLevel.RED,
            chiefComplaint = "Hôn mê, co giật, sùi bọt mép, nồng nặc mùi thuốc trừ sâu, đồng tử co nhỏ như đầu đinh ghim",
            arrivalTime = "19:15",
            initialVitals = VitalSigns(
                heartRate = 38,
                bpSys = 85,
                bpDia = 50,
                spo2 = 72,
                respRate = 12,
                temperature = 36.2f,
                gcs = 7,
                rhythm = EcgRhythm.NORMAL_SINUS
            ),
            abcde = AbcdeAssessment(
                airwayDesc = "TĂNG TIẾT ĐỜM DÃI TRẦM TRỌNG: Đờm bọt sùi ngập miệng và họng.",
                airwayIsClear = false,
                breathingDesc = "Thở chậm đứt quãng 12 lần/phút, phổi nghe rale ẩm rale ngáy lan tỏa 2 phế trường.",
                breathingIsNormal = false,
                circulationDesc = "Mạch chậm nặng nề 38 lần/phút, huyết áp tụt 85/50 mmHg.",
                circulationIsNormal = false,
                disabilityDesc = "HÔN MÊ GCS 7/15. Đồng tử 2 bên CO NHỎ NHƯ ĐẦU ĐINH GHIM (1mm), mất phản xạ ánh sáng.",
                disabilityIsNormal = false,
                exposureDesc = "Toàn thân ướt đẫm mồ hôi và dịch nôn nồng nặc mùi hôi tỏi/hóa chất hữu cơ, rung giật bó cơ khắp người.",
                exposureIsNormal = false
            ),
            historyQuestions = listOf(
                PatientHistoryQA("q1", "Người nhà phát hiện cô ấy như thế nào?", "Chiều nay mâu thuẫn gia đình, tôi đi làm về thấy nhà nồng nặc mùi thuốc trừ sâu Wofatox (Phospho hữu cơ), cô ấy nằm co giật sùi bọt mép trên sàn nhà bên cạnh lọ thuốc đã vơi nửa!", "Chồng bệnh nhân"),
                PatientHistoryQA("q2", "Uống lúc mấy giờ và có nôn ra được không?", "Chắc uống khoảng hơn 1 tiếng trước lúc tôi phát hiện, lúc tôi bế lên cô ấy nôn thốc tháo ra quần áo toàn mùi hóa chất!", "Chồng bệnh nhân")
            ),
            physicalExams = listOf(
                PhysicalExamSystemItem("sys_general", "Toàn thân", "Hôn mê sâu, vã mồ hôi ướt sũng quần áo, nồng nặc mùi hóa chất trừ sâu.", true),
                PhysicalExamSystemItem("sys_neuro", "Thần kinh", "HỘI CHỨNG MUSCARINIC & NICOTINIC: Đồng tử co nhỏ 1mm (Pinpoint pupils), giật rung bó cơ toàn thân (Fasciculation), hôn mê GCS 7.", true),
                PhysicalExamSystemItem("sys_resp", "Hô hấp", "Đờm dãi sùi bọt mép tràn ngập đường thở, phổi nghe rale ẩm rale ngáy cả 2 phế trường, SpO2 72%.", true),
                PhysicalExamSystemItem("sys_cardio", "Tim mạch", "Nhịp tim chậm nguy hiểm 38 lần/phút, mạch yếu, huyết áp 85/50 mmHg.", true),
                PhysicalExamSystemItem("sys_gi", "Tiêu hóa", "Tăng nhu động ruột liên tục, tiêu chảy, dịch nôn mùi hôi tỏi nồng nặc.", true),
                PhysicalExamSystemItem("sys_skin", "Da & Chấn thương", "Da lạnh ướt đẫm mồ hôi, dính hóa chất.", true)
            ),
            availableLabs = labs,
            goldenDiagnosis = "Ngộ độc cấp thuốc trừ sâu phospho hữu cơ mức độ nặng - Hội chứng Muscarinic và Nicotinic (Acute Organophosphate Poisoning)",
            diagnosisKeywords = listOf("phospho huu co", "thuoc tru sau", "ngoc doc", "organophosphate", "atropine", "pam", "cholinesterase"),
            goldenDifferentials = listOf("Ngộ độc thuốc trừ sâu Carbamate", "Ngộ độc nhóm Opiate", "Đột quỵ xuất huyết cầu não", "Ngộ độc thuốc phong bế Beta"),
            goldenClinicalReasoning = "Triệu chứng lâm sàng hội tụ đầy đủ 3 hội chứng kinh điển: Muscarinic (SLUDGE), Nicotinic (giật cơ), Thần kinh trung ương (hôn mê). Kết quả men Cholinesterase máu giảm nặng < 20% khẳng định ngộ độc Phospho hữu cơ. Phải dùng ngay ATROPINE liều cao tiêm tĩnh mạch đạt mục tiêu Atropin hóa, dùng thuốc giải độc đặc hiệu PAM và rửa dạ dày than hoạt.",
            standardOrders = MasterHospitalOrders.getAllMasterOrders(),
            badDelayNarrative = "Do không tiêm Atropine liều đủ cao hoặc chậm trễ bảo vệ đường thở, đờm dãi ngập đường thở gây ngạt cơ học và ngừng tim!",
            badDelayVitals = VitalSigns(0, 0, 0, 0, 0, 35.8f, 3, EcgRhythm.ASYSTOLE),
            successfulRescueNarrative = "Nhờ tiêm tĩnh mạch Atropine 3mg liên tục đạt tình trạng Atropin hóa kết hợp truyền giải độc PAM và rửa dạ dày, bệnh nhân đã hồi tỉnh qua khỏi cơn nguy kịch!",
            successVitals = VitalSigns(86, 120, 75, 98, 18, 36.8f, 14, EcgRhythm.NORMAL_SINUS),
            clinicalPearls = "Quy tắc vàng Bộ Y Tế: Thuốc giải độc quyết định sống còn của ngộ độc Phospho hữu cơ là ATROPINE! Phải tiêm TM nhanh đến khi đạt dấu hiệu Atropin hóa (phổi hết rale ẩm, miệng khô, đồng tử dãn >= 3-4mm, nhịp tim >= 80 bpm).",
            gratitudeSpeaker = "Chị Mai & Chồng",
            gratitudeMessage = "Bác sĩ ơi, trong cơn dại dột tôi đã uống thuốc trừ sâu... Cảm ơn các bác sĩ đã tận tình rửa ruột và tiêm thuốc giải độc kéo tôi từ cõi chết về với con cái!"
        )
    }

    // Helper to generate full 23 hospital lab tests with case customizations
    private fun createBaseLabList(
        ecgResult: String = "Nhịp xoang đều, tần số bình thường, không thấy ST chênh, không rối loạn dẫn truyền.",
        ecgCritical: Boolean = false, ecgRecommended: Boolean = false,
        tropResult: String = "hs-cTnI: 6.2 ng/L (Bình thường < 14 ng/L) - Âm tính.",
        tropCritical: Boolean = false, tropRecommended: Boolean = false,
        abgResult: String = "pH: 7.39, PaO2: 92 mmHg, PaCO2: 39 mmHg, HCO3-: 24 mmol/L, SaO2: 97%. Toan kiềm bình thường.",
        abgCritical: Boolean = false, abgRecommended: Boolean = false,
        lactateResult: String = "Lactate máu: 1.4 mmol/L (Tham chiếu: 0.5 - 2.0 mmol/L) - Bình thường.",
        lactateCritical: Boolean = false, lactateRecommended: Boolean = false,
        cbcResult: String = "Hồng cầu: 4.6 T/L, Hb: 138 g/L, Bạch cầu (WBC): 7.8 G/L, Tiểu cầu: 240 G/L - Bình thường.",
        cbcCritical: Boolean = false, cbcRecommended: Boolean = false,
        coagResult: String = "PT: 12.1s, INR: 1.02, aPTT: 30s, Fibrinogen: 3.5 g/L - Đông máu bình thường.",
        dDimerResult: String = "D-Dimer: 280 ng/ml (Tham chiếu < 500 ng/ml) - Âm tính, loại trừ thuyên tắc khối.",
        lytesResult: String = "Na+: 139 mmol/L, K+: 4.1 mmol/L, Cl-: 101 mmol/L, Ca2+: 1.18 mmol/L - Điện giải bình thường.",
        liverPancreasResult: String = "AST: 28 U/L, ALT: 32 U/L, Amylase: 65 U/L - Bình thường.",
        renalResult: String = "Ure: 5.4 mmol/L, Creatinine: 78 μmol/L, eGFR: 98 ml/min - Chức năng thận tốt.",
        bloodSugarResult: String = "Đường huyết mao mạch tại giường: 5.8 mmol/L (Bình thường).",
        cheResult: String = "Hoạt độ men Cholinesterase máu: 6,800 U/L (Tham chiếu: 4,000 - 12,000 U/L) - Bình thường.",
        cheCritical: Boolean = false, cheRecommended: Boolean = false,
        pctResult: String = "Procalcitonin (PCT): 0.08 ng/ml (Tham chiếu < 0.5 ng/ml) - Không có dấu hiệu nhiễm trùng toàn thân.",
        xrayChestResult: String = "Hai phế trường sáng đều, không thấy tổn thương nhu mô phổi, bóng tim không to.",
        xrayChestCritical: Boolean = false, xrayChestRecommended: Boolean = false,
        xrayAbdResult: String = "Các quai ruột không chướng hơi, không có mức nước - hơi, không thấy liềm hơi dưới cơ hoành.",
        xrayAbdCritical: Boolean = false, xrayAbdRecommended: Boolean = false,
        efastResult: String = "E-FAST: Không có dịch tự do khoang Morison, lách thận, Douglas. Không có dịch màng ngoài tim. Màng phổi trượt bình thường 2 bên.",
        efastCritical: Boolean = false, efastRecommended: Boolean = false,
        focusEchoResult: String = "FoCUS: Chức năng co bóp thất trái tốt, LVEF 60%, không rối loạn vận động vùng, không tràn dịch màng ngoài tim.",
        ctBrainResult: String = "CT sọ não không cản quang: Nhu mô não bình thường, không thấy xuất huyết nội sọ, không có tổn thương choán chỗ.",
        ctBrainCritical: Boolean = false, ctBrainRecommended: Boolean = false,
        ctChestResult: String = "CT ngực có cản quang: Động mạch chủ ngực kích thước bình thường, không bóc tách, các nhánh động mạch phổi ngấm thuốc đều không huyết khối.",
        ctAbdResult: String = "CT ổ bụng đa dãy: Gan lách tụy thận không tổn thương, không có ổ tụ dịch hay khí tự do phúc mạc.",
        bloodCultureResult: String = "Cấy máu 2 vị trí: Đang ủ ấm trong máy cấy tự động (kết quả sơ bộ sau 24h).",
        urinalysisResult: String = "Tổng phân tích nước tiểu 10 thông số: Bạch cầu (-), Hồng cầu (-), Protein (-), Glucose (-).",
        drugScreenResult: String = "Test nhanh ma túy nước tiểu 4 chất (Morphine, Amphetamine, THC, Ketamine): Âm tính."
    ): List<LabTestItem> {
        return listOf(
            LabTestItem("lab_ecg", "Điện tâm đồ 12 chuyển đạo tại giường (ECG)", "Thăm dò chức năng", 80000L, 2, "Nhịp xoang", if (ecgCritical) "BẤT THƯỜNG NGUY KỊCH" else "Bình thường", ecgResult, ecgCritical, ecgRecommended),
            LabTestItem("lab_trop", "Troponin I độ nhạy cao (hs-cTnI)", "Sinh hóa - Miễn dịch", 150000L, 15, "< 14 ng/L", if (tropCritical) "TĂNG RẤT CAO" else "Bình thường", tropResult, tropCritical, tropRecommended),
            LabTestItem("lab_abg", "Khí máu động mạch (ABG)", "Khí máu - Hồi sức", 120000L, 5, "pH 7.35-7.45", if (abgCritical) "RỐI LOẠN TOAN KIỀM NẶNG" else "Bình thường", abgResult, abgCritical, abgRecommended),
            LabTestItem("lab_lactate", "Lactate máu động mạch", "Khí máu - Hồi sức", 90000L, 10, "< 2.0 mmol/L", if (lactateCritical) "TĂNG CAO THIẾU MÁU MÔ" else "Bình thường", lactateResult, lactateCritical, lactateRecommended),
            LabTestItem("lab_cbc", "Tổng phân tích tế bào máu ngoại vi (CBC)", "Huyết học", 65000L, 10, "WBC 4-10 G/L", if (cbcCritical) "BẤT THƯỜNG" else "Bình thường", cbcResult, cbcCritical, cbcRecommended),
            LabTestItem("lab_coag", "Đông máu cơ bản (PT, aPTT, INR, Fibrinogen)", "Huyết học - Đông máu", 110000L, 15, "INR 0.8-1.2", "Bình thường", coagResult, false, false),
            LabTestItem("lab_ddimer", "D-Dimer định lượng", "Huyết học - Đông máu", 220000L, 15, "< 500 ng/ml", "Bình thường", dDimerResult, false, false),
            LabTestItem("lab_lytes", "Điện giải đồ (Na+, K+, Cl-, Ca2+)", "Sinh hóa", 75000L, 10, "K+ 3.5-5.0 mmol/L", "Bình thường", lytesResult, false, false),
            LabTestItem("lab_liver_panc", "Men gan (AST, ALT) & Men tụy (Amylase)", "Sinh hóa", 130000L, 15, "Men gan < 40 U/L", "Bình thường", liverPancreasResult, false, false),
            LabTestItem("lab_renal", "Ure, Creatinine máu (Chức năng thận)", "Sinh hóa", 60000L, 10, "Crea < 106 μmol/L", "Bình thường", renalResult, false, false),
            LabTestItem("lab_sugar", "Đường huyết mao mạch tại giường", "Sinh hóa cấp cứu", 30000L, 1, "4.0 - 7.0 mmol/L", "Bình thường", bloodSugarResult, false, true),
            LabTestItem("lab_che", "Hoạt độ men Cholinesterase máu (ChE)", "Độc chất", 140000L, 20, "4,000 - 12,000 U/L", if (cheCritical) "GIẢM NẶNG < 20%" else "Bình thường", cheResult, cheCritical, cheRecommended),
            LabTestItem("lab_pct", "Procalcitonin (PCT) định lượng", "Miễn dịch nhiễm khuẩn", 280000L, 20, "< 0.5 ng/ml", "Bình thường", pctResult, false, false),
            LabTestItem("lab_xray_chest", "X-quang ngực thẳng tại giường", "Chẩn đoán hình ảnh", 120000L, 5, "Phổi sáng đều", if (xrayChestCritical) "BẤT THƯỜNG NGUY HIỂM" else "Bình thường", xrayChestResult, xrayChestCritical, xrayChestRecommended),
            LabTestItem("lab_xray_abd", "X-quang bụng không chuẩn bị đứng", "Chẩn đoán hình ảnh", 120000L, 10, "Không liềm hơi", if (xrayAbdCritical) "LIỀM HƠI DƯỚI HOÀNH" else "Bình thường", xrayAbdResult, xrayAbdCritical, xrayAbdRecommended),
            LabTestItem("lab_efast", "Siêu âm cấp cứu tại giường E-FAST", "Thăm dò hình ảnh", 160000L, 5, "Không dịch tự do", if (efastCritical) "BẤT THƯỜNG" else "Bình thường", efastResult, efastCritical, efastRecommended),
            LabTestItem("lab_focus_echo", "Siêu âm tim tập trung cấp cứu (FoCUS)", "Thăm dò hình ảnh", 200000L, 7, "EF > 55%", "Đã khảo sát", focusEchoResult, false, false),
            LabTestItem("lab_ct_brain", "Chụp CT sọ não không cản quang", "Chẩn đoán hình ảnh", 850000L, 12, "Không xuất huyết", if (ctBrainCritical) "TỔN THƯƠNG THIẾU MÁU" else "Bình thường", ctBrainResult, ctBrainCritical, ctBrainRecommended),
            LabTestItem("lab_ct_chest", "Chụp CT ngực có cản quang (CT Angio)", "Chẩn đoán hình ảnh", 1250000L, 18, "Không bóc tách", "Đã dựng hình", ctChestResult, false, false),
            LabTestItem("lab_ct_abd", "Chụp CT ổ bụng đa dãy", "Chẩn đoán hình ảnh", 140000L, 20, "Không tổn thương", "Đã dựng hình", ctAbdResult, false, false),
            LabTestItem("lab_blood_cult", "Cấy máu 2 vị trí", "Vi sinh", 250000L, 60, "Đang cấy", "Đang xử lý trong máy cấy", bloodCultureResult, false, false),
            LabTestItem("lab_urinalysis", "Tổng phân tích nước tiểu 10 thông số", "Xét nghiệm nước tiểu", 45000L, 8, "Bình thường", "Bình thường", urinalysisResult, false, false),
            LabTestItem("lab_drug_screen", "Test nhanh ma túy nước tiểu 4 chất", "Độc chất cấp cứu", 90000L, 5, "Âm tính", "Âm tính", drugScreenResult, false, false)
        )
    }
}
