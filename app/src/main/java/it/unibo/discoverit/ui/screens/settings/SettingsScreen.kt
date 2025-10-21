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
import androidx.compose.ui.unit.dp
import androidx.fragment.app.FragmentActivity
import androidx.navigation.NavHostController
import it.unibo.discoverit.BottomNavDestination
import it.unibo.discoverit.Destination
import it.unibo.discoverit.ui.composables.DiscoverItNavigationBar
import it.unibo.discoverit.ui.composables.DiscoverItTopAppBar
import it.unibo.discoverit.ui.screens.settings.composables.BiometricSection
import it.unibo.discoverit.ui.screens.settings.composables.InfoSection
import it.unibo.discoverit.ui.screens.settings.composables.NotificationsSection
import it.unibo.discoverit.ui.screens.settings.composables.ThemeSection
import it.unibo.discoverit.utils.biometric.BiometricAuthAdapter

@Composable
fun SettingsScreen(
    navController: NavHostController,
    state: SettingsState,
    actions: SettingsActions,
    onNavigateTo: (BottomNavDestination) -> Unit
) {
    val context = LocalContext.current
    val activity = context as? FragmentActivity
    val biometricAdapter = remember { BiometricAuthAdapter(context) }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            DiscoverItTopAppBar(
                navController = navController,
                title = "Impostazioni"
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

            ThemeSection(
                selectedTheme = state.selectedTheme,
                onThemeChange = actions::onThemeChange
            )

            SectionDivider()

            BiometricSection(
                biometricEnabled = state.biometricLoginEnabled,
                onBiometricChange = actions::onBiometricLoginChange,
                biometricAvailable = biometricAdapter.canAuthenticate(),
            )

            SectionDivider()

            NotificationsSection(
                notificationsEnabled = state.notificationsEnabled,
                onNotificationsChange = actions::onNotificationsChange
            )

            SectionDivider()

            InfoSection(appVersion = state.appVersion)
        }
    }
}

@Composable
private fun SectionDivider() {
    HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp))
}