package com.smartfinance.mobile.settings.domain.repository

import com.smartfinance.mobile.settings.domain.model.UserSettings

interface SettingsRepository {
    suspend fun getSettings(): UserSettings
    suspend fun updateSettings(settings: UserSettings): UserSettings
    suspend fun updatePassword(currentPass: String, newPass: String): Boolean
}
