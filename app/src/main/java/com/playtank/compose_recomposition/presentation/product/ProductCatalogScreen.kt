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
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.playtank.compose_recomposition.presentation.product.components.CounterSection
import com.playtank.compose_recomposition.presentation.product.components.ProductItem

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

/**
 * Stateless content composable.
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

                items(
                    items = uiState.products,
                    key = { "unoptimized_${it.id}" },
                ) { product ->
                    ProductItem(
                        uiState = uiState,
                        productId = product.id,
                        onToggleFavorite = {
                            onIntent(ProductCatalogUiIntent.ToggleFavorite(product.id))
                        },
                    )
                }

            }
        }
    }
}
