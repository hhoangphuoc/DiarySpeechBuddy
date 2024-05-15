package com.hhoangphuoc.diarybuddy.speech

//message handler for wearables
import android.content.Intent
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer


/**
 * Functions that handling speech recognition in the app
 */
fun startSpeechRecognition(speechRecognizer: SpeechRecognizer) {
    val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
    intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
    speechRecognizer.startListening(intent)
}
fun stopSpeechRecognition(speechRecognizer: SpeechRecognizer) {
    speechRecognizer.stopListening()
}

/**
 * Handling speech recognition connected with wearable devices
 */
fun connectToWearableDevice() {


//    val nodeClient = Wearable.getNodeClient(this)
//
//    nodeClient.addListener(object : NodeClient.NodeListener {
//        override fun onPeerConnected(peerId: NodeId) {
//            // Send a message to the wearable device to start speech recognition
//            val message = "start_speech_recognition"
//            Wearable.getMessageClient(this@MainActivity).sendMessage(peerId, message, null)
//        }
//
//        override fun onPeerDisconnected(peerId: NodeId) {
//            // Handle peer disconnection
//        }
//    })
}
//private fun sendStartSpeechRecognitionMessage() {
//    val message = "start_speech_recognition"
//    Wearable.getMessageClient(this).sendMessage(nodeId, message, null)
//}
//
//private fun sendStartSpeechRecognitionMessage() {
//    val message = "start_speech_recognition"
//    Wearable.getMessageClient(this).sendMessage(nodeId, message, null)
//}
//
//private fun receiveMessagesFromWearableDevice() {
//    Wearable.getMessageClient(this).addListener(object : MessageListener {
//        override fun onMessageReceived(messageEvent: MessageEvent) {
//            // Process messages from the wearable device
//        }
//    })
//}