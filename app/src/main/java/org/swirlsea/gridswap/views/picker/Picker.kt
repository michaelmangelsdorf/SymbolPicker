package org.swirlsea.symbolpicker.views.picker

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import kotlinx.coroutines.launch
import kotlin.math.roundToInt


private const val PREF_NAME = "RecentSymbolsPrefs"
private const val RECENT_SYMBOLS_KEY = "RecentSymbols"

fun saveRecentSymbols(context: Context, recentSymbols: List<String>) {
    val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    val json = Gson().toJson(recentSymbols)
    sharedPreferences.edit().putString(RECENT_SYMBOLS_KEY, json).apply()
}

fun loadRecentSymbols(context: Context): List<String> {
    val sharedPreferences = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    val json = sharedPreferences.getString(RECENT_SYMBOLS_KEY, null)
    return if (json != null) {
        val type = object : TypeToken<List<String>>() {}.type
        Gson().fromJson(json, type)
    } else {
        emptyList()
    }
}



@RequiresApi(Build.VERSION_CODES.VANILLA_ICE_CREAM)
@Composable
fun Picker(
    columns: Int = 5,
    onSymbolSelected: ((String) -> Unit)? = null,
    onWasPicked: ((String) -> Unit)? = null,
    onWasUnpicked: (() -> Unit)? = null,
) {
    val context = LocalContext.current

    val selectedSymbol = remember { mutableStateOf(" ") }
    val isPicked = remember { mutableStateOf(false) }
    val recentSymbolsMode = remember { mutableStateOf(false) }
    val recentSymbols = remember { mutableStateListOf<String>() }

    var offset by remember { mutableStateOf(0x1F97F) }
    val totalSymbols = columns * columns

    var symbols by remember { mutableStateOf<List<String>>(emptyList()) }

    val fillPicker: (Int, Boolean) -> Unit = { newOffset, recentMode ->
        offset = newOffset
        symbols = if (!recentMode) {
            List(totalSymbols) { i -> Character.toString(newOffset + i) }
        } else {
            (recentSymbols + List(totalSymbols) { "" }).take(totalSymbols)
        }
    }

    val spacing = 2.dp
    val itemScale = 0.6f
    val fontScaleFactor = 0.6f
    val configuration = LocalConfiguration.current
    val screenWidth = configuration.screenWidthDp.dp

    val itemSize = remember(screenWidth, columns, spacing) {
        (screenWidth - spacing * (columns + 1)) / columns * itemScale
    }

    val fontSize: TextUnit = with(LocalDensity.current) {
        (itemSize.toPx() * fontScaleFactor).toSp()
    }

    val dragOffset = remember { Animatable(0f) }
    val scope = rememberCoroutineScope()
    val dragThreshold = with(LocalDensity.current) { 80.dp.toPx() }

    // Load recent symbols from SharedPreferences
    LaunchedEffect(Unit) {
        recentSymbols.clear()
        recentSymbols.addAll(loadRecentSymbols(context))
        fillPicker(offset, recentSymbolsMode.value)
    }

    // Save recent symbols on changes
    LaunchedEffect(recentSymbols) {
        saveRecentSymbols(context, recentSymbols)
    }

    Column(
        modifier = Modifier.pointerInput(recentSymbolsMode.value) {
            detectHorizontalDragGestures(
                onHorizontalDrag = { change, dragAmount ->
                    change.consume()
                    if (!recentSymbolsMode.value) {
                        scope.launch {
                            dragOffset.snapTo(dragOffset.value + dragAmount)
                        }
                    }
                },
                onDragEnd = {
                    scope.launch {
                        if (!recentSymbolsMode.value) {
                            when {
                                dragOffset.value > dragThreshold -> {
                                    fillPicker((offset - 25).coerceAtLeast(0), false)
                                }
                                dragOffset.value < -dragThreshold -> {
                                    fillPicker(offset + 25, false)
                                }
                            }
                        }
                        dragOffset.animateTo(0f)
                    }
                }
            )
        }
    ) {
        TopBar(
            itemSize = itemSize,
            selectedSymbol = selectedSymbol.value,
            isPicked = isPicked.value,
            onUnpick = {
                isPicked.value = false
                onWasUnpicked?.invoke()
            },
            onFastBackClick = {
                fillPicker((offset - 250).coerceAtLeast(0), recentSymbolsMode.value)
            },
            onLeftClick = {
                fillPicker((offset - 25).coerceAtLeast(0), recentSymbolsMode.value)
            },
            onRightClick = {
                fillPicker(offset + 25, recentSymbolsMode.value)
            },
            onFastForwardClick = {
                fillPicker(offset + 250, recentSymbolsMode.value)
            },
            onJumpToEmoji = {
                fillPicker(0x1F600, false)
            },
            onToggleRecent = {
                recentSymbolsMode.value = !recentSymbolsMode.value
                fillPicker(offset, recentSymbolsMode.value)
            },
            recentSymbolsMode = recentSymbolsMode.value,
            fontSize = fontSize,
        )

        PickerGrid(
            symbols = symbols,
            itemSize = itemSize,
            fontSize = fontSize,
            spacing = spacing,
            onSymbolDoubleTapped = { symbol ->
                if (symbol.isNotBlank()) {
                    isPicked.value = true
                    onWasPicked?.invoke(symbol)
                    selectedSymbol.value = symbol
                    onSymbolSelected?.invoke(symbol)

                    if (!recentSymbolsMode.value) {
                        recentSymbols.remove(symbol)
                        recentSymbols.add(0, symbol)
                        if (recentSymbols.size > 49) {
                            recentSymbols.removeLast()
                        }
                        saveRecentSymbols(context, recentSymbols)
                    }

                    recentSymbolsMode.value = false
                    fillPicker(offset, recentSymbolsMode.value)
                }
            },
            onSymbolTapped = {
                isPicked.value = false
                onWasUnpicked?.invoke()
            },
            modifier = Modifier.offset { IntOffset(dragOffset.value.roundToInt(), 0) }
        )

        BottomBar(
            selectedSymbol = selectedSymbol.value,
            modifier = Modifier,
            recentSymbolsMode = recentSymbolsMode.value,
            onClearRecents = {
                recentSymbols.clear()
                saveRecentSymbols(context, recentSymbols)
                fillPicker(offset, recentSymbolsMode.value)
            }
        )
    }
}

