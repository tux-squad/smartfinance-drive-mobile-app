package com.smartfinance.mobile.di

import android.content.Context
import com.smartfinance.mobile.catalog.domain.repository.AnalyticsRepository
import com.smartfinance.mobile.catalog.domain.repository.VehicleRepository
import com.smartfinance.mobile.catalog.infrastructure.remote.AnalyticsApi
import com.smartfinance.mobile.catalog.infrastructure.remote.VehicleApi
import com.smartfinance.mobile.catalog.infrastructure.repository.AnalyticsRepositoryImpl
import com.smartfinance.mobile.catalog.infrastructure.repository.VehicleRepositoryImpl
import com.smartfinance.mobile.core.network.RetrofitProvider
import com.smartfinance.mobile.core.storage.TokenStorage
import com.smartfinance.mobile.crm.domain.repository.CrmRepository
import com.smartfinance.mobile.crm.domain.repository.TestDriveRepository
import com.smartfinance.mobile.crm.infrastructure.remote.CrmApi
import com.smartfinance.mobile.crm.infrastructure.repository.CrmRepositoryImpl
import com.smartfinance.mobile.crm.infrastructure.repository.TestDriveRepositoryImpl
import com.smartfinance.mobile.financing.domain.repository.FinancingRepository
import com.smartfinance.mobile.financing.infrastructure.remote.FinancingApi
import com.smartfinance.mobile.financing.infrastructure.repository.FinancingRepositoryImpl
import com.smartfinance.mobile.iam.domain.repository.AuthRepository
import com.smartfinance.mobile.iam.infrastructure.remote.AuthApi
import com.smartfinance.mobile.iam.infrastructure.repository.AuthRepositoryImpl
import com.smartfinance.mobile.messaging.domain.repository.ConsultationRepository
import com.smartfinance.mobile.messaging.domain.repository.MessagingRepository
import com.smartfinance.mobile.messaging.infrastructure.remote.ConsultationApi
import com.smartfinance.mobile.messaging.infrastructure.repository.ConsultationRepositoryImpl
import com.smartfinance.mobile.messaging.infrastructure.repository.MessagingRepositoryImpl
import com.smartfinance.mobile.profiles.domain.repository.ProfileRepository
import com.smartfinance.mobile.profiles.infrastructure.remote.ProfileApi
import com.smartfinance.mobile.profiles.infrastructure.repository.ProfileRepositoryImpl
import com.smartfinance.mobile.settings.domain.repository.BillingRepository
import com.smartfinance.mobile.settings.infrastructure.remote.BillingApi
import com.smartfinance.mobile.settings.infrastructure.repository.BillingRepositoryImpl

/**
 * Composition root de la aplicación.
 * Mantiene una única instancia de almacenamiento, cliente HTTP y repositorios.
 */
class AppContainer(private val context: Context) {
    
    // Core
    val tokenStorage: TokenStorage by lazy {
        TokenStorage(context)
    }

    private val retrofit by lazy {
        RetrofitProvider.getRetrofit(tokenStorage)
    }

    // ── Remote APIs ──────────────────────────────────────────────────────────
    private val authApi: AuthApi by lazy {
        retrofit.create(AuthApi::class.java)
    }

    private val vehicleApi: VehicleApi by lazy {
        retrofit.create(VehicleApi::class.java)
    }

    private val profileApi: ProfileApi by lazy {
        retrofit.create(ProfileApi::class.java)
    }

    private val financingApi: FinancingApi by lazy {
        retrofit.create(FinancingApi::class.java)
    }

    private val consultationApi: ConsultationApi by lazy {
        retrofit.create(ConsultationApi::class.java)
    }

    private val crmApi: CrmApi by lazy {
        retrofit.create(CrmApi::class.java)
    }

    private val billingApi: BillingApi by lazy {
        retrofit.create(BillingApi::class.java)
    }

    private val analyticsApi: AnalyticsApi by lazy {
        retrofit.create(AnalyticsApi::class.java)
    }

    // ── Singletons Repositories ──────────────────────────────────────────────
    val authRepository: AuthRepository by lazy {
        AuthRepositoryImpl(authApi, tokenStorage)
    }

    val vehicleRepository: VehicleRepository by lazy {
        VehicleRepositoryImpl(vehicleApi)
    }

    val profileRepository: ProfileRepository by lazy {
        ProfileRepositoryImpl(profileApi)
    }

    val financingRepository: FinancingRepository by lazy {
        FinancingRepositoryImpl(financingApi, tokenStorage)
    }

    val consultationRepository: ConsultationRepository by lazy {
        ConsultationRepositoryImpl(consultationApi)
    }

    val testDriveRepository: TestDriveRepository by lazy {
        TestDriveRepositoryImpl(crmApi)
    }

    val crmRepository: CrmRepository by lazy {
        CrmRepositoryImpl(crmApi)
    }

    val messagingRepository: MessagingRepository by lazy {
        MessagingRepositoryImpl()
    }

    val billingRepository: BillingRepository by lazy {
        BillingRepositoryImpl(billingApi)
    }

    val analyticsRepository: AnalyticsRepository by lazy {
        AnalyticsRepositoryImpl(analyticsApi)
    }
}
