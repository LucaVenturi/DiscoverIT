package it.unibo.discoverit.ui.screens.userdetail

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.StarOutline
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import it.unibo.discoverit.Destination
import it.unibo.discoverit.ui.composables.DiscoverItNavigationBar
import it.unibo.discoverit.ui.composables.MyTopAppBar

data class AchievementMock(
    val title: String,
    val description: String,
    val completed: Boolean,
    val dateCompleted: String? = null
)

@Composable
fun UserDetailScreen(
    navController: NavHostController
) {
    val achievements = listOf(
        AchievementMock("Esploratore", "Visita 5 luoghi", true, "2024-06-01"),
        AchievementMock("Collezionista", "Completa tutti i musei", false, null),
        AchievementMock("Pioniere", "Scopri un luogo nascosto", true, "2024-06-20"),
        AchievementMock("Maratoneta", "Visita 10 luoghi in un giorno", false, null),
        AchievementMock("Storico", "Completa tutti i monumenti", true, "2024-06-15")
    )

    Scaffold(
        modifier = Modifier.fillMaxWidth(),
        topBar = { MyTopAppBar(navController) },
        bottomBar = {
            DiscoverItNavigationBar(
                currentRoute = Destination.Home, // o Destination.Social se lo hai
                onNavigateTo = { /* TODO */ }
            )
        },
        containerColor = MaterialTheme.colorScheme.background,
        contentColor = MaterialTheme.colorScheme.onBackground
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            val (completed, notCompleted) = achievements.partition { it.completed }

            if (completed.isNotEmpty()) {
                item {
                    Text(
                        text = "Completati",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                }
                items(completed) { achievement ->
                    AchievementCardMock(achievement, completed = true)
                }
            }

            if (notCompleted.isNotEmpty()) {
                item {
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Da completare",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(vertical = 8.dp)
                    )
                }
                items(notCompleted) { achievement ->
                    AchievementCardMock(achievement, completed = false)
                }
            }
        }
    }
}

@Composable
fun AchievementCardMock(achievement: AchievementMock, completed: Boolean) {
    val borderColor = if (completed) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outline
    val icon = if (completed) Icons.Default.Check else Icons.Default.StarOutline
    val iconTint = if (completed) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        border = BorderStroke(2.dp, borderColor)
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = iconTint,
                modifier = Modifier.size(32.dp)
            )

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = achievement.title,
                    style = MaterialTheme.typography.bodyLarge,
                    color = if (completed) MaterialTheme.colorScheme.onBackground else MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = achievement.description,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            if (completed) {
                Text(
                    text = achievement.dateCompleted ?: "",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}
