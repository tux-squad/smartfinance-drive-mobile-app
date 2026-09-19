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
