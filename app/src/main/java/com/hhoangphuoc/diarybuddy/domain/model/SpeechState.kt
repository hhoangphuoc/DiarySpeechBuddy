package com.hhoangphuoc.diarybuddy.domain.model

data class SpeechState(
    val isSpeaking: Boolean,
    val languageCode: String,
    val spokenText: String = "",
    val error: String? = null
)
