package com.smartfinance.mobile.di

import android.content.Context
import com.smartfinance.mobile.catalog.domain.repository.VehicleRepository
import com.smartfinance.mobile.catalog.infrastructure.remote.VehicleApi
import com.smartfinance.mobile.catalog.infrastructure.repository.VehicleRepositoryImpl
import com.smartfinance.mobile.core.network.RetrofitProvider
import com.smartfinance.mobile.core.storage.TokenStorage
import com.smartfinance.mobile.financing.domain.repository.FinancingRepository
import com.smartfinance.mobile.financing.infrastructure.remote.FinancingApi
import com.smartfinance.mobile.financing.infrastructure.repository.FinancingRepositoryImpl
import com.smartfinance.mobile.iam.domain.repository.AuthRepository
import com.smartfinance.mobile.iam.infrastructure.remote.AuthApi
import com.smartfinance.mobile.iam.infrastructure.repository.AuthRepositoryImpl
import com.smartfinance.mobile.profiles.domain.repository.ProfileRepository
import com.smartfinance.mobile.profiles.infrastructure.remote.ProfileApi
import com.smartfinance.mobile.profiles.infrastructure.repository.ProfileRepositoryImpl

/**
 * Contenedor manual de Inyección de Dependencias (DI) para la app.
 * Proveerá instancias únicas (Singletons) de todos los repositorios y servicios de red.
 */
/**
 * Composition root de la aplicacion.
 *
 * Mantiene una unica instancia de almacenamiento, cliente HTTP y repositorios.
 */
class AppContainer(private val context: Context) {
    
    // Core
    val tokenStorage: TokenStorage by lazy {
        TokenStorage(context)
    }

    private val retrofit by lazy {
        RetrofitProvider.getRetrofit(tokenStorage)
    }

    // APIs
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

    // Repositories
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
}
