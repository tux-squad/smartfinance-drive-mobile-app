package com.smartfinance.mobile.settings.domain.repository

import com.smartfinance.mobile.settings.domain.model.BillingInvoice
import com.smartfinance.mobile.settings.domain.model.BillingPlan
import com.smartfinance.mobile.settings.domain.model.DealerSubscription

interface BillingRepository {
    suspend fun getPlans(): Result<List<BillingPlan>>
    suspend fun getCurrentSubscription(): Result<DealerSubscription>
    suspend fun createCheckoutSession(planId: String): Result<String> // returns checkoutUrl
    suspend fun getMyInvoices(): Result<List<BillingInvoice>>
}
