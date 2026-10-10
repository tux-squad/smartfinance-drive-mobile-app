package com.smartfinance.mobile.iam.infrastructure.remote

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.POST

/** Endpoints REST de registro, autenticacion y control de sesion. */
interface AuthApi {
    @POST("api/v1/auth/sessions")
    suspend fun signIn(@Body request: SignInResource): Response<AuthenticatedUserResource>

    @POST("api/v1/auth/registrations")
    suspend fun signUp(@Body request: SignUpResource): Response<UserResource>

    @POST("api/v1/auth/tokens")
    suspend fun refreshToken(@Body request: RefreshTokenResource): Response<AuthenticatedUserResource>

    @DELETE("api/v1/auth/sessions/current")
    suspend fun revokeCurrentSession(): Response<Unit>
}
