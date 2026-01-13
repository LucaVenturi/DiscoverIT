package it.unibo.discoverit.ui.screens.account

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import it.unibo.discoverit.data.repositories.AccountSettingsRepository
import it.unibo.discoverit.data.repositories.UserRepository
import it.unibo.discoverit.ui.screens.login.UserViewModel
import it.unibo.discoverit.utils.accountservice.AccountService
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * Data class representing the state of the account settings screen.
 *
 * @property userId The ID of the logged-in user.
 * @property username The current username inside of the textbox.
 * @property isLoading Whether the screen is currently loading.
 * @property errorMsg The error message to be displayed, if any.
 * @property isUsernameChanged Whether the username has been changed.
 * @property showImageSourceDialog Whether the image source dialog should be shown.
 * @property showLogoutDialog Whether the logout dialog should be shown.
 * @property showDeleteAccountDialog Whether the delete account dialog should be shown.
 */
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

/**
 * Interface defining the actions that can be performed on the account settings screen.
 */
interface AccountSettingsActions {
    /**
     * Called when the username inside the textbox changes.
     *
     * @param username The changed text.
     */
    fun onUsernameChange(username: String)

    /**
     * Called when the user clicks on the change profile picture button.
     */
    fun onChangeProfilePicClick()

    /**
     * Called when the user clicks on the dismiss button of the image source dialog.
     */
    fun onDismissImageSourceDialog()

    /**
     * Called when the user clicks on the pick from gallery button of the image source dialog.
     */
    fun onPickFromGallery()

    /**
     * Called when the user clicks on the take photo button of the image source dialog.
     */
    fun onTakePhoto()

    /**
     * Called when an image has been chosen from the gallery or the camera.
     *
     * @param bitmap The bitmap of the picked image.
     */
    fun onImagePicked(bitmap: Bitmap)

    /**
     * Called when the user clicks on the save button of the username section to save the changes.
     */
    fun onSaveClick()

    /**
     * Called when the user clicks on the logout button.
     */
    fun onLogoutClick()

    /**
     * Called when the user clicks on the delete account button.
     */
    fun onDeleteAccountClick()

    /**
     * Called when the user confirms the logout action.
     */
    fun onLogoutConfirmation()

    /**
     * Called when the user confirms the delete account action.
     */
    fun onDeleteAccountConfirmation()

    /**
     * Called when the user dismisses the logout dialog.
     */
    fun onLogoutDismiss()

    /**
     * Called when the user dismisses the delete account dialog.
     */
    fun onDeleteAccountDismiss()
}

/**
 * ViewModel for the account settings screen.
 *
 * @property userViewModel The view model saving the state of the logged-in user.
 * @property userRepository The repository that handles user data.
 * @property accountService Helper class for managing user authentication.
 * Used to logout the user or delete his account.
 * @property accountSettingsRepository The repository that handles account settings data.
 */
class AccountSettingsViewModel(
    private val userViewModel: UserViewModel,
    private val userRepository: UserRepository,
    private val accountService: AccountService,
    private val accountSettingsRepository: AccountSettingsRepository
) : ViewModel() {

    // Safe way to expose the state.
    private val _state = MutableStateFlow(AccountSettingsState(username = ""))
    val state: StateFlow<AccountSettingsState> = _state.asStateFlow()

    // Initialize the state with the logged-in user's data
    init {
        viewModelScope.launch {
            // Get the logged-in user from the user viewmodel.
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

    /**
     * Local implementation of the actions.
     * @see AccountSettingsActions
     */
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

                    val userId = _state.value.userId
                        ?: throw IllegalStateException("No logged-in user")

                    // Update the profile picture in the database.
                    val updatedUser = accountSettingsRepository.updateProfilePicture(userId, bitmap)

                    // Update the user in the viewmodel.
                    userViewModel.setUser(updatedUser)
                } catch (e: Exception) {
                    _state.update {
                        it.copy(errorMsg = e.message ?: "Unknown error")
                    }
                } finally {
                    _state.update { it.copy(isLoading = false) }
                }
            }
        }

        override fun onSaveClick() {
            viewModelScope.launch {
                _state.update { it.copy(isLoading = true) }
                try {
                    // Get the user from the database and update its username
                    val user = userRepository.getUserById(
                        userId = _state.value.userId
                            ?: throw IllegalStateException("No logged-in user"),
                    )
                    val newUser = user.copy(username = _state.value.username)
                    accountSettingsRepository.changeUsername(
                        userId = newUser.userId,
                        newUsername = newUser.username
                    )

                    // Update the user in the viewmodel
                    userViewModel.setUser(newUser)

                    // Update the state.
                    _state.update {
                        it.copy(
                            isUsernameChanged = false,
                            errorMsg = null
                        )
                    }
                } catch (e: Exception) {
                    _state.update { it.copy(errorMsg = e.message ?: "Unknown error") }
                } finally {
                    _state.update { it.copy(isLoading = false) }
                }
            }
        }

        override fun onLogoutClick() {
            _state.update { it.copy(showLogoutDialog = true) }
        }

        override fun onDeleteAccountClick() {
            _state.update { it.copy(showDeleteAccountDialog = true) }
        }

        override fun onLogoutConfirmation() {
            viewModelScope.launch {
                _state.update { it.copy(isLoading = true, showLogoutDialog = false) }
                accountService.logout()
                _state.update { it.copy(showLogoutDialog = false) }
                userViewModel.logout()
            }
        }

        override fun onDeleteAccountConfirmation() {
            viewModelScope.launch {
                _state.update { it.copy(isLoading = true, showDeleteAccountDialog = false) }
                try {
                    userRepository.delete(
                        userViewModel.userState.value.user
                            ?: throw IllegalStateException("No logged-in user"),
                    )
                    // Also clear the user from the viewmodel
                    userViewModel.logout()
                } catch (e: Exception) {
                    _state.update { it.copy(errorMsg = e.message) }
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