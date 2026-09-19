package com.smartfinance.mobile.financing.domain.model

data class PreEvaluation(
    val vehicleId: String,
    val vehicleTitle: String = "Toyota RAV4 2024",
    val vehiclePriceUsd: Double = 28500.0,
    val dealershipName: String = "EuroMotors",
    val documentNumber: String,
    val netMonthlyIncomeUsd: Double,
    val preferredBank: String,
    val consentAccepted: Boolean = true
)
