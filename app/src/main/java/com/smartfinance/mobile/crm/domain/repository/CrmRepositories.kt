package com.smartfinance.mobile.crm.domain.repository

import com.smartfinance.mobile.crm.domain.model.Prospect
import com.smartfinance.mobile.crm.domain.model.ProspectStatus
import com.smartfinance.mobile.crm.domain.model.SalesAgent
import com.smartfinance.mobile.crm.domain.model.TestDrive

interface TestDriveRepository {
    suspend fun bookTestDrive(
        vehicleId: String,
        dealershipId: String,
        dealershipName: String,
        dealershipAddress: String,
        vehicleTitle: String,
        date: String,
        timeSlot: String,
        notes: String = ""
    ): Result<TestDrive>

    suspend fun getMyTestDrives(): Result<List<TestDrive>>
    suspend fun cancelTestDrive(testDriveId: String): Result<Unit>
}

interface CrmRepository {
    suspend fun getProspects(): Result<List<Prospect>>
    suspend fun getProspectById(id: String): Result<Prospect>
    suspend fun updateProspectStatus(id: String, status: ProspectStatus): Result<Prospect>
    suspend fun addProspectNote(id: String, note: String): Result<Unit>
    suspend fun getSalesAgents(): Result<List<SalesAgent>>
    suspend fun createSalesAgent(fullName: String, email: String, phone: String, role: String): Result<SalesAgent>
}
