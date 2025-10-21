package it.unibo.discoverit.ui.screens.login

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import it.unibo.discoverit.utils.accountservice.AccountService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

enum class LoginPhase {
    SUCCESS,
    LOADING,
    IDLE
}

data class LoginState(
    val username: String = "",
    val password: String = "",
    val currentPhase: LoginPhase = LoginPhase.IDLE,
    val errorMsg: String? = null,
)

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
                    val user = accountService.login(
                        username = _loginState.value.username,
                        password = _loginState.value.password
                    )
                    userViewModel.setUser(user)
                    _loginState.update { it.copy(currentPhase = LoginPhase.SUCCESS) }
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