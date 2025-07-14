package it.unibo.discoverit.ui.screens.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import it.unibo.discoverit.BottomNavDestination
import it.unibo.discoverit.Destination
import it.unibo.discoverit.ui.composables.DiscoverItNavigationBar
import it.unibo.discoverit.ui.composables.MyTopAppBar


@Composable
fun SettingsScreen(
    navController: NavHostController,
    state: SettingsState,
    actions: SettingsActions,
    onNavigateTo: (BottomNavDestination) -> Unit
) {
    Scaffold(
        modifier = Modifier.fillMaxWidth(),
        topBar = { MyTopAppBar(navController) },
        bottomBar = {
            DiscoverItNavigationBar(
                currentRoute = Destination.Settings,
                onNavigateTo = onNavigateTo
            )
        },
        containerColor = MaterialTheme.colorScheme.background,
        contentColor = MaterialTheme.colorScheme.onBackground
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                ThemeSection(state, actions)

                HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp))

                NotificationsSection(state, actions)

                HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp))

                InfoSection(state)
            }
        }
    }
}

@Composable
fun ThemeSection(state: SettingsState, actions: SettingsActions) {
    Text(
        text = "Tema",
        style = MaterialTheme.typography.titleMedium,
        modifier = Modifier.padding(bottom = 8.dp)
    )

    val themeOptions = ThemeOption.entries

    themeOptions.forEach { theme ->
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .clickable { actions.onThemeChange(theme) }
                .padding(vertical = 4.dp)
        ) {
            RadioButton(
                selected = (state.selectedTheme == theme),
                onClick = { actions.onThemeChange(theme) }
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = theme.name)
        }
    }
}

@Composable
fun NotificationsSection(state: SettingsState, actions: SettingsActions) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = "Notifiche",
            style = MaterialTheme.typography.titleMedium
        )
        Switch(
            checked = state.notificationsEnabled,
            onCheckedChange = { actions.onNotificationsChange(it) }
        )
    }
}

@Composable
fun InfoSection(state: SettingsState) {
    Text(
        text = "Info",
        style = MaterialTheme.typography.titleMedium,
        modifier = Modifier.padding(bottom = 8.dp)
    )

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = "Versione app")
        Text(
            text = state.appVersion,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

