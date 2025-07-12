package it.unibo.discoverit.ui.screens.categorydetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import it.unibo.discoverit.data.database.entities.PointOfInterest
import it.unibo.discoverit.data.repositories.PointOfInterestRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class CategoryDetailsState(
    val currentCategoryId: Long? = null,
    val poiList: List<PointOfInterest> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

interface CategoryDetailsActions {
    fun loadPOIs(categoryId: Long)
}

class CategoryDetailsViewModel(
    private val poiRepository: PointOfInterestRepository
) : ViewModel() {
    private val _state = MutableStateFlow(CategoryDetailsState())
    val state: StateFlow<CategoryDetailsState> = _state.asStateFlow()

    val actions = object : CategoryDetailsActions {
        override fun loadPOIs(categoryId: Long) {
            if (categoryId == _state.value.currentCategoryId) return

            _state.update { it.copy(currentCategoryId = categoryId, isLoading = true) }

            viewModelScope.launch {
                try {
                    val poiList = poiRepository.getPOIsByCategory(categoryId)
                    _state.update {
                        it.copy(poiList = poiList, isLoading = false)
                    }
                } catch (e: Exception) {
                    _state.update {
                        it.copy(error = e.message, isLoading = false)
                    }
                }
            }
        }
    }

}