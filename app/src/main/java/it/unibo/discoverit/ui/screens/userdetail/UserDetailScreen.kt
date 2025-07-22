package it.unibo.discoverit.ui.screens.userdetail

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import it.unibo.discoverit.BottomNavDestination
import it.unibo.discoverit.Destination
import it.unibo.discoverit.data.database.entities.Achievement
import it.unibo.discoverit.data.database.entities.UserAchievementProgress
import it.unibo.discoverit.ui.composables.DiscoverItNavigationBar
import it.unibo.discoverit.ui.composables.DiscoverItTopAppBar
import it.unibo.discoverit.ui.composables.ErrorMessage
import it.unibo.discoverit.ui.screens.userdetail.composables.AchievementCard

@Composable
fun UserDetailScreen(
    navController: NavHostController,
    state: UserDetailState,
    actions: UserDetailActions,
    onNavigateTo: (BottomNavDestination) -> Unit
) {
    Scaffold(
        modifier = Modifier.fillMaxWidth(),
        topBar = { DiscoverItTopAppBar(navController) },
        bottomBar = {
            DiscoverItNavigationBar(
                currentRoute = Destination.Social,
                onNavigateTo = onNavigateTo
            )
        },
        containerColor = MaterialTheme.colorScheme.background,
        contentColor = MaterialTheme.colorScheme.onBackground
    ) { innerPadding ->
        UserDetailContent(
            state = state,
            modifier = Modifier.padding(innerPadding)
        )
    }
}

@Composable
private fun UserDetailContent(
    state: UserDetailState,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        modifier = modifier
            .padding(16.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        achievementsSection(
            title = "Completati",
            achievements = state.achievementsWithProgress.filterValues { it?.isCompleted ?: false },
            emptyMessage = "Nessun achievement completato",
            completed = true
        )

        item { Spacer(modifier = Modifier.height(16.dp)) }

        achievementsSection(
            title = "Da completare",
            achievements = state.achievementsWithProgress.filterValues { !(it?.isCompleted ?: false) },
            emptyMessage = "Tutti gli achievement completati!",
            completed = false
        )

        state.errorMsg?.let { errorMsg ->
            item {
                ErrorMessage(errorMsg)
            }
        }
    }
}

// Uso una extension function per poter usare "item" e "items" su LazyListScope,
// Se creassi un composable e facessi item { mioComposable } la lazycolumn non funzionerebbe a dovere
private fun LazyListScope.achievementsSection(
    title: String,
    achievements: Map<Achievement, UserAchievementProgress?>,
    emptyMessage: String,
    completed: Boolean
) {
    item {
        Text(
            text = title,
            style = MaterialTheme.typography.titleMedium,
            modifier = Modifier.padding(bottom = 8.dp)
        )
    }

    if (achievements.isNotEmpty()) {
        items(achievements.toList()) { achievementWithProgress ->
            AchievementCard(achievementWithProgress)
        }
    } else {
        item {
            Text(
                text = emptyMessage,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.padding(vertical = 8.dp)
            )
        }
    }
}