package com.buffersolve.jutloader.presentation.ui.compose.dialog.lists

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavHostController
import com.buffersolve.jutloader.presentation.ui.ExceptionState
import com.buffersolve.jutloader.presentation.ui.input
import com.buffersolve.jutloader.R
import com.buffersolve.jutloader.presentation.ui.JutLoaderViewModel
import com.buffersolve.jutloader.presentation.ui.webViewAgent
import kotlinx.coroutines.launch

@Composable
fun SeasonPeakList(
    controller: NavHostController,
    numberSeasonsList: List<String>,
    linkSeriesList: List<String>,
    seriesList: List<String>,
    userAgent: String,
    viewLifecycleOwner: LifecycleOwner,
    viewModel: JutLoaderViewModel,
    lifecycleScope: LifecycleCoroutineScope,


//    exceptionState: MutableState<String>
) {
    Column {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (numberSeasonsList.isEmpty()) CircularProgressIndicator()
        }

        LazyColumn {
            items(numberSeasonsList) {
                if (ExceptionState.lastOrNull() == "null") {
                    TextButton(
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.surface,
                            contentColor = MaterialTheme.colorScheme.onSurface
                        ),
                        onClick = {
                            if (ExceptionState.last() == "null") {
                                val index = numberSeasonsList.indexOf(it)

                                lifecycleScope.launch {
                                    viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                                        viewModel.isOnlyOneSeason.collect {
                                            if (it.isOnlyOneSeason == true) {
                                                viewModel.getOnlyOneSeries(
                                                    url = input,
                                                    userAgent = userAgent
                                                )
                                            } else {
                                                viewModel.getSeries(
                                                    url = linkSeriesList[index],
                                                    userAgent = userAgent
                                                )
                                            }
                                        }
                                    }
                                }

//                                viewModel.isOnlyOneSeason.observe(viewLifecycleOwner) {
//                                    if (it) {
//                                        viewModel.getOnlyOneSeries(
//                                            url = input,
//                                            userAgent = userAgent
//                                        )
//                                    } else {
//                                        viewModel.getSeries(
//                                            url = linkSeriesList[index],
//                                            userAgent = userAgent
//                                        )
//                                    }
//                                }
                                controller.navigate("SeriesPeakList")
                            }
                        }
                    ) {
                        Text(
                            text = it,
                            style = TextStyle(fontSize = 20.sp)
                        )
                    }
                } else {
                    Box {
                        Icon(
                            modifier = Modifier.align(Alignment.CenterStart).size(27.dp),
                            painter = painterResource(id = R.drawable.outline_error),
                            tint = MaterialTheme.colorScheme.error,
                            contentDescription = null
                        )
                        Text(
                            text = it,
                            color = MaterialTheme.colorScheme.error,
                            fontSize = 20.sp,
                            modifier = Modifier.padding(start = 40.dp, bottom = 2.dp)
                        )
                    }
                }
            }
        }
    }
}