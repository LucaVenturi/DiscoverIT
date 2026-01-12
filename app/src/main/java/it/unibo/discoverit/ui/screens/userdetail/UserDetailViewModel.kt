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

/**
 * Represents the state of the user detail screen.
 *
 * @property achievementsWithProgress A map of achievements and their progress.
 * @property isLoading Whether the screen is currently loading.
 * @property errorMsg The error message to be displayed, if any.
 */
data class UserDetailState(
    val achievementsWithProgress: Map<Achievement, UserAchievementProgress?> = emptyMap(),  // Map with achievement and progress
    val isLoading: Boolean = false,
    val errorMsg: String? = null
)

/**
 * Actions that can be performed on the user detail screen.
 * Currently empty.
 */
interface UserDetailActions

/**
 * The viewmodel that manages the state of the user detail screen.
 *
 * @property userId The ID of the user to load the achievements for.
 * @property achievementRepository The repository for achievements.
 * @property state The current state of the screen.
 * @property actions The actions that can be performed on the screen.
 */
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
