package it.unibo.discoverit.ui.screens.settings

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import it.unibo.discoverit.data.repositories.SettingsRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

enum class ThemeOption(val displayName: String){
    LIGHT("Light"),
    DARK("Dark"),
    SYSTEM("System default")
}

data class SettingsState(
    val selectedTheme: ThemeOption = ThemeOption.SYSTEM,
    val notificationsEnabled: Boolean = true,
    val appVersion: String = "0.0.0",
)

interface SettingsActions{
    fun onThemeChange(theme: ThemeOption)
    fun onNotificationsChange(enabled: Boolean)
}

class SettingsViewModel(
    private val settingsRepository: SettingsRepository
): ViewModel() {
    var state by mutableStateOf(SettingsState())
        private set

    init {
        viewModelScope.launch {
            state = state.copy(
                selectedTheme = ThemeOption.valueOf(settingsRepository.theme.first()),
                notificationsEnabled = settingsRepository.notificationsEnabled.first(),
                appVersion = settingsRepository.appVersion
            )
        }
    }

    val actions = object : SettingsActions{
        override fun onThemeChange(theme: ThemeOption) {
            state = state.copy(selectedTheme = theme)
            viewModelScope.launch {
                try {
                    settingsRepository.setTheme(theme)
                } catch (e: Exception) {
                    // /*TODO*/
                }
            }
        }

        override fun onNotificationsChange(enabled: Boolean) {
            state = state.copy(notificationsEnabled = enabled)
            viewModelScope.launch {
                try {
                    settingsRepository.setNotificationsEnabled(enabled)
                } catch (e: Exception) {
                    // /*TODO*/
                }
            }
        }
    }
}