package it.unibo.discoverit.ui.composables

import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.lifecycle.compose.dropUnlessStarted
import it.unibo.discoverit.BottomNavDestination
import it.unibo.discoverit.Destination

/**
 * Bottom navigation bar for switching between main app destinations.
 *
 * Displays a set of [BottomNavDestination] items with icons and labels.
 * Highlights the currently selected destination and handles navigation.
 *
 * @param items The list of [BottomNavDestination]s to display. Defaults to all destinations.
 * @param currentRoute The currently active [Destination] to highlight.
 * @param onNavigateTo Callback invoked when a navigation item is selected.
 */
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
                onClick = dropUnlessStarted {
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