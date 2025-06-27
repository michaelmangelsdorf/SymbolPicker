

package org.swirlsea.symbolpicker.views.picker

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.itemsIndexed
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp

@Composable
fun PickerGrid(
    symbols: List<String>,
    itemSize: Dp,
    fontSize: TextUnit,
    spacing: Dp = 2.dp,
    modifier: Modifier = Modifier,
    onSymbolDoubleTapped: ((String) -> Unit)? = null,
    onSymbolTapped: (() -> Unit)? = null,
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(5),
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = spacing),
        horizontalArrangement = Arrangement.spacedBy(spacing),
        verticalArrangement = Arrangement.spacedBy(spacing)
    ) {
        itemsIndexed(symbols, key = { index, _ -> index }) { index, symbol ->
            SymbolCard(
                symbol = symbol,
                fontSize = fontSize,
                onDoubleTap = { onSymbolDoubleTapped?.invoke(symbol) },
                onTap = { onSymbolTapped?.invoke() },
                modifier = Modifier.size(itemSize)
            )
        }
    }
}

