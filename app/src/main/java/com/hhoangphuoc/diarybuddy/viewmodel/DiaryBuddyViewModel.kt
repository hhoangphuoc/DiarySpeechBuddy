package com.hhoangphuoc.diarybuddy.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.hhoangphuoc.diarybuddy.intent.DiaryBuddyIntent
import com.hhoangphuoc.diarybuddy.model.DiaryBuddyState
import com.hhoangphuoc.diarybuddy.repository.DiaryBuddyRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch


class DiaryBuddyViewModel (private val repository: DiaryBuddyRepository) : ViewModel(){

    //declare in-app states
    private val _state =  MutableStateFlow(DiaryBuddyState())

    val state: StateFlow<DiaryBuddyState> = _state.asStateFlow()

    init {
        observeRepositoryResults()
    }

    /**
     * This function is used to executed certain commands specified by different user intents
     */
    fun processIntent(intent: DiaryBuddyIntent) {

        //FIXME: Using `when(intent)` as the switch-case, must cover all possible intents
        when(intent){
            //if the speech is activate
            is DiaryBuddyIntent.Activate -> {
                // For now, activation is handled directly in the MediaButtonReceiver
                // and triggers the Accessibility Service. We'll connect it through
                // ScreenContentChanged.
            }
            is DiaryBuddyIntent.ScreenContentChanged -> {
                //if the screen content is changed, we update the current screen content to new result
                _state.update {
                    it.copy(currentScreenContent = intent.content, currentAppName = intent.appName, isLoading = true)
                }
                //relaunch the app by update view model
                viewModelScope.launch {
                    repository.sendToGenerativeAI(_state.value.currentScreenContent, _state.value.currentAppName, "summarize")
                }
            }
            is DiaryBuddyIntent.StopSpeaking -> {
                repository.stopSpeaking()
                _state.update { it.copy(isSpeaking = false, spokenText = "") }
            }
            DiaryBuddyIntent.ReadAll -> {
                viewModelScope.launch{
                    repository.sendToGenerativeAI(_state.value.currentScreenContent, _state.value.currentAppName, "read all")
                }
            }
            DiaryBuddyIntent.Summarize -> {
                viewModelScope.launch{
                    repository.sendToGenerativeAI(_state.value.currentScreenContent, _state.value.currentAppName, "summarize")
                }
            }
            is DiaryBuddyIntent.UserSpeak -> TODO()
        }
    }

    private fun observeRepositoryResults() {
        repository.aiResponse
            .onEach { result: String ->
                _state.update { it.copy(spokenText = result, isSpeaking = true, isLoading = false) }
            }
            .launchIn(viewModelScope)

        repository.ttsCompletion
            .onEach {
                _state.update { it.copy(isSpeaking = false)}
            }
            .launchIn(viewModelScope)
    }
}