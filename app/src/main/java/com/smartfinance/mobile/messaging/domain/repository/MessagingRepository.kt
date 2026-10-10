package com.smartfinance.mobile.messaging.domain.repository

import com.smartfinance.mobile.messaging.domain.model.Conversation

interface MessagingRepository {
    suspend fun getConversations(): List<Conversation>
    suspend fun sendMessage(conversationId: String, messageText: String): Boolean
    suspend fun startConversation(dealershipName: String, vehicleTitle: String, initialMessage: String): Conversation
}
