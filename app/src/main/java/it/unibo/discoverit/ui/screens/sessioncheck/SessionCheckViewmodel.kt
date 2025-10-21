package it.unibo.discoverit.ui.screens.sessioncheck

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import it.unibo.discoverit.data.database.entities.User
import it.unibo.discoverit.data.repositories.SettingsRepository
import it.unibo.discoverit.ui.screens.login.UserViewModel
import it.unibo.discoverit.utils.accountservice.AccountService
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

enum class SessionCheckPhase {
    CHECKING,
    USER_LOGGED_IN,
    USER_NOT_LOGGED_IN,
    ERROR,
    BIOMETRIC_REQUIRED
}

data class SessionCheckState(
    val currentPhase: SessionCheckPhase = SessionCheckPhase.CHECKING,
    val user: User? = null,
    val errorMsg: String? = null
)

interface SessionCheckActions {
    fun onBiometricSuccess()
}

class SessionCheckViewModel(
    private val accountService: AccountService,
    private val userViewModel: UserViewModel,
    private val settingsRepository: SettingsRepository
) : ViewModel() {

    private val _state = MutableStateFlow(SessionCheckState())
    val state: StateFlow<SessionCheckState> = _state

    // Action chiamata dalla View per notificare al ViewModel che l'utente ha completato l'autenticazione biometrica
    val actions = object : SessionCheckActions {
        override fun onBiometricSuccess() {
            _state.update { current ->
                current.user?.let { user ->
                    userViewModel.setUser(user) // solo dopo biometria
                    current.copy(currentPhase = SessionCheckPhase.USER_LOGGED_IN)
                } ?: current
            }
        }
    }

    init {
        checkSession()
    }

    private fun checkSession() {
        viewModelScope.launch {
            _state.update { it.copy(currentPhase = SessionCheckPhase.CHECKING) }

            try {
                val user = accountService.getCurrentUser()
                val biometricEnabled = settingsRepository.biometricLoginEnabled.first()
                Log.e("SessionCheckViewModel", "User: $user, Biometric: $biometricEnabled")

                val newPhase = when {
                    user == null -> SessionCheckPhase.USER_NOT_LOGGED_IN
                    biometricEnabled -> SessionCheckPhase.BIOMETRIC_REQUIRED
                    else -> {
                        // Imposta subito lo user nel ViewModel se non serve biometria
                        userViewModel.setUser(user)
                        SessionCheckPhase.USER_LOGGED_IN
                    }
                }

                Log.e("SessionCheckViewModel", "New phase: $newPhase")


                // Aggiorna lo state con l'user e la fase
                _state.update { it.copy(currentPhase = newPhase, user = user) }

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
