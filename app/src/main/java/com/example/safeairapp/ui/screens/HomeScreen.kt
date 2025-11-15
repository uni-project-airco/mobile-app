package com.example.safeairapp.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.safeairapp.R

@Composable
fun HomeScreen(airQualityValue: Float = 65f, notifications: Int = 2) {

    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {

        val totalHeight = maxHeight
        val headerHeight = totalHeight * 0.8f
        val whiteBlockStart = totalHeight * 0.75f

        AirQualityHeader(
            airValue = airQualityValue,
            modifier = Modifier
                .fillMaxWidth()
                .height(headerHeight)
        )

        HeaderBar(notifications = notifications)

        Surface(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = whiteBlockStart)
                .verticalScroll(rememberScrollState()),
            color = Color.White,
            shape = RoundedCornerShape(topStart = 40.dp, topEnd = 40.dp)
        ) {
            Column(Modifier.padding(24.dp)) {
                Text("Content goes here...")
                Spacer(modifier = Modifier.height(1200.dp))
            }
        }
    }
}

fun getGradientForAirQuality(value: Float): Brush {
    return when {
        value >= 80f -> Brush.verticalGradient(
            listOf(Color(0xFF80E4FF), Color(0xFF23CFFF))
        )
        value >= 60f -> Brush.verticalGradient(
            listOf(Color(0xFF98F477), Color(0xFF7BF439))
        )
        value >= 40f -> Brush.verticalGradient(
            listOf(Color(0xFFFFEA71), Color(0xFFFFD323))
        )
        value >= 20f -> Brush.verticalGradient(
            listOf(Color(0xFFFFBF71), Color(0xFFFA7121))
        )
        else -> Brush.verticalGradient(
            listOf(Color(0xFFFF8F71), Color(0xFFF53C27))
        )
    }
}

@Composable
fun AirQualityHeader(airValue: Float, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .background(getGradientForAirQuality(airValue))
    )
}

@Composable
fun HeaderBar(notifications: Int) {
    Row (
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 40.dp, start = 24.dp, end = 24.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {

        Image(
            painter = painterResource(R.drawable.safeair_logo_b),
            contentDescription = "Logo",
            modifier = Modifier.height(40.dp)
        )

        Box(
            modifier = Modifier
                .size(42.dp),
            contentAlignment = Alignment.Center
        ) {

            Box(
                modifier = Modifier
                    .size(42.dp)
                    .background(
                        color = Color(0xFFFFF3D4).copy(alpha = 0.5f),
                        shape = RoundedCornerShape(50)
                    )
            )

            Image(
                painter = painterResource(R.drawable.notification),
                contentDescription = "Notifications",
                modifier = Modifier.size(20.dp)
            )

            if (notifications > 0) {
                Box(
                    modifier = Modifier
                        .size(18.dp)
                        .align(Alignment.TopEnd),
                    contentAlignment = Alignment.Center
                ) {
                    Box(
                        modifier = Modifier
                            .matchParentSize()
                            .background(Color.Red, RoundedCornerShape(50))
                    )

                    Text(
                        text = notifications.toString(),
                        color = Color.White,
                        fontSize = 11.sp,
                        modifier = Modifier.offset(y = (-3).dp)
                    )
                }
            }
        }
    }
}


@Preview(name = "Very Bad", showBackground = true, heightDp = 900)
@Composable
fun PreviewVeryBad() {
    HomeScreen(airQualityValue = 10f)
}

@Preview(name = "Bad", showBackground = true, heightDp = 900)
@Composable
fun PreviewBad() {
    HomeScreen(airQualityValue = 30f)
}

@Preview(name = "Poor", showBackground = true, heightDp = 900)
@Composable
fun PreviewPoor() {
    HomeScreen(airQualityValue = 45f)
}

@Preview(name = "Fair", showBackground = true, heightDp = 900)
@Composable
fun PreviewFair() {
    HomeScreen(airQualityValue = 65f)
}

@Preview(name = "Good", showBackground = true, heightDp = 900)
@Composable
fun PreviewGood() {
    HomeScreen(airQualityValue = 90f)
}
