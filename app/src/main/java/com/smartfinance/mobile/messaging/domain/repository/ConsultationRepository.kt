package com.smartfinance.mobile.messaging.domain.repository

import com.smartfinance.mobile.messaging.domain.model.ChatMessage

interface ConsultationRepository {
    suspend fun askAssistant(prompt: String): ChatMessage?
    suspend fun getHistory(): List<ChatMessage>
}
