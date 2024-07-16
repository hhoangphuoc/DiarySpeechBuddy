package com.hhoangphuoc.diarybuddy

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.hhoangphuoc.diarybuddy.auth.AuthViewModel
import com.hhoangphuoc.diarybuddy.auth.LoginPage
import com.hhoangphuoc.diarybuddy.auth.SignupPage
import com.hhoangphuoc.diarybuddy.home.HomePage


@Composable
fun AppNavigation(modifier: Modifier = Modifier, authViewModel: AuthViewModel){
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "home", //FIXME: When authentication is connected, changed to "login"
        builder = {
                composable("home"){
                    HomePage(modifier, navController, authViewModel)
                }
                composable("login"){
                    LoginPage(modifier, navController, authViewModel)
                }
                composable("signup"){
                    SignupPage(modifier = modifier, navController = navController, authViewModel = authViewModel)
                }
            }
        )
}