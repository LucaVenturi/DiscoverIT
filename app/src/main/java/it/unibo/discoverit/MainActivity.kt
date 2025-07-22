package it.unibo.discoverit

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.rememberNavController
import it.unibo.discoverit.ui.screens.settings.SettingsViewModel
import it.unibo.discoverit.ui.screens.settings.ThemeOption
import it.unibo.discoverit.ui.theme.DiscoverItTheme
import org.koin.androidx.compose.KoinAndroidContext
import org.koin.androidx.compose.koinViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            KoinAndroidContext {
                val settingsViewModel: SettingsViewModel = koinViewModel()
                val state by settingsViewModel.state.collectAsStateWithLifecycle()

                DiscoverItTheme(
                    darkTheme = when (state.selectedTheme) {
                        ThemeOption.LIGHT -> false
                        ThemeOption.DARK -> true
                        ThemeOption.SYSTEM -> isSystemInDarkTheme()
                    }
                ) {
                    val navController = rememberNavController()
                    DiscoverItNavGraph(navController)
                }
            }
        }
    }
}