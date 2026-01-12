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
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import it.unibo.discoverit.BottomNavDestination
import it.unibo.discoverit.Destination
import it.unibo.discoverit.R
import it.unibo.discoverit.ui.composables.DiscoverItNavigationBar
import it.unibo.discoverit.ui.composables.DiscoverItTopAppBar
import it.unibo.discoverit.ui.composables.EmptyStateUI
import it.unibo.discoverit.ui.screens.categorydetails.composables.POIList

/**
 * Composable for displaying the details of a category.
 * Shows a list of the POIs in the category.
 * Each is a card and can be clicked to navigate to its details.
 *
 * @param navController The navigation controller needed by the top app bar.
 * @param categoryDetailsState The state of the category details screen.
 * @param categoryDetailsActions The actions for the category details screen.
 * @param onNavigateTo The callback to navigate to a destination of the bottom navigation.
 * @param onPOIClick The callback to navigate to the details of a POI
 */
@Composable
fun CategoryDetailsScreen(
    navController: NavHostController,
    categoryDetailsState: CategoryDetailsState,
    categoryDetailsActions: CategoryDetailsActions,
    onNavigateTo: (BottomNavDestination) -> Unit,
    onPOIClick: (Long) -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }

    // If there is an error, show a snackbar with the error message.
    categoryDetailsState.error?.let { error ->
        LaunchedEffect(error) {
            snackbarHostState.showSnackbar(message = error)
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxWidth(),
        topBar = {
            DiscoverItTopAppBar(navController, categoryDetailsState.currentCategoryName)
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
                // If it's loading, show a circular progress indicator.
                categoryDetailsState.isLoading -> {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .align(Alignment.Center)
                    )
                }
                // In case of empty state, show an empty state UI.
                categoryDetailsState.poiList.isEmpty() && !categoryDetailsState.isLoading -> {
                    EmptyStateUI(
                        stringResource(R.string.no_poi_found),
                        categoryDetailsActions::onRefresh
                    )
                }
                // If everything is fine, show the list of POIs.
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

