package com.example.safeairapp.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
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

data class NotificationItem(
    val id: Int,
    val icon: Int,
    val title: String,
    val message: String,
    val time: String,
    val status: String,
    val isNew: Boolean
)

fun statusColor(status: String): Color =
    when (status) {
        "high" -> Color(0xFFE57373)
        "warning" -> Color(0xFFFFB74D)
        "info" -> Color(0xFF64B5F6)
        "success" -> Color(0xFF81C784)
        else -> Color.Gray
    }

val sampleNotifications = listOf(
    NotificationItem(
        id = 1,
        icon = R.drawable.warning,
        title = "CO₂ Level Elevated",
        message = "PM2.5 levels are at 45 μg/m³. Avoid outdoor activities.",
        time = "5 minutes ago",
        status = "high",
        isNew = true
    ),
    NotificationItem(
        id = 2,
        icon = R.drawable.high,
        title = "PM2.5 Threshold Exceeded",
        message = "CO₂ concentration reached 850 ppm. Ventilate the room.",
        time = "2 hours ago",
        status = "warning",
        isNew = true
    ),
    NotificationItem(
        id = 3,
        icon = R.drawable.warning_info,
        title = "Temperature Change",
        message = "Temperature has dropped by 3°C.",
        time = "3 hours ago",
        status = "info",
        isNew = false
    ),
    NotificationItem(
        id = 4,
        icon = R.drawable.checkmark,
        title = "Air Quality Improved",
        message = "All parameters returned to optimal levels.",
        time = "6 hours ago",
        status = "success",
        isNew = false
    )
)
@Composable
fun NotificationsScreen(
    notifications: Int = 5,
    selectedTab: String = "notifications",
    onTabSelected: (String) -> Unit
) {

    Box(modifier = Modifier.fillMaxSize()) {

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
        ) {

            NotificationsHeader(newCount = notifications)

            LazyColumn (
                modifier = Modifier
                    .padding(bottom = 105.dp)
                    .padding(horizontal = 16.dp)
            ) {

                item {
                    Spacer(modifier = Modifier.height(20.dp))
                    Text(
                        text = "Mark all as read",
                        fontSize = 14.sp,
                        fontFamily = Montserrat,
                        color = Color.Black,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 10.dp, bottom = 14.dp, end = 10.dp),
                        textAlign = TextAlign.End
                    )
                }

                items(sampleNotifications) { item ->
                    NotificationCard(item)
                    Spacer(modifier = Modifier.height(18.dp))
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

@Composable
fun NotificationsHeader(newCount: Int = 2) {

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(260.dp)
            .background(
                color = Color(0xFF1D1D1D),
                shape = RoundedCornerShape(bottomStart = 30.dp, bottomEnd = 30.dp)
            )
            .padding(horizontal = 24.dp, vertical = 50.dp)
    ) {

        Column {

            Image(
                painter = painterResource(id = R.drawable.safeair_logo_w),
                contentDescription = "Logo",
                modifier = Modifier.size(40.dp)
            )

            Spacer(modifier = Modifier.height(40.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Text(
                    text = "Notifications",
                    fontSize = 28.sp,
                    fontFamily = Montserrat,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.White
                )

                if (newCount > 0) {
                    Box(
                        modifier = Modifier
                            .background(
                                color = Color(0xFF7A7A7A).copy(alpha = 0.45f),
                                shape = RoundedCornerShape(10.dp)
                            )
                            .padding(horizontal = 12.dp, vertical = 5.dp)
                    ) {
                        Text(
                            text = "$newCount new",
                            fontSize = 14.sp,
                            fontFamily = Montserrat,
                            fontWeight = FontWeight.SemiBold,
                            color = Color.White
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "Stay informed about air quality changes",
                fontSize = 16.sp,
                fontFamily = Montserrat,
                fontWeight = FontWeight.Normal,
                color = Color.White.copy(alpha = 0.7f)
            )
        }
    }
}

@Composable
fun NotificationCard(item: NotificationItem) {

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White, RoundedCornerShape(20.dp))
            .border(
                width = 1.dp,
                color = Color(0xFF989898),
                shape = RoundedCornerShape(20.dp)
            )
            .padding(22.dp)
    ) {

        Column {

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.widthIn(max = 220.dp)
                ) {
                    Image(
                        painter = painterResource(id = item.icon),
                        contentDescription = "",
                        modifier = Modifier.size(34.dp)
                    )

                    Spacer(modifier = Modifier.width(12.dp))

                    Text(
                        text = item.title,
                        fontSize = 18.sp,
                        fontFamily = Montserrat,
                        fontWeight = FontWeight.SemiBold,
                        maxLines = 2,
                        softWrap = true
                    )
                }

                Box(
                    modifier = Modifier
                        .background(
                            statusColor(item.status).copy(alpha = 0.25f),
                            RoundedCornerShape(8.dp)
                        )
                        .padding(horizontal = 12.dp, vertical = 5.dp)
                ) {
                    Text(
                        text = item.status.replaceFirstChar { it.uppercase() },
                        color = statusColor(item.status),
                        fontSize = 13.sp,
                        fontFamily = Montserrat,
                        fontWeight = FontWeight.Bold
                    )
                }
            }


            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = item.message,
                fontSize = 14.sp,
                fontFamily = Montserrat,
                lineHeight = 18.sp,
                color = Color.Black.copy(alpha = 0.75f)
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {

                Text(
                    text = item.time,
                    fontSize = 13.sp,
                    color = Color.DarkGray,
                    fontFamily = Montserrat
                )

                Image(
                    painter = painterResource(id = R.drawable.delete),
                    contentDescription = "delete",
                    modifier = Modifier.size(18.dp)
                )
            }
        }

        if (item.isNew) {
            Box(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .offset(x = 10.dp, y = (-10).dp)
                    .size(14.dp)
                    .background(Color.Black, CircleShape)
            )
        }
    }
}



@Preview(showBackground = true)
@Composable
fun PreviewNotificationsScreen() {
    NotificationsScreen(
        notifications = 5,
        selectedTab = "notifications",
        onTabSelected = {}
    )
}
