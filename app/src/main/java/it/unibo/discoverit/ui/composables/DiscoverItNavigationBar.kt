package it.unibo.discoverit.ui.composables

import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import it.unibo.discoverit.BottomNavDestination
import it.unibo.discoverit.Destination

@Composable
fun DiscoverItNavigationBar(
    items: List<BottomNavDestination> = BottomNavDestination.items,
    currentRoute: Destination,
    onNavigateTo: (BottomNavDestination) -> Unit
) {
    val colorScheme = MaterialTheme.colorScheme

    NavigationBar(
        containerColor = colorScheme.surfaceVariant,
        contentColor = colorScheme.onSurfaceVariant
    ) {
        items.forEach { destination ->
            val selected = destination.route == currentRoute
            NavigationBarItem(
                icon = { Icon(destination.icon, contentDescription = destination.label) },
                label = { Text(destination.label) },
                selected = selected,
                onClick = {
                    if (!selected) {
                        onNavigateTo(destination)
                    }
                },
                colors = NavigationBarItemDefaults.colors(
                    selectedIconColor = colorScheme.primary,
                    selectedTextColor = colorScheme.primary,
                    indicatorColor = colorScheme.primaryContainer,
                    unselectedIconColor = colorScheme.onSurfaceVariant,
                    unselectedTextColor = colorScheme.onSurfaceVariant
                )
            )
        }
    }
}