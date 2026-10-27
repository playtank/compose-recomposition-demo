package com.playtank.compose_recomposition.presentation.product

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.playtank.compose_recomposition.presentation.product.components.CounterSection
import com.playtank.compose_recomposition.presentation.product.components.ProductItemOptimized
import com.playtank.compose_recomposition.presentation.product.components.ProductItemUnoptimized
import com.playtank.compose_recomposition.presentation.product.components.SectionHeader

// ─────────────────────────────────────────────────────────────────────────────
// Entry point
// ─────────────────────────────────────────────────────────────────────────────

/**
 * Screen entry point.
 *
 * Creates (or retrieves) the [ProductCatalogViewModel] from the composition-local
 * ViewModelStore, collects its state, and delegates rendering to
 * [ProductCatalogContent].
 *
 * Keeping state collection here (rather than deeper in the tree) means child
 * composables receive only the data they need, which is exactly the pattern we
 * are demonstrating with [ProductItemOptimized].
 */
@Composable
fun ProductCatalogScreen(
    viewModel: ProductCatalogViewModel = viewModel(),
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    ProductCatalogContent(
        uiState = uiState,
        onIntent = viewModel::processIntent,
    )
}

// ─────────────────────────────────────────────────────────────────────────────
// Stateless content shell
// ─────────────────────────────────────────────────────────────────────────────

/**
 * Stateless content composable that renders the full demo screen.
 *
 * Separated from [ProductCatalogScreen] to make Compose Preview and testing
 * straightforward — just pass a [ProductCatalogUiState] with no ViewModel required.
 *
 * The list is split into two sections:
 * - **Before (Unoptimized)** — each item receives the whole [uiState]; counter
 *   increments cause all 30 items to recompose.
 * - **After (Optimized)** — each item receives only `name`, `price`, and
 *   `isFavorite`; counter increments are skipped entirely.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ProductCatalogContent(
    uiState: ProductCatalogUiState,
    onIntent: (ProductCatalogUiIntent) -> Unit,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Recomposition Demo") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary,
                ),
            )
        },
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {

            // ── Counter trigger ───────────────────────────────────────────────
            item(key = "counter") {
                CounterSection(
                    counter = uiState.clickCounter,
                    onIncrement = { onIntent(ProductCatalogUiIntent.IncrementCounter) },
                )
            }

            // ── Before section header ─────────────────────────────────────────
            item(key = "header_before") {
                SectionHeader(
                    title = "⚠️ Before — whole state passed in",
                    subtitle = "Every item recomposes on every counter tap",
                    headerColor = MaterialTheme.colorScheme.errorContainer,
                    textColor = MaterialTheme.colorScheme.onErrorContainer,
                )
            }

            // ── Unoptimized items — receive the whole UiState ─────────────────
            //
            // ⚠️ ANTI-PATTERN: the whole state object is passed down.
            // When clickCounter changes, uiState becomes a new instance →
            // equality check fails → every item recomposes even though
            // none of their product data changed.
            items(
                items = uiState.products,
                key = { "unoptimized_${it.id}" },
            ) { product ->
                ProductItemUnoptimized(
                    uiState = uiState,
                    productId = product.id,
                    onToggleFavorite = {
                        onIntent(ProductCatalogUiIntent.ToggleFavorite(product.id))
                    },
                )
            }

            // ── After section header ──────────────────────────────────────────
            item(key = "header_after") {
                SectionHeader(
                    title = "✅ After — only needed fields passed in",
                    subtitle = "Items are skipped on counter tap — badge count stays frozen",
                    headerColor = Color(0xFF1B5E20),
                    textColor = Color(0xFFE8F5E9),
                )
            }

            // ── Optimized items — receive only what they render ───────────────
            //
            // ✅ CORRECT: only the three fields this item actually renders are
            // passed in. When clickCounter changes, these fields are structurally
            // equal to last time → Compose skips recomposition of these items.
            items(
                items = uiState.products,
                key = { "optimized_${it.id}" },
            ) { product ->
                ProductItemOptimized(
                    name = product.name,
                    price = product.price,
                    isFavorite = product.isFavorite,
                    onToggleFavorite = {
                        onIntent(ProductCatalogUiIntent.ToggleFavorite(product.id))
                    },
                )
            }
        }
    }
}
