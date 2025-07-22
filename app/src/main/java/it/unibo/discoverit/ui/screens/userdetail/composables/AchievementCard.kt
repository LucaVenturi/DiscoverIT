package it.unibo.discoverit.ui.screens.userdetail.composables

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.StarOutline
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import it.unibo.discoverit.data.database.entities.Achievement
import it.unibo.discoverit.data.database.entities.UserAchievementProgress
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun AchievementCard(
    achievementWithProgress: Pair<Achievement, UserAchievementProgress?>
) {
    val achievement = achievementWithProgress.first
    val progress = achievementWithProgress.second
    val completed = progress?.isCompleted ?: false

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
                    text = achievement.name,
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
                    text = progress?.completionDate?.let { millis ->
                        // Formattazione della data
                        SimpleDateFormat("dd/MM/yy", Locale.getDefault())
                            .format(Date(millis))
                    } ?: "error",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.primary
                )
            } else {
                Text(
                    text = StringBuilder()
                        .append(progress?.progress ?: 0)
                        .append("/")
                        .append(achievement.targetCount)
                        .toString(),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}