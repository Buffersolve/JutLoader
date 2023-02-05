package com.buffersolve.jutloader.presentation.ui.compose

import android.util.Log
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LifecycleOwner
import com.buffersolve.jutloader.presentation.ui.*

@Composable
fun ButtonSearch(
    viewLifecycleOwner: LifecycleOwner,
    viewModel: JutLoaderViewModel
//    exceptionState: MutableState<String>,
) {

    OutlinedButton(
        modifier = Modifier
            .fillMaxWidth()
            .padding(start = 24.dp, end = 24.dp),
        onClick = {
            if (input.isNotEmpty() && input.length != 1) {
                DialogState.add(true)

                viewModel.isOnlyOneSeason(input, webViewAgent)
                viewModel.isOnlyOneSeason.observe(viewLifecycleOwner) {
                    if (it && ExceptionState.last().isEmpty()) {
                        viewModel.getOnlyOneSeasons(url = input, userAgent = webViewAgent)
                    } else if (!it && ExceptionState.last().isEmpty()) {
                        viewModel.getSeasons(input, webViewAgent)
                    }
                }
            }
        },
    ) {
        Text(text = "Search & Download")
    }
}