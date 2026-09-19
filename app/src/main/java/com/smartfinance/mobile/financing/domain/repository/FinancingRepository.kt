package com.smartfinance.mobile.financing.domain.repository

import com.smartfinance.mobile.financing.domain.model.CreditRequest
import com.smartfinance.mobile.financing.domain.model.PreEvaluation

/** Operaciones de simulacion y consulta de solicitudes de financiamiento. */
interface FinancingRepository {
    suspend fun getMyCreditRequests(): List<CreditRequest>
    suspend fun submitPreEvaluation(preEvaluation: PreEvaluation): Boolean
}
