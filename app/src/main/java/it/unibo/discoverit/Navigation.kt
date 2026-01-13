package it.unibo.discoverit

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Groups
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.toRoute
import it.unibo.discoverit.ui.screens.account.AccountSettingsScreen
import it.unibo.discoverit.ui.screens.account.AccountSettingsViewModel
import it.unibo.discoverit.ui.screens.categorydetails.CategoryDetailsScreen
import it.unibo.discoverit.ui.screens.categorydetails.CategoryDetailsViewModel
import it.unibo.discoverit.ui.screens.home.HomeScreen
import it.unibo.discoverit.ui.screens.home.HomeViewModel
import it.unibo.discoverit.ui.screens.login.LoginScreen
import it.unibo.discoverit.ui.screens.login.LoginViewModel
import it.unibo.discoverit.ui.screens.login.UserViewModel
import it.unibo.discoverit.ui.screens.poidetails.POIDetailsScreen
import it.unibo.discoverit.ui.screens.poidetails.POIDetailsViewModel
import it.unibo.discoverit.ui.screens.registration.RegistrationScreen
import it.unibo.discoverit.ui.screens.registration.RegistrationViewModel
import it.unibo.discoverit.ui.screens.sessioncheck.SessionCheckScreen
import it.unibo.discoverit.ui.screens.sessioncheck.SessionCheckViewModel
import it.unibo.discoverit.ui.screens.settings.SettingsScreen
import it.unibo.discoverit.ui.screens.settings.SettingsViewModel
import it.unibo.discoverit.ui.screens.social.SocialScreen
import it.unibo.discoverit.ui.screens.social.SocialViewModel
import it.unibo.discoverit.ui.screens.userdetail.UserDetailScreen
import it.unibo.discoverit.ui.screens.userdetail.UserDetailViewModel
import kotlinx.serialization.Serializable
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

// ==================== DESTINATION DEFINITIONS ====================

sealed interface Destination {
    // Auth Graph
    @Serializable
    data object AuthGraph : Destination

    @Serializable
    data object SessionCheck: Destination

    @Serializable
    data object Login: Destination

    @Serializable
    data object Register: Destination

    // Main Graph
    @Serializable
    data object MainGraph : Destination

    @Serializable
    data object Home: Destination

    @Serializable
    data object Social: Destination

    @Serializable
    data object Settings: Destination

    @Serializable
    data object Account: Destination

    @Serializable
    data class CategoryDetails(val categoryId: Long): Destination

    @Serializable
    data class POIDetails(val poiId: Long): Destination

    @Serializable
    data class UserDetail(val userId: Long): Destination
}

sealed interface BottomNavDestination {
    val route: Destination
    val icon: ImageVector
    val label: String

    data object Home : BottomNavDestination {
        override val route = Destination.Home
        override val icon = Icons.Default.Home
        override val label = "Home"
    }

    data object Social : BottomNavDestination {
        override val route = Destination.Social
        override val icon = Icons.Default.Groups
        override val label = "Social"
    }

    data object Settings : BottomNavDestination {
        override val route = Destination.Settings
        override val icon = Icons.Default.Settings
        override val label = "Settings"
    }

    companion object {
        val items = listOf(Home, Social, Settings)
        val routes = items.map { it.route::class.qualifiedName }.toSet()
    }
}

// ==================== MAIN NAVIGATION ====================

@Composable
fun DiscoverItNavGraph(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = Destination.SessionCheck,
        modifier = Modifier.fillMaxSize()
    ) {
        // Session Check - punto di ingresso
        composable<Destination.SessionCheck> {
            val sessionCheckViewModel: SessionCheckViewModel = koinViewModel()
            val sessionCheckState by sessionCheckViewModel.state.collectAsStateWithLifecycle()

            SessionCheckScreen(
                state = sessionCheckState,
                actions = sessionCheckViewModel.actions,
                onNavigateToLogin = {
                    navController.navigate(Destination.AuthGraph) {
                        popUpTo(Destination.SessionCheck) { inclusive = true }
                    }
                },
                onNavigateToHome = {
                    navController.navigate(Destination.MainGraph) {
                        popUpTo(Destination.SessionCheck) { inclusive = true }
                    }
                }
            )
        }

        // Auth Graph - tutto il flusso di autenticazione
        authGraph(
            navController = navController,
            onLoginSuccess = {
                navController.navigate(Destination.MainGraph) {
                    popUpTo(Destination.AuthGraph) { inclusive = true }
                }
            }
        )

        // Main Graph - tutto il flusso autenticato
        mainGraph(
            navController = navController,
            onLogout = {
                navController.navigate(Destination.AuthGraph) {
                    popUpTo(Destination.MainGraph) { inclusive = true }
                }
            }
        )
    }
}

// ==================== AUTH GRAPH ====================

fun NavGraphBuilder.authGraph(
    navController: NavHostController,
    onLoginSuccess: () -> Unit
) {
    navigation<Destination.AuthGraph>(
        startDestination = Destination.Login
    ) {
        composable<Destination.Login> {
            val loginViewModel: LoginViewModel = koinViewModel()

            LoginScreen(
                loginState = loginViewModel.loginState.collectAsState().value,
                loginActions = loginViewModel.loginActions,
                onNavigateToRegister = {
                    navController.navigate(Destination.Register)
                },
                onLoginSuccess = onLoginSuccess
            )
        }

        composable<Destination.Register> {
            val registrationViewModel: RegistrationViewModel = koinViewModel()

            RegistrationScreen(
                state = registrationViewModel.state.collectAsState().value,
                actions = registrationViewModel.actions,
                onNavigateToLogin = {
                    navController.popBackStack(Destination.Login, inclusive = false)
                },
                onRegistrationSuccess = onLoginSuccess
            )
        }
    }
}

