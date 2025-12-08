package com.example.safeairapp.ui.screens

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun AppNavigation() {
    val navController = rememberNavController()
    val activeRecommendationsList = remember { mutableStateListOf<Recommendation>() }
    val completedRecommendationsList = remember { mutableStateListOf<Recommendation>() }
    val processedRecommendationKeys = remember { mutableStateListOf<String>() }
    val temperatureRange = remember { mutableStateOf(28f..35f) }
    val humidityRange = remember { mutableStateOf(65f..80f) }
    val co2Range = remember { mutableStateOf(800f..1100f) }
    val pm25Range = remember { mutableStateOf(35f..55f) }

    NavHost(
        navController = navController,
        startDestination = "login"
    ) {
        composable("login") {
            LoginScreen(
                onSignUpClick = { navController.navigate("register") },
                onSignInClick = { navController.navigate("home") }
            )
        }
        composable("register") {
            RegisterScreen(
                onLoginClick = { navController.navigate("login") },
                onSignUpComplete = { navController.navigate("home") }
            )
        }
        composable("home") {
            HomeScreen(
                selectedTab = "home",
                onTabSelected = { navController.navigate(it) },
                onNotificationsClick = { navController.navigate("notifications") },
                temperatureRange = temperatureRange,
                humidityRange = humidityRange,
                co2Range = co2Range,
                pm25Range = pm25Range
            )
        }

        composable("history") {
            HistoryScreen(
                selectedTab = "history",
                onTabSelected = { navController.navigate(it) },
                onNotificationsClick = { navController.navigate("notifications") }
            )
        }

        composable("tips") {
            TipsScreen(
                selectedTab = "tips",
                onTabSelected = { navController.navigate(it) },
                onNotificationsClick = { navController.navigate("notifications") },
                activeList = activeRecommendationsList,
                completedList = completedRecommendationsList,
                processedRecommendationKeys = processedRecommendationKeys
            )
        }

        composable("notifications") {
            NotificationsScreen(
                selectedTab = "notifications",
                onTabSelected = { navController.navigate(it) }
            )
        }

        composable("settings") {
            SettingsScreen(
                selectedTab = "settings",
                onTabSelected = { navController.navigate(it) },
                onNotificationsClick = { navController.navigate("notifications") },
                onOpenThresholds = { navController.navigate("thresholds") }
            )
        }

        composable("thresholds") {
            AlertThresholdsScreen(
                onTabSelected = { navController.navigate(it) },
                onNotificationsClick = { navController.navigate("notifications") },
                temperatureRange = temperatureRange,
                humidityRange = humidityRange,
                co2Range = co2Range,
                pm25Range = pm25Range
            )
        }

    }
}