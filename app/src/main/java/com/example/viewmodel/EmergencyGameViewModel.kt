package com.example.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.ai.CaseAiEngine
import com.example.ai.GeminiClinicalService
import com.example.audio.HospitalAudioSynthesizer
import com.example.data.EmergencyRepository
import com.example.data.SavedShiftData
import com.example.model.*
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

enum class AppScreen {
    START,
    DONNING_ANIMATION,
    IN_SHIFT
}

data class EmergencyGameState(
    val currentScreen: AppScreen = AppScreen.START,
    val currentCase: PatientCase? = null,
    val currentStep: Int = 1, // 1 to 5 (Streamlined: 1. Khám & Bệnh sử AI, 2. Cận lâm sàng, 3. Chẩn đoán, 4. Xử trí, 5. Giao ban)
    val currentVitals: VitalSigns = VitalSigns(80, 120, 80, 98, 16, 36.8f, 15, EcgRhythm.NORMAL_SINUS),
    val patientStability: Int = 55, // 0 to 100% Stability Gauge
    val remainingSeconds: Int = 720,
    val isSoundMuted: Boolean = false,
    val chatMessages: List<ChatMessage> = emptyList(),
    val isAiChatResponding: Boolean = false,
    val examinedSystems: Set<String> = emptySet(),
    val orderedLabIds: Set<String> = emptySet(),
    val primaryDx: String = "",
    val diffDxList: List<String> = emptyList(),
    val reasoningText: String = "",
    val executedOrderIds: Set<String> = emptySet(),
    val isCardiacArrest: Boolean = false,
    val isRescued: Boolean = false,
    val showGratitudeScene: Boolean = false,
    val latestNurseCallout: String = "Điều dưỡng Thảo: 'Bác sĩ ơi, bệnh nhân vừa nhập viện trong tình trạng nguy kịch!'",
    val clinicalLogs: List<String> = emptyList(),
    val aiClinicalMentorComment: String? = null,
    val isAiAnalyzing: Boolean = false,
    val showDefibModal: Boolean = false,
    val showCprModal: Boolean = false,
    val savedShift: SavedShiftData? = null,
    val patientsTreated: Int = 0,
    val patientsSaved: Int = 0,
    val reputationScore: Int = 100
)

class EmergencyGameViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = EmergencyRepository(application)
    private val _state = MutableStateFlow(EmergencyGameState())
    val state: StateFlow<EmergencyGameState> = _state.asStateFlow()

    private var timerJob: Job? = null
    private val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())

    init {
        loadInitialData()
    }

    private fun loadInitialData() {
        val saved = repository.loadSavedShiftData()
        _state.value = _state.value.copy(
            savedShift = saved,
            patientsTreated = repository.getPatientsTreated(),
            patientsSaved = repository.getPatientsSaved(),
            reputationScore = repository.getReputation()
        )
    }

    fun startNewShiftSequence() {
        val nextCase = repository.getNextCase(_state.value.currentCase?.id)
        val initialCallout = "Điều dưỡng Thảo: 'Bác sĩ ơi, bệnh nhân ${nextCase.patientName} (${nextCase.age}t) vừa vào cấp cứu: ${nextCase.chiefComplaint}!'"
        val initialLogs = listOf("${timeFormat.format(Date())} - Tiếp nhận ca cấp cứu: ${nextCase.chiefComplaint}")

        // Seed initial message from patient
        val initialChat = listOf(
            ChatMessage("msg_0", nextCase.patientName, "Bác sĩ ơi, tôi mệt và khó thở quá, cứu tôi với!", isDoctor = false, timeFormat.format(Date()))
        )

        _state.value = _state.value.copy(
            currentScreen = AppScreen.DONNING_ANIMATION,
            currentCase = nextCase,
            currentStep = 1,
            currentVitals = nextCase.initialVitals,
            patientStability = 50,
            remainingSeconds = nextCase.timeLimitMinutes * 60,
            chatMessages = initialChat,
            isAiChatResponding = false,
            examinedSystems = emptySet(),
            orderedLabIds = emptySet(),
            primaryDx = "",
            diffDxList = emptyList(),
            reasoningText = "",
            executedOrderIds = emptySet(),
            isCardiacArrest = nextCase.initialVitals.rhythm == EcgRhythm.VFIB || nextCase.initialVitals.rhythm == EcgRhythm.ASYSTOLE,
            isRescued = false,
            showGratitudeScene = false,
            latestNurseCallout = initialCallout,
            clinicalLogs = initialLogs,
            aiClinicalMentorComment = null,
            isAiAnalyzing = false
        )
    }

    fun getAllAvailableCases(): List<PatientCase> {
        return repository.getAllCases()
    }

    fun startSpecificCase(caseId: String) {
        val selectedCase = repository.getCaseById(caseId) ?: repository.getNextCase(null)
        val initialCallout = "Điều dưỡng Thảo: 'Bác sĩ ơi, tiếp nhận ca ${selectedCase.patientName} (${selectedCase.age}t): ${selectedCase.chiefComplaint}!'"
        val initialLogs = listOf("${timeFormat.format(Date())} - Tiếp nhận ca cấp cứu: ${selectedCase.chiefComplaint}")

        val initialChat = listOf(
            ChatMessage("msg_0", selectedCase.patientName, "Bác sĩ ơi, tôi mệt quá, cứu tôi với!", isDoctor = false, timeFormat.format(Date()))
        )

        repository.clearSavedShift()

        _state.value = _state.value.copy(
            currentScreen = AppScreen.DONNING_ANIMATION,
            currentCase = selectedCase,
            currentStep = 1,
            currentVitals = selectedCase.initialVitals,
            patientStability = 50,
            remainingSeconds = selectedCase.timeLimitMinutes * 60,
            chatMessages = initialChat,
            isAiChatResponding = false,
            examinedSystems = emptySet(),
            orderedLabIds = emptySet(),
            primaryDx = "",
            diffDxList = emptyList(),
            reasoningText = "",
            executedOrderIds = emptySet(),
            isCardiacArrest = selectedCase.initialVitals.rhythm == EcgRhythm.VFIB || selectedCase.initialVitals.rhythm == EcgRhythm.ASYSTOLE,
            isRescued = false,
            showGratitudeScene = false,
            latestNurseCallout = initialCallout,
            clinicalLogs = initialLogs,
            aiClinicalMentorComment = null,
            isAiAnalyzing = false,
            savedShift = null
        )
    }

    fun finishDonningAnimation() {
        _state.value = _state.value.copy(currentScreen = AppScreen.IN_SHIFT)
        startCountdownTimer()
    }

    fun resumeSavedShift() {
        val saved = repository.loadSavedShiftData() ?: return
        val patientCase = repository.getCaseById(saved.caseId) ?: repository.getNextCase(null)

        val vitals = if (saved.isRescued) patientCase.successVitals
        else if (saved.isCardiacArrest) patientCase.badDelayVitals
        else patientCase.initialVitals

        val restoredStability = if (saved.isRescued) 90 else if (saved.isCardiacArrest) 10 else 55

        _state.value = _state.value.copy(
            currentScreen = AppScreen.IN_SHIFT,
            currentCase = patientCase,
            currentStep = saved.stepIndex,
            currentVitals = vitals,
            patientStability = restoredStability,
            remainingSeconds = (patientCase.timeLimitMinutes * 60 - saved.elapsedSeconds).coerceAtLeast(60),
            examinedSystems = patientCase.physicalExams.map { it.systemKey }.toSet(),
            orderedLabIds = saved.orderedLabs,
            primaryDx = saved.primaryDx,
            diffDxList = saved.diffDx,
            reasoningText = saved.reasoning,
            executedOrderIds = saved.executedOrders,
            isCardiacArrest = saved.isCardiacArrest,
            isRescued = saved.isRescued,
            showGratitudeScene = false,
            latestNurseCallout = "Điều dưỡng Thảo: 'Tiếp tục ca trực bệnh nhân ${patientCase.patientName}!'",
            clinicalLogs = listOf("Đã khôi phục ca trực từ bộ nhớ.")
        )
        startCountdownTimer()
    }

    /**
     * Restart current case fresh from the beginning (reset all progress, timer, vitals)
     */
    fun restartCurrentCase() {
        val saved = repository.loadSavedShiftData()
        val currentCase = if (saved != null) {
            repository.getCaseById(saved.caseId) ?: _state.value.currentCase ?: repository.getNextCase(null)
        } else {
            _state.value.currentCase ?: repository.getNextCase(null)
        }

        val initialCallout = "Điều dưỡng Thảo: 'Bác sĩ ơi, bắt đầu lại ca bệnh ${currentCase.patientName} (${currentCase.age}t): ${currentCase.chiefComplaint}!'"
        val initialLogs = listOf("${timeFormat.format(Date())} - Bắt đầu lại ca cấp cứu: ${currentCase.chiefComplaint}")
        val initialChat = listOf(
            ChatMessage("msg_0", currentCase.patientName, "Bác sĩ ơi, tôi mệt quá, cứu tôi với!", isDoctor = false, timeFormat.format(Date()))
        )

        repository.clearSavedShift()

        _state.value = _state.value.copy(
            currentScreen = AppScreen.IN_SHIFT,
            currentCase = currentCase,
            currentStep = 1,
            currentVitals = currentCase.initialVitals,
            patientStability = 50,
            remainingSeconds = currentCase.timeLimitMinutes * 60,
            chatMessages = initialChat,
            isAiChatResponding = false,
            examinedSystems = emptySet(),
            orderedLabIds = emptySet(),
            primaryDx = "",
            diffDxList = emptyList(),
            reasoningText = "",
            executedOrderIds = emptySet(),
            isCardiacArrest = false,
            isRescued = false,
            showGratitudeScene = false,
            latestNurseCallout = initialCallout,
            clinicalLogs = initialLogs,
            aiClinicalMentorComment = null,
            isAiAnalyzing = false,
            savedShift = null
        )
        startCountdownTimer()
    }

    fun pauseAndSaveShift() {
        stopTimer()
        HospitalAudioSynthesizer.stopAlarm()
        HospitalAudioSynthesizer.stopMetronome()
        val c = _state.value.currentCase ?: return
        val elapsed = (c.timeLimitMinutes * 60 - _state.value.remainingSeconds).coerceAtLeast(0)

        repository.saveShift(
            caseId = c.id,
            stepIndex = _state.value.currentStep,
            elapsedSeconds = elapsed,
            abcdeChecked = emptySet(),
            orderedLabIds = _state.value.orderedLabIds,
            primaryDx = _state.value.primaryDx,
            diffDxList = _state.value.diffDxList,
            reasoningText = _state.value.reasoningText,
            executedOrderIds = _state.value.executedOrderIds,
            isCardiacArrest = _state.value.isCardiacArrest,
            isRescued = _state.value.isRescued
        )

        _state.value = _state.value.copy(
            currentScreen = AppScreen.START,
            savedShift = repository.loadSavedShiftData()
        )
    }

    private fun startCountdownTimer() {
        stopTimer()
        timerJob = viewModelScope.launch {
            while (_state.value.remainingSeconds > 0 && _state.value.currentScreen == AppScreen.IN_SHIFT) {
                delay(1000)
                val newSec = _state.value.remainingSeconds - 1
                _state.value = _state.value.copy(remainingSeconds = newSec)

                if (newSec == 150 && !_state.value.isRescued && !_state.value.isCardiacArrest) {
                    val curr = _state.value.currentCase
                    if (curr != null && curr.triageLevel == TriageLevel.RED) {
                        val newLogs = _state.value.clinicalLogs.toMutableList()
                        newLogs.add(0, "CẢNH BÁO ĐỎ: Trễ mốc thời gian vàng! Bệnh nhân trụy mạch ngưng tim!")
                        _state.value = _state.value.copy(
                            isCardiacArrest = true,
                            patientStability = 10,
                            currentVitals = curr.badDelayVitals,
                            latestNurseCallout = "Điều dưỡng Thảo: 'Bác sĩ ơi! Huyết áp tụt về 0 rồi, monitor chuyển thành vô tâm thu!'",
                            clinicalLogs = newLogs
                        )
                    }
                }
            }
        }
    }

    private fun stopTimer() {
        timerJob?.cancel()
        timerJob = null
    }

    fun toggleSound() {
        val newMuted = !_state.value.isSoundMuted
        HospitalAudioSynthesizer.setMuted(newMuted)
        _state.value = _state.value.copy(isSoundMuted = newMuted)
    }

    fun setStep(step: Int) {
        _state.value = _state.value.copy(currentStep = step)
    }

    /**
     * Interactive AI Chat with Patient/Family Member
     */
    fun sendDoctorQuestion(questionText: String) {
        val patientCase = _state.value.currentCase ?: return
        val currentList = _state.value.chatMessages.toMutableList()
        val docMsg = ChatMessage(
            id = "msg_${System.currentTimeMillis()}",
            sender = "Bác sĩ",
            text = questionText,
            isDoctor = true,
            timestamp = timeFormat.format(Date())
        )
        currentList.add(docMsg)

        _state.value = _state.value.copy(
            chatMessages = currentList,
            isAiChatResponding = true
        )

        viewModelScope.launch {
            val (speaker, answer) = GeminiClinicalService.chatWithPatient(
                patientCase = patientCase,
                doctorQuestion = questionText,
                chatHistory = currentList
            )

            val patientMsg = ChatMessage(
                id = "msg_${System.currentTimeMillis() + 1}",
                sender = speaker,
                text = answer,
                isDoctor = false,
                timestamp = timeFormat.format(Date())
            )

            val updatedList = _state.value.chatMessages.toMutableList()
            updatedList.add(patientMsg)

            _state.value = _state.value.copy(
                chatMessages = updatedList,
                isAiChatResponding = false
            )
        }
    }

    fun examineSystem(key: String) {
        val current = _state.value.examinedSystems.toMutableSet()
        current.add(key)
        _state.value = _state.value.copy(examinedSystems = current)
    }

    fun toggleLab(labId: String) {
        val current = _state.value.orderedLabIds.toMutableSet()
        if (labId in current) {
            current.remove(labId)
            _state.value = _state.value.copy(orderedLabIds = current)
            return
        }

        current.add(labId)
        val caseId = _state.value.currentCase?.id ?: ""
        val branchEvent = CaseAiEngine.evaluateLabOrder(
            caseId = caseId,
            labId = labId,
            currentStability = _state.value.patientStability,
            executedOrders = _state.value.executedOrderIds
        )

        val newStability = (_state.value.patientStability + branchEvent.stabilityDelta).coerceIn(5, 100)
        val penalizedSec = (_state.value.remainingSeconds - branchEvent.timePenaltyMinutes * 60).coerceAtLeast(30)
        val newLogs = _state.value.clinicalLogs.toMutableList()
        newLogs.add(0, "${timeFormat.format(Date())} - ${branchEvent.title}: ${branchEvent.narrative}")

        var vitals = _state.value.currentVitals
        var isArrest = _state.value.isCardiacArrest
        if (branchEvent.triggersCardiacArrest) {
            isArrest = true
            vitals = _state.value.currentCase?.badDelayVitals ?: vitals
        }

        _state.value = _state.value.copy(
            orderedLabIds = current,
            patientStability = newStability,
            remainingSeconds = penalizedSec,
            latestNurseCallout = branchEvent.nurseCallout,
            clinicalLogs = newLogs,
            currentVitals = vitals,
            isCardiacArrest = isArrest
        )
    }

    fun setPrimaryDx(dx: String) {
        _state.value = _state.value.copy(primaryDx = dx)
    }

    fun addDiffDx(dx: String) {
        val list = _state.value.diffDxList.toMutableList()
        list.add(dx)
        _state.value = _state.value.copy(diffDxList = list)
    }

    fun removeDiffDx(index: Int) {
        val list = _state.value.diffDxList.toMutableList()
        if (index in list.indices) {
            list.removeAt(index)
            _state.value = _state.value.copy(diffDxList = list)
        }
    }

    fun setReasoning(text: String) {
        _state.value = _state.value.copy(reasoningText = text)
    }

    fun evaluateDiagnosisWithAi() {
        val patientCase = _state.value.currentCase ?: return
        val primaryDx = _state.value.primaryDx
        val reasoning = _state.value.reasoningText
        val executedOrders = _state.value.executedOrderIds.toList()

        _state.value = _state.value.copy(isAiAnalyzing = true)
        viewModelScope.launch {
            val aiResponse = GeminiClinicalService.analyzeDoctorDecision(
                patientCase = patientCase,
                doctorTypedDx = primaryDx,
                doctorReasoning = reasoning,
                executedOrders = executedOrders
            )
            val newLogs = _state.value.clinicalLogs.toMutableList()
            newLogs.add(0, "HỘI CHẨN AI: ${aiResponse.take(80)}...")

            _state.value = _state.value.copy(
                isAiAnalyzing = false,
                aiClinicalMentorComment = aiResponse,
                clinicalLogs = newLogs
            )
        }
    }

    fun executeEmergencyOrder(order: EmergencyOrder) {
        val executed = _state.value.executedOrderIds.toMutableSet()
        executed.add(order.id)

        val patientCase = _state.value.currentCase ?: return
        val branchEvent = CaseAiEngine.evaluateTreatmentOrder(
            patientCase = patientCase,
            order = order,
            currentStability = _state.value.patientStability
        )

        val newStability = (_state.value.patientStability + branchEvent.stabilityDelta).coerceIn(5, 100)
        val newLogs = _state.value.clinicalLogs.toMutableList()
        newLogs.add(0, "${timeFormat.format(Date())} - ${branchEvent.title}")

        var vitals = _state.value.currentVitals
        var isRescued = _state.value.isRescued
        var isArrest = _state.value.isCardiacArrest

        if (branchEvent.triggersCardiacArrest) {
            isArrest = true
            isRescued = false
            vitals = patientCase.badDelayVitals
        } else if (branchEvent.triggersRosc) {
            isRescued = true
            isArrest = false
            vitals = patientCase.successVitals
        }

        _state.value = _state.value.copy(
            executedOrderIds = executed,
            patientStability = newStability,
            latestNurseCallout = branchEvent.nurseCallout,
            clinicalLogs = newLogs,
            currentVitals = vitals,
            isRescued = isRescued,
            isCardiacArrest = isArrest
        )
    }

    fun openDefibModal() {
        _state.value = _state.value.copy(showDefibModal = true)
    }

    fun closeDefibModal() {
        _state.value = _state.value.copy(showDefibModal = false)
    }

    fun deliverShock(joules: Int) {
        closeDefibModal()
        val c = _state.value.currentCase
        val newVitals = if (_state.value.currentVitals.rhythm == EcgRhythm.VFIB || _state.value.currentCase?.id == "case_stemi_01") {
            c?.successVitals ?: VitalSigns(86, 115, 75, 98, 18, 36.8f, 15, EcgRhythm.NORMAL_SINUS)
        } else {
            _state.value.currentVitals
        }

        val newLogs = _state.value.clinicalLogs.toMutableList()
        newLogs.add(0, "${timeFormat.format(Date())} - SỐC ĐIỆN ${joules}J KHỬ RUNG THÀNH CÔNG -> NHỊP XOANG HỒI PHỤC!")

        _state.value = _state.value.copy(
            currentVitals = newVitals,
            patientStability = 85,
            isRescued = true,
            isCardiacArrest = false,
            latestNurseCallout = "Điều dưỡng Tuấn: 'Sốc điện thành công rồi! Monitor trở lại nhịp xoang, mạch nảy rõ!'",
            clinicalLogs = newLogs
        )
    }

    fun openCprModal() {
        _state.value = _state.value.copy(showCprModal = true)
    }

    fun closeCprModal() {
        _state.value = _state.value.copy(showCprModal = false)
    }

    fun triggerRoscFromCpr() {
        closeCprModal()
        val c = _state.value.currentCase
        val restored = c?.successVitals ?: VitalSigns(90, 110, 70, 97, 18, 36.8f, 15, EcgRhythm.NORMAL_SINUS)

        val newLogs = _state.value.clinicalLogs.toMutableList()
        newLogs.add(0, "${timeFormat.format(Date())} - HỒI SINH TIM PHỔI (CPR) ĐẠT CHUẨN -> TỰ ĐẬP LẠI (ROSC)!")

        _state.value = _state.value.copy(
            currentVitals = restored,
            patientStability = 75,
            isRescued = true,
            isCardiacArrest = false,
            latestNurseCallout = "Điều dưỡng Thảo: 'Bác sĩ ơi, tim bệnh nhân tự đập lại rồi! Huyết áp 110/70 mmHg!'",
            clinicalLogs = newLogs
        )
    }

    /**
     * Finish interventions and check if patient gratitude scene should be displayed
     */
    fun completeInterventionsAndCheckGratitude() {
        if (_state.value.isRescued || _state.value.patientStability >= 70) {
            _state.value = _state.value.copy(showGratitudeScene = true)
        } else {
            _state.value = _state.value.copy(currentStep = 5)
        }
    }

    fun dismissGratitudeAndGoToDebrief() {
        _state.value = _state.value.copy(
            showGratitudeScene = false,
            currentStep = 5
        )
    }

    fun acceptNextPatient() {
        repository.incrementPatientsTreated()
        if (_state.value.isRescued) {
            repository.incrementPatientsSaved()
            repository.addScore(25)
        } else {
            repository.addScore(5)
        }
        repository.clearSavedShift()

        val nextCase = repository.getNextCase(_state.value.currentCase?.id)
        val initialCallout = "Điều dưỡng Thảo: 'Bác sĩ ơi, ca cấp cứu mới vừa vào: ${nextCase.patientName} (${nextCase.age}t)!'"
        val initialLogs = listOf("${timeFormat.format(Date())} - Tiếp nhận ca cấp cứu mới: ${nextCase.chiefComplaint}")
        val initialChat = listOf(
            ChatMessage("msg_0", nextCase.patientName, "Bác sĩ ơi, tôi mệt quá...", isDoctor = false, timeFormat.format(Date()))
        )

        _state.value = _state.value.copy(
            currentScreen = AppScreen.IN_SHIFT,
            currentCase = nextCase,
            currentStep = 1,
            currentVitals = nextCase.initialVitals,
            patientStability = 50,
            remainingSeconds = nextCase.timeLimitMinutes * 60,
            chatMessages = initialChat,
            isAiChatResponding = false,
            examinedSystems = emptySet(),
            orderedLabIds = emptySet(),
            primaryDx = "",
            diffDxList = emptyList(),
            reasoningText = "",
            executedOrderIds = emptySet(),
            isCardiacArrest = false,
            isRescued = false,
            showGratitudeScene = false,
            savedShift = null,
            latestNurseCallout = initialCallout,
            clinicalLogs = initialLogs,
            aiClinicalMentorComment = null,
            isAiAnalyzing = false,
            patientsTreated = repository.getPatientsTreated(),
            patientsSaved = repository.getPatientsSaved(),
            reputationScore = repository.getReputation()
        )
        startCountdownTimer()
    }
}
