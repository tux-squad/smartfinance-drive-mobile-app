package com.smartfinance.mobile.catalog.infrastructure.remote

import com.google.gson.annotations.SerializedName
import java.math.BigDecimal

data class CreateVehicleResource(
    val financialEntityId: String,
    val brand: String,
    val model: String,
    val manufactureYear: Int,
    val condition: String,
    val priceAmount: BigDecimal,
    val currency: String = "USD",
    val imagePath: String? = null
)

data class UpdateVehicleResource(
    val financialEntityId: String,
    val brand: String,
    val model: String,
    val manufactureYear: Int,
    val condition: String,
    val priceAmount: BigDecimal,
    val currency: String = "USD",
    val imagePath: String? = null
)

data class VehicleResource(
    @SerializedName("id")
    val id: String = "",
    @SerializedName("userId")
    val userId: String? = null,
    @SerializedName("financialEntityId")
    val financialEntityId: String? = null,
    @SerializedName("dealershipId")
    val dealershipId: String? = null,
    @SerializedName("brand")
    val brand: String = "",
    @SerializedName("model")
    val model: String = "",
    @SerializedName("manufactureYear")
    val manufactureYear: Int = 2024,
    @SerializedName("condition")
    val condition: String? = "NEW",
    @SerializedName("priceAmount")
    val priceAmount: BigDecimal = BigDecimal.ZERO,
    @SerializedName("currency")
    val currency: String? = "USD",
    @SerializedName("imagePath")
    val imagePath: String? = null,
    @SerializedName("status")
    val status: String? = "ACTIVE"
)

data class PageResponse<T>(
    val content: List<T> = emptyList(),
    val number: Int = 0,
    val size: Int = 20,
    val totalElements: Long = 0,
    val totalPages: Int = 0,
    val first: Boolean = true,
    val last: Boolean = true,
    val numberOfElements: Int = 0,
    val empty: Boolean = false
)

data class DealershipResource(
    val id: String = "",
    val userId: String? = null,
    val ruc: String? = null,
    val name: String = "",
    val address: String? = null,
    val phone: String? = null,
    val email: String? = null,
    val website: String? = null,
    val description: String? = null,
    val operatingHours: String? = null,
    val rating: Double? = 4.8,
    val logoUrl: String? = null,
    val bannerUrl: String? = null,
    val active: Boolean = true
)
