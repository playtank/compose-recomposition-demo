package com.playtank.compose_recomposition.presentation.product.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.playtank.compose_recomposition.presentation.product.ProductCatalogUiState

/**
 * Anti-pattern: receives the entire [uiState] containing both products and clickCounter.
 * Incrementing the counter changes the state instance, forcing this card to recompose (red badge).
 */
@Composable
internal fun ProductItemUnoptimized(
    uiState: ProductCatalogUiState,
    productId: Int,
    onToggleFavorite: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val product = uiState.products.firstOrNull { it.id == productId } ?: return

    val recompositionCount = remember { intArrayOf(0) }
    recompositionCount[0]++

    ProductItemLayout(
        name = product.name,
        price = product.price,
        isFavorite = product.isFavorite,
        onToggleFavorite = onToggleFavorite,
        badgeCount = recompositionCount[0],
        badgeColor = MaterialTheme.colorScheme.error,       // 红色：代表重组风暴 (Storm)
        badgeTextColor = MaterialTheme.colorScheme.onError,
        modifier = modifier,
    )
}