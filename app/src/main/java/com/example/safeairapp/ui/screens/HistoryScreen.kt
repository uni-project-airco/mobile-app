package com.example.safeairapp.ui.screens

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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.safeairapp.R
import com.example.safeairapp.ui.theme.Montserrat
import com.patrykandpatrick.vico.compose.axis.horizontal.rememberBottomAxis
import com.patrykandpatrick.vico.compose.axis.vertical.rememberStartAxis
import com.patrykandpatrick.vico.compose.chart.Chart
import com.patrykandpatrick.vico.compose.chart.line.lineChart
import com.patrykandpatrick.vico.core.entry.ChartEntryModelProducer
import com.patrykandpatrick.vico.core.entry.entryOf

data class Stats(
    val average: Float,
    val max: Float,
    val min: Float,
    val current: Float
)

@Composable
fun HistoryScreen(
    notifications: Int = 2,
    selectedTab: String = "history",
    onTabSelected: (String) -> Unit,
    onNotificationsClick: () -> Unit
) {
    var selectedCategory by remember { mutableStateOf("Temp") }
    var filterExpanded by remember { mutableStateOf(false) }
    var selectedFilter by remember { mutableStateOf("Last 24h") }

    Box(modifier = Modifier.fillMaxSize()) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .background(Color.White)
        ) {

            HistoryHeader(
                notifications = notifications,
                onNotificationsClick = onNotificationsClick
            )

            Spacer(modifier = Modifier.height(30.dp))

            CategorySwitcher(
                selected = selectedCategory,
                onSelect = { selectedCategory = it }
            )

            Spacer(modifier = Modifier.height(35.dp))

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Row(verticalAlignment = Alignment.CenterVertically) {
                    val icon = when (selectedCategory) {
                        "Temp" -> R.drawable.temperature_sensor
                        "Humidity" -> R.drawable.humidity
                        "CO₂" -> R.drawable.co2
                        else -> R.drawable.dust
                    }

                    Image(
                        painter = painterResource(icon),
                        contentDescription = null,
                        modifier = Modifier.size(24.dp)
                    )

                    Spacer(modifier = Modifier.width(10.dp))

                    Text(
                        text = when (selectedCategory) {
                            "Temp" -> "Temperature"
                            "Humidity" -> "Humidity"
                            "CO₂" -> "CO₂"
                            else -> "Dust"
                        },
                        fontFamily = Montserrat,
                        fontWeight = FontWeight.Bold,
                        fontSize = 22.sp
                    )
                }

                Box(
                    modifier = Modifier
                        .background(Color.White, RoundedCornerShape(20.dp))
                        .border(1.dp, Color(0xFFC9C9C9), RoundedCornerShape(20.dp))
                        .clickable { filterExpanded = !filterExpanded }
                        .padding(horizontal = 16.dp, vertical = 8.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = selectedFilter,
                            fontFamily = Montserrat,
                            fontWeight = FontWeight.Medium,
                            fontSize = 14.sp
                        )

                        Spacer(modifier = Modifier.width(8.dp))

                        Text(
                            text = if (filterExpanded) "▲" else "▼",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(30.dp))

            HistoryChart(selectedCategory, selectedFilter)

            Spacer(modifier = Modifier.height(45.dp))

            StatsGrid(selectedCategory, selectedFilter)

            Spacer(modifier = Modifier.height(135.dp))
        }

        if (filterExpanded) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 420.dp, end = 24.dp),
                contentAlignment = Alignment.TopEnd
            ) {
                Column(
                    modifier = Modifier
                        .width(160.dp)
                        .background(Color.White, RoundedCornerShape(16.dp))
                        .border(1.dp, Color(0xFFE0E0E0), RoundedCornerShape(16.dp))
                        .padding(vertical = 6.dp)
                ) {
                    listOf("Last 24h", "7 days", "1 month").forEach { option ->
                        Text(
                            text = option,
                            fontFamily = Montserrat,
                            fontSize = 15.sp,
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    selectedFilter = option
                                    filterExpanded = false
                                }
                                .padding(horizontal = 14.dp, vertical = 10.dp)
                        )
                    }
                }
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

fun getStats(data: List<Float>): Stats {
    return Stats(
        average = data.average().toFloat(),
        max = data.maxOrNull() ?: 0f,
        min = data.minOrNull() ?: 0f,
        current = data.lastOrNull() ?: 0f
    )
}

