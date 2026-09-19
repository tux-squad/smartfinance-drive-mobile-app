package com.smartfinance.mobile.catalog.infrastructure.repository

import com.smartfinance.mobile.catalog.domain.model.Dealership
import com.smartfinance.mobile.catalog.domain.model.Vehicle
import com.smartfinance.mobile.catalog.domain.repository.VehicleRepository
import com.smartfinance.mobile.catalog.infrastructure.remote.VehicleApi

class VehicleRepositoryImpl(private val api: VehicleApi) : VehicleRepository {
    override suspend fun getRecommendedVehicles(): List<Vehicle> {
        return try {
            val response = api.getVehicles(page = 0, size = 20)
            if (response.isSuccessful) {
                response.body()?.content?.map {
                    Vehicle(
                        id = it.id,
                        brand = it.brand,
                        model = it.model,
                        year = it.manufactureYear,
                        priceUsd = it.priceAmount.toDouble(),
                        city = "Lima", // Backend no provee ciudad por ahora
                        kilometers = if (it.condition == "NEW") 0 else 50000,
                        imageUrl = it.imagePath ?: "https://via.placeholder.com/300x200?text=No+Image"
                    )
                } ?: emptyList()
            } else {
                emptyList()
            }
        } catch (e: Exception) {
            emptyList()
        }
    }

    override suspend fun getDealerships(): List<Dealership> {
        // Backend no tiene endpoint explicito para concesionarios aun, se deberia simular o retornar vacio
        return emptyList()
    }

    override suspend fun getDealershipInventory(dealershipId: String): List<Vehicle> {
        return emptyList()
    }
}
