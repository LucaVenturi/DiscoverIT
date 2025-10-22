package it.unibo.discoverit.ui.screens.home.composables

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.AccountBalance
import androidx.compose.material.icons.outlined.BeachAccess
import androidx.compose.material.icons.outlined.Castle
import androidx.compose.material.icons.outlined.Category
import androidx.compose.material.icons.outlined.Church
import androidx.compose.material.icons.outlined.Landscape
import androidx.compose.material.icons.outlined.Museum
import androidx.compose.material.icons.outlined.Nature
import androidx.compose.material.icons.outlined.Park
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material.icons.outlined.Storefront
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import it.unibo.discoverit.data.database.entities.CategoryStats

@Composable
fun CategoryCard(
    categoryWithStats: CategoryStats,
    onCategoryClick: (Long) -> Unit
) {
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
                imageVector = getIconFromName(categoryWithStats.category.iconName),
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
                "${(categoryWithStats.visitedCount.toDouble() / categoryWithStats.totalPOIs * 100).toLong()}%",
                style = MaterialTheme.typography.bodyLarge,
                textAlign = TextAlign.End
            )
        }
    }
}

private fun getIconFromName(iconName: String?) :ImageVector {
    return when (iconName?.lowercase()) {
        "monument" -> Icons.Outlined.AccountBalance
        "castle" -> Icons.Outlined.Castle
        "museum" -> Icons.Outlined.Museum
        "park" -> Icons.Outlined.Park
        "panorama" -> Icons.Outlined.Landscape
        "church" -> Icons.Outlined.Church
        "market" -> Icons.Outlined.ShoppingCart
        "landscape" -> Icons.Outlined.Landscape
        "storefront" -> Icons.Outlined.Storefront
        "nature" -> Icons.Outlined.Nature
        "beach_access" -> Icons.Outlined.BeachAccess
        else -> Icons.Outlined.Category
    }
}