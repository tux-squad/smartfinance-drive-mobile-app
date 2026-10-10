package com.smartfinance.mobile.catalog.presentation.dashboard.dealer

import androidx.compose.foundation.background
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Inventory2
import androidx.compose.material.icons.filled.People
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.smartfinance.mobile.catalog.domain.model.DealerAnalytics
import com.smartfinance.mobile.catalog.domain.repository.AnalyticsRepository
import com.smartfinance.mobile.core.ui.theme.AccentOrange
import com.smartfinance.mobile.core.ui.theme.PrimaryBlue
import kotlinx.coroutines.launch

@Composable
fun DealershipDashboardScreen(
    analyticsRepository: AnalyticsRepository? = null,
    onNavigateToInventory: () -> Unit,
    onNavigateToMessages: () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    var selectedPeriod by remember { mutableStateOf("MONTH") } // "WEEK", "MONTH", "YEAR"
    var analytics by remember { mutableStateOf(DealerAnalytics()) }
    var isLoading by remember { mutableStateOf(false) }

    fun loadAnalytics(period: String) {
        if (analyticsRepository == null) return
        isLoading = true
        coroutineScope.launch {
            val result = analyticsRepository.getDealerAnalytics(period)
            result.onSuccess { analytics = it }
            isLoading = false
        }
    }

    LaunchedEffect(selectedPeriod) {
        loadAnalytics(selectedPeriod)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF4F7FB))
    ) {
        // Cabecera principal
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(horizontal = 20.dp, vertical = 18.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Panel Concesionaria / Resumen Comercial",
                        color = Color(0xFF64748B),
                        fontSize = 12.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Métricas y Desempeño \uD83D\uDCCA",
                        color = Color(0xFF0F203C),
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                IconButton(onClick = { loadAnalytics(selectedPeriod) }) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = "Actualizar",
                        tint = PrimaryBlue
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Selector de Período
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                PeriodChip(
                    title = "Esta Semana",
                    isSelected = selectedPeriod == "WEEK",
                    onClick = { selectedPeriod = "WEEK" }
                )
                PeriodChip(
                    title = "Este Mes",
                    isSelected = selectedPeriod == "MONTH",
                    onClick = { selectedPeriod = "MONTH" }
                )
                PeriodChip(
                    title = "Este Año",
                    isSelected = selectedPeriod == "YEAR",
                    onClick = { selectedPeriod = "YEAR" }
                )
            }
        }

        // Contenido Scrollable
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 14.dp)
        ) {
            if (isLoading) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(160.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(color = PrimaryBlue)
                }
            } else {
                // KPIs Principales en Grid 2x2
                Row(modifier = Modifier.fillMaxWidth()) {
                    Box(modifier = Modifier.weight(1f)) {
                        KpiCard(
                            title = "Vehículos",
                            value = "${analytics.totalVehicles}",
                            subtitle = "${analytics.activeVehicles} activos en catálogo",
                            icon = Icons.Default.Inventory2,
                            iconColor = Color(0xFF0284C7),
                            bgColor = Color(0xFFE0F2FE)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Box(modifier = Modifier.weight(1f)) {
                        KpiCard(
                            title = "Prospectos",
                            value = "${analytics.totalProspects}",
                            subtitle = "Leads cualificados",
                            icon = Icons.Default.People,
                            iconColor = AccentOrange,
                            bgColor = Color(0xFFFFEDD5)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Row(modifier = Modifier.fillMaxWidth()) {
                    Box(modifier = Modifier.weight(1f)) {
                        KpiCard(
                            title = "Test Drives",
                            value = "${analytics.testDrivesScheduled}",
                            subtitle = "Citas confirmadas",
                            icon = Icons.Default.DirectionsCar,
                            iconColor = Color(0xFF7C3AED),
                            bgColor = Color(0xFFEDE9FE)
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Box(modifier = Modifier.weight(1f)) {
                        KpiCard(
                            title = "Ventas Estimadas",
                            value = analytics.monthlySalesVolume,
                            subtitle = "Tasa cierre: ${analytics.conversionRate}",
                            icon = Icons.Default.TrendingUp,
                            iconColor = Color(0xFF059669),
                            bgColor = Color(0xFFD1FAE5)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Métricas de Alcance
                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .background(Color(0xFFF1F5F9), CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(Icons.Default.Visibility, contentDescription = null, tint = PrimaryBlue, modifier = Modifier.size(20.dp))
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text("Visualizaciones de Inventario", fontSize = 13.sp, color = Color(0xFF64748B))
                                Text("${analytics.totalViews} visitas", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color(0xFF0F203C))
                            }
                        }

                        Box(
                            modifier = Modifier
                                .background(Color(0xFFDCFCE7), RoundedCornerShape(8.dp))
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text("+18.4%", color = Color(0xFF15803D), fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Acciones Rápidas
            Text(
                text = "Acciones Rápidas",
                color = Color(0xFF0F203C),
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(10.dp))

            Row(modifier = Modifier.fillMaxWidth()) {
                Button(
                    onClick = onNavigateToInventory,
                    colors = ButtonDefaults.buttonColors(containerColor = AccentOrange),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.height(48.dp).weight(1f)
                ) {
                    Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Añadir Auto", fontWeight = FontWeight.SemiBold)
                }

                Spacer(modifier = Modifier.width(12.dp))

                OutlinedButton(
                    onClick = onNavigateToMessages,
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFF0F203C)),
                    modifier = Modifier.height(48.dp).weight(1f)
                ) {
                    Icon(Icons.Default.Chat, contentDescription = null, modifier = Modifier.size(18.dp), tint = PrimaryBlue)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("Mensajes", fontWeight = FontWeight.SemiBold)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun PeriodChip(
    title: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .background(if (isSelected) PrimaryBlue else Color(0xFFF1F5F9))
            .clickable { onClick() }
            .padding(horizontal = 14.dp, vertical = 7.dp)
    ) {
        Text(
            text = title,
            color = if (isSelected) Color.White else Color(0xFF475569),
            fontSize = 12.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
        )
    }
}

@Composable
private fun KpiCard(
    title: String,
    value: String,
    subtitle: String,
    icon: ImageVector,
    iconColor: Color,
    bgColor: Color
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .background(bgColor, RoundedCornerShape(8.dp)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = iconColor,
                    modifier = Modifier.size(20.dp)
                )
            }
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = value,
                color = Color(0xFF0F203C),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = title,
                color = Color(0xFF0F203C),
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = subtitle,
                color = Color(0xFF64748B),
                fontSize = 11.sp
            )
        }
    }
}
