package it.unibo.discoverit.ui.screens.categorydetails

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import it.unibo.discoverit.data.database.entities.Category
import it.unibo.discoverit.data.database.entities.PointOfInterest
import it.unibo.discoverit.data.repositories.CategoryRepository
import it.unibo.discoverit.data.repositories.PointOfInterestRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class CategoryDetailsState(
    val currentCategoryId: Long? = null,
    val currentCategoryName: String = "Category Details",
    val poiList: List<PointOfInterest> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

interface CategoryDetailsActions {
    fun onRefresh()
}

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