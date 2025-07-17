package it.unibo.discoverit.ui.screens.account

import android.graphics.Bitmap
import android.net.Uri
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
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
    val profilePictureUri: Uri? = null,
    val isLoading: Boolean = false,
    val errorMsg: String? = null,
    val isUsernameChanged: Boolean = false,
    val showImageSourceDialog: Boolean = false
)

interface AccountSettingsActions {
    fun onUsernameChange(username: String)
    fun onChangeProfilePicClick()
    fun onDismissImageSourceDialog()
    fun onPickFromGallery()
    fun onTakePhoto()
    fun onImagePicked(bitmap: Bitmap)
    fun onSaveClick()
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
                            profilePictureUri = Uri.parse(user.profilePicPath)
                        )
                    }
                }
            }
        }
    }

    val actions = object : AccountSettingsActions {
        override fun onUsernameChange(username: String) {
            _state.update { it.copy(username = username) }
        }

        override fun onChangeProfilePicClick() {
            _state.update { it.copy(showImageSourceDialog = true) }
        }

        override fun onDismissImageSourceDialog() {
            _state.update { it.copy(showImageSourceDialog = false) }
        }

        override fun onPickFromGallery() {
            _state.update { it.copy(showImageSourceDialog = false) }
            // trigger gallery launcher in composable
        }

        override fun onTakePhoto() {
            _state.update { it.copy(showImageSourceDialog = false) }
            // trigger camera launcher in composable
        }

        override fun onImagePicked(bitmap: Bitmap) {
            viewModelScope.launch {
                val userId = userViewModel.userState.value.user?.userId
                    ?: throw IllegalStateException("No logged-in user")
                val path = accountSettingsRepository.updateProfilePicture(userId, bitmap)
                _state.update { it.copy(profilePictureUri = Uri.parse(path)) }
            }
        }

        override fun onSaveClick() {
            viewModelScope.launch {
                _state.update { it.copy(isLoading = true) }
                try {
                    if (_state.value.username.isBlank()) {
                        throw Exception("Username cannot be empty")
                    }
                    val user = userRepository.getUserById(_state.value.userId ?: throw IllegalStateException("No logged-in user"))
                    val newUser = user.copy(username = _state.value.username)
                    userRepository.update(newUser)
                } catch (e: Exception) {
                    _state.update { it.copy(errorMsg = e.message ?: "Unknown error") }
                }
                _state.update { it.copy(isLoading = false) }
            }
        }
    }
}