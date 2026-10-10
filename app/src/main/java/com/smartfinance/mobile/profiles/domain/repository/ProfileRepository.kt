package com.smartfinance.mobile.profiles.domain.repository

import com.smartfinance.mobile.profiles.domain.model.Profile
import com.smartfinance.mobile.profiles.infrastructure.remote.ReniecDniResource

/** Contrato de lectura, creación, actualización y consulta RENIEC del perfil. */
interface ProfileRepository {
    suspend fun getProfileByUserId(userId: String): Profile?
    suspend fun createProfile(profile: Profile): Profile
    suspend fun updateProfile(profile: Profile): Profile
    suspend fun consultReniecDni(dni: String): ReniecDniResource?
}
