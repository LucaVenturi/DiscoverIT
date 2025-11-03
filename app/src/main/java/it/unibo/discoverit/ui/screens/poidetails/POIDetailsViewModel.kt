package it.unibo.discoverit.ui.screens.poidetails

import android.location.Location
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import it.unibo.discoverit.data.database.entities.PointOfInterest
import it.unibo.discoverit.data.repositories.AchievementRepository
import it.unibo.discoverit.data.repositories.PointOfInterestRepository
import it.unibo.discoverit.ui.screens.login.UserViewModel
import it.unibo.discoverit.utils.location.LocationService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class POIDetailsState(
    val currentPoi: PointOfInterest? = null,
    val isVisited: Boolean = false,
    val isLoading: Boolean = false,
    val errorMsg: String? = null,
    val isLocationLoading: Boolean = false,
    val distanceToPOI: Float? = null,
    val locationError: LocationError? = null,
    val permissionError: PermissionError? = null,
    val showOutOfRangeMessage: Boolean = false
) {
    val isUserInRange: Boolean
        get() = distanceToPOI != null && distanceToPOI <= 50f
}

interface POIDetailsActions {
    fun toggleVisit()
    fun dismissError()
    fun onRefresh()
    fun onGPSUse()
    fun onPermanentlyDenied()
    fun onDenied()
    fun onDismissLocationError()
    fun onDismissPermissionError()
}

sealed class LocationError {
    data object GpsDisabled : LocationError()
    data class GenericError(val message: String) : LocationError()
}

sealed class PermissionError {
    data object PermanentlyDenied : PermissionError()
    data object Denied : PermissionError()
}

class POIDetailsViewModel(
    private val poiRepository: PointOfInterestRepository,
    private val userViewModel: UserViewModel,
    private val selectedPoiId: Long,
    private val locationService: LocationService,
    private val achievementRepository: AchievementRepository
) : ViewModel() {
    private val _state = MutableStateFlow(POIDetailsState())
    val state: StateFlow<POIDetailsState> = _state.asStateFlow()

    init {
        loadPOI()
    }

    val actions = object : POIDetailsActions {
        override fun toggleVisit() {
            viewModelScope.launch {
                try {
                    val userId = userViewModel.userState.value.user?.userId
                        ?: throw Exception("Utente non loggato")
                    poiRepository.toggleVisit(userId, selectedPoiId)
                    // Recupera la categoria del POI
                    val poi = poiRepository.getPOIDetails(selectedPoiId)
                    val categoryId = poi?.categoryId ?: return@launch
                    achievementRepository.updateAchievementsProgressForUser(userId, categoryId)
                    _state.update { currentState ->
                        currentState.copy(
                            isVisited = !currentState.isVisited
                        )
                    }
                } catch (e: Exception) {
                    _state.update {
                        it.copy(errorMsg = e.message ?: "Errore durante l'aggiornamento")
                    }
                }
            }
        }

        override fun dismissError() {
            _state.update { it.copy(errorMsg = null) }
        }

        override fun onRefresh() {
            loadPOI()
        }

        override fun onGPSUse() {
            Log.d("POIDetailsViewModel", "onGPSUse called")
            _state.update { it.copy(isLocationLoading = true) }
            viewModelScope.launch {
                try {
                    val location = locationService.getCurrentLocation(usePreciseLocation = true)
                        ?: throw Exception("Impossibile ottenere la posizione")
                    Log.d("POIDetailsViewModel", "Location received: $location")

                    val currentPoi = state.value.currentPoi ?: throw Exception("POI non trovato")
                    Log.d("POIDetailsViewModel", "POI received: $currentPoi")

                    val distanceArray = FloatArray(1)
                    Location.distanceBetween(
                        location.latitude,
                        location.longitude,
                        currentPoi.latitude,
                        currentPoi.longitude,
                        distanceArray
                    )
                    Log.d("POIDetailsViewModel", "Distance calculated: ${distanceArray[0]}")

                    _state.update { it.copy(distanceToPOI = distanceArray[0]) }

                    if (_state.value.isUserInRange && !_state.value.isVisited) {
                        _state.update { it.copy(showOutOfRangeMessage = false) }
                        toggleVisit()
                        Log.d("POIDetailsViewModel", "Visit toggled")
                    } else {
                        _state.update { it.copy(showOutOfRangeMessage = true) }
                        Log.d("POIDetailsViewModel", "Out of range message shown")
                    }
                } catch (e: IllegalStateException) {
                    Log.e("POIDetailsViewModel", "IllegalStateException: ${e.message}")
                    _state.update { it.copy(locationError = LocationError.GpsDisabled) }
                }catch (e: SecurityException) {
                    Log.e("POIDetailsViewModel", "SecurityException: ${e.message}")
                } finally {
                    _state.update { it.copy(isLocationLoading = false) }
                    Log.d("POIDetailsViewModel", "onGPSUse finished")
                }
            }
        }

        override fun onPermanentlyDenied() {
            _state.update { it.copy(permissionError = PermissionError.PermanentlyDenied) }
        }

        override fun onDenied() {
            _state.update { it.copy(permissionError = PermissionError.Denied) }
        }

        override fun onDismissLocationError() {
            _state.update { it.copy(locationError = null) }
        }

        override fun onDismissPermissionError() {
            _state.update { it.copy(permissionError = null) }
        }
    }

    private fun loadPOI() {
        _state.update { it.copy(isLoading = true, errorMsg = null) }

        viewModelScope.launch {
            try {
                val poi = poiRepository.getPOIDetails(selectedPoiId)
                    ?: throw Exception("POI non trovato")
                val userId = userViewModel.userState.value.user?.userId
                    ?: throw Exception("Utente non loggato")
                val isVisited = poiRepository.isVisited(userId, selectedPoiId)

                _state.update {
                    it.copy(
                        currentPoi = poi,
                        isVisited = isVisited,
                        isLoading = false
                    )
                }
            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        errorMsg = e.message ?: "Errore di caricamento",
                        isLoading = false
                    )
                }
            }
        }
    }
}