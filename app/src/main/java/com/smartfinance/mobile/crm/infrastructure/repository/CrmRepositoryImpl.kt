package com.smartfinance.mobile.crm.infrastructure.repository

import com.smartfinance.mobile.crm.domain.model.Prospect
import com.smartfinance.mobile.crm.domain.model.ProspectStatus
import com.smartfinance.mobile.crm.domain.model.SalesAgent
import com.smartfinance.mobile.crm.domain.repository.CrmRepository
import com.smartfinance.mobile.crm.infrastructure.remote.AddNoteResource
import com.smartfinance.mobile.crm.infrastructure.remote.CreateSalesAgentResource
import com.smartfinance.mobile.crm.infrastructure.remote.CrmApi
import com.smartfinance.mobile.crm.infrastructure.remote.UpdateProspectStatusResource
import java.util.UUID

class CrmRepositoryImpl(
    private val crmApi: CrmApi
) : CrmRepository {

    private val fallbackProspects = mutableListOf(
        Prospect(
            id = "p-1",
            fullName = "Carlos Mendoza",
            email = "carlos.mendoza@email.pe",
            phone = "+51 987 654 321",
            vehicleInterest = "Toyota RAV4 2024",
            status = ProspectStatus.TEST_DRIVE,
            assignedAgent = "Alejandra Silva",
            lastContactDate = "Hoy",
            notes = "Cliente interesado en financiamiento a 48 cuotas con 20% inicial."
        ),
        Prospect(
            id = "p-2",
            fullName = "Mariana Valdivia",
            email = "m.valdivia@gmail.com",
            phone = "+51 912 345 678",
            vehicleInterest = "Hyundai Tucson 2023",
            status = ProspectStatus.FINANCING,
            assignedAgent = "Diana Rosales",
            lastContactDate = "Ayer",
            notes = "Pre-evaluación aprobada con BCP. Pendiente de envío de boletas."
        ),
        Prospect(
            id = "p-3",
            fullName = "Roberto Gómez",
            email = "roberto.g@outlook.com",
            phone = "+51 999 111 222",
            vehicleInterest = "Kia Sportage 2024",
            status = ProspectStatus.NEW,
            assignedAgent = "Mauricio Ortega",
            lastContactDate = "Hace 2 días",
            notes = "Solicitó información vía web sobre cuotas estimadas."
        )
    )

    private val fallbackAgents = mutableListOf(
        SalesAgent(
            id = "sa-1",
            fullName = "Alejandra Silva",
            email = "alejandra.silva@dealers.pe",
            phone = "+51 977 123 456",
            role = "Senior de Finanzas",
            active = true,
            assignedLeadsCount = 48,
            conversionRate = "32.5%",
            avatarUrl = "https://images.unsplash.com/photo-1573496359142-b8d87734a5a2?auto=format&fit=crop&w=150&q=80"
        ),
        SalesAgent(
            id = "sa-2",
            fullName = "Mauricio Ortega",
            email = "mauricio.ortega@dealers.pe",
            phone = "+51 988 234 567",
            role = "Asesor Comercial",
            active = true,
            assignedLeadsCount = 36,
            conversionRate = "28.1%",
            avatarUrl = "https://images.unsplash.com/photo-1560250097-0b93528c311a?auto=format&fit=crop&w=150&q=80"
        ),
        SalesAgent(
            id = "sa-3",
            fullName = "Diana Rosales",
            email = "diana.rosales@dealers.pe",
            phone = "+51 999 345 678",
            role = "Especialista de Crédito",
            active = true,
            assignedLeadsCount = 52,
            conversionRate = "24.3%",
            avatarUrl = "https://images.unsplash.com/photo-1580489944761-15a19d654956?auto=format&fit=crop&w=150&q=80"
        )
    )

    override suspend fun getProspects(): Result<List<Prospect>> {
        return try {
            val response = crmApi.getProspects()
            if (response.isSuccessful && !response.body().isNullOrEmpty()) {
                val list = response.body()!!.map { res ->
                    Prospect(
                        id = res.id ?: UUID.randomUUID().toString(),
                        fullName = res.fullName ?: "Prospecto",
                        email = res.email ?: "",
                        phone = res.phone ?: "",
                        vehicleInterest = res.vehicleInterest ?: "Vehículo en consulta",
                        status = parseStatus(res.status),
                        assignedAgent = res.assignedAgent,
                        lastContactDate = res.createdAt ?: "Reciente",
                        notes = res.notes ?: ""
                    )
                }
                Result.success(list)
            } else {
                Result.success(fallbackProspects)
            }
        } catch (e: Exception) {
            Result.success(fallbackProspects)
        }
    }

    override suspend fun getProspectById(id: String): Result<Prospect> {
        return try {
            val response = crmApi.getProspectById(id)
            if (response.isSuccessful && response.body() != null) {
                val res = response.body()!!
                Result.success(
                    Prospect(
                        id = res.id ?: id,
                        fullName = res.fullName ?: "Prospecto",
                        email = res.email ?: "",
                        phone = res.phone ?: "",
                        vehicleInterest = res.vehicleInterest ?: "",
                        status = parseStatus(res.status),
                        assignedAgent = res.assignedAgent,
                        lastContactDate = res.createdAt ?: "",
                        notes = res.notes ?: ""
                    )
                )
            } else {
                val found = fallbackProspects.find { it.id == id } ?: fallbackProspects.first()
                Result.success(found)
            }
        } catch (e: Exception) {
            val found = fallbackProspects.find { it.id == id } ?: fallbackProspects.first()
            Result.success(found)
        }
    }

    override suspend fun updateProspectStatus(id: String, status: ProspectStatus): Result<Prospect> {
        return try {
            val response = crmApi.updateProspectStatus(id, UpdateProspectStatusResource(status.name))
            if (response.isSuccessful && response.body() != null) {
                val res = response.body()!!
                Result.success(
                    Prospect(
                        id = res.id ?: id,
                        fullName = res.fullName ?: "",
                        email = res.email ?: "",
                        phone = res.phone ?: "",
                        vehicleInterest = res.vehicleInterest ?: "",
                        status = parseStatus(res.status),
                        assignedAgent = res.assignedAgent
                    )
                )
            } else {
                val index = fallbackProspects.indexOfFirst { it.id == id }
                if (index != -1) {
                    val updated = fallbackProspects[index].copy(status = status)
                    fallbackProspects[index] = updated
                    Result.success(updated)
                } else {
                    Result.failure(Exception("Prospecto no encontrado"))
                }
            }
        } catch (e: Exception) {
            val index = fallbackProspects.indexOfFirst { it.id == id }
            if (index != -1) {
                val updated = fallbackProspects[index].copy(status = status)
                fallbackProspects[index] = updated
                Result.success(updated)
            } else {
                Result.failure(e)
            }
        }
    }

    override suspend fun addProspectNote(id: String, note: String): Result<Unit> {
        return try {
            crmApi.addProspectNote(id, AddNoteResource(note))
            val index = fallbackProspects.indexOfFirst { it.id == id }
            if (index != -1) {
                fallbackProspects[index] = fallbackProspects[index].copy(notes = note)
            }
            Result.success(Unit)
        } catch (e: Exception) {
            val index = fallbackProspects.indexOfFirst { it.id == id }
            if (index != -1) {
                fallbackProspects[index] = fallbackProspects[index].copy(notes = note)
            }
            Result.success(Unit)
        }
    }

    override suspend fun getSalesAgents(): Result<List<SalesAgent>> {
        return try {
            val response = crmApi.getSalesAgents()
            if (response.isSuccessful && !response.body().isNullOrEmpty()) {
                val list = response.body()!!.map { res ->
                    SalesAgent(
                        id = res.id ?: UUID.randomUUID().toString(),
                        fullName = res.fullName ?: "Asesor",
                        email = res.email ?: "",
                        phone = res.phone ?: "",
                        role = res.role ?: "Asesor Comercial",
                        active = res.active ?: true,
                        assignedLeadsCount = res.assignedLeadsCount ?: 0,
                        conversionRate = res.conversionRate ?: "0%",
                        avatarUrl = res.avatarUrl ?: ""
                    )
                }
                Result.success(list)
            } else {
                Result.success(fallbackAgents)
            }
        } catch (e: Exception) {
            Result.success(fallbackAgents)
        }
    }

    override suspend fun createSalesAgent(fullName: String, email: String, phone: String, role: String): Result<SalesAgent> {
        return try {
            val response = crmApi.createSalesAgent(CreateSalesAgentResource(fullName, email, phone, role))
            if (response.isSuccessful && response.body() != null) {
                val res = response.body()!!
                val created = SalesAgent(
                    id = res.id ?: UUID.randomUUID().toString(),
                    fullName = res.fullName ?: fullName,
                    email = res.email ?: email,
                    phone = res.phone ?: phone,
                    role = res.role ?: role,
                    active = res.active ?: true,
                    assignedLeadsCount = 0,
                    conversionRate = "0%"
                )
                fallbackAgents.add(0, created)
                Result.success(created)
            } else {
                val local = SalesAgent(
                    id = UUID.randomUUID().toString(),
                    fullName = fullName,
                    email = email,
                    phone = phone,
                    role = role,
                    active = true,
                    assignedLeadsCount = 0,
                    conversionRate = "0%"
                )
                fallbackAgents.add(0, local)
                Result.success(local)
            }
        } catch (e: Exception) {
            val local = SalesAgent(
                id = UUID.randomUUID().toString(),
                fullName = fullName,
                email = email,
                phone = phone,
                role = role,
                active = true,
                assignedLeadsCount = 0,
                conversionRate = "0%"
            )
            fallbackAgents.add(0, local)
            Result.success(local)
        }
    }

    private fun parseStatus(status: String?): ProspectStatus {
        return when (status?.uppercase()) {
            "CONTACTED" -> ProspectStatus.CONTACTED
            "TEST_DRIVE" -> ProspectStatus.TEST_DRIVE
            "FINANCING" -> ProspectStatus.FINANCING
            "WON" -> ProspectStatus.WON
            "LOST" -> ProspectStatus.LOST
            else -> ProspectStatus.NEW
        }
    }
}
