package com.smartfinance.mobile.catalog.domain.model

data class Vehicle(
    val id: String,
    val brand: String,
    val model: String,
    val year: Int,
    val priceUsd: Double,
    val city: String,
    val kilometers: Int,
    val imageUrl: String
)
