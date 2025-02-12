package com.hhoangphuoc.diarybuddy.data.service

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

/**
 * Recognition Listener that handle speech recognition events
 */
class VoiceRecognitionService(
    private val context: Context //the recognition was binds with the context, instead of the app

) : RecognitionListener {

    private val _speechState = MutableStateFlow(
        DiarySpeechState(
            isSpeaking = false,
            languageCode = "en-US"
        )
    )
    val speechState = _speechState.asStateFlow()

    private val recogniser: SpeechRecognizer = SpeechRecognizer.createSpeechRecognizer(context)

    fun startListening(languageCode: String = "en-US") {

        //checking if the recogniser is available
//        if (!recogniser.isRecognitionAvailable(context)){
//            _speechState.update {
//                it.copy(
//                    error = "Speech recognition is not available"
//                )
//            }
//        }

        //Create the intent to specify what speech will be recognise
        val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
//            putExtra(
//                RecognizerIntent.EXTRA_LANGUAGE_MODEL,
//                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
//            )
//            putExtra(
//                RecognizerIntent.EXTRA_LANGUAGE, language
//            )
            //Adding the extras bundles to the intent for monitor the recognition
            putExtras(
                Bundle().apply {
                    putString(
                        RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                        RecognizerIntent.LANGUAGE_MODEL_FREE_FORM
                    )
                    putString(RecognizerIntent.EXTRA_LANGUAGE, languageCode)
                }
            )
        }

        //Start listening
        recogniser.setRecognitionListener(this)
        recogniser.startListening(intent)

        //update the state
        _speechState.update {
            it.copy(
                isSpeaking = true
            )
        }
    }

    fun stopListening() {
        _speechState.update {
            it.copy(
                isSpeaking = false
            )
        }
        recogniser.stopListening()
    }

    override fun onReadyForSpeech(params: Bundle?) {
        _speechState.update {
            it.copy(
                error = null
            )
        }
    }

    override fun onBeginningOfSpeech() {
        _speechState.update {
            it.copy(
                error = null
            )
        }
    }

    override fun onRmsChanged(rmsdB: Float) = Unit

    override fun onBufferReceived(buffer: ByteArray?) = Unit

    override fun onEndOfSpeech() {
        _speechState.update {
            it.copy(
                isSpeaking = false
            )
        }
    }

    override fun onError(error: Int) {
        if (error == SpeechRecognizer.ERROR_NO_MATCH) {
            _speechState.update {
                it.copy(
                    error = "No matching speech found"
                )
            }
        } else if (error == SpeechRecognizer.ERROR_CLIENT) {
            return
        }
        _speechState.update {
            it.copy(
                error = "Error: $error"
            )
        }
    }

    override fun onResults(results: Bundle?) {
        results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
            ?.getOrNull(0)
            ?.let { result ->
                _speechState.update {
                    it.copy(
                        spokenText = result
                    )
                }
            }
    }

    override fun onPartialResults(partialResults: Bundle?) = Unit

    override fun onEvent(eventType: Int, params: Bundle?) = Unit
}