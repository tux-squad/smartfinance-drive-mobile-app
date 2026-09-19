package com.smartfinance.mobile.profiles.infrastructure.repository

import com.smartfinance.mobile.profiles.domain.model.Profile
import com.smartfinance.mobile.profiles.domain.repository.ProfileRepository
import com.smartfinance.mobile.profiles.infrastructure.remote.ProfileApi
import com.smartfinance.mobile.profiles.infrastructure.remote.UpdateProfileResource
import java.math.BigDecimal

class ProfileRepositoryImpl(private val api: ProfileApi) : ProfileRepository {
    override suspend fun getProfileByUserId(userId: String): Profile? {
        return try {
            val response = api.getProfileByUserId(userId)
            if (response.isSuccessful) {
                response.body()?.let {
                    Profile(
                        id = it.id,
                        userId = it.userId,
                        fullName = it.fullLegalNames,
                        email = it.email,
                        phone = "${it.phoneCountryCode} ${it.mobilePhone}",
                        documentNumber = it.nationalId,
                        monthlyIncome = it.monthlyIncomeAmount.toDouble()
                    )
                }
            } else {
                null
            }
        } catch (e: Exception) {
            null
        }
    }

    override suspend fun updateProfile(profile: Profile): Profile {
        val request = UpdateProfileResource(
            email = profile.email,
            nationalId = profile.documentNumber ?: "00000000",
            fullLegalNames = profile.fullName,
            dateOfBirth = "1990-01-01", // Mapeado por defecto si no existe en el dominio actual
            phoneCountryCode = "+51",
            mobilePhone = profile.phone?.replace("+51 ", "") ?: "999999999",
            monthlyIncomeAmount = BigDecimal(profile.monthlyIncome ?: 0.0),
            monthlyIncomeCurrency = "PEN",
            employmentStatus = "EMPLOYED"
        )
        val response = api.updateProfile(profile.id, request)
        if (response.isSuccessful) {
            return profile
        } else {
            throw Exception("Failed to update profile: ${response.code()}")
        }
    }
}
