package com.smartfinance.mobile.financing.infrastructure.remote

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

/** Endpoints REST de simulaciones financieras, entidades aliadas y solicitudes de crédito formales. */
interface FinancingApi {

    // ── Entidades Financieras (Bancos Aliados) ──
    @GET("api/v1/financial-entities")
    suspend fun getFinancialEntities(): Response<List<FinancialEntityResource>>

    @GET("api/v1/financial-entities/{id}")
    suspend fun getFinancialEntityById(@Path("id") id: String): Response<FinancialEntityResource>

    // ── Simulaciones Financieras ──
    @POST("api/v1/simulations")
    suspend fun createSimulation(@Body request: CreateSimulationResource): Response<SimulationResource>

    @GET("api/v1/simulations")
    suspend fun getSimulations(
        @Query("page") page: Int = 0,
        @Query("size") size: Int = 20
    ): Response<PageResponse<SimulationResource>>

    @GET("api/v1/simulations/{id}")
    suspend fun getSimulationById(@Path("id") id: String): Response<SimulationResource>

    @POST("api/v1/simulations/{id}/apply")
    suspend fun applyFromSimulation(@Path("id") id: String): Response<CreditApplicationResource>

    @DELETE("api/v1/simulations/{id}")
    suspend fun deleteSimulation(@Path("id") id: String): Response<Void>

    // ── Solicitudes de Crédito Formales (Credit Applications) ──
    @POST("api/v1/credit-applications")
    suspend fun createCreditApplication(@Body request: CreateCreditApplicationResource): Response<CreditApplicationResource>

    @GET("api/v1/credit-applications/me")
    suspend fun getMyCreditApplications(): Response<List<CreditApplicationResource>>

    @GET("api/v1/credit-applications/{id}")
    suspend fun getCreditApplicationById(@Path("id") id: String): Response<CreditApplicationResource>
}
