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
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavHostController
import com.buffersolve.jutloader.presentation.ui.ExceptionState
import com.buffersolve.jutloader.presentation.ui.input
import com.buffersolve.jutloader.presentation.ui.viewModel
import com.buffersolve.jutloader.R

@Composable
fun SeasonPeakList(
    controller: NavHostController,
    numberSeasonsList: List<String>,
    linkSeriesList: List<String>,
    seriesList: List<String>,
    userAgent: String,
    viewLifecycleOwner: LifecycleOwner,
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
                if (ExceptionState.lastOrNull()?.isEmpty() == true) {
                    TextButton(
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.surface,
                            contentColor = MaterialTheme.colorScheme.onSurface
                        ),
                        onClick = {
                            if (ExceptionState.last().isEmpty()) {
                                val index = numberSeasonsList.indexOf(it)

                                viewModel.isOnlyOneSeason.observe(viewLifecycleOwner) {
                                    if (it) {
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