package it.unibo.discoverit.ui.screens.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import it.unibo.discoverit.data.database.entities.CategoryStats
import it.unibo.discoverit.data.repositories.CategoryRepository
import it.unibo.discoverit.ui.screens.login.UserViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * Data class representing the state of the home screen.
 *
 * @property categories The list of categories with their statistics.
 * @property isLoading Whether the page is currently loading.
 * @property errorMsg The error message to be displayed, if any.
 */
data class HomeState(
    val categories: List<CategoryStats>,
    val isLoading: Boolean = false,
    val errorMsg: String? = null
)

/**
 * ViewModel for the home screen.
 *
 * @property categoryRepository The repository that handles category data.
 * @property userViewModel The view model saving the state of the logged-in user.
 */
class HomeViewModel(
    private val categoryRepository: CategoryRepository,
    private val userViewModel: UserViewModel
) :ViewModel() {
    private val _homeState = MutableStateFlow(HomeState(emptyList()))
    val homeState: StateFlow<HomeState> = _homeState.asStateFlow()

    /**
     * Initialize the state by loading the categories with the stats of the logged-in user.
     */
    init {
        viewModelScope.launch {
            // Observe the user state and load the categories when the user changes
            userViewModel.userState.collect { userState ->
                userState.user?.let { user ->
                    loadCategories(user.userId)
                }
            }
        }
    }

    /**
     * Loads the categories with the stats of the logged-in user.
     *
     * @param userId The ID of the logged-in user.
     */
    private fun loadCategories(userId: Long) {
        viewModelScope.launch {
            _homeState.update { it.copy(isLoading = true) }
            try {
                categoryRepository.getCategoriesWithStats(userId).collect { categories ->
                    _homeState.update {
                        it.copy(
                            categories = categories,
                            isLoading = false,
                            errorMsg = null
                        )
                    }
                }
            } catch (e: Exception) {
                _homeState.update {
                    it.copy(isLoading = false, errorMsg = e.message)
                }
            }
        }
    }
}