package com.smartfinance.mobile.catalog.domain.model

data class Dealership(
    val id: String,
    val name: String,
    val address: String,
    val city: String = "Lima",
    val phone: String? = null,
    val email: String? = null,
    val rating: Double = 4.8,
    val isVerified: Boolean = true,
    val imageUrl: String? = null,
    val description: String? = null,
    val operatingHours: String? = null
)
