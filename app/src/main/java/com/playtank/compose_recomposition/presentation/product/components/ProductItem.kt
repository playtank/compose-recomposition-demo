package com.playtank.compose_recomposition.presentation.product.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.playtank.compose_recomposition.presentation.product.ProductCatalogUiState

/**
 * Anti-pattern: receives the entire [uiState] containing both products and clickCounter.
 * Incrementing the counter changes the state instance, forcing this card to recompose
 * even when no product data changed.
 */
@Composable
internal fun ProductItem(
    uiState: ProductCatalogUiState,
    productId: Int,
    onToggleFavorite: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val product = uiState.products.firstOrNull { it.id == productId } ?: return

    ProductItemLayout(
        name = product.name,
        price = product.price,
        isFavorite = product.isFavorite,
        onToggleFavorite = onToggleFavorite,
        modifier = modifier,
    )
}