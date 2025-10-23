package it.unibo.discoverit.ui.screens.categorydetails

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
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
import it.unibo.discoverit.ui.screens.categorydetails.composables.POIList

@Composable
fun CategoryDetailsScreen(
    navController: NavHostController,
    categoryDetailsState: CategoryDetailsState,
    categoryDetailsActions: CategoryDetailsActions,
    onNavigateTo: (BottomNavDestination) -> Unit,
    onPOIClick: (Long) -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }

    // Gestione errori
    categoryDetailsState.error?.let { error ->
        LaunchedEffect(error) {
            snackbarHostState.showSnackbar(message = error)
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxWidth(),
        topBar = {
            DiscoverItTopAppBar(navController, categoryDetailsState.currentCategoryName)
            Log.e("CATEGORY_TESTING", categoryDetailsState.currentCategoryName)
        },
        bottomBar = {
            DiscoverItNavigationBar(
                currentRoute = Destination.Home,
                onNavigateTo = onNavigateTo
            )
        },
        containerColor = MaterialTheme.colorScheme.background,
        contentColor = MaterialTheme.colorScheme.onBackground
    ) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            when {
                // Gestisce lo stato di caricamento.
                categoryDetailsState.isLoading -> {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .align(Alignment.Center)
                    )
                }
                // Gestisce il caso in cui la lista dei punti di interesse sia vuota.
                categoryDetailsState.poiList.isEmpty() && !categoryDetailsState.isLoading -> {
                    EmptyStateUI("Nessun punto di interesse trovato", categoryDetailsActions::onRefresh)
                }
                // Gestisce lo stato di successo mostrando la lista dei punti di interesse.
                else -> {
                    POIList(
                        poiList = categoryDetailsState.poiList,
                        onPOIClick = onPOIClick
                    )
                }
            }
        }
    }
}

