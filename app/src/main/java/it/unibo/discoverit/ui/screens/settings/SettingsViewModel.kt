package it.unibo.discoverit.ui.screens.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import it.unibo.discoverit.data.repositories.SettingsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

/**
 * Available theme options for the app.
 *
 * @property displayName The human-readable name of the theme.
 */
enum class ThemeOption(val displayName: String) {
    LIGHT("Light"),
    DARK("Dark"),
    SYSTEM("System default")
}

/**
 * Represents the UI state of the settings screen.
 *
 * @property selectedTheme The currently selected theme.
 * @property notificationsEnabled Whether notifications are enabled.
 * @property biometricLoginEnabled Whether biometric login is enabled.
 * @property appVersion The current version of the app.
 */
data class SettingsState(
    val selectedTheme: ThemeOption = ThemeOption.SYSTEM,
    val notificationsEnabled: Boolean = true,
    val biometricLoginEnabled: Boolean = false,
    val appVersion: String = "0.0.0",
)

/**
 * Available actions for the settings screen.
 */
interface SettingsActions {
    /**
     * Changes the app theme.
     */
    fun onThemeChange(theme: ThemeOption)

    /**
     * Toggles notifications on/off.
     */
    fun onNotificationsChange(enabled: Boolean)

    /**
     * Toggles biometric login on/off.
     */
    fun onBiometricLoginChange(enabled: Boolean)
}

/**
 * ViewModel for the settings screen.
 * Manages user preferences and persists them through the settings repository.
 *
 * @property settingsRepository Repository for accessing and persisting app settings.
 */
class SettingsViewModel(
    private val settingsRepository: SettingsRepository
) : ViewModel() {

    private val _state = MutableStateFlow(SettingsState())
    val state: StateFlow<SettingsState> = _state.asStateFlow()

    init {
        loadSettings()
    }

    val actions = object : SettingsActions {
        override fun onThemeChange(theme: ThemeOption) {
            _state.update { it.copy(selectedTheme = theme) }
            viewModelScope.launch {
                try {
                    settingsRepository.setTheme(theme)
                } catch (_: Exception) {
                    // Silently fail, state already updated optimistically
                }
            }
        }

        override fun onNotificationsChange(enabled: Boolean) {
            _state.update { it.copy(notificationsEnabled = enabled) }
            viewModelScope.launch {
                try {
                    settingsRepository.setNotificationsEnabled(enabled)
                } catch (_: Exception) {
                    // Silently fail, state already updated optimistically
                }
            }
        }

        override fun onBiometricLoginChange(enabled: Boolean) {
            _state.update { it.copy(biometricLoginEnabled = enabled) }
            viewModelScope.launch {
                try {
                    settingsRepository.setBiometricLoginEnabled(enabled)
                } catch (_: Exception) {
                    // Silently fail, state already updated optimistically
                }
            }
        }
    }

    /**
     * Loads the current settings from the repository.
     */
    private fun loadSettings() {
        viewModelScope.launch {
            _state.update {
                it.copy(
                    selectedTheme = ThemeOption.valueOf(settingsRepository.theme.first()),
                    notificationsEnabled = settingsRepository.notificationsEnabled.first(),
                    biometricLoginEnabled = settingsRepository.biometricLoginEnabled.first(),
                    appVersion = settingsRepository.appVersion
                )
            }
        }
    }
}