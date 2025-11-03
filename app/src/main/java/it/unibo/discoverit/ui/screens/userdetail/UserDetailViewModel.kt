package it.unibo.discoverit.ui.screens.userdetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import it.unibo.discoverit.data.database.entities.Achievement
import it.unibo.discoverit.data.database.entities.UserAchievementProgress
import it.unibo.discoverit.data.repositories.AchievementRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class UserDetailState(
    val achievementsWithProgress: Map<Achievement, UserAchievementProgress?> = emptyMap(),  // Map with achievement and progress
    val isLoading: Boolean = false,
    val errorMsg: String? = null
)

interface UserDetailActions

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

        viewModelScope.launch {
            try {
                achievementRepository.getAchievementsWithProgress(userId).collect { achievements ->
                    _state.update { it.copy(achievementsWithProgress = achievements) }
                }
            } catch (e: Exception) {
                _state.update { it.copy(errorMsg = "Error loading completed: ${e.message}") }
            }
        }
    }

    val actions = object : UserDetailActions {

    }
}
