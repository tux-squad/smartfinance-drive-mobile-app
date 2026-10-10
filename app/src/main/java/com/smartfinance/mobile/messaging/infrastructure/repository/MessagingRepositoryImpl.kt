package com.smartfinance.mobile.messaging.infrastructure.repository

import com.smartfinance.mobile.messaging.domain.model.Conversation
import com.smartfinance.mobile.messaging.domain.repository.MessagingRepository
import java.util.UUID

class MessagingRepositoryImpl : MessagingRepository {

    private val conversations = mutableListOf(
        Conversation(
            id = "conv-1",
            dealershipName = "AutoPlaza Lima",
            vehicleTitle = "Toyota RAV4 2024",
            lastMessage = "Hola Carlos, confirmamos que recibimos tu solicitud de Test Drive. ¿Te esperamos este sábado?",
            timestamp = "Hace 5 min",
            unreadCount = 1,
            avatarType = "autoplaza"
        ),
        Conversation(
            id = "conv-2",
            dealershipName = "MegaMotors Santiago",
            vehicleTitle = "Honda Civic 2023",
            lastMessage = "Entendido, procedemos con la validación de tus requisitos con la financiera.",
            timestamp = "10:24 AM",
            unreadCount = 0,
            avatarType = "megamotors"
        ),
        Conversation(
            id = "conv-3",
            dealershipName = "EuroMotors San Isidro",
            vehicleTitle = "Volkswagen Tiguan 2024",
            lastMessage = "Tu cotización con tasa preferencial del 13.5% TEA está lista para revisión.",
            timestamp = "Ayer",
            unreadCount = 0,
            avatarType = "euromotors"
        ),
        Conversation(
            id = "conv-4",
            dealershipName = "DriveCenter Lima",
            vehicleTitle = "Mazda CX-5 2023",
            lastMessage = "¡Gracias por contactarnos! El vehículo cuenta con disponibilidad inmediata.",
            timestamp = "2 oct",
            unreadCount = 0,
            avatarType = "drivecenter"
        )
    )

    override suspend fun getConversations(): List<Conversation> {
        return conversations.toList()
    }

    override suspend fun sendMessage(conversationId: String, messageText: String): Boolean {
        val index = conversations.indexOfFirst { it.id == conversationId }
        if (index != -1) {
            val conv = conversations[index]
            conversations[index] = conv.copy(
                lastMessage = messageText,
                timestamp = "Ahora",
                unreadCount = 0
            )
            return true
        }
        return false
    }

    override suspend fun startConversation(
        dealershipName: String,
        vehicleTitle: String,
        initialMessage: String
    ): Conversation {
        val newConv = Conversation(
            id = UUID.randomUUID().toString(),
            dealershipName = dealershipName,
            vehicleTitle = vehicleTitle,
            lastMessage = initialMessage,
            timestamp = "Ahora",
            unreadCount = 0,
            avatarType = "default"
        )
        conversations.add(0, newConv)
        return newConv
    }
}
