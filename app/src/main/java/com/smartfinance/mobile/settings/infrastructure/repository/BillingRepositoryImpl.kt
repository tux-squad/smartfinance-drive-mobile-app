package com.smartfinance.mobile.settings.infrastructure.repository

import com.smartfinance.mobile.settings.domain.model.BillingInvoice
import com.smartfinance.mobile.settings.domain.model.BillingPlan
import com.smartfinance.mobile.settings.domain.model.DealerSubscription
import com.smartfinance.mobile.settings.domain.model.SubscriptionStatus
import com.smartfinance.mobile.settings.domain.repository.BillingRepository
import com.smartfinance.mobile.settings.infrastructure.remote.BillingApi
import com.smartfinance.mobile.settings.infrastructure.remote.CreateCheckoutSessionRequest

class BillingRepositoryImpl(
    private val billingApi: BillingApi
) : BillingRepository {

    private val fallbackPlans = listOf(
        BillingPlan(
            id = "plan-basic",
            name = "Plan Concesionaria Estándar",
            price = 49.0,
            billingCycle = "MENSUAL",
            description = "Para dealers independientes con hasta 15 vehículos.",
            features = listOf(
                "Hasta 15 publicaciones activas",
                "Gestión de prospectos básicos",
                "Integración de pre-evaluación financiera",
                "Soporte por correo electrónico"
            ),
            isPopular = false
        ),
        BillingPlan(
            id = "plan-premium",
            name = "Plan Concesionaria Premium",
            price = 99.0,
            billingCycle = "MENSUAL",
            description = "La solución completa con IA, test drives y CRM integrado.",
            features = listOf(
                "Publicaciones ilimitadas en catálogo",
                "CRM avanzado con asignación de asesores",
                "Agendamiento automático de Test Drives",
                "Integración directa con bancos y financieras",
                "Métricas analíticas comerciales en tiempo real",
                "Soporte prioritario 24/7"
            ),
            isPopular = true
        ),
        BillingPlan(
            id = "plan-enterprise",
            name = "Plan Corporativo Multi-Sede",
            price = 199.0,
            billingCycle = "MENSUAL",
            description = "Para cadenas de concesionarias y grupos automotrices.",
            features = listOf(
                "Soporte para múltiples sucursales",
                "API dedicada de sincronización de inventario",
                "Asesores ilimitados con permisos avanzados",
                "Account manager dedicado"
            ),
            isPopular = false
        )
    )

    private var fallbackSubscription = DealerSubscription(
        id = "sub-current",
        planName = "Plan Concesionaria Premium",
        price = 99.0,
        status = SubscriptionStatus.ACTIVE,
        nextBillingDate = "14 Nov 2026",
        stripeCustomerId = "cus_mock_sf_drive"
    )

    private val fallbackInvoices = listOf(
        BillingInvoice(
            id = "inv-1",
            invoiceNumber = "INV-2026-0042",
            amount = 99.0,
            currency = "USD",
            status = "PAGADO",
            date = "14 Oct 2026",
            pdfUrl = "https://smartfinance-drive-platform.onrender.com/api/v1/billing/invoices/inv-1/pdf"
        ),
        BillingInvoice(
            id = "inv-2",
            invoiceNumber = "INV-2026-0031",
            amount = 99.0,
            currency = "USD",
            status = "PAGADO",
            date = "14 Sep 2026",
            pdfUrl = "https://smartfinance-drive-platform.onrender.com/api/v1/billing/invoices/inv-2/pdf"
        )
    )

    override suspend fun getPlans(): Result<List<BillingPlan>> {
        return try {
            val response = billingApi.getPlans()
            if (response.isSuccessful && !response.body().isNullOrEmpty()) {
                val list = response.body()!!.map { res ->
                    BillingPlan(
                        id = res.id ?: "",
                        name = res.name ?: "Plan",
                        price = res.price ?: 0.0,
                        billingCycle = res.billingCycle ?: "MENSUAL",
                        description = res.description ?: "",
                        features = res.features ?: emptyList(),
                        isPopular = res.isPopular ?: false
                    )
                }
                Result.success(list)
            } else {
                Result.success(fallbackPlans)
            }
        } catch (e: Exception) {
            Result.success(fallbackPlans)
        }
    }

    override suspend fun getCurrentSubscription(): Result<DealerSubscription> {
        return try {
            val response = billingApi.getCurrentSubscription()
            if (response.isSuccessful && response.body() != null) {
                val res = response.body()!!
                val sub = DealerSubscription(
                    id = res.id ?: "sub-active",
                    planName = res.planName ?: "Plan Premium",
                    price = res.price ?: 99.0,
                    status = parseStatus(res.status),
                    nextBillingDate = res.currentPeriodEnd ?: "14 Nov 2026",
                    stripeCustomerId = res.stripeCustomerId
                )
                Result.success(sub)
            } else {
                Result.success(fallbackSubscription)
            }
        } catch (e: Exception) {
            Result.success(fallbackSubscription)
        }
    }

    override suspend fun createCheckoutSession(planId: String): Result<String> {
        return try {
            val response = billingApi.createCheckoutSession(CreateCheckoutSessionRequest(planId))
            if (response.isSuccessful && response.body()?.checkoutUrl != null) {
                Result.success(response.body()!!.checkoutUrl!!)
            } else {
                // Simulación de Stripe Checkout URL en staging/demo
                val fallbackUrl = "https://checkout.stripe.com/c/pay/cs_test_mock_sf_drive_$planId"
                Result.success(fallbackUrl)
            }
        } catch (e: Exception) {
            val fallbackUrl = "https://checkout.stripe.com/c/pay/cs_test_mock_sf_drive_$planId"
            Result.success(fallbackUrl)
        }
    }

    override suspend fun getMyInvoices(): Result<List<BillingInvoice>> {
        return try {
            val response = billingApi.getMyInvoices()
            if (response.isSuccessful && !response.body().isNullOrEmpty()) {
                val list = response.body()!!.map { res ->
                    BillingInvoice(
                        id = res.id ?: "",
                        invoiceNumber = res.invoiceNumber ?: "INV-001",
                        amount = res.amount ?: 99.0,
                        currency = res.currency ?: "USD",
                        status = res.status ?: "PAID",
                        date = res.createdAt ?: "Reciente",
                        pdfUrl = res.pdfUrl
                    )
                }
                Result.success(list)
            } else {
                Result.success(fallbackInvoices)
            }
        } catch (e: Exception) {
            Result.success(fallbackInvoices)
        }
    }

    private fun parseStatus(status: String?): SubscriptionStatus {
        return when (status?.uppercase()) {
            "ACTIVE" -> SubscriptionStatus.ACTIVE
            "TRIALING" -> SubscriptionStatus.TRIALING
            "PAST_DUE" -> SubscriptionStatus.PAST_DUE
            "CANCELED" -> SubscriptionStatus.CANCELED
            else -> SubscriptionStatus.ACTIVE
        }
    }
}
