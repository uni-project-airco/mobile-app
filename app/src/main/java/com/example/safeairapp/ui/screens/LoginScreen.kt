package com.example.safeairapp.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.safeairapp.R


@Composable
fun LoginScreen(modifier: Modifier = Modifier){
    val monsteratt = FontFamily(
        Font(R.font.montserrat_light, FontWeight.Light),
        Font(R.font.montserrat, FontWeight.Normal),
        Font(R.font.montserrat_medium, FontWeight.Medium),
        Font(R.font.montserrat_semibold, FontWeight.SemiBold),
        Font(R.font.montserrat_bold, FontWeight.Bold),
        Font(R.font.montserrat_thin, FontWeight.Thin)
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
            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Login",
                fontFamily = monsteratt,
                fontWeight = FontWeight.Medium,
                fontSize = 40.sp,
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Start
            )

            Spacer(modifier = Modifier.height(20.dp))
        }

    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    LoginScreen()
}