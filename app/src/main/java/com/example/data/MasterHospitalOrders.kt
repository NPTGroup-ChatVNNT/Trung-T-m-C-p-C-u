package com.example.data

import com.example.model.EmergencyOrder
import com.example.model.OrderCategory

object MasterHospitalOrders {

    fun getAllMasterOrders(): List<EmergencyOrder> {
        return listOf(
            // ==========================================
            // 1. ĐƯỜNG THỞ & HÔ HẤP (AIRWAY)
            // ==========================================
            EmergencyOrder(
                id = "o_o2_cannula",
                category = OrderCategory.AIRWAY,
                name = "Thở oxy qua cannula 2 - 4 lít/phút",
                description = "Cung cấp oxy nồng độ thấp đến trung bình cho bệnh nhân khó thở nhẹ - vừa",
                costVnd = 60000L,
                suppliesUsed = listOf("Cannula thở oxy 2 nhánh", "Dây oxy kết nối lưu lượng kế"),
                isEssential = false,
                feedbackOnExecution = "Đã gắn oxy cannula 3L/phút cho bệnh nhân, dòng khí thông thoáng."
            ),
            EmergencyOrder(
                id = "o_o2_mask",
                category = OrderCategory.AIRWAY,
                name = "Thở oxy qua mask có túi dự trữ 10 - 15 lít/phút",
                description = "Cung cấp FiO2 cao (60-90%) cho bệnh nhân suy hô hấp, sốc tụt SpO2",
                costVnd = 80000L,
                suppliesUsed = listOf("Mask thở có túi dự trữ (Non-rebreather)", "Dây oxy áp lực cao"),
                isEssential = false,
                feedbackOnExecution = "Đã áp mask oxy túi 12L/phút kín khít mặt, túi khí phồng đều theo nhịp thở."
            ),
            EmergencyOrder(
                id = "o_intubation",
                category = OrderCategory.AIRWAY,
                name = "Đặt nội khí quản & Thở máy xâm lấn cấp cứu",
                description = "Kiểm soát đường thở dứt điểm trong hôn mê GCS < 8 hoặc suy hô hấp kiệt cơ",
                costVnd = 650000L,
                suppliesUsed = listOf("Ống nội khí quản số 7.5", "Đèn soi thanh quản lưỡi cong Mac 3", "Bơm tiêm chèn cuff 10ml", "Dây cố định ống", "Máy thở xâm lấn"),
                isEssential = false,
                feedbackOnExecution = "Bác sĩ đặt NKQ thành công qua dây thanh âm, bóng chèn phồng tốt, kết nối máy thở êm."
            ),
            EmergencyOrder(
                id = "o_bipap",
                category = OrderCategory.AIRWAY,
                name = "Thở máy không xâm lấn (BiPAP / CPAP)",
                description = "Hỗ trợ thông khí áp lực dương cho phù phổi cấp hoặc đợt cấp COPD",
                costVnd = 380000L,
                suppliesUsed = listOf("Mặt nạ thở không xâm lấn cỡ vừa", "Dây máy thở kép"),
                isEssential = false,
                feedbackOnExecution = "Đã khởi động BiPAP IPAP 12, EPAP 5 cmH2O, bệnh nhân bớt co kéo cơ hô hấp."
            ),
            EmergencyOrder(
                id = "o_aerosol_broncho",
                category = OrderCategory.AIRWAY,
                name = "Khí dung Salbutamol 5mg + Ipratropium 0.5mg",
                description = "Thuốc dãn phế quản tác dụng nhanh qua máy khí dung khí oxy",
                costVnd = 650000L,
                suppliesUsed = listOf("Bầu khí dung kèm mask ngậm", "Ống Salbutamol 5mg", "Ống Ipratropium 0.5mg"),
                isEssential = false,
                feedbackOnExecution = "Bắt đầu phun khí dung với oxy 6L/phút, sương mịn bao phủ đường thở."
            ),
            EmergencyOrder(
                id = "o_suction_airway",
                category = OrderCategory.AIRWAY,
                name = "Hút đờm dãi, làm sạch và khai thông đường thở",
                description = "Hút sạch dịch đờm bọt sùi hoặc dị vật đọng khoang hầu họng",
                costVnd = 45000L,
                suppliesUsed = listOf("Ống hút đờm vô trùng cỡ 14F", "Găng tay vô trùng", "Máy hút áp lực âm"),
                isEssential = false,
                feedbackOnExecution = "Hút ra nhiều dịch tiết và đờm nhớt, đường thở trên thông thoáng trở lại."
            ),

            // ==========================================
            // 2. THUỐC HỒI SỨC & DỊCH TRUYỀN (MEDICATION)
            // ==========================================
            EmergencyOrder(
                id = "ad_epi_im",
                category = OrderCategory.MEDICATION,
                name = "Adrenaline (Epinephrine) 1mg/1ml tiêm bắp sâu 0.5mg",
                description = "Thuốc thiết yếu sống còn đầu tay trong sốc phản vệ và ngừng tuần hoàn",
                costVnd = 45000L,
                suppliesUsed = listOf("Ống tiêm Adrenaline 1mg/1ml", "Bơm tiêm 1ml kim 23G", "Bông cồn sát khuẩn"),
                isEssential = false,
                feedbackOnExecution = "Đã tiêm bắp sâu 0.5ml Adrenaline vào mặt trước ngoài đùi ngay lập tức!"
            ),
            EmergencyOrder(
                id = "o_iv_glucose",
                category = OrderCategory.MEDICATION,
                name = "Glucose 30% 50ml tiêm tĩnh mạch trực tiếp",
                description = "Cấp cứu hạ đường huyết cấp, nâng đường huyết ngay trong 2 phút",
                costVnd = 35000L,
                suppliesUsed = listOf("2 ống Glucose 30% 20ml + 1 ống 10ml", "Bơm tiêm 50ml", "Khóa 3 chạc"),
                isEssential = false,
                feedbackOnExecution = "Đã tiêm chậm tĩnh mạch 50ml Glucose 30%, đường huyết mao mạch bắt đầu tăng vọt."
            ),
            EmergencyOrder(
                id = "o_anti_htn_oral",
                category = OrderCategory.MEDICATION,
                name = "Amlodipine 5mg uống / Captopril 25mg ngậm dưới lưỡi",
                description = "Hạ áp từ từ và an toàn trong cơn tăng huyết áp khẩn cấp (Hypertensive Urgency)",
                costVnd = 40000L,
                suppliesUsed = listOf("Viên nén Amlodipine 5mg", "Cốc nước y tế"),
                isEssential = false,
                feedbackOnExecution = "Bệnh nhân đã uống thuốc hạ áp, theo dõi huyết áp mỗi 15 phút."
            ),
            EmergencyOrder(
                id = "o_nicardipine_iv",
                category = OrderCategory.MEDICATION,
                name = "Nicardipine 10mg/10ml pha truyền tĩnh mạch kiểm soát HA",
                description = "Thuốc hạ áp tĩnh mạch chuẩn độ chính xác theo huyết áp mục tiêu",
                costVnd = 180000L,
                suppliesUsed = listOf("Ống Nicardipine 10mg", "Bơm tiêm điện 50ml", "Dây truyền nối dài"),
                isEssential = false,
                feedbackOnExecution = "Bắt đầu truyền Nicardipine qua bơm tiêm điện với tốc độ 5 mg/h."
            ),
            EmergencyOrder(
                id = "o_dapt",
                category = OrderCategory.MEDICATION,
                name = "Kháng kết tập tiểu cầu kép (Aspirin 300mg + Ticagrelor 180mg uống nhai)",
                description = "Y lệnh chuẩn Bộ Y Tế trong hội chứng vành cấp / Nhồi máu cơ tim",
                costVnd = 45000L,
                suppliesUsed = listOf("Viên nén Aspirin 300mg", "Viên Ticagrelor 180mg", "Cốc đựng thuốc"),
                isEssential = false,
                feedbackOnExecution = "Đã cho bệnh nhân nhai nuốt ngay Aspirin và Ticagrelor."
            ),
            EmergencyOrder(
                id = "o_heparin",
                category = OrderCategory.MEDICATION,
                name = "Enoxaparin (Heparin TLPT thấp) 30mg tiêm TM bolus + 1mg/kg TDD",
                description = "Chống đông nền tảng khẩn cấp trong hội chứng vành cấp",
                costVnd = 185000L,
                suppliesUsed = listOf("Bơm tiêm đóng sẵn Enoxaparin 40mg/0.4ml", "Bông cồn"),
                isEssential = false,
                feedbackOnExecution = "Đã tiêm tĩnh mạch bolus và tiêm dưới da bụng đúng liều."
            ),
            EmergencyOrder(
                id = "o_atropine",
                category = OrderCategory.MEDICATION,
                name = "Atropine Sunfat 0.5mg tiêm tĩnh mạch",
                description = "Kháng cholinergic điều trị nhịp tim chậm và ngộ độc Phospho hữu cơ",
                costVnd = 35000L,
                suppliesUsed = listOf("Ống Atropine 0.5mg", "Bơm tiêm 1ml"),
                isEssential = false,
                feedbackOnExecution = "Đã tiêm tĩnh mạch Atropine, theo dõi nhịp tim và đồng tử."
            ),
            EmergencyOrder(
                id = "op_pam",
                category = OrderCategory.MEDICATION,
                name = "Pralidoxime (PAM) 1g pha 100ml NaCl 0.9% truyền tĩnh mạch",
                description = "Thuốc giải độc đặc hiệu phục hồi men Cholinesterase trong ngộ độc phospho hữu cơ",
                costVnd = 320000L,
                suppliesUsed = listOf("Lọ bột đông khô PAM 1g", "Chai NaCl 0.9% 100ml", "Dây truyền dịch"),
                isEssential = false,
                feedbackOnExecution = "Bắt đầu truyền tĩnh mạch PAM trong 30 phút."
            ),
            EmergencyOrder(
                id = "o_corticoid",
                category = OrderCategory.MEDICATION,
                name = "Methylprednisolone 40mg - 80mg tiêm tĩnh mạch",
                description = "Glucocorticoid chống viêm mạnh, dự phòng phản vệ pha 2 và cắt cơn hen",
                costVnd = 65000L,
                suppliesUsed = listOf("Lọ Methylprednisolone 40mg", "Nước cất pha tiêm", "Bơm tiêm 5ml"),
                isEssential = false,
                feedbackOnExecution = "Đã tiêm tĩnh mạch chậm Methylprednisolone."
            ),
            EmergencyOrder(
                id = "o_ppi",
                category = OrderCategory.MEDICATION,
                name = "Esomeprazole 80mg tiêm tĩnh mạch bolus",
                description = "Ức chế bơm proton liều cao trong xuất huyết tiêu hóa và loét dạ dày",
                costVnd = 110000L,
                suppliesUsed = listOf("2 lọ Esomeprazole 40mg", "Dung môi hoàn nguyên", "Bơm tiêm 10ml"),
                isEssential = false,
                feedbackOnExecution = "Đã tiêm bolus tĩnh mạch 80mg Esomeprazole, độ pH dạ dày được nâng cao."
            ),
            EmergencyOrder(
                id = "o_antispasmodic",
                category = OrderCategory.MEDICATION,
                name = "Drotaverine (No-Spa) 40mg tiêm bắp / tĩnh mạch",
                description = "Thuốc giãn cơ trơn giảm đau co thắt trong cơn đau quặn thận / đau bụng quặn",
                costVnd = 55000L,
                suppliesUsed = listOf("Ống Drotaverine 40mg/2ml", "Bơm tiêm 5ml"),
                isEssential = false,
                feedbackOnExecution = "Đã tiêm Drotaverine, cơ trơn niệu quản và tiêu hóa bắt đầu thư giãn."
            ),
            EmergencyOrder(
                id = "o_nsaid_pain",
                category = OrderCategory.MEDICATION,
                name = "Ketorolac 30mg tiêm bắp / tĩnh mạch",
                description = "Thuốc giảm đau chống viêm non-steroid cực mạnh cho cơn đau quặn thận",
                costVnd = 45000L,
                suppliesUsed = listOf("Ống Ketorolac 30mg/1ml", "Bơm tiêm 2ml"),
                isEssential = false,
                feedbackOnExecution = "Đã tiêm Ketorolac, ức chế prostaglandin giảm phù nề và áp lực đài bể thận."
            ),
            EmergencyOrder(
                id = "o_antiemetic",
                category = OrderCategory.MEDICATION,
                name = "Metoclopramide 10mg tiêm bắp / tĩnh mạch",
                description = "Thuốc chống nôn, tăng trương lực co bóp dạ dày ruột",
                costVnd = 35000L,
                suppliesUsed = listOf("Ống Metoclopramide 10mg/2ml", "Bơm tiêm 3ml"),
                isEssential = false,
                feedbackOnExecution = "Đã tiêm Metoclopramide, giảm nhanh cảm giác buồn nôn và nôn ói."
            ),
            EmergencyOrder(
                id = "o_vertigo_med",
                category = OrderCategory.MEDICATION,
                name = "Tanganil (Acetyl-leucine) 500mg/5ml tiêm tĩnh mạch",
                description = "Thuốc điều trị chóng mặt tiền đình cấp tính",
                costVnd = 65000L,
                suppliesUsed = listOf("2 ống Tanganil 500mg", "Bơm tiêm 10ml"),
                isEssential = false,
                feedbackOnExecution = "Đã tiêm tĩnh mạch chậm Tanganil, làm dịu các kích thích tiền đình trung ương."
            ),
            EmergencyOrder(
                id = "o_fluids_nacl",
                category = OrderCategory.MEDICATION,
                name = "Truyền dịch tinh thể NaCl 0.9% 500ml - 1000ml tĩnh mạch",
                description = "Bù dịch tuần hoàn khẩn cấp trong sốc, tụt huyết áp và mất nước",
                costVnd = 85000L,
                suppliesUsed = listOf("Chai NaCl 0.9% 500ml", "Dây truyền dịch có bầu đếm giọt", "Kim luồn 18G"),
                isEssential = false,
                feedbackOnExecution = "Bắt đầu xả dịch NaCl 0.9% tốc độ nhanh qua đường truyền lớn."
            ),
            EmergencyOrder(
                id = "o_antibiotic_broad",
                category = OrderCategory.MEDICATION,
                name = "Ceftriaxone 2g tiêm tĩnh mạch (Kháng sinh phổ rộng)",
                description = "Kháng sinh Cephalosporin thế hệ 3 điều trị nhiễm khuẩn huyết và nhiễm trùng",
                costVnd = 150000L,
                suppliesUsed = listOf("Lọ Ceftriaxone 2g", "Nước cất pha tiêm", "Bơm tiêm 10ml"),
                isEssential = false,
                feedbackOnExecution = "Đã tiêm kháng sinh Ceftriaxone tĩnh mạch sau khi đã cấy máu."
            ),
            EmergencyOrder(
                id = "o_morphine",
                category = OrderCategory.MEDICATION,
                name = "Morphin Sulfat 2mg - 5mg tiêm tĩnh mạch chậm",
                description = "Thuốc giảm đau opioid mạnh cho nhồi máu cơ tim hoặc đau dữ dội",
                costVnd = 50000L,
                suppliesUsed = listOf("Ống Morphin 10mg/1ml", "Bơm tiêm 10ml pha loãng", "Tủ thuốc gây nghiện"),
                isEssential = false,
                feedbackOnExecution = "Đã tiêm chậm 3mg Morphin tĩnh mạch, bệnh nhân bớt đau dữ dội."
            ),
            EmergencyOrder(
                id = "o_paracetamol_iv",
                category = OrderCategory.MEDICATION,
                name = "Paracetamol 1g truyền tĩnh mạch",
                description = "Hạ sốt và giảm đau mức độ nhẹ đến trung bình",
                costVnd = 60000L,
                suppliesUsed = listOf("Chai truyền Paracetamol 1000mg/100ml", "Dây truyền dịch"),
                isEssential = false,
                feedbackOnExecution = "Bắt đầu truyền Paracetamol trong 15 phút."
            ),
            EmergencyOrder(
                id = "o_harmful_bb",
                category = OrderCategory.MEDICATION,
                name = "Metoprolol (Thuốc chẹn Beta giao cảm) 5mg tiêm tĩnh mạch",
                description = "Thuốc ức chế thụ thể Beta giao cảm giảm nhịp tim",
                costVnd = 60000L,
                suppliesUsed = listOf("Ống Metoprolol 5mg", "Bơm tiêm 5ml"),
                isEssential = false,
                isHarmful = true,
                feedbackOnExecution = "CẢNH BÁO: Thuốc chẹn Beta gây co thắt phế quản ác tính trong hen và làm sụp đổ huyết động trong sốc!"
            ),
            EmergencyOrder(
                id = "o_harmful_sedative",
                category = OrderCategory.MEDICATION,
                name = "Diazepam 10mg tiêm tĩnh mạch (An thần gây ngủ)",
                description = "Thuốc an thần nhóm Benzodiazepine ức chế thần kinh trung ương",
                costVnd = 35000L,
                suppliesUsed = listOf("Ống Diazepam 10mg", "Bơm tiêm 3ml"),
                isEssential = false,
                isHarmful = true,
                feedbackOnExecution = "CẢNH BÁO: Thuốc an thần ức chế trung tâm hô hấp làm bệnh nhân ngừng thở hoàn toàn!"
            ),
            EmergencyOrder(
                id = "o_harmful_insulin",
                category = OrderCategory.MEDICATION,
                name = "Insulin Actrapid 10 UI tiêm tĩnh mạch",
                description = "Insulin tác dụng nhanh hạ đường huyết máu",
                costVnd = 85000L,
                suppliesUsed = listOf("Lọ Insulin Actrapid", "Bơm tiêm chuyên dụng 100 UI"),
                isEssential = false,
                isHarmful = true,
                feedbackOnExecution = "THẢM HỌA: Tiêm Insulin cho bệnh nhân đang hạ đường huyết làm tụt đường huyết về 0, hôn mê tử vong!"
            ),

            // ==========================================
            // 3. THỦ THUẬT & CAN THIỆP (PROCEDURE)
            // ==========================================
            EmergencyOrder(
                id = "p_decomp",
                category = OrderCategory.PROCEDURE,
                name = "Chọc hút giải áp màng phổi bằng kim lớn (Kim 14G)",
                description = "Cấp cứu tức thì tràn khí màng phổi áp lực tại khoang liên sườn 2 đường trung đòn",
                costVnd = 180000L,
                suppliesUsed = listOf("Kim luồn 14G cỡ lớn", "Bơm tiêm 50ml", "Khóa 3 chạc", "Khay vô trùng"),
                isEssential = false,
                feedbackOnExecution = "Cắm kim vào KLS 2, tiếng khí xì mạnh ra ngoài như mở van lốp xe! Áp lực màng phổi được giải tỏa ngay!"
            ),
            EmergencyOrder(
                id = "p_chest_tube",
                category = OrderCategory.PROCEDURE,
                name = "Đặt dẫn lưu màng phổi kín (Chest tube) KLS 5",
                description = "Dẫn lưu khí/dịch liên tục với hệ thống bình hút kín một chiều",
                costVnd = 580000L,
                suppliesUsed = listOf("Ống dẫn lưu ngực 28F", "Bộ dụng cụ phẫu thuật tiểu phẫu", "Bình hút kín 1 bình", "Thuốc tê Lidocain 2%"),
                isEssential = false,
                feedbackOnExecution = "Đã đặt xong ống dẫn lưu ngực, bình dẫn lưu sủi bọt khí liên tục theo nhịp thở."
            ),
            EmergencyOrder(
                id = "op_gastric_lavage",
                category = OrderCategory.PROCEDURE,
                name = "Đặt sonde Faucher rửa dạ dày cấp cứu & bơm than hoạt",
                description = "Loại bỏ độc chất còn tồn đọng trong lòng dạ dày",
                costVnd = 420000L,
                suppliesUsed = listOf("Ống Faucher rửa dạ dày cỡ lớn", "Bình nước cất rửa 5 lít", "Chai than hoạt tính 50g", "Túi chứa dịch thải"),
                isEssential = false,
                feedbackOnExecution = "Hút ra nhiều dịch dạ dày nồng nặc mùi hôi hóa chất, đã bơm than hoạt tính hấp phụ độc chất."
            ),
            EmergencyOrder(
                id = "p_ng_tube",
                category = OrderCategory.PROCEDURE,
                name = "Đặt ống thông dạ dày (Sonde dạ dày) hút dịch ngắt quãng",
                description = "Giải áp dạ dày trong thủng tạng rỗng, tắc ruột và kiểm tra xuất huyết",
                costVnd = 95000L,
                suppliesUsed = listOf("Ống sonde dạ dày số 16", "Gel bôi trơn K-Y", "Túi đựng dịch dẫn lưu"),
                isEssential = false,
                feedbackOnExecution = "Đặt sonde dạ dày thành công, gắn túi dẫn lưu để theo dõi màu sắc và lượng dịch."
            ),
            EmergencyOrder(
                id = "p_foley",
                category = OrderCategory.PROCEDURE,
                name = "Đặt ống thông tiểu Foley theo dõi lượng nước tiểu / giờ",
                description = "Đánh giá tưới máu thận và cân bằng xuất nhập dịch trong hồi sức",
                costVnd = 85000L,
                suppliesUsed = listOf("Ống thông tiểu Foley 2 nhánh 16F", "Túi nước tiểu có vạch chia ml", "Gel tê Lidocain", "Găng tay vô trùng"),
                isEssential = false,
                feedbackOnExecution = "Đặt sonde tiểu thành công, nước tiểu chảy vào túi dẫn lưu trong suốt."
            ),
            EmergencyOrder(
                id = "p_iv_access",
                category = OrderCategory.PROCEDURE,
                name = "Lập 2 đường truyền tĩnh mạch ngoại vi kim lớn 18G",
                description = "Đảm bảo đường truyền kích thước lớn để xả dịch và truyền máu cấp cứu",
                costVnd = 50000L,
                suppliesUsed = listOf("2 kim luồn ngoại vi 18G màu xanh lá", "Gạc vô trùng", "Dây garo"),
                isEssential = false,
                feedbackOnExecution = "Đã lấy 2 ven kim 18G to nảy ở 2 cẳng tay, xả dịch chảy thành dòng."
            ),
            EmergencyOrder(
                id = "p_epley",
                category = OrderCategory.PROCEDURE,
                name = "Nghiệm pháp tái định vị sỏi tai Epley",
                description = "Điều trị dứt điểm chóng mặt kịch phát tư thế lành tính (BPPV) ống bán khuyên sau",
                costVnd = 120000L,
                suppliesUsed = listOf("Gối đệm đầu giường cấp cứu"),
                isEssential = false,
                feedbackOnExecution = "Bác sĩ xoay đầu bệnh nhân qua các góc 45 và 90 độ, sỏi tai trở về xoang nang an toàn."
            ),

            // ==========================================
            // 4. HỘI CHẨN & CHUYỂN KHOA (CONSULTATION)
            // ==========================================
            EmergencyOrder(
                id = "c_cathlab",
                category = OrderCategory.CONSULTATION,
                name = "Kích hoạt phòng Can thiệp Mạch vành khẩn cấp (Cathlab STEMI)",
                description = "Báo động đỏ tim mạch đưa bệnh nhân đi chụp và can thiệp nong đặt stent mạch vành",
                costVnd = 500000L,
                suppliesUsed = listOf("Hồ sơ bệnh án chuyển viện/chuyển khoa cấp cứu", "Bộ dụng cụ vận chuyển tim mạch có monitor mang theo"),
                isEssential = false,
                feedbackOnExecution = "Kíp Cathlab can thiệp mạch vành đã sẵn sàng tiếp nhận bệnh nhân ngay tại bàn can thiệp!"
            ),
            EmergencyOrder(
                id = "c_code_stroke",
                category = OrderCategory.CONSULTATION,
                name = "Kích hoạt Code Stroke - Đơn vị Đột quỵ tiêu sợi huyết rTPA",
                description = "Kích hoạt quy trình đột quỵ cấp, chuẩn bị thuốc Alteplase tiêu sợi huyết giờ vàng",
                costVnd = 600000L,
                suppliesUsed = listOf("Bảng điểm NIHSS", "Thuốc Alteplase (rTPA) 50mg"),
                isEssential = false,
                feedbackOnExecution = "Bác sĩ chuyên khoa thần kinh đột quỵ có mặt tại giường, chuẩn bị tính liều rTPA!"
            ),
            EmergencyOrder(
                id = "c_surgery_er",
                category = OrderCategory.CONSULTATION,
                name = "Hội chẩn Ngoại Tổng quát mổ cấp cứu (Thủng tạng, ruột thừa)",
                description = "Báo động kíp phẫu thuật chuẩn bị phòng mổ cấp cứu",
                costVnd = 300000L,
                suppliesUsed = listOf("Biên bản hội chẩn cấp cứu", "Phiếu cam kết phẫu thuật"),
                isEssential = false,
                feedbackOnExecution = "Phẫu thuật viên trưởng kíp trực ngoại đã khám và ký duyệt chuyển mổ cấp cứu!"
            ),
            EmergencyOrder(
                id = "c_urology",
                category = OrderCategory.CONSULTATION,
                name = "Hội chẩn chuyên khoa Ngoại Tiết niệu tán sỏi",
                description = "Đánh giá can thiệp tán sỏi niệu quản ngược dòng (URSL) hoặc tán sỏi ngoài cơ thể",
                costVnd = 250000L,
                suppliesUsed = listOf("Hồ sơ hội chẩn chuyên khoa"),
                isEssential = false,
                feedbackOnExecution = "Bác sĩ Ngoại Tiết niệu đã hội chẩn, lên lịch tán sỏi nội soi sau khi kiểm soát đau."
            ),
            EmergencyOrder(
                id = "c_poison_icu",
                category = OrderCategory.CONSULTATION,
                name = "Hội chẩn Trung tâm Chống độc / Hồi sức tích cực (ICU)",
                description = "Chuyển khoa Hồi sức tích cực theo dõi lọc máu và giải độc chuyên sâu",
                costVnd = 300000L,
                suppliesUsed = listOf("Phiếu bàn giao người bệnh nặng ICU"),
                isEssential = false,
                feedbackOnExecution = "Khoa Hồi sức tích cực (ICU) đã chuẩn bị giường có máy thở và máy lọc máu."
            ),
            EmergencyOrder(
                id = "c_internal_ward",
                category = OrderCategory.CONSULTATION,
                name = "Chuyển khoa Nội tổng hợp theo dõi và điều trị tiếp",
                description = "Bệnh nhân đã ổn định sinh hiệu sau cấp cứu, chuyển khoa nội theo dõi",
                costVnd = 150000L,
                suppliesUsed = listOf("Hồ sơ bệnh án chuyển khoa"),
                isEssential = false,
                feedbackOnExecution = "Đã hoàn thành thủ tục chuyển bệnh nhân lên khoa Nội tổng hợp."
            ),
            EmergencyOrder(
                id = "c_outpatient",
                category = OrderCategory.CONSULTATION,
                name = "Kê đơn thuốc ngoại trú & Cho bệnh nhân xuất viện hẹn tái khám",
                description = "Bệnh nhân phục hồi hoàn toàn, sinh hiệu tốt, hướng dẫn dùng thuốc tại nhà",
                costVnd = 100000L,
                suppliesUsed = listOf("Đơn thuốc điện tử", "Sổ khám bệnh"),
                isEssential = false,
                feedbackOnExecution = "Bệnh nhân và gia đình vui mừng nhận đơn thuốc xuất viện, dặn dò tái khám chu đáo."
            )
        )
    }
}
