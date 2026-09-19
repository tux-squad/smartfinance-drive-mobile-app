package com.smartfinance.mobile.iam.infrastructure.remote

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

/** Endpoints REST publicos de registro y autenticacion. */
interface AuthApi {
    @POST("api/v1/auth/sessions")
    suspend fun signIn(@Body request: SignInResource): Response<AuthenticatedUserResource>

    @POST("api/v1/auth/registrations")
    suspend fun signUp(@Body request: SignUpResource): Response<UserResource>
}
