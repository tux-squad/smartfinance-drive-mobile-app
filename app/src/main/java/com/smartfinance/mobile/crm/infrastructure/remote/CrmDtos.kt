package com.smartfinance.mobile.crm.infrastructure.remote

import com.google.gson.annotations.SerializedName

// ── Test Drive DTOs ──────────────────────────────────────────────────────────

data class BookTestDriveResource(
    @SerializedName("vehicleId") val vehicleId: String,
    @SerializedName("dealershipId") val dealershipId: String,
    @SerializedName("scheduledDate") val scheduledDate: String,
    @SerializedName("timeSlot") val timeSlot: String,
    @SerializedName("notes") val notes: String = ""
)

data class TestDriveResource(
    @SerializedName("id") val id: String?,
    @SerializedName("vehicleId") val vehicleId: String?,
    @SerializedName("vehicleTitle") val vehicleTitle: String?,
    @SerializedName("dealershipId") val dealershipId: String?,
    @SerializedName("dealershipName") val dealershipName: String?,
    @SerializedName("dealershipAddress") val dealershipAddress: String?,
    @SerializedName("scheduledDate") val scheduledDate: String?,
    @SerializedName("timeSlot") val timeSlot: String?,
    @SerializedName("status") val status: String?,
    @SerializedName("notes") val notes: String?
)

// ── CRM Prospects & Agents DTOs ──────────────────────────────────────────────

data class ProspectResource(
    @SerializedName("id") val id: String?,
    @SerializedName("fullName") val fullName: String?,
    @SerializedName("email") val email: String?,
    @SerializedName("phone") val phone: String?,
    @SerializedName("vehicleInterest") val vehicleInterest: String?,
    @SerializedName("status") val status: String?,
    @SerializedName("assignedAgent") val assignedAgent: String?,
    @SerializedName("createdAt") val createdAt: String?,
    @SerializedName("notes") val notes: String?
)

data class SalesAgentResource(
    @SerializedName("id") val id: String?,
    @SerializedName("fullName") val fullName: String?,
    @SerializedName("email") val email: String?,
    @SerializedName("phone") val phone: String?,
    @SerializedName("role") val role: String?,
    @SerializedName("active") val active: Boolean?,
    @SerializedName("assignedLeadsCount") val assignedLeadsCount: Int?,
    @SerializedName("conversionRate") val conversionRate: String?,
    @SerializedName("avatarUrl") val avatarUrl: String?
)

data class CreateSalesAgentResource(
    @SerializedName("fullName") val fullName: String,
    @SerializedName("email") val email: String,
    @SerializedName("phone") val phone: String,
    @SerializedName("role") val role: String
)

data class UpdateProspectStatusResource(
    @SerializedName("status") val status: String
)

data class AddNoteResource(
    @SerializedName("note") val note: String
)
