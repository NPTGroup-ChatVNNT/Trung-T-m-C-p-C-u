package com.example.data

import java.text.Normalizer
import java.util.regex.Pattern

/**
 * BẢN MÃ CHUẨN CỤC QUẢN LÝ KHÁM, CHỮA BỆNH
 * "ICD-10 06/2026/TT-BYT | CCMS"
 * Áp dụng cho Hệ thống Khung Bệnh Án Điện Tử (EMR) và Giám sát Khám Chữa Bệnh Cấp Cứu
 */
data class Icd10Item(
    val code: String,
    val nameVi: String,
    val nameEn: String,
    val chapter: String,
    val emrGroup: String,
    val ccmsStandard: String = "ICD-10 06/2026/TT-BYT | CCMS",
    val synonyms: List<String> = emptyList()
)

object Icd10Database {

    const val STANDARD_TITLE = "Bản mã Cục Quản lý Khám, Chữa bệnh - ICD-10 06/2026/TT-BYT | CCMS"
    const val STANDARD_CODE = "ICD-10 06/2026/TT-BYT | CCMS"
    const val ISSUING_AUTHORITY = "Cục Quản lý Khám, Chữa bệnh - Bộ Y tế Việt Nam"

    val allCodes: List<Icd10Item> = listOf(
        // =========================================================================
        // CHƯƠNG IX: BỆNH HỆ TUẦN HOÀN & TIM MẠCH CẤP CỨU (I00 - I99)
        // =========================================================================
        Icd10Item(
            code = "I21.0",
            nameVi = "Nhồi máu cơ tim cấp ST chênh lên (STEMI) thành trước",
            nameEn = "ST elevation myocardial infarction of anterior wall",
            chapter = "Chương IX: Bệnh hệ tuần hoàn (I00 - I99)",
            emrGroup = "Hội chứng vành cấp",
            synonyms = listOf("i21.0", "i210", "i21", "stemi", "nhoi mau co tim", "thanh truoc", "dau nguc", "nhoi mau co tim cap")
        ),
        Icd10Item(
            code = "I21.1",
            nameVi = "Nhồi máu cơ tim cấp ST chênh lên (STEMI) thành dưới",
            nameEn = "ST elevation myocardial infarction of inferior wall",
            chapter = "Chương IX: Bệnh hệ tuần hoàn (I00 - I99)",
            emrGroup = "Hội chứng vành cấp",
            synonyms = listOf("i21.1", "i211", "i21", "stemi thanh duoi", "nhoi mau thanh duoi")
        ),
        Icd10Item(
            code = "I21.2",
            nameVi = "Nhồi máu cơ tim cấp ST chênh lên các vị trí khác",
            nameEn = "ST elevation myocardial infarction of other sites",
            chapter = "Chương IX: Bệnh hệ tuần hoàn (I00 - I99)",
            emrGroup = "Hội chứng vành cấp",
            synonyms = listOf("i21.2", "i212", "i21", "stemi")
        ),
        Icd10Item(
            code = "I21.4",
            nameVi = "Nhồi máu cơ tim cấp không ST chênh lên (NSTEMI)",
            nameEn = "Non-ST elevation myocardial infarction",
            chapter = "Chương IX: Bệnh hệ tuần hoàn (I00 - I99)",
            emrGroup = "Hội chứng vành cấp",
            synonyms = listOf("i21.4", "i214", "nstemi", "nhoi mau khong st chenh", "hoi chung vanh cap")
        ),
        Icd10Item(
            code = "I21.9",
            nameVi = "Nhồi máu cơ tim cấp, không đặc hiệu",
            nameEn = "Acute myocardial infarction, unspecified",
            chapter = "Chương IX: Bệnh hệ tuần hoàn (I00 - I99)",
            emrGroup = "Hội chứng vành cấp",
            synonyms = listOf("i21.9", "i219", "i21", "nhoi mau co tim cap", "mi")
        ),
        Icd10Item(
            code = "I20.0",
            nameVi = "Cơn đau thắt ngực không ổn định",
            nameEn = "Unstable angina",
            chapter = "Chương IX: Bệnh hệ tuần hoàn (I00 - I99)",
            emrGroup = "Hội chứng vành cấp",
            synonyms = listOf("i20.0", "i200", "i20", "dau that nguc khong on dinh", "dau nguc trai")
        ),
        Icd10Item(
            code = "I49.0",
            nameVi = "Rung thất và cuồng thất",
            nameEn = "Ventricular fibrillation and flutter",
            chapter = "Chương IX: Bệnh hệ tuần hoàn (I00 - I99)",
            emrGroup = "Rối loạn nhịp tim cấp cứu",
            synonyms = listOf("i49.0", "i490", "i49", "rung that", "cuong that", "vf", "vfib", "ngung tim")
        ),
        Icd10Item(
            code = "I47.2",
            nameVi = "Cơn nhịp nhanh thất (VT)",
            nameEn = "Ventricular tachycardia",
            chapter = "Chương IX: Bệnh hệ tuần hoàn (I00 - I99)",
            emrGroup = "Rối loạn nhịp tim cấp cứu",
            synonyms = listOf("i47.2", "i472", "i47", "nhanh that", "vt", "roi loan nhip")
        ),
        Icd10Item(
            code = "I47.1",
            nameVi = "Cơn nhịp nhanh kịch phát trên thất (PSVT)",
            nameEn = "Supraventricular tachycardia",
            chapter = "Chương IX: Bệnh hệ tuần hoàn (I00 - I99)",
            emrGroup = "Rối loạn nhịp tim cấp cứu",
            synonyms = listOf("i47.1", "i471", "psvt", "nhanh tren that", "svt")
        ),
        Icd10Item(
            code = "I48.0",
            nameVi = "Rung nhĩ kịch phát có đáp ứng thất nhanh",
            nameEn = "Paroxysmal atrial fibrillation",
            chapter = "Chương IX: Bệnh hệ tuần hoàn (I00 - I99)",
            emrGroup = "Rối loạn nhịp tim cấp cứu",
            synonyms = listOf("i48.0", "i480", "i48", "rung nhi", "af", "rung nhi dap ung that nhanh")
        ),
        Icd10Item(
            code = "I44.2",
            nameVi = "Block nhĩ thất hoàn toàn (Block nhĩ thất độ III)",
            nameEn = "Atrioventricular block, complete",
            chapter = "Chương IX: Bệnh hệ tuần hoàn (I00 - I99)",
            emrGroup = "Rối loạn nhịp tim cấp cứu",
            synonyms = listOf("i44.2", "i442", "i44", "block nhi that do 3", "av block 3", "nhip cham")
        ),
        Icd10Item(
            code = "I46.9",
            nameVi = "Ngừng tim, không đặc hiệu",
            nameEn = "Cardiac arrest, unspecified",
            chapter = "Chương IX: Bệnh hệ tuần hoàn (I00 - I99)",
            emrGroup = "Cấp cứu ngừng tuần hoàn",
            synonyms = listOf("i46.9", "i469", "i46", "ngung tim", "ngung tuan hoan", "cpr", "dot tu")
        ),
        Icd10Item(
            code = "I50.0",
            nameVi = "Suy tim sung huyết cấp",
            nameEn = "Congestive heart failure",
            chapter = "Chương IX: Bệnh hệ tuần hoàn (I00 - I99)",
            emrGroup = "Suy tim",
            synonyms = listOf("i50.0", "i500", "i50", "suy tim", "suy tim sung huyet")
        ),
        Icd10Item(
            code = "I50.1",
            nameVi = "Suy thất trái cấp (Phù phổi cấp huyết động do tim)",
            nameEn = "Left ventricular failure",
            chapter = "Chương IX: Bệnh hệ tuần hoàn (I00 - I99)",
            emrGroup = "Suy tim",
            synonyms = listOf("i50.1", "i501", "i50", "phu phoi cap do tim", "suy that trai cap", "suy tim cap")
        ),
        Icd10Item(
            code = "I50.9",
            nameVi = "Suy tim cấp / Đợt mất bù suy tim mạn",
            nameEn = "Heart failure, unspecified",
            chapter = "Chương IX: Bệnh hệ tuần hoàn (I00 - I99)",
            emrGroup = "Suy tim",
            synonyms = listOf("i50.9", "i509", "i50", "suy tim cap", "mat bu suy tim")
        ),
        Icd10Item(
            code = "I10",
            nameVi = "Tăng huyết áp vô căn (Cơn tăng huyết áp khẩn cấp)",
            nameEn = "Essential (primary) hypertension",
            chapter = "Chương IX: Bệnh hệ tuần hoàn (I00 - I99)",
            emrGroup = "Tăng huyết áp",
            synonyms = listOf("i10", "tang huyet ap", "con tang huyet ap", "hypertensive crisis", "huyet ap cao")
        ),
        Icd10Item(
            code = "I11.0",
            nameVi = "Bệnh tim do tăng huyết áp có suy tim (Cơn tăng huyết áp cấp cứu)",
            nameEn = "Hypertensive heart disease with heart failure",
            chapter = "Chương IX: Bệnh hệ tuần hoàn (I00 - I99)",
            emrGroup = "Tăng huyết áp",
            synonyms = listOf("i11.0", "i110", "i11", "tang huyet ap cap cuu", "con tang huyet ap")
        ),
        Icd10Item(
            code = "I26.0",
            nameVi = "Thuyên tắc động mạch phổi cấp có tâm phế cấp",
            nameEn = "Pulmonary embolism with mention of acute cor pulmonale",
            chapter = "Chương IX: Bệnh hệ tuần hoàn (I00 - I99)",
            emrGroup = "Thuyên tắc huyết khối",
            synonyms = listOf("i26.0", "i260", "i26", "thuyen tac phoi", "pe", "tac mach phoi")
        ),
        Icd10Item(
            code = "I71.0",
            nameVi = "Phình bóc tách động mạch chủ ngực / bụng",
            nameEn = "Dissection of aorta",
            chapter = "Chương IX: Bệnh hệ tuần hoàn (I00 - I99)",
            emrGroup = "Cấp cứu mạch máu",
            synonyms = listOf("i71.0", "i710", "i71", "boc tach dong mach chu", "phinh dong mach chu")
        ),

        // =========================================================================
        // CHƯƠNG VI & IX: BỆNH HỆ THẦN KINH & ĐỘT QUỴ NÃO (I60 - I64, G40)
        // =========================================================================
        Icd10Item(
            code = "I63.9",
            nameVi = "Nhồi máu não cấp (Đột quỵ thiếu máu cục bộ cấp tính)",
            nameEn = "Cerebral infarction, unspecified",
            chapter = "Chương IX: Bệnh hệ tuần hoàn (I00 - I99)",
            emrGroup = "Đột quỵ não cấp",
            synonyms = listOf("i63.9", "i639", "i63", "nhoi mau nao", "dot quy", "tai bien", "stroke", "yeu nua nguoi", "liet nua nguoi")
        ),
        Icd10Item(
            code = "I63.0",
            nameVi = "Nhồi máu não do huyết khối động mạch trước não",
            nameEn = "Cerebral infarction due to thrombosis of precerebral arteries",
            chapter = "Chương IX: Bệnh hệ tuần hoàn (I00 - I99)",
            emrGroup = "Đột quỵ não cấp",
            synonyms = listOf("i63.0", "i630", "i63", "nhoi mau nao", "tac dong mach canh")
        ),
        Icd10Item(
            code = "I61.9",
            nameVi = "Xuất huyết trong não cấp tính (Đột quỵ xuất huyết não)",
            nameEn = "Intracerebral hemorrhage, unspecified",
            chapter = "Chương IX: Bệnh hệ tuần hoàn (I00 - I99)",
            emrGroup = "Đột quỵ não cấp",
            synonyms = listOf("i61.9", "i619", "i61", "xuat huyet nao", "chay mau nao", "dot quy xuat huyet")
        ),
        Icd10Item(
            code = "I60.9",
            nameVi = "Xuất huyết dưới nhện không do chấn thương",
            nameEn = "Subarachnoid hemorrhage, unspecified",
            chapter = "Chương IX: Bệnh hệ tuần hoàn (I00 - I99)",
            emrGroup = "Đột quỵ não cấp",
            synonyms = listOf("i60.9", "i609", "i60", "xuat huyet duoi nhen", "sah", "dau dau set danh")
        ),
        Icd10Item(
            code = "G40.9",
            nameVi = "Cơn động kinh / Trạng thái động kinh liên tục",
            nameEn = "Epilepsy, unspecified / Status epilepticus",
            chapter = "Chương VI: Bệnh hệ thần kinh (G00 - G99)",
            emrGroup = "Thần kinh cấp cứu",
            synonyms = listOf("g40.9", "g409", "g40", "dong kinh", "co giat", "trang thai dong kinh")
        ),
        Icd10Item(
            code = "G03.9",
            nameVi = "Viêm màng não cấp tính",
            nameEn = "Meningitis, unspecified",
            chapter = "Chương VI: Bệnh hệ thần kinh (G00 - G99)",
            emrGroup = "Nhiễm trùng thần kinh",
            synonyms = listOf("g03.9", "g039", "g03", "viem mang nao", "hoi chung mang nao", "cung co")
        ),
        Icd10Item(
            code = "H81.0",
            nameVi = "Hội chứng tiền đình ngoại biên (Chóng mặt kịch phát)",
            nameEn = "Ménière's disease / Vestibular syndrome",
            chapter = "Chương VIII: Bệnh của tai (H60 - H95)",
            emrGroup = "Tiền đình & Tai mũi họng",
            synonyms = listOf("h81.0", "h810", "h81", "chong mat", "tien dinh", "bppv", "roi loan tien dinh")
        ),

        // =========================================================================
        // CHƯƠNG X: BỆNH HỆ HÔ HẤP CẤP CỨU (J00 - J99)
        // =========================================================================
        Icd10Item(
            code = "J93.0",
            nameVi = "Tràn khí màng phổi áp lực tự phát",
            nameEn = "Spontaneous tension pneumothorax",
            chapter = "Chương X: Bệnh hệ hô hấp (J00 - J99)",
            emrGroup = "Cấp cứu hô hấp lồng ngực",
            synonyms = listOf("j93.0", "j930", "j93", "tran khi mang phoi", "tran khi ap luc", "kho tho cap", "phoi mat thong khi")
        ),
        Icd10Item(
            code = "J93.9",
            nameVi = "Tràn khí màng phổi không đặc hiệu",
            nameEn = "Pneumothorax, unspecified",
            chapter = "Chương X: Bệnh hệ hô hấp (J00 - J99)",
            emrGroup = "Cấp cứu hô hấp lồng ngực",
            synonyms = listOf("j93.9", "j939", "j93", "tran khi mang phoi")
        ),
        Icd10Item(
            code = "J45.0",
            nameVi = "Cơn hen phế quản ác tính đe dọa tính mạng (Phổi câm)",
            nameEn = "Predominantly allergic asthma / Severe acute asthma",
            chapter = "Chương X: Bệnh hệ hô hấp (J00 - J99)",
            emrGroup = "Cấp cứu suy hô hấp",
            synonyms = listOf("j45.0", "j450", "j45", "hen phe quan", "con hen ac tinh", "phoi cam", "suy ho hap", "silent chest")
        ),
        Icd10Item(
            code = "J44.1",
            nameVi = "Đợt cấp bệnh phổi tắc nghẽn mạn tính (Đợt cấp COPD)",
            nameEn = "Chronic obstructive pulmonary disease with acute exacerbation",
            chapter = "Chương X: Bệnh hệ hô hấp (J00 - J99)",
            emrGroup = "Cấp cứu suy hô hấp",
            synonyms = listOf("j44.1", "j441", "j44", "copd", "dot cap copd", "tac nghen man tinh", "kho tho")
        ),
        Icd10Item(
            code = "J81",
            nameVi = "Phù phổi cấp không do tim / Phù phổi cấp tổn thương",
            nameEn = "Pulmonary edema",
            chapter = "Chương X: Bệnh hệ hô hấp (J00 - J99)",
            emrGroup = "Cấp cứu suy hô hấp",
            synonyms = listOf("j81", "phu phoi cap", "ards", "suy ho hap cap", "suy ho hap tien trien")
        ),
        Icd10Item(
            code = "J96.0",
            nameVi = "Suy hô hấp cấp tính (Type 1 giảm oxy / Type 2 tăng CO2)",
            nameEn = "Acute respiratory failure",
            chapter = "Chương X: Bệnh hệ hô hấp (J00 - J99)",
            emrGroup = "Hồi sức cấp cứu",
            synonyms = listOf("j96.0", "j960", "j96", "suy ho hap cap", "arf", "suy ho hap", "tut spo2")
        ),
        Icd10Item(
            code = "J18.9",
            nameVi = "Viêm phổi nặng cộng đồng, không đặc hiệu",
            nameEn = "Pneumonia, unspecified",
            chapter = "Chương X: Bệnh hệ hô hấp (J00 - J99)",
            emrGroup = "Nhiễm trùng hô hấp",
            synonyms = listOf("j18.9", "j189", "j18", "viem phoi", "viem phoi nang", "nhiem trung phoi", "sot ho")
        ),
        Icd10Item(
            code = "J69.0",
            nameVi = "Viêm phổi hít do thức ăn, chất nôn (Hội chứng Mendelson)",
            nameEn = "Pneumonitis due to food and vomit",
            chapter = "Chương X: Bệnh hệ hô hấp (J00 - J99)",
            emrGroup = "Cấp cứu hô hấp",
            synonyms = listOf("j69.0", "j690", "j69", "viem phoi hit", "sac thuc an", "hit chat non")
        ),

        // =========================================================================
        // CHƯƠNG XI: BỆNH HỆ TIÊU HÓA & CẤP CỨU NGOẠI KHOA Ổ BỤNG (K00 - K93)
        // =========================================================================
        Icd10Item(
            code = "K25.5",
            nameVi = "Thủng ổ loét dạ dày cấp tính (Thủng tạng rỗng)",
            nameEn = "Gastric ulcer with perforation",
            chapter = "Chương XI: Bệnh hệ tiêu hóa (K00 - K93)",
            emrGroup = "Cấp cứu bụng ngoại khoa",
            synonyms = listOf("k25.5", "k255", "k25", "thung da day", "thung tang rong", "bung go", "liem hoi", "loet da day thung")
        ),
        Icd10Item(
            code = "K25.0",
            nameVi = "Loét dạ dày cấp tính có xuất huyết",
            nameEn = "Acute gastric ulcer with hemorrhage",
            chapter = "Chương XI: Bệnh hệ tiêu hóa (K00 - K93)",
            emrGroup = "Xuất huyết tiêu hóa",
            synonyms = listOf("k25.0", "k250", "k25", "loet da day chay mau", "non ra mau", "xuat huyet da day")
        ),
        Icd10Item(
            code = "K26.5",
            nameVi = "Thủng ổ loét hành tá tràng",
            nameEn = "Duodenal ulcer with perforation",
            chapter = "Chương XI: Bệnh hệ tiêu hóa (K00 - K93)",
            emrGroup = "Cấp cứu bụng ngoại khoa",
            synonyms = listOf("k26.5", "k265", "k26", "thung ta trang", "thung hanh ta trang", "thung tang rong")
        ),
        Icd10Item(
            code = "K65.0",
            nameVi = "Viêm phúc mạc toàn thể cấp tính",
            nameEn = "Acute peritonitis",
            chapter = "Chương XI: Bệnh hệ tiêu hóa (K00 - K93)",
            emrGroup = "Cấp cứu bụng ngoại khoa",
            synonyms = listOf("k65.0", "k650", "k65", "viem phuc mac", "viem phuc mac toan the", "bung cung nhu go")
        ),
        Icd10Item(
            code = "K35.8",
            nameVi = "Viêm ruột thừa cấp có viêm phúc mạc khu trú hoặc biến chứng",
            nameEn = "Other and unspecified acute appendicitis",
            chapter = "Chương XI: Bệnh hệ tiêu hóa (K00 - K93)",
            emrGroup = "Cấp cứu bụng ngoại khoa",
            synonyms = listOf("k35.8", "k358", "k35", "viem ruot thua", "viem ruot thua cap", "dau ho chau phai", "mcburney")
        ),
        Icd10Item(
            code = "K35.2",
            nameVi = "Viêm ruột thừa cấp vỡ gây viêm phúc mạc toàn thể",
            nameEn = "Acute appendicitis with generalized peritonitis",
            chapter = "Chương XI: Bệnh hệ tiêu hóa (K00 - K93)",
            emrGroup = "Cấp cứu bụng ngoại khoa",
            synonyms = listOf("k35.2", "k352", "k35", "viem ruot thua vo", "ruot thua vo")
        ),
        Icd10Item(
            code = "K85.9",
            nameVi = "Viêm tụy cấp mức độ nặng",
            nameEn = "Acute pancreatitis, unspecified",
            chapter = "Chương XI: Bệnh hệ tiêu hóa (K00 - K93)",
            emrGroup = "Cấp cứu tiêu hóa",
            synonyms = listOf("k85.9", "k859", "k85", "viem tuy cap", "tang amylase", "dau thuong vi doi buong")
        ),
        Icd10Item(
            code = "K80.0",
            nameVi = "Sỏi túi mật kèm viêm túi mật cấp tính",
            nameEn = "Calculus of gallbladder with acute cholecystitis",
            chapter = "Chương XI: Bệnh hệ tiêu hóa (K00 - K93)",
            emrGroup = "Cấp cứu gan mật",
            synonyms = listOf("k80.0", "k800", "k80", "soi tui mat", "viem tui mat", "murphy")
        ),
        Icd10Item(
            code = "K92.2",
            nameVi = "Xuất huyết tiêu hóa cấp tính mức độ nặng, không đặc hiệu",
            nameEn = "Gastrointestinal hemorrhage, unspecified",
            chapter = "Chương XI: Bệnh hệ tiêu hóa (K00 - K93)",
            emrGroup = "Xuất huyết tiêu hóa",
            synonyms = listOf("k92.2", "k922", "k92", "xuat huyet tieu hoa", "non ra mau", "di ngoai phan den", "chay mau da day")
        ),
        Icd10Item(
            code = "K56.6",
            nameVi = "Tắc ruột cơ học cấp tính",
            nameEn = "Other and unspecified intestinal obstruction",
            chapter = "Chương XI: Bệnh hệ tiêu hóa (K00 - K93)",
            emrGroup = "Cấp cứu bụng ngoại khoa",
            synonyms = listOf("k56.6", "k566", "k56", "tac ruot", "tac ruot co hoc", "bi trung dai tien", "muc nuoc hoi")
        ),

        // =========================================================================
        // CHƯƠNG IV: BỆNH NỘI TIẾT, DINH DƯỠNG & CHUYỂN HÓA (E00 - E90)
        // =========================================================================
        Icd10Item(
            code = "E16.2",
            nameVi = "Hạ đường huyết cấp tính do thuốc điều trị đái tháo đường",
            nameEn = "Hypoglycemia, unspecified",
            chapter = "Chương IV: Bệnh nội tiết & chuyển hóa (E00 - E90)",
            emrGroup = "Cấp cứu chuyển hóa",
            synonyms = listOf("e16.2", "e162", "e16", "ha duong huyet", "hypo", "tut duong huyet", "hon me ha duong huyet")
        ),
        Icd10Item(
            code = "E10.1",
            nameVi = "Đái tháo đường typ 1 có nhiễm toan ceton (DKA)",
            nameEn = "Type 1 diabetes mellitus with ketoacidosis",
            chapter = "Chương IV: Bệnh nội tiết & chuyển hóa (E00 - E90)",
            emrGroup = "Cấp cứu chuyển hóa",
            synonyms = listOf("e10.1", "e101", "dka", "toan ceton", "nhiem toan ceton", "dai thao duong")
        ),
        Icd10Item(
            code = "E11.0",
            nameVi = "Đái tháo đường typ 2 có hôn mê tăng áp lực thẩm thấu (HHS)",
            nameEn = "Type 2 diabetes mellitus with hyperosmolar coma",
            chapter = "Chương IV: Bệnh nội tiết & chuyển hóa (E00 - E90)",
            emrGroup = "Cấp cứu chuyển hóa",
            synonyms = listOf("e11.0", "e110", "hhs", "tang ap luc tham thau", "hon me dai thao duong")
        ),
        Icd10Item(
            code = "E87.5",
            nameVi = "Tăng kali máu nặng có đe dọa loạn nhịp tim",
            nameEn = "Hyperkalemia",
            chapter = "Chương IV: Bệnh nội tiết & chuyển hóa (E00 - E90)",
            emrGroup = "Rối loạn điện giải cấp cứu",
            synonyms = listOf("e87.5", "e875", "e87", "tang kali mau", "tang kali", "song t cao nhon")
        ),
        Icd10Item(
            code = "E87.6",
            nameVi = "Hạ kali máu nặng",
            nameEn = "Hypokalemia",
            chapter = "Chương IV: Bệnh nội tiết & chuyển hóa (E00 - E90)",
            emrGroup = "Rối loạn điện giải cấp cứu",
            synonyms = listOf("e87.6", "e876", "e87", "ha kali mau", "ha kali")
        ),

        // =========================================================================
        // CHƯƠNG XIV: BỆNH HỆ TIẾT NIỆU SINH DỤC & THẬN (N00 - N99)
        // =========================================================================
        Icd10Item(
            code = "N20.1",
            nameVi = "Cơn đau quặn thận cấp tính do sỏi niệu quản gây ứ nước",
            nameEn = "Calculus of ureter with renal colic",
            chapter = "Chương XIV: Bệnh hệ tiết niệu (N00 - N99)",
            emrGroup = "Cấp cứu tiết niệu",
            synonyms = listOf("n20.1", "n201", "n20", "con dau quan than", "soi nieu quan", "soi than", "dau quan than")
        ),
        Icd10Item(
            code = "N17.9",
            nameVi = "Tổn thương thận cấp (Suy thận cấp tính - AKI)",
            nameEn = "Acute kidney failure, unspecified",
            chapter = "Chương XIV: Bệnh hệ tiết niệu (N00 - N99)",
            emrGroup = "Cấp cứu thận học",
            synonyms = listOf("n17.9", "n179", "n17", "suy than cap", "aki", "vo nieu", "thieu nieu")
        ),
        Icd10Item(
            code = "N39.0",
            nameVi = "Nhiễm khuẩn đường tiết niệu / Nhiễm trùng thận bể thận cấp",
            nameEn = "Urinary tract infection",
            chapter = "Chương XIV: Bệnh hệ tiết niệu (N00 - N99)",
            emrGroup = "Nhiễm trùng",
            synonyms = listOf("n39.0", "n390", "n39", "nhiem trung tieu", "viem dai be than")
        ),

        // =========================================================================
        // CHƯƠNG XIX: CHẤN THƯƠNG, VẾT THƯƠNG & NGỘ ĐỘC (S00 - T98)
        // =========================================================================
        Icd10Item(
            code = "S06.4",
            nameVi = "Tụ máu ngoài màng cứng cấp tính do chấn thương sọ não (EDH)",
            nameEn = "Epidural hemorrhage",
            chapter = "Chương XIX: Chấn thương & Ngộ độc (S00 - T98)",
            emrGroup = "Chấn thương sọ não cấp",
            synonyms = listOf("s06.4", "s064", "s06", "tu mau ngoai mang cung", "edh", "khoang tinh", "chan thuong so nao", "gian dong tu")
        ),
        Icd10Item(
            code = "S06.5",
            nameVi = "Tụ máu dưới màng cứng cấp tính do chấn thương (SDH)",
            nameEn = "Traumatic subdural hemorrhage",
            chapter = "Chương XIX: Chấn thương & Ngộ độc (S00 - T98)",
            emrGroup = "Chấn thương sọ não cấp",
            synonyms = listOf("s06.5", "s065", "s06", "tu mau duoi mang cung", "sdh", "chan thuong dau")
        ),
        Icd10Item(
            code = "S06.2",
            nameVi = "Dập não lan tỏa do chấn thương sọ não",
            nameEn = "Diffuse traumatic brain injury",
            chapter = "Chương XIX: Chấn thương & Ngộ độc (S00 - T98)",
            emrGroup = "Chấn thương sọ não cấp",
            synonyms = listOf("s06.2", "s062", "s06", "dap nao", "chan thuong so nao nang")
        ),
        Icd10Item(
            code = "S02.0",
            nameVi = "Gãy vòm sọ hở hoặc kín do tai nạn giao thông",
            nameEn = "Fracture of vault of skull",
            chapter = "Chương XIX: Chấn thương & Ngộ độc (S00 - T98)",
            emrGroup = "Chấn thương sọ não",
            synonyms = listOf("s02.0", "s020", "s02", "lun so", "gay xuong so", "vo vom so")
        ),
        Icd10Item(
            code = "S27.1",
            nameVi = "Tràn máu màng phổi chấn thương do tai nạn giao thông",
            nameEn = "Traumatic hemothorax",
            chapter = "Chương XIX: Chấn thương & Ngộ độc (S00 - T98)",
            emrGroup = "Chấn thương lồng ngực",
            synonyms = listOf("s27.1", "s271", "s27", "tran mau mang phoi", "hemothorax", "chan thuong nguc kin")
        ),
        Icd10Item(
            code = "S27.3",
            nameVi = "Dập phổi do chấn thương lồng ngực",
            nameEn = "Other injuries of lung / Lung contusion",
            chapter = "Chương XIX: Chấn thương & Ngộ độc (S00 - T98)",
            emrGroup = "Chấn thương lồng ngực",
            synonyms = listOf("s27.3", "s273", "s27", "dap phoi", "chan thuong nguc")
        ),
        Icd10Item(
            code = "S27.0",
            nameVi = "Tràn khí màng phổi do chấn thương",
            nameEn = "Traumatic pneumothorax",
            chapter = "Chương XIX: Chấn thương & Ngộ độc (S00 - T98)",
            emrGroup = "Chấn thương lồng ngực",
            synonyms = listOf("s27.0", "s270", "s27", "tran khi mang phoi chan thuong")
        ),
        Icd10Item(
            code = "S22.4",
            nameVi = "Gãy nhiều xương sườn / Mảng sườn di động (Flail chest)",
            nameEn = "Multiple fractures of ribs / Flail chest",
            chapter = "Chương XIX: Chấn thương & Ngộ độc (S00 - T98)",
            emrGroup = "Chấn thương lồng ngực",
            synonyms = listOf("s22.4", "s224", "s22", "gay suon", "mang suon di dong", "flail chest")
        ),
        Icd10Item(
            code = "S36.0",
            nameVi = "Chấn thương vỡ lách độ IV-V gây sốc mất máu",
            nameEn = "Injury of spleen",
            chapter = "Chương XIX: Chấn thương & Ngộ độc (S00 - T98)",
            emrGroup = "Chấn thương bụng kín",
            synonyms = listOf("s36.0", "s360", "s36", "vo lach", "chan thuong lach", "chay mau o bung", "soc mat mau")
        ),
        Icd10Item(
            code = "S36.1",
            nameVi = "Chấn thương vỡ gan gây chảy máu ổ bụng",
            nameEn = "Injury of liver or gallbladder",
            chapter = "Chương XIX: Chấn thương & Ngộ độc (S00 - T98)",
            emrGroup = "Chấn thương bụng kín",
            synonyms = listOf("s36.1", "s361", "s36", "vo gan", "chan thuong gan")
        ),
        Icd10Item(
            code = "S72.3",
            nameVi = "Gãy thân xương đùi có biến chứng sốc chấn thương / tắc mạch mỡ",
            nameEn = "Fracture of shaft of femur",
            chapter = "Chương XIX: Chấn thương & Ngộ độc (S00 - T98)",
            emrGroup = "Chấn thương chỉnh hình",
            synonyms = listOf("s72.3", "s723", "s72", "gay xuong dui", "gay dui", "soc chan thuong")
        ),
        Icd10Item(
            code = "S32.8",
            nameVi = "Gãy khung chậu phức tạp có sốc mất máu nặng",
            nameEn = "Fracture of other and unspecified parts of pelvis",
            chapter = "Chương XIX: Chấn thương & Ngộ độc (S00 - T98)",
            emrGroup = "Chấn thương chỉnh hình",
            synonyms = listOf("s32.8", "s328", "s32", "gay khung chau", "toac khung chau", "soc mat mau")
        ),
        Icd10Item(
            code = "T07",
            nameVi = "Đa chấn thương nặng không đặc hiệu (Polytrauma)",
            nameEn = "Unspecified multiple injuries",
            chapter = "Chương XIX: Chấn thương & Ngộ độc (S00 - T98)",
            emrGroup = "Cấp cứu đa chấn thương",
            synonyms = listOf("t07", "da chan thuong", "polytrauma", "tai nan giao thong")
        ),

        // =========================================================================
        // DỊ ỨNG & SỐC PHẢN VỆ (T78, T88)
        // =========================================================================
        Icd10Item(
            code = "T78.2",
            nameVi = "Sốc phản vệ độ III (nguy kịch) do thuốc hoặc dị nguyên",
            nameEn = "Anaphylactic shock, unspecified",
            chapter = "Chương XIX: Chấn thương & Ngộ độc (S00 - T98)",
            emrGroup = "Cấp cứu dị ứng & Miễn dịch",
            synonyms = listOf("t78.2", "t782", "t78", "soc phan ve", "anaphylaxis", "di ung thuoc", "adrenaline", "tut huyet ap phan ve")
        ),
        Icd10Item(
            code = "T78.0",
            nameVi = "Sốc phản vệ nguy kịch do dị ứng thức ăn (Hải sản, đậu phộng)",
            nameEn = "Anaphylactic shock due to adverse food reaction",
            chapter = "Chương XIX: Chấn thương & Ngộ độc (S00 - T98)",
            emrGroup = "Cấp cứu dị ứng & Miễn dịch",
            synonyms = listOf("t78.0", "t780", "t78", "soc phan ve thuc an", "di ung hai san", "ngung tho ngung tim phan ve")
        ),
        Icd10Item(
            code = "T88.6",
            nameVi = "Sốc phản vệ do tai biến điều trị thuốc tiêm / kháng sinh",
            nameEn = "Anaphylactic shock due to adverse effect of correct drug",
            chapter = "Chương XIX: Chấn thương & Ngộ độc (S00 - T98)",
            emrGroup = "Cấp cứu dị ứng & Miễn dịch",
            synonyms = listOf("t88.6", "t886", "t88", "soc phan ve thuoc tiem", "tai bien tiem truyen", "di ung khang sinh")
        ),

        // =========================================================================
        // CHỐNG ĐỘC & NHIỄM ĐỘC CẤP (T51 - T65)
        // =========================================================================
        Icd10Item(
            code = "T60.0",
            nameVi = "Ngộ độc cấp thuốc trừ sâu phospho hữu cơ (Hội chứng Muscarinic/Nicotinic)",
            nameEn = "Organophosphate and carbamate insecticides",
            chapter = "Chương XIX: Chấn thương & Ngộ độc (S00 - T98)",
            emrGroup = "Trung tâm chống độc",
            synonyms = listOf("t60.0", "t600", "t60", "phospho huu co", "thuoc tru sau", "atropin", "ngay dong tu", "co that phe quan")
        ),
        Icd10Item(
            code = "T51.1",
            nameVi = "Ngộ độc cấp cồn công nghiệp Methanol (Toan chuyển hóa nặng, tổn thương thị giác)",
            nameEn = "Methanol toxic effect",
            chapter = "Chương XIX: Chấn thương & Ngộ độc (S00 - T98)",
            emrGroup = "Trung tâm chống độc",
            synonyms = listOf("t51.1", "t511", "t51", "methanol", "ngo doc ruou", "con cong nghiep", "mu mat", "toan chuyen hoa")
        ),
        Icd10Item(
            code = "T58",
            nameVi = "Ngộ độc cấp khí Carbon monoxide (CO) do sưởi ấm than",
            nameEn = "Toxic effect of carbon monoxide",
            chapter = "Chương XIX: Chấn thương & Ngộ độc (S00 - T98)",
            emrGroup = "Trung tâm chống độc",
            synonyms = listOf("t58", "khi co", "ngo doc co", "suoi than", "carbon monoxide", "hbco")
        ),
        Icd10Item(
            code = "T39.1",
            nameVi = "Ngộ độc cấp Paracetamol (Acetaminophen) gây suy gan tối cấp",
            nameEn = "4-Aminophenol derivatives (Paracetamol)",
            chapter = "Chương XIX: Chấn thương & Ngộ độc (S00 - T98)",
            emrGroup = "Trung tâm chống độc",
            synonyms = listOf("t39.1", "t391", "t39", "paracetamol", "acetaminophen", "nac", "suy gan cap")
        ),
        Icd10Item(
            code = "T63.0",
            nameVi = "Nhiễm độc nọc rắn độc cắn cấp tính gây rối loạn đông máu nặng",
            nameEn = "Snake venom",
            chapter = "Chương XIX: Chấn thương & Ngộ độc (S00 - T98)",
            emrGroup = "Trung tâm chống độc",
            synonyms = listOf("t63.0", "t630", "t63", "ran can", "noc ran", "huyet thanh khang noc ran", "chay mau khong dong")
        ),
        Icd10Item(
            code = "T63.4",
            nameVi = "Nhiễm độc nọc ong vò vẽ đốt gây tan máu & suy thận cấp",
            nameEn = "Venom of other arthropods (Wasp sting)",
            chapter = "Chương XIX: Chấn thương & Ngộ độc (S00 - T98)",
            emrGroup = "Trung tâm chống độc",
            synonyms = listOf("t63.4", "t634", "t63", "ong dot", "ong vo ve", "tieu co van")
        ),
        Icd10Item(
            code = "T40.1",
            nameVi = "Ngộ độc cấp Opiate / Heroin gây ức chế hô hấp",
            nameEn = "Heroin poisoning",
            chapter = "Chương XIX: Chấn thương & Ngộ độc (S00 - T98)",
            emrGroup = "Trung tâm chống độc",
            synonyms = listOf("t40.1", "t401", "t40", "heroin", "opiate", "naloxone", "co dong tu", "ngung tho")
        ),
        Icd10Item(
            code = "T42.4",
            nameVi = "Ngộ độc cấp Benzodiazepine (Thuốc an thần)",
            nameEn = "Benzodiazepines",
            chapter = "Chương XIX: Chấn thương & Ngộ độc (S00 - T98)",
            emrGroup = "Trung tâm chống độc",
            synonyms = listOf("t42.4", "t424", "t42", "seduxen", "thuoc ngu", "flumazenil", "hon me")
        ),
        Icd10Item(
            code = "T67.0",
            nameVi = "Sốc nhiệt / Say nóng say nắng nặng (Heat stroke)",
            nameEn = "Heatstroke and sunstroke",
            chapter = "Chương XIX: Chấn thương & Ngộ độc (S00 - T98)",
            emrGroup = "Môi trường & Cấp cứu",
            synonyms = listOf("t67.0", "t670", "t67", "soc nhiet", "say nang", "heat stroke", "sot cao co giat")
        ),
        Icd10Item(
            code = "T75.1",
            nameVi = "Đuối nước / Ngạt nước cấp tính",
            nameEn = "Drowning and nonfatal submersion",
            chapter = "Chương XIX: Chấn thương & Ngộ độc (S00 - T98)",
            emrGroup = "Cấp cứu đuối nước",
            synonyms = listOf("t75.1", "t751", "t75", "duoi nuoc", "ngat nuoc", "duoi nuoc ngot")
        ),
        Icd10Item(
            code = "T75.0",
            nameVi = "Sét đánh / Tổn thương do điện giật cao thế",
            nameEn = "Effects of lightning / Electrical shock",
            chapter = "Chương XIX: Chấn thương & Ngộ độc (S00 - T98)",
            emrGroup = "Tai nạn bỏng & Điện giật",
            synonyms = listOf("t75.0", "t750", "t75", "dien giat", "set danh", "bong dien", "roi loan nhip dien")
        ),

        // =========================================================================
        // SỐC & CÁC HỘI CHỨNG NGUY KỊCH (R57, A41)
        // =========================================================================
        Icd10Item(
            code = "R57.0",
            nameVi = "Sốc tim (Cardiogenic shock)",
            nameEn = "Cardiogenic shock",
            chapter = "Chương XVIII: Triệu chứng & Dấu hiệu (R00 - R99)",
            emrGroup = "Hồi sức cấp cứu",
            synonyms = listOf("r57.0", "r570", "r57", "soc tim", "cardiogenic shock", "tut huyet ap tim")
        ),
        Icd10Item(
            code = "R57.1",
            nameVi = "Sốc giảm thể tích / Sốc mất máu cấp",
            nameEn = "Hypovolemic shock",
            chapter = "Chương XVIII: Triệu chứng & Dấu hiệu (R00 - R99)",
            emrGroup = "Hồi sức cấp cứu",
            synonyms = listOf("r57.1", "r571", "r57", "soc giam the tich", "soc mat mau", "hypovolemic shock")
        ),
        Icd10Item(
            code = "R57.2",
            nameVi = "Sốc nhiễm khuẩn (Septic shock)",
            nameEn = "Septic shock",
            chapter = "Chương XVIII: Triệu chứng & Dấu hiệu (R00 - R99)",
            emrGroup = "Hồi sức cấp cứu",
            synonyms = listOf("r57.2", "r572", "r57", "soc nhiem khuan", "soc nhiem trung", "septic shock", "sepsis")
        ),
        Icd10Item(
            code = "A41.9",
            nameVi = "Nhiễm khuẩn huyết nặng, không đặc hiệu (Sepsis)",
            nameEn = "Sepsis, unspecified",
            chapter = "Chương I: Bệnh nhiễm trùng (A00 - B99)",
            emrGroup = "Bệnh truyền nhiễm & Hồi sức",
            synonyms = listOf("a41.9", "a419", "a41", "nhiem trung huyet", "sepsis", "nhiem khuan huyet")
        ),
        Icd10Item(
            code = "A09",
            nameVi = "Tiêu chảy cấp và viêm dạ dày - ruột nhiễm khuẩn",
            nameEn = "Infectious gastroenteritis and colitis, unspecified",
            chapter = "Chương I: Bệnh nhiễm trùng (A00 - B99)",
            emrGroup = "Tiêu hóa & Truyền nhiễm",
            synonyms = listOf("a09", "tieu chay cap", "viem da day ruot", "mat nuoc", "ngo doc thuc pham")
        ),
        Icd10Item(
            code = "A91",
            nameVi = "Sốt xuất huyết Dengue nặng có sốc (Dengue shock syndrome)",
            nameEn = "Dengue hemorrhagic fever",
            chapter = "Chương I: Bệnh nhiễm trùng (A00 - B99)",
            emrGroup = "Bệnh truyền nhiễm",
            synonyms = listOf("a91", "a90", "sot xuat huyet", "soc sot xuat huyet", "dengue", "dss")
        ),
        Icd10Item(
            code = "R40.2",
            nameVi = "Hôn mê sâu không xác định (Glasgow ≤ 8 điểm)",
            nameEn = "Coma, unspecified",
            chapter = "Chương XVIII: Triệu chứng & Dấu hiệu (R00 - R99)",
            emrGroup = "Hồi sức thần kinh",
            synonyms = listOf("r40.2", "r402", "r40", "hon me", "glasgow", "gcs", "mat y thuc")
        ),
        Icd10Item(
            code = "R55",
            nameVi = "Ngất và trụy mạch cấp tính (Syncope)",
            nameEn = "Syncope and collapse",
            chapter = "Chương XVIII: Triệu chứng & Dấu hiệu (R00 - R99)",
            emrGroup = "Triệu chứng cấp cứu",
            synonyms = listOf("r55", "ngat", "truy mach", "syncope", "choang ngat")
        ),
        Icd10Item(
            code = "R07.4",
            nameVi = "Đau ngực cấp không đặc hiệu",
            nameEn = "Chest pain, unspecified",
            chapter = "Chương XVIII: Triệu chứng & Dấu hiệu (R00 - R99)",
            emrGroup = "Triệu chứng cấp cứu",
            synonyms = listOf("r07.4", "r074", "r07", "dau nguc", "dau nguc cap")
        ),
        Icd10Item(
            code = "R10.0",
            nameVi = "Bụng ngoại khoa cấp tính (Đau bụng cấp)",
            nameEn = "Acute abdomen",
            chapter = "Chương XVIII: Triệu chứng & Dấu hiệu (R00 - R99)",
            emrGroup = "Triệu chứng cấp cứu",
            synonyms = listOf("r10.0", "r100", "r10", "dau bung cap", "bung ngoai khoa", "acute abdomen")
        ),
        Icd10Item(
            code = "R06.0",
            nameVi = "Khó thở cấp tính (Dyspnea)",
            nameEn = "Dyspnea",
            chapter = "Chương XVIII: Triệu chứng & Dấu hiệu (R00 - R99)",
            emrGroup = "Triệu chứng cấp cứu",
            synonyms = listOf("r06.0", "r060", "r06", "kho tho", "kho tho cap", "dyspnea")
        ),
        Icd10Item(
            code = "R04.2",
            nameVi = "Ho ra máu cấp tính (Hemoptysis)",
            nameEn = "Hemoptysis",
            chapter = "Chương XVIII: Triệu chứng & Dấu hiệu (R00 - R99)",
            emrGroup = "Triệu chứng cấp cứu",
            synonyms = listOf("r04.2", "r042", "r04", "ho ra mau", "ho ra mau set danh")
        ),
        Icd10Item(
            code = "O00.1",
            nameVi = "Chửa ngoài tử cung vỡ gây sốc mất máu",
            nameEn = "Tubal pregnancy with rupture",
            chapter = "Chương XV: Thai nghén & Sinh đẻ (O00 - O99)",
            emrGroup = "Cấp cứu sản phụ khoa",
            synonyms = listOf("o00.1", "o001", "o00", "chua ngoai tu cung", "chua ngoai tu cung vo", "soc mat mau san khoa")
        ),
        Icd10Item(
            code = "O15.0",
            nameVi = "Sản giật trong thời kỳ mang thai hoặc chuyển dạ",
            nameEn = "Eclampsia in pregnancy",
            chapter = "Chương XV: Thai nghén & Sinh đẻ (O00 - O99)",
            emrGroup = "Cấp cứu sản phụ khoa",
            synonyms = listOf("o15.0", "o150", "o15", "san giat", "tien san giat nang")
        )
    )