@Composable
fun StatCard(label: String, value: Float, unit: String, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .height(150.dp)
            .background(Color.White, RoundedCornerShape(26.dp))
            .border(1.dp, Color(0xFFCBCBCB), RoundedCornerShape(26.dp))
            .padding(24.dp)
    ) {
        Column {
            Text(
                text = label,
                fontFamily = Montserrat,
                fontSize = 20.sp,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = value.toInt().toString() + " " + unit,
                fontFamily = Montserrat,
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )
        }
    }
}



@Composable
fun StatsGrid(category: String, range: String) {
    val data = getChartData(category, range)
    val stats = getStats(data)

    val unit = when (category) {
        "Temp" -> "°C"
        "Humidity" -> "%"
        "CO₂" -> "ppm"
        else -> "µg/m³"
    }

    Column(modifier = Modifier.padding(horizontal = 24.dp)) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            StatCard("Average", stats.average, unit, modifier = Modifier.weight(1f))
            StatCard("Peak", stats.max, unit, modifier = Modifier.weight(1f))
        }

        Spacer(modifier = Modifier.height(20.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            StatCard("Minimum", stats.min, unit, modifier = Modifier.weight(1f))
            StatCard("Current", stats.current, unit, modifier = Modifier.weight(1f))
        }
    }
}


@Composable
fun CategorySwitcher(
    selected: String,
    onSelect: (String) -> Unit
) {
    val categories = listOf("Temp", "Humidity", "CO₂", "Dust")

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp)
            .background(Color(0xFFF0F0F0), RoundedCornerShape(30.dp))
            .padding(6.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        categories.forEach { item ->
            val isSelected = item == selected

            Box(
                modifier = Modifier
                    .background(
                        if (isSelected) Color.White else Color.Transparent,
                        RoundedCornerShape(20.dp)
                    )
                    .clickable { onSelect(item) }
                    .padding(horizontal = 14.dp, vertical = 10.dp)
            ) {
                Text(
                    text = item,
                    fontFamily = Montserrat,
                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                    fontSize = 16.sp,
                    color = Color.Black
                )
            }
        }
    }
}

fun getChartData(category: String, range: String): List<Float> {
    return when (category) {
        "Temp" -> when (range) {
            "Last 24h" -> listOf(20f, 21f, 22f, 23f, 22f, 24f, 25f)
            "7 days" -> listOf(18f, 19f, 20f, 22f, 21f, 23f, 24f)
            else -> listOf(17f, 18f, 19f, 20f)
        }

        "Humidity" -> listOf(40f, 45f, 43f, 47f, 50f)
        "CO₂" -> listOf(500f, 620f, 580f, 650f, 700f)
        else -> listOf(10f, 12f, 15f, 13f, 14f)
    }
}

@Composable
fun HistoryChart(category: String, range: String) {

    val data = getChartData(category, range)
    val entries = data.mapIndexed { index, value -> entryOf(index, value) }
    val modelProducer = ChartEntryModelProducer(entries)

    val bottomLabels = when (range) {
        "Last 24h" -> listOf(
            "00:00", "04:00", "08:00", "12:00", "16:00", "20:00", "24:00"
        )
        "7 days" -> listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")
        else -> listOf("1w", "2w", "3w", "4w")
    }

    val bottomAxis = rememberBottomAxis(
        valueFormatter = { x, _ ->
            val index = x.toInt()
            if (index in bottomLabels.indices) bottomLabels[index] else ""
        }
    )

    Chart(
        chart = lineChart(),
        chartModelProducer = modelProducer,
        startAxis = rememberStartAxis(),
        bottomAxis = bottomAxis,
        modifier = Modifier
            .fillMaxWidth()
            .height(250.dp)
            .padding(horizontal = 24.dp)
    )
}


@Composable
fun HistoryHeader(notifications: Int, onNotificationsClick: () -> Unit) {

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
                                Color.White.copy(alpha = 0.12f),
                                RoundedCornerShape(50)
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
                text = "History & Analytics",
                fontSize = 26.sp,
                fontFamily = Montserrat,
                fontWeight = FontWeight.SemiBold,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "View historical data and trends",
                fontSize = 16.sp,
                fontFamily = Montserrat,
                fontWeight = FontWeight.Normal,
                color = Color.White.copy(alpha = 0.7f)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewHistoryScreen() {
    HistoryScreen(
        notifications = 2,
        selectedTab = "history",
        onTabSelected = {},
        onNotificationsClick = {}
    )
}
