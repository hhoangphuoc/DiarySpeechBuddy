package com.hhoangphuoc.connectivity.messaging

private const val SPEECH_RECOGNITION_CAPABILITY_NAME = ""
...
private fun setupVoiceTranscription() {
    val capabilityInfo: CapabilityInfo = Tasks.await(
        Wearable.getCapabilityClient(context)
            .getCapability(
                VOICE_TRANSCRIPTION_CAPABILITY_NAME,
                CapabilityClient.FILTER_REACHABLE
            )
    )
    // capabilityInfo has the reachable nodes with the transcription capability
    updateTranscriptionCapability(capabilityInfo)
}