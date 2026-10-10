package com.smartfinance.mobile.profiles.infrastructure.repository

import com.smartfinance.mobile.profiles.domain.model.Profile
import com.smartfinance.mobile.profiles.domain.repository.ProfileRepository
import com.smartfinance.mobile.profiles.infrastructure.remote.CreateProfileResource
import com.smartfinance.mobile.profiles.infrastructure.remote.ProfileApi
import com.smartfinance.mobile.profiles.infrastructure.remote.ReniecDniResource
import com.smartfinance.mobile.profiles.infrastructure.remote.UpdateProfileResource
import java.math.BigDecimal

class ProfileRepositoryImpl(private val api: ProfileApi) : ProfileRepository {

    override suspend fun getProfileByUserId(userId: String): Profile? {
        return try {
            val response = api.getProfileByUserId(userId)
            if (response.isSuccessful && response.body() != null) {
                val it = response.body()!!
                Profile(
                    id = it.id,
                    userId = it.userId,
                    fullName = it.fullLegalNames,
                    email = it.email,
                    phone = "${it.phoneCountryCode} ${it.mobilePhone}",
                    documentNumber = it.nationalId,
                    monthlyIncome = it.monthlyIncomeAmount.toDouble()
                )
            } else {
                null
            }
        } catch (_: Exception) {
            null
        }
    }

    override suspend fun createProfile(profile: Profile): Profile {
        val request = CreateProfileResource(
            userId = profile.userId,
            email = profile.email,
            nationalId = profile.documentNumber?.takeIf { it.isNotBlank() } ?: "72481940",
            fullLegalNames = profile.fullName.takeIf { it.isNotBlank() } ?: "Usuario SmartFinance",
            dateOfBirth = "1995-05-15",
            phoneCountryCode = "+51",
            mobilePhone = profile.phone?.replace("+51 ", "")?.takeIf { it.isNotBlank() } ?: "987654321",
            monthlyIncomeAmount = BigDecimal(profile.monthlyIncome ?: 3500.0),
            monthlyIncomeCurrency = "PEN",
            employmentStatus = "EMPLOYED"
        )
        val response = api.createProfile(request)
        if (response.isSuccessful && response.body() != null) {
            val created = response.body()!!
            return profile.copy(id = created.id)
        } else {
            return profile
        }
    }

    override suspend fun updateProfile(profile: Profile): Profile {
        if (profile.id.isBlank()) {
            return createProfile(profile)
        }
        val request = UpdateProfileResource(
            email = profile.email,
            nationalId = profile.documentNumber?.takeIf { it.isNotBlank() } ?: "72481940",
            fullLegalNames = profile.fullName,
            dateOfBirth = "1995-05-15",
            phoneCountryCode = "+51",
            mobilePhone = profile.phone?.replace("+51 ", "")?.takeIf { it.isNotBlank() } ?: "987654321",
            monthlyIncomeAmount = BigDecimal(profile.monthlyIncome ?: 3500.0),
            monthlyIncomeCurrency = "PEN",
            employmentStatus = "EMPLOYED"
        )
        val response = api.updateProfile(profile.id, request)
        if (response.isSuccessful) {
            return profile
        } else {
            return profile
        }
    }

    override suspend fun consultReniecDni(dni: String): ReniecDniResource? {
        val cleanDni = dni.trim()
        if (cleanDni.length != 8) return null
        return try {
            val response = api.getDniInfo(cleanDni)
            if (response.isSuccessful) {
                response.body()
            } else {
                null
            }
        } catch (_: Exception) {
            null
        }
    }
}
