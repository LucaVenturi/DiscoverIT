package it.unibo.discoverit.ui.screens.settings

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import it.unibo.discoverit.BottomNavDestination
import it.unibo.discoverit.Destination
import it.unibo.discoverit.R
import it.unibo.discoverit.ui.composables.DiscoverItNavigationBar
import it.unibo.discoverit.ui.composables.DiscoverItTopAppBar
import it.unibo.discoverit.ui.screens.settings.composables.BiometricSection
import it.unibo.discoverit.ui.screens.settings.composables.InfoSection
import it.unibo.discoverit.ui.screens.settings.composables.NotificationsSection
import it.unibo.discoverit.ui.screens.settings.composables.ThemeSection
import it.unibo.discoverit.utils.biometric.BiometricAuthHelper

/**
 * Composable of the settings screen.
 *
 * @param navController The navigation controller, needed by the top app bar.
 * @param state The state of the screen.
 * @param actions The actions that can be performed on the screen.
 * @param onNavigateTo The action to perform when the user clicks on a navigation item.
 */
@Composable
fun SettingsScreen(
    navController: NavHostController,
    state: SettingsState,
    actions: SettingsActions,
    onNavigateTo: (BottomNavDestination) -> Unit
) {
    val context = LocalContext.current
    val biometricHelper = remember { BiometricAuthHelper(context) }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            DiscoverItTopAppBar(
                navController = navController,
                title = stringResource(R.string.theme)
            )
        },
        bottomBar = {
            DiscoverItNavigationBar(
                currentRoute = Destination.Settings,
                onNavigateTo = onNavigateTo
            )
        },
        containerColor = MaterialTheme.colorScheme.background,
        contentColor = MaterialTheme.colorScheme.onBackground
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp)
        ) {
            // Section to select the theme.
            ThemeSection(
                selectedTheme = state.selectedTheme,
                onThemeChange = actions::onThemeChange
            )

            SectionDivider()

            // Section to enable/disable biometric authentication.
            BiometricSection(
                biometricEnabled = state.biometricLoginEnabled,
                onBiometricChange = actions::onBiometricLoginChange,
                biometricAvailable = biometricHelper.isBiometricAvailable()
            )

//            SectionDivider()

//            NotificationsSection(
//                notificationsEnabled = state.notificationsEnabled,
//                onNotificationsChange = actions::onNotificationsChange
//            )

            SectionDivider()

            // Section to show the app version.
            InfoSection(appVersion = state.appVersion)
        }
    }
}

@Composable
private fun SectionDivider() {
    HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp))
}