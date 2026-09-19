package com.smartfinance.mobile.iam.domain.model

data class User(
    val id: String = "",
    val username: String = "",
    val email: String = "",
    val role: String = "ROLE_USER",
    val accessToken: String = "",
    val refreshToken: String = ""
)
