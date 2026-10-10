package com.smartfinance.mobile.iam.domain.model

data class User(
    val id: String = "",
    val username: String = "",
    val email: String = "",
    val roles: List<String> = listOf("ROLE_USER"),
    val accessToken: String = "",
    val refreshToken: String = ""
) {
    /** Retorna el rol principal o ROLE_USER para compatibilidad con código existente. */
    val role: String
        get() = roles.firstOrNull() ?: "ROLE_USER"

    val isDealer: Boolean
        get() = roles.any { it.contains("DEALER", ignoreCase = true) }

    val isFinancialInstitution: Boolean
        get() = roles.any { it.contains("FINANCIAL_INSTITUTION", ignoreCase = true) }

    val isFinancialAnalyst: Boolean
        get() = roles.any { it.contains("FINANCIAL_ANALYST", ignoreCase = true) }

    val isAdmin: Boolean
        get() = roles.any { it.contains("ADMIN", ignoreCase = true) }

    val isBuyer: Boolean
        get() = roles.isEmpty() || roles.any { it.contains("USER", ignoreCase = true) }
}
