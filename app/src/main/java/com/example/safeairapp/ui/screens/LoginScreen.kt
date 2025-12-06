package com.example.safeairapp.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.safeairapp.R
import com.example.safeairapp.api.ApiClient
import com.example.safeairapp.api.LoginRequest
import kotlinx.coroutines.launch


@Composable
fun LoginScreen(modifier: Modifier = Modifier,
                onSignUpClick: () -> Unit = {},
                onSignInClick: () -> Unit = {}){
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    var emailError by remember { mutableStateOf("") }
    var passwordError by remember { mutableStateOf("") }
    var loginError by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    
    val coroutineScope = rememberCoroutineScope()

    val monsteratt = FontFamily(
        Font(R.font.montserrat_light, FontWeight.Light),
        Font(R.font.montserrat, FontWeight.Normal),
        Font(R.font.montserrat_medium, FontWeight.Medium),
        Font(R.font.montserrat_semibold, FontWeight.SemiBold),
        Font(R.font.montserrat_bold, FontWeight.Bold),
        Font(R.font.montserrat_thin, FontWeight.Thin)
    )

    val gradient = Brush.verticalGradient(
        colors = listOf(
            Color(0xFFFFFFFF),
            Color(0xFFD4D4D4)
        )
    )

    Column(modifier = modifier
        .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Image(
            painter = painterResource(R.drawable.login_image),
            contentDescription = "Login illustration",
            modifier = Modifier
                .fillMaxWidth()
                .height(450.dp),
            contentScale = ContentScale.Crop
        )

        Column(modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 60.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "Login",
                fontFamily = monsteratt,
                fontWeight = FontWeight.Medium,
                fontSize = 40.sp,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Start
            )

            Spacer(modifier = Modifier.height(40.dp))

            TextField(
                value = email,
                onValueChange = {
                    email = it
                    emailError = ""
                                },
                placeholder = {
                    Text(
                        "Email...",
                        fontFamily = monsteratt,
                        fontSize = 18.sp
                    )
                },
                trailingIcon = {
                    Icon(
                        painter = painterResource(R.drawable.mail),
                        contentDescription = "Email Icon",
                        tint = Color.Black,
                        modifier = Modifier.size(22.dp)
                    )
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
                    .background(brush = gradient, shape = RoundedCornerShape(15.dp))
                    .border(1.dp, Color.Black, RoundedCornerShape(15.dp)),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                shape = RoundedCornerShape(15.dp),
                textStyle = TextStyle(
                    fontFamily = monsteratt,
                    fontSize = 18.sp
                )
            )

            Text(
                text = emailError,
                color = Color.Red,
                fontSize = 12.sp,
                fontFamily = monsteratt,
                modifier = Modifier
                    .padding(start = 2.dp, top = 1.dp)
                    .align(Alignment.Start)
            )

            Spacer(modifier = Modifier.height(8.dp))

            TextField(
                value = password,
                onValueChange = {
                    password = it
                    passwordError = ""
                                },
                placeholder = {
                    Text(
                        "Password...",
                        fontFamily = monsteratt,
                        fontSize = 18.sp
                    )
                },
                visualTransformation = PasswordVisualTransformation(),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(51.dp)
                    .background(brush = gradient, shape = RoundedCornerShape(15.dp))
                    .border(1.dp, Color.Black, RoundedCornerShape(15.dp)),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    disabledContainerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent
                ),
                shape = RoundedCornerShape(15.dp),
                textStyle = TextStyle(
                    fontFamily = monsteratt,
                    fontSize = 18.sp
                )
            )

            Text(
                text = passwordError,
                color = Color.Red,
                fontSize = 12.sp,
                fontFamily = monsteratt,
                modifier = Modifier
                    .padding(start = 2.dp, top = 1.dp)
                    .align(Alignment.Start)
            )

            Spacer(modifier = Modifier.height(2.dp))

            Text(
                text = "Forgot Password?",
                fontFamily = monsteratt,
                fontWeight = FontWeight.Normal,
                fontSize = 14.sp,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.End
            )

            Spacer(modifier = Modifier.height(28.dp))

            if (loginError.isNotEmpty()) {
                Text(
                    text = loginError,
                    color = Color.Red,
                    fontSize = 14.sp,
                    fontFamily = monsteratt,
                    modifier = Modifier
                        .padding(bottom = 8.dp)
                        .fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
            }

            Button (
                onClick = {
                    var hasError = false
                    loginError = ""

                    if (email.isBlank()) {
                        emailError = "Please enter your email"
                        hasError = true
                    }

                    if (password.isBlank()) {
                        passwordError = "Please enter your password"
                        hasError = true
                    }

                    if (!hasError) {
                        isLoading = true
                        coroutineScope.launch {
                            try {
                                val response = ApiClient.apiServices.login(
                                    LoginRequest(
                                        username = email.trim(),
                                        password = password
                                    )
                                )
                                
                                if (response.code() == 200 && response.body() != null) {
                                    val loginResponse = response.body()!!
                                    if (loginResponse.access_token != null) {
                                        isLoading = false
                                        onSignInClick()
                                    } else {
                                        isLoading = false
                                        loginError = "Login failed"
                                    }
                                } else {
                                    isLoading = false
                                    loginError = response.message() ?: "Login failed. Please try again."
                                }
                            } catch (e: Exception) {
                                isLoading = false
                                loginError = "Network error: ${e.message ?: "Unable to connect to server"}"
                            }
                        }
                    }

                },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Black),
                shape = RoundedCornerShape(50),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(55.dp),
                enabled = !isLoading
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = Color.White
                    )
                } else {
                    Text(
                        text = "Sign in",
                        color = Color.White,
                        fontFamily = monsteratt,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 22.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(26.dp))

            Text(
                text = "Don’t have an account? Sign up",
                fontFamily = monsteratt,
                fontWeight = FontWeight.Normal,
                fontSize = 16.sp,
                modifier = Modifier
                    .clickable { onSignUpClick() }
            )
        }

    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    LoginScreen()
}