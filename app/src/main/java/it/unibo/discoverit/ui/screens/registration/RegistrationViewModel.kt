package it.unibo.discoverit.ui.screens.registration

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import it.unibo.discoverit.data.repositories.UserRepository
import it.unibo.discoverit.ui.screens.login.UserViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

enum class RegistrationPhase {
    IDLE,
    LOADING,
    SUCCESS,
}

data class RegistrationState(
    val username: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val currentPhase: RegistrationPhase = RegistrationPhase.IDLE,
    val error: String? = null
)

interface RegistrationActions {
    fun onUsernameChanged(username: String)
    fun onPasswordChanged(password: String)
    fun onConfirmPasswordChanged(password: String)
    fun onRegisterClicked()
}

class RegistrationViewModel(
    private val userRepository: UserRepository,
    private val userViewModel: UserViewModel
) : ViewModel() {
    private val _state = MutableStateFlow(RegistrationState())
    val state: StateFlow<RegistrationState> = _state

    val actions = object : RegistrationActions {
        override fun onUsernameChanged(username: String) {
            _state.update { it.copy(username = username) }
        }

        override fun onPasswordChanged(password: String) {
            _state.update { it.copy(password = password) }
        }

        override fun onConfirmPasswordChanged(password: String) {
            _state.update { it.copy(confirmPassword = password) }
        }

        override fun onRegisterClicked() {
            viewModelScope.launch {
                _state.update { it.copy(currentPhase = RegistrationPhase.LOADING, error = null) }
                try {
                    validateInputs()
                    userRepository.register(
                        username = _state.value.username,
                        plainPassword = _state.value.password
                    )
                    // Registrazione riuscita
                    val user = userRepository.login(username = _state.value.username, plainPassword = _state.value.password)
                    userViewModel.setUser(user)
                    _state.update { it.copy(currentPhase = RegistrationPhase.SUCCESS) }
                } catch (e: Exception) {
                    _state.update {
                        it.copy(
                            currentPhase = RegistrationPhase.IDLE,
                            error = e.message ?: "Registration failed"
                        )
                    }
                }
            }
        }
    }

    private fun validateInputs() {
        if (_state.value.password != _state.value.confirmPassword) {
            throw IllegalArgumentException("Passwords don't match")
        }
        if (_state.value.password.length < 8) {
            throw IllegalArgumentException("Password must be at least 8 characters")
        }
    }
}