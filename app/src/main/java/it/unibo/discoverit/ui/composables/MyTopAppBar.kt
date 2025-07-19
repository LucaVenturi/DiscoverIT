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
import androidx.compose.ui.text.style.TextOverflow
import androidx.navigation.NavHostController
import it.unibo.discoverit.BottomNavDestination
import it.unibo.discoverit.Destination

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyTopAppBar(
    navController: NavHostController,
    title: String = "Discoverit"
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
                IconButton(onClick = { navController.navigateUp() }) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Localized description"
                    )
                }
            }
        },
        actions = {
            if(navController.currentBackStackEntry?.destination?.route != Destination.Account::class.qualifiedName) {
                IconButton(onClick = { navController.navigate(Destination.Account) }) {
                    Icon(
                        imageVector = Icons.Outlined.AccountCircle,
                        contentDescription = "Localized description"
                    )
                }
            }
        },
        scrollBehavior = scrollBehavior
    )
}