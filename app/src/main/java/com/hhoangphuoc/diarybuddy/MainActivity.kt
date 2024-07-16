package com.hhoangphuoc.diarybuddy

//import local classes
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import com.hhoangphuoc.diarybuddy.auth.AuthViewModel
import com.hhoangphuoc.diarybuddy.ui.theme.DiaryBuddyTheme


class MainActivity : ComponentActivity() {

//    //add speech recognition component to the model
//    //using lazy - only initialize when needed / it was activated
//    val diarySpeechRecognition by lazy {
//        DiarySpeechRecognition(application)
//    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()

        //authentication model
        val authViewModel : AuthViewModel by viewModels()

        //UI CONTENT
        setContent {
            DiaryBuddyTheme {
                // A surface container using the 'background' color from the theme
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
//                    color = MaterialTheme.colorScheme.background
                ) { innerpadding ->
                    AppNavigation(modifier = Modifier.padding(innerpadding), authViewModel)
                }
            }
        }
    }

}




//UI RENDERED CONTENT


//@Preview(showBackground = true)
//@Composable
//fun GreetingPreview() {
//    DiaryBuddyTheme {
////        Greeting("Android")
//        HomePage()
//    }
//}
