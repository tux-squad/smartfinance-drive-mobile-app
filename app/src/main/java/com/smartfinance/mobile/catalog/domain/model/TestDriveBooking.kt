package com.smartfinance.mobile.catalog.domain.model

data class TestDriveBooking(
    val id: String = "1",
    val vehicleId: String = "1",
    val vehicleTitle: String = "Toyota RAV4 2024",
    val dealershipName: String = "AutoCentro San Isidro",
    val dealershipAddress: String = "Av. Javier Prado Este 1234, San Isidro",
    val selectedDate: String = "08/10/2026",
    val selectedTimeSlot: String = "11:30 AM"
)
