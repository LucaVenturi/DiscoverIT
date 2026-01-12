package it.unibo.discoverit.data.repositories

import it.unibo.discoverit.ui.screens.settings.ThemeOption
import kotlinx.coroutines.flow.Flow

/**
 * Repository for settings-related operations.
 *
 * @property theme a flow emitting the current theme option.
 * @property notificationsEnabled a flow emitting a boolean indicating whether
 * notifications are enabled.
 * @property biometricLoginEnabled a flow emitting a boolean indicating whether
 * biometric login is enabled.
 * @property appVersion a string representing the current version of the app.
 */
interface SettingsRepository {
    val theme: Flow<String>
    val notificationsEnabled: Flow<Boolean>
    val biometricLoginEnabled: Flow<Boolean>
    val appVersion: String

    /**
     * Sets the current theme option.
     *
     * @param theme the [theme option][ThemeOption] to set.
     */
    suspend fun setTheme(theme: ThemeOption)

    /**
     * Sets notifications on or off.
     *
     * @param enabled how to set notifications.
     */
    suspend fun setNotificationsEnabled(enabled: Boolean)

    /**
     * Sets biometric login enabled or not.
     *
     * @param enabled how to set biometric login.
     */
    suspend fun setBiometricLoginEnabled(enabled: Boolean)
}