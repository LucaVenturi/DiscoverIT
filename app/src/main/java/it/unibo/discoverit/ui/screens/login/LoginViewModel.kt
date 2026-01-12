package it.unibo.discoverit.ui.screens.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import it.unibo.discoverit.utils.accountservice.AccountService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * Represents the current phase of the login process.
 * [LoginPhase.SUCCESS] indicates that the login was successful.
 * [LoginPhase.LOADING] indicates that the login is currently in progress.
 * [LoginPhase.IDLE] indicates that the there is no login in progress.
 */
enum class LoginPhase {
    SUCCESS,
    LOADING,
    IDLE
}

/**
 * Represents the state of the login screen.
 *
 * @property username The username entered by the user.
 * @property password The password entered by the user.
 * @property currentPhase The current phase of the login process.
 * @property errorMsg The error message to be displayed, if any.
 */
data class LoginState(
    val username: String = "",
    val password: String = "",
    val currentPhase: LoginPhase = LoginPhase.IDLE,
    val errorMsg: String? = null,
)

/**
 * Represents the actions that can be performed on the login screen.
 *
 * @property onUsernameChanged Called when the username is changed.
 * @property onPasswordChanged Called when the password is changed.
 * @property onLoginClicked Called when the login button is clicked.
 */
interface LoginActions{
    fun onUsernameChanged(username: String)
    fun onPasswordChanged(password: String)
    fun onLoginClicked()
}

class LoginViewModel(
    private val accountService: AccountService,
    private val userViewModel: UserViewModel
) : ViewModel() {
    private val _loginState = MutableStateFlow(LoginState())
    val loginState: StateFlow<LoginState> = _loginState

    val loginActions = object : LoginActions {
        override fun onUsernameChanged(username: String) {
            _loginState.update { it.copy(username = username) }
        }

        override fun onPasswordChanged(password: String) {
            _loginState.update { it.copy(password = password) }
        }

        override fun onLoginClicked() {
            viewModelScope.launch {
                _loginState.update { it.copy(currentPhase = LoginPhase.LOADING, errorMsg = null) }
                try {
                    // Perform the login
                    val user = accountService.login(
                        username = _loginState.value.username,
                        password = _loginState.value.password
                    )
                    // Set the user in the view model
                    userViewModel.setUser(user)
                    // Update the state
                    _loginState.update { it.copy(currentPhase = LoginPhase.SUCCESS, errorMsg = null) }
                } catch (e: Exception) {
                    _loginState.update {
                        it.copy(
                            currentPhase = LoginPhase.IDLE,
                            errorMsg = e.message ?: "Login failed"
                        )
                    }
                }
            }
        }
    }
}