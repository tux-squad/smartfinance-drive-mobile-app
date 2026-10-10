package com.smartfinance.mobile.profiles.presentation.profile

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
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.smartfinance.mobile.R
import com.smartfinance.mobile.core.ui.theme.AccentOrange
import com.smartfinance.mobile.core.ui.theme.BorderSoft
import com.smartfinance.mobile.core.ui.theme.PrimaryBlue
import com.smartfinance.mobile.core.ui.theme.SmartFinanceDriveTheme
import com.smartfinance.mobile.core.ui.theme.StatusApprovedBg
import com.smartfinance.mobile.core.ui.theme.StatusApprovedText
import com.smartfinance.mobile.core.ui.theme.StatusReviewBg
import com.smartfinance.mobile.core.ui.theme.StatusReviewText
import com.smartfinance.mobile.core.ui.theme.TextPrimary
import com.smartfinance.mobile.core.ui.theme.TextSecondary
import com.smartfinance.mobile.financing.domain.model.CreditRequest
import com.smartfinance.mobile.financing.domain.model.CreditStatus
import com.smartfinance.mobile.shared.ui.layouts.MobileShell
import com.smartfinance.mobile.profiles.domain.model.Profile
import com.smartfinance.mobile.profiles.domain.repository.ProfileRepository
import com.smartfinance.mobile.financing.domain.repository.FinancingRepository
import kotlinx.coroutines.launch

