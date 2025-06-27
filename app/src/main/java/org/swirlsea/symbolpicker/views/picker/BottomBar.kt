package org.swirlsea.symbolpicker.views.picker

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

fun getUnicodeInfo(symbol: String?): Pair<String, String> {
    val codePoint = symbol?.codePoints()?.toArray()?.firstOrNull() ?: 0x20 // default: space
    val unicodeHex = "U+%04X".format(codePoint)
    val name = try {
        Character.getName(codePoint) ?: "UNKNOWN"
    } catch (e: IllegalArgumentException) {
        "UNKNOWN"
    }
    return unicodeHex to name
}

@Composable
fun BottomBar(
    selectedSymbol: String?,
    recentSymbolsMode: Boolean,
    modifier: Modifier = Modifier,
    onClearRecents: (() -> Unit)? = null
) {
    val (unicodeHex, name) = getUnicodeInfo(selectedSymbol)

    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(48.dp)
            .background(Color.White)
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            modifier = Modifier.alpha(if (recentSymbolsMode) 0f else 1f),
            text = "$unicodeHex – $name",
            style = TextStyle(fontSize = 16.sp, color = Color.Black)
        )

        if (recentSymbolsMode && onClearRecents != null) {
            TextButton(
                onClick = onClearRecents,
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp)
            ) {
                Text(text = "♻️ Clear", fontSize = 16.sp, color = Color.Gray)
            }
        }
    }
}