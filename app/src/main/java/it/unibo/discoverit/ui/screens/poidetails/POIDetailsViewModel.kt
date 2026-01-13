package it.unibo.discoverit.ui.screens.poidetails

import android.location.Location
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

/**
 * Represents the state of the POI details screen.
 *
 * @property currentPoi The current POI being displayed.
 * @property isVisited Whether the current POI has been visited by the user.
 * @property isLoading Whether the screen is currently loading.
 * @property errorMsg The error message to be displayed, if any.
 * @property isLocationLoading Whether the location is currently being loaded.
 * @property distanceToPOI The distance between the current POI and the user's location.
 * @property locationError The error that occurred while getting the location.
 * @property permissionError The error that occurred while getting the permission.
 * @property showOutOfRangeMessage Whether to show the out of range message.
 * @property isUserInRange Whether the user is in range of the POI.
 */
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
        get() = distanceToPOI != null && distanceToPOI <= 50f // Must be within 50 meters.
}

/**
 * Represents the actions that can be performed on the POI details screen.
 */
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

/**
 * Represents the error that occurred while getting the location.
 */
sealed class LocationError {
    data object GpsDisabled : LocationError()
    data class GenericError(val message: String) : LocationError()
}

/**
 * Represents the error that occurred while getting the permissions.
 */
sealed class PermissionError {
    data object PermanentlyDenied : PermissionError()
    data object Denied : PermissionError()
}

/**
 * ViewModel for the POI details screen.
 *
 * @property poiRepository The repository for the POI data.
 * @property userViewModel The view model saving the state of the logged-in user.
 * @property selectedPoiId The ID of the selected POI.
 * @property locationService The service for getting the location.
 * @property achievementRepository The repository for the achievements data.
 * @property actions The actions that can be performed on the screen.
 * @property state The state of the screen.
 */
class POIDetailsViewModel(
    private val poiRepository: PointOfInterestRepository,
    private val userViewModel: UserViewModel,
    private val selectedPoiId: Long,
    private val locationService: LocationService,
    private val achievementRepository: AchievementRepository
) : ViewModel() {
    private val _state = MutableStateFlow(POIDetailsState())
    val state: StateFlow<POIDetailsState> = _state.asStateFlow()

    // Load the POI when the ViewModel is created.
    init {
        loadPOI()
    }

    val actions = object : POIDetailsActions {
        override fun toggleVisit() {
            viewModelScope.launch {
                try {
                    // Check if the user is logged in.
                    val userId = userViewModel.userState.value.user?.userId
                        ?: throw Exception("User not logged in")

                    // Toggle the visit status of the POI for the user.
                    poiRepository.toggleVisit(userId, selectedPoiId)

                    // Get the complete POI.
                    val poi = poiRepository.getPOIDetails(selectedPoiId)
                    // Get the category ID of the POI.
                    val categoryId = poi?.categoryId ?: throw Exception("POI not found")

                    // Update the achievements progress for the user.
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
            _state.update { it.copy(isLocationLoading = true) }
            viewModelScope.launch {
                try {
                    // Get the current location.
                    val location = locationService.getCurrentLocation(usePreciseLocation = false)
                        ?: throw Exception("Impossibile ottenere la posizione")

                    // get the current POI.
                    val currentPoi = state.value.currentPoi
                        ?: throw Exception("POI non trovato")

                    // Calculate the distance between the current location and the POI.
                    val distanceArray = FloatArray(1)
                    Location.distanceBetween(
                        location.latitude,
                        location.longitude,
                        currentPoi.latitude,
                        currentPoi.longitude,
                        distanceArray
                    )

                    _state.update { it.copy(distanceToPOI = distanceArray[0]) }

                    if (_state.value.isUserInRange && !_state.value.isVisited) {
                        _state.update { it.copy(showOutOfRangeMessage = false) }
                        toggleVisit()
                    } else {
                        _state.update { it.copy(showOutOfRangeMessage = true) }
                    }
                } catch (e: IllegalStateException) {
                    _state.update { it.copy(locationError = LocationError.GpsDisabled) }
                }catch (e: SecurityException) {

                } finally {
                    _state.update { it.copy(isLocationLoading = false) }
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
                    ?: throw Exception("POI not found")
                val userId = userViewModel.userState.value.user?.userId
                    ?: throw Exception("User not logged in")
                val isVisited = poiRepository.isVisited(userId, selectedPoiId)

                _state.update {
                    it.copy(
                        currentPoi = poi,
                        isVisited = isVisited,
                    )
                }
            } catch (e: Exception) {
                _state.update {
                    it.copy(errorMsg = e.message ?: "Error loading POI")
                }
            } finally {
                _state.update { it.copy(isLoading = false) }
            }
        }
    }
}