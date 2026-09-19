package com.smartfinance.mobile.financing.presentation.preevaluation

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
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
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
import com.smartfinance.mobile.core.ui.theme.AccentOrange
import com.smartfinance.mobile.core.ui.theme.BorderSoft
import com.smartfinance.mobile.core.ui.theme.PrimaryBlue
import com.smartfinance.mobile.core.ui.theme.SmartFinanceDriveTheme
import com.smartfinance.mobile.core.ui.theme.TextPrimary
import com.smartfinance.mobile.core.ui.theme.TextSecondary
import com.smartfinance.mobile.shared.ui.layouts.MobileShell
import com.smartfinance.mobile.financing.domain.model.PreEvaluation
import com.smartfinance.mobile.financing.domain.repository.FinancingRepository
import kotlinx.coroutines.launch

@Composable
fun PreEvaluationScreen(
    financingRepository: FinancingRepository? = null,
    vehicleId: String = "1",
    vehicleName: String = "Toyota RAV4 2024",
    priceText: String = "$28,500 USD",
    dealershipName: String = "EuroMotors",
    onBack: () -> Unit = {},
    onSubmit: () -> Unit = {}
) {
    var documentNumber by remember { mutableStateOf("72481940") }
    var netIncome by remember { mutableStateOf("2,500") }
    var selectedBank by remember { mutableStateOf("BCP (Banco de Crédito del Perú)") }
    var isBankDropdownExpanded by remember { mutableStateOf(false) }
    var consentAccepted by remember { mutableStateOf(true) }
    var isSubmitting by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    val scope = rememberCoroutineScope()

    val bankOptions = listOf(
        "BCP (Banco de Crédito del Perú)",
        "BBVA Perú",
        "Interbank",
        "Scotiabank Perú",
        "BanBif"
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
                text = "Panel de Comprador / Concesionaria / Pre-evaluación",
                color = TextSecondary,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium
            )
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Heading & Subtitle
        Text(
            text = "Pre-evaluación Crediticia",
            color = TextPrimary,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(3.dp))

        Text(
            text = "Ingresa tus datos para pre-evaluar tu capacidad crediticia de forma segura con entidades afiliadas.",
            color = TextSecondary,
            fontSize = 13.sp,
            lineHeight = 18.sp
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Selected Vehicle Summary Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, BorderSoft, RoundedCornerShape(14.dp)),
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                AsyncImage(
                    model = "https://images.unsplash.com/photo-1553440569-bcc63803a83d?auto=format&fit=crop&w=400&q=80",
                    contentDescription = vehicleName,
                    contentScale = ContentScale.Crop,
                    placeholder = ColorPainter(Color(0xFFE7EEF6)),
                    modifier = Modifier
                        .size(56.dp)
                        .clip(RoundedCornerShape(8.dp))
                )

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Text(
                        text = vehicleName,
                        color = TextPrimary,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(2.dp))

                    Text(
                        text = "$priceText · $dealershipName",
                        color = TextSecondary,
                        fontSize = 13.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Field 1: Número de Documento (DNI)
        Text(
            text = "NÚMERO DE DOCUMENTO (DNI)",
            color = TextSecondary,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 0.5.sp,
            modifier = Modifier.padding(bottom = 6.dp)
        )
        OutlinedTextField(
            value = documentNumber,
            onValueChange = { documentNumber = it },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            shape = RoundedCornerShape(10.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = PrimaryBlue,
                unfocusedBorderColor = BorderSoft,
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                focusedTextColor = TextPrimary,
                unfocusedTextColor = TextPrimary,
                cursorColor = PrimaryBlue
            )
        )

        Spacer(modifier = Modifier.height(14.dp))

        // Field 2: Ingreso Mensual Neto (USD)
        Text(
            text = "INGRESO MENSUAL NETO (USD)",
            color = TextSecondary,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 0.5.sp,
            modifier = Modifier.padding(bottom = 6.dp)
        )
        OutlinedTextField(
            value = netIncome,
            onValueChange = { netIncome = it },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true,
            shape = RoundedCornerShape(10.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedBorderColor = PrimaryBlue,
                unfocusedBorderColor = BorderSoft,
                focusedContainerColor = Color.White,
                unfocusedContainerColor = Color.White,
                focusedTextColor = TextPrimary,
                unfocusedTextColor = TextPrimary,
                cursorColor = PrimaryBlue
            )
        )

        Spacer(modifier = Modifier.height(14.dp))

        // Field 3: Banco de Preferencia Dropdown
        Text(
            text = "BANCO DE PREFERENCIA",
            color = TextSecondary,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            letterSpacing = 0.5.sp,
            modifier = Modifier.padding(bottom = 6.dp)
        )

        Box(
            modifier = Modifier.fillMaxWidth()
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White, RoundedCornerShape(10.dp))
                    .border(1.dp, BorderSoft, RoundedCornerShape(10.dp))
                    .clickable { isBankDropdownExpanded = true }
                    .padding(horizontal = 14.dp, vertical = 14.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = selectedBank,
                        color = TextPrimary,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium
                    )

                    Icon(
                        imageVector = Icons.Default.KeyboardArrowDown,
                        contentDescription = "Desplegar",
                        tint = TextSecondary,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }

            DropdownMenu(
                expanded = isBankDropdownExpanded,
                onDismissRequest = { isBankDropdownExpanded = false },
                modifier = Modifier
                    .fillMaxWidth(0.9f)
                    .background(Color.White)
            ) {
                bankOptions.forEach { bank ->
                    DropdownMenuItem(
                        text = {
                            Text(
                                text = bank,
                                color = TextPrimary,
                                fontSize = 14.sp
                            )
                        },
                        onClick = {
                            selectedBank = bank
                            isBankDropdownExpanded = false
                        }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Consent Checkbox Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Checkbox(
                checked = consentAccepted,
                onCheckedChange = { consentAccepted = it },
                colors = CheckboxDefaults.colors(
                    checkedColor = Color(0xFF1E40AF),
                    uncheckedColor = BorderSoft,
                    checkmarkColor = Color.White
                )
            )

            Text(
                text = "Autorizo el tratamiento de mis datos para evaluación crediticia externa.",
                color = TextSecondary,
                fontSize = 12.sp,
                lineHeight = 16.sp,
                modifier = Modifier.clickable { consentAccepted = !consentAccepted }
            )
        }

        Spacer(modifier = Modifier.height(18.dp))

        // Submit Button
        Button(
            onClick = {
                if (!consentAccepted || financingRepository == null) {
                    errorMessage = if (!consentAccepted) {
                        "Debes aceptar el tratamiento de datos."
                    } else {
                        "El servicio de financiación no está disponible."
                    }
                } else {
                    scope.launch {
                        isSubmitting = true
                        errorMessage = null
                        val submitted = financingRepository.submitPreEvaluation(
                            PreEvaluation(
                                vehicleId = vehicleId,
                                vehicleTitle = vehicleName,
                                vehiclePriceUsd = priceText
                                    .replace(Regex("[^0-9.]"), "")
                                    .toDoubleOrNull() ?: 0.0,
                                dealershipName = dealershipName,
                                documentNumber = documentNumber,
                                netMonthlyIncomeUsd = netIncome
                                    .replace(",", "")
                                    .toDoubleOrNull() ?: 0.0,
                                preferredBank = selectedBank,
                                consentAccepted = consentAccepted
                            )
                        )
                        isSubmitting = false
                        if (submitted) onSubmit()
                        else errorMessage = "No se pudo enviar la solicitud."
                    }
                }
            },
            enabled = !isSubmitting,
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            shape = RoundedCornerShape(10.dp),
            colors = ButtonDefaults.buttonColors(containerColor = AccentOrange)
        ) {
            Text(
                text = if (isSubmitting) "Enviando..." else "Enviar Solicitud a Bancos",
                color = Color.White,
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold
            )
        }
        if (errorMessage != null) {
            Text(
                text = errorMessage!!,
                color = Color(0xFFB91C1C),
                fontSize = 12.sp,
                modifier = Modifier.padding(top = 8.dp)
            )
        }
    }
}

@Preview(showBackground = true, widthDp = 390, heightDp = 844)
@Composable
fun PreEvaluationScreenPreview() {
    SmartFinanceDriveTheme {
        MobileShell(selectedNavIndex = 1) {
            PreEvaluationScreen()
        }
    }
}
