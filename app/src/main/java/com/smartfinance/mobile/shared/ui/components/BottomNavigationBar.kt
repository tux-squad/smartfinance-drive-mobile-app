package com.smartfinance.mobile.shared.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.ChatBubble
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.smartfinance.mobile.core.ui.theme.AccentOrange
import com.smartfinance.mobile.core.ui.theme.NavInactive
import com.smartfinance.mobile.core.ui.theme.NavyDark
import androidx.compose.ui.res.stringResource
import com.smartfinance.mobile.R

@Composable
fun BottomNavigationBar(
    selectedIndex: Int = 0,
    onItemClick: (Int) -> Unit = {}
) {
    val items = listOf(
        stringResource(R.string.home) to Icons.Default.DirectionsCar,
        stringResource(R.string.search) to Icons.Default.Search,
        stringResource(R.string.messages) to Icons.Default.ChatBubble,
        stringResource(R.string.ai_consult) to Icons.Default.AutoAwesome,
        stringResource(R.string.reports) to Icons.Default.Assessment,
        stringResource(R.string.my_profile) to Icons.Default.AccountCircle
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(NavyDark)
            .padding(vertical = 8.dp, horizontal = 2.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            items.forEachIndexed { index, item ->
                val isSelected = index == selectedIndex
                val color = if (isSelected) AccentOrange else NavInactive

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .clickable(
                            interactionSource = remember { MutableInteractionSource() },
                            indication = null
                        ) { onItemClick(index) }
                        .padding(vertical = 4.dp, horizontal = 4.dp)
                ) {
                    Icon(
                        imageVector = item.second,
                        contentDescription = item.first,
                        tint = color,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = item.first,
                        color = color,
                        fontSize = 10.sp,
                        fontWeight = if (isSelected) FontWeight.SemiBold else FontWeight.Normal,
                        textAlign = TextAlign.Center,
                        maxLines = 1
                    )
                }
            }
        }
    }
}
