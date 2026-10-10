package com.smartfinance.mobile.crm.infrastructure.remote

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path

interface CrmApi {
    // ── Test Drives ──────────────────────────────────────────────────────────
    @POST("api/v1/test-drives")
    suspend fun bookTestDrive(@Body body: BookTestDriveResource): Response<TestDriveResource>

    @GET("api/v1/test-drives/me")
    suspend fun getMyTestDrives(): Response<List<TestDriveResource>>

    @GET("api/v1/test-drives/{id}")
    suspend fun getTestDriveById(@Path("id") id: String): Response<TestDriveResource>

    @DELETE("api/v1/test-drives/{id}")
    suspend fun cancelTestDrive(@Path("id") id: String): Response<Unit>

    // ── Dealers CRM & Prospects ──────────────────────────────────────────────
    @GET("api/v1/dealers/me/prospects")
    suspend fun getProspects(): Response<List<ProspectResource>>

    @GET("api/v1/dealers/me/prospects/{id}")
    suspend fun getProspectById(@Path("id") id: String): Response<ProspectResource>

    @PATCH("api/v1/prospects/{id}/status")
    suspend fun updateProspectStatus(
        @Path("id") id: String,
        @Body body: UpdateProspectStatusResource
    ): Response<ProspectResource>

    @POST("api/v1/prospects/{id}/notes")
    suspend fun addProspectNote(
        @Path("id") id: String,
        @Body body: AddNoteResource
    ): Response<Unit>

    // ── Sales Agents ─────────────────────────────────────────────────────────
    @GET("api/v1/dealers/me/sales-agents")
    suspend fun getSalesAgents(): Response<List<SalesAgentResource>>

    @POST("api/v1/dealers/me/sales-agents")
    suspend fun createSalesAgent(@Body body: CreateSalesAgentResource): Response<SalesAgentResource>
}
