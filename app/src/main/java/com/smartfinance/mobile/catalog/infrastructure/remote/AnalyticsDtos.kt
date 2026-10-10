package com.smartfinance.mobile.catalog.infrastructure.remote

import com.google.gson.annotations.SerializedName
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

data class DealerAnalyticsResource(
    @SerializedName("totalVehicles") val totalVehicles: Int?,
    @SerializedName("activeVehicles") val activeVehicles: Int?,
    @SerializedName("totalProspects") val totalProspects: Int?,
    @SerializedName("testDrivesScheduled") val testDrivesScheduled: Int?,
    @SerializedName("totalViews") val totalViews: Int?,
    @SerializedName("conversionRate") val conversionRate: String?,
    @SerializedName("monthlySalesVolume") val monthlySalesVolume: String?,
    @SerializedName("period") val period: String?
)

interface AnalyticsApi {
    @GET("api/v1/analytics/dealer")
    suspend fun getDealerAnalytics(
        @Query("period") period: String = "MONTH",
        @Query("dealerUserId") dealerUserId: String? = null
    ): Response<DealerAnalyticsResource>
}
