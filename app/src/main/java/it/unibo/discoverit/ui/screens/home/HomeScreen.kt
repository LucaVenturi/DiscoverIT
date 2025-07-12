package it.unibo.discoverit.ui.screens.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Category
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import it.unibo.discoverit.BottomNavDestination
import it.unibo.discoverit.Destination
import it.unibo.discoverit.data.database.entities.CategoryStats
import it.unibo.discoverit.ui.composables.DiscoverItNavigationBar
import it.unibo.discoverit.ui.composables.MyTopAppBar

@Composable
fun HomeScreen(
    navController: NavHostController,
    homeState: HomeState,
    onCategoryClick: (Long) -> Unit,
    onNavigateTo: (BottomNavDestination) -> Unit
) {
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
        contentColor = MaterialTheme.colorScheme.onBackground,
    ) { innerPadding ->
        val categories = homeState.categories

        LazyColumn(
            Modifier.padding(innerPadding).fillMaxSize().padding(6.dp),
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


@Composable
fun CategoryCard(categoryWithStats: CategoryStats, onCategoryClick: (Long) -> Unit) {
    ElevatedCard(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer,
            contentColor = MaterialTheme.colorScheme.onSecondaryContainer
        ),
        onClick = {
            onCategoryClick(categoryWithStats.category.categoryId)
        }
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Immagine/Icona più grande
            Icon(
                imageVector = Icons.Filled.Category,
                contentDescription = null,
                modifier = Modifier.size(56.dp),
                tint = MaterialTheme.colorScheme.primary
            )

            // Spazio tra icona e testo
            Spacer(modifier = Modifier.width(12.dp))

            // Nome
            Text(
                categoryWithStats.category.name,
                style = MaterialTheme.typography.bodyLarge
            )

            Spacer(modifier = Modifier.weight(1f))

            // Percentuale
            Text(
                "${categoryWithStats.visitedCount / categoryWithStats.totalPOIs * 100}%",
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.End
            )
        }
    }
}