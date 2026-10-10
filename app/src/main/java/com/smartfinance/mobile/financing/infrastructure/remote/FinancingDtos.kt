package com.smartfinance.mobile.financing.infrastructure.remote

import java.math.BigDecimal

data class CreateSimulationResource(
    val title: String,
    val userId: String,
    val vehicleId: String,
    val financialEntityId: String,
    val vehiclePriceAmount: BigDecimal,
    val currency: String,
    val downPaymentPercentage: BigDecimal,
    val balloonPaymentPercentage: BigDecimal,
    val annualEffectiveRate: BigDecimal,
    val monthlyCreditLifeInsuranceRate: BigDecimal? = null,
    val vehicleInsuranceFeeAmount: BigDecimal? = null,
    val vehicleInsuranceType: String? = null,
    val loanTermMonths: Int,
    val gracePeriodType: String,
    val gracePeriodMonths: Int,
    val initialFeesAmount: BigDecimal? = null,
    val discountRate: BigDecimal? = null,
    val startDate: String? = null // yyyy-MM-dd
)

data class FinancialMetricsResource(
    val financedAmount: BigDecimal,
    val downPaymentAmount: BigDecimal,
    val balloonPaymentAmount: BigDecimal,
    val tcea: BigDecimal,
    val tir: BigDecimal,
    val van: BigDecimal,
    val totalInterest: BigDecimal,
    val totalAmount: BigDecimal
)

data class PaymentPeriodResource(
    val id: String,
    val periodNumber: Int,
    val dueDate: String,
    val daysInPeriod: Int,
    val currency: String,
    val initialBalanceAmount: BigDecimal,
    val interestPaymentAmount: BigDecimal,
    val principalAmortizationAmount: BigDecimal,
    val creditLifeInsuranceAmount: BigDecimal,
    val vehicleInsuranceAmount: BigDecimal,
    val totalInstallmentAmount: BigDecimal,
    val finalBalanceAmount: BigDecimal,
    val graceType: String
)

data class SimulationResource(
    val id: String,
    val title: String,
    val userId: String,
    val vehicleId: String,
    val financialEntityId: String,
    val currency: String,
    val vehiclePriceAmount: BigDecimal,
    val downPaymentPercentage: BigDecimal,
    val balloonPaymentPercentage: BigDecimal,
    val annualEffectiveRate: BigDecimal,
    val loanTermMonths: Int,
    val gracePeriodType: String,
    val gracePeriodMonths: Int,
    val startDate: String?,
    val metrics: FinancialMetricsResource,
    val paymentSchedule: List<PaymentPeriodResource>
)

data class PageResponse<T>(
    val content: List<T>,
    val number: Int,
    val size: Int,
    val totalElements: Long,
    val totalPages: Int,
    val first: Boolean,
    val last: Boolean,
    val numberOfElements: Int,
    val empty: Boolean
)

data class FinancialEntityResource(
    val id: String = "",
    val userId: String? = null,
    val ruc: String? = null,
    val name: String = "",
    val logoUrl: String? = null,
    val bannerUrl: String? = null,
    val rateBenchmarks: List<RateBenchmarkResource>? = null
)

data class RateBenchmarkResource(
    val id: String? = null,
    val rateType: String? = null,
    val annualRate: BigDecimal? = null,
    val currency: String? = null,
    val sourceLabel: String? = null,
    val sourceUrl: String? = null,
    val effectiveFrom: String? = null
)

data class CreateCreditApplicationResource(
    val vehicleId: String,
    val financialEntityId: String,
    val simulationId: String? = null,
    val requestedAmount: BigDecimal,
    val downPayment: BigDecimal? = null,
    val termMonths: Int,
    val monthlyIncome: BigDecimal,
    val currency: String = "USD",
    val employmentStatus: String = "EMPLOYED"
)

data class CreditApplicationResource(
    val id: String = "",
    val applicantUserId: String = "",
    val vehicleId: String = "",
    val financialEntityId: String = "",
    val simulationId: String? = null,
    val requestedAmount: BigDecimal = BigDecimal.ZERO,
    val downPayment: BigDecimal? = null,
    val termMonths: Int = 36,
    val monthlyIncome: BigDecimal = BigDecimal.ZERO,
    val currency: String = "USD",
    val employmentStatus: String? = null,
    val status: String = "PENDING",
    val notes: String? = null,
    val createdAt: String? = null
)

