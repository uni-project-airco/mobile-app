package com.example.safeairapp.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.RangeSlider
import androidx.compose.material3.SliderDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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

@Composable
fun AlertThresholdsScreen(
    selectedTab: String = "settings",
    notifications: Int = 2,
    onTabSelected: (String) -> Unit,
    onNotificationsClick: () -> Unit
) {

    Box(modifier = Modifier.fillMaxSize()) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(bottom = 80.dp)
                .verticalScroll(rememberScrollState())
        ) {

            AlertThresholdsHeader(
                notifications = notifications,
                onNotificationsClick = onNotificationsClick,
                onBackClick = { onTabSelected("settings") }
            )

            Spacer(modifier = Modifier.height(20.dp))

            AlertDescriptionCard()

            Spacer(modifier = Modifier.height(20.dp))

            AlertSliderCard(
                title = "Temperature",
                iconRes = R.drawable.temperature,
                iconBg = Color(0xFFFFE1C2),
                minValue = 0f,
                maxValue = 50f,
                unitLabel = "°C",
                initialWarning = 28f,
                initialDanger = 35f
            )

            Spacer(modifier = Modifier.height(8.dp))

            AlertSliderCard(
                title = "Humidity",
                iconRes = R.drawable.humidity,
                iconBg = Color(0xFFE5E5FF),
                minValue = 0f,
                maxValue = 100f,
                unitLabel = "%",
                initialWarning = 65f,
                initialDanger = 80f
            )

            Spacer(modifier = Modifier.height(8.dp))

            AlertSliderCard(
                title = "CO₂",
                iconRes = R.drawable.co2,
                iconBg = Color(0xFFEDE5FF),
                minValue = 400f,
                maxValue = 2000f,
                unitLabel = "ppm",
                initialWarning = 800f,
                initialDanger = 1100f
            )

            Spacer(modifier = Modifier.height(8.dp))

            AlertSliderCard(
                title = "PM2.5",
                iconRes = R.drawable.dust,
                iconBg = Color(0xFFE5F6C5),
                minValue = 0f,
                maxValue = 100f,
                unitLabel = "µg/m³",
                initialWarning = 35f,
                initialDanger = 55f
            )

            Spacer(modifier = Modifier.height(80.dp))
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
fun AlertDescriptionCard() {

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp)
            .background(
                Color(0xFFFFF8EC),
                RoundedCornerShape(18.dp)
            )
            .border(1.dp, Color(0xFFFFB200), RoundedCornerShape(18.dp))
            .padding(20.dp)
    ) {

        Column {

            Text("Set custom thresholds using the slider.", fontFamily = Montserrat, fontSize = 16.sp, color = Color(0xFF505050), lineHeight = 22.sp)

            Row {
                Text("Values ", fontFamily = Montserrat, fontSize = 16.sp, color = Color(0xFF505050), lineHeight = 22.sp)
                Text("below the warning handle", color = Color(0xFF41A536), fontWeight = FontWeight.SemiBold, fontSize = 16.sp, lineHeight = 22.sp)
                Text(" are", fontFamily = Montserrat, fontSize = 16.sp, color = Color(0xFF505050), lineHeight = 22.sp)
            }

            Row {
                Text("normal, values ", fontFamily = Montserrat, fontSize = 16.sp, color = Color(0xFF505050), lineHeight = 22.sp)
                Text("between the handles", color = Color(0xFFFF9800), fontWeight = FontWeight.SemiBold, fontSize = 16.sp, lineHeight = 22.sp)

            }
            Row {
                Text("trigger a warning, and values ", fontFamily = Montserrat,fontSize = 16.sp,color = Color(0xFF505050), lineHeight = 22.sp)
                Text("above the ", color = Color(0xFFE53935), fontWeight = FontWeight.SemiBold, fontSize = 16.sp, lineHeight = 22.sp)
            }
            Row {
                Text("danger handle", color = Color(0xFFE53935), fontWeight = FontWeight.SemiBold, fontSize = 16.sp, lineHeight = 22.sp)
                Text(" trigger a danger alert.", fontFamily = Montserrat, fontSize = 16.sp,color = Color(0xFF505050), lineHeight = 22.sp)
            }
        }
    }
}

