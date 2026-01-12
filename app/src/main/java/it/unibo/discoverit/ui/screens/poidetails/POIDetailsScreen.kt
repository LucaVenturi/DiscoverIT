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

    // Location Permissions manager. Asks for coarse and fine location permissions.
    val locationPermissions = rememberMultiplePermissions(
        permissions = listOf(
            Manifest.permission.ACCESS_COARSE_LOCATION,
            Manifest.permission.ACCESS_FINE_LOCATION
        )
    ) { statuses ->
        when {
            statuses.any { it.value == PermissionStatus.Granted } -> {
            /*
                Uno tra FINE e COARSE è stato garantito, per quanto sarebbe meglio FINE
                lascio comunque che funzioni, ma al prossimo click verrà
                chiesto di aumentare la precisione
             */
            }

            statuses.all { it.value == PermissionStatus.PermanentlyDenied } -> {
                /*
                    Entrambi negati permanentemente, chiamo la funzione di gestione
                 */
                actions.onPermanentlyDenied()
            }

            else -> {
                /*
                    Entrambi negati temporaneamente, chiamo la funzione di gestione
                 */
                actions.onDenied()
            }
        }
    }

    // In case of error, show the error message with a snackbar and dismiss it.
    state.errorMsg?.let { error ->
        LaunchedEffect(error) {
            snackbarHostState.showSnackbar(message = error)
            actions.dismissError()
        }
    }

    // In case of location error, show the error message with a snackbar and dismiss it.
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

    // In case of permission error, show the error message with a snackbar and dismiss it.
    state.permissionError?.let { permissionError ->
        val errorMessage = stringResource(R.string.permissions_permanently_negated_error)
        val actionLabel = stringResource(R.string.go_to_settings)
        LaunchedEffect(permissionError) {
            when (permissionError) {
                // If the permission is permanently denied,
                // show a snackbar with the action to go to settings.
                PermissionError.PermanentlyDenied -> {
                    val res = snackbarHostState.showSnackbar(
                        message = errorMessage,
                        actionLabel = actionLabel,
                        duration = SnackbarDuration.Long,
                    )
                    if (res == SnackbarResult.ActionPerformed)
                        openAppSettings(ctx)
                }

                // If the permission is denied, show a snackbar to notify the user.
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
                    // If the POI is loaded, show the POI details.
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
                                val fineLocationPermissionStatus =
                                    locationPermissions.statuses[Manifest.permission.ACCESS_FINE_LOCATION]
                                if (fineLocationPermissionStatus != PermissionStatus.Granted &&
                                    fineLocationPermissionStatus != PermissionStatus.PermanentlyDenied) {
                                    // If the user didnt grant the fine location permission,
                                    // and the permission is not permanently denied,
                                    // request the fine location permission.
                                    locationPermissions.launchPermissionRequest()
                                } else {
                                    actions.onGPSUse()
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

/**
 * Opens the POI in Google Maps.
 * Launches an intent to open the Google Maps app with the given location.
 * If Google Maps is not installed, launches an intent to open the location on the OSM website.
 *
 * @param context The context of the activity.
 * @param latitude The latitude of the location.
 * @param longitude The longitude of the location.
 * @param locationName The name of the location.
 */
private fun openInMaps(
    context: Context,
    latitude: Double,
    longitude: Double,
    locationName: String
) {
    // Create the URI for the location on Google Maps
    val uri = "geo:$latitude,$longitude?q=$latitude,$longitude($locationName)".toUri()

    // Create the intent to open Google Maps
    val openInMapsIntent = Intent(Intent.ACTION_VIEW).apply {
        data = uri
    }

    // If the intent can be handled, start the activity.
    // Otherwise, open the location on the OSM website.
    if (openInMapsIntent.resolveActivity(context.packageManager) != null) {
        context.startActivity(openInMapsIntent)
    } else {
        // No app can handle the intent, open the location on the OSM website.
        val webUri = "https://www.openstreetmap.org/?mlat=$latitude&mlon=$longitude&zoom=16".toUri()
        val webIntent = Intent(Intent.ACTION_VIEW, webUri)
        context.startActivity(webIntent)
        // se non ha nemmeno un broswer... unlucky
    }
}

/**
 * Opens the location settings of the device.
 * Launches an intent to open the location settings of the device.
 *
 * @param ctx The context of the activity.
 */
private fun openLocationSettings(ctx: Context) {
    // Create the intent to open the correct settings.
    val intent = Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS).apply {
        flags = Intent.FLAG_ACTIVITY_NEW_TASK
    }
    // If the intent can be handled, start the activity.
    if (intent.resolveActivity(ctx.packageManager) != null) {
        ctx.startActivity(intent)
    }
}

/**
 * Opens the app settings of the device.
 * Launches an intent to open the app settings.
 *
 * @param ctx The context of the activity.
 */
private fun openAppSettings(ctx: Context) {
    // Create the intent to open the settings of the App.
    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
        data = Uri.fromParts("package", ctx.packageName, null)
        flags = Intent.FLAG_ACTIVITY_NEW_TASK
    }
    // If the intent can be handled, start the activity.
    if (intent.resolveActivity(ctx.packageManager) != null) {
        ctx.startActivity(intent)
    }
}
