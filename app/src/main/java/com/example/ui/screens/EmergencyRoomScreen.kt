package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.components.CPRModal
import com.example.ui.components.DefibrillatorModal
import com.example.ui.components.PatientMonitorView
import com.example.ui.components.TraumaBayBedCard
import com.example.ui.steps.*
import com.example.ui.theme.*
import com.example.viewmodel.EmergencyGameState
import com.example.viewmodel.EmergencyGameViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EmergencyRoomScreen(
    viewModel: EmergencyGameViewModel,
    state: EmergencyGameState,
    modifier: Modifier = Modifier
) {
    val patientCase = state.currentCase ?: return
    val scrollState = rememberScrollState()
    var showClinicalLogDialog by remember { mutableStateOf(false) }

    // If gratitude scene is triggered, display full screen anime gratitude
    if (state.showGratitudeScene) {
        PatientGratitudeScreen(
            patientCase = patientCase,
            onContinueToDebrief = { viewModel.dismissGratitudeAndGoToDebrief() }
        )
        return
    }

    // Handle system back navigation
    BackHandler {
        if (state.currentStep > 1) {
            viewModel.setStep(state.currentStep - 1)
        } else {
            viewModel.pauseAndSaveShift()
        }
    }

    Scaffold(
        modifier = modifier.fillMaxSize(),
        containerColor = DarkBackground,
        topBar = {
            TopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "PHÒNG CẤP CỨU",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Black,
                            color = Color.White
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(4.dp))
                                .background(Color(patientCase.triageLevel.badgeColor))
                                .padding(horizontal = 6.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = patientCase.triageLevel.name,
                                fontSize = 9.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )
                        }
                    }
                },
                navigationIcon = {
                    IconButton(onClick = {
                        if (state.currentStep > 1) {
                            viewModel.setStep(state.currentStep - 1)
                        } else {
                            viewModel.pauseAndSaveShift()
                        }
                    }) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Quay lại",
                            tint = Color.White
                        )
                    }
                },
                actions = {
                    // Clinical event logs button
                    IconButton(onClick = { showClinicalLogDialog = true }) {
                        Icon(
                            imageVector = Icons.Default.HistoryEdu,
                            contentDescription = "Nhật ký ca trực",
                            tint = Spo2Cyan
                        )
                    }

                    // Restart case immediately button (Xanh lá)
                    IconButton(
                        onClick = { viewModel.restartCurrentCase() },
                        modifier = Modifier.testTag("btn_restart_case_topbar")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "Bắt đầu lại ca này",
                            tint = MedicalGreenLight
                        )
                    }

                    // Sound mute toggle
                    IconButton(onClick = { viewModel.toggleSound() }) {
                        Icon(
                            imageVector = if (state.isSoundMuted) Icons.Default.VolumeOff else Icons.Default.VolumeUp,
                            contentDescription = "Âm thanh",
                            tint = if (state.isSoundMuted) TextSecondary else Color(0xFFFEF08A)
                        )
                    }

                    // Explicit Pause and Exit button
                    Button(
                        onClick = { viewModel.pauseAndSaveShift() },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF1E293B),
                            contentColor = AlertOrange
                        ),
                        border = androidx.compose.foundation.BorderStroke(1.dp, AlertOrange.copy(alpha = 0.6f)),
                        shape = RoundedCornerShape(8.dp),
                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 2.dp),
                        modifier = Modifier.padding(end = 6.dp).testTag("btn_pause_and_exit")
                    ) {
                        Icon(Icons.Default.Pause, contentDescription = null, modifier = Modifier.size(15.dp))
                        Spacer(modifier = Modifier.width(3.dp))
                        Text("Dừng & Thoát", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = DarkSurface)
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // 1. STICKY PATIENT MONITOR (VITAL SIGNS & REAL-TIME LEAD II WAVE)
            PatientMonitorView(
                vitals = state.currentVitals,
                remainingSeconds = state.remainingSeconds,
                isSoundMuted = state.isSoundMuted,
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
            )

            // 2. RPG TRAUMA BAY BED CARD (PATIENT AVATAR, STABILITY BAR & NURSE CALLOUT)
            TraumaBayBedCard(
                patientCase = patientCase,
                vitals = state.currentVitals,
                stability = state.patientStability,
                nurseCallout = state.latestNurseCallout,
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 2.dp)
            )

            // 3. GAME QUEST STEP NAVIGATION BAR (5 STREAMLINED STEPS)
            val questSteps = listOf(
                "1. Khám & Bệnh sử",
                "2. Xét nghiệm",
                "3. Chẩn đoán",
                "4. Cấp cứu",
                "5. Giao ban"
            )

            ScrollableTabRow(
                selectedTabIndex = (state.currentStep - 1).coerceIn(0, questSteps.size - 1),
                containerColor = DarkSurface,
                edgePadding = 12.dp,
                divider = { HorizontalDivider(color = BorderSubtle) }
            ) {
                questSteps.forEachIndexed { index, name ->
                    val stepNum = index + 1
                    val isSelected = state.currentStep == stepNum

                    Tab(
                        selected = isSelected,
                        onClick = { viewModel.setStep(stepNum) },
                        text = {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                if (isSelected) {
                                    Box(
                                        modifier = Modifier
                                            .size(6.dp)
                                            .clip(RoundedCornerShape(50))
                                            .background(MedicalPrimaryLight)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                }
                                Text(
                                    text = name,
                                    fontSize = 11.sp,
                                    fontWeight = if (isSelected) FontWeight.Black else FontWeight.Normal,
                                    color = if (isSelected) MedicalPrimaryLight else TextSecondary
                                )
                            }
                        }
                    )
                }
            }

            // 4. MAIN SCROLLABLE STEP CONTENT
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(scrollState)
            ) {
                when (state.currentStep) {
                    1 -> PatientHistoryAndPhysicalExamStep(
                        patientCase = patientCase,
                        chatMessages = state.chatMessages,
                        isAiResponding = state.isAiChatResponding,
                        onSendDoctorQuestion = { viewModel.sendDoctorQuestion(it) },
                        examinedSystems = state.examinedSystems,
                        onExamSystem = { viewModel.examineSystem(it) },
                        onProceedToNextStep = { viewModel.setStep(2) }
                    )
                    2 -> ComprehensiveLabMenuStep(
                        patientCase = patientCase,
                        orderedLabIds = state.orderedLabIds,
                        onToggleLab = { viewModel.toggleLab(it) },
                        onProceedToNextStep = { viewModel.setStep(3) }
                    )
                    3 -> DifferentialDiagnosisStep(
                        patientCase = patientCase,
                        primaryDx = state.primaryDx,
                        onPrimaryDxChange = { viewModel.setPrimaryDx(it) },
                        diffDxList = state.diffDxList,
                        onAddDiffDx = { viewModel.addDiffDx(it) },
                        onRemoveDiffDx = { viewModel.removeDiffDx(it) },
                        reasoningText = state.reasoningText,
                        onReasoningChange = { viewModel.setReasoning(it) },
                        aiComment = state.aiClinicalMentorComment,
                        isAiAnalyzing = state.isAiAnalyzing,
                        onConsultAi = { viewModel.evaluateDiagnosisWithAi() },
                        onProceedToNextStep = { viewModel.setStep(4) }
                    )
                    4 -> EmergencyInterventionsStep(
                        patientCase = patientCase,
                        executedOrderIds = state.executedOrderIds,
                        onExecuteOrder = { viewModel.executeEmergencyOrder(it) },
                        onOpenDefibrillator = { viewModel.openDefibModal() },
                        onOpenCpr = { viewModel.openCprModal() },
                        isCardiacArrest = state.isCardiacArrest,
                        isRescued = state.isRescued,
                        onProceedToNextStep = { viewModel.completeInterventionsAndCheckGratitude() }
                    )
                    5 -> DebriefAndHandoverStep(
                        patientCase = patientCase,
                        examinedSystems = state.examinedSystems,
                        orderedLabIds = state.orderedLabIds,
                        primaryDx = state.primaryDx,
                        diffDxList = state.diffDxList,
                        reasoningText = state.reasoningText,
                        executedOrderIds = state.executedOrderIds,
                        isCardiacArrest = state.isCardiacArrest,
                        isRescued = state.isRescued,
                        onAcceptNextPatient = { viewModel.acceptNextPatient() }
                    )
                }
            }
        }
    }

    // Defibrillator Modal Dialog
    if (state.showDefibModal) {
        DefibrillatorModal(
            onDismiss = { viewModel.closeDefibModal() },
            onShockDelivered = { joules -> viewModel.deliverShock(joules) }
        )
    }

    // CPR Interactive Modal Dialog
    if (state.showCprModal) {
        CPRModal(
            onDismiss = { viewModel.closeCprModal() },
            onRoscAchieved = { viewModel.triggerRoscFromCpr() }
        )
    }

    // Clinical Event Log Dialog (Nhật ký diễn biến ca trực)
    if (showClinicalLogDialog) {
        AlertDialog(
            onDismissRequest = { showClinicalLogDialog = false },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.HistoryEdu, contentDescription = null, tint = Spo2Cyan)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("NHẬT KÝ CA TRỰC", fontSize = 15.sp, fontWeight = FontWeight.Bold)
                }
            },
            text = {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 350.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    if (state.clinicalLogs.isEmpty()) {
                        Text("Chưa có sự kiện nào ghi nhận.", fontSize = 12.sp, color = TextSecondary)
                    } else {
                        state.clinicalLogs.forEach { log ->
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp)
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(DarkSurfaceVariant)
                                    .padding(8.dp)
                            ) {
                                Text(
                                    text = log,
                                    fontSize = 11.sp,
                                    color = if (log.contains("CẢNH BÁO") || log.contains("NGUY HIỂM")) MedicalRedLight else TextPrimary,
                                    lineHeight = 15.sp
                                )
                            }
                        }
                    }
                }
            },
            confirmButton = {
                TextButton(onClick = { showClinicalLogDialog = false }) {
                    Text("Đóng", color = Color.White)
                }
            },
            containerColor = DarkSurface
        )
    }
}

