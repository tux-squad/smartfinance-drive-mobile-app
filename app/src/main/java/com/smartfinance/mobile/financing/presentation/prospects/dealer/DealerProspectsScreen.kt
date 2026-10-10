package com.smartfinance.mobile.financing.presentation.prospects.dealer

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
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
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import coil.compose.AsyncImage
import com.smartfinance.mobile.core.ui.theme.AccentOrange
import com.smartfinance.mobile.core.ui.theme.BorderSoft
import com.smartfinance.mobile.core.ui.theme.PrimaryBlue
import com.smartfinance.mobile.crm.domain.model.Prospect
import com.smartfinance.mobile.crm.domain.model.ProspectStatus
import com.smartfinance.mobile.crm.domain.model.SalesAgent
import com.smartfinance.mobile.crm.domain.repository.CrmRepository
import kotlinx.coroutines.launch

@Composable
fun DealerProspectsScreen(
    crmRepository: CrmRepository? = null
) {
    val coroutineScope = rememberCoroutineScope()
    var selectedTab by remember { mutableIntStateOf(0) } // 0 = Prospectos, 1 = Asesores

    var prospects by remember { mutableStateOf<List<Prospect>>(emptyList()) }
    var salesAgents by remember { mutableStateOf<List<SalesAgent>>(emptyList()) }
    var isLoading by remember { mutableStateOf(false) }
    var showAddAgentDialog by remember { mutableStateOf(false) }

    fun loadData() {
        if (crmRepository == null) return
        isLoading = true
        coroutineScope.launch {
            val prospectsResult = crmRepository.getProspects()
            prospectsResult.onSuccess { prospects = it }

            val agentsResult = crmRepository.getSalesAgents()
            agentsResult.onSuccess { salesAgents = it }

            isLoading = false
        }
    }

    LaunchedEffect(Unit) {
        loadData()
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
                        text = "Panel Concesionaria / CRM Comercial",
                        color = Color(0xFF64748B),
                        fontSize = 12.sp
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Gestión de Prospectos y Asesores",
                        color = Color(0xFF0F203C),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
                IconButton(onClick = { loadData() }) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = "Actualizar",
                        tint = PrimaryBlue
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = Color.White,
                contentColor = PrimaryBlue,
                indicator = { tabPositions ->
                    TabRowDefaults.SecondaryIndicator(
                        Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                        color = PrimaryBlue
                    )
                }
            ) {
                Tab(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    text = {
                        Text(
                            text = "Prospectos (${prospects.size})",
                            fontWeight = if (selectedTab == 0) FontWeight.Bold else FontWeight.Medium,
                            fontSize = 14.sp
                        )
                    }
                )
                Tab(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    text = {
                        Text(
                            text = "Asesores (${salesAgents.size})",
                            fontWeight = if (selectedTab == 1) FontWeight.Bold else FontWeight.Medium,
                            fontSize = 14.sp
                        )
                    }
                )
            }
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
            when (selectedTab) {
                0 -> ProspectsListTab(
                    prospects = prospects,
                    onStatusChange = { prospectId, newStatus ->
                        coroutineScope.launch {
                            crmRepository?.updateProspectStatus(prospectId, newStatus)
                            loadData()
                        }
                    }
                )
                1 -> SalesAgentsListTab(
                    agents = salesAgents,
                    onAddAgentClick = { showAddAgentDialog = true }
                )
            }
        }
    }

    if (showAddAgentDialog) {
        AddSalesAgentDialog(
            onDismiss = { showAddAgentDialog = false },
            onConfirm = { name, email, phone, role ->
                coroutineScope.launch {
                    crmRepository?.createSalesAgent(name, email, phone, role)
                    showAddAgentDialog = false
                    loadData()
                }
            }
        )
    }
}

@Composable
private fun ProspectsListTab(
    prospects: List<Prospect>,
    onStatusChange: (String, ProspectStatus) -> Unit
) {
    if (prospects.isEmpty()) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            contentAlignment = Alignment.Center
        ) {
            Text("No hay prospectos asignados en este momento", color = Color(0xFF64748B))
        }
        return
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 14.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(prospects) { prospect ->
            ProspectCard(prospect = prospect, onStatusChange = onStatusChange)
        }
    }
}

@Composable
private fun ProspectCard(
    prospect: Prospect,
    onStatusChange: (String, ProspectStatus) -> Unit
) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = prospect.fullName,
                        color = Color(0xFF0F203C),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = prospect.vehicleInterest,
                        color = PrimaryBlue,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
                ProspectStatusBadge(prospect.status)
            }

            Spacer(modifier = Modifier.height(10.dp))
            HorizontalDivider(color = Color(0xFFF1F5F9))
            Spacer(modifier = Modifier.height(10.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Call, contentDescription = null, tint = Color(0xFF64748B), modifier = Modifier.size(15.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text(text = prospect.phone, color = Color(0xFF475569), fontSize = 13.sp)

                Spacer(modifier = Modifier.width(16.dp))

                Icon(Icons.Default.Email, contentDescription = null, tint = Color(0xFF64748B), modifier = Modifier.size(15.dp))
                Spacer(modifier = Modifier.width(6.dp))
                Text(text = prospect.email, color = Color(0xFF475569), fontSize = 13.sp, maxLines = 1)
            }

            if (prospect.notes.isNotBlank()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Nota: ${prospect.notes}",
                    color = Color(0xFF64748B),
                    fontSize = 12.sp
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Selector rápido de estado
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                StatusFilterChip(
                    text = "Test Drive",
                    isSelected = prospect.status == ProspectStatus.TEST_DRIVE,
                    onClick = { onStatusChange(prospect.id, ProspectStatus.TEST_DRIVE) }
                )
                StatusFilterChip(
                    text = "Financiamiento",
                    isSelected = prospect.status == ProspectStatus.FINANCING,
                    onClick = { onStatusChange(prospect.id, ProspectStatus.FINANCING) }
                )
                StatusFilterChip(
                    text = "Venta Ganada",
                    isSelected = prospect.status == ProspectStatus.WON,
                    onClick = { onStatusChange(prospect.id, ProspectStatus.WON) }
                )
            }
        }
    }
}

