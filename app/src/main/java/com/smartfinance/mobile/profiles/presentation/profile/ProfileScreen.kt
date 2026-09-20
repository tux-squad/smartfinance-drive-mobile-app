package com.smartfinance.mobile.profiles.presentation.profile

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
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.res.stringResource
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
    onNavigateToRequests: () -> Unit = {},
    isSpanish: Boolean = true,
    onLanguageChange: (Boolean) -> Unit = {}
) {
    var fullName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var profile by remember { mutableStateOf<Profile?>(null) }
    var requests by remember { mutableStateOf<List<CreditRequest>>(emptyList()) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(profileRepository, financingRepository, userId) {
        if (profileRepository != null && !userId.isNullOrBlank()) {
            profile = profileRepository.getProfileByUserId(userId)
            profile?.let {
                fullName = it.fullName
                email = it.email
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
            text = stringResource(R.string.buyer_profile_breadcrumb),
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
                    text = stringResource(R.string.my_profile),
                    color = TextPrimary,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(2.dp))

                Text(
                    text = stringResource(R.string.profile_description),
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
                    contentDescription = stringResource(R.string.settings),
                    tint = PrimaryBlue,
                    modifier = Modifier.size(20.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

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
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = stringResource(R.string.language_label),
                        color = TextPrimary,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = stringResource(R.string.profile_language_description),
                        color = TextSecondary,
                        fontSize = 12.sp
                    )
                }
                Row(
                    modifier = Modifier
                        .background(Color(0xFFF2F4F7), RoundedCornerShape(10.dp))
                        .padding(4.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    ProfileLanguageOption(
                        label = stringResource(R.string.language_spanish),
                        selected = isSpanish,
                        onClick = { onLanguageChange(true) }
                    )
                    ProfileLanguageOption(
                        label = stringResource(R.string.language_english),
                        selected = !isSpanish,
                        onClick = { onLanguageChange(false) }
                    )
                }
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
                    text = stringResource(R.string.personal_information),
                    color = TextPrimary,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(14.dp))

                // Full Name Field
                Text(
                    text = stringResource(R.string.full_name),
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
                    text = stringResource(R.string.email),
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
                        val current = profile
                        if (profileRepository != null && current != null) {
                            scope.launch {
                                profile = profileRepository.updateProfile(
                                    current.copy(fullName = fullName, email = email)
                                )
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
                        text = stringResource(R.string.save_changes),
                        color = Color.White,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(22.dp))

        // Mis Solicitudes Section
        Text(
            text = stringResource(R.string.my_requests),
            color = TextPrimary,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(2.dp))

        Text(
            text = stringResource(R.string.requests_description),
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
private fun ProfileLanguageOption(
    label: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Surface(
        color = if (selected) PrimaryBlue else Color.Transparent,
        shape = RoundedCornerShape(7.dp),
        modifier = Modifier.clickable(onClick = onClick)
    ) {
        Text(
            text = label,
            color = if (selected) Color.White else TextSecondary,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 7.dp)
        )
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
                    text = stringResource(R.string.dealership),
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
                    text = stringResource(R.string.date),
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
                    text = stringResource(R.string.credit_status),
                    color = TextSecondary,
                    fontSize = 13.sp
                )

                val (bgColor, textColor, label) = when (request.status) {
                    CreditStatus.APPROVED -> Triple(StatusApprovedBg, StatusApprovedText, stringResource(R.string.approved))
                    CreditStatus.IN_REVIEW -> Triple(StatusReviewBg, StatusReviewText, stringResource(R.string.in_review))
                    CreditStatus.REJECTED -> Triple(Color(0xFFFEE2E2), Color(0xFFDC2626), stringResource(R.string.rejected))
                    CreditStatus.PENDING -> Triple(Color(0xFFE2E8F0), Color(0xFF475569), stringResource(R.string.pending))
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
