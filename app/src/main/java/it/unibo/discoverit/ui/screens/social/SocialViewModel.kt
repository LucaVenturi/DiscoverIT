package it.unibo.discoverit.ui.screens.social

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import it.unibo.discoverit.data.database.entities.User
import it.unibo.discoverit.data.repositories.AchievementRepository
import it.unibo.discoverit.data.repositories.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

data class SocialState(
    val friendsAndCountCompleted: Map<User, Long> = emptyMap(),
    val currentUserCountCompleted: Long = 0,
    val isLoading: Boolean = false,
    val errorMsg: String? = null
)

interface SocialActions{
    fun onAddFriendClick()
    fun onUserClick()
}

class SocialViewModel(
    private val achievementRepository: AchievementRepository,
    private val userRepository: UserRepository,
    private val currentUserId: Long
) : ViewModel() {
    private val _state = MutableStateFlow(SocialState())
    val state: StateFlow<SocialState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            _state.value = _state.value.copy(isLoading = true)
            try {
                userRepository.getFriendsAndCountCompletedAchievements(currentUserId).collect { friendsAndCountCompleted ->
                    _state.value = _state.value.copy(friendsAndCountCompleted = friendsAndCountCompleted)
                }
                _state.value = _state.value.copy(isLoading = false)
            } catch (e: Exception) {
                _state.value = _state.value.copy(
                    errorMsg = e.message,
                    isLoading = false
                )
            }
        }
    }

    val actions = object : SocialActions {
        override fun onAddFriendClick() {
            TODO("Not yet implemented")
        }

        override fun onUserClick() {
            TODO("Not yet implemented")
        }
    }
}