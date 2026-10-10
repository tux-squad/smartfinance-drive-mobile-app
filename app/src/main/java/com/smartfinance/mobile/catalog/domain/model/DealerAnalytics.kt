package com.smartfinance.mobile.catalog.domain.model

data class DealerAnalytics(
    val totalVehicles: Int = 24,
    val activeVehicles: Int = 21,
    val totalProspects: Int = 18,
    val testDrivesScheduled: Int = 12,
    val totalViews: Int = 1420,
    val conversionRate: String = "28.5%",
    val monthlySalesVolume: String = "$148,500",
    val period: String = "MONTH"
)
