package com.smartfinance.mobile.financing.infrastructure.remote

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

/** Endpoints REST de simulaciones financieras. */
interface FinancingApi {
    @POST("api/v1/simulations")
    suspend fun createSimulation(@Body request: CreateSimulationResource): Response<SimulationResource>

    @GET("api/v1/simulations")
    suspend fun getSimulations(
        @Query("page") page: Int = 0,
        @Query("size") size: Int = 20
    ): Response<PageResponse<SimulationResource>>

    @GET("api/v1/simulations/{id}")
    suspend fun getSimulationById(@Path("id") id: String): Response<SimulationResource>

    @DELETE("api/v1/simulations/{id}")
    suspend fun deleteSimulation(@Path("id") id: String): Response<Void>
}
