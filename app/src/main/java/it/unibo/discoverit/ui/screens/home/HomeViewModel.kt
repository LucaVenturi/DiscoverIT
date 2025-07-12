package it.unibo.discoverit.ui.screens.home

import android.util.Log
import android.view.View
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import it.unibo.discoverit.data.database.entities.CategoryStats
import it.unibo.discoverit.data.repositories.CategoryRepository
import it.unibo.discoverit.ui.screens.login.UserState
import it.unibo.discoverit.ui.screens.login.UserViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.mapNotNull
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
            // Osserva direttamente lo StateFlow dell'UserViewModel
            userViewModel.userState.collect { userState ->
                Log.d("HOME_VM", "User state updated: ${userState.user?.username}")
                userState.user?.let { user ->
                    loadCategories(user.userId)
                }
            }
        }
    }

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