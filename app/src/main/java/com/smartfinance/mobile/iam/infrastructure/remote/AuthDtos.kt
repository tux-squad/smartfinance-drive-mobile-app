package com.smartfinance.mobile.iam.infrastructure.remote

import com.google.gson.annotations.SerializedName

data class SignInResource(
    val username: String,
    val password: String
)

data class SignUpResource(
    val username: String,
    val password: String,
    val roles: List<String>? = null
)

data class AuthenticatedUserResource(
    @SerializedName("id")
    val id: String,
    @SerializedName("username")
    val username: String,
    @SerializedName("token", alternate = ["accessToken"])
    val token: String,
    @SerializedName("refreshToken")
    val refreshToken: String,
    @SerializedName("roles")
    val roles: List<String> = emptyList()
)

data class RefreshTokenResource(
    @SerializedName("refreshToken")
    val refreshToken: String
)

data class UserResource(
    @SerializedName("id")
    val id: String,
    @SerializedName("username")
    val username: String,
    @SerializedName("roles")
    val roles: List<String> = emptyList()
)