// ==================== MAIN GRAPH ====================

fun NavGraphBuilder.mainGraph(
    navController: NavHostController,
    onLogout: () -> Unit
) {
    navigation<Destination.MainGraph>(
        startDestination = Destination.Home
    ) {
        // ===== BOTTOM NAV SCREENS =====

        composable<Destination.Home> {
            val homeViewModel: HomeViewModel = koinViewModel()
            val homeState by homeViewModel.homeState.collectAsStateWithLifecycle()

            val homeActions = homeViewModel.actions

            HomeScreen(
                navController = navController,
                homeState = homeState,
                homeActions = homeActions,
                onCategoryClick = { categoryId ->
                    navController.navigate(Destination.CategoryDetails(categoryId))
                },
                onNavigateTo = { destination ->
                    bottomNavOnNavigateTo(destination, navController)
                }
            )
        }

        composable<Destination.Social> {
            val userViewModel: UserViewModel = koinViewModel()
            val userState by userViewModel.userState.collectAsStateWithLifecycle()

            val socialViewModel: SocialViewModel = koinViewModel(
                parameters = { parametersOf(userState.user?.userId) }
            )
            val socialState by socialViewModel.state.collectAsStateWithLifecycle()

            SocialScreen(
                navController = navController,
                onNavigateTo = {
                    bottomNavOnNavigateTo(it, navController)
                },
                state = socialState,
                actions = socialViewModel.actions,
                userState = userState,
                onUserClick = { userId ->
                    navController.navigate(Destination.UserDetail(userId))
                }
            )
        }

        composable<Destination.Settings> {
            val settingsViewModel: SettingsViewModel = koinViewModel()
            val settingsState by settingsViewModel.state.collectAsStateWithLifecycle()

            SettingsScreen(
                navController = navController,
                state = settingsState,
                actions = settingsViewModel.actions
            ) {
                bottomNavOnNavigateTo(it, navController)
            }
        }

        // ===== DETAIL SCREENS =====

        composable<Destination.Account> {
            val accountSettingsViewModel: AccountSettingsViewModel = koinViewModel()
            val accountSettingsState by accountSettingsViewModel.state.collectAsStateWithLifecycle()

            val userViewModel: UserViewModel = koinViewModel()
            val userState by userViewModel.userState.collectAsStateWithLifecycle()

            AccountSettingsScreen(
                navController = navController,
                state = accountSettingsState,
                actions = accountSettingsViewModel.actions,
                userState = userState,
                onLogout = onLogout
            )
        }

        composable<Destination.UserDetail> { backStackEntry ->
            val args = backStackEntry.toRoute<Destination.UserDetail>()
            val userDetailViewModel: UserDetailViewModel = koinViewModel(
                parameters = { parametersOf(args.userId) }
            )
            val userDetailState by userDetailViewModel.state.collectAsStateWithLifecycle()

            UserDetailScreen(
                navController = navController,
                state = userDetailState,
                actions = userDetailViewModel.actions,
                onNavigateTo = { bottomNavOnNavigateTo(it, navController) }
            )
        }

        composable<Destination.CategoryDetails> { backStackEntry ->
            val args = backStackEntry.toRoute<Destination.CategoryDetails>()
            val categoryDetailsViewModel: CategoryDetailsViewModel = koinViewModel(
                parameters = { parametersOf(args.categoryId) }
            )
            val categoryDetailsState by categoryDetailsViewModel.state.collectAsStateWithLifecycle()

            CategoryDetailsScreen(
                navController = navController,
                categoryDetailsState = categoryDetailsState,
                categoryDetailsActions = categoryDetailsViewModel.actions,
                onNavigateTo = { destination ->
                    bottomNavOnNavigateTo(destination, navController)
                },
                onPOIClick = { poiId ->
                    navController.navigate(Destination.POIDetails(poiId))
                }
            )
        }

        composable<Destination.POIDetails> { backStackEntry ->
            val args = backStackEntry.toRoute<Destination.POIDetails>()
            val poiDetailsViewModel: POIDetailsViewModel = koinViewModel(
                parameters = { parametersOf(args.poiId) }
            )
            val poiDetailsState by poiDetailsViewModel.state.collectAsStateWithLifecycle()

            POIDetailsScreen(
                navController = navController,
                state = poiDetailsState,
                actions = poiDetailsViewModel.actions,
                onNavigateTo = { destination ->
                    bottomNavOnNavigateTo(destination, navController)
                }
            )
        }
    }
}

// ==================== NAVIGATION HELPERS ====================

fun bottomNavOnNavigateTo(
    bottomNavDestination: BottomNavDestination,
    navController: NavHostController
) {
    navController.navigate(bottomNavDestination.route) {
        // Evita destinazioni duplicate
        launchSingleTop = true

        // Torna sempre a Home e salva lo stato
        popUpTo(Destination.Home) {
            saveState = true
        }

        // Ripristina lo stato della destinazione
        restoreState = true
    }
}