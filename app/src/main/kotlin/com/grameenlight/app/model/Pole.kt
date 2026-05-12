package com.grameenlight.app.model

data class Pole(
    val id: String,
    val streetName: String,
    val latitude: Double,
    val longitude: Double,
    val gridX: Float,
    val gridY: Float,
    val status: PoleStatus,
    val repairStatus: RepairStatus,
    val assignedTo: String? = null,
    val complaintId: String? = null,
    val lastReportedMillis: Long? = null
)
