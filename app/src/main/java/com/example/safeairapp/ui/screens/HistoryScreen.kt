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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.safeairapp.R
import com.example.safeairapp.SafeAirApplication
import com.example.safeairapp.ui.theme.Montserrat
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import com.patrykandpatrick.vico.compose.axis.horizontal.rememberBottomAxis
import com.patrykandpatrick.vico.compose.axis.vertical.rememberStartAxis
import com.patrykandpatrick.vico.compose.chart.Chart
import com.patrykandpatrick.vico.compose.chart.line.lineChart
import com.patrykandpatrick.vico.core.entry.ChartEntryModelProducer
import com.patrykandpatrick.vico.core.entry.entryOf
import com.example.safeairapp.api.ApiClient
import com.example.safeairapp.api.HistoricalData


data class Stats(
    val average: Float,
    val max: Float,
    val min: Float,
    val current: Float
)

@Composable
fun HistoryScreen(
    selectedTab: String = "history",
    onTabSelected: (String) -> Unit,
    onNotificationsClick: () -> Unit
) {
    val context = LocalContext.current
    val application = context.applicationContext as SafeAirApplication
    val pubNubService = remember { application.pubNubService }

    val notificationsList by pubNubService.notifications.collectAsState()
    val notificationCount = notificationsList.size

    var selectedCategory by remember { mutableStateOf("Temp") }
    var filterExpanded by remember { mutableStateOf(false) }
    var selectedFilter by remember { mutableStateOf("Last 24h") }

    var daysTelemetry by remember { mutableStateOf<List<HistoricalData>?>(null) }
    var weekTelemetry by remember { mutableStateOf<List<HistoricalData>?>(null) }
    var isLoading by remember { mutableStateOf(false) }

    LaunchedEffect(selectedFilter) {
        isLoading = true
        try {
            val response = ApiClient.apiServices.getHistoricalData()

            if (response.code() == 200 && response.body() != null) {
                daysTelemetry = response.body()?.day
                weekTelemetry = response.body()?.week
                Log.d("HistoryScreen", "Fetched ${daysTelemetry?.size} day records and ${weekTelemetry?.size} week records")
            } else {
                Log.d("fetchTelemetry", response.code().toString() + ": " + response.message())
            }
        } catch (e: Exception) {
            Log.e(
                "fetchTelemetry Exception Caught:",
                "Network error: ${e.message ?: "Unable to connect to server"}", e
            )
        } finally {
            isLoading = false
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .background(Color.White)
        ) {

            HistoryHeader(
                notifications = notificationCount,
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
                        fontSize = 20.sp
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

            HistoryChart(selectedCategory, selectedFilter, daysTelemetry, weekTelemetry)

            Spacer(modifier = Modifier.height(45.dp))

            StatsGrid(selectedCategory, selectedFilter, daysTelemetry, weekTelemetry)

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
fun StatsGrid(
    category: String, 
    range: String,
    daysTelemetry: List<HistoricalData>?,
    weekTelemetry: List<HistoricalData>?
) {
    val data = getChartData(category, range, daysTelemetry, weekTelemetry)
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

fun getValue(category: String, item: HistoricalData): Float {
    return when (category) {
        "Temp" -> item.avg_temperature?.toFloat() ?: 0f
        "Humidity" -> item.avg_humidity?.toFloat() ?: 0f
        "CO₂" -> item.avg_co2?.toFloat() ?: 0f
        else -> item.avg_pm25?.toFloat() ?: 0f
    }
}

fun getChartData(category: String, range: String, daysData: List<HistoricalData>?, weekData: List<HistoricalData>?): List<Float> {
    val source = when (range) {
        "Last 24h" -> daysData
        "7 days" -> weekData
        else -> null
    } ?: return emptyList()

    return source.map { getValue(category, it) }
}

fun getHour(time: String?): String? {
    return try {
        time?.split("T")?.getOrNull(1)?.split(":")?.getOrNull(0)
    } catch (e: Exception) {
        null
    }
}

fun getTimeRanges(daysData: List<HistoricalData>?): List<String> {
    return daysData?.mapNotNull { getHour(it.updated_at) } ?: emptyList()
}

@Composable
fun HistoryChart(
    category: String, 
    range: String,
    daysTelemetry: List<HistoricalData>?,
    weekTelemetry: List<HistoricalData>?
) {
    val data = getChartData(category, range, daysTelemetry, weekTelemetry)
    val entries = remember(data) { data.mapIndexed { index, value -> entryOf(index, value) } }
    val modelProducer = remember(entries) { ChartEntryModelProducer(entries) }

    val bottomLabels = when (range) {
        "Last 24h" -> getTimeRanges(daysTelemetry)
        "7 days" -> {
            val days = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")
            weekTelemetry?.indices?.map { days[it % days.size] } ?: emptyList()
        }
        else -> listOf("1w", "2w", "3w", "4w")
    }

    val bottomAxis = rememberBottomAxis(
        valueFormatter = { x, _ ->
            val index = x.toInt()
            if (index in bottomLabels.indices) bottomLabels[index] else ""
        }
    )

    if (data.isNotEmpty()) {
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
    } else {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(250.dp)
                .padding(horizontal = 24.dp),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = if (range == "1 month") "No data available" else "Loading...",
                fontFamily = Montserrat,
                fontSize = 16.sp,
                color = Color.Gray
            )
        }
    }
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
        selectedTab = "history",
        onTabSelected = {},
        onNotificationsClick = {}
    )
}
