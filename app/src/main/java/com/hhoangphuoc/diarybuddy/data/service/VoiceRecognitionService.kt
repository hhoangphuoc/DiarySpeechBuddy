package com.hhoangphuoc.diarybuddy.data.service

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.util.Log
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import com.hhoangphuoc.diarybuddy.domain.model.SpeechState

/**
 * Service that handles speech recognition with continuous listening capability
 */
class VoiceRecognitionService(
    private val context: Context
) : RecognitionListener {
    private val TAG = "VoiceRecognitionService"

    // Speech state flow
    private val _speechState = MutableStateFlow(
        SpeechState(
            isSpeaking = false,
            languageCode = "en-US"
        )
    )
    val speechState: StateFlow<SpeechState> = _speechState.asStateFlow()
    
    // Recognition results flow
    private val _recognitionResults = MutableSharedFlow<String>(replay = 0)
    val recognitionResults: SharedFlow<String> = _recognitionResults.asSharedFlow()

    // Speech recognizer
    private val recognizer: SpeechRecognizer = SpeechRecognizer.createSpeechRecognizer(context)
    
    // Track if we should restart listening after results
    private var continuousListening = false
    
    init {
        recognizer.setRecognitionListener(this)
    }

    /**
     * Start listening for speech input
     */
    fun startListening(languageCode: String = "en-US", continuous: Boolean = true) {
        if (!SpeechRecognizer.isRecognitionAvailable(context)) {
            Log.e(TAG, "Speech recognition is not available")
            return
        }
        
        continuousListening = continuous
        
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(
                RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
            )
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, languageCode)
            putExtra(RecognizerIntent.EXTRA_PARTIAL_RESULTS, true)
            
            // For continuous recognition, we don't want too long pauses
            putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_MINIMUM_LENGTH_MILLIS, 1000)
            putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_COMPLETE_SILENCE_LENGTH_MILLIS, 1500)
            putExtra(RecognizerIntent.EXTRA_SPEECH_INPUT_POSSIBLY_COMPLETE_SILENCE_LENGTH_MILLIS, 1000)
        }

        try {
            recognizer.startListening(intent)
            
            _speechState.update {
                it.copy(
                    isSpeaking = true,
                    error = null
                )
            }
        } catch (e: Exception) {
            Log.e(TAG, "Error starting speech recognition: ${e.message}")
            _speechState.update {
                it.copy(
                    error = "Failed to start speech recognition"
                )
            }
        }
    }

    /**
     * Stop listening for speech input
     */
    fun stopListening() {
        continuousListening = false
        
        try {
            recognizer.stopListening()
        } catch (e: Exception) {
            Log.e(TAG, "Error stopping speech recognition: ${e.message}")
        }
        
        _speechState.update {
            it.copy(
                isSpeaking = false
            )
        }
    }
    
    /**
     * Clean up resources
     */
    fun destroy() {
        try {
            stopListening()
            recognizer.destroy()
        } catch (e: Exception) {
            Log.e(TAG, "Error destroying speech recognizer: ${e.message}")
        }
    }

    // RecognitionListener implementation
    override fun onReadyForSpeech(params: Bundle?) {
        _speechState.update {
            it.copy(
                error = null
            )
        }
    }

    override fun onBeginningOfSpeech() {
        _speechState.update {
            it.copy(
                isSpeaking = true,
                error = null
            )
        }
    }

    override fun onRmsChanged(rmsdB: Float) {
        // Could be used to show a visual indicator of speech volume
    }

    override fun onBufferReceived(buffer: ByteArray?) {
        // Not used in this implementation
    }

    override fun onEndOfSpeech() {
        _speechState.update {
            it.copy(
                isSpeaking = false
            )
        }
    }

    override fun onError(error: Int) {
        // Handle specific errors
        val errorMessage = when (error) {
            SpeechRecognizer.ERROR_AUDIO -> "Audio recording error"
            SpeechRecognizer.ERROR_CLIENT -> "Client side error"
            SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS -> "Insufficient permissions"
            SpeechRecognizer.ERROR_NETWORK -> "Network error"
            SpeechRecognizer.ERROR_NETWORK_TIMEOUT -> "Network timeout"
            SpeechRecognizer.ERROR_NO_MATCH -> "No recognition match"
            SpeechRecognizer.ERROR_RECOGNIZER_BUSY -> "Recognition service busy"
            SpeechRecognizer.ERROR_SERVER -> "Server error"
            SpeechRecognizer.ERROR_SPEECH_TIMEOUT -> "No speech input"
            else -> "Unknown error: $error"
        }
        
        Log.e(TAG, "Speech recognition error: $errorMessage")
        
        // Some errors should not be shown to the user
        if (error == SpeechRecognizer.ERROR_NO_MATCH || 
            error == SpeechRecognizer.ERROR_SPEECH_TIMEOUT) {
            // Restart listening if in continuous mode
            if (continuousListening) {
                startListening()
            }
            return
        }
        
        _speechState.update {
            it.copy(
                error = errorMessage,
                isSpeaking = false
            )
        }
        
        // Restart listening if in continuous mode and not a fatal error
        if (continuousListening && 
            error != SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS &&
            error != SpeechRecognizer.ERROR_CLIENT) {
            startListening()
        }
    }

    override fun onResults(results: Bundle?) {
        results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
            ?.getOrNull(0)
            ?.let { result ->
                if (result.isNotBlank()) {
                    // Emit the result
                    try {
                        _recognitionResults.tryEmit(result)
                    } catch (e: Exception) {
                        Log.e(TAG, "Error emitting recognition result: ${e.message}")
                    }
                }
            }
        
        // Restart listening if in continuous mode
        if (continuousListening) {
            startListening()
        }
    }

    override fun onPartialResults(partialResults: Bundle?) {
        // Could be used to show partial results while speaking
        // Not implemented in this version
    }

    override fun onEvent(eventType: Int, params: Bundle?) {
        // Not used in this implementation
    }
}