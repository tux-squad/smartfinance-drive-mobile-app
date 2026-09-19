package com.smartfinance.mobile.messaging.presentation.messages.dealer

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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.smartfinance.mobile.core.ui.theme.AccentOrange

@Composable
fun DealershipMessagesScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // Cabecera
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 16.dp)
        ) {
            Text(
                text = "Panel Concesionaria / Mensajes",
                color = Color(0xFF64748B),
                fontSize = 12.sp
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "Bandeja de Entrada",
                color = Color(0xFF0F203C),
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Responde consultas de clientes y coordina aprobaciones.",
                color = Color(0xFF64748B),
                fontSize = 14.sp
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Lista de Chats
        val dummyChats = listOf(
            DealerChatPreview(
                id = "1",
                name = "María López",
                initials = "ML",
                lastMessage = "Perfecto, me queda bien a las 4 PM...",
                time = "", // Has badge instead
                badge = "Crédito Aprobado",
                isActive = true,
                avatarColor = Color(0xFFFDBA74) // Light orange
            ),
            DealerChatPreview(
                id = "2",
                name = "Carlos Ruiz",
                initials = "CR",
                lastMessage = "¿Tienen stock disponible en color azul?",
                time = "10:24 AM",
                badge = null,
                isActive = false,
                avatarColor = Color(0xFF1E3A8A) // Dark blue
            ),
            DealerChatPreview(
                id = "3",
                name = "Ana Martínez",
                initials = "AM",
                lastMessage = "Gracias por la cotización enviada.",
                time = "Ayer",
                badge = null,
                isActive = false,
                avatarColor = Color(0xFF1E3A8A)
            )
        )

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 20.dp, vertical = 8.dp)
        ) {
            items(dummyChats) { chat ->
                DealerChatCard(chat = chat)
                Spacer(modifier = Modifier.height(12.dp))
            }
        }
    }
}

data class DealerChatPreview(
    val id: String,
    val name: String,
    val initials: String,
    val lastMessage: String,
    val time: String,
    val badge: String?,
    val isActive: Boolean,
    val avatarColor: Color
)

@Composable
private fun DealerChatCard(chat: DealerChatPreview) {
    val borderColor = if (chat.isActive) AccentOrange else Color(0xFFE2E8F0)
    val bgColor = if (chat.isActive) Color.White else Color(0xFFF8FAFC)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(bgColor)
            .border(if (chat.isActive) 1.5.dp else 1.dp, borderColor, RoundedCornerShape(12.dp))
            .padding(16.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            // Avatar
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(chat.avatarColor),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = chat.initials,
                    color = Color.White,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Info
            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = chat.name,
                        color = Color(0xFF0F203C),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                    
                    if (chat.badge != null) {
                        Box(
                            modifier = Modifier
                                .background(Color(0xFFD1FAE5), RoundedCornerShape(12.dp))
                                .padding(horizontal = 8.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = chat.badge,
                                color = Color(0xFF059669),
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    } else if (chat.time.isNotEmpty()) {
                        Text(
                            text = chat.time,
                            color = Color(0xFF94A3B8),
                            fontSize = 12.sp
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(4.dp))
                
                Text(
                    text = chat.lastMessage,
                    color = Color(0xFF64748B),
                    fontSize = 14.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}
