package com.smartfinance.mobile.messaging.infrastructure.remote

import java.math.BigDecimal

data class CreateAiConsultationResource(
    val prompt: String,
    val monthlyIncome: BigDecimal? = null,
    val maxBudget: BigDecimal? = null,
    val currency: String? = "USD"
)

data class AiConsultationResource(
    val id: String = "",
    val userId: String = "",
    val prompt: String = "",
    val monthlyIncome: BigDecimal? = null,
    val maxBudget: BigDecimal? = null,
    val currency: String? = "USD",
    val recommendationText: String = "",
    val recommendedVehicleCategory: String? = null,
    val estimatedMaxMonthlyFee: Double? = null,
    val createdAt: String? = null
)
