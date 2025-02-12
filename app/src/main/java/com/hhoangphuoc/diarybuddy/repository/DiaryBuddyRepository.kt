package com.hhoangphuoc.diarybuddy.repository

import android.content.Context
import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener
import android.util.Log
import android.view.accessibility.AccessibilityNodeInfo
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.content
import com.hhoangphuoc.diarybuddy.util.extractTextFromNode
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

/**
 * This class contains the connections between the app  with the external Generative Model,
 * reserved by the context `Context`
 */
class DiaryBuddyRepository(private val generativeModel: GenerativeModel, private val context: Context, private val viewModelScope: CoroutineScope) {

    //variables of the generative models
    private val _aiResponse = MutableSharedFlow<String>() //the response can be shared between multiple states
    val aiResponse = _aiResponse.asSharedFlow()

    private val _ttsCompletion = MutableSharedFlow<Unit>() //the state of TTS, whether or not it failed or completed
    val ttsCompletion =  _ttsCompletion.asSharedFlow()

    private var textToSpeech: TextToSpeech? =  null

    init {
        initializeTTS() //initially, run the TTS
    }

    private fun initializeTTS(){
        textToSpeech = TextToSpeech(context) {
            status ->
            if (status == TextToSpeech.SUCCESS){
                //output the result to speech
                val result = textToSpeech?.setLanguage(java.util.Locale.US) //set the language

                //handle if missing data
                if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                    Log.e("DiaryBuddyRepository", "TTS language is not supported.")
                }

                //display the text through speech
                //handle different utterence by configure `UtteranceProgressListener`
                textToSpeech?.setOnUtteranceProgressListener(object: UtteranceProgressListener() {
                    override fun onStart(utteranceId: String?) {

                    }

                    /**
                     * Called when the text-to-speech engine has finished speaking an utterance.
                     *
                     * @param utteranceId The ID of the utterance that has been spoken.
                     */
                    override fun onDone(utteranceId: String?) {
                        viewModelScope.launch {
                            _ttsCompletion.emit(Unit)
                        }
                    }

                    @Deprecated("Deprecated in Java")
                    override fun onError(utteranceId: String?) {
                    }

                })
            }
            else {
                Log.e("ScreenGlanceRepository", "TTS initialization failed.")
            }
        }
    }
    suspend fun sendToGenerativeAI(screenContent: String, appName: String, userCommand: String) {
        withContext(Dispatchers.IO) {
            try {
                val prompt = """
                    You are a helpful voice assistant that reads and summarizes screen content for Android users.
                    Analyze the following text from the user's screen, which came from the application: $appName.
                    Determine the type of content (email, webpage, chat, document, etc.).
                    Based on the content type, provide either a concise summary or the full text, as requested by the user.
                    If the user says "summarize", prioritize conciseness. If the user says "read all", provide the full text.
                    If the user says "What app is this?" return the $appName.
                    If the user says "Who sent this?", extract and return the sender's name or identifier from the text.
                    If the user says "What time is it?", give the current time. Don't refer to the text content.
                    Use clear and natural language. Be concise and avoid unnecessary repetition.
                    Start your response immediately. Do not include any preamble like "Here is the summary..."

                    TEXT:
                    $screenContent

                    USER COMMAND: $userCommand
                    """.trimIndent()


                val inputContent = content {
                    text(prompt)
                }

                generativeModel.generateContentStream(inputContent).collect { chunk ->
                    _aiResponse.emit(chunk.text ?: "")
                    speak(chunk.text ?: "") // Directly speak as chunks arrive
                }


            } catch (e: Exception) {
                Log.e("ScreenGlanceRepository", "Error in sendToGenerativeAI: ${e.message}", e)
                _aiResponse.emit("Error: ${e.message}") // Or some other error handling
                speak("An error occurred: ${e.message}")

            }
        }
    }
    fun speak(text: String) {
        textToSpeech?.speak(text, TextToSpeech.QUEUE_FLUSH, null, "ScreenGlanceUtteranceId")
    }

    fun stopSpeaking(){
        textToSpeech?.stop()
    }

    fun extractTextFromScreen(rootNode: AccessibilityNodeInfo?): String {
        return  extractTextFromNode(rootNode)
    }

    fun shutdown() {
        textToSpeech?.stop()
        textToSpeech?.shutdown()
    }
}