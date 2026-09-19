package com.smartfinance.mobile.messaging.domain.model

data class Conversation(
    val id: String,
    val dealershipName: String,
    val vehicleTitle: String,
    val lastMessage: String,
    val timestamp: String,
    val unreadCount: Int = 0,
    val avatarType: String = "default"
)
