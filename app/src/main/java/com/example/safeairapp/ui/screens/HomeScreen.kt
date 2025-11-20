package com.example.safeairapp.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.zIndex
import com.example.safeairapp.R
import com.example.safeairapp.ui.theme.Montserrat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun HomeScreen(airQualityValue: Float = 15f, notifications: Int = 2) {

    var selectedTab by remember { mutableStateOf("home") }

    Box(modifier = Modifier.fillMaxSize()) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {

            Box(modifier = Modifier.fillMaxWidth()) {

                AirQualityHeader(
                    airValue = airQualityValue,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(710.dp)
                )

                HeaderBar(notifications = notifications)
            }

            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .offset(y = (-35).dp),
                shape = RoundedCornerShape(topStart = 40.dp, topEnd = 40.dp),
                color = Color.White
            ) {
                Column(modifier = Modifier.padding(24.dp)) {

                    SensorsList()

                    Spacer(modifier = Modifier.height(120.dp))
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
                onTabSelected = { selectedTab = it }
            )
        }
    }
}

@Composable
fun BottomNavBar(
    selected: String,
    onTabSelected: (String) -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 5.dp)
            .background(
                color = Color(0xFF1E1E1E),
                shape = RoundedCornerShape(36.dp)
            )
            .padding(horizontal = 26.dp, vertical = 10.dp)
    ) {

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {

            BottomNavItem(
                icon = R.drawable.home,
                label = "Home",
                isSelected = selected == "home",
                onClick = { onTabSelected("home") }
            )

            BottomNavItem(
                icon = R.drawable.tips,
                label = "Tips",
                isSelected = selected == "tips",
                onClick = { onTabSelected("tips") }
            )

            BottomNavItem(
                icon = R.drawable.history,
                label = "History",
                isSelected = selected == "history",
                onClick = { onTabSelected("history") }
            )

            BottomNavItem(
                icon = R.drawable.settings,
                label = "Settings",
                isSelected = selected == "settings",
                onClick = { onTabSelected("settings") }
            )
        }
    }
}

@Composable
fun BottomNavItem(
    icon: Int,
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    if (isSelected) {
        Row(
            modifier = Modifier
                .background(Color(0xFF3D3D3D), RoundedCornerShape(35.dp))
                .clickable { onClick() }
                .padding(horizontal = 16.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(icon),
                contentDescription = label,
                modifier = Modifier.size(25.dp)
            )
            if (label.isNotEmpty()) {
                Spacer(Modifier.width(8.dp))
                Text(
                    text = label,
                    color = Color.White,
                    fontSize = 18.sp,
                    fontFamily = Montserrat,
                    fontWeight = FontWeight.Bold,
                )
            }
        }
    } else {
        Image(
            painter = painterResource(icon),
            contentDescription = label,
            modifier = Modifier
                .size(25.dp)
                .clickable { onClick() }
        )
    }
}

@Composable
fun AlertThresholdInfoCard() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(
                color = Color(0x4DCDECFF),
                shape = RoundedCornerShape(22.dp)
            )
            .border(
                width = 1.dp,
                color = Color(0xFFA7BEC8),
                shape = RoundedCornerShape(22.dp)
            )
            .padding(20.dp)
    ) {

        Column {

            Row(verticalAlignment = Alignment.CenterVertically) {

                Image(
                    painter = painterResource(id = R.drawable.alert_info),
                    contentDescription = "info",
                    modifier = Modifier.size(26.dp)
                )

                Spacer(modifier = Modifier.width(12.dp))

                Text(
                    text = "How to adjust your alert thresholds",
                    fontSize = 20.sp,
                    fontFamily = Montserrat,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = "You can also adjust your alert thresholds in Settings → Customise thresholds for a detailed view of all parameters.",
                fontSize = 15.sp,
                color = Color.Black.copy(alpha = 0.75f),
                fontFamily = Montserrat,
                fontWeight = FontWeight.Medium,
                lineHeight = 20.sp
            )
        }
    }
}