@Composable
fun AlertSliderCard(
    title: String,
    iconRes: Int,
    iconBg: Color,
    minValue: Float,
    maxValue: Float,
    unitLabel: String,
    initialWarning: Float,
    initialDanger: Float
) {
    var range by remember { mutableStateOf(initialWarning..initialDanger) }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 20.dp, vertical = 8.dp)
            .background(Color.White, RoundedCornerShape(24.dp))
            .border(1.dp, Color(0xFFE0E0E0), RoundedCornerShape(24.dp))
            .padding(20.dp)
    ) {
        Column {

            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(48.dp)
                        .background(iconBg, RoundedCornerShape(50))
                ) {
                    Image(
                        painter = painterResource(id = iconRes),
                        contentDescription = title,
                        modifier = Modifier
                            .size(24.dp)
                            .align(Alignment.Center)
                    )
                }

                Spacer(Modifier.width(12.dp))

                Column {
                    Text(
                        text = title,
                        fontFamily = Montserrat,
                        fontWeight = FontWeight.SemiBold,
                        fontSize = 20.sp,
                        color = Color(0xFF1D1D1D)
                    )

                    Text(
                        text = "Warning: ${range.start.toInt()}$unitLabel • Danger: ${range.endInclusive.toInt()}$unitLabel",
                        fontFamily = Montserrat,
                        fontSize = 14.sp,
                        color = Color(0xFF808080)
                    )
                }
            }

            Spacer(Modifier.height(18.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Dot(color = Color(0xFFFF9800))
                    Spacer(Modifier.width(4.dp))
                    Text(
                        text = "Warning ${range.start.toInt()}$unitLabel",
                        fontFamily = Montserrat,
                        fontSize = 14.sp,
                        color = Color(0xFF1D1D1D)
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Dot(color = Color(0xFFE53935))
                    Spacer(Modifier.width(4.dp))
                    Text(
                        text = "Danger ${range.endInclusive.toInt()}$unitLabel",
                        fontFamily = Montserrat,
                        fontSize = 14.sp,
                        color = Color(0xFFE53935)
                    )
                }
            }

            Spacer(Modifier.height(12.dp))

            RangeSlider(
                value = range,
                onValueChange = { range = it },
                valueRange = minValue..maxValue,
                colors = SliderDefaults.colors(
                    thumbColor = Color(0xFFFF9B00),
                    activeTrackColor = Color(0xFFFF9C0A),
                    inactiveTrackColor = Color(0xFFE0E0E0),
                )
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "${minValue.toInt()}$unitLabel",
                    fontFamily = Montserrat,
                    fontSize = 14.sp,
                    color = Color(0xFF808080)
                )
                Text(
                    text = "${maxValue.toInt()}$unitLabel",
                    fontFamily = Montserrat,
                    fontSize = 14.sp,
                    color = Color(0xFF808080)
                )
            }

            Spacer(Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                LegendItem("Normal", Color(0xFF41A536))
                LegendItem("Warning", Color(0xFFFF9800))
                LegendItem("Danger", Color(0xFFE53935))
            }
        }
    }
}

@Composable
private fun Dot(color: Color) {
    Box(
        modifier = Modifier
            .size(10.dp)
            .background(color, RoundedCornerShape(50))
    )
}

@Composable
private fun LegendItem(text: String, color: Color) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Dot(color = color)
        Spacer(Modifier.width(4.dp))
        Text(
            text = text,
            fontFamily = Montserrat,
            fontSize = 14.sp,
            color = Color(0xFF505050)
        )
    }
}


@Composable
fun AlertThresholdsHeader(
    notifications: Int,
    onNotificationsClick: () -> Unit,
    onBackClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
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
                                fontSize = 14.sp
                            )
                        }
                    }
                }
            }

            Spacer(Modifier.height(20.dp))

            Text(
                text = "◁-  Back to Settings",
                modifier = Modifier.clickable { onBackClick() },
                color = Color.White,
                fontSize = 16.sp,
                fontFamily = Montserrat,
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = "Alert Thresholds",
                fontSize = 28.sp,
                fontFamily = Montserrat,
                fontWeight = FontWeight.SemiBold,
                color = Color.White
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "Customize warning and danger levels",
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
fun PreviewAlertThresholdsScreen() {
    AlertThresholdsScreen(
        selectedTab = "settings",
        notifications = 3,
        onTabSelected = {},
        onNotificationsClick = {}
    )
}
