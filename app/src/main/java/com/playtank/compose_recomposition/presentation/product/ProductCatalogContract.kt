package com.playtank.compose_recomposition.presentation.product

// ─────────────────────────────────────────────────────────────────────────────
// MVI Contract — ProductCatalogScreen
// ─────────────────────────────────────────────────────────────────────────────

/**
 * UI State for the ProductCatalogScreen.
 * [clickCounter] and [products] live in the same state object to demonstrate
 * the monolithic state recomposition storm anti-pattern.
 */
data class ProductCatalogUiState(
    val products: List<DemoProduct> = emptyList(),
    val clickCounter: Int = 0,
)

/**
 * Lightweight synthetic product for self-contained demo purposes.
 */
data class DemoProduct(
    val id: Int,
    val name: String,
    val price: Double,
    val isFavorite: Boolean = false,
)

/**
 * User actions (Intents) emitted from UI to ViewModel.
 */
sealed interface ProductCatalogUiIntent {
    data object IncrementCounter : ProductCatalogUiIntent
    data class ToggleFavorite(val productId: Int) : ProductCatalogUiIntent
}