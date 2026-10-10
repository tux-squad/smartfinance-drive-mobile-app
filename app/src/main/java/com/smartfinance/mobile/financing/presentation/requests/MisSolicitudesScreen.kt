package com.smartfinance.mobile.financing.presentation.requests

import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.smartfinance.mobile.core.ui.theme.PrimaryBlue
import com.smartfinance.mobile.core.ui.theme.SmartFinanceDriveTheme
import com.smartfinance.mobile.core.ui.theme.TextPrimary
import com.smartfinance.mobile.core.ui.theme.TextSecondary
import com.smartfinance.mobile.financing.domain.model.CreditRequest
import com.smartfinance.mobile.financing.domain.model.CreditStatus
import com.smartfinance.mobile.financing.domain.repository.FinancingRepository
import com.smartfinance.mobile.shared.ui.layouts.MobileShell
import kotlinx.coroutines.launch

@Composable
fun MisSolicitudesScreen(
    financingRepository: FinancingRepository? = null
) {
    var requests by remember { mutableStateOf<List<CreditRequest>>(emptyList()) }
    var isLoading by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    fun loadData() {
        if (financingRepository != null) {
            scope.launch {
                isLoading = true
                requests = financingRepository.getMyCreditRequests()
                isLoading = false
            }
        }
    }

    LaunchedEffect(financingRepository) {
        loadData()
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF4F7FB)),
        contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 18.dp, vertical = 12.dp)
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Panel de Comprador / Créditos",
                        color = TextSecondary,
                        fontSize = 14.sp
                    )

                    Text(
                        text = "Mis Solicitudes de Crédito",
                        color = TextPrimary,
                        fontSize = 26.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(top = 4.dp)
                    )

                    Text(
                        text = "Estado formal de tus solicitudes ante entidades financieras.",
                        color = TextSecondary,
                        fontSize = 14.sp,
                        modifier = Modifier.padding(top = 2.dp)
                    )
                }

                IconButton(
                    onClick = { loadData() },
                    modifier = Modifier.padding(top = 10.dp)
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            strokeWidth = 2.dp,
                            color = PrimaryBlue
                        )
                    } else {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "Actualizar",
                            tint = PrimaryBlue
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }

        if (!isLoading && requests.isEmpty()) {
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 24.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(32.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.Assignment,
                            contentDescription = null,
                            tint = Color(0xFF94A3B8),
                            modifier = Modifier.size(48.dp)
                        )
                        Spacer(modifier = Modifier.height(14.dp))
                        Text(
                            text = "No tienes solicitudes registradas",
                            color = TextPrimary,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Explora vehículos en el catálogo e inicia tu pre-evaluación con los bancos aliados.",
                            color = TextSecondary,
                            fontSize = 13.sp,
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                    }
                }
            }
        }

        items(requests, key = { it.id }) { request ->
            CreditRequestCard(request = request)
            Spacer(modifier = Modifier.height(12.dp))
        }
    }
}

@Composable
private fun CreditRequestCard(request: CreditRequest) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Header: Title + Status Badge
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Row(
                    modifier = Modifier.weight(1f),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .background(Color(0xFFEFF6FF), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.DirectionsCar,
                            contentDescription = null,
                            tint = PrimaryBlue,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(10.dp))

                    Column {
                        Text(
                            text = request.vehicleTitle,
                            color = TextPrimary,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                        Text(
                            text = request.dealershipName,
                            color = TextSecondary,
                            fontSize = 13.sp
                        )
                    }
                }

                StatusBadge(status = request.status)
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Details: Monto, Plazo, Fecha
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFF8FAFC), RoundedCornerShape(10.dp))
                    .padding(horizontal = 12.dp, vertical = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Monto estimado",
                        color = TextSecondary,
                        fontSize = 11.sp
                    )
                    Text(
                        text = if (request.requestedAmountUsd > 0) "$${"%,.0f".format(request.requestedAmountUsd)} USD" else "Consultar",
                        color = TextPrimary,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = "Plazo",
                        color = TextSecondary,
                        fontSize = 11.sp
                    )
                    Text(
                        text = "${request.termMonths} meses",
                        color = TextPrimary,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.CalendarToday,
                        contentDescription = null,
                        tint = TextSecondary,
                        modifier = Modifier.size(12.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = request.date,
                        color = TextSecondary,
                        fontSize = 12.sp
                    )
                }
            }
        }
    }
}

@Composable
private fun StatusBadge(status: CreditStatus) {
    val (bgColor, textColor) = when (status) {
        CreditStatus.APPROVED, CreditStatus.PRE_APPROVED -> Pair(Color(0xFFDCFCE7), Color(0xFF166534))
        CreditStatus.IN_REVIEW -> Pair(Color(0xFFFEF3C7), Color(0xFF92400E))
        CreditStatus.PENDING -> Pair(Color(0xFFDBEAFE), Color(0xFF1E40AF))
        CreditStatus.REJECTED -> Pair(Color(0xFFFEE2E2), Color(0xFF991B1B))
        CreditStatus.DISBURSED -> Pair(Color(0xFFF3E8FF), Color(0xFF6B21A8))
    }

    Box(
        modifier = Modifier
            .background(bgColor, RoundedCornerShape(12.dp))
            .padding(horizontal = 10.dp, vertical = 4.dp)
    ) {
        Text(
            text = status.label,
            color = textColor,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold
        )
    }
}

@Preview(showBackground = true, widthDp = 390, heightDp = 844)
@Composable
fun MisSolicitudesScreenPreview() {
    SmartFinanceDriveTheme {
        MobileShell(selectedNavIndex = 4) {
            MisSolicitudesScreen()
        }
    }
}
