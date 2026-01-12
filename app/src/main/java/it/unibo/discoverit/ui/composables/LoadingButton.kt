package it.unibo.discoverit.ui.composables

import androidx.compose.foundation.layout.size
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.dropUnlessStarted

/**
 * A button that displays a loading indicator when an operation is in progress.
 *
 * @param onClick The action to perform when the button is clicked.
 * @param modifier The modifier to apply to the button.
 * @param isLoading Whether the button is currently in a loading state.
 * @param enabled Whether the button is enabled (independent of loading state).
 * @param loadingIndicatorSize The size of the loading indicator.
 * @param colors The button colors.
 * @param content The content of the button when not loading.
 */
@Composable
fun LoadingButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    isLoading: Boolean = false,
    enabled: Boolean = true,
    loadingIndicatorSize: Dp = 24.dp,
    colors: ButtonColors = ButtonDefaults.buttonColors(),
    content: @Composable () -> Unit
) {
    Button(
        onClick = dropUnlessStarted { onClick() },
        modifier = modifier,
        enabled = enabled && !isLoading,
        colors = colors
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.size(loadingIndicatorSize),
                strokeWidth = 2.dp
            )
        } else {
            content()
        }
    }
}