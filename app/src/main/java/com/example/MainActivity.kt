package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import com.example.ui.screens.DoctorDonningAnimationScreen
import com.example.ui.screens.EmergencyRoomScreen
import com.example.ui.screens.StartScreen
import com.example.ui.theme.DarkBackground
import com.example.ui.theme.MyApplicationTheme
import com.example.viewmodel.AppScreen
import com.example.viewmodel.EmergencyGameViewModel

class MainActivity : ComponentActivity() {
    private val viewModel: EmergencyGameViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = DarkBackground
                ) {
                    EmergencyAppContent(viewModel = viewModel)
                }
            }
        }
    }
}

@Composable
fun EmergencyAppContent(viewModel: EmergencyGameViewModel) {
    val state by viewModel.state.collectAsState()

    when (state.currentScreen) {
        AppScreen.START -> {
            StartScreen(
                savedShift = state.savedShift,
                patientsTreated = state.patientsTreated,
                patientsSaved = state.patientsSaved,
                reputationScore = state.reputationScore,
                isSoundMuted = state.isSoundMuted,
                availableCases = viewModel.getAllAvailableCases(),
                onToggleSound = { viewModel.toggleSound() },
                onStartNewShift = { viewModel.startNewShiftSequence() },
                onResumeShift = { viewModel.resumeSavedShift() },
                onRestartCurrentCase = { viewModel.restartCurrentCase() },
                onSelectCase = { caseId -> viewModel.startSpecificCase(caseId) }
            )
        }
        AppScreen.DONNING_ANIMATION -> {
            DoctorDonningAnimationScreen(
                onAnimationFinished = { viewModel.finishDonningAnimation() }
            )
        }
        AppScreen.IN_SHIFT -> {
            EmergencyRoomScreen(
                viewModel = viewModel,
                state = state
            )
        }
    }
}
