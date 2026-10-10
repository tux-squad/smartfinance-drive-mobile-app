package com.smartfinance.mobile.messaging.domain.model

data class ChatMessage(
    val id: String,
    val text: String,
    val isFromUser: Boolean,
    val timestamp: String = "",
    val category: String? = null,
    val estimatedFee: Double? = null
)
