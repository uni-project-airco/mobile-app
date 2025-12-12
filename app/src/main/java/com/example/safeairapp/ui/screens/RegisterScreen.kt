package com.example.safeairapp.ui.screens

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
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
import androidx.compose.ui.platform.LocalContext
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
import com.example.safeairapp.api.RegisterRequest
import kotlinx.coroutines.launch
import retrofit2.HttpException
@Composable
fun RegisterScreen(modifier: Modifier= Modifier, onLoginClick: () -> Unit = {},
                   onSignUpComplete: () -> Unit = {}){

    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    var isLoading by remember { mutableStateOf(false) }

    var fullName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }

    var fullNameError by remember { mutableStateOf("") }
    var emailError by remember { mutableStateOf("") }
    var passwordError by remember { mutableStateOf("") }
    var confirmPasswordError by remember { mutableStateOf("") }

    val montserrat = FontFamily(
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

    Box(
        modifier = modifier.fillMaxSize()
    ){
        Image(
            painter = painterResource(R.drawable.register_background),
            contentDescription = "Background illustration",
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop,
            alpha = 0.2f
        )

        Column (
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 60.dp, vertical = 40.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(12.dp))

            Image(
                painter = painterResource(R.drawable.safeair_logo_text_b),
                contentDescription = "SafeAir logo",
                modifier = Modifier
                    .height(90.dp)
            )

            Spacer(modifier = Modifier.height(46.dp))

            Text(
                text = "Create Account!",
                fontFamily = montserrat,
                fontWeight = FontWeight.Medium,
                fontSize = 34.sp,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(56.dp))

            TextField(
                value = fullName,
                onValueChange = {
                    fullName = it
                    fullNameError = ""
                                },
                placeholder = {
                    Text(
                        "Full Name...",
                        fontFamily = montserrat,
                        fontSize = 18.sp
                    )
                },
                trailingIcon = {
                    Icon(
                        painter = painterResource(R.drawable.user_icon),
                        contentDescription = "User icon",
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
                    fontFamily = montserrat,
                    fontSize = 18.sp
                )
            )

            Text(
                text = fullNameError,
                color = Color.Red,
                fontSize = 12.sp,
                modifier = Modifier.align(Alignment.Start)
            )

            Spacer(modifier = Modifier.height(16.dp))

            TextField(
                value = email,
                onValueChange = {
                    email = it
                    emailError = ""
                                },
                placeholder = {
                    Text(
                        "Email...",
                        fontFamily = montserrat,
                        fontSize = 18.sp
                    )
                },
                trailingIcon = {
                    Icon(
                        painter = painterResource(R.drawable.mail),
                        contentDescription = "Email icon",
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
                    fontFamily = montserrat,
                    fontSize = 18.sp
                )
            )

            Text(
                text = emailError,
                color = Color.Red,
                fontSize = 12.sp,
                modifier = Modifier.align(Alignment.Start)
            )

            Spacer(modifier = Modifier.height(16.dp))

            TextField(
                value = password,
                onValueChange = {
                    password = it
                    passwordError = ""
                                },
                placeholder = {
                    Text(
                        "Password...",
                        fontFamily = montserrat,
                        fontSize = 18.sp
                    )
                },
                visualTransformation = PasswordVisualTransformation(),
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
                    fontFamily = montserrat,
                    fontSize = 18.sp
                )
            )

            Text(
                text = passwordError,
                color = Color.Red,
                fontSize = 12.sp,
                modifier = Modifier.align(Alignment.Start)
            )

            Spacer(modifier = Modifier.height(16.dp))

            TextField(
                value = confirmPassword,
                onValueChange = {
                    confirmPassword = it
                    confirmPasswordError = ""
                                },
                placeholder = {
                    Text(
                        "Confirm Password...",
                        fontFamily = montserrat,
                        fontSize = 18.sp
                    )
                },
                visualTransformation = PasswordVisualTransformation(),
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
                    fontFamily = montserrat,
                    fontSize = 18.sp
                )
            )

            Text(
                text = confirmPasswordError,
                color = Color.Red,
                fontSize = 12.sp,
                modifier = Modifier.align(Alignment.Start)
            )

            Spacer(modifier = Modifier.height(38.dp))

            Button(
                onClick = {
                    var valid = true

                    if (fullName.isBlank()) {
                        fullNameError = "Please enter your name"
                        valid = false
                    }
                    if (email.isBlank()) {
                        emailError = "Please enter your email"
                        valid = false
                    }
                    if (password.isBlank()) {
                        passwordError = "Please enter your password"
                        valid = false
                    }
                    if (confirmPassword.isBlank()) {
                        confirmPasswordError = "Please confirm your password"
                        valid = false
                    }
                    if (password != confirmPassword) {
                        confirmPasswordError = "Passwords do not match"
                        valid = false
                    }
                    if (valid) {

                        coroutineScope.launch {
                            isLoading = true
                            try {

                                val response = ApiClient.apiServices.registerUser(
                                    RegisterRequest(
                                        username = fullName,
                                        email = email,
                                        password = password,
                                        confirm_password = confirmPassword

                                    )
                                )
                                Toast.makeText(context, "Registration successful! Please log in.", Toast.LENGTH_LONG).show()
                                onLoginClick()
                            } catch (e: HttpException) {
                                val error = e.response()?.errorBody()?.string() ?: "Unknown error"
                                Toast.makeText(context, "Registration failed: $error", Toast.LENGTH_LONG).show()
                            }  catch (e: Exception) {
                                Toast.makeText(context, "An unexpected error occurred: ${e.message}", Toast.LENGTH_LONG).show()
                            } finally {
                                isLoading = false
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
                        text = "Sign up",
                        color = Color.White,
                        fontFamily = montserrat,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 22.sp
                    )
                }

            }

            Spacer(modifier = Modifier.height(64.dp))

            Text(
                text = "Already have an account? Log in",
                fontFamily = montserrat,
                fontWeight = FontWeight.Normal,
                fontSize = 16.sp,
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onLoginClick() },
                textAlign = TextAlign.Center
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RegisterScreenPreview() {
    RegisterScreen()
}