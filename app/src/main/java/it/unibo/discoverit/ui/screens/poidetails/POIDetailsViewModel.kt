package it.unibo.discoverit.ui.screens.poidetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import it.unibo.discoverit.data.database.entities.PointOfInterest
import it.unibo.discoverit.data.repositories.PointOfInterestRepository
import it.unibo.discoverit.ui.screens.login.UserViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class POIDetailsState(
    val currentPoi: PointOfInterest? = null,
    val isVisited: Boolean = false,
    val isLoading: Boolean = false,
    val errorMsg: String? = null
)

interface POIDetailsActions {
    fun toggleVisit()
    fun dismissError()
    fun onRefresh()
}

class POIDetailsViewModel(
    private val poiRepository: PointOfInterestRepository,
    private val userViewModel: UserViewModel,
    private val selectedPoiId: Long
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