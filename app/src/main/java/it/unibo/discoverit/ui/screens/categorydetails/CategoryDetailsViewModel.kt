package it.unibo.discoverit.ui.screens.categorydetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import it.unibo.discoverit.data.database.entities.PointOfInterest
import it.unibo.discoverit.data.repositories.CategoryRepository
import it.unibo.discoverit.data.repositories.PointOfInterestRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * Represents the UI state of the category details screen.
 *
 * @property currentCategoryId The ID of the currently displayed category.
 * @property currentCategoryName The name of the currently displayed category.
 * @property poiList The list of points of interest in the category.
 * @property isLoading Whether data is currently being loaded.
 * @property error An error message if something went wrong, null otherwise.
 */
data class CategoryDetailsState(
    val currentCategoryId: Long? = null,
    val currentCategoryName: String = "Category Details",
    val poiList: List<PointOfInterest> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

/**
 * Available actions for the category details screen.
 */
interface CategoryDetailsActions {
    /**
     * Refreshes the list of points of interest.
     */
    fun onRefresh()
}

/**
 * ViewModel for the category details screen.
 * Manages the state and business logic for displaying points of interest in a category.
 *
 * @property poiRepository Repository for accessing point of interest data.
 * @property categoryRepository Repository for accessing category data.
 * @property selectedCategoryId The ID of the category to display.
 */
class CategoryDetailsViewModel(
    private val poiRepository: PointOfInterestRepository,
    private val categoryRepository: CategoryRepository,
    private val selectedCategoryId: Long
) : ViewModel() {

    private val _state = MutableStateFlow(CategoryDetailsState())
    val state: StateFlow<CategoryDetailsState> = _state.asStateFlow()

    init {
        loadCategoryName(selectedCategoryId)
        loadPOIs(selectedCategoryId)
    }

    val actions = object : CategoryDetailsActions {
        override fun onRefresh() {
            loadPOIs(selectedCategoryId)
        }
    }

    /**
     * Loads the name of the category from the repository.
     */
    private fun loadCategoryName(selectedCategoryId: Long) {
        viewModelScope.launch {
            try {
                _state.update { it.copy(isLoading = true) }
                val selectedCategoryName = categoryRepository.getCategoryName(selectedCategoryId)
                _state.update {
                    it.copy(
                        currentCategoryId = selectedCategoryId,
                        currentCategoryName = selectedCategoryName,
                    )
                }
            } catch (e: Exception) {
                _state.update { it.copy(error = e.message) }
            } finally {
                _state.update { it.copy(isLoading = false) }
            }
        }
    }

    /**
     * Loads the list of points of interest for the category.
     */
    private fun loadPOIs(categoryId: Long = selectedCategoryId) {
        viewModelScope.launch {
            try {
                _state.update { it.copy(isLoading = true) }
                val poiList = poiRepository.getPOIsByCategory(categoryId)
                _state.update { it.copy(poiList = poiList) }
            } catch (e: Exception) {
                _state.update { it.copy(error = e.message) }
            } finally {
                _state.update { it.copy(isLoading = false) }
            }
        }
    }
}