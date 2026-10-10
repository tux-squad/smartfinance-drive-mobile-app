package com.smartfinance.mobile.catalog.domain.repository

import com.smartfinance.mobile.catalog.domain.model.DealerAnalytics

interface AnalyticsRepository {
    suspend fun getDealerAnalytics(period: String = "MONTH"): Result<DealerAnalytics>
}
