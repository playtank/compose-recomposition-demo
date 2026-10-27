package com.playtank.compose_recomposition.presentation.product

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
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
 * Screen entry point — wires the ViewModel and delegates rendering to
 * [ProductCatalogContent].
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
 * Stateless content composable.
 *
 * Layout:
 * ┌──────────────────────────────────┐
 * │  TopAppBar                       │
 * ├──────────────────────────────────┤
 * │  CounterSection  ← STICKY        │  never scrolls away
 * ├──────────────────────────────────┤
 * │  Before header                │
 * │  Unoptimized item × 30           │  scrollable
 * │  After header                  │
 * │  Optimized item × 30             │
 * └──────────────────────────────────┘
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
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
        ) {
            // ── Sticky counter trigger — always visible, never scrolls away ──
            CounterSection(
                counter = uiState.clickCounter,
                onIncrement = { onIntent(ProductCatalogUiIntent.IncrementCounter) },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
            )

            // ── Scrollable product list ──────────────────────────────────────
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {

                // ── Before section header ────────────────────────────────────
                item(key = "header_before") {
                    SectionHeader(
                        title = "Before — Whole State Passed In",
                        subtitle = "Anti-pattern: counter increment forces all items to recompose.",
                        headerColor = MaterialTheme.colorScheme.errorContainer,
                        textColor = MaterialTheme.colorScheme.onErrorContainer,
                    )
                }

                // ── Unoptimized items — receive the whole UiState ────────────
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

                // ── After section header ─────────────────────────────────────
                item(key = "header_after") {
                    SectionHeader(
                        title = "After — Granular Fields Passed In",
                        subtitle = "Optimized: counter increment skips item recomposition entirely.",
                        headerColor = Color(0xFF1B5E20),
                        textColor = Color(0xFFE8F5E9),
                    )
                }

                // ── Optimized items — receive only what they render ──────────
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
}
