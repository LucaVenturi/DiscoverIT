package it.unibo.discoverit.ui.screens.poidetails

import android.Manifest
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.Settings
import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.navigation.NavHostController
import it.unibo.discoverit.BottomNavDestination
import it.unibo.discoverit.Destination
import it.unibo.discoverit.ui.composables.DiscoverItNavigationBar
import it.unibo.discoverit.ui.composables.DiscoverItTopAppBar
import it.unibo.discoverit.ui.composables.EmptyStateUI
import it.unibo.discoverit.ui.screens.poidetails.composables.POIDetailsContent
import it.unibo.discoverit.utils.permissions.PermissionStatus
import it.unibo.discoverit.utils.permissions.rememberMultiplePermissions
import androidx.core.net.toUri
import it.unibo.discoverit.R
import it.unibo.discoverit.R.string.poi_details

@Composable
fun POIDetailsScreen(
    navController: NavHostController,
    state: POIDetailsState,
    actions: POIDetailsActions,
    onNavigateTo: (BottomNavDestination) -> Unit
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val ctx = LocalContext.current

    val locationPermissions = rememberMultiplePermissions(
        listOf(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION)
    ) { statuses ->
        when {
            statuses.any { it.value == PermissionStatus.Granted } -> {
                /*
                    Uno tra FINE e COARSE è stato garantito, per quanto sarebbe meglio FINE
                    lascio comunque che funzioni, ma al prossimo click verrà
                    chiesto di aumentare la precisione
                 */
                Log.d("POIDetailsScreen", "Location permissions granted")
            }

            statuses.all { it.value == PermissionStatus.PermanentlyDenied } -> {
                /*
                    Entrambi negati permanentemente, chiamo la funzione di gestione
                 */
                actions.onPermanentlyDenied()
                Log.d("POIDetailsScreen", "Location permissions permanently denied")
            }

            else -> {
                /*
                    Entrambi negati temporaneamente, chiamo la funzione di gestione
                 */
                actions.onDenied()

            Log.d("POIDetailsScreen", "Location permissions denied")
            }
        }
    }

    // Gestione errori
    state.errorMsg?.let { error ->
        LaunchedEffect(error) {
            snackbarHostState.showSnackbar(message = error)
            actions.dismissError()
        }
    }

    state.locationError?.let { locationError ->
        val errorMessage = stringResource(R.string.gps_is_off_error)
        val actionLabel = stringResource(R.string.go_to_settings)
        LaunchedEffect(locationError) {
            when (locationError) {
                LocationError.GpsDisabled -> {
                    val res = snackbarHostState.showSnackbar(
                        errorMessage,
                        actionLabel,
                        duration = SnackbarDuration.Long
                    )
                    if (res == SnackbarResult.ActionPerformed)
                        openLocationSettings(ctx)
                }
                is LocationError.GenericError -> {
                    snackbarHostState.showSnackbar(locationError.message, duration = SnackbarDuration.Long)
                }
            }
            actions.onDismissLocationError()
        }
    }

    state.permissionError?.let { permissionError ->
        val errorMessage = stringResource(R.string.permissions_permanently_negated_error)
        val actionLabel = stringResource(R.string.go_to_settings)
        LaunchedEffect(permissionError) {
            when (permissionError) {
                PermissionError.PermanentlyDenied -> {
                    val res = snackbarHostState.showSnackbar(
                        message = errorMessage,
                        actionLabel = actionLabel,
                        duration = SnackbarDuration.Long,
                    )
                    if (res == SnackbarResult.ActionPerformed)
                        openAppSettings(ctx)
                }

                PermissionError.Denied -> {
                    snackbarHostState.showSnackbar(
                        "Permessi negati.",
                        duration = SnackbarDuration.Long
                    )
                }
            }
            actions.onDismissPermissionError()
        }
    }

    Scaffold(
        topBar = {
            DiscoverItTopAppBar(navController, state.currentPoi?.name ?: stringResource(poi_details))
        },
        bottomBar = {
            DiscoverItNavigationBar(
                currentRoute = Destination.Home,
                onNavigateTo = onNavigateTo
            )
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) },
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            when {
                state.isLoading -> {
                    CircularProgressIndicator(Modifier.align(Alignment.Center))
                }
                state.currentPoi == null && !state.isLoading -> {
                    EmptyStateUI(
                        message = stringResource(R.string.no_poi_found_error),
                        onRefresh = actions::onRefresh
                    )
                }
                else -> {
                    state.currentPoi?.let { poi ->
                        POIDetailsContent(
                            poi = poi,
                            isVisited = state.isVisited,
                            onToggleVisit = actions::toggleVisit,
                            showOutOfRangeMessage = state.showOutOfRangeMessage,
                            distanceToPOI = state.distanceToPOI,
                            onOpenInMap = {
                                openInMaps(
                                    context = ctx,
                                    latitude = state.currentPoi.latitude,
                                    longitude = state.currentPoi.longitude,
                                    locationName = state.currentPoi.name
                                )
                            },
                            onUseGPS = {
                                val fineLocationPermissionStatus = locationPermissions.statuses[Manifest.permission.ACCESS_FINE_LOCATION]
                                if (fineLocationPermissionStatus != PermissionStatus.Granted && fineLocationPermissionStatus != PermissionStatus.PermanentlyDenied) {
                                    /*
                                        Se non ho il permesso ACCESS_FINE_LOCATION,
                                        lancio la richiesta di permesso
                                        (Quindi anche se ho solo ACCESS_COARSE_LOCATION)
                                     */
                                    Log.d("POIDetailsScreen", "Requesting permission")
                                    locationPermissions.launchPermissionRequest()
                                    Log.d("POIDetailsScreen", "Permission requested")
                                } else {
                                    Log.d("POIDetailsScreen", "Permission already granted")
                                    actions.onGPSUse()
                                    Log.d("POIDetailsScreen", "onGPSUse called")
                                }
                            },
                            isButtonLoading = state.isLocationLoading
                        )
                    }
                }
            }
        }
    }
}

private fun openInMaps(
    context: Context,
    latitude: Double,
    longitude: Double,
    locationName: String
) {
    val uri = "geo:$latitude,$longitude?q=$latitude,$longitude($locationName)".toUri()
    val openInMapsIntent = Intent(Intent.ACTION_VIEW).apply {
        data = uri
    }

    if (openInMapsIntent.resolveActivity(context.packageManager) != null) {
        context.startActivity(openInMapsIntent)
    } else {
        // Se non dovessero esserci app per le mappe (crazy) lo apre sul browser da osm
        val webUri = "https://www.openstreetmap.org/?mlat=$latitude&mlon=$longitude&zoom=16".toUri()
        val webIntent = Intent(Intent.ACTION_VIEW, webUri)
        context.startActivity(webIntent)
    }
}

private fun openLocationSettings(ctx: Context) {
    val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS).apply {
        flags = Intent.FLAG_ACTIVITY_NEW_TASK
    }
    if (intent.resolveActivity(ctx.packageManager) != null) {
        ctx.startActivity(intent)
    }
}

private fun openAppSettings(ctx: Context) {
    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
        data = Uri.fromParts("package", ctx.packageName, null)
        flags = Intent.FLAG_ACTIVITY_NEW_TASK
    }
    if (intent.resolveActivity(ctx.packageManager) != null) {
        ctx.startActivity(intent)
    }
}
