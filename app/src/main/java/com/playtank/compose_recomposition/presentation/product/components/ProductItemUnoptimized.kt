package com.playtank.compose_recomposition.presentation.product.components

import android.util.Log
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.playtank.compose_recomposition.presentation.product.ProductCatalogUiState

private const val TAG = "RecompositionDemo"

/**
 * ## ⚠️ Anti-pattern — receives the **whole** [ProductCatalogUiState].
 *
 * Because [ProductCatalogUiState] bundles [products] AND [clickCounter] together,
 * every increment of the counter produces a *new* state instance. Compose performs
 * a structural-equality check on the parameter: the new instance is not equal to
 * the previous one, so **this composable recomposes even though the product it
 * renders is completely unchanged**.
 *
 * You can confirm this in Layout Inspector: the recomposition count on these items
 * climbs in lock-step with the counter, regardless of which product data changed.
 *
 * @param uiState    The entire UI state — the intentional flaw.
 * @param productId  ID used to look up the specific product inside [uiState].
 * @param onToggleFavorite Callback forwarded to [ProductItemLayout].
 */
@Composable
internal fun ProductItemUnoptimized(
    uiState: ProductCatalogUiState,
    productId: Int,
    onToggleFavorite: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val product = uiState.products.firstOrNull { it.id == productId } ?: return

    // intArrayOf is a mutable, non-observable ref.
    // Mutating it never schedules a recomposition — it only records how many
    // times Compose decided to recompose this composable for other reasons.
    val recompositionCount = remember { intArrayOf(0) }
    recompositionCount[0]++

    SideEffect {
        Log.d(TAG, "[UNOPTIMIZED] id=${product.id} recomposed ×${recompositionCount[0]}")
    }

    ProductItemLayout(
        name = product.name,
        price = product.price,
        isFavorite = product.isFavorite,
        onToggleFavorite = onToggleFavorite,
        badgeCount = recompositionCount[0],
        badgeColor = MaterialTheme.colorScheme.error,
        badgeTextColor = MaterialTheme.colorScheme.onError,
        modifier = modifier,
    )
}
