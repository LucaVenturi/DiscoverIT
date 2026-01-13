package it.unibo.discoverit.ui.screens.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import it.unibo.discoverit.BottomNavDestination
import it.unibo.discoverit.Destination
import it.unibo.discoverit.ui.composables.DiscoverItNavigationBar
import it.unibo.discoverit.ui.composables.DiscoverItTopAppBar
import it.unibo.discoverit.ui.composables.EmptyStateUI
import it.unibo.discoverit.ui.composables.LoadingScreen
import it.unibo.discoverit.ui.screens.home.composables.CategoryCard

/**
 * Composable for displaying the home screen.
 * Shows the list of categories.
 * Each is a card and can be clicked to navigate to its details,
 * revealing the points of interest in it.
 *
 * @param navController The navigation controller needed by the top app bar.
 * @param homeState The state of the home screen.
 * @param onCategoryClick The callback to navigate to the details of a category.
 * @param onNavigateTo The callback to navigate to a destination of the bottom navigation.
 */
@Composable
fun HomeScreen(
    navController: NavHostController,
    homeState: HomeState,
    homeActions: HomeActions,
    onCategoryClick: (Long) -> Unit,
    onNavigateTo: (BottomNavDestination) -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }

    homeState.errorMsg?.let { errorMsg ->
        LaunchedEffect(errorMsg) {
            snackbarHostState.showSnackbar(
                message = errorMsg,
                withDismissAction = true,
            )
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxWidth(),
        topBar = {
            DiscoverItTopAppBar(navController)
        },
        bottomBar = {
            DiscoverItNavigationBar(
                currentRoute = Destination.Home,

                onNavigateTo = onNavigateTo
            )
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
        containerColor = MaterialTheme.colorScheme.background,
        contentColor = MaterialTheme.colorScheme.onBackground,
    ) { innerPadding ->
        when {
            homeState.isLoading -> {
                LoadingScreen()
            }
            homeState.categories.isEmpty() -> EmptyStateUI(
                message = "No categories were found, probably an error.",
                onRefresh = homeActions::onRefresh
            )
            else -> {
                val categories = homeState.categories

                LazyColumn(
                    Modifier.padding(innerPadding).fillMaxSize().padding(4.dp),
                    contentPadding = PaddingValues(0.dp, 6.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    items(categories) { categoryWithStats ->
                        CategoryCard(
                            categoryWithStats = categoryWithStats,
                            onCategoryClick = onCategoryClick
                        )
                    }
                }
            }
        }
    }
}