@Composable
fun ProfileScreen(
    profileRepository: ProfileRepository? = null,
    financingRepository: FinancingRepository? = null,
    userId: String? = null,
    onNavigateToSettings: () -> Unit = {},
    onNavigateToRequests: () -> Unit = {}
) {
    var fullName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var dni by remember { mutableStateOf("") }
    var isVerifyingDni by remember { mutableStateOf(false) }
    var dniVerificationMessage by remember { mutableStateOf<String?>(null) }
    var saveFeedbackMessage by remember { mutableStateOf<String?>(null) }
    var profile by remember { mutableStateOf<Profile?>(null) }
    var requests by remember { mutableStateOf<List<CreditRequest>>(emptyList()) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(profileRepository, financingRepository, userId) {
        if (profileRepository != null && !userId.isNullOrBlank()) {
            profile = profileRepository.getProfileByUserId(userId)
            profile?.let {
                fullName = it.fullName
                email = it.email
                dni = it.documentNumber ?: ""
            }
        }
        if (financingRepository != null) {
            requests = financingRepository.getMyCreditRequests()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8FAFC))
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        // Breadcrumb
        Text(
            text = "Panel de Comprador / Mi Perfil",
            color = TextSecondary,
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium
        )

        Spacer(modifier = Modifier.height(6.dp))

        // Screen Heading with Settings Action Button
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "Mi Perfil",
                    color = TextPrimary,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = "Actualiza tus datos para tus pre-evaluaciones.",
                    color = TextSecondary,
                    fontSize = 13.sp
                )
            }

            IconButton(
                onClick = onNavigateToSettings,
                modifier = Modifier
                    .size(36.dp)
                    .background(Color.White, CircleShape)
                    .border(1.dp, BorderSoft, CircleShape)
            ) {
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = "Configuración",
                    tint = PrimaryBlue,
                    modifier = Modifier.size(20.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Personal Information Card
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
                    .padding(16.dp)
            ) {
                Text(
                    text = "Información Personal",
                    color = TextPrimary,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(14.dp))

                // DNI / Document Number Field with RENIEC lookup
                Text(
                    text = "Documento de Identidad (DNI)",
                    color = Color(0xFF334155),
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(bottom = 6.dp)
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = dni,
                        onValueChange = { if (it.length <= 8) dni = it },
                        placeholder = { Text("8 dígitos...", color = TextSecondary) },
                        modifier = Modifier.weight(1f),
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

                    Spacer(modifier = Modifier.width(8.dp))

                    Button(
                        onClick = {
                            if (dni.length == 8 && profileRepository != null && !isVerifyingDni) {
                                scope.launch {
                                    isVerifyingDni = true
                                    dniVerificationMessage = null
                                    val reniec = profileRepository.consultReniecDni(dni)
                                    isVerifyingDni = false
                                    if (reniec != null) {
                                        fullName = reniec.fullLegalName
                                        dniVerificationMessage = "✓ Identidad verificada con RENIEC"
                                    } else {
                                        dniVerificationMessage = "No se encontraron datos para este DNI"
                                    }
                                }
                            }
                        },
                        enabled = dni.length == 8 && !isVerifyingDni,
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = PrimaryBlue),
                        modifier = Modifier.height(48.dp)
                    ) {
                        if (isVerifyingDni) {
                            androidx.compose.material3.CircularProgressIndicator(
                                modifier = Modifier.size(16.dp),
                                strokeWidth = 2.dp,
                                color = Color.White
                            )
                        } else {
                            Text(
                                text = "RENIEC",
                                color = Color.White,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                if (dniVerificationMessage != null) {
                    Text(
                        text = dniVerificationMessage!!,
                        color = if (dniVerificationMessage!!.startsWith("✓")) Color(0xFF10B981) else Color(0xFFEF4444),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.padding(top = 4.dp, bottom = 4.dp)
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Full Name Field
                Text(
                    text = "Nombre Completo",
                    color = Color(0xFF334155),
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(bottom = 6.dp)
                )
                OutlinedTextField(
                    value = fullName,
                    onValueChange = { fullName = it },
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

                Spacer(modifier = Modifier.height(12.dp))

                // Email Field
                Text(
                    text = "Correo electrónico",
                    color = Color(0xFF334155),
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(bottom = 6.dp)
                )
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
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

                Spacer(modifier = Modifier.height(18.dp))

                // Save Changes Button
                Button(
                    onClick = {
                        if (profileRepository != null) {
                            scope.launch {
                                saveFeedbackMessage = null
                                val target = profile?.copy(
                                    fullName = fullName,
                                    email = email,
                                    documentNumber = dni
                                ) ?: Profile(
                                    id = "",
                                    userId = userId ?: "",
                                    fullName = fullName,
                                    email = email,
                                    documentNumber = dni
                                )
                                profile = profileRepository.updateProfile(target)
                                saveFeedbackMessage = "¡Perfil guardado correctamente!"
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(46.dp),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = AccentOrange)
                ) {
                    Text(
                        text = "Guardar Cambios",
                        color = Color.White,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                if (saveFeedbackMessage != null) {
                    Text(
                        text = saveFeedbackMessage!!,
                        color = Color(0xFF10B981),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(top = 8.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(22.dp))

        // Mis Solicitudes Section
        Text(
            text = "Mis Solicitudes",
            color = TextPrimary,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(2.dp))

        Text(
            text = "Consulta el estado de tus solicitudes de financiamiento.",
            color = TextSecondary,
            fontSize = 13.sp
        )

        Spacer(modifier = Modifier.height(12.dp))

        // List of Requests
        requests.forEach { request ->
            RequestItemCard(request = request)
            Spacer(modifier = Modifier.height(10.dp))
        }
    }
}

@Composable
private fun RequestItemCard(request: CreditRequest) {
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
                .padding(14.dp)
        ) {
            // Header: Car Icon + Vehicle Name
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .background(Color(0xFFEFF6FF), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.DirectionsCar,
                        contentDescription = null,
                        tint = Color(0xFF2563EB),
                        modifier = Modifier.size(18.dp)
                    )
                }

                Spacer(modifier = Modifier.width(10.dp))

                Text(
                    text = request.vehicleTitle,
                    color = TextPrimary,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Details Rows
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Concesionaria:",
                    color = TextSecondary,
                    fontSize = 13.sp
                )
                Text(
                    text = request.dealershipName,
                    color = TextPrimary,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Fecha:",
                    color = TextSecondary,
                    fontSize = 13.sp
                )
                Text(
                    text = request.date,
                    color = TextPrimary,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Estado de Crédito:",
                    color = TextSecondary,
                    fontSize = 13.sp
                )

                val (bgColor, textColor, label) = when (request.status) {
                    CreditStatus.APPROVED -> Triple(StatusApprovedBg, StatusApprovedText, stringResource(R.string.approved))
                    CreditStatus.IN_REVIEW -> Triple(StatusReviewBg, StatusReviewText, stringResource(R.string.in_review))
                    CreditStatus.REJECTED -> Triple(Color(0xFFFEE2E2), Color(0xFFDC2626), stringResource(R.string.rejected))
                    CreditStatus.PENDING -> Triple(Color(0xFFE2E8F0), Color(0xFF475569), stringResource(R.string.pending))
                    CreditStatus.PRE_APPROVED -> Triple(Color(0xFFDBEAFE), Color(0xFF2563EB), "Pre-aprobado")
                    CreditStatus.DISBURSED -> Triple(Color(0xFFF3E8FF), Color(0xFF7E22CE), "Desembolsado")
                }

                Box(
                    modifier = Modifier
                        .background(bgColor, RoundedCornerShape(999.dp))
                        .padding(horizontal = 10.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = label,
                        color = textColor,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 390, heightDp = 844)
@Composable
fun ProfileScreenPreview() {
    SmartFinanceDriveTheme {
        MobileShell(selectedNavIndex = 4) {
            ProfileScreen()
        }
    }
}
