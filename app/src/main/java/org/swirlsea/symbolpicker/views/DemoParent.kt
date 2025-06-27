package org.swirlsea.symbolpicker.views

import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import org.swirlsea.symbolpicker.views.picker.Picker


@RequiresApi(Build.VERSION_CODES.VANILLA_ICE_CREAM)
@Composable
fun DemoParent(
    context: Context,
    modifier: Modifier = Modifier
) {
    var pickedSymbol by remember { mutableStateOf("") }
    var isPicked by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
           Picker(
               onWasPicked = { isPicked = true; pickedSymbol = it },
               onWasUnpicked = { isPicked = false; pickedSymbol = "" }
           )
    }
}