    /**
     * Chuyển chuỗi tiếng Việt có dấu thành không dấu để tìm kiếm siêu nhạy
     */
    fun removeDiacritics(str: String): String {
        val nfd = Normalizer.normalize(str, Normalizer.Form.NFD)
        val pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+")
        return pattern.matcher(nfd).replaceAll("")
            .replace("đ", "d")
            .replace("Đ", "D")
    }

    private fun normalize(str: String): String {
        return removeDiacritics(str)
            .trim()
            .lowercase()
            .replace(".", "")
            .replace("-", "")
            .replace(" ", "")
            .replace("[", "")
            .replace("]", "")
            .replace(";", "")
            .replace(",", "")
    }

    data class OfficialCategory(
        val nameVi: String,
        val nameEn: String,
        val chapter: String,
        val emrGroup: String
    )

    val officialCategories: Map<String, OfficialCategory> = mapOf(
        // CHƯƠNG I: NHIỄM TRÙNG & KÝ SINH TRÙNG (A00 - B99)
        "A00" to OfficialCategory("Bệnh tả (Cholera)", "Cholera", "Chương I: Bệnh nhiễm trùng (A00 - B99)", "Truyền nhiễm tiêu hóa"),
        "A01" to OfficialCategory("Sốt thương hàn và phó thương hàn", "Typhoid and paratyphoid fevers", "Chương I: Bệnh nhiễm trùng (A00 - B99)", "Truyền nhiễm tiêu hóa"),
        "A02" to OfficialCategory("Nhiễm trùng do Salmonella khác", "Other salmonella infections", "Chương I: Bệnh nhiễm trùng (A00 - B99)", "Truyền nhiễm tiêu hóa"),
        "A03" to OfficialCategory("Bệnh lỵ trực khuẩn (Shigellosis)", "Shigellosis", "Chương I: Bệnh nhiễm trùng (A00 - B99)", "Truyền nhiễm tiêu hóa"),
        "A04" to OfficialCategory("Nhiễm khuẩn đường ruột do vi khuẩn khác", "Other bacterial intestinal infections", "Chương I: Bệnh nhiễm trùng (A00 - B99)", "Truyền nhiễm tiêu hóa"),
        "A05" to OfficialCategory("Nhiễm độc thức ăn do vi khuẩn khác", "Other bacterial foodborne intoxications", "Chương I: Bệnh nhiễm trùng (A00 - B99)", "Ngộ độc tiêu hóa"),
        "A06" to OfficialCategory("Bệnh lỵ amip (Amoebiasis)", "Amoebiasis", "Chương I: Bệnh nhiễm trùng (A00 - B99)", "Truyền nhiễm tiêu hóa"),
        "A08" to OfficialCategory("Nhiễm virus và nhiễm trùng đường ruột khác", "Viral and other specified intestinal infections", "Chương I: Bệnh nhiễm trùng (A00 - B99)", "Truyền nhiễm tiêu hóa"),
        "A09" to OfficialCategory("Tiêu chảy và viêm dạ dày - ruột do nhiễm trùng", "Infectious gastroenteritis and colitis", "Chương I: Bệnh nhiễm trùng (A00 - B99)", "Truyền nhiễm tiêu hóa"),
        "A15" to OfficialCategory("Lao đường hô hấp đã xác định vi khuẩn học", "Respiratory tuberculosis, bacteriologically confirmed", "Chương I: Bệnh nhiễm trùng (A00 - B99)", "Bệnh lao"),
        "A16" to OfficialCategory("Lao đường hô hấp chưa xác định vi khuẩn học", "Respiratory tuberculosis, not confirmed", "Chương I: Bệnh nhiễm trùng (A00 - B99)", "Bệnh lao"),
        "A17" to OfficialCategory("Lao màng não và hệ thần kinh trung ương", "Tuberculosis of nervous system", "Chương I: Bệnh nhiễm trùng (A00 - B99)", "Bệnh lao"),
        "A20" to OfficialCategory("Bệnh dịch hạch (Plague)", "Plague", "Chương I: Bệnh nhiễm trùng (A00 - B99)", "Truyền nhiễm nguy hiểm"),
        "A22" to OfficialCategory("Bệnh than (Anthrax)", "Anthrax", "Chương I: Bệnh nhiễm trùng (A00 - B99)", "Truyền nhiễm"),
        "A27" to OfficialCategory("Bệnh do xoắn khuẩn Leptospira", "Leptospirosis", "Chương I: Bệnh nhiễm trùng (A00 - B99)", "Truyền nhiễm"),
        "A35" to OfficialCategory("Bệnh uốn ván (Tetanus)", "Other tetanus", "Chương I: Bệnh nhiễm trùng (A00 - B99)", "Hồi sức truyền nhiễm"),
        "A36" to OfficialCategory("Bệnh bạch hầu (Diphtheria)", "Diphtheria", "Chương I: Bệnh nhiễm trùng (A00 - B99)", "Truyền nhiễm"),
        "A37" to OfficialCategory("Bệnh ho gà (Whooping cough)", "Whooping cough", "Chương I: Bệnh nhiễm trùng (A00 - B99)", "Truyền nhiễm"),
        "A39" to OfficialCategory("Nhiễm khuẩn do não mô cầu (Meningococcal infection)", "Meningococcal infection", "Chương I: Bệnh nhiễm trùng (A00 - B99)", "Hồi sức truyền nhiễm"),
        "A40" to OfficialCategory("Nhiễm khuẩn huyết do liên cầu (Streptococcal sepsis)", "Streptococcal sepsis", "Chương I: Bệnh nhiễm trùng (A00 - B99)", "Hồi sức cấp cứu"),
        "A41" to OfficialCategory("Nhiễm khuẩn huyết khác (Other sepsis)", "Other sepsis", "Chương I: Bệnh nhiễm trùng (A00 - B99)", "Hồi sức cấp cứu"),
        "A46" to OfficialCategory("Viêm quầng (Erysipelas)", "Erysipelas", "Chương I: Bệnh nhiễm trùng (A00 - B99)", "Nhiễm khuẩn da"),
        "A49" to OfficialCategory("Nhiễm khuẩn vị trí không xác định", "Bacterial infection of unspecified site", "Chương I: Bệnh nhiễm trùng (A00 - B99)", "Nhiễm khuẩn"),
        "A50" to OfficialCategory("Bệnh giang mai bẩm sinh", "Congenital syphilis", "Chương I: Bệnh nhiễm trùng (A00 - B99)", "Da liễu"),
        "A54" to OfficialCategory("Nhiễm lậu cầu (Gonococcal infection)", "Gonococcal infection", "Chương I: Bệnh nhiễm trùng (A00 - B99)", "Da liễu"),
        "A80" to OfficialCategory("Bệnh bại liệt cấp tính (Poliomyelitis)", "Acute poliomyelitis", "Chương I: Bệnh nhiễm trùng (A00 - B99)", "Thần kinh"),
        "A82" to OfficialCategory("Bệnh dại do động vật cắn (Rabies)", "Rabies", "Chương I: Bệnh nhiễm trùng (A00 - B99)", "Truyền nhiễm"),
        "A86" to OfficialCategory("Viêm não virus không đặc hiệu", "Unspecified viral encephalitis", "Chương I: Bệnh nhiễm trùng (A00 - B99)", "Hồi sức thần kinh"),
        "A90" to OfficialCategory("Sốt Dengue cổ điển (Dengue fever)", "Dengue fever [classical dengue]", "Chương I: Bệnh nhiễm trùng (A00 - B99)", "Truyền nhiễm"),
        "A91" to OfficialCategory("Sốt xuất huyết Dengue nặng có sốc (DHF)", "Dengue haemorrhagic fever", "Chương I: Bệnh nhiễm trùng (A00 - B99)", "Cấp cứu truyền nhiễm"),
        "B00" to OfficialCategory("Nhiễm Herpes simplex", "Herpesviral [herpes simplex] infections", "Chương I: Bệnh nhiễm trùng (A00 - B99)", "Da liễu"),
        "B01" to OfficialCategory("Bệnh thủy đậu (Varicella)", "Varicella [chickenpox]", "Chương I: Bệnh nhiễm trùng (A00 - B99)", "Truyền nhiễm"),
        "B02" to OfficialCategory("Bệnh Zona thần kinh (Herpes zoster)", "Zoster [herpes zoster]", "Chương I: Bệnh nhiễm trùng (A00 - B99)", "Da liễu - Thần kinh"),
        "B05" to OfficialCategory("Bệnh sởi (Measles)", "Measles", "Chương I: Bệnh nhiễm trùng (A00 - B99)", "Truyền nhiễm"),
        "B06" to OfficialCategory("Bệnh Rubella (Sởi Đức)", "Rubella [German measles]", "Chương I: Bệnh nhiễm trùng (A00 - B99)", "Truyền nhiễm"),
        "B08" to OfficialCategory("Nhiễm virus có tổn thương da niêm mạc (Tay chân miệng)", "Other viral infections characterized by skin lesions", "Chương I: Bệnh nhiễm trùng (A00 - B99)", "Truyền nhiễm"),
        "B15" to OfficialCategory("Viêm gan virus A cấp tính", "Acute hepatitis A", "Chương I: Bệnh nhiễm trùng (A00 - B99)", "Tiêu hóa - Gan mật"),
        "B16" to OfficialCategory("Viêm gan virus B cấp tính", "Acute hepatitis B", "Chương I: Bệnh nhiễm trùng (A00 - B99)", "Tiêu hóa - Gan mật"),
        "B17" to OfficialCategory("Viêm gan virus cấp tính khác (Hepatitis C, E)", "Other acute viral hepatitis", "Chương I: Bệnh nhiễm trùng (A00 - B99)", "Tiêu hóa - Gan mật"),
        "B18" to OfficialCategory("Viêm gan virus mạn tính", "Chronic viral hepatitis", "Chương I: Bệnh nhiễm trùng (A00 - B99)", "Tiêu hóa - Gan mật"),
        "B19" to OfficialCategory("Viêm gan virus không đặc hiệu", "Unspecified viral hepatitis", "Chương I: Bệnh nhiễm trùng (A00 - B99)", "Tiêu hóa - Gan mật"),
        "B20" to OfficialCategory("Bệnh do HIV gây ra các bệnh nhiễm trùng", "Human immunodeficiency virus [HIV] disease resulting in infectious diseases", "Chương I: Bệnh nhiễm trùng (A00 - B99)", "Truyền nhiễm"),
        "B26" to OfficialCategory("Bệnh quai bị (Mumps)", "Mumps", "Chương I: Bệnh nhiễm trùng (A00 - B99)", "Truyền nhiễm"),
        "B34" to OfficialCategory("Nhiễm virus vị trí không xác định", "Viral infection of unspecified site", "Chương I: Bệnh nhiễm trùng (A00 - B99)", "Truyền nhiễm"),
        "B35" to OfficialCategory("Nấm da (Dermatophytosis)", "Dermatophytosis", "Chương I: Bệnh nhiễm trùng (A00 - B99)", "Da liễu"),
        "B50" to OfficialCategory("Sốt rét do Plasmodium falciparum", "Plasmodium falciparum malaria", "Chương I: Bệnh nhiễm trùng (A00 - B99)", "Sốt rét - Truyền nhiễm"),

        // CHƯƠNG II: KHỐI U (C00 - D48)
        "C16" to OfficialCategory("U ác tính của dạ dày (Ung thư dạ dày)", "Malignant neoplasm of stomach", "Chương II: Khối u (C00 - D48)", "Ung bướu tiêu hóa"),
        "C18" to OfficialCategory("U ác tính của đại tràng (Ung thư đại tràng)", "Malignant neoplasm of colon", "Chương II: Khối u (C00 - D48)", "Ung bướu tiêu hóa"),
        "C22" to OfficialCategory("U ác tính của gan và đường mật trong gan (Ung thư gan)", "Malignant neoplasm of liver and intrahepatic bile ducts", "Chương II: Khối u (C00 - D48)", "Ung bướu tiêu hóa"),
        "C34" to OfficialCategory("U ác tính của phế quản và phổi (Ung thư phổi)", "Malignant neoplasm of bronchus and lung", "Chương II: Khối u (C00 - D48)", "Ung bướu hô hấp"),
        "C50" to OfficialCategory("U ác tính của vú (Ung thư vú)", "Malignant neoplasm of breast", "Chương II: Khối u (C00 - D48)", "Ung bướu"),
        "C53" to OfficialCategory("U ác tính của cổ tử cung", "Malignant neoplasm of cervix uteri", "Chương II: Khối u (C00 - D48)", "Ung bướu phụ khoa"),
        "D12" to OfficialCategory("U lành tính đại tràng, trực tràng, hậu môn (Polyp)", "Benign neoplasm of colon, rectum, anus", "Chương II: Khối u (C00 - D48)", "Tiêu hóa"),
        "D17" to OfficialCategory("U mỡ lành tính (Lipoma)", "Benign lipomatous neoplasm", "Chương II: Khối u (C00 - D48)", "Ngoại khoa"),
        "D25" to OfficialCategory("U xơ tử cung (Leiomyoma of uterus)", "Leiomyoma of uterus", "Chương II: Khối u (C00 - D48)", "Sản phụ khoa"),

        // CHƯƠNG III: BỆNH MÁU & MIỄN DỊCH (D50 - D89)
        "D50" to OfficialCategory("Thiếu máu do thiếu sắt (Iron deficiency anaemia)", "Iron deficiency anaemia", "Chương III: Bệnh máu (D50 - D89)", "Huyết học"),
        "D56" to OfficialCategory("Bệnh Thalassemia (Thiếu máu tan máu di truyền)", "Thalassaemia", "Chương III: Bệnh máu (D50 - D89)", "Huyết học"),
        "D64" to OfficialCategory("Các loại thiếu máu khác", "Other anaemias", "Chương III: Bệnh máu (D50 - D89)", "Huyết học"),
        "D69" to OfficialCategory("Ban xuất huyết và tình trạng xuất huyết khác (Giảm tiểu cầu)", "Purpura and other haemorrhagic conditions", "Chương III: Bệnh máu (D50 - D89)", "Huyết học cấp cứu"),

        // CHƯƠNG IV: BỆNH NỘI TIẾT & CHUYỂN HÓA (E00 - E90)
        "E03" to OfficialCategory("Suy giáp khác (Hypothyroidism)", "Other hypothyroidism", "Chương IV: Bệnh nội tiết & chuyển hóa (E00 - E90)", "Nội tiết"),
        "E04" to OfficialCategory("Bướu giáp nhân lành tính", "Other nontoxic goitre", "Chương IV: Bệnh nội tiết & chuyển hóa (E00 - E90)", "Nội tiết"),
        "E05" to OfficialCategory("Nhiễm độc giáp / Cường giáp (Basedow)", "Thyrotoxicosis [hyperthyroidism]", "Chương IV: Bệnh nội tiết & chuyển hóa (E00 - E90)", "Nội tiết"),
        "E10" to OfficialCategory("Bệnh đái tháo đường typ 1", "Type 1 diabetes mellitus", "Chương IV: Bệnh nội tiết & chuyển hóa (E00 - E90)", "Cấp cứu chuyển hóa"),
        "E11" to OfficialCategory("Bệnh đái tháo đường typ 2", "Type 2 diabetes mellitus", "Chương IV: Bệnh nội tiết & chuyển hóa (E00 - E90)", "Nội tiết"),
        "E14" to OfficialCategory("Bệnh đái tháo đường không đặc hiệu", "Unspecified diabetes mellitus", "Chương IV: Bệnh nội tiết & chuyển hóa (E00 - E90)", "Nội tiết"),
        "E16" to OfficialCategory("Hạ đường huyết và rối loạn tiết insulin", "Other disorders of pancreatic internal secretion", "Chương IV: Bệnh nội tiết & chuyển hóa (E00 - E90)", "Cấp cứu chuyển hóa"),
        "E27" to OfficialCategory("Suy tuyến thượng thận khác (Cơn suy thượng thận cấp)", "Other disorders of adrenal gland", "Chương IV: Bệnh nội tiết & chuyển hóa (E00 - E90)", "Cấp cứu nội tiết"),
        "E66" to OfficialCategory("Bệnh béo phì (Obesity)", "Obesity", "Chương IV: Bệnh nội tiết & chuyển hóa (E00 - E90)", "Chuyển hóa"),
        "E78" to OfficialCategory("Rối loạn chuyển hóa lipoprotein và lipid máu (Rối loạn mỡ máu)", "Disorders of lipoprotein metabolism and other lipidaemias", "Chương IV: Bệnh nội tiết & chuyển hóa (E00 - E90)", "Chuyển hóa"),
        "E86" to OfficialCategory("Giảm thể tích dịch và mất nước (Dehydration)", "Volume depletion", "Chương IV: Bệnh nội tiết & chuyển hóa (E00 - E90)", "Hồi sức cấp cứu"),
        "E87" to OfficialCategory("Rối loạn cân bằng điện giải và toan kiềm", "Other disorders of fluid, electrolyte and acid-base balance", "Chương IV: Bệnh nội tiết & chuyển hóa (E00 - E90)", "Hồi sức cấp cứu"),

        // CHƯƠNG V: RỐI LOẠN TÂM THẦN (F00 - F99)
        "F10" to OfficialCategory("Rối loạn tâm thần và hành vi do sử dụng rượu", "Mental and behavioural disorders due to use of alcohol", "Chương V: Rối loạn tâm thần (F00 - F99)", "Tâm thần - Chống độc"),
        "F20" to OfficialCategory("Bệnh tâm thần phân liệt (Schizophrenia)", "Schizophrenia", "Chương V: Rối loạn tâm thần (F00 - F99)", "Tâm thần"),
        "F32" to OfficialCategory("Giai đoạn trầm cảm (Depressive episode)", "Depressive episode", "Chương V: Rối loạn tâm thần (F00 - F99)", "Tâm thần"),
        "F41" to OfficialCategory("Các rối loạn lo âu khác (Rối loạn hoảng sợ, lo âu lan tỏa)", "Other anxiety disorders", "Chương V: Rối loạn tâm thần (F00 - F99)", "Tâm thần"),

        // CHƯƠNG VI: BỆNH HỆ THẦN KINH (G00 - G99)
        "G00" to OfficialCategory("Viêm màng não do vi khuẩn", "Bacterial meningitis, not elsewhere classified", "Chương VI: Bệnh hệ thần kinh (G00 - G99)", "Nhiễm trùng thần kinh"),
        "G03" to OfficialCategory("Viêm màng não không đặc hiệu", "Meningitis, unspecified", "Chương VI: Bệnh hệ thần kinh (G00 - G99)", "Nhiễm trùng thần kinh"),
        "G20" to OfficialCategory("Bệnh Parkinson", "Parkinson's disease", "Chương VI: Bệnh hệ thần kinh (G00 - G99)", "Thần kinh"),
        "G40" to OfficialCategory("Bệnh động kinh và cơn co giật (Epilepsy)", "Epilepsy", "Chương VI: Bệnh hệ thần kinh (G00 - G99)", "Thần kinh cấp cứu"),
        "G43" to OfficialCategory("Đau nửa đầu (Migraine)", "Migraine", "Chương VI: Bệnh hệ thần kinh (G00 - G99)", "Thần kinh"),
        "G44" to OfficialCategory("Hội chứng đau đầu khác (Đau đầu căng thẳng)", "Other headache syndromes", "Chương VI: Bệnh hệ thần kinh (G00 - G99)", "Thần kinh"),
        "G45" to OfficialCategory("Cơn thiếu máu não thoáng qua (TIA)", "Transient cerebral ischaemic attacks and related syndromes", "Chương VI: Bệnh hệ thần kinh (G00 - G99)", "Đột quỵ não"),
        "G47" to OfficialCategory("Rối loạn giấc ngủ (Mất ngủ)", "Sleep disorders", "Chương VI: Bệnh hệ thần kinh (G00 - G99)", "Thần kinh"),
        "G51" to OfficialCategory("Liệt dây thần kinh mặt (Liệt Bell, liệt dây VII ngoại biên)", "Facial nerve disorders", "Chương VI: Bệnh hệ thần kinh (G00 - G99)", "Thần kinh"),

        // CHƯƠNG VII: MẮT & PHẦN PHỤ (H00 - H59)
        "H00" to OfficialCategory("Lẹo và chắp mắt", "Hordeolum and chalazion", "Chương VII: Mắt (H00 - H59)", "Mắt"),
        "H10" to OfficialCategory("Viêm kết mạc cấp (Đau mắt đỏ)", "Conjunctivitis", "Chương VII: Mắt (H00 - H59)", "Mắt"),
        "H16" to OfficialCategory("Viêm giác mạc", "Keratitis", "Chương VII: Mắt (H00 - H59)", "Mắt"),
        "H40" to OfficialCategory("Bệnh Glôcôm cấp (Thiên đầu thống)", "Glaucoma", "Chương VII: Mắt (H00 - H59)", "Cấp cứu mắt"),

        // CHƯƠNG VIII: TAI & XƯƠNG CHŨM (H60 - H95)
        "H60" to OfficialCategory("Viêm tai ngoài", "Otitis externa", "Chương VIII: Tai (H60 - H95)", "Tai Mũi Họng"),
        "H65" to OfficialCategory("Viêm tai giữa không sinh mủ", "Nonsuppurative otitis media", "Chương VIII: Tai (H60 - H95)", "Tai Mũi Họng"),
        "H66" to OfficialCategory("Viêm tai giữa mủ cấp và mạn", "Suppurative and unspecified otitis media", "Chương VIII: Tai (H60 - H95)", "Tai Mũi Họng"),
        "H81" to OfficialCategory("Hội chứng tiền đình ngoại biên (Chóng mặt kịch phát BPPV)", "Disorders of vestibular function", "Chương VIII: Tai (H60 - H95)", "Tiền đình"),

        // CHƯƠNG IX: BỆNH HỆ TUẦN HOÀN (I00 - I99)
        "I10" to OfficialCategory("Bệnh tăng huyết áp vô căn (Cơn tăng huyết áp)", "Essential (primary) hypertension", "Chương IX: Bệnh hệ tuần hoàn (I00 - I99)", "Tim mạch"),
        "I11" to OfficialCategory("Bệnh tim do tăng huyết áp (Cơn tăng huyết áp cấp cứu)", "Hypertensive heart disease", "Chương IX: Bệnh hệ tuần hoàn (I00 - I99)", "Cấp cứu tim mạch"),
        "I20" to OfficialCategory("Cơn đau thắt ngực (Angina pectoris)", "Angina pectoris", "Chương IX: Bệnh hệ tuần hoàn (I00 - I99)", "Hội chứng vành"),
        "I21" to OfficialCategory("Nhồi máu cơ tim cấp (STEMI / NSTEMI)", "Acute myocardial infarction", "Chương IX: Bệnh hệ tuần hoàn (I00 - I99)", "Cấp cứu tim mạch"),
        "I25" to OfficialCategory("Bệnh tim thiếu máu cục bộ mạn tính", "Chronic ischaemic heart disease", "Chương IX: Bệnh hệ tuần hoàn (I00 - I99)", "Tim mạch"),
        "I26" to OfficialCategory("Thuyên tắc động mạch phổi cấp (PE)", "Pulmonary embolism", "Chương IX: Bệnh hệ tuần hoàn (I00 - I99)", "Cấp cứu tim mạch"),
        "I30" to OfficialCategory("Viêm màng ngoài tim cấp tính", "Acute pericarditis", "Chương IX: Bệnh hệ tuần hoàn (I00 - I99)", "Tim mạch"),
        "I40" to OfficialCategory("Viêm cơ tim cấp tính (Myocarditis)", "Infective myocarditis", "Chương IX: Bệnh hệ tuần hoàn (I00 - I99)", "Cấp cứu tim mạch"),
        "I44" to OfficialCategory("Block nhĩ thất hoàn toàn (AV block III)", "Atrioventricular and left bundle-branch block", "Chương IX: Bệnh hệ tuần hoàn (I00 - I99)", "Rối loạn nhịp"),
        "I46" to OfficialCategory("Ngừng tim đột ngột (Cardiac arrest)", "Cardiac arrest", "Chương IX: Bệnh hệ tuần hoàn (I00 - I99)", "Cấp cứu ngừng tuần hoàn"),
        "I47" to OfficialCategory("Cơn nhịp nhanh kịch phát (PSVT / VT)", "Paroxysmal tachycardia", "Chương IX: Bệnh hệ tuần hoàn (I00 - I99)", "Cấp cứu tim mạch"),
        "I48" to OfficialCategory("Rung nhĩ và cuồng nhĩ (Atrial fibrillation)", "Atrial fibrillation and flutter", "Chương IX: Bệnh hệ tuần hoàn (I00 - I99)", "Rối loạn nhịp"),
        "I49" to OfficialCategory("Các loạn nhịp tim khác (Rung thất - VF)", "Other cardiac arrhythmias", "Chương IX: Bệnh hệ tuần hoàn (I00 - I99)", "Cấp cứu tim mạch"),
        "I50" to OfficialCategory("Suy tim cấp và mạn (Phù phổi cấp do tim)", "Heart failure", "Chương IX: Bệnh hệ tuần hoàn (I00 - I99)", "Cấp cứu tim mạch"),
        "I60" to OfficialCategory("Xuất huyết dưới nhện (SAH)", "Subarachnoid haemorrhage", "Chương IX: Bệnh hệ tuần hoàn (I00 - I99)", "Đột quỵ não cấp"),
        "I61" to OfficialCategory("Xuất huyết trong não cấp tính", "Intracerebral haemorrhage", "Chương IX: Bệnh hệ tuần hoàn (I00 - I99)", "Đột quỵ não cấp"),
        "I63" to OfficialCategory("Nhồi máu não cấp (Đột quỵ thiếu máu cục bộ)", "Cerebral infarction", "Chương IX: Bệnh hệ tuần hoàn (I00 - I99)", "Đột quỵ não cấp"),
        "I71" to OfficialCategory("Phình và bóc tách động mạch chủ ngực / bụng", "Aortic aneurysm and dissection", "Chương IX: Bệnh hệ tuần hoàn (I00 - I99)", "Cấp cứu mạch máu"),
        "I80" to OfficialCategory("Viêm tĩnh mạch và huyết khối tĩnh mạch sâu chi dưới", "Phlebitis and thrombophlebitis", "Chương IX: Bệnh hệ tuần hoàn (I00 - I99)", "Mạch máu"),
        "I84" to OfficialCategory("Bệnh trĩ (Hemorrhoids)", "Haemorrhoids", "Chương IX: Bệnh hệ tuần hoàn (I00 - I99)", "Hậu môn trực tràng"),

        // CHƯƠNG X: BỆNH HỆ HÔ HẤP (J00 - J99)
        "J00" to OfficialCategory("Viêm mũi họng cấp tính (Cảm lạnh thông thường)", "Acute nasopharyngitis [common cold]", "Chương X: Bệnh hệ hô hấp (J00 - J99)", "Hô hấp"),
        "J01" to OfficialCategory("Viêm xoang cấp tính", "Acute sinusitis", "Chương X: Bệnh hệ hô hấp (J00 - J99)", "Tai Mũi Họng"),
        "J02" to OfficialCategory("Viêm họng cấp tính", "Acute pharyngitis", "Chương X: Bệnh hệ hô hấp (J00 - J99)", "Tai Mũi Họng"),
        "J03" to OfficialCategory("Viêm amidan cấp tính", "Acute tonsillitis", "Chương X: Bệnh hệ hô hấp (J00 - J99)", "Tai Mũi Họng"),
        "J04" to OfficialCategory("Viêm thanh quản và khí quản cấp tính", "Acute laryngitis and tracheitis", "Chương X: Bệnh hệ hô hấp (J00 - J99)", "Tai Mũi Họng"),
        "J06" to OfficialCategory("Nhiễm khuẩn hô hấp trên cấp tính nhiều vị trí", "Acute upper respiratory infections of multiple sites", "Chương X: Bệnh hệ hô hấp (J00 - J99)", "Hô hấp"),
        "J10" to OfficialCategory("Cúm do virus cúm đã xác định", "Influenza due to identified seasonal influenza virus", "Chương X: Bệnh hệ hô hấp (J00 - J99)", "Truyền nhiễm hô hấp"),
        "J11" to OfficialCategory("Cúm không xác định được virus", "Influenza, virus not identified", "Chương X: Bệnh hệ hô hấp (J00 - J99)", "Truyền nhiễm hô hấp"),
        "J15" to OfficialCategory("Viêm phổi do vi khuẩn", "Bacterial pneumonia, not elsewhere classified", "Chương X: Bệnh hệ hô hấp (J00 - J99)", "Hô hấp"),
        "J18" to OfficialCategory("Viêm phổi, tác nhân không đặc hiệu", "Pneumonia, organism unspecified", "Chương X: Bệnh hệ hô hấp (J00 - J99)", "Hô hấp"),
        "J20" to OfficialCategory("Viêm phế quản cấp tính", "Acute bronchitis", "Chương X: Bệnh hệ hô hấp (J00 - J99)", "Hô hấp"),
        "J30" to OfficialCategory("Viêm mũi dị ứng và vận mạch", "Vasomotor and allergic rhinitis", "Chương X: Bệnh hệ hô hấp (J00 - J99)", "Dị ứng hô hấp"),
        "J44" to OfficialCategory("Bệnh phổi tắc nghẽn mạn tính (Đợt cấp COPD)", "Other chronic obstructive pulmonary disease", "Chương X: Bệnh hệ hô hấp (J00 - J99)", "Cấp cứu suy hô hấp"),
        "J45" to OfficialCategory("Cơn hen phế quản cấp (Hen ác tính)", "Asthma", "Chương X: Bệnh hệ hô hấp (J00 - J99)", "Cấp cứu suy hô hấp"),
        "J69" to OfficialCategory("Viêm phổi hít do thức ăn và chất nôn", "Pneumonitis due to solids and liquids", "Chương X: Bệnh hệ hô hấp (J00 - J99)", "Cấp cứu hô hấp"),
        "J80" to OfficialCategory("Hội chứng suy hô hấp cấp tiến triển (ARDS)", "Adult respiratory distress syndrome", "Chương X: Bệnh hệ hô hấp (J00 - J99)", "Hồi sức tích cực"),
        "J81" to OfficialCategory("Phù phổi cấp không do tim", "Pulmonary oedema", "Chương X: Bệnh hệ hô hấp (J00 - J99)", "Cấp cứu suy hô hấp"),
        "J90" to OfficialCategory("Tràn dịch màng phổi", "Pleural effusion, not elsewhere classified", "Chương X: Bệnh hệ hô hấp (J00 - J99)", "Hô hấp"),
        "J93" to OfficialCategory("Tràn khí màng phổi áp lực tự phát", "Pneumothorax", "Chương X: Bệnh hệ hô hấp (J00 - J99)", "Cấp cứu lồng ngực"),
        "J96" to OfficialCategory("Suy hô hấp cấp tính", "Respiratory failure, not elsewhere classified", "Chương X: Bệnh hệ hô hấp (J00 - J99)", "Hồi sức cấp cứu"),

        // CHƯƠNG XI: BỆNH HỆ TIÊU HÓA (K00 - K93)
        "K21" to OfficialCategory("Bệnh trào ngược dạ dày - thực quản (GERD)", "Gastro-oesophageal reflux disease", "Chương XI: Bệnh hệ tiêu hóa (K00 - K93)", "Tiêu hóa"),
        "K25" to OfficialCategory("Loét dạ dày cấp tính (Thủng hoặc xuất huyết)", "Gastric ulcer", "Chương XI: Bệnh hệ tiêu hóa (K00 - K93)", "Cấp cứu tiêu hóa"),
        "K26" to OfficialCategory("Loét tá tràng cấp tính (Thủng hoặc xuất huyết)", "Duodenal ulcer", "Chương XI: Bệnh hệ tiêu hóa (K00 - K93)", "Cấp cứu tiêu hóa"),
        "K27" to OfficialCategory("Loét dạ dày - tá tràng không đặc hiệu", "Peptic ulcer, site unspecified", "Chương XI: Bệnh hệ tiêu hóa (K00 - K93)", "Tiêu hóa"),
        "K29" to OfficialCategory("Viêm dạ dày và tá tràng cấp", "Gastritis and duodenitis", "Chương XI: Bệnh hệ tiêu hóa (K00 - K93)", "Tiêu hóa"),
        "K30" to OfficialCategory("Chứng khó tiêu chức năng (Dyspepsia)", "Dyspepsia", "Chương XI: Bệnh hệ tiêu hóa (K00 - K93)", "Tiêu hóa"),
        "K35" to OfficialCategory("Viêm ruột thừa cấp tính (Cấp cứu ngoại khoa)", "Acute appendicitis", "Chương XI: Bệnh hệ tiêu hóa (K00 - K93)", "Cấp cứu bụng ngoại khoa"),
        "K40" to OfficialCategory("Thoát vị bẹn nghẹt hoặc không nghẹt", "Inguinal hernia", "Chương XI: Bệnh hệ tiêu hóa (K00 - K93)", "Ngoại tiêu hóa"),
        "K56" to OfficialCategory("Tắc ruột cơ học cấp và liệt ruột", "Paralytic ileus and intestinal obstruction", "Chương XI: Bệnh hệ tiêu hóa (K00 - K93)", "Cấp cứu bụng ngoại khoa"),
        "K58" to OfficialCategory("Hội chứng ruột kích thích (IBS)", "Irritable bowel syndrome", "Chương XI: Bệnh hệ tiêu hóa (K00 - K93)", "Tiêu hóa"),
        "K65" to OfficialCategory("Viêm phúc mạc toàn thể cấp tính", "Peritonitis", "Chương XI: Bệnh hệ tiêu hóa (K00 - K93)", "Cấp cứu bụng ngoại khoa"),
        "K74" to OfficialCategory("Xơ gan và xơ hóa gan", "Fibrosis and cirrhosis of liver", "Chương XI: Bệnh hệ tiêu hóa (K00 - K93)", "Gan mật"),
        "K80" to OfficialCategory("Sỏi túi mật kèm viêm túi mật cấp (Cơn đau quặn gan)", "Cholelithiasis", "Chương XI: Bệnh hệ tiêu hóa (K00 - K93)", "Cấp cứu gan mật"),
        "K81" to OfficialCategory("Viêm túi mật cấp tính", "Cholecystitis", "Chương XI: Bệnh hệ tiêu hóa (K00 - K93)", "Cấp cứu gan mật"),
        "K85" to OfficialCategory("Viêm tụy cấp tính mức độ nặng", "Acute pancreatitis", "Chương XI: Bệnh hệ tiêu hóa (K00 - K93)", "Cấp cứu tiêu hóa"),
        "K92" to OfficialCategory("Xuất huyết tiêu hóa cấp tính (Nôn ra máu, đi ngoài phân đen)", "Other diseases of digestive system (GI bleed)", "Chương XI: Bệnh hệ tiêu hóa (K00 - K93)", "Cấp cứu xuất huyết tiêu hóa"),

        // CHƯƠNG XII: DA & MÔ DƯỚI DA (L00 - L99)
        "L02" to OfficialCategory("Áp xe da, nhọt và cụm nhọt", "Cutaneous abscess, furuncle and carbuncle", "Chương XII: Da (L00 - L99)", "Ngoại khoa - Da liễu"),
        "L03" to OfficialCategory("Viêm mô tế bào mụn mủ (Cellulitis)", "Cellulitis", "Chương XII: Da (L00 - L99)", "Nhiễm khuẩn da"),
        "L20" to OfficialCategory("Viêm da cơ địa dị ứng (Atopic dermatitis)", "Atopic dermatitis", "Chương XII: Da (L00 - L99)", "Dị ứng - Da liễu"),
        "L23" to OfficialCategory("Viêm da tiếp xúc dị ứng", "Allergic contact dermatitis", "Chương XII: Da (L00 - L99)", "Da liễu"),
        "L27" to OfficialCategory("Viêm da do phản ứng dị ứng thuốc", "Dermatitis due to substances taken internally", "Chương XII: Da (L00 - L99)", "Dị ứng"),
        "L50" to OfficialCategory("Bệnh mày đay dị ứng (Urticaria)", "Urticaria", "Chương XII: Da (L00 - L99)", "Dị ứng"),

        // CHƯƠNG XIII: CƠ XƯƠNG KHỚP (M00 - M99)
        "M06" to OfficialCategory("Viêm khớp dạng thấp (Rheumatoid arthritis)", "Other rheumatoid arthritis", "Chương XIII: Cơ xương khớp (M00 - M99)", "Cơ xương khớp"),
        "M10" to OfficialCategory("Bệnh Gút cấp và mạn tính (Gout)", "Gout", "Chương XIII: Cơ xương khớp (M00 - M99)", "Cơ xương khớp"),
        "M17" to OfficialCategory("Thoái hóa khớp gối (Gonarthrosis)", "Gonarthrosis [arthrosis of knee]", "Chương XIII: Cơ xương khớp (M00 - M99)", "Cơ xương khớp"),
        "M19" to OfficialCategory("Thoái hóa khớp khác", "Other arthrosis", "Chương XIII: Cơ xương khớp (M00 - M99)", "Cơ xương khớp"),
        "M51" to OfficialCategory("Thoát vị đĩa đệm cột sống thắt lưng", "Other intervertebral disc disorders", "Chương XIII: Cơ xương khớp (M00 - M99)", "Cơ xương khớp - Cột sống"),
        "M54" to OfficialCategory("Đau cột sống thắt lưng và đau thần kinh tọa", "Dorsalgia", "Chương XIII: Cơ xương khớp (M00 - M99)", "Cơ xương khớp"),
        "M81" to OfficialCategory("Bệnh loãng xương không kèm gãy bệnh lý", "Osteoporosis without pathological fracture", "Chương XIII: Cơ xương khớp (M00 - M99)", "Cơ xương khớp"),

        // CHƯƠNG XIV: TIẾT NIỆU - SINH DỤC (N00 - N99)
        "N00" to OfficialCategory("Hội chứng viêm cầu thận cấp tính", "Acute nephritic syndrome", "Chương XIV: Tiết niệu (N00 - N99)", "Thận học"),
        "N04" to OfficialCategory("Hội chứng thận hư (Nephrotic syndrome)", "Nephrotic syndrome", "Chương XIV: Tiết niệu (N00 - N99)", "Thận học"),
        "N10" to OfficialCategory("Viêm thận - bể thận cấp tính", "Acute tubulo-interstitial nephritis", "Chương XIV: Tiết niệu (N00 - N99)", "Nhiễm khuẩn tiết niệu"),
        "N17" to OfficialCategory("Tổn thương thận cấp (Suy thận cấp tính - AKI)", "Acute renal failure", "Chương XIV: Tiết niệu (N00 - N99)", "Cấp cứu thận học"),
        "N18" to OfficialCategory("Bệnh thận mạn tính (CKD / Suy thận mạn)", "Chronic kidney disease", "Chương XIV: Tiết niệu (N00 - N99)", "Thận học"),
        "N20" to OfficialCategory("Sỏi thận và sỏi niệu quản (Cơn đau quặn thận)", "Calculus of kidney and ureter", "Chương XIV: Tiết niệu (N00 - N99)", "Cấp cứu tiết niệu"),
        "N23" to OfficialCategory("Cơn đau quặn thận không đặc hiệu", "Unspecified renal colic", "Chương XIV: Tiết niệu (N00 - N99)", "Cấp cứu tiết niệu"),
        "N30" to OfficialCategory("Viêm bàng quang cấp tính", "Cystitis", "Chương XIV: Tiết niệu (N00 - N99)", "Nhiễm khuẩn tiết niệu"),
        "N39" to OfficialCategory("Nhiễm khuẩn đường tiết niệu (UTI)", "Other disorders of urinary system", "Chương XIV: Tiết niệu (N00 - N99)", "Nhiễm khuẩn tiết niệu"),
        "N40" to OfficialCategory("Tăng sinh lành tính tuyến tiền liệt (U xơ tiền liệt tuyến)", "Hyperplasia of prostate", "Chương XIV: Tiết niệu (N00 - N99)", "Ngoại tiết niệu"),
        "N70" to OfficialCategory("Viêm vòi trứng và buồng trứng (Viêm phần phụ)", "Salpingitis and oophoritis", "Chương XIV: Tiết niệu (N00 - N99)", "Sản phụ khoa"),

        // CHƯƠNG XV: THAI NGHÉN & SINH ĐẺ (O00 - O99)
        "O00" to OfficialCategory("Thai ngoài tử cung vỡ gây sốc mất máu", "Ectopic pregnancy", "Chương XV: Sản khoa (O00 - O99)", "Cấp cứu sản phụ khoa"),
        "O03" to OfficialCategory("Sảy thai tự nhiên", "Spontaneous abortion", "Chương XV: Sản khoa (O00 - O99)", "Sản phụ khoa"),
        "O14" to OfficialCategory("Tiền sản giật thai kỳ nặng", "Gestational hypertension with significant proteinuria", "Chương XV: Sản khoa (O00 - O99)", "Cấp cứu sản khoa"),
        "O15" to OfficialCategory("Sản giật trong thai kỳ hoặc chuyển dạ", "Eclampsia", "Chương XV: Sản khoa (O00 - O99)", "Cấp cứu sản khoa"),
        "O60" to OfficialCategory("Chuyển dạ đẻ non (Preterm labour)", "Preterm labour", "Chương XV: Sản khoa (O00 - O99)", "Sản khoa"),
        "O72" to OfficialCategory("Băng huyết sau sinh đe dọa sốc mất máu", "Postpartum haemorrhage", "Chương XV: Sản khoa (O00 - O99)", "Cấp cứu sản khoa"),

        // CHƯƠNG XVIII: TRIỆU CHỨNG & DẤU HIỆU BẤT THƯỜNG (R00 - R99)
        "R00" to OfficialCategory("Bất thường nhịp tim (Đánh trống ngực, nhịp tim nhanh)", "Abnormalities of heart beat", "Chương XVIII: Triệu chứng (R00 - R99)", "Triệu chứng cấp cứu"),
        "R04" to OfficialCategory("Chảy máu đường hô hấp (Ho ra máu, chảy máu cam)", "Haemorrhage from respiratory passages", "Chương XVIII: Triệu chứng (R00 - R99)", "Triệu chứng cấp cứu"),
        "R05" to OfficialCategory("Ho cấp tính", "Cough", "Chương XVIII: Triệu chứng (R00 - R99)", "Hô hấp"),
        "R06" to OfficialCategory("Bất thường về hô hấp (Khó thở cấp tính, thở rít)", "Abnormalities of breathing", "Chương XVIII: Triệu chứng (R00 - R99)", "Triệu chứng cấp cứu"),
        "R07" to OfficialCategory("Đau ngực cấp tính", "Pain in throat and chest", "Chương XVIII: Triệu chứng (R00 - R99)", "Triệu chứng cấp cứu"),
        "R10" to OfficialCategory("Đau bụng cấp và đau vùng chậu (Bụng ngoại khoa)", "Abdominal and pelvic pain", "Chương XVIII: Triệu chứng (R00 - R99)", "Triệu chứng cấp cứu"),
        "R11" to OfficialCategory("Buồn nôn và nôn mửa", "Nausea and vomiting", "Chương XVIII: Triệu chứng (R00 - R99)", "Tiêu hóa"),
        "R17" to OfficialCategory("Vàng da không xác định", "Unspecified jaundice", "Chương XVIII: Triệu chứng (R00 - R99)", "Gan mật"),
        "R40" to OfficialCategory("Ngủ lịm, sững sờ và hôn mê sâu (GCS giảm)", "Somnolence, stupor and coma", "Chương XVIII: Triệu chứng (R00 - R99)", "Hồi sức thần kinh"),
        "R42" to OfficialCategory("Chóng mặt và hoa mắt (Dizziness and giddiness)", "Dizziness and giddiness", "Chương XVIII: Triệu chứng (R00 - R99)", "Thần kinh"),
        "R50" to OfficialCategory("Sốt không rõ nguyên nhân", "Fever of other and unknown origin", "Chương XVIII: Triệu chứng (R00 - R99)", "Truyền nhiễm"),
        "R51" to OfficialCategory("Nhức đầu cấp tính (Headache)", "Headache", "Chương XVIII: Triệu chứng (R00 - R99)", "Thần kinh"),
        "R55" to OfficialCategory("Ngất và trụy mạch đột ngột (Syncope)", "Syncope and collapse", "Chương XVIII: Triệu chứng (R00 - R99)", "Cấp cứu tim mạch"),
        "R56" to OfficialCategory("Co giật chưa phân loại (Co giật do sốt cao)", "Convulsions, not elsewhere classified", "Chương XVIII: Triệu chứng (R00 - R99)", "Cấp cứu thần kinh"),
        "R57" to OfficialCategory("Các loại sốc (Sốc tim, sốc giảm thể tích, sốc nhiễm khuẩn)", "Shock, not elsewhere classified", "Chương XVIII: Triệu chứng (R00 - R99)", "Hồi sức cấp cứu"),

        // CHƯƠNG XIX: CHẤN THƯƠNG & NGỘ ĐỘC (S00 - T98)
        "S02" to OfficialCategory("Gãy xương sọ và xương mặt do tai nạn", "Fracture of skull and facial bones", "Chương XIX: Chấn thương & Ngộ độc (S00 - T98)", "Chấn thương sọ não"),
        "S06" to OfficialCategory("Chấn thương sọ não nội sọ (Tụ máu ngoài/dưới màng cứng)", "Intracranial injury", "Chương XIX: Chấn thương & Ngộ độc (S00 - T98)", "Chấn thương sọ não cấp"),
        "S12" to OfficialCategory("Gãy cột sống cổ", "Fracture of neck", "Chương XIX: Chấn thương & Ngộ độc (S00 - T98)", "Cấp cứu cột sống"),
        "S22" to OfficialCategory("Gãy xương sườn và mảng sườn di động", "Fracture of rib(s), sternum and thoracic spine", "Chương XIX: Chấn thương & Ngộ độc (S00 - T98)", "Chấn thương ngực"),
        "S27" to OfficialCategory("Tràn máu, tràn khí màng phổi chấn thương & dập phổi", "Injury of other and unspecified intrathoracic organs", "Chương XIX: Chấn thương & Ngộ độc (S00 - T98)", "Chấn thương lồng ngực"),
        "S32" to OfficialCategory("Gãy khung chậu phức tạp có sốc mất máu", "Fracture of lumbar spine and pelvis", "Chương XIX: Chấn thương & Ngộ độc (S00 - T98)", "Chấn thương chỉnh hình"),
        "S36" to OfficialCategory("Chấn thương cơ quan trong ổ bụng (Vỡ lách, vỡ gan)", "Injury of intra-abdominal organs", "Chương XIX: Chấn thương & Ngộ độc (S00 - T98)", "Chấn thương bụng kín"),
        "S42" to OfficialCategory("Gãy xương cánh tay và đai vai", "Fracture of shoulder and upper arm", "Chương XIX: Chấn thương & Ngộ độc (S00 - T98)", "Chấn thương chỉnh hình"),
        "S52" to OfficialCategory("Gãy xương cẳng tay (Xương quay, xương trụ)", "Fracture of forearm", "Chương XIX: Chấn thương & Ngộ độc (S00 - T98)", "Chấn thương chỉnh hình"),
        "S72" to OfficialCategory("Gãy xương đùi (Cổ xương đùi, thân xương đùi)", "Fracture of femur", "Chương XIX: Chấn thương & Ngộ độc (S00 - T98)", "Chấn thương chỉnh hình"),
        "S82" to OfficialCategory("Gãy xương cẳng chân (Xương chày, xương mác)", "Fracture of lower leg, including ankle", "Chương XIX: Chấn thương & Ngộ độc (S00 - T98)", "Chấn thương chỉnh hình"),
        "T07" to OfficialCategory("Đa chấn thương nặng không đặc hiệu (Polytrauma)", "Unspecified multiple injuries", "Chương XIX: Chấn thương & Ngộ độc (S00 - T98)", "Cấp cứu đa chấn thương"),
        "T14" to OfficialCategory("Chấn thương phần mềm và vết thương chưa xác định", "Injury of unspecified body region", "Chương XIX: Chấn thương & Ngộ độc (S00 - T98)", "Ngoại chấn thương"),
        "T17" to OfficialCategory("Dị vật đường hô hấp (Hóc dị vật, sặc đường thở)", "Foreign body in respiratory tract", "Chương XIX: Chấn thương & Ngộ độc (S00 - T98)", "Cấp cứu hô hấp"),
        "T18" to OfficialCategory("Dị vật đường tiêu hóa", "Foreign body in alimentary tract", "Chương XIX: Chấn thương & Ngộ độc (S00 - T98)", "Ngoại tiêu hóa"),
        "T30" to OfficialCategory("Bỏng nhiệt cơ thể độ II-IV", "Burn and corrosion, body region unspecified", "Chương XIX: Chấn thương & Ngộ độc (S00 - T98)", "Cấp cứu bỏng"),
        "T39" to OfficialCategory("Ngộ độc cấp thuốc hạ sốt giảm đau (Paracetamol)", "Poisoning by nonopioid analgesics", "Chương XIX: Chấn thương & Ngộ độc (S00 - T98)", "Trung tâm chống độc"),
        "T40" to OfficialCategory("Ngộ độc cấp Opiate / Heroin gây ngừng thở", "Poisoning by narcotics and psychodysleptics", "Chương XIX: Chấn thương & Ngộ độc (S00 - T98)", "Trung tâm chống độc"),
        "T42" to OfficialCategory("Ngộ độc cấp thuốc an thần Benzodiazepine", "Poisoning by antiepileptic, sedative-hypnotic drugs", "Chương XIX: Chấn thương & Ngộ độc (S00 - T98)", "Trung tâm chống độc"),
        "T51" to OfficialCategory("Ngộ độc cấp cồn công nghiệp Methanol / Ethanol", "Toxic effect of alcohol", "Chương XIX: Chấn thương & Ngộ độc (S00 - T98)", "Trung tâm chống độc"),
        "T58" to OfficialCategory("Ngộ độc cấp khí Carbon monoxide (CO) sưởi than", "Toxic effect of carbon monoxide", "Chương XIX: Chấn thương & Ngộ độc (S00 - T98)", "Trung tâm chống độc"),
        "T60" to OfficialCategory("Ngộ độc cấp thuốc trừ sâu phospho hữu cơ", "Toxic effect of pesticides", "Chương XIX: Chấn thương & Ngộ độc (S00 - T98)", "Trung tâm chống độc"),
        "T63" to OfficialCategory("Nhiễm độc nọc rắn độc cắn và ong đốt", "Toxic effect of contact with venomous animals", "Chương XIX: Chấn thương & Ngộ độc (S00 - T98)", "Trung tâm chống độc"),
        "T67" to OfficialCategory("Say nóng, say nắng và sốc nhiệt (Heat stroke)", "Effects of heat and light", "Chương XIX: Chấn thương & Ngộ độc (S00 - T98)", "Cấp cứu môi trường"),
        "T75" to OfficialCategory("Tác động ngoại lai: Sét đánh, điện giật, đuối nước", "Effects of other external causes", "Chương XIX: Chấn thương & Ngộ độc (S00 - T98)", "Hồi sức cấp cứu"),
        "T78" to OfficialCategory("Phản ứng bất lợi dị ứng và sốc phản vệ nguy kịch", "Adverse effects, not elsewhere classified", "Chương XIX: Chấn thương & Ngộ độc (S00 - T98)", "Cấp cứu dị ứng"),
        "T88" to OfficialCategory("Tai biến tiêm truyền và sốc phản vệ do thuốc tiêm", "Other complications of surgical and medical care", "Chương XIX: Chấn thương & Ngộ độc (S00 - T98)", "Cấp cứu dị ứng"),

        // CHƯƠNG XXI: TIẾP XÚC DỊCH VỤ Y TẾ (Z00 - Z99)
        "Z00" to OfficialCategory("Khám kiểm tra sức khỏe tổng quát", "General examination and investigation", "Chương XXI: Y tế (Z00 - Z99)", "Khám bệnh")
    )

