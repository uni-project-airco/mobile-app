package com.example.safeairapp.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.safeairapp.R
import com.example.safeairapp.ui.theme.Montserrat

data class Recommendation(
    val id: Int,
    val title: String,
    val parameter: String,
    val value: Float,
    val level: String,
    val direction: String,
)

val sampleRecommendations = listOf(
    Recommendation(
        id = 1,
        title = "Improve Ventilation",
        parameter = "CO₂",
        value = 850f,
        level = "high",
        direction = "high"
    ),
    Recommendation(
        id = 2,
        title = "Increase Humidity Levels",
        parameter = "Humidity",
        value = 35f,
        level = "medium",
        direction = "low"
    )
)

@Composable
fun TipsScreen(
    selectedTab: String,
    onTabSelected: (String) -> Unit,
    notifications: Int = 2,
    onNotificationsClick: () -> Unit
) {

    Box(modifier = Modifier.fillMaxSize()) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .background(Color.White)
        ) {

            TipsHeader(
                notifications = notifications,
                onNotificationsClick = onNotificationsClick
            )

            Spacer(modifier = Modifier.height(24.dp))

            Column(modifier = Modifier.padding(horizontal = 24.dp)) {

                ActiveRecommendationsCard(
                    count = sampleRecommendations.size
                )

                Spacer(modifier = Modifier.height(140.dp))
            }
        }

        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .padding(bottom = 8.dp)
        ) {
            BottomNavBar(
                selected = selectedTab,
                onTabSelected = onTabSelected
            )
        }
    }
}

@Composable
fun TipsHeader(notifications: Int, onNotificationsClick: () -> Unit) {

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(280.dp)
            .background(
                color = Color(0xFF1D1D1D),
                shape = RoundedCornerShape(bottomStart = 30.dp, bottomEnd = 30.dp)
            )
            .padding(horizontal = 24.dp, vertical = 50.dp)
    ) {

        Column {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Image(
                    painter = painterResource(id = R.drawable.safeair_logo_w),
                    contentDescription = "",
                    modifier = Modifier.size(40.dp)
                )

                Box(
                    modifier = Modifier.size(52.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(R.drawable.notification_w),
                        contentDescription = "Notifications",
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(40.dp))

            Text(
                text = "Recommendations",
                fontSize = 26.sp,
                fontFamily = Montserrat,
                fontWeight = FontWeight.SemiBold,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Personalized tips to improve your air quality",
                fontSize = 16.sp,
                fontFamily = Montserrat,
                color = Color.White.copy(alpha = 0.7f)
            )
        }
    }
}

@Composable
fun ActiveRecommendationsCard(count: Int) {

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFF5F5F5), RoundedCornerShape(20.dp))
            .padding(24.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Bottom
        ) {

            Column {
                Text(
                    text = "Active Recommendations",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    fontFamily = Montserrat
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = count.toString(),
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold,
                    fontFamily = Montserrat
                )
            }

            Image(
                painter = painterResource(R.drawable.accept_mark),
                contentDescription = "",
                modifier = Modifier.size(56.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewTipsScreen() {
    TipsScreen(
        selectedTab = "tips",
        onTabSelected = {},
        notifications = 2,
        onNotificationsClick = {}
    )
}
