package com.hhoangphuoc.diarybuddy.auth

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavController


@Composable
fun SignupPage(modifier: Modifier, navController: NavController, authViewModel: AuthViewModel ) {
    var email by remember {
        mutableStateOf("")
    }
    var password by remember {
        mutableStateOf("")
    }

    val authState = authViewModel.authState.observeAsState() // Observe the authState in LiveData

    //Trigger the navigation effect when user authenticated
    LaunchedEffect(authState.value) {
        if (authState.value == ManualAuthState.Authenticated) {
            navController.navigate("home")
        }
    }

    //TODO: Create an UI for the Sign Up page

}