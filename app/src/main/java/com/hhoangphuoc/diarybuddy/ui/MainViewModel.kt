package com.hhoangphuoc.diarybuddy.ui

import android.app.Application
import android.content.Context
import android.speech.RecognitionListener
import android.speech.SpeechRecognizer
import android.speech.tts.TextToSpeech
import android.speech.tts.TextToSpeech.OnUtteranceProgressListener
import android.util.Log
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.viewModelScope
import com.hhoangphuoc.diarybuddy.DiaryBuddy
import com.hhoangphuoc.diarybuddy.data.service.ScreenContentService
import com.hhoangphuoc.diarybuddy.data.service.VoiceRecognitionService
import com.hhoangphuoc.diarybuddy.domain.model.ChatMessage
import com.hhoangphuoc.diarybuddy.domain.model.FeatureType
import com.hhoangphuoc.diarybuddy.domain.model.SpeechState
import com.hhoangphuoc.diarybuddy.domain.usecase.ProcessQueryUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Locale
import java.util.UUID

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val TAG = "MainViewModel"
    
    // State management
    private val _state = MutableStateFlow<MainState>(MainState.Hidden)
    val state: StateFlow<MainState> = _state.asStateFlow()
    
    // Services
    private val voiceRecognitionService = VoiceRecognitionService(application)
    private val screenContentService = ScreenContentService(application)
    private val processQueryUseCase = (application as DiaryBuddy).processQueryUseCase
    
    // Text-to-Speech
    private val textToSpeech: TextToSpeech = TextToSpeech(application) { status ->
        if (status == TextToSpeech.SUCCESS) {
            textToSpeech.language = Locale.US
            textToSpeech.setSpeechRate(1.0f)
            textToSpeech.setPitch(1.0f)
            
            // Set up TTS listener
            textToSpeech.setOnUtteranceProgressListener(object : OnUtteranceProgressListener() {
                override fun onStart(utteranceId: String) {
                    // TTS started speaking
                }
                
                override fun onDone(utteranceId: String) {
                    // TTS finished speaking
                }
                
                override fun onError(utteranceId: String) {
                    // TTS error
                }
            })
        } else {
            Log.e(TAG, "TTS initialization failed with status: $status")
        }
    }
    
    // Current screen content
    private var currentScreenContent: String = ""
    
    init {
        // Listen for speech recognition results
        viewModelScope.launch {
            voiceRecognitionService.recognitionResults.collect { result ->
                handleSpeechResult(result)
            }
        }
        
        // Listen for speech state changes
        viewModelScope.launch {
            voiceRecognitionService.speechState.collect { speechState ->
                updateListeningState(speechState.isSpeaking)
            }
        }
        
        // Listen for screen content changes
        viewModelScope.launch {
            screenContentService.currentContent.collect { content ->
                currentScreenContent = content
            }
        }
    }
    
    fun handleEvent(event: MainEvent) {
        when (event) {
            is MainEvent.Activate -> activateApp()
            is MainEvent.Exit -> hideApp()
            is MainEvent.FeatureSelected -> selectFeature(event.feature)
            is MainEvent.StartListening -> startListening()
            is MainEvent.StopListening -> stopListening()
            is MainEvent.UserSpeak -> processUserSpeech(event.text)
        }
    }
    
    private fun activateApp() {
        _state.update { 
            MainState.Active(
                activeFeature = FeatureType.VOICE_ASSISTANT
            )
        }
    }
    
    private fun hideApp() {
        stopListening()
        _state.value = MainState.Hidden
    }
    
    private fun selectFeature(feature: FeatureType) {
        val currentState = _state.value as? MainState.Active ?: return
        
        when (feature) {
            FeatureType.VOICE_ASSISTANT -> {
                _state.update { 
                    currentState.copy(
                        activeFeature = feature,
                        isLoading = false
                    )
                }
            }
            FeatureType.CONTENT_READER -> {
                _state.update { 
                    currentState.copy(
                        activeFeature = feature,
                        isLoading = true
                    )
                }
                readScreenContent()
            }
            FeatureType.NOTE_TAKING -> {
                _state.update { 
                    currentState.copy(
                        activeFeature = feature,
                        isLoading = false
                    )
                }
                // Start listening immediately when switching to note-taking
                startListening()
            }
        }
    }
    
    private fun startListening() {
        voiceRecognitionService.startListening()
    }
    
    private fun stopListening() {
        voiceRecognitionService.stopListening()
    }
    
    private fun updateListeningState(isListening: Boolean) {
        val currentState = _state.value as? MainState.Active ?: return
        _state.update {
            currentState.copy(isListening = isListening)
        }
    }
    
    private fun handleSpeechResult(text: String) {
        val currentState = _state.value as? MainState.Active ?: return
        
        when (currentState.activeFeature) {
            FeatureType.VOICE_ASSISTANT -> {
                // Add user message to chat
                addUserMessage(text)
                // Process the query
                processQuery(text)
            }
            FeatureType.NOTE_TAKING -> {
                // Update note text
                updateNoteText(text)
            }
            FeatureType.CONTENT_READER -> {
                // For content reader, we don't need to do anything with speech results
            }
        }
    }
    
    private fun processUserSpeech(text: String) {
        handleSpeechResult(text)
    }
    
    private fun addUserMessage(text: String) {
        val currentState = _state.value as? MainState.Active ?: return
        val updatedMessages = currentState.chatMessages + ChatMessage(text, true)
        
        _state.update {
            currentState.copy(
                chatMessages = updatedMessages,
                isLoading = true
            )
        }
    }
    
    private fun addAssistantMessage(text: String) {
        val currentState = _state.value as? MainState.Active ?: return
        val updatedMessages = currentState.chatMessages + ChatMessage(text, false)
        
        _state.update {
            currentState.copy(
                chatMessages = updatedMessages,
                isLoading = false
            )
        }
        
        // Speak the response
        speakText(text)
    }
    
    private fun updateNoteText(text: String) {
        val currentState = _state.value as? MainState.Active ?: return
        val updatedText = if (currentState.noteText.isEmpty()) {
            text
        } else {
            "${currentState.noteText} $text"
        }
        
        _state.update {
            currentState.copy(noteText = updatedText)
        }
    }
    
    private fun processQuery(query: String) {
        viewModelScope.launch {
            try {
                val response = processQueryUseCase.execute(query, currentScreenContent)
                addAssistantMessage(response)
            } catch (e: Exception) {
                Log.e(TAG, "Error processing query: ${e.message}")
                addAssistantMessage("Sorry, I couldn't process your request. Please try again.")
            }
        }
    }
    
    private fun readScreenContent() {
        viewModelScope.launch {
            try {
                val currentState = _state.value as? MainState.Active ?: return@launch
                
                // Set loading state
                _state.update {
                    currentState.copy(isLoading = true)
                }
                
                // Process the screen content using the dedicated summarize method
                val summary = processQueryUseCase.summarizeContent(currentScreenContent)
                
                // Update state with summary
                _state.update {
                    (it as MainState.Active).copy(
                        contentSummary = summary,
                        isLoading = false
                    )
                }
                
                // Speak the summary
                speakText(summary)
                
            } catch (e: Exception) {
                Log.e(TAG, "Error reading screen content: ${e.message}")
                
                val errorMessage = "Sorry, I couldn't read the screen content. Please try again."
                _state.update {
                    (it as MainState.Active).copy(
                        contentSummary = errorMessage,
                        isLoading = false
                    )
                }
                
                speakText(errorMessage)
            }
        }
    }
    
    private fun speakText(text: String) {
        val utteranceId = UUID.randomUUID().toString()
        textToSpeech.speak(text, TextToSpeech.QUEUE_FLUSH, null, utteranceId)
    }
    
    override fun onCleared() {
        super.onCleared()
        textToSpeech.stop()
        textToSpeech.shutdown()
        voiceRecognitionService.destroy()
    }
}