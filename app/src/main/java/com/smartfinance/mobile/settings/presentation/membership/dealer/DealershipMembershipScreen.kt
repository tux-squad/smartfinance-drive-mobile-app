package com.smartfinance.mobile.settings.presentation.membership.dealer

import android.content.Intent
import android.net.Uri
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.OpenInNew
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.smartfinance.mobile.core.ui.theme.AccentOrange
import com.smartfinance.mobile.core.ui.theme.BorderSoft
import com.smartfinance.mobile.core.ui.theme.PrimaryBlue
import com.smartfinance.mobile.settings.domain.model.BillingInvoice
import com.smartfinance.mobile.settings.domain.model.BillingPlan
import com.smartfinance.mobile.settings.domain.model.DealerSubscription
import com.smartfinance.mobile.settings.domain.model.SubscriptionStatus
import com.smartfinance.mobile.settings.domain.repository.BillingRepository
import kotlinx.coroutines.launch

@Composable
fun DealershipMembershipScreen(
    billingRepository: BillingRepository? = null
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    var subscription by remember { mutableStateOf<DealerSubscription?>(null) }
    var plans by remember { mutableStateOf<List<BillingPlan>>(emptyList()) }
    var invoices by remember { mutableStateOf<List<BillingInvoice>>(emptyList()) }
    var isLoading by remember { mutableStateOf(false) }
    var isCheckingOut by remember { mutableStateOf(false) }

    fun loadBillingData() {
        if (billingRepository == null) return
        isLoading = true
        coroutineScope.launch {
            val subResult = billingRepository.getCurrentSubscription()
            subResult.onSuccess { subscription = it }

            val plansResult = billingRepository.getPlans()
            plansResult.onSuccess { plans = it }

            val invResult = billingRepository.getMyInvoices()
            invResult.onSuccess { invoices = it }

            isLoading = false
        }
    }

    LaunchedEffect(Unit) {
        loadBillingData()
    }

    fun startStripeCheckout(planId: String) {
        if (billingRepository == null) return
        isCheckingOut = true
        coroutineScope.launch {
            val result = billingRepository.createCheckoutSession(planId)
            isCheckingOut = false
            result.onSuccess { url ->
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                try {
                    context.startActivity(intent)
                } catch (_: Exception) {}
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF4F7FB))
    ) {
        // Cabecera
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(horizontal = 20.dp, vertical = 16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Configuración / Membresía B2B",
                        color = Color(0xFF64748B),
                        fontSize = 12.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Suscripción Institucional",
                        color = Color(0xFF0F203C),
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                IconButton(onClick = { loadBillingData() }) {
                    Icon(Icons.Default.Refresh, contentDescription = "Actualizar", tint = PrimaryBlue)
                }
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Estatus de tu plan comercial, pasarela Stripe y facturas.",
                color = Color(0xFF64748B),
                fontSize = 13.sp
            )
        }

        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = PrimaryBlue)
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp)
            ) {
                // Tarjeta de Suscripción Actual
                val currentSub = subscription
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Text(
                            text = currentSub?.planName ?: "Plan Concesionaria Premium",
                            color = Color(0xFF0F203C),
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(10.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .background(Color(0xFFD1FAE5), RoundedCornerShape(12.dp))
                                    .padding(horizontal = 10.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = if (currentSub?.status == SubscriptionStatus.ACTIVE) "Suscripción Activa ✓" else "Prueba Gratuita",
                                    color = Color(0xFF059669),
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Text(
                                text = "Próx. Cobro: ${currentSub?.nextBillingDate ?: "14 Nov 2026"}",
                                color = Color(0xFF64748B),
                                fontSize = 12.sp
                            )
                        }

                        Spacer(modifier = Modifier.height(18.dp))

                        // Retorno de Inversión Box
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(Color(0xFFECFDF5), RoundedCornerShape(8.dp))
                                .padding(14.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.BarChart,
                                    contentDescription = null,
                                    tint = Color(0xFF059669),
                                    modifier = Modifier.size(24.dp)
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Column {
                                    Text(
                                        text = "Impacto Comercial de tu Plan",
                                        color = Color(0xFF0F203C),
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(
                                        text = buildAnnotatedString {
                                            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold, color = Color(0xFF059669))) {
                                                append("48 leads cualificados")
                                            }
                                            append(" e inventario conectado con bancos.")
                                        },
                                        color = Color(0xFF0F203C),
                                        fontSize = 12.sp
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(20.dp))
                        Text(
                            text = "BENEFICIOS ACTIVOS",
                            color = Color(0xFF64748B),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.height(10.dp))

                        BenefitItem("Publicación ilimitada en el catálogo vehicular")
                        Spacer(modifier = Modifier.height(6.dp))
                        BenefitItem("Recepción de solicitudes de crédito bancario")
                        Spacer(modifier = Modifier.height(6.dp))
                        BenefitItem("Gestión de asesores comerciales y CRM")
                        Spacer(modifier = Modifier.height(6.dp))
                        BenefitItem("Agendamiento de Test Drives en tiempo real")

                        Spacer(modifier = Modifier.height(18.dp))
                        HorizontalDivider(color = Color(0xFFF1F5F9))
                        Spacer(modifier = Modifier.height(14.dp))

                        Row(verticalAlignment = Alignment.Bottom) {
                            Text(
                                text = "$${currentSub?.price?.toInt() ?: 99}",
                                color = Color(0xFF0F203C),
                                fontSize = 28.sp,
                                fontWeight = FontWeight.ExtraBold
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "/ mes",
                                color = Color(0xFF64748B),
                                fontSize = 14.sp,
                                modifier = Modifier.padding(bottom = 4.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(18.dp))

                        // Botones de acción
                        Button(
                            onClick = {
                                if (invoices.isNotEmpty() && !invoices.first().pdfUrl.isNullOrBlank()) {
                                    val intent = Intent(Intent.ACTION_VIEW, Uri.parse(invoices.first().pdfUrl))
                                    try { context.startActivity(intent) } catch (_: Exception) {}
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = AccentOrange),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(46.dp)
                        ) {
                            Icon(Icons.Default.Download, contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("Descargar Última Factura (PDF)", color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        OutlinedButton(
                            onClick = {
                                startStripeCheckout(currentSub?.id ?: "plan-premium")
                            },
                            shape = RoundedCornerShape(10.dp),
                            border = androidx.compose.foundation.BorderStroke(1.dp, BorderSoft),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(46.dp)
                        ) {
                            if (isCheckingOut) {
                                CircularProgressIndicator(modifier = Modifier.size(20.dp), strokeWidth = 2.dp, color = PrimaryBlue)
                            } else {
                                Icon(Icons.Default.CreditCard, contentDescription = null, tint = PrimaryBlue, modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Portal de Pago Seguro Stripe", color = PrimaryBlue, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Historial de Facturas
                Text(
                    text = "Historial de Facturación",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF0F203C)
                )
                Spacer(modifier = Modifier.height(10.dp))

                if (invoices.isEmpty()) {
                    Text("No hay facturas registradas aún.", color = Color(0xFF64748B), fontSize = 13.sp)
                } else {
                    invoices.forEach { invoice ->
                        InvoiceCard(invoice = invoice, onDownload = { url ->
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                            try { context.startActivity(intent) } catch (_: Exception) {}
                        })
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }

                Spacer(modifier = Modifier.height(30.dp))
            }
        }
    }
}

@Composable
private fun InvoiceCard(
    invoice: BillingInvoice,
    onDownload: (String) -> Unit
) {
    Card(
        shape = RoundedCornerShape(10.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(text = invoice.invoiceNumber, fontWeight = FontWeight.Bold, fontSize = 14.sp, color = Color(0xFF0F203C))
                Text(text = invoice.date, fontSize = 12.sp, color = Color(0xFF64748B))
            }
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "$${invoice.amount.toInt()} ${invoice.currency}",
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp,
                    color = Color(0xFF0F203C)
                )
                Spacer(modifier = Modifier.width(10.dp))
                IconButton(
                    onClick = { invoice.pdfUrl?.let { onDownload(it) } },
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(Icons.Default.OpenInNew, contentDescription = "Ver PDF", tint = PrimaryBlue, modifier = Modifier.size(18.dp))
                }
            }
        }
    }
}

@Composable
private fun BenefitItem(text: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(
            imageVector = Icons.Default.Check,
            contentDescription = null,
            tint = Color(0xFF059669),
            modifier = Modifier.size(16.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = text,
            color = Color(0xFF475569),
            fontSize = 13.sp
        )
    }
}
