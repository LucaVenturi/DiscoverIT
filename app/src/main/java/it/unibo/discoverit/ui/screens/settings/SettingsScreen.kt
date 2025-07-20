package it.unibo.discoverit.ui.screens.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import it.unibo.discoverit.BottomNavDestination
import it.unibo.discoverit.Destination
import it.unibo.discoverit.ui.composables.DiscoverItNavigationBar
import it.unibo.discoverit.ui.composables.DiscoverItTopAppBar
import it.unibo.discoverit.ui.screens.settings.composables.InfoSection
import it.unibo.discoverit.ui.screens.settings.composables.NotificationsSection
import it.unibo.discoverit.ui.screens.settings.composables.ThemeSection

@Composable
fun SettingsScreen(
    navController: NavHostController,
    state: SettingsState,
    actions: SettingsActions,
    onNavigateTo: (BottomNavDestination) -> Unit
) {
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
                .padding(16.dp)
        ) {

            ThemeSection(
                selectedTheme = state.selectedTheme,
                onThemeChange = actions::onThemeChange
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