package com.smartfinance.mobile.settings.domain.model

data class BillingPlan(
    val id: String,
    val name: String,
    val price: Double,
    val billingCycle: String = "MONTHLY",
    val description: String = "",
    val features: List<String> = emptyList(),
    val isPopular: Boolean = false
)

enum class SubscriptionStatus {
    ACTIVE,
    TRIALING,
    PAST_DUE,
    CANCELED,
    INACTIVE
}

data class DealerSubscription(
    val id: String,
    val planName: String,
    val price: Double,
    val status: SubscriptionStatus = SubscriptionStatus.ACTIVE,
    val nextBillingDate: String = "",
    val stripeCustomerId: String? = null
)

data class BillingInvoice(
    val id: String,
    val invoiceNumber: String,
    val amount: Double,
    val currency: String = "USD",
    val status: String = "PAID",
    val date: String,
    val pdfUrl: String? = null
)
