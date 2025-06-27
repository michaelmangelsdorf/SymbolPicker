package org.swirlsea.symbolpicker.views.picker

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp

/**
 * Represents an individual tile in the Picker grid.
 * Handles double-tapping to pick and select a symbol.
 */
@Composable
fun SymbolCard(
    symbol: String,
    fontSize: TextUnit,
    onDoubleTap: (() -> Unit)? = null,
    onTap: (() -> Unit)? = null,
    modifier: Modifier = Modifier,
) {
    Box(
        modifier = modifier // Setting .size!
            .background(Color(0xFF_F2_F2_F2))
            .clip(RoundedCornerShape(5.dp))
            .pointerInput(symbol) {
                detectTapGestures(onDoubleTap = { onDoubleTap?.invoke() })
                detectTapGestures(onTap = { onTap?.invoke() })
            },
        contentAlignment = Alignment.Center
    ) {
        if (symbol.isNotBlank()) {
            Text(
                fontFamily = FontFamily(
                    Font(org.swirlsea.symbolpicker.R.font.notosans2, FontWeight.Normal)
                ),
                text = symbol,
                style = TextStyle(fontSize = fontSize)
            )
        }
    }
}

