package com.example.safeairapp.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
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
        ) {

            AlertThresholdsHeader(
                notifications = notifications,
                onNotificationsClick = onNotificationsClick,
                onBackClick = { onTabSelected("settings") }
            )

            Spacer(modifier = Modifier.height(20.dp))

            AlertDescriptionCard()

            Spacer(modifier = Modifier.height(20.dp))

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
                Text(" trigger", fontFamily = Montserrat, fontSize = 16.sp, color = Color(0xFF505050), lineHeight = 22.sp)
            }
            Row {
                Text("a warning, and values ", fontFamily = Montserrat,fontSize = 16.sp,color = Color(0xFF505050), lineHeight = 22.sp)
                Text("above the danger ", color = Color(0xFFE53935), fontWeight = FontWeight.SemiBold, fontSize = 16.sp, lineHeight = 22.sp)
            }
            Row {
                Text("handle", color = Color(0xFFE53935), fontWeight = FontWeight.SemiBold, fontSize = 16.sp, lineHeight = 22.sp)
                Text(" trigger a danger alert.", fontFamily = Montserrat, fontSize = 16.sp,color = Color(0xFF505050), lineHeight = 22.sp)
            }
        }
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
