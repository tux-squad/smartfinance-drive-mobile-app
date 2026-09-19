package com.smartfinance.mobile.catalog.presentation.comparison

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.smartfinance.mobile.catalog.domain.model.VehicleComparisonItem
import com.smartfinance.mobile.core.ui.theme.AccentOrange
import com.smartfinance.mobile.core.ui.theme.BorderSoft
import com.smartfinance.mobile.core.ui.theme.SmartFinanceDriveTheme
import com.smartfinance.mobile.core.ui.theme.TextPrimary
import com.smartfinance.mobile.core.ui.theme.TextSecondary
import com.smartfinance.mobile.shared.ui.layouts.MobileShell

@Composable
fun VehicleComparisonScreen(
    onBack: () -> Unit = {},
    onSelectForEvaluation: (VehicleComparisonItem) -> Unit = {}
) {
    val vehicle1 = VehicleComparisonItem(
        id = "1",
        title = "Toyota Corolla 2023",
        priceText = "$18,500 USD",
        condition = "Usado certificado",
        mileage = "15,000 km",
        estimatedMonthlyQuota = "$386 USD / mes",
        imageUrl = "https://images.unsplash.com/photo-1553440569-bcc63803a83d?auto=format&fit=crop&w=600&q=80"
    )

    val vehicle2 = VehicleComparisonItem(
        id = "2",
        title = "Mazda 3 2024",
        priceText = "$22,800 USD",
        condition = "Nuevo",
        mileage = "0 km",
        estimatedMonthlyQuota = "$475 USD / mes",
        imageUrl = "https://images.unsplash.com/photo-1541348263662-e0c8de4259ba?auto=format&fit=crop&w=600&q=80"
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
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Atrás",
                    tint = Color(0xFF1E40AF),
                    modifier = Modifier.size(16.dp)
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            Text(
                text = "Panel de Comprador / Concesionaria / Comparar",
                color = TextSecondary,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Screen Heading
        Text(
            text = "Comparar Vehículos",
            color = TextPrimary,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(2.dp))

        Text(
            text = "Revisa y compara los detalles clave frente a frente.",
            color = TextSecondary,
            fontSize = 13.sp
        )

        Spacer(modifier = Modifier.height(16.dp))

        // 2 Side-by-Side Comparison Cards
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            ComparisonCard(
                vehicle = vehicle1,
                onSelect = { onSelectForEvaluation(vehicle1) },
                modifier = Modifier.weight(1f)
            )

            ComparisonCard(
                vehicle = vehicle2,
                onSelect = { onSelectForEvaluation(vehicle2) },
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun ComparisonCard(
    vehicle: VehicleComparisonItem,
    onSelect: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .border(1.dp, BorderSoft, RoundedCornerShape(14.dp)),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp)
        ) {
            // Vehicle Image
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(95.dp)
            ) {
                AsyncImage(
                    model = vehicle.imageUrl,
                    contentDescription = vehicle.title,
                    contentScale = ContentScale.Crop,
                    placeholder = ColorPainter(Color(0xFFE7EEF6)),
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(10.dp))
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Vehicle Title
            Text(
                text = vehicle.title,
                color = TextPrimary,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                maxLines = 1
            )

            Spacer(modifier = Modifier.height(6.dp))

            // Spec 1: Precio
            Text(
                text = "Precio",
                color = TextSecondary,
                fontSize = 11.sp
            )
            Text(
                text = vehicle.priceText,
                color = TextPrimary,
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(6.dp))

            // Spec 2: Condición
            Text(
                text = "Condición",
                color = TextSecondary,
                fontSize = 11.sp
            )
            Text(
                text = vehicle.condition,
                color = TextPrimary,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(6.dp))

            // Spec 3: Kilometraje
            Text(
                text = "Kilometraje",
                color = TextSecondary,
                fontSize = 11.sp
            )
            Text(
                text = vehicle.mileage,
                color = TextPrimary,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(6.dp))

            // Spec 4: Cuota Estimada
            Text(
                text = "Cuota Estimada",
                color = TextSecondary,
                fontSize = 11.sp
            )
            Text(
                text = vehicle.estimatedMonthlyQuota,
                color = Color(0xFF0D9488),
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(10.dp))

            // Action Button
            Button(
                onClick = onSelect,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(38.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = AccentOrange)
            ) {
                Text(
                    text = "Elegir & Evaluar",
                    color = Color.White,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 390, heightDp = 844)
@Composable
fun VehicleComparisonScreenPreview() {
    SmartFinanceDriveTheme {
        MobileShell(selectedNavIndex = 1) {
            VehicleComparisonScreen()
        }
    }
}
