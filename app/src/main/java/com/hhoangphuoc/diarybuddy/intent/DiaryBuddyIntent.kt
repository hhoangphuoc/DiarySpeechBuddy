package com.hhoangphuoc.diarybuddy.intent

/**
 * List of Intent (or user actions) that  interaction with app
 */
sealed class DiaryBuddyIntent {
    object Activate: DiaryBuddyIntent()
    data class UserSpeak (val text: String) : DiaryBuddyIntent()

    object StopSpeaking: DiaryBuddyIntent()
    data class ScreenContentChanged (val content: String, val appName: String) : DiaryBuddyIntent()

    object Summarize: DiaryBuddyIntent()
    object ReadAll: DiaryBuddyIntent()
}