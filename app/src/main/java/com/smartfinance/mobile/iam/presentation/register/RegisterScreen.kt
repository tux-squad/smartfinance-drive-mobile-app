package com.smartfinance.mobile.iam.presentation.register

import android.util.Patterns
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.smartfinance.mobile.core.ui.theme.AccentOrange
import com.smartfinance.mobile.core.ui.theme.SmartFinanceDriveTheme
import kotlinx.coroutines.launch
import kotlinx.coroutines.delay
import com.smartfinance.mobile.iam.domain.repository.AuthRepository

private val NavyDark    = Color(0xFF071829)
private val Navy        = Color(0xFF0A2540)
private val NavyMid     = Color(0xFF0F3460)
private val TealAcc     = Color(0xFF14B8A6)
private val TextWhite   = Color(0xFFF1F5F9)
private val TextMuted   = Color(0xFF94A3B8)
private val ErrorRed    = Color(0xFFEF4444)
private val GlassWhite  = Color(0x1AFFFFFF)
private val GlassBorder = Color(0x33FFFFFF)

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
    var showContent by remember { mutableStateOf(false) }
    val scope = rememberCoroutineScope()

    LaunchedEffect(Unit) {
        delay(100)
        showContent = true
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(NavyDark, Navy, NavyMid)
                )
            )
    ) {
        // Decorative circles
        Box(
            modifier = Modifier
                .size(220.dp)
                .align(Alignment.TopEnd)
                .background(
                    Brush.radialGradient(
                        colors = listOf(AccentOrange.copy(alpha = 0.12f), Color.Transparent)
                    ),
                    CircleShape
                )
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 28.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(56.dp))

            // ── Brand Header ──────────────────────────────────────────────
            AnimatedVisibility(
                visible = showContent,
                enter = fadeIn() + slideInVertically(initialOffsetY = { -30 })
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Box(
                        modifier = Modifier
                            .size(72.dp)
                            .background(GlassWhite, CircleShape)
                            .border(1.5.dp, GlassBorder, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "SF",
                            color = Color.White,
                            fontSize = 22.sp,
                            fontWeight = FontWeight.ExtraBold
                        )
                    }
                    Spacer(modifier = Modifier.height(14.dp))
                    Text(
                        text = "Crear cuenta",
                        color = TextWhite,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Regístrate como comprador",
                        color = TextMuted,
                        fontSize = 13.sp,
                        textAlign = TextAlign.Center
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // ── Register Glass Card ───────────────────────────────────────
            AnimatedVisibility(
                visible = showContent,
                enter = fadeIn() + slideInVertically(initialOffsetY = { 50 })
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(GlassWhite, RoundedCornerShape(24.dp))
                        .border(1.dp, GlassBorder, RoundedCornerShape(24.dp))
                        .padding(24.dp)
                ) {
                    Column {
                        // Username
                        FieldLabel("Nombre de usuario")
                        OutlinedTextField(
                            value = username,
                            onValueChange = { username = it; errorMessage = null },
                            placeholder = { Text("Ej: carlos.morales", color = TextMuted.copy(alpha = 0.5f), fontSize = 14.sp) },
                            leadingIcon = { Icon(Icons.Default.Person, null, tint = TealAcc, modifier = Modifier.size(20.dp)) },
                            singleLine = true,
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            colors = fieldColors()
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        // Email
                        FieldLabel("Correo electrónico")
                        OutlinedTextField(
                            value = email,
                            onValueChange = { email = it; errorMessage = null },
                            placeholder = { Text("correo@ejemplo.com", color = TextMuted.copy(alpha = 0.5f), fontSize = 14.sp) },
                            leadingIcon = { Icon(Icons.Default.Email, null, tint = TealAcc, modifier = Modifier.size(20.dp)) },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            colors = fieldColors()
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        // Password
                        FieldLabel("Contraseña")
                        OutlinedTextField(
                            value = password,
                            onValueChange = { password = it; errorMessage = null },
                            placeholder = { Text("8+ caracteres, mayúscula y símbolo", color = TextMuted.copy(alpha = 0.5f), fontSize = 14.sp) },
                            leadingIcon = { Icon(Icons.Default.Lock, null, tint = TealAcc, modifier = Modifier.size(20.dp)) },
                            trailingIcon = {
                                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                    Icon(
                                        imageVector = if (passwordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                        contentDescription = null,
                                        tint = TextMuted,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                            },
                            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            colors = fieldColors()
                        )

                        Spacer(modifier = Modifier.height(14.dp))

                        // Confirm Password
                        FieldLabel("Confirmar contraseña")
                        OutlinedTextField(
                            value = confirmPassword,
                            onValueChange = { confirmPassword = it; errorMessage = null },
                            placeholder = { Text("Repite tu contraseña", color = TextMuted.copy(alpha = 0.5f), fontSize = 14.sp) },
                            leadingIcon = { Icon(Icons.Default.Lock, null, tint = TealAcc, modifier = Modifier.size(20.dp)) },
                            trailingIcon = {
                                IconButton(onClick = { confirmVisible = !confirmVisible }) {
                                    Icon(
                                        imageVector = if (confirmVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                        contentDescription = null,
                                        tint = TextMuted,
                                        modifier = Modifier.size(20.dp)
                                    )
                                }
                            },
                            visualTransformation = if (confirmVisible) VisualTransformation.None else PasswordVisualTransformation(),
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            colors = fieldColors()
                        )

                        // Error
                        if (errorMessage != null) {
                            Spacer(modifier = Modifier.height(12.dp))
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .background(ErrorRed.copy(alpha = 0.15f), RoundedCornerShape(10.dp))
                                    .border(1.dp, ErrorRed.copy(alpha = 0.4f), RoundedCornerShape(10.dp))
                                    .padding(horizontal = 14.dp, vertical = 10.dp)
                            ) {
                                Text(text = errorMessage!!, color = ErrorRed, fontSize = 13.sp)
                            }
                        }

                        Spacer(modifier = Modifier.height(22.dp))

                        // Register Button
                        Button(
                            onClick = {
                                scope.launch {
                                    when {
                                        username.isBlank() || email.isBlank() || password.isBlank() ->
                                            errorMessage = "Por favor completa todos los campos."
                                        !Patterns.EMAIL_ADDRESS.matcher(email.trim()).matches() ->
                                            errorMessage = "Ingresa un correo electrónico válido. El backend usa el correo como usuario."
                                        password.length < 8 ->
                                            errorMessage = "La contraseña debe tener al menos 8 caracteres."
                                        password.none { it.isUpperCase() } ->
                                            errorMessage = "La contraseña debe incluir al menos una letra mayúscula."
                                        password.none { !it.isLetterOrDigit() } ->
                                            errorMessage = "La contraseña debe incluir al menos un carácter especial, por ejemplo: !, @ o #."
                                        password != confirmPassword ->
                                            errorMessage = "Las contraseñas no coinciden."
                                        else -> {
                                            isLoading = true
                                            if (authRepository == null) {
                                                errorMessage = "El servicio de registro no está disponible."
                                            } else {
                                                val result = authRepository.signUp(
                                                    username.trim(),
                                                    email.trim(),
                                                    password
                                                )
                                                result.onSuccess {
                                                    onRegisterSuccess()
                                                }.onFailure { error ->
                                                    errorMessage = error.message ?: "No se pudo crear la cuenta."
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
                                .height(52.dp),
                            shape = RoundedCornerShape(14.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = AccentOrange,
                                disabledContainerColor = AccentOrange.copy(alpha = 0.4f)
                            )
                        ) {
                            if (isLoading) {
                                CircularProgressIndicator(color = Color.White, strokeWidth = 2.dp, modifier = Modifier.size(22.dp))
                            } else {
                                Text("Crear mi cuenta", color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // ── Back to Login ─────────────────────────────────────────────
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text("¿Ya tienes cuenta? ", color = TextMuted, fontSize = 14.sp)
                Text(
                    text = "Inicia sesión",
                    color = AccentOrange,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.clickable { onNavigateToLogin() }
                )
            }

            Spacer(modifier = Modifier.height(36.dp))
        }
    }
}

@Composable
private fun FieldLabel(text: String) {
    Text(
        text = text,
        color = Color(0xFF94A3B8),
        fontSize = 12.sp,
        fontWeight = FontWeight.Medium,
        modifier = Modifier.padding(bottom = 6.dp)
    )
}

@Composable
private fun fieldColors() = OutlinedTextFieldDefaults.colors(
    focusedBorderColor = Color(0xFF14B8A6),
    unfocusedBorderColor = Color(0x33FFFFFF),
    focusedContainerColor = Color(0x1AFFFFFF),
    unfocusedContainerColor = Color(0x0DFFFFFF),
    focusedTextColor = Color(0xFFF1F5F9),
    unfocusedTextColor = Color(0xFFF1F5F9),
    cursorColor = Color(0xFF14B8A6)
)

@Preview(showBackground = true, widthDp = 390, heightDp = 844)
@Composable
fun RegisterScreenPreview() {
    SmartFinanceDriveTheme {
        RegisterScreen()
    }
}
