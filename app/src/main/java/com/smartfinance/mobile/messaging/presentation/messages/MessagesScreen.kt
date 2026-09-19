package com.smartfinance.mobile.messaging.presentation.messages

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
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.smartfinance.mobile.core.ui.theme.BorderSoft
import com.smartfinance.mobile.core.ui.theme.PrimaryBlue
import com.smartfinance.mobile.core.ui.theme.SmartFinanceDriveTheme
import com.smartfinance.mobile.core.ui.theme.TextPrimary
import com.smartfinance.mobile.core.ui.theme.TextSecondary
import com.smartfinance.mobile.messaging.domain.model.Conversation
import com.smartfinance.mobile.shared.ui.layouts.MobileShell

@Composable
fun MessagesScreen(
    onConversationClick: (Conversation) -> Unit = {}
) {
    var searchQuery by remember { mutableStateOf("") }

    val conversations = listOf(
        Conversation(
            id = "1",
            dealershipName = "AutoPlaza Lima",
            vehicleTitle = "Toyota Corolla 2023",
            lastMessage = "Hola Carlos, bienvenido a AutoPlaza Lim...",
            timestamp = "Hace 2 min",
            unreadCount = 2,
            avatarType = "autoplaza"
        ),
        Conversation(
            id = "2",
            dealershipName = "MegaMotors Santiago",
            vehicleTitle = "Honda Civic 2022",
            lastMessage = "Entendido, procedemos con la cotización...",
            timestamp = "10:24 AM",
            unreadCount = 0,
            avatarType = "megamotors"
        ),
        Conversation(
            id = "3",
            dealershipName = "DriveCenter Bogotá",
            vehicleTitle = "Mazda 3 2024",
            lastMessage = "El test drive quedó agendado para mañana...",
            timestamp = "Hoy",
            unreadCount = 0,
            avatarType = "drivecenter"
        ),
        Conversation(
            id = "4",
            dealershipName = "Honda Premium",
            vehicleTitle = "Honda CR-V 2023",
            lastMessage = "Tu crédito pre-aprobado es apto...",
            timestamp = "Hoy",
            unreadCount = 1,
            avatarType = "honda"
        ),
        Conversation(
            id = "5",
            dealershipName = "Toyota Oficial",
            vehicleTitle = "Toyota RAV4 2024",
            lastMessage = "Contamos con unidades físicas listas...",
            timestamp = "Hoy",
            unreadCount = 0,
            avatarType = "toyota"
        )
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF8FAFC))
            .padding(horizontal = 16.dp, vertical = 12.dp)
    ) {
        // Breadcrumb
        Text(
            text = "Panel de Comprador / Mensajes",
            color = TextSecondary,
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Search Conversation Field
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            placeholder = { Text("Buscar conversación...", color = TextSecondary, fontSize = 14.sp) },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = null,
                    tint = TextSecondary,
                    modifier = Modifier.size(20.dp)
                )
            },
            singleLine = true,
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
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

        // Conversations List Container Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, BorderSoft, RoundedCornerShape(14.dp)),
            shape = RoundedCornerShape(14.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
        ) {
            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                conversations.forEachIndexed { index, item ->
                    ConversationItemRow(
                        conversation = item,
                        onClick = { onConversationClick(item) }
                    )
                    if (index < conversations.size - 1) {
                        HorizontalDivider(
                            color = Color(0xFFF1F5F9),
                            thickness = 1.dp,
                            modifier = Modifier.padding(horizontal = 14.dp)
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun ConversationItemRow(
    conversation: Conversation,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(horizontal = 14.dp, vertical = 13.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        // Avatar Icon
        val (bgAvatarColor, iconTint, avatarIcon) = getAvatarStyle(conversation.avatarType)

        Box(
            modifier = Modifier
                .size(42.dp)
                .clip(CircleShape)
                .background(bgAvatarColor)
                .border(1.dp, BorderSoft, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = avatarIcon,
                contentDescription = null,
                tint = iconTint,
                modifier = Modifier.size(22.dp)
            )
        }

        Spacer(modifier = Modifier.width(12.dp))

        // Conversation Details
        Column(
            modifier = Modifier.weight(1f)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = conversation.dealershipName,
                    color = TextPrimary,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.Bold
                )

                Text(
                    text = conversation.timestamp,
                    color = TextSecondary,
                    fontSize = 11.sp
                )
            }

            Spacer(modifier = Modifier.height(2.dp))

            Text(
                text = conversation.vehicleTitle,
                color = Color(0xFF1E40AF),
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(2.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = conversation.lastMessage,
                    color = TextSecondary,
                    fontSize = 12.sp,
                    maxLines = 1,
                    modifier = Modifier.weight(1f)
                )

                if (conversation.unreadCount > 0) {
                    Spacer(modifier = Modifier.width(8.dp))
                    Box(
                        modifier = Modifier
                            .size(20.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF0D9488)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = conversation.unreadCount.toString(),
                            color = Color.White,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

private fun getAvatarStyle(avatarType: String): Triple<Color, Color, ImageVector> {
    return when (avatarType) {
        "autoplaza" -> Triple(Color(0xFFF8FAFC), Color(0xFF1E293B), Icons.Default.Storefront)
        "megamotors" -> Triple(Color(0xFF0B5AC2), Color.White, Icons.Default.DirectionsCar)
        "drivecenter" -> Triple(Color(0xFFEFF6FF), Color(0xFF2563EB), Icons.Default.Public)
        "honda" -> Triple(Color(0xFFF1F5F9), Color(0xFF475569), Icons.Default.Business)
        "toyota" -> Triple(Color(0xFFFEF2F2), Color(0xFFDC2626), Icons.Default.DirectionsCar)
        else -> Triple(Color(0xFFF1F5F9), Color(0xFF64748B), Icons.Default.Storefront)
    }
}

@Preview(showBackground = true, widthDp = 390, heightDp = 844)
@Composable
fun MessagesScreenPreview() {
    SmartFinanceDriveTheme {
        MobileShell(selectedNavIndex = 2) {
            MessagesScreen()
        }
    }
}
