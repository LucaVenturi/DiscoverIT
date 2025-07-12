package it.unibo.discoverit.ui.screens.poidetails

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Map
import androidx.compose.material.icons.filled.Place
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import it.unibo.discoverit.BottomNavDestination
import it.unibo.discoverit.Destination
import it.unibo.discoverit.data.database.entities.PointOfInterest
import it.unibo.discoverit.ui.composables.DiscoverItNavigationBar
import it.unibo.discoverit.ui.composables.MyTopAppBar

@Composable
fun POIDetailsScreen(
    navController: NavHostController,
    poiId: Long,
    state: POIDetailsState,
    actions: POIDetailsActions,
    onNavigateTo: (BottomNavDestination) -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }

    // Caricamento iniziale
    LaunchedEffect(poiId) {
        if (state.currentPoi?.poiId != poiId) {
            actions.loadPOI(poiId)
        }
    }

    // Gestione errori
    state.errorMsg?.let { error ->
        LaunchedEffect(error) {
            snackbarHostState.showSnackbar(message = error)
            actions.dismissError()
        }
    }

    Scaffold(
        topBar = {
            MyTopAppBar(navController)
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
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                state.currentPoi == null && !state.isLoading -> {
                    EmptyStateUI(
                        onRetry = { actions.loadPOI(poiId) }
                    )
                }
                else -> {
                    state.currentPoi?.let { poi ->
                        POIDetailsContent(
                            poi = poi,
                            isVisited = state.isVisited,
                            onToggleVisit = { actions.toggleVisit(poiId) },
                            onOpenMap = { /* TODO: Implement map navigation */ }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun POIDetailsContent(
    poi: PointOfInterest,
    isVisited: Boolean,
    onToggleVisit: () -> Unit,
    onOpenMap: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        // Immagine principale
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1.5f)
                .clip(RoundedCornerShape(16.dp))
                .background(MaterialTheme.colorScheme.secondaryContainer),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Place,
                contentDescription = "POI Image",
                modifier = Modifier.size(96.dp),
                tint = MaterialTheme.colorScheme.primary
            )
        }

        Spacer(Modifier.height(16.dp))

        // Titolo
        Text(
            text = poi.name,
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(Modifier.height(8.dp))

        // Indirizzo
        Text(
            text = poi.address.street + (poi.address.civicNumber ?: "") + poi.address.province,
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(Modifier.height(16.dp))

        // Descrizione
        Text(
            text = poi.description,
            style = MaterialTheme.typography.bodyLarge
        )

        Spacer(Modifier.height(24.dp))

        // Azioni
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextButton(onClick = onOpenMap) {
                Icon(Icons.Default.Map, contentDescription = "Maps")
                Spacer(Modifier.width(8.dp))
                Text("Apri in Maps")
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text("Visitato")
                Checkbox(
                    checked = isVisited,
                    onCheckedChange = { onToggleVisit() }
                )
            }
        }
    }
}

@Composable
private fun EmptyStateUI(onRetry: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Punto di interesse non trovato",
            style = MaterialTheme.typography.bodyLarge
        )
        Spacer(Modifier.height(16.dp))
        TextButton(onClick = onRetry) {
            Text("Riprova")
        }
    }
}