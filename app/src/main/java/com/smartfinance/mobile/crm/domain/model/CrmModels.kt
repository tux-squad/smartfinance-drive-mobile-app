package com.smartfinance.mobile.crm.domain.model

enum class TestDriveStatus {
    PENDING,
    CONFIRMED,
    CANCELLED,
    COMPLETED
}

data class TestDrive(
    val id: String,
    val vehicleId: String,
    val vehicleTitle: String,
    val dealershipId: String,
    val dealershipName: String,
    val dealershipAddress: String,
    val scheduledDate: String,
    val timeSlot: String,
    val notes: String = "",
    val status: TestDriveStatus = TestDriveStatus.PENDING
)

enum class ProspectStatus {
    NEW,
    CONTACTED,
    TEST_DRIVE,
    FINANCING,
    WON,
    LOST
}

data class Prospect(
    val id: String,
    val fullName: String,
    val email: String,
    val phone: String,
    val vehicleInterest: String,
    val status: ProspectStatus = ProspectStatus.NEW,
    val assignedAgent: String? = null,
    val lastContactDate: String = "",
    val notes: String = ""
)

data class SalesAgent(
    val id: String,
    val fullName: String,
    val email: String,
    val phone: String,
    val role: String = "Asesor Comercial",
    val active: Boolean = true,
    val assignedLeadsCount: Int = 0,
    val conversionRate: String = "0%",
    val avatarUrl: String = ""
)