@Composable
private fun StatusFilterChip(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(6.dp))
            .background(if (isSelected) PrimaryBlue else Color(0xFFF1F5F9))
            .clickable { onClick() }
            .padding(horizontal = 10.dp, vertical = 6.dp)
    ) {
        Text(
            text = text,
            color = if (isSelected) Color.White else Color(0xFF334155),
            fontSize = 11.sp,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
        )
    }
}

@Composable
private fun ProspectStatusBadge(status: ProspectStatus) {
    val (bg, fg, label) = when (status) {
        ProspectStatus.NEW -> Triple(Color(0xFFE0F2FE), Color(0xFF0369A1), "Nuevo")
        ProspectStatus.CONTACTED -> Triple(Color(0xFFFEF3C7), Color(0xFFD97706), "Contactado")
        ProspectStatus.TEST_DRIVE -> Triple(Color(0xFFEDE9FE), Color(0xFF6D28D9), "Test Drive")
        ProspectStatus.FINANCING -> Triple(Color(0xFFDCFCE7), Color(0xFF15803D), "En Crédito")
        ProspectStatus.WON -> Triple(Color(0xFFD1FAE5), Color(0xFF065F46), "Ganado ✓")
        ProspectStatus.LOST -> Triple(Color(0xFFFEE2E2), Color(0xFFB91C1C), "Perdido")
    }
    Box(
        modifier = Modifier
            .background(bg, RoundedCornerShape(12.dp))
            .padding(horizontal = 10.dp, vertical = 4.dp)
    ) {
        Text(text = label, color = fg, fontSize = 11.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
private fun SalesAgentsListTab(
    agents: List<SalesAgent>,
    onAddAgentClick: () -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 14.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Button(
                onClick = onAddAgentClick,
                colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue),
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.fillMaxWidth().height(46.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = null, tint = Color.White)
                Spacer(modifier = Modifier.width(6.dp))
                Text("Registrar Nuevo Asesor", color = Color.White, fontWeight = FontWeight.SemiBold)
            }
        }

        items(agents) { agent ->
            SalesAgentCard(agent = agent)
        }
    }
}

@Composable
private fun SalesAgentCard(agent: SalesAgent) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                if (agent.avatarUrl.isNotBlank()) {
                    AsyncImage(
                        model = agent.avatarUrl,
                        contentDescription = agent.fullName,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.size(48.dp).clip(CircleShape)
                    )
                } else {
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .background(Color(0xFFE2E8F0), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.Person, contentDescription = null, tint = Color(0xFF64748B))
                    }
                }

                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text(
                        text = agent.fullName,
                        color = Color(0xFF0F203C),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = agent.role,
                        color = Color(0xFF64748B),
                        fontSize = 13.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))
            HorizontalDivider(color = Color(0xFFF1F5F9))
            Spacer(modifier = Modifier.height(10.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text("Leads Asignados", color = Color(0xFF64748B), fontSize = 12.sp)
                    Text("${agent.assignedLeadsCount}", color = Color(0xFF0F203C), fontSize = 15.sp, fontWeight = FontWeight.Bold)
                }
                Column {
                    Text("Tasa de Cierre", color = Color(0xFF64748B), fontSize = 12.sp)
                    Text(agent.conversionRate, color = Color(0xFF0284C7), fontSize = 15.sp, fontWeight = FontWeight.Bold)
                }
                Column {
                    Text("Estado", color = Color(0xFF64748B), fontSize = 12.sp)
                    Text(if (agent.active) "Activo" else "Inactivo", color = if (agent.active) Color(0xFF16A34A) else Color(0xFF94A3B8), fontSize = 15.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
private fun AddSalesAgentDialog(
    onDismiss: () -> Unit,
    onConfirm: (name: String, email: String, phone: String, role: String) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var role by remember { mutableStateOf("Asesor Comercial") }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            modifier = Modifier.fillMaxWidth().padding(16.dp)
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Text(
                    text = "Nuevo Asesor Comercial",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF0F203C)
                )
                Spacer(modifier = Modifier.height(14.dp))

                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Nombre Completo") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
                    label = { Text("Correo Corporativo") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = phone,
                    onValueChange = { phone = it },
                    label = { Text("Teléfono de Contacto") },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(modifier = Modifier.height(10.dp))

                OutlinedTextField(
                    value = role,
                    onValueChange = { role = it },
                    label = { Text("Especialidad / Rol") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(20.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    androidx.compose.material3.TextButton(onClick = onDismiss) {
                        Text("Cancelar", color = Color(0xFF64748B))
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Button(
                        onClick = {
                            if (name.isNotBlank() && email.isNotBlank()) {
                                onConfirm(name, email, phone, role)
                            }
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue)
                    ) {
                        Text("Guardar Asesor", color = Color.White)
                    }
                }
            }
        }
    }
}
