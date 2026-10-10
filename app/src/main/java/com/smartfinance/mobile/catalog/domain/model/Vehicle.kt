package com.smartfinance.mobile.catalog.domain.model

data class Vehicle(
    val id: String,
    val brand: String,
    val model: String,
    val year: Int,
    val priceUsd: Double,
    val city: String = "Lima",
    val kilometers: Int = 0,
    val imageUrl: String = "",
    val condition: String = "NEW",
    val financialEntityId: String = "",
    val dealershipId: String? = null
)
