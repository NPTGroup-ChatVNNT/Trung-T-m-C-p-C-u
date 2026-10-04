package com.example.audio

import android.media.AudioAttributes
import android.media.AudioFormat
import android.media.AudioTrack
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlin.math.sin

object HospitalAudioSynthesizer {
    private const val SAMPLE_RATE = 44100
    private var isMuted = false
    private val scope = CoroutineScope(Dispatchers.Default)

    private var alarmJob: Job? = null
    private var metronomeJob: Job? = null
    private var chargeJob: Job? = null

    fun setMuted(muted: Boolean) {
        isMuted = muted
        if (muted) {
            stopAlarm()
            stopMetronome()
            stopDefibCharge()
        }
    }

    fun isMuted(): Boolean = isMuted

    /**
     * Heartbeat pulse beep: pitch changes based on SpO2 (higher SpO2 = higher pitch, low SpO2 = dull low pitch)
     */
    fun playHeartbeat(spo2: Int) {
        if (isMuted) return
        scope.launch {
            val freq = when {
                spo2 >= 98 -> 880.0
                spo2 >= 95 -> 760.0
                spo2 >= 90 -> 620.0
                spo2 >= 85 -> 500.0
                spo2 >= 75 -> 420.0
                else -> 340.0
            }
            playTone(frequency = freq, durationMs = 85, volume = 0.6f)
        }
    }

    /**
     * Monitor critical alarm (yellow or red)
     */
    fun startAlarm(isRed: Boolean) {
        if (isMuted || alarmJob?.isActive == true) return
        alarmJob = scope.launch {
            while (isActive && !isMuted) {
                if (isRed) {
                    // Urgent red alarm: high-pitched alternating burst
                    playTone(987.0, 100, 0.7f)
                    delay(60)
                    playTone(1318.0, 100, 0.7f)
                    delay(60)
                    playTone(987.0, 100, 0.7f)
                    delay(600)
                } else {
                    // Yellow warning alarm: moderate double beep
                    playTone(750.0, 120, 0.5f)
                    delay(120)
                    playTone(750.0, 120, 0.5f)
                    delay(1200)
                }
            }
        }
    }

    fun stopAlarm() {
        alarmJob?.cancel()
        alarmJob = null
    }

    /**
     * Defibrillator capacitor charging sound (pitch sweeps from 320 Hz to 1450 Hz)
     */
    fun startDefibCharge(onFullyCharged: () -> Unit) {
        if (isMuted) {
            scope.launch {
                delay(2500)
                onFullyCharged()
            }
            return
        }
        stopDefibCharge()
        chargeJob = scope.launch {
            val durationMs = 2500
            val steps = 50
            val startFreq = 320.0
            val endFreq = 1450.0
            val stepTime = durationMs / steps

            for (i in 0..steps) {
                if (!isActive || isMuted) return@launch
                val progress = i.toDouble() / steps
                val currentFreq = startFreq + (endFreq - startFreq) * (progress * progress)
                playTone(currentFreq, stepTime, 0.65f)
            }

            // Steady high pitch ready tone
            if (isActive && !isMuted) {
                onFullyCharged()
                for (j in 0 until 12) {
                    if (!isActive || isMuted) break
                    playTone(1450.0, 120, 0.8f)
                    delay(100)
                }
            }
        }
    }

    fun stopDefibCharge() {
        chargeJob?.cancel()
        chargeJob = null
    }

    /**
     * Electric shock discharge ZAP sound (noise burst + punch)
     */
    fun playShockZap() {
        if (isMuted) return
        stopDefibCharge()
        scope.launch {
            val numSamples = (SAMPLE_RATE * 0.35).toInt()
            val buffer = ShortArray(numSamples)
            val random = java.util.Random()

            for (i in 0 until numSamples) {
                val decay = 1.0 - (i.toDouble() / numSamples)
                // Combination of heavy low-freq sub-bass thump and crackling electrical white noise
                val subBass = sin(2.0 * Math.PI * 65.0 * i / SAMPLE_RATE)
                val noise = (random.nextDouble() * 2.0 - 1.0)
                val mixed = (subBass * 0.5 + noise * 0.5) * decay
                buffer[i] = (mixed * Short.MAX_VALUE * 0.9).toInt().coerceIn(Short.MIN_VALUE.toInt(), Short.MAX_VALUE.toInt()).toShort()
            }

            playBuffer(buffer)
        }
    }

    /**
     * Metronome 110 BPM for CPR guidance
     */
    fun startMetronome() {
        if (metronomeJob?.isActive == true) return
        metronomeJob = scope.launch {
            // 110 BPM = 60000 / 110 = 545.45 ms per beat
            val intervalMs = 545L
            while (isActive) {
                if (!isMuted) {
                    playTone(1100.0, 40, 0.6f)
                }
                delay(intervalMs)
            }
        }
    }

    fun stopMetronome() {
        metronomeJob?.cancel()
        metronomeJob = null
    }

    private fun playTone(frequency: Double, durationMs: Int, volume: Float) {
        val numSamples = (SAMPLE_RATE * (durationMs / 1000.0)).toInt().coerceAtLeast(1)
        val buffer = ShortArray(numSamples)
        for (i in 0 until numSamples) {
            // Apply attack & decay envelope to eliminate popping
            val envelope = when {
                i < numSamples * 0.1 -> i / (numSamples * 0.1)
                i > numSamples * 0.8 -> (numSamples - i) / (numSamples * 0.2)
                else -> 1.0
            }
            val sample = sin(2.0 * Math.PI * frequency * i / SAMPLE_RATE) * envelope * volume
            buffer[i] = (sample * Short.MAX_VALUE).toInt().coerceIn(Short.MIN_VALUE.toInt(), Short.MAX_VALUE.toInt()).toShort()
        }
        playBuffer(buffer)
    }

    private fun playBuffer(buffer: ShortArray) {
        try {
            val audioTrack = AudioTrack.Builder()
                .setAudioAttributes(
                    AudioAttributes.Builder()
                        .setUsage(AudioAttributes.USAGE_GAME)
                        .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                        .build()
                )
                .setAudioFormat(
                    AudioFormat.Builder()
                        .setEncoding(AudioFormat.ENCODING_PCM_16BIT)
                        .setSampleRate(SAMPLE_RATE)
                        .setChannelMask(AudioFormat.CHANNEL_OUT_MONO)
                        .build()
                )
                .setBufferSizeInBytes(buffer.size * 2)
                .setTransferMode(AudioTrack.MODE_STATIC)
                .build()

            audioTrack.write(buffer, 0, buffer.size)
            audioTrack.play()
            scope.launch {
                delay((buffer.size * 1000L / SAMPLE_RATE) + 50)
                try {
                    audioTrack.stop()
                    audioTrack.release()
                } catch (_: Exception) {}
            }
        } catch (_: Exception) {
            // AudioTrack creation error handling
        }
    }
}
