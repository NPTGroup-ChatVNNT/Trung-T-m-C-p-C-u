package com.example.data

import android.content.Context
import android.content.SharedPreferences
import com.example.model.PatientCase
import org.json.JSONArray
import org.json.JSONObject

class EmergencyRepository(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("er_game_prefs", Context.MODE_PRIVATE)
    private val allCases = EmergencyCaseDatabase.getAllCases()

    // Retrieve shuffled non-repeating queue
    fun getNextCase(currentCaseId: String?): PatientCase {
        val historyStr = prefs.getString("past_case_ids", "") ?: ""
        val pastList = historyStr.split(",").filter { it.isNotBlank() }.toMutableList()

        // Filter out cases that were used in the last 4 runs
        val recentCases = pastList.takeLast(4)
        val available = allCases.filter { it.id !in recentCases && it.id != currentCaseId }

        val selected = if (available.isNotEmpty()) {
            available.shuffled().first()
        } else {
            allCases.filter { it.id != currentCaseId }.shuffled().firstOrNull() ?: allCases.first()
        }

        pastList.add(selected.id)
        prefs.edit().putString("past_case_ids", pastList.joinToString(",")).apply()
        return selected
    }

    fun getCaseById(caseId: String): PatientCase? {
        return allCases.find { it.id == caseId }
    }

    fun getAllCases(): List<PatientCase> {
        return allCases
    }

    fun hasSavedShift(): Boolean {
        return prefs.getBoolean("has_active_shift", false)
    }

    fun clearSavedShift() {
        prefs.edit()
            .putBoolean("has_active_shift", false)
            .remove("saved_case_id")
            .remove("saved_step")
            .remove("saved_time_sec")
            .remove("saved_abcde_flags")
            .remove("saved_orders")
            .remove("saved_labs")
            .remove("saved_dx_primary")
            .remove("saved_dx_diffs")
            .remove("saved_dx_reasoning")
            .remove("saved_cardiac_arrest")
            .remove("saved_rescued")
            .apply()
    }

    fun saveShift(
        caseId: String,
        stepIndex: Int,
        elapsedSeconds: Int,
        abcdeChecked: Set<String>,
        orderedLabIds: Set<String>,
        primaryDx: String,
        diffDxList: List<String>,
        reasoningText: String,
        executedOrderIds: Set<String>,
        isCardiacArrest: Boolean,
        isRescued: Boolean
    ) {
        prefs.edit()
            .putBoolean("has_active_shift", true)
            .putString("saved_case_id", caseId)
            .putInt("saved_step", stepIndex)
            .putInt("saved_time_sec", elapsedSeconds)
            .putString("saved_abcde_flags", abcdeChecked.joinToString(","))
            .putString("saved_labs", orderedLabIds.joinToString(","))
            .putString("saved_dx_primary", primaryDx)
            .putString("saved_dx_diffs", JSONArray(diffDxList).toString())
            .putString("saved_dx_reasoning", reasoningText)
            .putString("saved_orders", executedOrderIds.joinToString(","))
            .putBoolean("saved_cardiac_arrest", isCardiacArrest)
            .putBoolean("saved_rescued", isRescued)
            .apply()
    }

    fun loadSavedShiftData(): SavedShiftData? {
        if (!hasSavedShift()) return null
        val caseId = prefs.getString("saved_case_id", null) ?: return null
        val stepIndex = prefs.getInt("saved_step", 1)
        val elapsedSeconds = prefs.getInt("saved_time_sec", 0)
        val abcdeFlags = prefs.getString("saved_abcde_flags", "")?.split(",")?.filter { it.isNotBlank() }?.toSet() ?: emptySet()
        val labs = prefs.getString("saved_labs", "")?.split(",")?.filter { it.isNotBlank() }?.toSet() ?: emptySet()
        val primaryDx = prefs.getString("saved_dx_primary", "") ?: ""
        val diffDxJson = prefs.getString("saved_dx_diffs", "[]") ?: "[]"
        val diffList = mutableListOf<String>()
        try {
            val jsonArr = JSONArray(diffDxJson)
            for (i in 0 until jsonArr.length()) {
                diffList.add(jsonArr.getString(i))
            }
        } catch (_: Exception) {}
        val reasoning = prefs.getString("saved_dx_reasoning", "") ?: ""
        val orders = prefs.getString("saved_orders", "")?.split(",")?.filter { it.isNotBlank() }?.toSet() ?: emptySet()
        val isArrest = prefs.getBoolean("saved_cardiac_arrest", false)
        val isRescued = prefs.getBoolean("saved_rescued", false)

        return SavedShiftData(
            caseId = caseId,
            stepIndex = stepIndex,
            elapsedSeconds = elapsedSeconds,
            abcdeChecked = abcdeFlags,
            orderedLabs = labs,
            primaryDx = primaryDx,
            diffDx = diffList,
            reasoning = reasoning,
            executedOrders = orders,
            isCardiacArrest = isArrest,
            isRescued = isRescued
        )
    }

    // Shift career stats
    fun incrementPatientsTreated() {
        val count = prefs.getInt("stat_patients_treated", 0)
        prefs.edit().putInt("stat_patients_treated", count + 1).apply()
    }

    fun getPatientsTreated(): Int = prefs.getInt("stat_patients_treated", 0)

    fun incrementPatientsSaved() {
        val count = prefs.getInt("stat_patients_saved", 0)
        prefs.edit().putInt("stat_patients_saved", count + 1).apply()
    }

    fun getPatientsSaved(): Int = prefs.getInt("stat_patients_saved", 0)

    fun addScore(points: Int) {
        val current = prefs.getInt("stat_reputation", 100)
        prefs.edit().putInt("stat_reputation", (current + points).coerceAtLeast(0)).apply()
    }

    fun getReputation(): Int = prefs.getInt("stat_reputation", 100)
}

data class SavedShiftData(
    val caseId: String,
    val stepIndex: Int,
    val elapsedSeconds: Int,
    val abcdeChecked: Set<String>,
    val orderedLabs: Set<String>,
    val primaryDx: String,
    val diffDx: List<String>,
    val reasoning: String,
    val executedOrders: Set<String>,
    val isCardiacArrest: Boolean,
    val isRescued: Boolean
)
