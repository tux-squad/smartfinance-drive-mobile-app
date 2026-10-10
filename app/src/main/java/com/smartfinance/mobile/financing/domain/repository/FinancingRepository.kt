package com.smartfinance.mobile.financing.domain.repository

import com.smartfinance.mobile.financing.domain.model.CreditRequest
import com.smartfinance.mobile.financing.domain.model.FinancialEntity
import com.smartfinance.mobile.financing.domain.model.PreEvaluation
import com.smartfinance.mobile.financing.infrastructure.remote.SimulationResource

/** Operaciones de simulaciones, entidades financieras y solicitudes de crédito formales. */
interface FinancingRepository {
    /** Consulta las solicitudes de crédito formales del usuario (GET /api/v1/credit-applications/me). */
    suspend fun getMyCreditRequests(): List<CreditRequest>

    /** Consulta las simulaciones guardadas del usuario (GET /api/v1/simulations). */
    suspend fun getMySimulations(): List<SimulationResource>

    /** Consulta el directorio de entidades financieras y bancos aliados (GET /api/v1/financial-entities). */
    suspend fun getFinancialEntities(): List<FinancialEntity>

    /** Envía una pre-evaluación crediticia creando una simulación y solicitud formal. */
    suspend fun submitPreEvaluation(preEvaluation: PreEvaluation): Boolean
}
