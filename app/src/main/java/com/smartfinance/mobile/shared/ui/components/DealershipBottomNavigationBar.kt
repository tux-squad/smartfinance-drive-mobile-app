package com.smartfinance.mobile.shared.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Badge
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.smartfinance.mobile.core.ui.theme.AccentOrange

@Composable
fun DealershipBottomNavigationBar(
    selectedIndex: Int,
    onItemSelected: (Int) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF0F203C)) // Navy background
            .padding(vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        DealershipNavItem(
            icon = Icons.Default.Dashboard,
            label = "Dashboard",
            isSelected = selectedIndex == 0,
            onClick = { onItemSelected(0) }
        )
        DealershipNavItem(
            icon = Icons.Default.Inventory2,
            label = "Inventario",
            isSelected = selectedIndex == 1,
            onClick = { onItemSelected(1) }
        )
        DealershipNavItem(
            icon = Icons.Default.People,
            label = "Prospectos",
            isSelected = selectedIndex == 2,
            onClick = { onItemSelected(2) }
        )
        DealershipNavItem(
            icon = Icons.Default.Chat,
            label = "Mensajes",
            isSelected = selectedIndex == 3,
            onClick = { onItemSelected(3) }
        )
        DealershipNavItem(
            icon = Icons.Default.Badge,
            label = "Membresía",
            isSelected = selectedIndex == 4,
            onClick = { onItemSelected(4) }
        )
        DealershipNavItem(
            icon = Icons.Default.Settings,
            label = "Config",
            isSelected = selectedIndex == 5,
            onClick = { onItemSelected(5) }
        )
    }
}

@Composable
private fun DealershipNavItem(
    icon: ImageVector,
    label: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val color = if (isSelected) AccentOrange else Color(0xFF94A3B8)

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = color,
            modifier = Modifier.size(24.dp)
        )
        Text(
            text = label,
            color = color,
            fontSize = 11.sp,
            modifier = Modifier.padding(top = 4.dp)
        )
    }
}
