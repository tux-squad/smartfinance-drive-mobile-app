package com.smartfinance.mobile.settings.domain.model

data class UserSettings(
    val updateAlerts: Boolean = true,
    val weeklySummary: Boolean = false
)
