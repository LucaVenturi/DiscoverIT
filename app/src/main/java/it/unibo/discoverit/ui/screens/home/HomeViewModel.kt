package it.unibo.discoverit.ui.screens.home

import android.view.View
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

data class HomeState(
    val categories: List<CategoryStats>,
    val isLoading: Boolean = false,
    val errorMsg: String? = null
)

class HomeViewModel(
    private val categoryRepository: CategoryRepository,
    private val userViewModel: UserViewModel
) :ViewModel() {
    private val _homeState = MutableStateFlow(HomeState(emptyList()))
    val homeState: StateFlow<HomeState> = _homeState.asStateFlow()


    init {
        viewModelScope.launch {
            _homeState.update { it.copy(isLoading = true) }
            try {
                val userId = userViewModel.userState.value.user
                    ?: throw IllegalArgumentException("User not logged in")
                categoryRepository.getCategoriesWithStats(userId.userId).collect { categories ->
                    _homeState.update { it.copy(categories = categories, isLoading = false) }
                }
            } catch (e: Exception) {
                _homeState.update { it.copy(isLoading = false, errorMsg = e.message) }
            }
        }
    }

}