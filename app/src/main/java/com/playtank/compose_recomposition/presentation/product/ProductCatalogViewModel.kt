package com.playtank.compose_recomposition.presentation.product

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

/**
 * ViewModel for [ProductCatalogScreen].
 *
 * Owns all business logic and exposes a single [StateFlow] of [ProductCatalogUiState].
 * UI events are sent via [processIntent] following the MVI pattern.
 *
 * The [ProductCatalogUiState.clickCounter] and [ProductCatalogUiState.products] are
 * deliberately co-located in the same state object so the demo can show:
 *
 * - **Unoptimized path** — composables that receive the whole state object will
 *   recompose on every counter increment (recomposition storm).
 * - **Optimized path** — composables that receive only the fields they render
 *   will be skipped entirely when just the counter changes.
 */
class ProductCatalogViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(
        ProductCatalogUiState(products = generateProducts()),
    )
    val uiState: StateFlow<ProductCatalogUiState> = _uiState.asStateFlow()

    fun processIntent(intent: ProductCatalogUiIntent) {
        when (intent) {
            ProductCatalogUiIntent.IncrementCounter -> incrementCounter()
            is ProductCatalogUiIntent.ToggleFavorite -> toggleFavorite(intent.productId)
        }
    }

    // ── Private helpers ───────────────────────────────────────────────────────

    private fun incrementCounter() {
        _uiState.update { it.copy(clickCounter = it.clickCounter + 1) }
    }

    private fun toggleFavorite(productId: Int) {
        _uiState.update { state ->
            state.copy(
                products = state.products.map { product ->
                    if (product.id == productId) product.copy(isFavorite = !product.isFavorite)
                    else product
                },
            )
        }
    }

    private fun generateProducts(): List<DemoProduct> = List(30) { index ->
        DemoProduct(
            id = index + 1,
            name = "Product ${index + 1}",
            price = 9.99 + index * 5.0,
        )
    }
}
