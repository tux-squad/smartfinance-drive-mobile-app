package com.smartfinance.mobile.catalog.infrastructure.remote

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

/** Endpoints REST de consulta y mantenimiento de vehiculos. */
interface VehicleApi {
    @GET("api/v1/vehicles")
    suspend fun getVehicles(
        @Query("page") page: Int = 0,
        @Query("size") size: Int = 20,
        @Query("brand") brand: String? = null
    ): Response<PageResponse<VehicleResource>>

    @GET("api/v1/vehicles/{id}")
    suspend fun getVehicleById(@Path("id") id: String): Response<VehicleResource>

    @POST("api/v1/vehicles")
    suspend fun createVehicle(@Body resource: CreateVehicleResource): Response<VehicleResource>

    @PUT("api/v1/vehicles/{id}")
    suspend fun updateVehicle(@Path("id") id: String, @Body resource: UpdateVehicleResource): Response<VehicleResource>
}
