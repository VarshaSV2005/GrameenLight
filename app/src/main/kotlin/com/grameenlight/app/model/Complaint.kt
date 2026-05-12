package com.grameenlight.app.model

data class Complaint(
    val id: String,
    val poleId: String,
    val streetName: String,
    val issue: String,
    val repairStatus: RepairStatus,
    val assignedTo: String,
    val timeLabel: String
)