@Composable
fun SensorCard(
    icon: Int,
    title: String,
    value: String,
    unit: String,
    status: String
) {
    val statusColor = when (status) {
        "Good" -> Color(0xFF5BC45F)
        "Warning" -> Color(0xFFE9A84C)
        "Bad" -> Color(0xFFD9534F)
        else -> Color.Gray
    }

    val statusColorText = when (status) {
        "Good" -> Color(0xFF338D38)
        "Warning" -> Color(0xFFBD832B)
        "Bad" -> Color(0xFFAD3531)
        else -> Color.Gray
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White, RoundedCornerShape(22.dp))
            .border(
                width = 1.dp,
                color = Color(0xFFA8A8A8),
                shape = RoundedCornerShape(22.dp)
            )
            .padding(horizontal = 18.dp, vertical = 20.dp)
    ) {

        Column {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(
                        painter = painterResource(icon),
                        contentDescription = title,
                        modifier = Modifier.size(32.dp)
                    )
                    Spacer(Modifier.width(12.dp))
                    Text(title,
                        fontSize = 22.sp,
                        fontFamily = Montserrat,
                        fontWeight = FontWeight.Medium,
                        color = Color.Black)
                }

                Box(
                    modifier = Modifier
                        .background(statusColor.copy(alpha = 0.2f), RoundedCornerShape(10.dp))
                        .padding(horizontal = 14.dp, vertical = 6.dp)
                ) {
                    Text(status,
                        color = statusColorText,
                        fontFamily = Montserrat,
                        fontWeight = FontWeight.Medium,
                        fontSize = 16.sp)
                }
            }

            Spacer(Modifier.height(14.dp))

            Text(
                text = value + " " + unit,
                fontSize = 33.sp,
                fontFamily = Montserrat,
                fontWeight = FontWeight.Bold,
                color = Color.Black
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Text(
                    text = "Current value",
                    fontSize = 15.sp,
                    fontFamily = Montserrat,
                    fontWeight = FontWeight.Normal,
                    color = Color.Gray
                )

                Text(
                    text = "More details >",
                    fontSize = 17.sp,
                    fontFamily = Montserrat,
                    fontWeight = FontWeight.Medium,
                    color = Color.Black.copy(alpha = 0.75f)
                )
            }
        }
    }
}


@Composable
fun SensorsList() {
    Column(verticalArrangement = Arrangement.spacedBy(18.dp)) {
        Spacer(modifier = Modifier.height(10.dp))
        SensorCard(
            icon = R.drawable.temperature_card,
            title = "Temperature",
            value = "22.5",
            unit = "°C",
            status = "Good"
        )

        SensorCard(
            icon = R.drawable.humidity_card,
            title = "Humidity",
            value = "45",
            unit = "%",
            status = "Warning"
        )

        SensorCard(
            icon = R.drawable.co2_card,
            title = "CO₂ Level",
            value = "850",
            unit = "ppm",
            status = "Warning"
        )

        SensorCard(
            icon = R.drawable.dust_card,
            title = "PM2.5",
            value = "12.3",
            unit = "µg/m³",
            status = "Good"
        )

        Spacer(modifier = Modifier.height(22.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(Color(0xFFE6E6E6))
        )
        Spacer(modifier = Modifier.height(22.dp))

        AlertThresholdInfoCard()

        Spacer(modifier = Modifier.height(40.dp))
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
            listOf(Color(0xFFFFE868), Color(0xFFFFD91C))
        )
        value >= 20f -> Brush.verticalGradient(
            listOf(Color(0xFFFFBF71), Color(0xFFFA7121))
        )
        else -> Brush.verticalGradient(
            listOf(Color(0xFFFF8F71), Color(0xFFF53C27))
        )
    }
}

fun getAirQualityText(value: Float): String {
    return when {
        value >= 80f -> "Good air\nquality"
        value >= 60f -> "Air quality\nis fair"
        value >= 40f -> "Air quality\nis poor"
        value >= 20f -> "Air quality\nis bad"
        else -> "Air quality\nis very\nbad"
    }
}

