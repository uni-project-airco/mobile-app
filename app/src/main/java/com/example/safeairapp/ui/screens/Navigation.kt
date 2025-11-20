package com.example.safeairapp.ui.screens

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "login"
    ) {
        composable("login") {
            LoginScreen(
                onSignUpClick = { navController.navigate("register")},
                onSignInClick = { navController.navigate("home")}
            )
        }
        composable("register") {
            RegisterScreen(
                onLoginClick = { navController.navigate("login") },
                onSignUpComplete = { navController.navigate("home") }
            )
        }
        composable("home") {
            HomeScreen()
        }

        composable("history") {
            HistoryScreen()
        }
    }
}