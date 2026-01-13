package it.unibo.discoverit.ui.screens.social

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import it.unibo.discoverit.data.database.entities.User
import it.unibo.discoverit.data.repositories.UserRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * Represents the state of the social screen.
 *
 * @property friendsAndCountCompleted A map of friends and the number of completed achievements.
 * @property currentUserCountCompleted The number of completed achievements of the current user.
 * @property isLoading Whether the screen is currently loading.
 * @property errorMsg The error message to be displayed, if any.
 * @property isAddFriendDialogVisible Whether the add friend dialog is visible.
 * @property usernameToAdd The username of the friend to be added.
 * @property showSnackbar Whether a snackbar should be shown.
 * @property snackbarMessage The message to be displayed in the snackbar, if any.
 * @property selectedFriendForRemoval The friend to be removed, if any.
 * @property showRemoveFriendDialog Whether the remove friend dialog is visible.
 */
data class SocialState(
    val friendsAndCountCompleted: Map<User, Long> = emptyMap(),
    val currentUserCountCompleted: Long = 0,
    val isLoading: Boolean = false,
    val errorMsg: String? = null,
    val isAddFriendDialogVisible: Boolean = false,
    val usernameToAdd: String = "",
    val showSnackbar: Boolean = false,
    val snackbarMessage: String? = null,    //This shouldnt be here, should create message/event system and let the UI handle it.
    val selectedFriendForRemoval: User? = null,
    val showRemoveFriendDialog: Boolean = false
)

interface SocialActions{
    fun onAddFriendClick()
    fun onDismissAddFriendDialog()
    fun onConfirmAddFriendDialog(username: String)
    fun onUsernameChange(username: String)
    fun onSnackbarDismiss()
    fun onFriendLongPress(friend: User)  // Long press su un amico
    fun onDismissRemoveFriendDialog()   // Chiudi dialog
    fun onConfirmRemoveFriend()         // Conferma rimozione
}

/**
 * ViewModel for the social screen.
 *
 * @property userRepository The repository for the user data.
 * @property currentUserId The ID of the current user.
 * @property state The state of the screen.
 * @property actions The actions that can be performed on the screen.
 */
class SocialViewModel(
    private val userRepository: UserRepository,
    private val currentUserId: Long
) : ViewModel() {
    private val _state = MutableStateFlow(SocialState())
    val state: StateFlow<SocialState> = _state.asStateFlow()

    // Load data when the ViewModel is created
    init {
        loadData()
    }

    /**
     * Loads the data for the screen.
     */
    private fun loadData() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            try {
                // Uses combine to combine the two flows and update the state accordingly.
                combine(
                    userRepository.getCountCompletedAchievements(currentUserId),
                    userRepository.getFriendsAndCountCompletedAchievements(currentUserId)
                ) { count, friendsMap ->
                    SocialState(
                        currentUserCountCompleted = count,
                        friendsAndCountCompleted = friendsMap,
                        isLoading = false
                    )
                }.collect { newData ->
                    // Update the state with the new data.
                    _state.update { currentState ->
                        currentState.copy(
                            currentUserCountCompleted = newData.currentUserCountCompleted,
                            friendsAndCountCompleted = newData.friendsAndCountCompleted,
                            isLoading = newData.isLoading
                        )
                    }
                }
            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        errorMsg = e.message,
                        isLoading = false
                    )
                }
            }
        }
    }

    val actions = object : SocialActions {
        override fun onAddFriendClick() {
            _state.update { it.copy(isAddFriendDialogVisible = true) }
        }

        override fun onDismissAddFriendDialog() {
            _state.update { it.copy(isAddFriendDialogVisible = false, usernameToAdd = "") }
        }

        override fun onConfirmAddFriendDialog(username: String) {
            _state.update { it.copy(isAddFriendDialogVisible = false) }
            viewModelScope.launch {
                try {
                    userRepository.addFriendship(currentUserId, username)
                    _state.update {
                        it.copy(
                            snackbarMessage = "Amico aggiunto con successo!",
                            showSnackbar = true,
                            usernameToAdd = ""
                        )
                    }
                    // Delay before hiding the snackbar
                    delay(3000)
                    _state.update { it.copy(showSnackbar = false) }
                } catch (e: Exception) {
                    _state.update {
                        it.copy(
                            snackbarMessage = "Errore: ${e.message}",
                            showSnackbar = true,
                            usernameToAdd = ""
                        )
                    }
                    delay(3000)
                    _state.update { it.copy(showSnackbar = false) }
                }
            }
        }

        override fun onUsernameChange(username: String) {
            _state.update { it.copy(usernameToAdd = username) }
        }

        override fun onSnackbarDismiss() {
            _state.update { it.copy(showSnackbar = false, snackbarMessage = null) }
        }

        override fun onFriendLongPress(friend: User) {
            _state.update { it.copy(selectedFriendForRemoval = friend, showRemoveFriendDialog = true) }
        }

        override fun onDismissRemoveFriendDialog() {
            _state.update { it.copy(showRemoveFriendDialog = false, selectedFriendForRemoval = null) }
        }

        override fun onConfirmRemoveFriend() {
            val friend = _state.value.selectedFriendForRemoval ?: return
            viewModelScope.launch {
                try {
                    userRepository.removeFriendship(currentUserId, friend.userId)
                    _state.update {
                        it.copy(
                            showRemoveFriendDialog = false,
                            selectedFriendForRemoval = null,
                            snackbarMessage = "${friend.username} rimosso dagli amici",
                            showSnackbar = true
                        )
                    }
                } catch (e: Exception) {
                    _state.update {
                        it.copy(
                            snackbarMessage = "Errore: ${e.message}",
                            showSnackbar = true
                        )
                    }
                }
            }
        }
    }
}