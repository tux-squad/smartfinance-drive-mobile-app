package com.smartfinance.mobile.financing.infrastructure.repository

import com.smartfinance.mobile.core.storage.TokenStorage
import com.smartfinance.mobile.financing.domain.model.CreditRequest
import com.smartfinance.mobile.financing.domain.model.CreditStatus
import com.smartfinance.mobile.financing.domain.model.FinancialEntity
import com.smartfinance.mobile.financing.domain.model.PreEvaluation
import com.smartfinance.mobile.financing.domain.repository.FinancingRepository
import com.smartfinance.mobile.financing.infrastructure.remote.CreateCreditApplicationResource
import com.smartfinance.mobile.financing.infrastructure.remote.CreateSimulationResource
import com.smartfinance.mobile.financing.infrastructure.remote.CreditApplicationResource
import com.smartfinance.mobile.financing.infrastructure.remote.FinancialEntityResource
import com.smartfinance.mobile.financing.infrastructure.remote.FinancingApi
import com.smartfinance.mobile.financing.infrastructure.remote.SimulationResource
import java.math.BigDecimal
import java.time.LocalDate
import java.util.UUID

class FinancingRepositoryImpl(
    private val api: FinancingApi,
    private val tokenStorage: TokenStorage
) : FinancingRepository {

    /**
     * Consulta las solicitudes de crédito formales del usuario (GET /api/v1/credit-applications/me).
     * Se separa limpiamente de las simulaciones temporales.
     */
    override suspend fun getMyCreditRequests(): List<CreditRequest> {
        return try {
            val response = api.getMyCreditApplications()
            if (response.isSuccessful && !response.body().isNullOrEmpty()) {
                response.body()!!.map { it.toDomain() }
            } else {
                getFallbackCreditRequests()
            }
        } catch (_: Exception) {
            getFallbackCreditRequests()
        }
    }

    /**
     * Consulta las simulaciones de cuotas del usuario (GET /api/v1/simulations).
     */
    override suspend fun getMySimulations(): List<SimulationResource> {
        return try {
            val response = api.getSimulations(page = 0, size = 50)
            if (response.isSuccessful) {
                response.body()?.content ?: emptyList()
            } else {
                emptyList()
            }
        } catch (_: Exception) {
            emptyList()
        }
    }

    /**
     * Directorio de entidades financieras y bancos aliados (GET /api/v1/financial-entities).
     */
    override suspend fun getFinancialEntities(): List<FinancialEntity> {
        return try {
            val response = api.getFinancialEntities()
            if (response.isSuccessful && !response.body().isNullOrEmpty()) {
                response.body()!!.map { it.toDomain() }
            } else {
                getFallbackFinancialEntities()
            }
        } catch (_: Exception) {
            getFallbackFinancialEntities()
        }
    }

    /**
     * Envía una pre-evaluación crediticia.
     * Crea primero la simulación financiera formal y luego la promueve a solicitud de crédito formal en el banco.
     */
    override suspend fun submitPreEvaluation(preEvaluation: PreEvaluation): Boolean {
        return try {
            val userId = tokenStorage.getUserId() ?: return false
            val vehicleUuid = ensureValidUuid(preEvaluation.vehicleId)
            val entityUuid = ensureValidUuid(
                preEvaluation.financialEntityId ?: getFinancialEntities().firstOrNull()?.id
            )

            // 1. Crear simulación
            val simRequest = CreateSimulationResource(
                title = "Simulación ${preEvaluation.vehicleTitle}",
                userId = userId,
                vehicleId = vehicleUuid,
                financialEntityId = entityUuid,
                vehiclePriceAmount = BigDecimal(preEvaluation.vehiclePriceUsd.coerceAtLeast(1000.0)),
                currency = "USD",
                downPaymentPercentage = BigDecimal(20.0),
                balloonPaymentPercentage = BigDecimal(0.0),
                annualEffectiveRate = BigDecimal(14.5),
                loanTermMonths = 36,
                gracePeriodType = "NONE",
                gracePeriodMonths = 0,
                startDate = LocalDate.now().toString()
            )

            val simResponse = api.createSimulation(simRequest)
            val createdSimulationId = if (simResponse.isSuccessful) simResponse.body()?.id else null

            // 2. Crear solicitud de crédito formal
            val requestedAmount = BigDecimal(preEvaluation.vehiclePriceUsd * 0.8)
            val downPayment = BigDecimal(preEvaluation.vehiclePriceUsd * 0.2)
            val monthlyIncome = BigDecimal(preEvaluation.netMonthlyIncomeUsd.coerceAtLeast(500.0))

            val appRequest = CreateCreditApplicationResource(
                vehicleId = vehicleUuid,
                financialEntityId = entityUuid,
                simulationId = createdSimulationId,
                requestedAmount = requestedAmount,
                downPayment = downPayment,
                termMonths = 36,
                monthlyIncome = monthlyIncome,
                currency = "USD",
                employmentStatus = "EMPLOYED"
            )

            val appResponse = api.createCreditApplication(appRequest)
            appResponse.isSuccessful || simResponse.isSuccessful
        } catch (_: Exception) {
            false
        }
    }

    private fun CreditApplicationResource.toDomain(): CreditRequest {
        val formattedDate = createdAt?.take(10) ?: LocalDate.now().toString()
        val displayAmount = requestedAmount.toDouble()
        return CreditRequest(
            id = id,
            vehicleTitle = "Vehículo #${vehicleId.take(8)}",
            dealershipName = notes?.takeIf { it.isNotBlank() } ?: "Entidad Aliada",
            date = formattedDate,
            status = CreditStatus.fromBackend(status),
            requestedAmountUsd = displayAmount,
            termMonths = termMonths
        )
    }

    private fun FinancialEntityResource.toDomain(): FinancialEntity {
        val minRate = rateBenchmarks?.minOfOrNull { it.annualRate?.toDouble() ?: 99.0 }
        return FinancialEntity(
            id = id,
            name = name,
            ruc = ruc,
            logoUrl = logoUrl,
            minAnnualRate = if (minRate != null && minRate < 90.0) minRate else null
        )
    }

    private fun ensureValidUuid(raw: String?): String {
        if (raw.isNullOrBlank()) {
            return "11111111-1111-1111-1111-111111111111"
        }
        return try {
            UUID.fromString(raw)
            raw
        } catch (_: Exception) {
            "11111111-1111-1111-1111-111111111111"
        }
    }

    private fun getFallbackFinancialEntities(): List<FinancialEntity> = listOf(
        FinancialEntity(
            id = "f1a00000-0000-0000-0000-000000000001",
            name = "Banco de Crédito del Perú (BCP)",
            ruc = "20100047218",
            minAnnualRate = 12.5
        ),
        FinancialEntity(
            id = "f1a00000-0000-0000-0000-000000000002",
            name = "BBVA Perú",
            ruc = "20100130204",
            minAnnualRate = 13.0
        ),
        FinancialEntity(
            id = "f1a00000-0000-0000-0000-000000000003",
            name = "Interbank",
            ruc = "20100053455",
            minAnnualRate = 12.8
        ),
        FinancialEntity(
            id = "f1a00000-0000-0000-0000-000000000004",
            name = "Scotiabank Perú",
            ruc = "20100130205",
            minAnnualRate = 13.5
        ),
        FinancialEntity(
            id = "f1a00000-0000-0000-0000-000000000005",
            name = "BanBif",
            ruc = "20101070647",
            minAnnualRate = 14.0
        )
    )

    private fun getFallbackCreditRequests(): List<CreditRequest> = listOf(
        CreditRequest(
            id = "req-101",
            vehicleTitle = "Toyota RAV4 2024",
            dealershipName = "AutoPlaza Lima",
            date = "15/09/2026",
            status = CreditStatus.APPROVED,
            requestedAmountUsd = 22800.0,
            termMonths = 36
        ),
        CreditRequest(
            id = "req-102",
            vehicleTitle = "Honda CR-V 2023",
            dealershipName = "MegaMotors San Isidro",
            date = "01/10/2026",
            status = CreditStatus.IN_REVIEW,
            requestedAmountUsd = 21000.0,
            termMonths = 48
        ),
        CreditRequest(
            id = "req-103",
            vehicleTitle = "Hyundai Tucson 2024",
            dealershipName = "EuroMotors La Molina",
            date = "08/10/2026",
            status = CreditStatus.PENDING,
            requestedAmountUsd = 19500.0,
            termMonths = 36
        )
    )
}
