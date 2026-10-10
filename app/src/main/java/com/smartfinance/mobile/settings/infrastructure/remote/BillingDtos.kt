package com.smartfinance.mobile.settings.infrastructure.remote

import com.google.gson.annotations.SerializedName
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

data class BillingPlanResource(
    @SerializedName("id") val id: String?,
    @SerializedName("name") val name: String?,
    @SerializedName("price") val price: Double?,
    @SerializedName("billingCycle") val billingCycle: String?,
    @SerializedName("description") val description: String?,
    @SerializedName("features") val features: List<String>?,
    @SerializedName("isPopular") val isPopular: Boolean?
)

data class SubscriptionResource(
    @SerializedName("id") val id: String?,
    @SerializedName("planName") val planName: String?,
    @SerializedName("price") val price: Double?,
    @SerializedName("status") val status: String?,
    @SerializedName("currentPeriodEnd") val currentPeriodEnd: String?,
    @SerializedName("stripeCustomerId") val stripeCustomerId: String?
)

data class CreateCheckoutSessionRequest(
    @SerializedName("planId") val planId: String
)

data class CheckoutSessionResponse(
    @SerializedName("checkoutUrl") val checkoutUrl: String?,
    @SerializedName("sessionId") val sessionId: String?
)

data class InvoiceResource(
    @SerializedName("id") val id: String?,
    @SerializedName("invoiceNumber") val invoiceNumber: String?,
    @SerializedName("amount") val amount: Double?,
    @SerializedName("currency") val currency: String?,
    @SerializedName("status") val status: String?,
    @SerializedName("createdAt") val createdAt: String?,
    @SerializedName("pdfUrl") val pdfUrl: String?
)

interface BillingApi {
    @GET("api/v1/billing/plans")
    suspend fun getPlans(): Response<List<BillingPlanResource>>

    @GET("api/v1/billing/subscriptions/me")
    suspend fun getCurrentSubscription(): Response<SubscriptionResource>

    @POST("api/v1/billing/subscriptions/checkout-session")
    suspend fun createCheckoutSession(@Body body: CreateCheckoutSessionRequest): Response<CheckoutSessionResponse>

    @GET("api/v1/billing/invoices/me")
    suspend fun getMyInvoices(): Response<List<InvoiceResource>>
}
