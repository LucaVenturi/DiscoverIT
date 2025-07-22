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
import androidx.navigation.NavHostController
import it.unibo.discoverit.BottomNavDestination
import it.unibo.discoverit.Destination
import it.unibo.discoverit.ui.composables.DiscoverItNavigationBar
import it.unibo.discoverit.ui.composables.DiscoverItTopAppBar
import it.unibo.discoverit.ui.composables.EmptyStateUI
import it.unibo.discoverit.ui.screens.poidetails.composables.POIDetailsContent
import it.unibo.discoverit.utils.permissions.PermissionStatus
import it.unibo.discoverit.utils.permissions.rememberMultiplePermissions

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
            statuses.all { it.value == PermissionStatus.Granted } -> {}
            statuses.all { it.value == PermissionStatus.PermanentlyDenied } ->
                actions.onPermanentlyDenied()
            else ->
                actions.onDenied()
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
        LaunchedEffect(locationError) {
            Log.d("POIDetailsScreen", "Location error: $locationError")
            when (locationError) {
                LocationError.GpsDisabled -> {
                    Log.d("POIDetailsScreen", "Showing GPS disabled snackbar")
                    val res = snackbarHostState.showSnackbar(
                        "GPS disattivato, attivalo nelle impostazioni.",
                        "Vai alle impostazioni",
                        duration = SnackbarDuration.Long
                    )
                    Log.d("POIDetailsScreen", "Snackbar result: $res")
                    if (res == SnackbarResult.ActionPerformed)
                        openLocationSettings(ctx)
                    Log.d("POIDetailsScreen", "Snackbar action performed")
                }
                is LocationError.GenericError -> {
                    Log.d("POIDetailsScreen", "Showing generic error snackbar")
                    snackbarHostState.showSnackbar(locationError.message, duration = SnackbarDuration.Long)
                    Log.d("POIDetailsScreen", "Snackbar shown")
                }
            }
            actions.onDismissLocationError()
        }
    }

    state.permissionError?.let { permissionError ->
        LaunchedEffect(permissionError) {
            when (permissionError) {
                PermissionError.PermanentlyDenied -> {
                    val res = snackbarHostState.showSnackbar(
                        message = "Permessi negati permanentemente, concedili dalle impostazioni.",
                        actionLabel = "Vai alle impostazioni",
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
            DiscoverItTopAppBar(navController, state.currentPoi?.name ?: "POI Details")
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
                                if (locationPermissions.statuses.all { it.value != PermissionStatus.Granted }) {
                                    locationPermissions.launchPermissionRequest()
                                    Log.d("POIDetailsScreen", "Requesting permissions")
                                    return@POIDetailsContent
                                } else {
                                    actions.onGPSUse()
                                    Log.d("POIDetailsScreen", "Using GPS")
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
    val uri = Uri.parse("geo:$latitude,$longitude?q=$latitude,$longitude($locationName)")
    val openInMapsIntent = Intent(Intent.ACTION_VIEW).apply {
        data = uri
    }

    if (openInMapsIntent.resolveActivity(context.packageManager) != null) {
        context.startActivity(openInMapsIntent)
    } else {
        // Se non dovessero esserci app per le mappe (crazy) lo apre sul browser
        val webUri = Uri.parse("https://www.openstreetmap.org/?mlat=$latitude&mlon=$longitude&zoom=16")
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
