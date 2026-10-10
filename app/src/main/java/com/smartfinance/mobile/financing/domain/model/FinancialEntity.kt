package com.smartfinance.mobile.financing.domain.model

data class FinancialEntity(
    val id: String,
    val name: String,
    val ruc: String? = null,
    val logoUrl: String? = null,
    val minAnnualRate: Double? = null
)
