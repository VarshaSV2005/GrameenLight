package com.grameenlight.app.viewmodel

import android.location.Location
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.grameenlight.app.data.LocationService
import com.grameenlight.app.data.PoleRepository
import com.grameenlight.app.model.Complaint
import com.grameenlight.app.model.Pole
import com.grameenlight.app.model.PoleStatus
import com.grameenlight.app.utils.MapUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val locationService: LocationService,
    private val repository: PoleRepository
) : ViewModel() {

    val poles: StateFlow<List<Pole>> = repository.poles
    val complaints: StateFlow<List<Complaint>> = repository.complaints

    private val _userLocation = MutableStateFlow<Location?>(null)
    val userLocation: StateFlow<Location?> = _userLocation.asStateFlow()

    private val _locationError = MutableStateFlow<String?>(null)
    val locationError: StateFlow<String?> = _locationError.asStateFlow()

    private val _selectedPole = MutableStateFlow<Pole?>(null)
    val selectedPole: StateFlow<Pole?> = _selectedPole.asStateFlow()

    private val _filterStatus = MutableStateFlow<PoleStatus?>(null)
    val filterStatus: StateFlow<PoleStatus?> = _filterStatus.asStateFlow()

    val filteredPoles: StateFlow<List<Pole>> = poles
        .map { list ->
            val filter = _filterStatus.value
            if (filter == null) list else list.filter { it.status == filter }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), emptyList())

    val energySavedKwh: StateFlow<Int> = poles
        .map { list -> list.count { it.status == PoleStatus.BURNING_IN_DAY } * 6 + 300 }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), 342)

    // Report form state
    private val _selectedStreet = MutableStateFlow(repository.getStreets().firstOrNull() ?: "")
    val selectedStreet: StateFlow<String> = _selectedStreet.asStateFlow()

    private val _selectedPoleId = MutableStateFlow("")
    val selectedPoleId: StateFlow<String> = _selectedPoleId.asStateFlow()

    private val _selectedReportStatus = MutableStateFlow(PoleStatus.FUSED)
    val selectedReportStatus: StateFlow<PoleStatus> = _selectedReportStatus.asStateFlow()

    private val _reportNote = MutableStateFlow("")
    val reportNote: StateFlow<String> = _reportNote.asStateFlow()

    private val _lastComplaintId = MutableStateFlow<String?>(null)
    val lastComplaintId: StateFlow<String?> = _lastComplaintId.asStateFlow()

    fun getStreets() = repository.getStreets()
    fun getPolesByStreet(street: String) = repository.getPolesByStreet(street)

    fun setSelectedStreet(street: String) {
        _selectedStreet.value = street
        _selectedPoleId.value = ""
    }

    fun setSelectedPoleId(id: String) { _selectedPoleId.value = id }
    fun setReportStatus(s: PoleStatus) { _selectedReportStatus.value = s }
    fun setReportNote(n: String) { _reportNote.value = n }
    fun setFilter(s: PoleStatus?) { _filterStatus.value = s }
    fun selectPole(p: Pole?) { _selectedPole.value = p }
    fun clearComplaintId() { _lastComplaintId.value = null }

    fun submitReport() {
        val poleId = _selectedPoleId.value.ifEmpty { return }
        val id = repository.updatePoleStatus(poleId, _selectedReportStatus.value)
        _lastComplaintId.value = id
        _selectedPoleId.value = ""
        _reportNote.value = ""
    }

    fun startLocationTracking() {
        viewModelScope.launch {
            locationService.liveLocation()
                .catch { e -> _locationError.value = e.message }
                .collect { loc ->
                    _userLocation.value = loc
                    val (gx, gy) = MapUtils.latLngToGrid(loc.latitude, loc.longitude)
                }
        }
    }
}
