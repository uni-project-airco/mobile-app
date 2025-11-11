package com.example.safeairapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.safeairapp.ui.screens.LoginScreen
import com.example.safeairapp.ui.screens.RegisterScreen
import com.example.safeairapp.ui.theme.SafeAirAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SafeAirAppTheme {
                RegisterScreen()
            }
        }
    }
}

