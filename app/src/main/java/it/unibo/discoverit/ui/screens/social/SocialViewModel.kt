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

data class SocialState(
    val friendsAndCountCompleted: Map<User, Long> = emptyMap(),
    val currentUserCountCompleted: Long = 0,
    val isLoading: Boolean = false,
    val errorMsg: String? = null,
    val isAddFriendDialogVisible: Boolean = false,
    val usernameToAdd: String = "",
    val showSnackbar: Boolean = false,
    val snackbarMessage: String? = null,
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

class SocialViewModel(
    private val userRepository: UserRepository,
    private val currentUserId: Long
) : ViewModel() {
    private val _state = MutableStateFlow(SocialState())
    val state: StateFlow<SocialState> = _state.asStateFlow()

    init {
        Log.d("SocialViewModel", "Initializing with currentUserId: $currentUserId")
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            _state.update { it.copy(isLoading = true) }
            try {
                // Usa combine per raccogliere entrambi i Flow
                combine(
                    userRepository.getCountCompletedAchievements(currentUserId),
                    userRepository.getFriendsAndCountCompletedAchievements(currentUserId)
                ) { count, friendsMap ->
                    Log.d("SocialViewModel", "Data updated - count: $count, friends: ${friendsMap.size}")
                    SocialState(
                        currentUserCountCompleted = count,
                        friendsAndCountCompleted = friendsMap,
                        isLoading = false
                    )
                }.collect { newData ->
                    // Aggiorna lo stato mantenendo i valori degli altri campi
                    _state.update { currentState ->
                        currentState.copy(
                            currentUserCountCompleted = newData.currentUserCountCompleted,
                            friendsAndCountCompleted = newData.friendsAndCountCompleted,
                            isLoading = newData.isLoading
                        )
                    }
                }
            } catch (e: Exception) {
                Log.e("SocialViewModel", "Error fetching data", e)
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
                    // Chiudi automaticamente dopo 3 secondi
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