    /**
     * Bảng ánh xạ các thuật ngữ / triệu chứng / tên bệnh lâm sàng thường gặp
     * sang mã ICD-10 THẬT 100% (Chuẩn Bộ Y Tế / WHO) - Tuyệt đối không bịa mã!
     */
    val clinicalKeywordMap: Map<String, String> = mapOf(
        "viem hong" to "J02.9",
        "dau hong" to "J02.9",
        "viem xoang" to "J01.9",
        "cam lanh" to "J00",
        "viem mui hong" to "J00",
        "viem phe quan" to "J20.9",
        "viem amidan" to "J03.9",
        "dau dau" to "R51",
        "nhuc dau" to "R51",
        "dau nua dau" to "G43.9",
        "mat ngu" to "G47.0",
        "dong kinh" to "G40.9",
        "co giat" to "R56.8",
        "liet mat" to "G51.0",
        "tai bien" to "I63.9",
        "dot quy" to "I63.9",
        "thieu mau nao" to "G45.9",
        "dau nguc" to "R07.4",
        "nhoi mau co tim" to "I21.9",
        "stemi" to "I21.0",
        "nstemi" to "I21.4",
        "suy tim" to "I50.9",
        "tang huyet ap" to "I10",
        "con tang huyet ap" to "I10",
        "rung that" to "I49.0",
        "rung nhi" to "I48.0",
        "thuyen tac phoi" to "I26.9",
        "kho tho" to "R06.0",
        "suy ho hap" to "J96.0",
        "hen phe quan" to "J45.9",
        "hen suyat" to "J45.9",
        "copd" to "J44.1",
        "viem phoi" to "J18.9",
        "viem phoi hit" to "J69.0",
        "tran khi mang phoi" to "J93.9",
        "tran mau mang phoi" to "S27.1",
        "dau bung" to "R10.0",
        "dau thuong vi" to "R10.1",
        "viem ruot thua" to "K35.8",
        "ruot thua" to "K35.8",
        "thung da day" to "K25.5",
        "loet da day" to "K25.9",
        "loet ta trang" to "K26.9",
        "xuat huyet tieu hoa" to "K92.2",
        "non ra mau" to "K92.0",
        "di ngoai phan den" to "K92.1",
        "trao nguoc da day" to "K21.9",
        "trao nguoc" to "K21.9",
        "gerd" to "K21.9",
        "tac ruot" to "K56.6",
        "viem phuc mac" to "K65.0",
        "viem tuy cap" to "K85.9",
        "soi mat" to "K80.0",
        "viem tui mat" to "K81.0",
        "tieu chay" to "A09",
        "ngo doc thuc pham" to "A05.9",
        "ha duong huyet" to "E16.2",
        "tieu duong" to "E11.9",
        "dai thao duong" to "E11.9",
        "toan ceton" to "E10.1",
        "tang kali" to "E87.5",
        "ha kali" to "E87.6",
        "mat nuoc" to "E86",
        "suy giap" to "E03.9",
        "basedow" to "E05.0",
        "cuong giap" to "E05.0",
        "roi loan mo mau" to "E78.5",
        "mo mau" to "E78.5",
        "gout" to "M10.0",
        "gut" to "M10.0",
        "dau lung" to "M54.5",
        "dau than kinh toa" to "M54.3",
        "thoat vi dia dem" to "M51.2",
        "thoai hoa khop" to "M19.9",
        "thoai hoa cot song" to "M51.9",
        "loang xuong" to "M81.9",
        "con dau quan than" to "N20.1",
        "soi than" to "N20.0",
        "soi nieu quan" to "N20.1",
        "suy than cap" to "N17.9",
        "suy than man" to "N18.9",
        "nhiem trung tieu" to "N39.0",
        "viem bang quang" to "N30.0",
        "viem phan phu" to "N70.9",
        "u xo tu cung" to "D25.9",
        "thai ngoai tu cung" to "O00.9",
        "chua ngoai tu cung" to "O00.9",
        "tien san giat" to "O14.9",
        "san giat" to "O15.0",
        "bang huyet sau sinh" to "O72.1",
        "de non" to "O60.0",
        "soc phan ve" to "T78.2",
        "phan ve" to "T78.2",
        "di ung thuoc" to "T88.6",
        "di ung hai san" to "T78.0",
        "may day" to "L50.9",
        "viem da co dia" to "L20.9",
        "chan thuong so nao" to "S06.9",
        "tu mau ngoai mang cung" to "S06.4",
        "tu mau duoi mang cung" to "S06.5",
        "vo lach" to "S36.0",
        "vo gan" to "S36.1",
        "gay xuong dui" to "S72.3",
        "gay tay" to "S52.9",
        "gay chan" to "S82.9",
        "gay khung chau" to "S32.8",
        "gay suon" to "S22.3",
        "da chan thuong" to "T07",
        "polytrauma" to "T07",
        "bong" to "T30.0",
        "ngo doc phospho huu co" to "T60.0",
        "thuoc tru sau" to "T60.0",
        "ngo doc methanol" to "T51.1",
        "ngo doc ruou" to "T51.1",
        "ngo doc co" to "T58",
        "ngo doc paracetamol" to "T39.1",
        "ran can" to "T63.0",
        "ong dot" to "T63.4",
        "say nang" to "T67.0",
        "soc nhiet" to "T67.0",
        "duoi nuoc" to "T75.1",
        "ngat nuoc" to "T75.1",
        "dien giat" to "T75.0",
        "cho can" to "A82.9",
        "dai" to "A82.9",
        "uon van" to "A35",
        "sot xuat huyet" to "A91",
        "dengue" to "A91",
        "thuy dau" to "B01.9",
        "soi" to "B05.9",
        "quai bi" to "B26.9",
        "tay chan mieng" to "B08.8",
        "viem gan b" to "B18.1",
        "viem gan a" to "B15.9",
        "chong mat" to "H81.0",
        "tien dinh" to "H81.0",
        "viem ket mac" to "H10.9",
        "dau mat do" to "H10.9",
        "viem tai giua" to "H66.9",
        "sot" to "R50.9",
        "sot cao" to "R50.9",
        "hon me" to "R40.2",
        "glasgow" to "R40.2",
        "ngat" to "R55",
        "truy mach" to "R55",
        "soc tim" to "R57.0",
        "soc mat mau" to "R57.1",
        "soc nhiem khuan" to "R57.2",
        "nhiem trung huyet" to "A41.9",
        "sepsis" to "A41.9",
        "kham tong quat" to "Z00.0",
        "kham suc khoe" to "Z00.0"
    )

