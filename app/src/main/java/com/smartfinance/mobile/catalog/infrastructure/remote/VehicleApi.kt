package com.smartfinance.mobile.catalog.infrastructure.remote

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import retrofit2.http.Query

/** Endpoints REST de consulta y mantenimiento de vehículos. */
interface VehicleApi {
    @GET("api/v1/vehicles")
    suspend fun getVehicles(
        @Query("page") page: Int = 0,
        @Query("size") size: Int = 20,
        @Query("brand") brand: String? = null,
        @Query("model") model: String? = null,
        @Query("minPrice") minPrice: Double? = null,
        @Query("maxPrice") maxPrice: Double? = null,
        @Query("minYear") minYear: Int? = null,
        @Query("maxYear") maxYear: Int? = null,
        @Query("condition") condition: String? = null,
        @Query("sort") sort: String? = null
    ): Response<PageResponse<VehicleResource>>

    @GET("api/v1/vehicles/brands")
    suspend fun getBrands(): Response<List<String>>

    @GET("api/v1/vehicles/{id}")
    suspend fun getVehicleById(@Path("id") id: String): Response<VehicleResource>

    @GET("api/v1/vehicles/my-listings")
    suspend fun getMyListings(): Response<List<VehicleResource>>

    @POST("api/v1/vehicles")
    suspend fun createVehicle(@Body resource: CreateVehicleResource): Response<VehicleResource>

    @PUT("api/v1/vehicles/{id}")
    suspend fun updateVehicle(@Path("id") id: String, @Body resource: UpdateVehicleResource): Response<VehicleResource>

    @PATCH("api/v1/vehicles/{id}/status")
    suspend fun updateStatus(@Path("id") id: String, @Query("status") status: String): Response<VehicleResource>

    @DELETE("api/v1/vehicles/{id}")
    suspend fun deleteVehicle(@Path("id") id: String): Response<Unit>

    @GET("api/v1/dealerships")
    suspend fun getDealerships(
        @Query("search") search: String? = null,
        @Query("page") page: Int = 0,
        @Query("size") size: Int = 20
    ): Response<PageResponse<DealershipResource>>

    @GET("api/v1/dealerships/{id}")
    suspend fun getDealershipById(@Path("id") id: String): Response<DealershipResource>

    @GET("api/v1/dealerships/{id}/vehicles")
    suspend fun getDealershipVehicles(@Path("id") id: String): Response<List<VehicleResource>>
}
