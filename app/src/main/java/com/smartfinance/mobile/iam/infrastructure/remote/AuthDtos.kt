package com.smartfinance.mobile.iam.infrastructure.remote

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
    val id: Long,
    val username: String,
    val token: String,
    val refreshToken: String,
    val roles: List<String>
)

data class UserResource(
    val id: Long,
    val username: String,
    val roles: List<String>
)

