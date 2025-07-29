package com.example.messagingapp.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.messagingapp.presentation.screens.AllScreens
import com.example.messagingapp.presentation.screens.MainHostScreen
import com.example.messagingapp.presentation.screens.contact.ContactScreen
import com.example.messagingapp.presentation.screens.login.LoginScreen
import com.example.messagingapp.presentation.screens.signup.SignUpScreen
import com.example.messagingapp.presentation.screens.splash.SplashScreen
@Composable
fun Navigation() {
    val navController = rememberNavController()
    NavHost(
        navController = navController,
        startDestination = AllScreens.SplashScreen.name
    ) {
        composable(AllScreens.SplashScreen.name) {
            SplashScreen(navController = navController)
        }

        composable(AllScreens.LoginScreen.name) {
            LoginScreen(navController = navController)
        }

        composable(AllScreens.SignUpScreen.name) {
            SignUpScreen(navController = navController)
        }

        composable(
            route = "${AllScreens.ContactScreen.name}/{myUsername}/{contactUsername}",
            arguments = listOf(
            navArgument("myUsername") { type = NavType.StringType },
            navArgument("contactUsername") { type = NavType.StringType },
            )
        ){
            val myUsername = it.arguments?.getString("myUsername") ?: ""
            val contactUsername = it.arguments?.getString("contactUsername") ?: ""
            ContactScreen(navController = navController,
                loggedInUsername = myUsername,
                contactUsername = contactUsername)
        }

        composable("${AllScreens.MainHostScreen.name}/{username}") { backStackEntry ->
            val username = backStackEntry.arguments?.getString("username") ?: ""
            MainHostScreen(loggedInUsername = username, navControllerRoot = navController)
        }
    }
}