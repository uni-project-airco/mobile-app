package com.example.safeairapp.ui.screens

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.safeairapp.R
import com.example.safeairapp.SafeAirApplication
import com.example.safeairapp.services.PubNubService
import com.example.safeairapp.ui.theme.Montserrat
import java.util.concurrent.atomic.AtomicInteger
import kotlin.String

data class Recommendation(
    val id: Int,
    val title: String,
    val parameter: String,
    val value: Float,
    val level: String,
    val direction: String,
    val actions: List<String>,
    val completed: Boolean = false
)

val sampleRecommendations = emptyList<Recommendation>()
private val titleMap = mapOf("co2" to "Improve Ventilation", "humidity" to "Optimize Moisture Control",
    "temperature" to "Adjust Heating/Cooling", "pm25" to "Enhance Air Filtration")

private val actionsMap = mapOf(
    "temperature" to listOf(
        "Lower thermostat or turn on cooling",
        "Increase airflow with fans or ventilation",
        "Close blinds/curtains to reduce heat gain",
        "Avoid using heat-producing appliances"
    ),
    "humidity" to listOf(
        "Turn on a dehumidifier",
        "Increase ventilation in wet areas",
        "Fix leaks or remove standing water",
        "Avoid drying clothes indoors"
    ),
    "co2" to listOf(
        "Open windows for fresh air",
        "Turn on mechanical ventilation",
        "Reduce room occupancy",
        "Take breaks outdoors to lower accumulated CO₂"
    ),
    "pm25" to listOf(
        "Turn on an air purifier with a HEPA filter",
        "Keep windows closed during outdoor pollution events",
        "Avoid smoking, candles, or frying indoors",
        "Clean surfaces and vacuum with a particle filter"
    )
)

private val recommendationsIdCounter = AtomicInteger(1000)

private fun generateRecommendation(data: PubNubService.NotificationData): Recommendation {
    val indicator = data.message.split(' ')[0]
    val title = titleMap[indicator]
    val value = data.value.toFloat()
    val level = data.status

    val actions = when(level) {
        "high" -> actionsMap[indicator]
        else -> actionsMap[indicator]?.slice(0..1)
    }

    return Recommendation(
        id = recommendationsIdCounter.getAndIncrement(),
        title = title ?: "",
        parameter = indicator,
        value = value,
        level = level,
        direction = "high",
        actions = actions ?: emptyList(),
        completed = false
    )

}
@Composable
fun TipsScreen(
    selectedTab: String,
    onTabSelected: (String) -> Unit,
    notifications: Int = 2,
    onNotificationsClick: () -> Unit
) {
    var activeList by remember { mutableStateOf(sampleRecommendations.toMutableList()) }
    var completedList by remember { mutableStateOf(mutableListOf<Recommendation>()) }

    val context = LocalContext.current
    val application = context.applicationContext as SafeAirApplication
    val pubNubService = remember { application.pubNubService }

    // Collect PubNub notifications
    val pubNubRecommendations by pubNubService.notifications.collectAsState()
    var processedRecommendationKeys by remember {
        mutableStateOf<Set<String>>(emptySet())
    }

    LaunchedEffect(pubNubRecommendations) {
        val newPubNubItems = pubNubRecommendations.mapNotNull { pubNubData ->
            val recommendationKey =
                "${pubNubData.timestamp}_${pubNubData.title}_${pubNubData.message}_R"

            val level = pubNubData.status
            Log.d("STATUS", level)
            if ((level == "high" || level == "warning") && recommendationKey !in processedRecommendationKeys) {
                val recommendationItem = generateRecommendation(pubNubData)

                processedRecommendationKeys = processedRecommendationKeys + recommendationKey

                recommendationItem
            } else {
                null
            }
        }
        if (newPubNubItems.isNotEmpty()) {
            activeList = (newPubNubItems + activeList).toMutableList()
        }
    }

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

            Spacer(modifier = Modifier.height(20.dp))

            Column(modifier = Modifier.padding(horizontal = 24.dp)) {

                ActiveRecommendationsCard(
                    count = activeList.size
                )

                Spacer(modifier = Modifier.height(28.dp))

                Text(
                    text = "Action Required",
                    fontSize = 22.sp,
                    fontFamily = Montserrat,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )

                Spacer(modifier = Modifier.height(18.dp))

                activeList.forEach { rec ->
                    RecommendationCard(
                        rec = rec,
                        onCompleted = { completed ->
                            activeList = activeList.filter { it.id != completed.id }.toMutableList()
                            completedList = (completedList + completed.copy(completed = true)).toMutableList()
                        }
                    )
                    Spacer(modifier = Modifier.height(22.dp))
                }

                Spacer(modifier = Modifier.height(32.dp))

                Text(
                    text = "Completed",
                    fontSize = 22.sp,
                    fontFamily = Montserrat,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )

                Spacer(modifier = Modifier.height(14.dp))

                if (completedList.isEmpty()) {
                    Text(
                        text = "No completed recommendations yet.",
                        fontSize = 16.sp,
                        fontFamily = Montserrat,
                        fontWeight = FontWeight.Medium,
                        color = Color.Gray
                    )
                } else {
                    completedList.forEach { rec ->
                        CompletedRecommendationCard(rec)
                        Spacer(modifier = Modifier.height(18.dp))
                    }
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
                    painter = painterResource(id = R.drawable.safeair_logo_w),
                    contentDescription = "Logo",
                    modifier = Modifier.size(40.dp)
                )

                Box(
                    modifier = Modifier.size(52.dp),
                    contentAlignment = Alignment.Center
                ) {

                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .background(
                                color = Color.White.copy(alpha = 0.12f),
                                shape = RoundedCornerShape(50)
                            )
                    )

                    Image(
                        painter = painterResource(R.drawable.notification_w),
                        contentDescription = "Notifications",
                        modifier = Modifier
                            .size(24.dp)
                            .clickable { onNotificationsClick() }
                    )

                    if (notifications > 0) {
                        Box(
                            modifier = Modifier
                                .size(22.dp)
                                .align(Alignment.TopEnd)
                                .offset(x = 5.dp, y = (-2).dp),
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
                                fontSize = 14.sp,
                                fontFamily = Montserrat,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.offset(y = (-1).dp)
                            )
                        }
                    }
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

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "Personalized tips to improve your air quality",
                fontSize = 16.sp,
                fontFamily = Montserrat,
                fontWeight = FontWeight.Normal,
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
                    fontFamily = Montserrat,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Black
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = count.toString(),
                    fontSize = 32.sp,
                    fontFamily = Montserrat,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
            }

            Image(
                painter = painterResource(R.drawable.accept_mark),
                contentDescription = null,
                modifier = Modifier
                    .size(56.dp)
                    .align(Alignment.Bottom)
            )
        }
    }
}

