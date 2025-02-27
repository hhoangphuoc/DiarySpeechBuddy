package com.hhoangphuoc.diarybuddy.ui

import com.hhoangphuoc.diarybuddy.domain.model.ChatMessage
import com.hhoangphuoc.diarybuddy.domain.model.FeatureType

//data class DiaryBuddyState(
//    val isSpeaking: Boolean =  false, //speaking state of the audio
//    val spokenText: String = "", // spoken text, capture by Speech Recognition
//    val currentScreenContent: String = "", //screen content that captured
//    val currentAppName: String = "",
//    val isLoading: Boolean = false,
//    val error: String? = null
//)
/**
 * THIS IS THE STATE CLASS OF THE ENTIRE APP
 */
sealed class MainState {
    object Hidden : MainState()
    data class Active(
        val activeFeature: FeatureType = FeatureType.VOICE_ASSISTANT,
        val noteText: String = "",
        val chatMessages: List<ChatMessage> = emptyList(),
        val contentSummary: String = "",
        val isLoading: Boolean = false,
        val isListening: Boolean = false,
        val error: String? = null
    ) : MainState()
}