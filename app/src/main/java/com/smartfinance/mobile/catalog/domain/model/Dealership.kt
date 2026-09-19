package com.smartfinance.mobile.catalog.domain.model

data class Dealership(
    val id: String,
    val name: String,
    val address: String,
    val city: String,
    val isVerified: Boolean = true,
    val imageUrl: String? = null
)
