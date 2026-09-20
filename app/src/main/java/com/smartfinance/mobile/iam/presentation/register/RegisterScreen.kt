package com.smartfinance.mobile.iam.presentation.register

import android.util.Patterns
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.res.stringResource
import com.smartfinance.mobile.R
import com.smartfinance.mobile.core.ui.theme.PrimaryBlue
import com.smartfinance.mobile.core.ui.theme.SmartFinanceDriveTheme
import com.smartfinance.mobile.iam.domain.repository.AuthRepository
import kotlinx.coroutines.launch

private val PageBackground = Color(0xFFF4F6F9)
private val TextPrimary = Color(0xFF111827)
private val TextSecondary = Color(0xFF667085)
private val FieldBorder = Color(0xFFD0D5DD)
private val SecurityBlue = Color(0xFF1F3F98)
private val SecurityBlueLight = Color(0xFFEAF4FF)
private val ErrorBackground = Color(0xFFFFF1F0)
private val ErrorText = Color(0xFFB42318)

@Composable
fun RegisterScreen(
    authRepository: AuthRepository? = null,
    onRegisterSuccess: () -> Unit = {},
    onNavigateToLogin: () -> Unit = {}
) {
    var username by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var confirmVisible by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    val scope = rememberCoroutineScope()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(PageBackground)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 24.dp, vertical = 32.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(24.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(60.dp)
                        .background(SecurityBlue, RoundedCornerShape(16.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = stringResource(R.string.create_account_accessibility),
                        tint = Color.White,
                        modifier = Modifier.size(30.dp)
                    )
                }
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = stringResource(R.string.app_name),
                    color = SecurityBlue,
                    fontSize = 25.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = (-0.5).sp
                )
            }

            Spacer(modifier = Modifier.height(34.dp))

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White, RoundedCornerShape(24.dp))
                    .border(1.dp, Color(0xFFE4E7EC), RoundedCornerShape(24.dp))
                    .padding(horizontal = 24.dp, vertical = 28.dp)
            ) {
                Text(
                    text = stringResource(R.string.create_account),
                    color = TextPrimary,
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = stringResource(R.string.register_description),
                    color = TextSecondary,
                    fontSize = 16.sp,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(28.dp))
                FieldLabel(stringResource(R.string.username_label))
                OutlinedTextField(
                    value = username,
                    onValueChange = { username = it; errorMessage = null },
                    placeholder = { Text(stringResource(R.string.username_example), color = TextSecondary) },
                    leadingIcon = {
                        Icon(Icons.Default.Person, contentDescription = null, tint = Color(0xFF98A2B3))
                    },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = fieldColors()
                )

                Spacer(modifier = Modifier.height(18.dp))
                FieldLabel(stringResource(R.string.email_label))
                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it; errorMessage = null },
                    placeholder = { Text(stringResource(R.string.email_example), color = TextSecondary) },
                    leadingIcon = {
                        Icon(Icons.Default.Email, contentDescription = null, tint = Color(0xFF98A2B3))
                    },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = fieldColors()
                )

                Spacer(modifier = Modifier.height(18.dp))
                FieldLabel(stringResource(R.string.password))
                PasswordField(
                    value = password,
                    placeholder = stringResource(R.string.password_requirements),
                    visible = passwordVisible,
                    onValueChange = { password = it; errorMessage = null },
                    onToggleVisibility = { passwordVisible = !passwordVisible }
                )

                Spacer(modifier = Modifier.height(18.dp))
                FieldLabel(stringResource(R.string.confirm_password))
                PasswordField(
                    value = confirmPassword,
                    placeholder = stringResource(R.string.repeat_password),
                    visible = confirmVisible,
                    onValueChange = { confirmPassword = it; errorMessage = null },
                    onToggleVisibility = { confirmVisible = !confirmVisible }
                )

                if (errorMessage != null) {
                    Spacer(modifier = Modifier.height(14.dp))
                    Text(
                        text = errorMessage!!,
                        color = ErrorText,
                        fontSize = 13.sp,
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(ErrorBackground, RoundedCornerShape(10.dp))
                            .padding(12.dp)
                    )
                }

                val completeFieldsMessage = stringResource(R.string.complete_fields)
                val validEmailMessage = stringResource(R.string.valid_email)
                val passwordMinimumMessage = stringResource(R.string.password_minimum)
                val passwordUppercaseMessage = stringResource(R.string.password_uppercase)
                val passwordSpecialMessage = stringResource(R.string.password_special)
                val passwordMismatchMessage = stringResource(R.string.password_mismatch)
                val registrationUnavailableMessage = stringResource(R.string.registration_unavailable)
                val registrationFailedMessage = stringResource(R.string.registration_failed)

                Spacer(modifier = Modifier.height(24.dp))
                Button(
                    onClick = {
                        scope.launch {
                            when {
                                username.isBlank() || email.isBlank() || password.isBlank() ->
                                    errorMessage = completeFieldsMessage
                                !Patterns.EMAIL_ADDRESS.matcher(email.trim()).matches() ->
                                    errorMessage = validEmailMessage
                                password.length < 8 ->
                                    errorMessage = passwordMinimumMessage
                                password.none { it.isUpperCase() } ->
                                    errorMessage = passwordUppercaseMessage
                                password.none { !it.isLetterOrDigit() } ->
                                    errorMessage = passwordSpecialMessage
                                password != confirmPassword ->
                                    errorMessage = passwordMismatchMessage
                                else -> {
                                    isLoading = true
                                    val result = authRepository?.signUp(
                                        username.trim(),
                                        email.trim(),
                                        password
                                    )
                                    if (result == null) {
                                        errorMessage = registrationUnavailableMessage
                                    } else {
                                        result.onSuccess {
                                            onRegisterSuccess()
                                        }.onFailure {
                                            errorMessage = registrationFailedMessage
                                        }
                                    }
                                    isLoading = false
                                }
                            }
                        }
                    },
                    enabled = !isLoading,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = SecurityBlue,
                        disabledContainerColor = SecurityBlue.copy(alpha = 0.45f)
                    )
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(
                            color = Color.White,
                            strokeWidth = 2.dp,
                            modifier = Modifier.size(22.dp)
                        )
                    } else {
                        Text(stringResource(R.string.create_my_account), fontSize = 17.sp, fontWeight = FontWeight.Bold)
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(SecurityBlueLight, RoundedCornerShape(12.dp))
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.Key,
                        contentDescription = null,
                        tint = PrimaryBlue,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = stringResource(R.string.password_security),
                        color = Color(0xFF17517A),
                        fontSize = 12.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(stringResource(R.string.already_account) + " ", color = TextSecondary, fontSize = 15.sp)
                Text(
                    text = stringResource(R.string.sign_in),
                    color = SecurityBlue,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable { onNavigateToLogin() }
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
private fun FieldLabel(text: String) {
    Text(
        text = text,
        color = Color(0xFF344054),
        fontSize = 13.sp,
        fontWeight = FontWeight.Bold,
        letterSpacing = 0.5.sp,
        modifier = Modifier.padding(bottom = 8.dp)
    )
}

@Composable
private fun PasswordField(
    value: String,
    placeholder: String,
    visible: Boolean,
    onValueChange: (String) -> Unit,
    onToggleVisibility: () -> Unit
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        placeholder = { Text(placeholder, color = TextSecondary) },
        leadingIcon = {
            Icon(Icons.Default.Key, contentDescription = null, tint = Color(0xFF98A2B3))
        },
        trailingIcon = {
            IconButton(onClick = onToggleVisibility) {
                Icon(
                    imageVector = if (visible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                    contentDescription = if (visible) "Ocultar contraseña" else "Mostrar contraseña",
                    tint = Color(0xFF98A2B3)
                )
            }
        },
        visualTransformation = if (visible) VisualTransformation.None else PasswordVisualTransformation(),
        singleLine = true,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = fieldColors()
    )
}

@Composable
private fun fieldColors() = OutlinedTextFieldDefaults.colors(
    focusedBorderColor = SecurityBlue,
    unfocusedBorderColor = FieldBorder,
    focusedContainerColor = Color(0xFFFCFCFD),
    unfocusedContainerColor = Color(0xFFFCFCFD),
    focusedTextColor = TextPrimary,
    unfocusedTextColor = TextPrimary,
    cursorColor = SecurityBlue
)

@Preview(showBackground = true, widthDp = 390, heightDp = 844)
@Composable
fun RegisterScreenPreview() {
    SmartFinanceDriveTheme {
        RegisterScreen()
    }
}
