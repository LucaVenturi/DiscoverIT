package it.unibo.discoverit.ui.screens.account

import android.graphics.Bitmap
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import it.unibo.discoverit.data.repositories.AccountSettingsRepository
import it.unibo.discoverit.data.repositories.UserRepository
import it.unibo.discoverit.ui.screens.login.UserViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class AccountSettingsState(
    val userId: Long? = null,
    val username: String,
    val isLoading: Boolean = false,
    val errorMsg: String? = null,
    val isUsernameChanged: Boolean = false,
    val showImageSourceDialog: Boolean = false,
    val showLogoutDialog: Boolean = false,
    val showDeleteAccountDialog: Boolean = false
)

interface AccountSettingsActions {
    fun onUsernameChange(username: String)
    fun onChangeProfilePicClick()
    fun onDismissImageSourceDialog()
    fun onPickFromGallery()
    fun onTakePhoto()
    fun onImagePicked(bitmap: Bitmap)
    fun onSaveClick()
    fun onLogoutClick()
    fun onDeleteAccountClick()
    fun onLogoutConfirmation()
    fun onDeleteAccountConfirmation()
    fun onLogoutDismiss()
    fun onDeleteAccountDismiss()
}

class AccountSettingsViewModel(
    private val userViewModel: UserViewModel,
    userRepository: UserRepository,
    private val accountSettingsRepository: AccountSettingsRepository
) : ViewModel() {
    private val _state = MutableStateFlow(AccountSettingsState(username = ""))
    val state: StateFlow<AccountSettingsState> = _state.asStateFlow()

    init {
        viewModelScope.launch {
            userViewModel.userState.collect { userState ->
                userState.user?.let { user ->
                    _state.update {
                        it.copy(
                            userId = user.userId,
                            username = user.username,
                        )
                    }
                }
            }
        }
    }

    val actions = object : AccountSettingsActions {
        override fun onUsernameChange(username: String) {
            _state.update {
                it.copy(
                    username = username,
                    isUsernameChanged = username != userViewModel.userState.value.user?.username,
                )
            }
        }

        override fun onChangeProfilePicClick() {
            _state.update { it.copy(showImageSourceDialog = true) }
        }

        override fun onDismissImageSourceDialog() {
            _state.update { it.copy(showImageSourceDialog = false) }
        }

        override fun onPickFromGallery() {
            _state.update { it.copy(showImageSourceDialog = false) }
        }

        override fun onTakePhoto() {
            _state.update { it.copy(showImageSourceDialog = false) }
        }

        override fun onImagePicked(bitmap: Bitmap) {
            viewModelScope.launch {
                try {
                    _state.update { it.copy(isLoading = true) }

                    val userId = _state.value.userId ?: throw IllegalStateException("No logged-in user")

                    // Aggiorna l'immagine nel repository e recupera l'utente aggiornato
                    val updatedUser = accountSettingsRepository.updateProfilePicture(userId, bitmap)
                    userViewModel.setUser(updatedUser)
                } catch (e: Exception) {
                    _state.update { it.copy(errorMsg = e.message ?: "Errore durante l'aggiornamento della foto") }
                } finally {
                    _state.update { it.copy(isLoading = false) }
                }
            }
        }

        override fun onSaveClick() {
            viewModelScope.launch {
                _state.update { it.copy(isLoading = true) }
                try {
                    val user = userRepository.getUserById(_state.value.userId ?: throw IllegalStateException("No logged-in user"))
                    val newUser = user.copy(username = _state.value.username)
                    accountSettingsRepository.changeUsername(newUser.userId, newUser.username)

                    // Aggiorna anche il UserViewModel con i nuovi dati
                    userViewModel.setUser(newUser)

                    // Aggiorna lo stato locale per riflettere che l'username non è più "changed"
                    _state.update {
                        it.copy(
                            isUsernameChanged = false,
                            errorMsg = null
                        )
                    }
                } catch (e: Exception) {
                    _state.update { it.copy(errorMsg = e.message ?: "Unknown error") }
                }
                _state.update { it.copy(isLoading = false) }
            }
        }

        override fun onLogoutClick() {
            _state.update { it.copy(showLogoutDialog = true) }
        }

        override fun onDeleteAccountClick() {
            _state.update { it.copy(showDeleteAccountDialog = true) }
        }

        override fun onLogoutConfirmation() {
            _state.update { it.copy(showLogoutDialog = false) }
            userViewModel.logout()
        }

        override fun onDeleteAccountConfirmation() {
            viewModelScope.launch {
                _state.update { it.copy(isLoading = true, showDeleteAccountDialog = false) }
                try {
                    userRepository.delete(userViewModel.userState.value.user ?: throw IllegalStateException("No logged-in user"))
                    userViewModel.logout()
                } catch (e: Exception) {
                    _state.update { it.copy(errorMsg = e.message ?: "Unknown error") }
                } finally {
                    _state.update { it.copy(isLoading = false) }
                }

            }
        }

        override fun onLogoutDismiss() {
            _state.update { it.copy(showLogoutDialog = false) }
        }

        override fun onDeleteAccountDismiss() {
            _state.update { it.copy(showDeleteAccountDialog = false) }
        }
    }
}