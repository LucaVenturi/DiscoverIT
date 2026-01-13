package it.unibo.discoverit.ui.screens.userdetail

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import it.unibo.discoverit.BottomNavDestination
import it.unibo.discoverit.Destination
import it.unibo.discoverit.R
import it.unibo.discoverit.data.database.entities.Achievement
import it.unibo.discoverit.data.database.entities.UserAchievementProgress
import it.unibo.discoverit.ui.composables.DiscoverItNavigationBar
import it.unibo.discoverit.ui.composables.DiscoverItTopAppBar
import it.unibo.discoverit.ui.composables.ErrorMessage
import it.unibo.discoverit.ui.screens.userdetail.composables.AchievementCard
import androidx.compose.ui.res.stringResource

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
fun UserDetailContent(
    state: UserDetailState,
    modifier: Modifier = Modifier
) {
    val titleCompleted = stringResource(R.string.completed)
    val titleToBeCompleted = stringResource(R.string.to_be_completed)
    val emptyMessage = stringResource(R.string.no_completed_achievement)
    val fullMessage = stringResource(R.string.all_achievements_completed)

    LazyColumn(
        modifier = modifier
            .padding(16.dp)
            .fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        achievementsSection(
            title = titleCompleted,
            achievements = state.achievementsWithProgress.filterValues { it?.isCompleted ?: false },
            emptyMessage = emptyMessage
        )

        item { Spacer(modifier = Modifier.height(16.dp)) }

        achievementsSection(
            title = titleToBeCompleted,
            achievements = state.achievementsWithProgress.filterValues { !(it?.isCompleted ?: false) },
            emptyMessage = fullMessage
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
    emptyMessage: String
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