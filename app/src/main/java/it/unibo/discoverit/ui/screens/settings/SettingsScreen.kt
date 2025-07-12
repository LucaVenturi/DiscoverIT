package it.unibo.discoverit.ui.screens.settings

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import it.unibo.discoverit.Destination
import it.unibo.discoverit.ui.composables.DiscoverItNavigationBar
import it.unibo.discoverit.ui.composables.MyTopAppBar


@Composable
fun SettingsScreen(navController: NavHostController) {
    Scaffold(
        modifier = Modifier.fillMaxWidth(),
        topBar = { MyTopAppBar(navController) },
        bottomBar = {
            DiscoverItNavigationBar(
                currentRoute = Destination.Settings,
                onNavigateTo = { /*TODO*/ }
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
                ThemeSection()

                HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp))

                NotificationsSection()

                HorizontalDivider(modifier = Modifier.padding(vertical = 16.dp))

                InfoSection()
            }
        }
    }
}

@Composable
fun ThemeSection() {
    Text(
        text = "Tema",
        style = MaterialTheme.typography.titleMedium,
        modifier = Modifier.padding(bottom = 8.dp)
    )

    val themeOptions = listOf("Chiaro", "Scuro", "Di sistema")
    var selectedTheme by remember { mutableStateOf("Di sistema") }

    themeOptions.forEach { theme ->
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .fillMaxWidth()
                .clickable { selectedTheme = theme }
                .padding(vertical = 4.dp)
        ) {
            RadioButton(
                selected = (theme == selectedTheme),
                onClick = { selectedTheme = theme }
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = theme)
        }
    }
}

@Composable
fun NotificationsSection() {
    var notificationsEnabled by remember { mutableStateOf(true) }

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
            checked = notificationsEnabled,
            onCheckedChange = { notificationsEnabled = it }
        )
    }
}

@Composable
fun InfoSection() {
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
            text = "1.0.0",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

