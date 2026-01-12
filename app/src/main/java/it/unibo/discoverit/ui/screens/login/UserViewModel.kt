package it.unibo.discoverit.ui.screens.login

import androidx.lifecycle.ViewModel
import it.unibo.discoverit.data.database.entities.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

/**
 * Represents the state of the logged-in user.
 *
 * @property user The logged-in user, or null if no user is logged in.
 */
data class UserState(
    val user: User? = null
)

/**
 * ViewModel for the logged-in user.
 *
 * This ViewModel is responsible for managing the state of the logged-in user.
 * It provides a way for the screens to know who is the logged-in user.
 * It also provides methods to set the user and log out the user.
 *
 * @property userState The state of the logged-in user.
 */
class UserViewModel() : ViewModel() {
    private val _userState = MutableStateFlow(UserState())
    val userState: StateFlow<UserState> = _userState

    /**
     * Sets the logged-in user.
     *
     * @param user The logged-in user.
     */
    fun setUser(user: User) {
        _userState.update { it.copy(user = user) }
    }

    /**
     * Logs out the user by resetting the user state.
     */
    fun logout() {
        _userState.update { UserState() }
    }
}