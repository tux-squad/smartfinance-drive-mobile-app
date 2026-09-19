package com.smartfinance.mobile.catalog.domain.model

data class VehicleComparisonItem(
    val id: String,
    val title: String,
    val priceText: String,
    val condition: String,
    val mileage: String,
    val estimatedMonthlyQuota: String,
    val imageUrl: String
)
