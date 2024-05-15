package com.hhoangphuoc.diarybuddy

//import local classes
import android.os.Bundle
import android.speech.SpeechRecognizer
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.hhoangphuoc.diarybuddy.speech.DiaryRecognitionListener
import com.hhoangphuoc.diarybuddy.ui.theme.DiaryBuddyTheme


class MainActivity : ComponentActivity() {

    //speech objects
    private var diarySpeechRecognizer: SpeechRecognizer? = null
    private var diaryRecognitionListener: DiaryRecognitionListener? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize the SpeechRecognizer
        diarySpeechRecognizer = SpeechRecognizer.createSpeechRecognizer(this)

        //UI CONTENT
        setContent {
            DiaryBuddyTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Greeting("Android")
                }
            }
        }
    }

}




//UI RENDERED CONTENT
@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    DiaryBuddyTheme {
        Greeting("Android")
    }
}
