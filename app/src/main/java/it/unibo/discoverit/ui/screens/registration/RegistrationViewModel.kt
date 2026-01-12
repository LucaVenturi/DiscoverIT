package it.unibo.discoverit.ui.screens.registration

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import it.unibo.discoverit.ui.screens.login.UserViewModel
import it.unibo.discoverit.utils.accountservice.AccountService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * Represents the current phase of the registration process.
 *
 * [RegistrationPhase.IDLE] indicates that the registration process is not in progress.
 * [RegistrationPhase.LOADING] indicates that the registration process is in progress.
 * [RegistrationPhase.SUCCESS] indicates that the registration process was successful.
 */
enum class RegistrationPhase {
    IDLE,
    LOADING,
    SUCCESS,
}

/**
 * Represents the state of the registration screen.
 *
 * @property username The username entered by the user.
 * @property password The password entered by the user.
 * @property confirmPassword The password entered by the user again.
 * @property currentPhase The current phase of the registration process.
 * @property error The error message to be displayed, if any.
 * @property isLoading Whether the registration is currently in progress.
 * @property isFormValid Whether the form is valid.
 */
data class RegistrationState(
    val username: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val currentPhase: RegistrationPhase = RegistrationPhase.IDLE,
    val error: String? = null
) {
    val isLoading: Boolean
        get() = currentPhase == RegistrationPhase.LOADING

    val isFormValid: Boolean
        get() = username.isNotBlank() &&
                password.length >= 8 &&
                confirmPassword.length >= 8 &&
                password == confirmPassword
}

/**
 * Represents the actions that can be performed on the registration screen.
 *
 * @property onUsernameChanged Called when the username is changed.
 * @property onPasswordChanged Called when the password is changed.
 * @property onConfirmPasswordChanged Called when the confirm password is changed.
 * @property onRegisterClicked Called when the register button is clicked.
 */
interface RegistrationActions {
    fun onUsernameChanged(username: String)
    fun onPasswordChanged(password: String)
    fun onConfirmPasswordChanged(password: String)
    fun onRegisterClicked()
}

/**
 * ViewModel for the registration screen.
 *
 * @property accountService The service for managing user accounts.
 * @property userViewModel The view model saving the state of the logged-in user.
 * @property state The state of the screen.
 * @property actions The actions that can be performed on the screen.
 */
class RegistrationViewModel(
    private val accountService: AccountService,
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
                    val user = accountService.register(username = _state.value.username, password = _state.value.password)
                    // Sets the logged-in user in the view model to the one
                    // returned by the account service.
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

    /**
     * Validates the inputs of the registration form.
     *
     * @throws IllegalArgumentException if the passwords don't match
     * or if the password is less than 8 characters.
     */
    private fun validateInputs() {
        if (_state.value.password != _state.value.confirmPassword) {
            throw IllegalArgumentException("Passwords don't match")
        }
        if (_state.value.password.length < 8) {
            throw IllegalArgumentException("Password must be at least 8 characters")
        }
    }
}