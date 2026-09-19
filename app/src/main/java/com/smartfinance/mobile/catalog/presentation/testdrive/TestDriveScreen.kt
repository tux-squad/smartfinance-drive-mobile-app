package com.smartfinance.mobile.catalog.presentation.testdrive

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.smartfinance.mobile.catalog.domain.model.TestDriveBooking
import com.smartfinance.mobile.core.ui.theme.AccentOrange
import com.smartfinance.mobile.core.ui.theme.BorderSoft
import com.smartfinance.mobile.core.ui.theme.SmartFinanceDriveTheme
import com.smartfinance.mobile.core.ui.theme.TextPrimary
import com.smartfinance.mobile.core.ui.theme.TextSecondary
import com.smartfinance.mobile.shared.ui.layouts.MobileShell

@Composable
fun TestDriveScreen(
    dealershipName: String = "AutoCentro San Isidro",
    dealershipAddress: String = "Av. Javier Prado Este 1234, San Isidro",
    distanceText: String = "A 3.2 km de tu ubicación",
    vehicleName: String = "Toyota RAV4 2024",
    onBack: () -> Unit = {},
    onConfirmBooking: (TestDriveBooking) -> Unit = {}
) {
    var selectedDay by remember { mutableIntStateOf(8) }
    var selectedTimeSlot by remember { mutableStateOf("11:30 AM") }

    val daysOfWeek = listOf("L", "M", "M", "J", "V", "S", "D")
    val timeSlots = listOf("10:00 AM", "11:30 AM", "2:00 PM", "3:30 PM")

    // Calendar grid items (dayNumber, isCurrentMonth)
    val week1 = listOf(
        Pair(29, false),
        Pair(30, false),
        Pair(1, true),
        Pair(2, true),
        Pair(3, true),
        Pair(4, true),
        Pair(5, true)
    )
    val week2 = listOf(
        Pair(6, true),
        Pair(7, true),
        Pair(8, true),
        Pair(9, true),
        Pair(10, true),
        Pair(11, true),
        Pair(12, true)
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8FAFC))
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        // Breadcrumb with Back Navigation
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onBack,
                modifier = Modifier
                    .size(28.dp)
                    .background(Color.White, CircleShape)
                    .border(1.dp, BorderSoft, CircleShape)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = "Atrás",
                    tint = Color(0xFF1E40AF),
                    modifier = Modifier.size(16.dp)
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = "Panel de Comprador / Concesionaria / Test Drive",
                color = TextSecondary,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium
            )
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Main Title
        Text(
            text = "Agendar Test Drive",
            color = Color(0xFF0F172A),
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Calendar Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, BorderSoft, RoundedCornerShape(16.dp)),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 14.dp)
            ) {
                // Month Header with Prev/Next
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = {},
                        modifier = Modifier.size(28.dp)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                            contentDescription = "Mes anterior",
                            tint = Color(0xFF475569),
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    Text(
                        text = "Octubre 2026",
                        color = Color(0xFF1E3A8A),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )

                    IconButton(
                        onClick = {},
                        modifier = Modifier.size(28.dp)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                            contentDescription = "Mes siguiente",
                            tint = Color(0xFF475569),
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Days of week header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    daysOfWeek.forEach { dayName ->
                        Text(
                            text = dayName,
                            color = Color(0xFF64748B),
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium,
                            textAlign = TextAlign.Center,
                            modifier = Modifier.width(36.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Week 1
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    week1.forEach { (day, isCurrentMonth) ->
                        DayCell(
                            dayNumber = day,
                            isCurrentMonth = isCurrentMonth,
                            isSelected = isCurrentMonth && day == selectedDay,
                            onClick = {
                                if (isCurrentMonth) selectedDay = day
                            }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Week 2
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    week2.forEach { (day, isCurrentMonth) ->
                        DayCell(
                            dayNumber = day,
                            isCurrentMonth = isCurrentMonth,
                            isSelected = isCurrentMonth && day == selectedDay,
                            onClick = {
                                if (isCurrentMonth) selectedDay = day
                            }
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Horarios Disponibles Section
        Text(
            text = "HORARIOS DISPONIBLES",
            color = Color(0xFF64748B),
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 0.5.sp
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Time slot selection pills
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            timeSlots.forEach { slot ->
                val isSelected = slot == selectedTimeSlot
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(40.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(
                            if (isSelected) Color(0xFFF0FDFA) else Color.White
                        )
                        .border(
                            width = if (isSelected) 1.5.dp else 1.dp,
                            color = if (isSelected) Color(0xFF0D9488) else BorderSoft,
                            shape = RoundedCornerShape(10.dp)
                        )
                        .clickable { selectedTimeSlot = slot },
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = slot,
                        color = if (isSelected) Color(0xFF0F766E) else Color(0xFF1E293B),
                        fontSize = 12.sp,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Dealership Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, BorderSoft, RoundedCornerShape(14.dp)),
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 14.dp)
            ) {
                Text(
                    text = dealershipName,
                    color = Color(0xFF0F172A),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(3.dp))

                Text(
                    text = dealershipAddress,
                    color = Color(0xFF64748B),
                    fontSize = 13.sp
                )

                Spacer(modifier = Modifier.height(6.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = "Ubicación",
                        tint = Color(0xFF2563EB),
                        modifier = Modifier.size(15.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = distanceText,
                        color = Color(0xFF2563EB),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Confirm Button
        Button(
            onClick = {
                val booking = TestDriveBooking(
                    id = System.currentTimeMillis().toString(),
                    vehicleTitle = vehicleName,
                    dealershipName = dealershipName,
                    dealershipAddress = dealershipAddress,
                    selectedDate = "$selectedDay/10/2026",
                    selectedTimeSlot = selectedTimeSlot
                )
                onConfirmBooking(booking)
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(containerColor = AccentOrange)
        ) {
            Text(
                text = "Confirmar Cita",
                color = Color.White,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun DayCell(
    dayNumber: Int,
    isCurrentMonth: Boolean,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    val cellModifier = if (isSelected) {
        Modifier
            .width(36.dp)
            .height(34.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(Color(0xFF1E40AF))
            .clickable(onClick = onClick)
    } else {
        Modifier
            .width(36.dp)
            .height(34.dp)
            .clip(RoundedCornerShape(8.dp))
            .clickable(enabled = isCurrentMonth, onClick = onClick)
    }

    Box(
        modifier = cellModifier,
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = dayNumber.toString(),
            color = when {
                isSelected -> Color.White
                !isCurrentMonth -> Color(0xFFCBD5E1)
                else -> Color(0xFF1E293B)
            },
            fontSize = 14.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
        )
    }
}

@Preview(showBackground = true, widthDp = 390, heightDp = 844)
@Composable
fun TestDriveScreenPreview() {
    SmartFinanceDriveTheme {
        MobileShell(selectedNavIndex = 1) {
            TestDriveScreen()
        }
    }
}
