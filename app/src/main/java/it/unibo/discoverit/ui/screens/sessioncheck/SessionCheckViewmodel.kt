package it.unibo.discoverit.ui.screens.sessioncheck

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import it.unibo.discoverit.ui.screens.login.UserViewModel
import it.unibo.discoverit.utils.accountservice.AccountService
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

enum class SessionCheckPhase {
    CHECKING,
    USER_LOGGED_IN,
    USER_NOT_LOGGED_IN,
    ERROR
}

data class SessionCheckState(
    val currentPhase: SessionCheckPhase = SessionCheckPhase.CHECKING,
    val errorMsg: String? = null
)

interface SessionCheckActions {

}

class SessionCheckViewModel(
    private val accountService: AccountService,
    private val userViewModel: UserViewModel
) : ViewModel() {
    private val _state = MutableStateFlow(SessionCheckState())
    val state: StateFlow<SessionCheckState> = _state

    val actions = object : SessionCheckActions {}

    init {
        checkSession()
    }

    private fun checkSession() {
        viewModelScope.launch {
            _state.update { it.copy(currentPhase = SessionCheckPhase.CHECKING) }
            try {
                val user = accountService.getCurrentUser()
                //delay(5000) //for testing
                if (user != null) {
                    userViewModel.setUser(user)
                    _state.update { it.copy(currentPhase = SessionCheckPhase.USER_LOGGED_IN) }
                } else {
                    _state.update { it.copy(currentPhase = SessionCheckPhase.USER_NOT_LOGGED_IN) }
                }
            } catch (e: Exception) {
                _state.update {
                    it.copy(
                        currentPhase = SessionCheckPhase.ERROR,
                        errorMsg = "Errore nel recupero della sessione"
                    )
                }
            }
        }
    }
}