package com.hhoangphuoc.diarybuddy.ui

import com.hhoangphuoc.diarybuddy.domain.model.FeatureType

sealed class MainEvent {
    object Activate : MainEvent()
    object Exit : MainEvent()
    data class FeatureSelected(val feature: FeatureType) : MainEvent()
    object StartListening : MainEvent()
    object StopListening : MainEvent() // Add this
    data class UserSpeak(val text: String) : MainEvent() //Might not need in this case
}