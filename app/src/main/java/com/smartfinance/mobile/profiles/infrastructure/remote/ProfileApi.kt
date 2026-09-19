package com.smartfinance.mobile.profiles.infrastructure.remote

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

/** Endpoints REST de perfiles asociados a usuarios. */
interface ProfileApi {
    @POST("api/v1/profiles")
    suspend fun createProfile(@Body request: CreateProfileResource): Response<ProfileResource>

    @GET("api/v1/profiles/{profileId}")
    suspend fun getProfileById(@Path("profileId") profileId: String): Response<ProfileResource>

    @GET("api/v1/profiles/users/{userId}")
    suspend fun getProfileByUserId(@Path("userId") userId: String): Response<ProfileResource>

    @PUT("api/v1/profiles/{profileId}")
    suspend fun updateProfile(@Path("profileId") profileId: String, @Body request: UpdateProfileResource): Response<ProfileResource>

    @DELETE("api/v1/profiles/{profileId}")
    suspend fun deleteProfile(@Path("profileId") profileId: String): Response<Void>
}
