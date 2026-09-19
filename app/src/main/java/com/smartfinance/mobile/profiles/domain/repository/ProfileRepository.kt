package com.smartfinance.mobile.profiles.domain.repository

import com.smartfinance.mobile.profiles.domain.model.Profile

/** Contrato de lectura y actualizacion del perfil del usuario autenticado. */
interface ProfileRepository {
    suspend fun getProfileByUserId(userId: String): Profile?
    suspend fun updateProfile(profile: Profile): Profile
}
