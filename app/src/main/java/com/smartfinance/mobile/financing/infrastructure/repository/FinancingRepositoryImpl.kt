package com.smartfinance.mobile.financing.infrastructure.repository

import com.smartfinance.mobile.financing.domain.model.CreditRequest
import com.smartfinance.mobile.financing.domain.model.CreditStatus
import com.smartfinance.mobile.financing.domain.model.PreEvaluation
import com.smartfinance.mobile.financing.domain.repository.FinancingRepository
import com.smartfinance.mobile.financing.infrastructure.remote.CreateSimulationResource
import com.smartfinance.mobile.financing.infrastructure.remote.FinancingApi
import com.smartfinance.mobile.core.storage.TokenStorage
import java.math.BigDecimal
import java.time.LocalDate

class FinancingRepositoryImpl(
    private val api: FinancingApi,
    private val tokenStorage: TokenStorage
) : FinancingRepository {

    override suspend fun getMyCreditRequests(): List<CreditRequest> {
        return try {
            val response = api.getSimulations(page = 0, size = 50)
            if (response.isSuccessful) {
                response.body()?.content?.map {
                    CreditRequest(
                        id = it.id,
                        vehicleTitle = it.title,
                        dealershipName = it.financialEntityId,
                        date = it.startDate ?: "",
                        status = CreditStatus.PENDING
                    )
                } ?: emptyList()
            } else {
                emptyList()
            }
        } catch (e: Exception) {
            emptyList()
        }
    }

    override suspend fun submitPreEvaluation(preEvaluation: PreEvaluation): Boolean {
        return try {
            val request = CreateSimulationResource(
                title = "Simulación ${preEvaluation.vehicleTitle}",
                userId = tokenStorage.getUserId()
                    ?: return false,
                vehicleId = preEvaluation.vehicleId,
                financialEntityId = preEvaluation.preferredBank,
                vehiclePriceAmount = BigDecimal(preEvaluation.vehiclePriceUsd),
                currency = "USD",
                downPaymentPercentage = BigDecimal(20.0), 
                balloonPaymentPercentage = BigDecimal(0.0), 
                annualEffectiveRate = BigDecimal(15.0), 
                loanTermMonths = 36, 
                gracePeriodType = "NONE",
                gracePeriodMonths = 0,
                startDate = LocalDate.now().toString()
            )
            val response = api.createSimulation(request)
            response.isSuccessful
        } catch (e: Exception) {
            false
        }
    }
}
