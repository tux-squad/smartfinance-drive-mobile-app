package com.smartfinance.mobile.financing.domain.model

enum class CreditStatus(val label: String) {
    APPROVED("Aprobado"),
    IN_REVIEW("En revisión"),
    REJECTED("Rechazado"),
    PENDING("Pendiente")
}

data class CreditRequest(
    val id: String,
    val vehicleTitle: String,
    val dealershipName: String,
    val date: String,
    val status: CreditStatus
)
