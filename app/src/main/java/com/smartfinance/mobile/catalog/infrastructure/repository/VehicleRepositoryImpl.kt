package com.smartfinance.mobile.catalog.infrastructure.repository

import com.smartfinance.mobile.catalog.domain.model.Dealership
import com.smartfinance.mobile.catalog.domain.model.Vehicle
import com.smartfinance.mobile.catalog.domain.repository.VehicleRepository
import com.smartfinance.mobile.catalog.infrastructure.remote.VehicleApi
import com.smartfinance.mobile.catalog.infrastructure.remote.VehicleResource

import com.smartfinance.mobile.catalog.infrastructure.remote.DealershipResource

class VehicleRepositoryImpl(private val api: VehicleApi) : VehicleRepository {

    override suspend fun getVehicles(
        page: Int,
        size: Int,
        brand: String?,
        model: String?,
        minPrice: Double?,
        maxPrice: Double?
    ): List<Vehicle> {
        return try {
            val response = api.getVehicles(
                page = page,
                size = size,
                brand = brand?.takeIf { it.isNotBlank() && it != "Todos" },
                model = model?.takeIf { it.isNotBlank() },
                minPrice = minPrice,
                maxPrice = maxPrice
            )
            if (response.isSuccessful) {
                response.body()?.content?.map { it.toDomain() } ?: emptyList()
            } else {
                emptyList()
            }
        } catch (_: Exception) {
            emptyList()
        }
    }

    override suspend fun getRecommendedVehicles(): List<Vehicle> {
        return getVehicles(page = 0, size = 20)
    }

    override suspend fun getVehicleById(id: String): Vehicle? {
        return try {
            val response = api.getVehicleById(id)
            if (response.isSuccessful) {
                response.body()?.toDomain()
            } else {
                null
            }
        } catch (_: Exception) {
            null
        }
    }

    override suspend fun getBrands(): List<String> {
        return try {
            val response = api.getBrands()
            if (response.isSuccessful && !response.body().isNullOrEmpty()) {
                response.body()!!
            } else {
                listOf("Toyota", "Nissan", "Hyundai", "Kia", "Honda", "Mazda")
            }
        } catch (_: Exception) {
            listOf("Toyota", "Nissan", "Hyundai", "Kia", "Honda", "Mazda")
        }
    }

    override suspend fun getMyListings(): List<Vehicle> {
        return try {
            val response = api.getMyListings()
            if (response.isSuccessful) {
                response.body()?.map { it.toDomain() } ?: emptyList()
            } else {
                emptyList()
            }
        } catch (_: Exception) {
            emptyList()
        }
    }

    override suspend fun createVehicle(resource: com.smartfinance.mobile.catalog.infrastructure.remote.CreateVehicleResource): Vehicle? {
        return try {
            val response = api.createVehicle(resource)
            if (response.isSuccessful && response.body() != null) {
                response.body()!!.toDomain()
            } else {
                null
            }
        } catch (_: Exception) {
            null
        }
    }

    override suspend fun getDealerships(search: String?): List<Dealership> {
        return try {
            val response = api.getDealerships(search = search?.takeIf { it.isNotBlank() })
            if (response.isSuccessful && !response.body()?.content.isNullOrEmpty()) {
                response.body()!!.content.map { it.toDomain() }
            } else {
                getFallbackDealerships().filter {
                    search.isNullOrBlank() || it.name.contains(search, ignoreCase = true) || it.address.contains(search, ignoreCase = true)
                }
            }
        } catch (_: Exception) {
            getFallbackDealerships().filter {
                search.isNullOrBlank() || it.name.contains(search, ignoreCase = true) || it.address.contains(search, ignoreCase = true)
            }
        }
    }

    override suspend fun getDealershipInventory(dealershipId: String): List<Vehicle> {
        return try {
            val response = api.getDealershipVehicles(dealershipId)
            if (response.isSuccessful && !response.body().isNullOrEmpty()) {
                response.body()!!.map { it.toDomain() }
            } else {
                getVehicles(page = 0, size = 10)
            }
        } catch (_: Exception) {
            getVehicles(page = 0, size = 10)
        }
    }

    private fun DealershipResource.toDomain(): Dealership {
        val displayImage = logoUrl?.takeIf { it.isNotBlank() }
            ?: bannerUrl?.takeIf { it.isNotBlank() }
            ?: "https://images.unsplash.com/photo-1493238792000-8113da705763?auto=format&fit=crop&w=1200&q=80"
        return Dealership(
            id = id,
            name = name,
            address = address ?: "Av. Javier Prado Este 4571",
            city = "Lima",
            phone = phone,
            email = email,
            rating = rating ?: 4.8,
            isVerified = active,
            imageUrl = displayImage,
            description = description,
            operatingHours = operatingHours
        )
    }

    private fun getFallbackDealerships(): List<Dealership> = listOf(
        Dealership(
            id = "d1a00000-0000-0000-0000-000000000001",
            name = "AutoPlaza Lima",
            address = "Av. Javier Prado Este 4571, Santiago de Surco",
            city = "Lima, Perú",
            phone = "+51 1 619-2000",
            email = "ventas@autoplaza.pe",
            rating = 4.9,
            isVerified = true,
            imageUrl = "https://images.unsplash.com/photo-1493238792000-8113da705763?auto=format&fit=crop&w=1200&q=80",
            description = "Concesionario multimarca líder en vehículos certificados con garantía de fábrica.",
            operatingHours = "Lun - Sáb: 9:00 - 19:00"
        ),
        Dealership(
            id = "d1a00000-0000-0000-0000-000000000002",
            name = "MegaMotors San Isidro",
            address = "Av. República de Panamá 3850, San Isidro",
            city = "Lima, Perú",
            phone = "+51 1 422-8900",
            email = "contacto@megamotors.pe",
            rating = 4.8,
            isVerified = true,
            imageUrl = "https://images.unsplash.com/photo-1503376780353-7e6692767b70?auto=format&fit=crop&w=1200&q=80",
            description = "Especialistas en vehículos premium y seminuevos garantizados.",
            operatingHours = "Lun - Vie: 8:30 - 18:30"
        ),
        Dealership(
            id = "d1a00000-0000-0000-0000-000000000003",
            name = "EuroMotors La Molina",
            address = "Av. La Molina 1120, La Molina",
            city = "Lima, Perú",
            phone = "+51 1 349-5500",
            email = "info@euromotors.com.pe",
            rating = 4.7,
            isVerified = true,
            imageUrl = "https://images.unsplash.com/photo-1563720223185-11003d516935?auto=format&fit=crop&w=1200&q=80",
            description = "Representante oficial de marcas europeas con asesoría financiera directa.",
            operatingHours = "Lun - Sáb: 9:00 - 20:00"
        )
    )

    private fun VehicleResource.toDomain(): Vehicle {
        val displayImage = imagePath?.takeIf { it.isNotBlank() }
            ?: "https://images.unsplash.com/photo-1552519507-da3b142c6e3d?auto=format&fit=crop&w=800&q=80"
        return Vehicle(
            id = id,
            brand = brand,
            model = model,
            year = manufactureYear,
            priceUsd = priceAmount.toDouble(),
            city = "Lima",
            kilometers = if (condition.equals("NEW", ignoreCase = true)) 0 else 35000,
            imageUrl = displayImage,
            condition = condition ?: "NEW",
            financialEntityId = financialEntityId.orEmpty(),
            dealershipId = dealershipId
        )
    }
}
