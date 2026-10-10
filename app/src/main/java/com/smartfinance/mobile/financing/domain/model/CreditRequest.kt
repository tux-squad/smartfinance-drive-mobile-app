package com.smartfinance.mobile.financing.domain.model

enum class CreditStatus(val label: String) {
    PENDING("Pendiente"),
    IN_REVIEW("En revisión"),
    PRE_APPROVED("Pre-aprobado"),
    APPROVED("Aprobado"),
    REJECTED("Rechazado"),
    DISBURSED("Desembolsado");

    companion object {
        fun fromBackend(status: String?): CreditStatus {
            return when (status?.trim()?.uppercase()) {
                "APPROVED" -> APPROVED
                "IN_REVIEW", "UNDER_REVIEW" -> IN_REVIEW
                "PRE_APPROVED" -> PRE_APPROVED
                "REJECTED" -> REJECTED
                "DISBURSED" -> DISBURSED
                else -> PENDING
            }
        }
    }
}

data class CreditRequest(
    val id: String,
    val vehicleTitle: String,
    val dealershipName: String,
    val date: String,
    val status: CreditStatus,
    val requestedAmountUsd: Double = 0.0,
    val termMonths: Int = 36
)
