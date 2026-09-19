package com.smartfinance.mobile.profiles.infrastructure.remote

import java.math.BigDecimal

data class CreateProfileResource(
    val userId: String,
    val email: String,
    val nationalId: String,
    val fullLegalNames: String,
    val dateOfBirth: String, // LocalDate serializes as yyyy-MM-dd
    val phoneCountryCode: String,
    val mobilePhone: String,
    val monthlyIncomeAmount: BigDecimal,
    val monthlyIncomeCurrency: String,
    val employmentStatus: String
)

data class UpdateProfileResource(
    val email: String,
    val nationalId: String,
    val fullLegalNames: String,
    val dateOfBirth: String,
    val phoneCountryCode: String,
    val mobilePhone: String,
    val monthlyIncomeAmount: BigDecimal,
    val monthlyIncomeCurrency: String,
    val employmentStatus: String
)

data class ProfileResource(
    val id: String,
    val userId: String,
    val email: String,
    val nationalId: String,
    val fullLegalNames: String,
    val dateOfBirth: String,
    val phoneCountryCode: String,
    val mobilePhone: String,
    val monthlyIncomeAmount: BigDecimal,
    val monthlyIncomeCurrency: String,
    val employmentStatus: String
)