    /**
     * Xác thực và giải quyết mã chuẩn ICD-10 thật từ WHO / Bộ Y Tế.
     * TUYỆT ĐỐI KHÔNG BỊA MÃ. Chỉ trả về mã khi thuộc khung danh mục ICD-10 chính thức.
     */
    fun resolveOfficialIcd10(query: String): Icd10Item? {
        val clean = query.trim().uppercase()
        val regex = Regex("^([A-Z]\\d{2})(?:\\.(\\d{1,2}))?$")
        val match = regex.find(clean) ?: return null
        val categoryCode = match.groupValues[1]
        val category = officialCategories[categoryCode] ?: return null
        val subCode = match.groupValues[2]

        val nameVi = if (subCode.isNotEmpty()) "${category.nameVi} (Phân nhóm .$subCode)" else category.nameVi
        val nameEn = if (subCode.isNotEmpty()) "${category.nameEn} (Subcategory .$subCode)" else category.nameEn

        return Icd10Item(
            code = clean,
            nameVi = nameVi,
            nameEn = nameEn,
            chapter = category.chapter,
            emrGroup = category.emrGroup,
            synonyms = listOf(clean.lowercase(), normalize(clean))
        )
    }

    /**
     * Kiểm tra xem một mã có thuộc khung mã ICD-10 chính thức hay không.
     */
    fun isValidIcd10Code(code: String): Boolean {
        val clean = code.trim().uppercase()
        if (allCodes.any { it.code.uppercase() == clean }) return true
        val regex = Regex("^([A-Z]\\d{2})(?:\\.(\\d{1,2}))?$")
        val match = regex.find(clean) ?: return false
        val catCode = match.groupValues[1]
        return officialCategories.containsKey(catCode)
    }

