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

data class ReniecDniResource(
    val dni: String = "",
    val verificationDigit: String? = null,
    val firstNames: String? = null,
    val paternalSurname: String? = null,
    val maternalSurname: String? = null,
    val fullLegalName: String = "",
    val department: String? = null,
    val province: String? = null,
    val district: String? = null,
    val address: String? = null,
    val fullAddress: String? = null,
    val birthDate: String? = null,
    val gender: String? = null
)

