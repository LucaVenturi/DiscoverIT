package it.unibo.discoverit.ui.screens.poidetails

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import it.unibo.discoverit.BottomNavDestination
import it.unibo.discoverit.Destination
import it.unibo.discoverit.ui.composables.DiscoverItNavigationBar
import it.unibo.discoverit.ui.composables.DiscoverItTopAppBar
import it.unibo.discoverit.ui.composables.EmptyStateUI
import it.unibo.discoverit.ui.screens.poidetails.composables.POIDetailsContent

@Composable
fun POIDetailsScreen(
    navController: NavHostController,
    state: POIDetailsState,
    actions: POIDetailsActions,
    onNavigateTo: (BottomNavDestination) -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }

    // Gestione errori
    state.errorMsg?.let { error ->
        LaunchedEffect(error) {
            snackbarHostState.showSnackbar(message = error)
            actions.dismissError()
        }
    }

    Scaffold(
        topBar = {
            DiscoverItTopAppBar(navController, state.currentPoi?.name ?: "POI Details")
        },
        bottomBar = {
            DiscoverItNavigationBar(
                currentRoute = Destination.Home,
                onNavigateTo = onNavigateTo
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            when {
                state.isLoading -> {
                    CircularProgressIndicator(Modifier.align(Alignment.Center))
                }
                state.currentPoi == null && !state.isLoading -> {
                    EmptyStateUI(
                        message = "Nessun punto di interesse trovato",
                        onRefresh = actions::onRefresh
                    )
                }
                else -> {
                    state.currentPoi?.let { poi ->
                        POIDetailsContent(
                            poi = poi,
                            isVisited = state.isVisited,
                            onToggleVisit = actions::toggleVisit,
                            onOpenMap = { /* TODO: Implement map navigation */ },
                            onUseGPS = { /* TODO: Implement GPS integration */ }
                        )
                    }
                }
            }
        }
    }
}

