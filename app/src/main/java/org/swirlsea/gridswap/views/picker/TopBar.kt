package org.swirlsea.symbolpicker.views.picker

import android.util.Log
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.NavigateBefore
import androidx.compose.material.icons.automirrored.filled.NavigateNext
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.Icon
import androidx.compose.material3.Text // Make sure Text is imported
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily // If you use custom fonts, keep this
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp

@Composable
fun TopBar(
    itemSize: Dp,
    selectedSymbol: String?,
    onFastBackClick: () -> Unit,
    onLeftClick: () -> Unit,
    onRightClick: () -> Unit,
    onFastForwardClick: () -> Unit,
    onJumpToEmoji: () -> Unit,
    onToggleRecent: () -> Unit,
    fontSize: TextUnit,
    isPicked: Boolean,
    onUnpick: () -> Unit,
    recentSymbolsMode: Boolean,
) {

    val initialBorderColor = if (isPicked) Color.Red else Color.White
    val targetBorderColor = if (isPicked) Color.Red.copy(alpha = 0.5f) else Color.White // Slightly less opaque red
    val topBarItemSize = itemSize * 0.8f

    // Animation for the border color alpha
    val animatedBorderAlpha by animateFloatAsState(
        targetValue = if (isPicked) 1f else 0f, // From fully opaque to transparent when not picked
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 800, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse // Pulsates back and forth
        ), label = "borderAlphaAnimation"
    )

    // Derived color for the border, so it pulses when isPicked is true
    val pulsingBorderColor = if (isPicked) Color.Green.copy(alpha = animatedBorderAlpha) else Color.White


    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(topBarItemSize)
            .padding(horizontal = 8.dp)
            .background(Color.White),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            imageVector = Icons.Filled.FastRewind,
            contentDescription = "Fast Backward",
            modifier = Modifier
                .size(topBarItemSize)
                .alpha(if (recentSymbolsMode || isPicked) 0f else 1f)
                .clickable(enabled = !recentSymbolsMode && !isPicked) { onFastBackClick() }
        )
        Spacer(modifier = Modifier.width(8.dp))

        Icon(
            imageVector = Icons.AutoMirrored.Filled.NavigateBefore,
            contentDescription = "Left",
            modifier = Modifier
                .size(topBarItemSize)
                .alpha(if (recentSymbolsMode|| isPicked) 0f else 1f)
                .clickable(enabled = !recentSymbolsMode && !isPicked) { onLeftClick() }
        )

        Spacer(modifier = Modifier.weight(1f))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Filled.SentimentSatisfiedAlt,
                contentDescription = "Emoji",
                modifier = Modifier
                    .size(topBarItemSize)
                    .padding(horizontal = 8.dp)
                    .alpha(if (recentSymbolsMode|| isPicked) 0f else 1f)
                    .clickable(enabled = !recentSymbolsMode && !isPicked) { onJumpToEmoji() }
            )

            Icon(
                imageVector = Icons.Filled.AccessTime,
                contentDescription = "Recent",
                modifier = Modifier
                    .size(topBarItemSize)
                    .padding(horizontal = 4.dp)
                    .alpha(if (isPicked) 0f else 1f)
                    .clickable { if (!isPicked) onToggleRecent() }
                    .scale(if (recentSymbolsMode) 1.15f else 1f)
            )

            Spacer(modifier = Modifier.width(8.dp))

            Box(
                modifier = Modifier
                    .size(topBarItemSize)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color.White)
                    .alpha(if (recentSymbolsMode) 0f else 1f)
                    .border(
                        width = 3.dp,
                        color = pulsingBorderColor, // Use the animated color here
                        shape = RoundedCornerShape(12.dp)
                    )
                    .clickable(
                        enabled = true // Always clickable
                    ) {
                            onUnpick()
                    },
                contentAlignment = Alignment.Center
            ) {
                Text(
                    fontFamily = FontFamily(
                        Font(org.swirlsea.symbolpicker.R.font.notosans2, FontWeight.Normal)
                    ),
                    text = selectedSymbol ?: "\u00A0", // Use non-breaking space for empty
                    fontSize = fontSize,
                    color = Color.Black // Ensure text is visible
                )
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        Icon(
            imageVector = Icons.AutoMirrored.Filled.NavigateNext,
            contentDescription = "Right",
            modifier = Modifier
                .size(itemSize)
                .alpha(if (recentSymbolsMode|| isPicked) 0f else 1f)
                .clickable(enabled = !recentSymbolsMode && !isPicked) { onRightClick() }
        )
        Spacer(modifier = Modifier.width(8.dp))

        Icon(
            imageVector = Icons.Filled.FastForward,
            contentDescription = "Fast Forward",
            modifier = Modifier
                .size(itemSize)
                .alpha(if (recentSymbolsMode|| isPicked) 0f else 1f)
                .clickable(enabled = !recentSymbolsMode && !isPicked) { onFastForwardClick() }
        )
    }
}