package com.smartfinance.mobile.shared.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.ChatBubble
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.smartfinance.mobile.core.ui.theme.NavyDark
import androidx.compose.ui.res.stringResource
import com.smartfinance.mobile.R

data class NavDrawerItem(
    val routeKey: String,
    val title: String,
    val icon: ImageVector
)

@Composable
fun AppNavigationDrawer(
    selectedRoute: String = "messages",
    onSelectItem: (String) -> Unit = {}
) {
    val menuItems = listOf(
        NavDrawerItem("dashboard", "Dashboard", Icons.Default.Dashboard),
        NavDrawerItem("vehicles", stringResource(R.string.vehicles), Icons.Default.DirectionsCar),
        NavDrawerItem("dealerships", stringResource(R.string.dealerships), Icons.Default.Storefront),
        NavDrawerItem("messages", stringResource(R.string.messages), Icons.Default.ChatBubble),
        NavDrawerItem("ai_consult", stringResource(R.string.ai_consult), Icons.Default.AutoAwesome),
        NavDrawerItem("reports", stringResource(R.string.reports), Icons.Default.Assessment),
        NavDrawerItem("profile", stringResource(R.string.profile), Icons.Default.Person),
        NavDrawerItem("settings", stringResource(R.string.settings), Icons.Default.Settings)
    )

    Column(
        modifier = Modifier
            .fillMaxHeight()
            .width(280.dp)
            .background(NavyDark)
            .padding(vertical = 24.dp, horizontal = 16.dp)
    ) {
        // App Logo Header in Drawer
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(bottom = 28.dp, start = 4.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(Color(0xFF1E3A8A)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.DirectionsCar,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column {
                Text(
                    text = "SmartFinance",
                    color = Color.White,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "Drive",
                    color = Color.White.copy(alpha = 0.8f),
                    fontSize = 12.sp
                )
            }
        }

        // Navigation Items
        menuItems.forEach { item ->
            val isSelected = item.routeKey == selectedRoute

            val containerColor = if (isSelected) Color(0xFF1E40AF) else Color.Transparent
            val contentColor = if (isSelected) Color.White else Color(0xFF94A3B8)

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(containerColor)
                    .clickable { onSelectItem(item.routeKey) }
                    .padding(horizontal = 14.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = item.icon,
                    contentDescription = item.title,
                    tint = contentColor,
                    modifier = Modifier.size(20.dp)
                )

                Spacer(modifier = Modifier.width(14.dp))

                Text(
                    text = item.title,
                    color = contentColor,
                    fontSize = 14.sp,
                    fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Medium
                )
            }

            Spacer(modifier = Modifier.height(4.dp))
        }
    }
}
