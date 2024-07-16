package com.hhoangphuoc.diarybuddy.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.hhoangphuoc.diarybuddy.auth.AuthViewModel

@Composable
fun HomePage (modifier: Modifier, navController: NavController, authViewModel: AuthViewModel) {

    //TODO: Uncommented once the connection is established
//    val authState = authViewModel.authState.observeAsState() //checking the authentication status of the user
//
//    LaunchedEffect(authState.value) {
//        when (authState.value) {
//            is ManualAuthState.Unauthenticated -> navController.navigate("login")  //if user is not authenticated, navigate to login page
//            else -> Unit //stay in the page
//        }
//    }

    //content of the home page
    //TODO: Create UI for the home page
    Column (
        modifier = modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Home Page", fontSize = 32.sp)

        Text(text = "Welcome to Diary Buddy!")

        TextButton(
            onClick = { authViewModel.logout() }
        ) {
            Text(text = "Sign Out")
        }
    }
}