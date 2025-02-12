package com.hhoangphuoc.diarybuddy.domain.model

data class ChatMessage (
    val text: String,
    val isUser: Boolean,
    val timestamp: Long = System.currentTimeMillis()
)