package com.smartfinance.mobile.shared.ui.layouts

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.smartfinance.mobile.core.ui.theme.SurfaceCard
import com.smartfinance.mobile.core.ui.theme.SurfaceDark
import com.smartfinance.mobile.shared.ui.components.AppTopHeader
import com.smartfinance.mobile.shared.ui.components.BottomNavigationBar

@Composable
fun MobileShell(
    selectedNavIndex: Int = 0,
    onItemClick: (Int) -> Unit = {},
    onSearchClick: () -> Unit = {},
    onProfileClick: () -> Unit = {},
    header: @Composable () -> Unit = {
        AppTopHeader(
            onSearchClick = onSearchClick,
            onProfileClick = onProfileClick
        )
    },
    content: @Composable () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(SurfaceDark)
    ) {
        Card(
            modifier = Modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(0.dp)),
            colors = CardDefaults.cardColors(containerColor = SurfaceCard),
            shape = RoundedCornerShape(0.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFFF8FAFC))
            ) {
                header()

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth()
                        .verticalScroll(rememberScrollState())
                ) {
                    content()
                }

                BottomNavigationBar(
                    selectedIndex = selectedNavIndex,
                    onItemClick = onItemClick
                )
            }
        }
    }
}
