package com.playtank.compose_recomposition.presentation.product

// ─────────────────────────────────────────────────────────────────────────────
// MVI Contract — ProductCatalogScreen
// ─────────────────────────────────────────────────────────────────────────────

/**
 * The single source of truth for the ProductCatalogScreen.
 *
 * **Intentional flaw (demo purpose)**
 * [products] and [clickCounter] are completely unrelated, yet they live in the
 * same state object. Any composable that receives the *whole* [ProductCatalogUiState]
 * will be forced to recompose every time [clickCounter] changes — even when none
 * of the product data changed. This is the "recomposition storm" anti-pattern.
 */
data class ProductCatalogUiState(
    val products: List<DemoProduct> = emptyList(),
    /**
     * High-frequency counter that has nothing to do with product data.
     * Exists solely to trigger recompositions so the before/after difference
     * becomes measurable in Layout Inspector.
     */
    val clickCounter: Int = 0,
)

/**
 * A lightweight, synthetic product used as list content for this demo.
 */
data class DemoProduct(
    val id: Int,
    val name: String,
    val price: Double,
    val isFavorite: Boolean = false,
)

/**
 * All user actions the screen can emit to the ViewModel.
 */
sealed interface ProductCatalogUiIntent {
    /** User tapped the "Increment counter" button. */
    data object IncrementCounter : ProductCatalogUiIntent

    /** User toggled the favourite icon on [productId]. */
    data class ToggleFavorite(val productId: Int) : ProductCatalogUiIntent
}
