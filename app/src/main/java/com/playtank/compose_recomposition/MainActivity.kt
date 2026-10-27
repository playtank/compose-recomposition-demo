package com.playtank.compose_recomposition

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.playtank.compose_recomposition.presentation.product.ProductCatalogScreen
import com.playtank.compose_recomposition.ui.theme.ComposeRecompositionTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ComposeRecompositionTheme {
                ProductCatalogScreen()
            }
        }
    }
}
