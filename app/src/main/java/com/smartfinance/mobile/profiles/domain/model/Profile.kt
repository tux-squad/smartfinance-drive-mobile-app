package com.smartfinance.mobile.profiles.domain.model

data class Profile(
    val id: String,
    val userId: String,
    val fullName: String,
    val email: String,
    val phone: String? = null,
    val documentNumber: String? = null,
    val monthlyIncome: Double? = null
)
