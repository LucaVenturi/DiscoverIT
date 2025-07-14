package it.unibo.discoverit.ui.screens.userdetail

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import it.unibo.discoverit.data.database.entities.Achievement
import it.unibo.discoverit.data.database.entities.User
import it.unibo.discoverit.data.repositories.AchievementRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class UserDetailState(
    val completedAchievements: List<Achievement> = emptyList(),
    val toDoAchievements: List<Achievement> = emptyList(),
    val isLoading: Boolean = false,
    val errorMsg: String? = null
)

interface UserDetailActions {

}

class UserDetailViewModel(
    private val userId: Long,
    private val achievementRepository: AchievementRepository,
): ViewModel() {
    private val _state = MutableStateFlow(UserDetailState())
    val state: StateFlow<UserDetailState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
        }

        // Lancio due coroutine separate
        // una per ottenere i achievements completati
        viewModelScope.launch {
            try {
                achievementRepository.getCompletedAchievements(userId).collect { completed ->
                    _state.update { it.copy(completedAchievements = completed) }
                }
            } catch (e: Exception) {
                _state.update { it.copy(errorMsg = "Error loading completed: ${e.message}") }
            }
        }
        // e l'altra per ottenere i achievements da fare
        viewModelScope.launch {
            try {
                achievementRepository.getToDoAchievements(userId).collect { toDo ->
                    _state.update { it.copy(toDoAchievements = toDo) }
                }
            } catch (e: Exception) {
                _state.update { it.copy(errorMsg = "Error loading to-do: ${e.message}") }
            } finally {
                _state.update { it.copy(isLoading = false) }
            }
        }
    }

    val actions = object : UserDetailActions {

    }
}
