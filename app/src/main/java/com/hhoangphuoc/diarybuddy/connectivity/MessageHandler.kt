package com.hhoangphuoc.diarybuddy.connectivity

import android.content.Context
import com.google.android.gms.tasks.Tasks
import com.google.android.gms.wearable.CapabilityClient
import com.google.android.gms.wearable.CapabilityInfo
import com.google.android.gms.wearable.Wearable


private const val SPEECH_RECOGNITION_CAPABILITY_NAME = "speech_recognition"

/**
 * Sets up the capability for voice transcription. Why in wearable?
 * -> To transfer this capability to the wearable device.
 */
private fun setupVoiceTranscription(context : Context) {
    val capabilityInfo: CapabilityInfo = Tasks.await(
        Wearable.getCapabilityClient(context)
            .getCapability(
                SPEECH_RECOGNITION_CAPABILITY_NAME,
                CapabilityClient.FILTER_REACHABLE
            )
    )
    // capabilityInfo has the reachable nodes with the transcription capability
    updateTranscriptionCapability(capabilityInfo)
}

fun updateTranscriptionCapability(capabilityInfo: CapabilityInfo) {


}

//setupVoiceTranscription(this)
