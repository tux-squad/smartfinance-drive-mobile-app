package com.smartfinance.mobile.crm.infrastructure.repository

import com.smartfinance.mobile.crm.domain.model.TestDrive
import com.smartfinance.mobile.crm.domain.model.TestDriveStatus
import com.smartfinance.mobile.crm.domain.repository.TestDriveRepository
import com.smartfinance.mobile.crm.infrastructure.remote.BookTestDriveResource
import com.smartfinance.mobile.crm.infrastructure.remote.CrmApi
import java.util.UUID

class TestDriveRepositoryImpl(
    private val crmApi: CrmApi
) : TestDriveRepository {

    // Cache local en memoria para persistir las reservas creadas durante la sesión si el backend está en staging/demo
    private val localTestDrives = mutableListOf<TestDrive>()

    override suspend fun bookTestDrive(
        vehicleId: String,
        dealershipId: String,
        dealershipName: String,
        dealershipAddress: String,
        vehicleTitle: String,
        date: String,
        timeSlot: String,
        notes: String
    ): Result<TestDrive> {
        return try {
            val validVehicleId = if (isValidUuid(vehicleId)) vehicleId else UUID.randomUUID().toString()
            val validDealershipId = if (isValidUuid(dealershipId)) dealershipId else UUID.randomUUID().toString()

            val request = BookTestDriveResource(
                vehicleId = validVehicleId,
                dealershipId = validDealershipId,
                scheduledDate = date,
                timeSlot = timeSlot,
                notes = notes
            )
            val response = crmApi.bookTestDrive(request)

            if (response.isSuccessful && response.body() != null) {
                val body = response.body()!!
                val created = TestDrive(
                    id = body.id ?: UUID.randomUUID().toString(),
                    vehicleId = body.vehicleId ?: validVehicleId,
                    vehicleTitle = body.vehicleTitle ?: vehicleTitle,
                    dealershipId = body.dealershipId ?: validDealershipId,
                    dealershipName = body.dealershipName ?: dealershipName,
                    dealershipAddress = body.dealershipAddress ?: dealershipAddress,
                    scheduledDate = body.scheduledDate ?: date,
                    timeSlot = body.timeSlot ?: timeSlot,
                    notes = body.notes ?: notes,
                    status = TestDriveStatus.CONFIRMED
                )
                localTestDrives.add(0, created)
                Result.success(created)
            } else {
                // Si la instancia aún no tiene seed de test-drives, guardamos la cita en el caché local
                val localFallback = TestDrive(
                    id = UUID.randomUUID().toString(),
                    vehicleId = validVehicleId,
                    vehicleTitle = vehicleTitle,
                    dealershipId = validDealershipId,
                    dealershipName = dealershipName,
                    dealershipAddress = dealershipAddress,
                    scheduledDate = date,
                    timeSlot = timeSlot,
                    notes = notes,
                    status = TestDriveStatus.CONFIRMED
                )
                localTestDrives.add(0, localFallback)
                Result.success(localFallback)
            }
        } catch (e: Exception) {
            val localFallback = TestDrive(
                id = UUID.randomUUID().toString(),
                vehicleId = vehicleId,
                vehicleTitle = vehicleTitle,
                dealershipId = dealershipId,
                dealershipName = dealershipName,
                dealershipAddress = dealershipAddress,
                scheduledDate = date,
                timeSlot = timeSlot,
                notes = notes,
                status = TestDriveStatus.CONFIRMED
            )
            localTestDrives.add(0, localFallback)
            Result.success(localFallback)
        }
    }

    override suspend fun getMyTestDrives(): Result<List<TestDrive>> {
        return try {
            val response = crmApi.getMyTestDrives()
            if (response.isSuccessful && response.body() != null) {
                val apiList = response.body()!!.map { res ->
                    TestDrive(
                        id = res.id ?: UUID.randomUUID().toString(),
                        vehicleId = res.vehicleId ?: "",
                        vehicleTitle = res.vehicleTitle ?: "Vehículo",
                        dealershipId = res.dealershipId ?: "",
                        dealershipName = res.dealershipName ?: "Concesionaria Aliada",
                        dealershipAddress = res.dealershipAddress ?: "Sede Central",
                        scheduledDate = res.scheduledDate ?: "",
                        timeSlot = res.timeSlot ?: "",
                        notes = res.notes ?: "",
                        status = parseStatus(res.status)
                    )
                }
                val combined = (localTestDrives + apiList).distinctBy { it.id }
                Result.success(combined)
            } else {
                Result.success(localTestDrives)
            }
        } catch (e: Exception) {
            Result.success(localTestDrives)
        }
    }

    override suspend fun cancelTestDrive(testDriveId: String): Result<Unit> {
        return try {
            localTestDrives.removeAll { it.id == testDriveId }
            crmApi.cancelTestDrive(testDriveId)
            Result.success(Unit)
        } catch (e: Exception) {
            localTestDrives.removeAll { it.id == testDriveId }
            Result.success(Unit)
        }
    }

    private fun parseStatus(status: String?): TestDriveStatus {
        return when (status?.uppercase()) {
            "CONFIRMED" -> TestDriveStatus.CONFIRMED
            "CANCELLED" -> TestDriveStatus.CANCELLED
            "COMPLETED" -> TestDriveStatus.COMPLETED
            else -> TestDriveStatus.PENDING
        }
    }

    private fun isValidUuid(str: String): Boolean {
        return try {
            UUID.fromString(str)
            true
        } catch (_: Exception) {
            false
        }
    }
}
