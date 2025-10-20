package it.unibo.discoverit.ui.screens.login

import androidx.lifecycle.ViewModel
import it.unibo.discoverit.data.database.entities.User
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update

data class UserState(
    val user: User? = null
)

class UserViewModel() : ViewModel() {
    private val _userState = MutableStateFlow(UserState())
    val userState: StateFlow<UserState> = _userState

    fun setUser(user: User) {
        _userState.update { it.copy(user = user) }
    }

    fun logout() {
        _userState.update { UserState() }
    }
}