fun getAirQualityImage(value: Float): Int {
    return when {
        value >= 80f -> R.drawable.air_good
        value >= 60f -> R.drawable.air_fair
        value >= 40f -> R.drawable.air_poor
        value >= 20f -> R.drawable.air_bad
        else -> R.drawable.air_very_bad
    }
}

fun getFormattedToday(): String {
    val formatter = SimpleDateFormat("EEEE, MMMM d", Locale.ENGLISH)
    val date = Date()
    return formatter.format(date)
}

@Composable
fun AirIndicatorsRow() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 24.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IndicatorItem(R.drawable.temperature_sensor, "Temperature")
        IndicatorItem(R.drawable.humidity, "Humidity")
        IndicatorItem(R.drawable.co2, "CO2")
        IndicatorItem(R.drawable.dust, "Dust")
    }
}

@Composable
fun IndicatorItem(icon: Int, label: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(55.dp)
                .background(
                    color = Color(0xFFFFF8ED).copy(alpha = 0.8f),
                    shape = RoundedCornerShape(50)
                ),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(icon),
                contentDescription = label,
                modifier = Modifier.size(31.dp)
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = label,
            fontSize = 15.sp,
            fontFamily = Montserrat,
            fontWeight = FontWeight.Medium,
            color = Color.Black
        )
    }
}


@Composable
fun HeaderBar(notifications: Int, modifier: Modifier = Modifier) {
    Row (
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 45.dp, start = 24.dp, end = 24.dp),
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
                .size(52.dp),
            contentAlignment = Alignment.Center
        ) {

            Box(
                modifier = Modifier
                    .size(42.dp)
                    .background(
                        color = Color(0xFFFFF8ED).copy(alpha = 0.5f),
                        shape = RoundedCornerShape(50)
                    )
            )

            Image(
                painter = painterResource(R.drawable.notification),
                contentDescription = "Notifications",
                modifier = Modifier.size(25.dp)
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
}

@Composable
fun AirQualityHeader(airValue: Float, modifier: Modifier = Modifier) {
    val qualityText = getAirQualityText(airValue)
    val imageRes = getAirQualityImage(airValue)

    Box(
        modifier = modifier
            .background(getGradientForAirQuality(airValue))
    ) {

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 105.dp)
        ) {

            Column(modifier = Modifier.padding(start = 24.dp, end = 24.dp)) {

                Spacer(modifier = Modifier.height(26.dp))

                Text(
                    text = getFormattedToday(),
                    fontSize = 28.sp,
                    fontFamily = Montserrat,
                    fontWeight = FontWeight.Medium,
                    color = Color.Black
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = "Air quality category:",
                    fontSize = 16.sp,
                    fontFamily = Montserrat,
                    fontWeight = FontWeight.Normal,
                    color = Color.Black.copy(alpha = 0.6f)
                )

                Spacer(modifier = Modifier.height(5.dp))
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Box(
                    modifier = Modifier.size(310.dp)
                ) {
                    Image(
                        painter = painterResource(R.drawable.air_circles),
                        contentDescription = null,
                        modifier = Modifier.matchParentSize()
                    )

                    Text(
                        text = getAirQualityText(airValue),
                        fontSize = 27.sp,
                        lineHeight = 34.sp,
                        fontFamily = Montserrat,
                        fontWeight = FontWeight.Medium,
                        color = Color.Black,
                        textAlign = TextAlign.Center,
                        modifier = Modifier
                            .align(Alignment.Center)
                            .offset(x = (-45).dp)
                            .fillMaxWidth()
                    )
                }
                
                Image(
                    painter = painterResource(id = imageRes),
                    contentDescription = "Air quality illustration",
                    modifier = Modifier.requiredSize(360.dp)
                )
            }

            Spacer(modifier = Modifier.height(5.dp))

            AirIndicatorsRow()

            Spacer(modifier = Modifier.height(20.dp))
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
