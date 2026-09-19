package com.smartfinance.mobile.messaging.domain.repository

import com.smartfinance.mobile.messaging.domain.model.Conversation

interface MessagingRepository {
    suspend fun getConversations(): List<Conversation>
}
