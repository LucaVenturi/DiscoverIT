package it.unibo.discoverit.ui.screens.sessioncheck

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import it.unibo.discoverit.data.database.entities.User
import it.unibo.discoverit.data.repositories.SettingsRepository
import it.unibo.discoverit.ui.screens.login.UserViewModel
import it.unibo.discoverit.utils.accountservice.AccountService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * Enum class representing the different phases of the session check process.
 *
 * @property CHECKING Indicates that the session check is currently in progress.
 * @property USER_LOGGED_IN Indicates that the user is logged in.
 * @property USER_NOT_LOGGED_IN Indicates that the user is not logged in.
 * @property ERROR Indicates that an error occurred during the session check.
 * @property BIOMETRIC_REQUIRED Indicates that biometric authentication is required.
 */
enum class SessionCheckPhase {
    CHECKING,
    USER_LOGGED_IN,
    USER_NOT_LOGGED_IN,
    ERROR,
    BIOMETRIC_REQUIRED
}

/**
 * Data class representing the state of the session check screen.
 *
 * @property currentPhase The current phase of the session check process.
 * @property user The user that is currently logged in.
 * @property errorMsg The error message to be displayed, if any.
 */
data class SessionCheckState(
    val currentPhase: SessionCheckPhase = SessionCheckPhase.CHECKING,
    val user: User? = null,
    val errorMsg: String? = null
)

/**
 * Interface for the actions that can be performed on the session check screen.
 *
 * @property onBiometricSuccess Called when biometric authentication is successful.
 */
interface SessionCheckActions {
    fun onBiometricSuccess()
}

/**
 * ViewModel for the session check screen.
 *
 * @property accountService The service for managing user accounts.
 * @property userViewModel The view model saving the state of the logged-in user.
 * @property settingsRepository The repository for the settings data.
 * @property state The state of the screen.
 * @property actions The actions that can be performed on the screen.
 */
class SessionCheckViewModel(
    private val accountService: AccountService,
    private val userViewModel: UserViewModel,
    private val settingsRepository: SettingsRepository
) : ViewModel() {

    private val _state = MutableStateFlow(SessionCheckState())
    val state: StateFlow<SessionCheckState> = _state

    val actions = object : SessionCheckActions {
        /**
         * Called when biometric authentication is successful.
         * Sets the user in the view model and updates the state to indicate
         * that the user is logged in.
         */
        override fun onBiometricSuccess() {
            _state.update { current ->
                current.user?.let { user ->
                    userViewModel.setUser(user) // solo dopo biometria
                    current.copy(currentPhase = SessionCheckPhase.USER_LOGGED_IN)
                } ?: current
            }
        }
    }

    // Check the session when the ViewModel is created.
    init {
        checkSession()
    }

    /**
     * Checks the session of the user.
     * If the user is not logged in, navigates to the login screen.
     * If the user is logged in, checks if biometric authentication is enabled.
     * If it is, asks for biometric authentication.
     * If it is not, logs the user in.
     */
    private fun checkSession() {
        viewModelScope.launch {
            _state.update { it.copy(currentPhase = SessionCheckPhase.CHECKING) }

            try {
                // Retrive the user and the biometric login status from the account service.
                val user = accountService.getCurrentUser()
                val biometricEnabled = settingsRepository.biometricLoginEnabled.first()

                // Set the new phase based on the user and the biometric login status.
                val newPhase = when {
                    user == null -> SessionCheckPhase.USER_NOT_LOGGED_IN
                    biometricEnabled -> SessionCheckPhase.BIOMETRIC_REQUIRED
                    else -> {
                        // If biometric authentication is not enabled, log the user in.
                        userViewModel.setUser(user)
                        SessionCheckPhase.USER_LOGGED_IN
                    }
                }

                // Update the state with the new phase and the user.
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
