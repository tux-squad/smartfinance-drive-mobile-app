package com.smartfinance.mobile.messaging.infrastructure.remote

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ConsultationApi {
    @POST("api/v1/consultations/chat")
    suspend fun chat(@Body request: CreateAiConsultationResource): Response<AiConsultationResource>

    @GET("api/v1/consultations/history")
    suspend fun getHistory(): Response<List<AiConsultationResource>>
}
