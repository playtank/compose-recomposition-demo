package com.playtank.compose_recomposition.presentation.product.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

@Composable
internal fun ProductItemOptimized(
    name: String,
    price: Double,
    isFavorite: Boolean,
    onToggleFavorite: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val recompositionCount = remember { intArrayOf(0) }
    recompositionCount[0]++

    ProductItemLayout(
        name = name,
        price = price,
        isFavorite = isFavorite,
        onToggleFavorite = onToggleFavorite,
        badgeCount = recompositionCount[0],
        badgeColor = Color(0xFF2E7D32),       // 绿色：代表优化成功 (Skipped)
        badgeTextColor = Color(0xFFE8F5E9),
        modifier = modifier,
    )
}