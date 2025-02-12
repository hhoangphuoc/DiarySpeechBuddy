package com.hhoangphuoc.diarybuddy.ui.components

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Mic
import androidx.compose.material.icons.rounded.Stop
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.sp
import com.hhoangphuoc.diarybuddy.data.service.VoiceRecognitionService
import com.hhoangphuoc.diarybuddy.domain.model.SpeechState
import com.hhoangphuoc.diarybuddy.ui.MainEvent

@Composable
fun SpeechPage(
    speechState: SpeechState, // Receives state from MainViewModel
    onEvent: (MainEvent) -> Unit, // Sends events to MainViewModel
    modifier: Modifier = Modifier,
) {

    var canRecorded by remember {
        mutableStateOf(false)
    }

    //request permission to launch microphone - make sure it is granted
    val recordAudioLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { granted ->
            canRecorded = granted
        }
    )

    //LAUNCH THE MIC WHEN PERMISSION IS GRANTED
    LaunchedEffect(key1 = recordAudioLauncher) {
        recordAudioLauncher.launch(android.Manifest.permission.RECORD_AUDIO)
    }

    //content of the home page
    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = {
                    if (speechState.isSpeaking) {
                        onEvent(MainEvent.StopListening) // Send Stop event
                    } else {
                        onEvent(MainEvent.StartListening) // Send Start event
                    }
                }
            ) {
                AnimatedContent(targetState = speechState.isSpeaking, label = "speech-icon") {
                    if (it) {
                        Icon(imageVector = Icons.Rounded.Stop, contentDescription = "Stop")
                    } else {
                        Icon(imageVector = Icons.Rounded.Mic, contentDescription = "Speak")
                    }
                }
            }
        },
    ) { padding ->
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(padding),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = "Welcome to Diary Buddy!", fontSize = 24.sp)

            //THIS IS SPEECH BUTTON - ANIMATED WHEN SPEECH IS SPEAKING
            AnimatedContent(targetState = speechState.isSpeaking, label = "text-display") {
                if (it) {
                    Text(text = "Listening...")
                } else {
                    Text(text = speechState.spokenText.ifEmpty { "Click to Speak" })
                }
            }
        }

    }
}