@Composable
fun RecommendationCard(
    rec: Recommendation,
    onCompleted: (Recommendation) -> Unit
) {

    var expanded by remember { mutableStateOf(false) }

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
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {

                Column(modifier = Modifier.widthIn(max = 220.dp)) {

                    Text(
                        text = rec.title,
                        fontSize = 20.sp,
                        fontFamily = Montserrat,
                        fontWeight = FontWeight.Bold,
                        maxLines = 2,
                        color = Color.Black
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Box(
                        modifier = Modifier
                            .background(Color(0xFFF2F2F2), RoundedCornerShape(8.dp))
                            .padding(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = rec.parameter,
                            color = Color.Black,
                            fontFamily = Montserrat,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 13.sp
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
                "CO₂" ->
                    "Your CO₂ levels are elevated at ${rec.value} ppm. This can cause drowsiness and reduced concentration. Immediate action is recommended."
                "Humidity" ->
                    "Humidity is at ${rec.value}%, which is below the optimal range of 40–60%. Low humidity can cause dry skin and respiratory discomfort."
                else ->
                    "Current ${rec.parameter} level is ${rec.value}."
            }

            Text(
                text = description,
                fontFamily = Montserrat,
                fontSize = 15.sp,
                lineHeight = 20.sp,
                fontWeight = FontWeight.Medium,
                color = Color.Black
            )

            Spacer(modifier = Modifier.height(14.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { expanded = !expanded },
                horizontalArrangement = Arrangement.SpaceBetween
            ) {

                Text(
                    text = if (expanded) "Hide recommended actions" else "View recommended actions",
                    fontFamily = Montserrat,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 16.sp,
                    color = Color.Black
                )

                Text(
                    text = if (expanded) "▲" else "▼",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
            }

            if (expanded) {

                Spacer(modifier = Modifier.height(10.dp))

                rec.actions.forEach {
                    Text(
                        text = "▶ $it",
                        fontSize = 15.sp,
                        fontFamily = Montserrat,
                        fontWeight = FontWeight.Medium,
                        color = Color.Black,
                        modifier = Modifier.padding(vertical = 3.dp)
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.Black, RoundedCornerShape(12.dp))
                        .clickable { onCompleted(rec) }
                        .padding(vertical = 10.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Mark as Completed",
                        color = Color.White,
                        fontFamily = Montserrat,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 17.sp
                    )
                }
            }
        }
    }
}

@Composable
fun CompletedRecommendationCard(rec: Recommendation) {

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFFF7F7F7), RoundedCornerShape(24.dp))
            .border(1.dp, Color(0xFFBEBEBE), RoundedCornerShape(24.dp))
            .padding(24.dp)
    ) {
        Column {

            Text(
                text = rec.title,
                fontFamily = Montserrat,
                fontWeight = FontWeight.SemiBold,
                fontSize = 18.sp,
                color = Color(0xFF0AA60F)
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "Completed",
                fontSize = 14.sp,
                fontFamily = Montserrat,
                fontWeight = FontWeight.Medium,
                color = Color.DarkGray
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