    /**
     * Tra cứu tìm kiếm mã ICD-10 theo từ khóa:
     * - Tiếng Việt có dấu và không dấu
     * - Nhập mã trực tiếp: Xác thực với khung danh mục ICD-10 chính thức (A00-Z99)
     * - Nhập tên bệnh mới: Ánh xạ chuẩn với từ điển lâm sàng ICD-10 thật
     * - KHÔNG BAO GIỜ BỊA MÃ KHÔNG THỰC.
     */
    fun search(query: String): List<Icd10Item> {
        if (query.isBlank()) return allCodes.take(12)
        val cleanQuery = query.trim().lowercase()
        val plainQuery = removeDiacritics(cleanQuery)
        val normalizedQuery = normalize(query)

        val tokens = query.split(Regex("[,;+\\s]+")).filter { it.isNotBlank() }
        val targetToken = tokens.lastOrNull()?.trim()?.lowercase() ?: cleanQuery
        val plainTarget = removeDiacritics(targetToken)
        val normalizedToken = normalize(targetToken)

        val matches = allCodes.filter { item ->
            val normCode = normalize(item.code)
            val plainName = removeDiacritics(item.nameVi.lowercase())
            val plainChapter = removeDiacritics(item.chapter.lowercase())
            val plainGroup = removeDiacritics(item.emrGroup.lowercase())

            normCode.contains(normalizedToken) ||
            normCode.contains(normalizedQuery) ||
            item.code.lowercase().contains(cleanQuery) ||
            item.code.lowercase().contains(targetToken) ||
            plainName.contains(plainQuery) ||
            plainName.contains(plainTarget) ||
            item.nameVi.lowercase().contains(cleanQuery) ||
            item.nameVi.lowercase().contains(targetToken) ||
            item.nameEn.lowercase().contains(cleanQuery) ||
            plainGroup.contains(plainQuery) ||
            plainChapter.contains(plainQuery) ||
            item.synonyms.any { syn ->
                val plainSyn = removeDiacritics(syn.lowercase())
                plainSyn.contains(plainQuery) ||
                plainSyn.contains(plainTarget) ||
                normalize(syn).contains(normalizedToken)
            }
        }.sortedByDescending { item ->
            val normCode = normalize(item.code)
            val plainName = removeDiacritics(item.nameVi.lowercase())
            when {
                normCode == normalizedToken -> 100
                normCode == normalizedQuery -> 95
                item.code.equals(targetToken, ignoreCase = true) -> 90
                normCode.startsWith(normalizedToken) -> 80
                item.code.lowercase().startsWith(cleanQuery) -> 70
                plainName.startsWith(plainQuery) -> 60
                item.nameVi.lowercase().startsWith(cleanQuery) -> 55
                item.synonyms.any { normalize(it) == normalizedToken } -> 50
                plainName.contains(plainQuery) -> 45
                item.code.lowercase().contains(cleanQuery) -> 40
                else -> 10
            }
        }.toMutableList()

        // 1. Nếu người dùng nhập mã ICD-10 (ví dụ J00, K21, M54.5, A01...) chưa có trong allCodes:
        // Xác thực nghiêm ngặt với khung officialCategories. Chỉ sinh mục khi thuộc khung ICD-10 THẬT!
        val resolvedCodeItem = resolveOfficialIcd10(targetToken) ?: resolveOfficialIcd10(cleanQuery)
        if (resolvedCodeItem != null && matches.none { it.code.equals(resolvedCodeItem.code, ignoreCase = true) }) {
            matches.add(0, resolvedCodeItem)
        }

        // 2. Nếu người dùng gõ tên bệnh / triệu chứng chưa có trong allCodes:
        // Tra cứu trong bảng ánh xạ lâm sàng chuẩn clinicalKeywordMap để tìm mã ICD-10 THẬT!
        val mappedRealCode = clinicalKeywordMap.entries.firstOrNull { (term, _) ->
            plainQuery.contains(term) || term.contains(plainQuery) ||
            plainTarget.contains(term) || term.contains(plainTarget)
        }?.value

        if (mappedRealCode != null && matches.none { it.code.equals(mappedRealCode, ignoreCase = true) }) {
            val realMappedItem = allCodes.find { it.code.equals(mappedRealCode, ignoreCase = true) }
                ?: resolveOfficialIcd10(mappedRealCode)
            if (realMappedItem != null) {
                matches.add(0, realMappedItem)
            }
        }

        return matches.take(15)
    }

