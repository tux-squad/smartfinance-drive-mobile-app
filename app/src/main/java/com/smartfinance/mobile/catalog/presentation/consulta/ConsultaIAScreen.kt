package com.smartfinance.mobile.catalog.presentation.consulta

import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.smartfinance.mobile.core.ui.theme.BorderSoft
import com.smartfinance.mobile.core.ui.theme.PrimaryBlue
import com.smartfinance.mobile.core.ui.theme.SmartFinanceDriveTheme
import com.smartfinance.mobile.core.ui.theme.TextPrimary
import com.smartfinance.mobile.core.ui.theme.TextSecondary
import com.smartfinance.mobile.messaging.domain.model.ChatMessage
import com.smartfinance.mobile.messaging.domain.repository.ConsultationRepository
import com.smartfinance.mobile.shared.ui.layouts.MobileShell
import kotlinx.coroutines.launch
import java.util.UUID

@Composable
fun ConsultaIAScreen(
    consultationRepository: ConsultationRepository? = null
) {
    var messages by remember {
        mutableStateOf(
            listOf(
                ChatMessage(
                    id = "init-1",
                    text = "¡Hola! Soy tu Asesor Financiero con Inteligencia Artificial de SmartFinance Drive. ¿Cuál es tu presupuesto mensual o qué auto estás buscando?",
                    isFromUser = false
                )
            )
        )
    }
    var inputText by remember { mutableStateOf("") }
    var isThinking by remember { mutableStateOf(false) }
    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()

    val quickQuestions = listOf(
        "¿Qué auto me alcanza con $400/mes?",
        "¿Cuáles son los requisitos de crédito?",
        "¿Cómo funciona el scoring crediticio?"
    )

    LaunchedEffect(consultationRepository) {
        if (consultationRepository != null) {
            val history = consultationRepository.getHistory()
            if (history.isNotEmpty()) {
                messages = history
            }
        }
    }

    LaunchedEffect(messages.size) {
        if (messages.isNotEmpty()) {
            listState.animateScrollToItem(messages.size - 1)
        }
    }

    fun sendMessage(text: String) {
        if (text.isBlank() || isThinking) return
        val userMsg = ChatMessage(
            id = UUID.randomUUID().toString(),
            text = text.trim(),
            isFromUser = true
        )
        messages = messages + userMsg
        inputText = ""

        scope.launch {
            isThinking = true
            val reply = consultationRepository?.askAssistant(text.trim()) ?: ChatMessage(
                id = UUID.randomUUID().toString(),
                text = "He analizado tu consulta. Según tu perfil financiero, calificas a financiamiento automotriz con cuotas competitivas en nuestros bancos aliados.",
                isFromUser = false
            )
            messages = messages + reply
            isThinking = false
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF4F7FB))
    ) {
        // Encabezado
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(horizontal = 18.dp, vertical = 12.dp)
        ) {
            Text(
                text = "Panel de Comprador / Asistente IA",
                color = TextSecondary,
                fontSize = 13.sp
            )

            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(top = 4.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.AutoAwesome,
                    contentDescription = null,
                    tint = PrimaryBlue,
                    modifier = Modifier.size(22.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = "Asistente Financiero IA",
                    color = TextPrimary,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Text(
                text = "Consulta cuotas, requisitos y recomendaciones en tiempo real.",
                color = TextSecondary,
                fontSize = 13.sp,
                modifier = Modifier.padding(top = 2.dp)
            )
        }

        // Historial de Mensajes
        LazyColumn(
            state = listState,
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(messages, key = { it.id }) { msg ->
                ChatBubble(
                    message = msg.text,
                    isFromUser = msg.isFromUser,
                    category = msg.category,
                    estimatedFee = msg.estimatedFee
                )
            }

            if (isThinking) {
                item {
                    Row(
                        modifier = Modifier.padding(top = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(16.dp),
                            strokeWidth = 2.dp,
                            color = PrimaryBlue
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "SmartFinance IA está analizando...",
                            color = TextSecondary,
                            fontSize = 12.sp
                        )
                    }
                }
            }
        }

        // Preguntas sugeridas
        LazyRow(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 4.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(quickQuestions) { question ->
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = Color.White,
                    border = androidx.compose.foundation.BorderStroke(1.dp, BorderSoft),
                    modifier = Modifier.clickable { sendMessage(question) }
                ) {
                    Text(
                        text = question,
                        color = PrimaryBlue,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                    )
                }
            }
        }

        // Barra inferior de envío
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White)
                .padding(horizontal = 16.dp, vertical = 10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = inputText,
                onValueChange = { inputText = it },
                placeholder = {
                    Text("Escribe tu consulta financiera...", color = TextSecondary, fontSize = 14.sp)
                },
                singleLine = true,
                modifier = Modifier.weight(1f),
                shape = RoundedCornerShape(20.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = PrimaryBlue,
                    unfocusedBorderColor = BorderSoft,
                    focusedContainerColor = Color(0xFFF8FAFC),
                    unfocusedContainerColor = Color(0xFFF8FAFC),
                    cursorColor = PrimaryBlue
                )
            )

            Spacer(modifier = Modifier.width(8.dp))

            IconButton(
                onClick = { sendMessage(inputText) },
                enabled = inputText.isNotBlank() && !isThinking,
                modifier = Modifier
                    .size(46.dp)
                    .background(if (inputText.isNotBlank()) PrimaryBlue else Color(0xFFCBD5E1), CircleShape)
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowForward,
                    contentDescription = "Enviar",
                    tint = Color.White,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

@Composable
private fun ChatBubble(
    message: String,
    isFromUser: Boolean,
    category: String? = null,
    estimatedFee: Double? = null
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = if (isFromUser) Alignment.End else Alignment.Start
    ) {
        Box(
            modifier = Modifier
                .padding(horizontal = 4.dp)
                .background(
                    color = if (isFromUser) PrimaryBlue else Color.White,
                    shape = RoundedCornerShape(
                        topStart = 16.dp,
                        topEnd = 16.dp,
                        bottomStart = if (isFromUser) 16.dp else 4.dp,
                        bottomEnd = if (isFromUser) 4.dp else 16.dp
                    )
                )
                .padding(horizontal = 14.dp, vertical = 10.dp)
        ) {
            Column {
                Text(
                    text = message,
                    color = if (isFromUser) Color.White else TextPrimary,
                    fontSize = 14.sp,
                    lineHeight = 20.sp
                )

                if (category != null || estimatedFee != null) {
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        if (category != null) {
                            Text(
                                text = "Categoría: $category",
                                color = if (isFromUser) Color.White.copy(alpha = 0.8f) else Color(0xFF059669),
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                        if (estimatedFee != null) {
                            Text(
                                text = "Cuota est: $$estimatedFee USD",
                                color = if (isFromUser) Color.White.copy(alpha = 0.8f) else PrimaryBlue,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true, widthDp = 390, heightDp = 844)
@Composable
fun ConsultaIAScreenPreview() {
    SmartFinanceDriveTheme {
        MobileShell(selectedNavIndex = 3) {
            ConsultaIAScreen()
        }
    }
}
