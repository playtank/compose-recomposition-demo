package com.playtank.compose_recomposition.presentation.product.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

/**
 * A coloured label bar that separates the "Before" (unoptimized) and
 * "After" (optimized) sections in the demo list.
 *
 * @param title   Bold heading line (e.g. "⚠️ Before — whole state passed in").
 * @param subtitle Explanatory subtitle shown below the title.
 * @param headerColor Background colour of the bar.
 * @param textColor   Foreground colour for both text elements.
 */
@Composable
internal fun SectionHeader(
    title: String,
    subtitle: String,
    headerColor: Color,
    textColor: Color,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(headerColor)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(2.dp),
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleSmall,
            fontWeight = FontWeight.Bold,
            color = textColor,
        )
        Text(
            text = subtitle,
            style = MaterialTheme.typography.bodySmall,
            color = textColor,
        )
    }
}
