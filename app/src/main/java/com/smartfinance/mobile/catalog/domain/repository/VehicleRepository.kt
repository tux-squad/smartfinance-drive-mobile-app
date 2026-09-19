package com.smartfinance.mobile.catalog.domain.repository

import com.smartfinance.mobile.catalog.domain.model.Dealership
import com.smartfinance.mobile.catalog.domain.model.Vehicle

/** Operaciones de dominio para catalogo e inventario de vehiculos. */
interface VehicleRepository {
    suspend fun getRecommendedVehicles(): List<Vehicle>
    suspend fun getDealerships(): List<Dealership>
    suspend fun getDealershipInventory(dealershipId: String): List<Vehicle>
}
