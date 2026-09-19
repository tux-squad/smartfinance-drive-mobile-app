package com.smartfinance.mobile.catalog.presentation.inventory.dealer

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.smartfinance.mobile.core.ui.theme.AccentOrange

@Composable
fun DealershipInventoryScreen(
    onNavigateToAddVehicle: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF4F7FB))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(horizontal = 20.dp, vertical = 16.dp)
        ) {
            Text(
                text = "Panel Concesionaria / Gestión de Inventario",
                color = Color(0xFF64748B),
                fontSize = 12.sp
            )
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = "Gestión de Inventario",
                        color = Color(0xFF0F203C),
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Catálogo de unidades de tu concesionaria.",
                        color = Color(0xFF64748B),
                        fontSize = 14.sp
                    )
                }
                Button(
                    onClick = onNavigateToAddVehicle,
                    colors = ButtonDefaults.buttonColors(containerColor = AccentOrange),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.height(40.dp)
                ) {
                    Text(text = "+ Añadir", color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 20.dp, vertical = 8.dp)
        ) {
            val dummyInventory = listOf(
                DealerVehicleItem("Toyota Corolla", "2023", "4 días", "$24,500", "Nuevo", "Activo", "https://images.unsplash.com/photo-1629897048514-3dd74142fb76?auto=format&fit=crop&w=600&q=80"),
                DealerVehicleItem("Honda Civic", "2022", "14 días", "$21,900", "Usado", "Activo", "https://images.unsplash.com/photo-1590362891991-f776e747a588?auto=format&fit=crop&w=600&q=80"),
                DealerVehicleItem("Ford Explorer", "2021", "28 días", "$34,000", "Usado", "Activo", "https://images.unsplash.com/photo-1519245659620-e859806a8d3b?auto=format&fit=crop&w=600&q=80"),
                DealerVehicleItem("Hyundai Elantra", "2023", "2 días", "$22,000", "Nuevo", "Activo", "https://images.unsplash.com/photo-1580273916550-e323be2ae537?auto=format&fit=crop&w=600&q=80"),
                DealerVehicleItem("Jeep Cherokee", "2019", "45 días", "$18,500", "Usado", "Vendido", "https://images.unsplash.com/photo-1533473359331-0135ef1b58bf?auto=format&fit=crop&w=600&q=80"),
                DealerVehicleItem("Mazda CX-5", "2022", "11 días", "$26,400", "Usado", "Activo", "https://images.unsplash.com/photo-1609521263047-f8f205293f24?auto=format&fit=crop&w=600&q=80")
            )
            items(dummyInventory) { item ->
                DealerVehicleCard(item)
                Spacer(modifier = Modifier.height(12.dp))
            }
        }
    }
}

data class DealerVehicleItem(
    val title: String,
    val year: String,
    val days: String,
    val price: String,
    val condition: String, // "Nuevo" or "Usado"
    val status: String,    // "Activo" or "Vendido"
    val imageUrl: String
)

@Composable
private fun DealerVehicleCard(item: DealerVehicleItem) {
    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Image
            AsyncImage(
                model = item.imageUrl,
                contentDescription = item.title,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(8.dp))
            )

            // Info
            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(horizontal = 12.dp)
            ) {
                Text(
                    text = item.title,
                    color = Color(0xFF0F203C),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "${item.year} • ${item.days}",
                    color = Color(0xFF64748B),
                    fontSize = 13.sp
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = item.price,
                    color = Color(0xFF0F203C),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.ExtraBold
                )
            }

            // Tags (Right side)
            Column(
                horizontalAlignment = Alignment.End,
                verticalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.height(80.dp)
            ) {
                // Condition Badge
                val condBgColor = if (item.condition == "Nuevo") Color(0xFFE0F2FE) else Color(0xFFFFF7ED)
                val condTextColor = if (item.condition == "Nuevo") Color(0xFF0284C7) else Color(0xFFEA580C)
                Box(
                    modifier = Modifier
                        .background(condBgColor, RoundedCornerShape(4.dp))
                        .padding(horizontal = 8.dp, vertical = 2.dp)
                ) {
                    Text(text = item.condition, color = condTextColor, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                }

                // Status Badge
                val statusBgColor = if (item.status == "Activo") Color(0xFFD1FAE5) else Color(0xFFF1F5F9)
                val statusTextColor = if (item.status == "Activo") Color(0xFF059669) else Color(0xFF64748B)
                Box(
                    modifier = Modifier
                        .background(statusBgColor, RoundedCornerShape(4.dp))
                        .padding(horizontal = 8.dp, vertical = 2.dp)
                ) {
                    Text(text = item.status, color = statusTextColor, fontSize = 10.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
