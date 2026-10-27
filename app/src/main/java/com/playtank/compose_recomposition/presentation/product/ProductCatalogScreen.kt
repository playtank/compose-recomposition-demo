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

            // ── Before section (Unoptimized) ──────────────────────────────────
            item(key = "header_before") {
                SectionHeader(
                    title = "⚠️ Before — Whole State Passed In",
                    subtitle = "Anti-pattern: Counter increment forces all items to recompose.",
                    headerColor = MaterialTheme.colorScheme.errorContainer,
                    textColor = MaterialTheme.colorScheme.onErrorContainer,
                )
            }

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

            // ── After section (Optimized) ─────────────────────────────────────
            item(key = "header_after") {
                SectionHeader(
                    title = "✅ After — Granular Fields Passed In",
                    subtitle = "Optimized: Counter increment skips item recomposition entirely.",
                    headerColor = Color(0xFF1B5E20),
                    textColor = Color(0xFFE8F5E9),
                )
            }

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