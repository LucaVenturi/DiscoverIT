package it.unibo.discoverit.ui.screens.settings.composables

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import it.unibo.discoverit.ui.screens.settings.ThemeOption

@Composable
fun ThemeSection(
    selectedTheme: ThemeOption,
    onThemeChange: (ThemeOption) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier) {
        SectionTitle(title = "Tema")

        Spacer(modifier = Modifier.height(12.dp))

        Column(modifier = Modifier.selectableGroup()) {
            ThemeOption.entries.forEach { theme ->
                ThemeOptionItem(
                    theme = theme,
                    isSelected = selectedTheme == theme,
                    onSelect = { onThemeChange(theme) }
                )
            }
        }
    }
}