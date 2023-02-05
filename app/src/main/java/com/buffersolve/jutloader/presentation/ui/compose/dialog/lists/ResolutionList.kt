package com.buffersolve.jutloader.presentation.ui.compose.dialog.lists

import android.content.Context
import android.os.Handler
import android.os.Looper
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavHostController
import com.buffersolve.jutloader.presentation.ui.*

@Composable
fun ResolutionPeakList(
    controller: NavHostController,
    linkSeriesList: List<String>,
    userAgent: String,
    viewLifecycleOwner: LifecycleOwner,
    resList: List<String>,
    context: Context,
    viewModel: JutLoaderViewModel
) {

    Column {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (resList.isEmpty()) CircularProgressIndicator()
        }

        var specificLink: List<String>
        var specificName: List<String>

        LazyColumn {
            items(resList) {
                TextButton(
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.surface,
                        contentColor = MaterialTheme.colorScheme.onSurface
                    ),
                    onClick = {

                        val index = resList.indexOf(it)

                        viewModel.getSpecificLinkSeries(
                            linkSeriesList,
                            userAgent,
                            resList[index].replace("p", "")
                        )

                        viewModel.specificLinks.observe(viewLifecycleOwner) {
                            specificLink = it.linkToSpecificSeries
                            specificName = it.listOfSeriesName

                            if (specificLink.isNotEmpty()) {

                                val id: Long = viewModel.download(
                                    userAgent = userAgent,
                                    linkOfConcreteSeries = specificLink.toMutableList(),
                                    names = specificName.toMutableList()
                                )

                                val handler = Handler(Looper.getMainLooper())
                                viewModel.progressObserve(
                                    viewLifecycleOwner, context, handler, id
                                )

                                SeriesSnapshotStateList.clear()
                                SeriesLinkSnapshotStateList.clear()

                            }
                        }

                        DialogState.add(false)
                        controller.popBackStack(
                            "SeasonPeakList",
                            inclusive = false,
                            saveState = false
                        )
                    }
                ) {
                    Text(
                        text = it,
                        style = TextStyle(fontSize = 20.sp),
                    )
                }
            }
        }
    }
}