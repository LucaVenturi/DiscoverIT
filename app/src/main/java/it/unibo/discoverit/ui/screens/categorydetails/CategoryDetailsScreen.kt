package it.unibo.discoverit.ui.screens.categorydetails

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Category
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import it.unibo.discoverit.BottomNavDestination
import it.unibo.discoverit.Destination
import it.unibo.discoverit.data.database.entities.PointOfInterest
import it.unibo.discoverit.ui.composables.DiscoverItNavigationBar
import it.unibo.discoverit.ui.composables.MyTopAppBar
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.TextButton
import androidx.compose.runtime.remember

@Composable
fun CategoryDetailsScreen(
    navController: NavHostController,
    categoryId: Long,
    categoryDetailsState: CategoryDetailsState,
    categoryDetailsActions: CategoryDetailsActions,
    onNavigateTo: (BottomNavDestination) -> Unit,
    onPOIClick: (Long) -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }

    // Caricamento iniziale e al cambio di categoryId
    LaunchedEffect(categoryId) {
        categoryDetailsActions.loadPOIs(categoryId)
    }

    // Gestione errori
    categoryDetailsState.error?.let { error ->
        LaunchedEffect(error) {
            snackbarHostState.showSnackbar(message = error)
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxWidth(),
        topBar = {
            MyTopAppBar(navController)
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
                categoryDetailsState.isLoading -> {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .align(Alignment.Center)
                    )
                }
                categoryDetailsState.poiList.isEmpty() && !categoryDetailsState.isLoading -> {
                    EmptyStateUI()
                }
                else -> {
                    POIList(
                        poiList = categoryDetailsState.poiList,
                        onPOIClick = { poiId ->
                            navController.navigate(Destination.POIDetails(poiId))
                        }
                    )
                }
            }
        }
    }
}

@Composable
private fun POIList(
    poiList: List<PointOfInterest>,
    onPOIClick: (Long) -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier.fillMaxSize()
    ) {
        items(poiList.size) { index ->
            POICard(
                poi = poiList[index],
                onPOIClick = onPOIClick
            )
        }
    }
}


@Composable
fun POICard(
    poi: PointOfInterest,
    modifier: Modifier = Modifier,
    onPOIClick: (Long) -> Unit
) {
    ElevatedCard(
        modifier = modifier
            .aspectRatio(1f) // quadrata
            .padding(4.dp),
        onClick = { onPOIClick(poi.poiId) }
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .weight(1f) // prende tutto lo spazio disponibile sopra
                    .fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Filled.Category,
                    contentDescription = null,
                    modifier = Modifier
                        .fillMaxSize(0.5f), // più piccolo, per non uscire
                    tint = MaterialTheme.colorScheme.primary
                )
            }

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                poi.name,
                style = MaterialTheme.typography.bodyMedium.copy(
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Center
                ),
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
private fun EmptyStateUI() {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Nessun punto di interesse trovato",
            style = MaterialTheme.typography.bodyLarge
        )
        Spacer(modifier = Modifier.height(16.dp))
        TextButton(onClick = { /* TODO: Refresh logic */ }) {
            Text("Ricarica")
        }
    }
}
