package com.playtank.compose_recomposition.presentation.product.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

/**
 * Pure layout shell shared by [ProductItemUnoptimized] and [ProductItemOptimized].
 *
 * Renders a card with product name, price, and a favourite toggle. A small
 * recomposition-count badge is overlaid in the top-right corner; its colour
 * visually distinguishes which variant (red = unoptimized, green = optimized)
 * caused the recomposition.
 *
 * This composable is intentionally kept free of any state logic — all state
 * decisions happen one level up in the caller.
 */
@Composable
internal fun ProductItemLayout(
    name: String,
    price: Double,
    isFavorite: Boolean,
    onToggleFavorite: () -> Unit,
    badgeCount: Int,
    badgeColor: Color,
    badgeTextColor: Color,
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier.fillMaxWidth()) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = name,
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.SemiBold,
                    )
                    Text(
                        text = "$%.2f".format(price),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
                IconButton(onClick = onToggleFavorite) {
                    Icon(
                        imageVector = if (isFavorite) Icons.Filled.Favorite
                        else Icons.Filled.FavoriteBorder,
                        contentDescription = if (isFavorite) "Remove from favourites"
                        else "Add to favourites",
                        tint = if (isFavorite) MaterialTheme.colorScheme.error
                        else MaterialTheme.colorScheme.onSurfaceVariant,
                    )
                }
            }
        }

        // ── Recomposition badge ───────────────────────────────────────────────
        // Colour = red for unoptimized, green for optimized.
        // Count increments every time this subtree recomposes.
        Text(
            text = "recomposed ×$badgeCount",
            style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
            color = badgeTextColor,
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(top = 4.dp, end = 4.dp)
                .clip(RoundedCornerShape(4.dp))
                .background(badgeColor)
                .padding(horizontal = 6.dp, vertical = 2.dp),
        )
    }
}
