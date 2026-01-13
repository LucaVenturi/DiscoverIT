package it.unibo.discoverit.ui.composables

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.outlined.AccountCircle
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.lifecycle.compose.dropUnlessStarted
import androidx.navigation.NavHostController
import it.unibo.discoverit.BottomNavDestination
import it.unibo.discoverit.Destination
import it.unibo.discoverit.R
import it.unibo.discoverit.R.string.arrowback_description

/**
 * Top app bar composable with dynamic navigation controls.
 *
 * Shows a back button when not in bottom navigation destinations,
 * and a profile button when not already on the account screen.
 *
 * @param navController The [NavHostController] for navigation actions.
 * @param title The text to display as the app bar title. Defaults to app name.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DiscoverItTopAppBar(
    navController: NavHostController,
    title: String = stringResource(R.string.title)
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior(rememberTopAppBarState())

    // Usa le destinazioni condivise
    val bottomNavDestinations = BottomNavDestination.routes

    val currentRoute = navController.currentBackStackEntry?.destination?.route
    val isBottomNavDestination = currentRoute in bottomNavDestinations

    CenterAlignedTopAppBar(
        colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
            titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
            navigationIconContentColor = MaterialTheme.colorScheme.onPrimaryContainer,
            actionIconContentColor = MaterialTheme.colorScheme.onPrimaryContainer
        ),
        title = {
            Text(
                title,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
        },
        navigationIcon = {
            // Mostra la freccia indietro solo se:
            // 1. Non siamo in una destinazione della bottom navigation
            // 2. E c'è effettivamente una destinazione precedente
            if (!isBottomNavDestination && navController.previousBackStackEntry != null) {
                IconButton(onClick = dropUnlessStarted { navController.navigateUp() }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = stringResource(arrowback_description)
                    )
                }
            }
        },
        actions = {
            val isOnAccountScreen =
                navController.currentBackStackEntry?.destination?.route == Destination.Account::class.qualifiedName

            if(!isOnAccountScreen) {
                IconButton(onClick = dropUnlessStarted {
                    navController.navigate(Destination.Account)
                }) {
                    Icon(
                        imageVector = Icons.Outlined.AccountCircle,
                        contentDescription = "Account icon."
                    )
                }
            }
        },
        scrollBehavior = scrollBehavior
    )
}