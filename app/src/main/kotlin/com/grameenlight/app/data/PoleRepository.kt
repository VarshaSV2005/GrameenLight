package com.grameenlight.app.data

import com.grameenlight.app.model.Complaint
import com.grameenlight.app.model.Pole
import com.grameenlight.app.model.PoleStatus
import com.grameenlight.app.model.RepairStatus
import com.grameenlight.app.model.SampleData
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PoleRepository @Inject constructor() {

    private val _poles = MutableStateFlow(SampleData.poles.toList())
    val poles: StateFlow<List<Pole>> = _poles.asStateFlow()

    private val _complaints = MutableStateFlow(SampleData.complaints.toList())
    val complaints: StateFlow<List<Complaint>> = _complaints.asStateFlow()

    private var complaintCounter = 2842

    fun updatePoleStatus(poleId: String, newStatus: PoleStatus): String {
        val id = "CMP-${complaintCounter++}"
        val updatedPoles = _poles.value.map { pole ->
            if (pole.id == poleId) {
                pole.copy(
                    status = newStatus,
                    repairStatus = RepairStatus.PENDING,
                    complaintId = id,
                    lastReportedMillis = System.currentTimeMillis()
                )
            } else pole
        }
        _poles.value = updatedPoles

        val pole = updatedPoles.find { it.id == poleId }
        val newComplaint = Complaint(
            id = id,
            poleId = poleId,
            streetName = pole?.streetName ?: "",
            issue = newStatus.label,
            repairStatus = RepairStatus.PENDING,
            assignedTo = "—",
            timeLabel = "Just now"
        )
        _complaints.value = listOf(newComplaint) + _complaints.value
        return id
    }

    fun getStreets(): List<String> = _poles.value.map { it.streetName }.distinct()

    fun getPolesByStreet(street: String): List<Pole> =
        _poles.value.filter { it.streetName == street }
}
