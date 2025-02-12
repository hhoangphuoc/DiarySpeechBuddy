package com.hhoangphuoc.diarybuddy.model

//class DiaryBuddyState {
//}
/**
 * THIS IS THE STATE CLASS OF THE APP
 */
data class DiaryBuddyState(
    val isSpeaking: Boolean =  false, //speaking state of the audio
    val spokenText: String = "", // spoken text, capture by Speech Recognition
    val currentScreenContent: String = "", //screen content that captured
    val currentAppName: String = "",
    val isLoading: Boolean = false,
    val error: String? = null
)