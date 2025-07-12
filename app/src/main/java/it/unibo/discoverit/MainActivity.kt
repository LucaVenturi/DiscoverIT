package it.unibo.discoverit

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import it.unibo.discoverit.ui.theme.DiscoverItTheme
import org.koin.androidx.compose.koinViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DiscoverItTheme {
                val navController = rememberNavController()
                DiscoverItNavGraph(navController)
            }
        }
    }
}