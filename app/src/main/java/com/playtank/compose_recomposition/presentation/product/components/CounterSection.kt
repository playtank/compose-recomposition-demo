package com.playtank.compose_recomposition.presentation.product.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

/**
 * Sticky header card that holds the unrelated counter trigger.
 *
 * Tapping "Increment counter" changes [ProductCatalogUiState.clickCounter] which has
 * nothing to do with product data — that's the whole point. Composables that
 * receive the whole state object will recompose; composables that receive only
 * product-specific fields will be skipped.
 *
 * @param counter Current value of the unrelated click counter.
 * @param onIncrement Lambda invoked when the increment button is tapped.
 */
@Composable
internal fun CounterSection(
    counter: Int,
    onIncrement: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.errorContainer,
        ),
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            Text(
                text = "Unrelated state trigger",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onErrorContainer,
            )
            Text(
                text = "clickCounter has nothing to do with products. " +
                    "Tap the button and watch the red badges (recompositions) " +
                    "vs the green badges (skipped — count stays frozen).",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onErrorContainer,
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
                Button(onClick = onIncrement) {
                    Text("Increment counter")
                }
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = "Clicks: $counter",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onErrorContainer,
                )
            }
        }
    }
}
