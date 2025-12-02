package com.example.safeairapp.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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

            TipsHeader(notifications = notifications, onNotificationsClick = onNotificationsClick)

            Spacer(modifier = Modifier.height(24.dp))

            Column(modifier = Modifier.padding(horizontal = 24.dp)) {

                ActiveRecommendationsCard(count = sampleRecommendations.size)

                Spacer(modifier = Modifier.height(26.dp))

                Text(
                    text = "Action Required",
                    fontSize = 22.sp,
                    fontFamily = Montserrat,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(20.dp))

                sampleRecommendations.forEach { rec ->
                    RecommendationCard(rec)
                    Spacer(modifier = Modifier.height(20.dp))
                }

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
                    painter = painterResource(R.drawable.safeair_logo_w),
                    contentDescription = "",
                    modifier = Modifier.size(40.dp)
                )

                // Notification icon
                Box(
                    modifier = Modifier.size(52.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(R.drawable.notification_w),
                        contentDescription = "Notifications",
                        modifier = Modifier
                            .size(24.dp)
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

@Composable
fun RecommendationCard(rec: Recommendation) {

    val badgeBackground = when (rec.level) {
        "high" -> Color(0xFFE57373).copy(alpha = 0.25f)
        "medium" -> Color(0xFFFFC85C).copy(alpha = 0.25f)
        else -> Color(0xFFE0E0E0)
    }

    val badgeTextColor = when (rec.level) {
        "high" -> Color(0xFFB24343)
        "medium" -> Color(0xFFC88F1F)
        else -> Color.DarkGray
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White, RoundedCornerShape(24.dp))
            .border(1.dp, Color(0xFFD9D9D9), RoundedCornerShape(24.dp))
            .padding(24.dp)
    ) {

        Column {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                Column(modifier = Modifier.widthIn(max = 220.dp)) {

                    Text(
                        text = rec.title,
                        fontSize = 20.sp,
                        fontFamily = Montserrat,
                        fontWeight = FontWeight.Bold,
                        maxLines = 2
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Box(
                        modifier = Modifier
                            .background(Color(0xFFF2F2F2), RoundedCornerShape(8.dp))
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = rec.parameter,
                            fontSize = 13.sp,
                            color = Color.Black,
                            fontFamily = Montserrat
                        )
                    }
                }

                Box(
                    modifier = Modifier
                        .background(badgeBackground, RoundedCornerShape(10.dp))
                        .padding(horizontal = 14.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = rec.level.replaceFirstChar { it.uppercase() },
                        fontSize = 14.sp,
                        color = badgeTextColor,
                        fontFamily = Montserrat,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            val description = when (rec.parameter) {
                "CO₂" -> "Your CO₂ levels are elevated at ${rec.value} ppm. This can cause drowsiness and reduced concentration."
                "Humidity" -> "Humidity is at ${rec.value}%, which is below the optimal range of 40–60%."
                else -> "Current ${rec.parameter} level is ${rec.value}."
            }

            Text(
                text = description,
                fontSize = 15.sp,
                fontFamily = Montserrat,
                lineHeight = 20.sp,
                color = Color.Black
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
