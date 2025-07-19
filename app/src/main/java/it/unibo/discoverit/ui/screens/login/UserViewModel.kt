package it.unibo.discoverit.ui.screens.login

import android.net.Uri
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import it.unibo.discoverit.data.database.entities.User
import it.unibo.discoverit.data.repositories.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class UserState(
    val user: User? = null
)

class UserViewModel(private val userRepository: UserRepository) : ViewModel() {
    private val _userState = MutableStateFlow<UserState>(UserState())
    val userState: StateFlow<UserState> = _userState

    fun setUser(user: User) {
        _userState.update { it.copy(user = user) }
    }

    fun logout() {
        _userState.update { UserState() } // Resetta tutto
    }
}