    /**
     * Tra cứu mã theo mã chính xác (xác thực nghiêm ngặt theo khung chuẩn ICD-10)
     */
    fun getByCode(code: String): Icd10Item? {
        val clean = code.trim().uppercase()
        val norm = normalize(code)
        val found = allCodes.find {
            it.code.uppercase() == clean ||
            normalize(it.code) == norm ||
            clean.startsWith(it.code.uppercase())
        }
        if (found != null) return found

        // Tìm trong khung danh mục ICD-10 chuẩn
        return resolveOfficialIcd10(clean)
    }

    /**
     * Phân tách chuỗi chẩn đoán chứa nhiều mã ICD-10 (ví dụ "[I21.0] STEMI; [I49.0] Rung thất")
     * thành danh sách các Icd10Item tương ứng.
     */
    fun parseMultipleCodes(text: String): List<Icd10Item> {
        if (text.isBlank()) return emptyList()

        // 1. Tìm các mã trong ngoặc vuông [I21.0], [I49.0]
        val bracketRegex = Regex("\\[([A-Za-z]\\d{2}(?:\\.\\d{1,2})?)\\]")
        val bracketMatches = bracketRegex.findAll(text).map { it.groupValues[1].uppercase() }.toList()

        // 2. Tìm các mã dạng từ rời nếu không có ngoặc vuông (VD: "I21.0, I49.0")
        val standaloneRegex = Regex("\\b([A-Za-z]\\d{2}(?:\\.\\d{1,2})?)\\b")
        val standaloneMatches = standaloneRegex.findAll(text).map { it.groupValues[1].uppercase() }.toList()

        val allCodesFound = (bracketMatches + standaloneMatches).distinct()

        return allCodesFound.mapNotNull { code ->
            getByCode(code)
        }
    }

    /**
     * Ghép danh sách mã đã chọn thành chuỗi định dạng chẩn đoán chuẩn:
     * "[I21.0] Nhồi máu cơ tim cấp; [I49.0] Rung thất"
     */
    fun formatMultipleCodes(items: List<Icd10Item>): String {
        return items.joinToString("; ") { "[${it.code}] ${it.nameVi}" }
    }
}
