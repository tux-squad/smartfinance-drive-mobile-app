package com.smartfinance.mobile.catalog.domain.repository

import com.smartfinance.mobile.catalog.domain.model.Dealership
import com.smartfinance.mobile.catalog.domain.model.Vehicle
import com.smartfinance.mobile.catalog.infrastructure.remote.CreateVehicleResource

/** Operaciones de dominio para catálogo e inventario de vehículos. */
interface VehicleRepository {
    suspend fun getVehicles(
        page: Int = 0,
        size: Int = 20,
        brand: String? = null,
        model: String? = null,
        minPrice: Double? = null,
        maxPrice: Double? = null
    ): List<Vehicle>

    suspend fun getRecommendedVehicles(): List<Vehicle>
    suspend fun getVehicleById(id: String): Vehicle?
    suspend fun getBrands(): List<String>
    suspend fun getMyListings(): List<Vehicle>
    suspend fun createVehicle(resource: CreateVehicleResource): Vehicle?
    suspend fun getDealerships(search: String? = null): List<Dealership>
    suspend fun getDealershipInventory(dealershipId: String): List<Vehicle>
}
