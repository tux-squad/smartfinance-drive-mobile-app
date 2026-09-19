package com.smartfinance.mobile.catalog.infrastructure.remote

import java.math.BigDecimal

data class CreateVehicleResource(
    val userId: String,
    val financialEntityId: String,
    val brand: String,
    val model: String,
    val manufactureYear: Int,
    val condition: String,
    val priceAmount: BigDecimal,
    val currency: String,
    val imagePath: String?
)

data class UpdateVehicleResource(
    val financialEntityId: String,
    val brand: String,
    val model: String,
    val manufactureYear: Int,
    val condition: String,
    val priceAmount: BigDecimal,
    val currency: String,
    val imagePath: String?
)

data class VehicleResource(
    val id: String,
    val userId: String,
    val financialEntityId: String,
    val brand: String,
    val model: String,
    val manufactureYear: Int,
    val condition: String,
    val priceAmount: BigDecimal,
    val currency: String,
    val imagePath: String?
)

data class PageResponse<T>(
    val content: List<T>,
    val number: Int,
    val size: Int,
    val totalElements: Long,
    val totalPages: Int,
    val first: Boolean,
    val last: Boolean,
    val numberOfElements: Int,
    val empty: Boolean
)
