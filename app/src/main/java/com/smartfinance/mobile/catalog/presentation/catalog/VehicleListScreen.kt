package com.smartfinance.mobile.catalog.presentation.catalog

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
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Circle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.smartfinance.mobile.core.ui.theme.AccentOrange
import com.smartfinance.mobile.core.ui.theme.BorderSoft
import com.smartfinance.mobile.core.ui.theme.PrimaryBlue
import com.smartfinance.mobile.core.ui.theme.PrimaryBlueDark
import com.smartfinance.mobile.core.ui.theme.SmartFinanceDriveTheme
import com.smartfinance.mobile.core.ui.theme.SurfaceCard
import com.smartfinance.mobile.core.ui.theme.SurfaceDark
import com.smartfinance.mobile.core.ui.theme.TextPrimary
import com.smartfinance.mobile.core.ui.theme.TextSecondary
import com.smartfinance.mobile.shared.ui.layouts.MobileShell
import kotlinx.coroutines.launch
import com.smartfinance.mobile.catalog.domain.model.Vehicle
import com.smartfinance.mobile.catalog.domain.repository.VehicleRepository
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Surface

@Composable
fun VehicleListScreen(
    vehicleRepository: VehicleRepository? = null,
    username: String? = null,
    onNavigateToDealerships: () -> Unit = {},
    onOpenDetail: (Vehicle) -> Unit = {},
    onRequestPreEvaluation: (String, String, String) -> Unit = { _, _, _ -> }
) {
    var vehicles by remember { mutableStateOf<List<Vehicle>>(emptyList()) }
    var brands by remember { mutableStateOf<List<String>>(emptyList()) }
    var selectedBrand by remember { mutableStateOf("Todos") }
    var searchQuery by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }
    var currentPage by remember { mutableStateOf(0) }
    var hasMoreVehicles by remember { mutableStateOf(true) }
    var isLoadingMore by remember { mutableStateOf(false) }
    val scope = androidx.compose.runtime.rememberCoroutineScope()

    LaunchedEffect(Unit) {
        if (vehicleRepository != null) {
            brands = listOf("Todos") + vehicleRepository.getBrands()
        }
    }

    LaunchedEffect(selectedBrand, vehicleRepository) {
        if (vehicleRepository != null) {
            isLoading = true
            currentPage = 0
            val initial = vehicleRepository.getVehicles(
                page = 0,
                size = 15,
                brand = if (selectedBrand == "Todos") null else selectedBrand
            )
            vehicles = initial
            hasMoreVehicles = initial.size >= 15
            isLoading = false
        }
    }

    val filteredVehicles = remember(vehicles, searchQuery) {
        if (searchQuery.isBlank()) {
            vehicles
        } else {
            val query = searchQuery.trim().lowercase()
            vehicles.filter {
                it.brand.lowercase().contains(query) ||
                    it.model.lowercase().contains(query) ||
                    it.year.toString().contains(query)
            }
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF4F7FB)),
        contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 18.dp, vertical = 12.dp)
    ) {
        // Encabezado
        item {
            Text(
                text = "Panel de Comprador / Inicio",
                color = TextSecondary,
                fontSize = 14.sp
            )

            val displayName = username?.takeIf { it.isNotBlank() } ?: "Comprador"
            Text(
                text = "Bienvenido, $displayName",
                color = TextPrimary,
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(top = 6.dp)
            )

            Text(
                text = "Explora el catálogo certificado y simula tu financiamiento.",
                color = TextSecondary,
                fontSize = 14.sp,
                modifier = Modifier.padding(top = 2.dp)
            )

            // Buscador
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text("Buscar por marca o modelo...", color = TextSecondary) },
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = null,
                        tint = TextSecondary
                    )
                },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 14.dp),
                shape = RoundedCornerShape(16.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = PrimaryBlue,
                    unfocusedBorderColor = BorderSoft,
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White,
                    cursorColor = PrimaryBlue
                )
            )

            // Chips de Marcas
            if (brands.isNotEmpty()) {
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(brands) { brand ->
                        val isSelected = brand == selectedBrand
                        FilterChip(
                            selected = isSelected,
                            onClick = { selectedBrand = brand },
                            label = { Text(brand, fontSize = 13.sp) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = PrimaryBlue,
                                selectedLabelColor = Color.White,
                                containerColor = Color.White,
                                labelColor = TextPrimary
                            ),
                            border = FilterChipDefaults.filterChipBorder(
                                enabled = true,
                                selected = isSelected,
                                borderColor = if (isSelected) PrimaryBlue else BorderSoft
                            ),
                            shape = RoundedCornerShape(20.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Vehículos disponibles (${filteredVehicles.size})",
                    color = TextPrimary,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )

                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(18.dp),
                        strokeWidth = 2.dp,
                        color = PrimaryBlue
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))
        }

        // Estado vacío
        if (!isLoading && filteredVehicles.isEmpty()) {
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 20.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(28.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = null,
                            tint = Color(0xFF94A3B8),
                            modifier = Modifier.size(44.dp)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Text(
                            text = "No se encontraron vehículos",
                            color = TextPrimary,
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "Prueba con otra marca o término de búsqueda.",
                            color = TextSecondary,
                            fontSize = 13.sp,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }

        // Lista de vehículos
        items(filteredVehicles, key = { it.id }) { vehicle ->
            val title = "${vehicle.brand} ${vehicle.model} ${vehicle.year}"
            val price = "$${"%,.2f".format(vehicle.priceUsd)} USD"
            val details = "${vehicle.city} • ${vehicle.kilometers} km"

            OfferCard(
                title = title,
                price = price,
                details = details,
                imageUrl = vehicle.imageUrl,
                onCardClick = { onOpenDetail(vehicle) },
                onRequestPreEvaluation = { onRequestPreEvaluation(title, price, vehicle.id) }
            )
            Spacer(modifier = Modifier.height(14.dp))
        }

        // Paginación: Cargar más vehículos
        if (hasMoreVehicles && !isLoading && filteredVehicles.isNotEmpty()) {
            item {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    if (isLoadingMore) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(24.dp),
                            strokeWidth = 2.dp,
                            color = PrimaryBlue
                        )
                    } else {
                        androidx.compose.material3.OutlinedButton(
                            onClick = {
                                if (vehicleRepository != null && !isLoadingMore) {
                                    kotlinx.coroutines.CoroutineScope(kotlinx.coroutines.Dispatchers.Main).let {
                                        scope.launch {
                                            isLoadingMore = true
                                            val nextPage = currentPage + 1
                                            val more = vehicleRepository.getVehicles(
                                                page = nextPage,
                                                size = 15,
                                                brand = if (selectedBrand == "Todos") null else selectedBrand
                                            )
                                            if (more.isNotEmpty()) {
                                                vehicles = vehicles + more
                                                currentPage = nextPage
                                                hasMoreVehicles = more.size >= 15
                                            } else {
                                                hasMoreVehicles = false
                                            }
                                            isLoadingMore = false
                                        }
                                    }
                                }
                            },
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text("Cargar más vehículos", color = PrimaryBlue, fontWeight = FontWeight.SemiBold)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun OfferCard(
    title: String,
    price: String,
    details: String,
    imageUrl: String,
    onCardClick: () -> Unit = {},
    onRequestPreEvaluation: () -> Unit = {}
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onCardClick() },
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
            ) {
                AsyncImage(
                    model = imageUrl,
                    contentDescription = title,
                    contentScale = ContentScale.Crop,
                    placeholder = ColorPainter(Color(0xFFE5E7EB)),
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(topStart = 18.dp, topEnd = 18.dp))
                )
                Text(
                    text = "Certificado",
                    color = Color.White,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier
                        .padding(12.dp)
                        .background(Color.Black.copy(alpha = 0.40f), RoundedCornerShape(999.dp))
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                )
            }

            Column(modifier = Modifier.padding(14.dp)) {
                Text(
                    text = title,
                    fontWeight = FontWeight.Bold,
                    color = TextPrimary,
                    fontSize = 18.sp
                )
                Text(
                    text = price,
                    fontWeight = FontWeight.Bold,
                    color = PrimaryBlue,
                    fontSize = 18.sp,
                    modifier = Modifier.padding(top = 4.dp)
                )
                Text(
                    text = details,
                    color = TextSecondary,
                    fontSize = 13.sp,
                    modifier = Modifier.padding(top = 4.dp)
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    androidx.compose.material3.OutlinedButton(
                        onClick = onCardClick,
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(10.dp)
                    ) {
                        Text("Ver detalle", fontSize = 13.sp)
                    }

                    Button(
                        onClick = onRequestPreEvaluation,
                        modifier = Modifier.weight(1.3f),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = AccentOrange)
                    ) {
                        Text(
                            text = "Pre-evaluar",
                            color = Color.White,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 13.sp
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun RecommendationCard(imageUrl: String) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 12.dp, bottom = 8.dp),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(132.dp)
        ) {
            AsyncImage(
                model = imageUrl,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                placeholder = ColorPainter(Color(0xFFDDE7F5)),
                modifier = Modifier.fillMaxSize()
            )
            Text(
                text = "Recomendado",
                color = Color.White,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                modifier = Modifier
                    .padding(12.dp)
                    .background(Color.Black.copy(alpha = 0.28f), RoundedCornerShape(999.dp))
                    .padding(horizontal = 10.dp, vertical = 5.dp)
            )
        }
    }
}

@Composable
private fun BottomNavigation() {
    val items = listOf(
        "Inicio" to Icons.Default.Home,
        "Buscar" to Icons.Default.Search,
        "Consulta IA" to Icons.Default.Assessment,
        "Reportes" to Icons.Default.Build,
        "Mi Perfil" to Icons.Default.Person
    )

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(vertical = 10.dp, horizontal = 8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically
        ) {
            items.forEachIndexed { index, item ->
                val selected = index == 0
                val iconColor = if (selected) PrimaryBlue else TextSecondary
                val labelColor = if (selected) PrimaryBlue else TextSecondary
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .clickable { }
                        .padding(vertical = 6.dp)
                ) {
                    Icon(
                        imageVector = item.second,
                        contentDescription = item.first,
                        tint = iconColor,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = item.first,
                        color = labelColor,
                        fontSize = 10.sp,
                        textAlign = TextAlign.Center,
                        maxLines = 1
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 390, heightDp = 844)
@Composable
fun VehicleListScreenPreview() {
    SmartFinanceDriveTheme {
        MobileShell(selectedNavIndex = 0) {
            VehicleListScreen()
        }
    }
}
