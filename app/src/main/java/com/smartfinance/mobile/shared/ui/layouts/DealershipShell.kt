package com.smartfinance.mobile.shared.ui.layouts

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.smartfinance.mobile.shared.ui.components.DealershipBottomNavigationBar

@Composable
fun DealershipShell(
    selectedNavIndex: Int,
    header: @Composable () -> Unit,
    onItemClick: (Int) -> Unit,
    content: @Composable () -> Unit
) {
    Scaffold(
        topBar = header,
        bottomBar = {
            DealershipBottomNavigationBar(
                selectedIndex = selectedNavIndex,
                onItemSelected = onItemClick
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            content()
        }
    }
}
