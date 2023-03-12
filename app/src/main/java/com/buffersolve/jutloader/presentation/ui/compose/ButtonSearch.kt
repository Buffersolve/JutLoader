package com.buffersolve.jutloader.presentation.ui.compose

import android.util.Log
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import com.buffersolve.jutloader.presentation.ui.*
import kotlinx.coroutines.launch

@Composable
fun ButtonSearch(
    viewLifecycleOwner: LifecycleOwner,
    viewModel: JutLoaderViewModel,
    lifecycleScope: LifecycleCoroutineScope,

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

//                Log.d("EXCEPLIDT", ExceptionState.last())

                lifecycleScope.launch {
                    viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                        viewModel.isOnlyOneSeason.collect {
                            if (it.isOnlyOneSeason == true && ExceptionState.last() == "null") {
                                viewModel.getOnlyOneSeasons(url = input, userAgent = webViewAgent)
                            } else if (it.isOnlyOneSeason == false && ExceptionState.last() == "null") {
                                viewModel.getSeasons(input, webViewAgent)
                            }
                        }
                    }
                }

//                viewModel.isOnlyOneSeason.observe(viewLifecycleOwner) {
//                    if (it && ExceptionState.last().isEmpty()) {
//                        viewModel.getOnlyOneSeasons(url = input, userAgent = webViewAgent)
//                    } else if (!it && ExceptionState.last().isEmpty()) {
//                        viewModel.getSeasons(input, webViewAgent)
//                    }
//                }
            }
        },
    ) {
        Text(text = "Search & Download")
    }
}