package com.playtank.compose_recomposition.presentation.product.components

import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

private const val TAG = "RecompositionDemo"

/**
 * ## ✅ Optimized — receives **only the three fields it renders**.
 *
 * Compose's smart recomposition works by checking whether each parameter is equal
 * to its previous value (using `equals()`). Because [name], [price], and [isFavorite]
 * are stable, value-type parameters, and none of them change when [clickCounter]
 * is incremented, Compose can **skip** this composable entirely — zero extra work.
 *
 * You can confirm this in Layout Inspector: the recomposition count on these items
 * does **not** increment when the counter is tapped; it only increments when the
 * product's own data (e.g. [isFavorite]) actually changes.
 *
 * @param name           Product display name.
 * @param price          Product price in USD.
 * @param isFavorite     Whether this product is in the user's favourites.
 * @param onToggleFavorite Callback forwarded to [ProductItemLayout].
 */
@Composable
internal fun ProductItemOptimized(
    name: String,
    price: Double,
    isFavorite: Boolean,
    onToggleFavorite: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val recompositionCount = remember { intArrayOf(0) }
    recompositionCount[0]++

    SideEffect {
        Log.d(TAG, "[OPTIMIZED] $name recomposed ×${recompositionCount[0]}")
    }

    ProductItemLayout(
        name = name,
        price = price,
        isFavorite = isFavorite,
        onToggleFavorite = onToggleFavorite,
        badgeCount = recompositionCount[0],
        badgeColor = Color(0xFF2E7D32),
        badgeTextColor = Color(0xFFE8F5E9),
        modifier = modifier,
    )
}
