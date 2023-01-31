package com.buffersolve.jutloader.presentation.ui.compose.dialog

import android.content.Context
import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.buffersolve.jutloader.data.parser.Parser
import com.buffersolve.jutloader.presentation.ui.*
import com.buffersolve.jutloader.presentation.ui.compose.dialog.lists.ResolutionPeakList
import com.buffersolve.jutloader.presentation.ui.compose.dialog.lists.SeasonPeakList
import com.buffersolve.jutloader.presentation.ui.compose.dialog.lists.SeriesPeakList

@Composable
fun NavigationDialog(
    viewLifecycleOwner: LifecycleOwner,
    userAgent: String,
//    exceptionState: MutableState<String>,
    context: Context
) {

    // Lists
    val seasonList = remember { mutableStateOf(listOf<String>()) }
    val seasonLink = remember { mutableStateOf(listOf<String>()) }
    viewModel.season.observe(viewLifecycleOwner) {
        seasonList.value = it.season
        seasonLink.value = it.seasonLink
    }

    // Series
    val seriesList = remember { mutableStateOf(listOf<String>()) }
    val seriesLink = remember { mutableStateOf(listOf<String>()) }
    viewModel.series.observe(viewLifecycleOwner) {
        seriesList.value = it.seria
        seriesLink.value = it.seriaLink
    }

    // Resolution
    val resList = remember { mutableStateOf(listOf<String>()) }
    viewModel.resolution.observe(viewLifecycleOwner) {
        resList.value = it.res
    }

    // Exception
    Parser.exception.observe(viewLifecycleOwner) {
        ExceptionState.add(it)
        if (it.isNotEmpty()) seasonList.value = listOf(it)
    }

    val controller = rememberNavController()

    // Destination Listener
    val destinationChangedListener = remember {
        mutableStateOf("")
    }
    controller.addOnDestinationChangedListener { _, destination, _ ->
        destinationChangedListener.value = destination.route.toString()
    }.toString()

    // Dialog
    if (DialogState.last()) {
        AlertDialog(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 24.dp, bottom = 24.dp)
                .wrapContentHeight()
                .animateContentSize(
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioNoBouncy,
                        stiffness = Spring.StiffnessLow
                    )
                ),
            onDismissRequest = { DialogState.add(false) },
            title = {
                Text(
                    text = "Download",
                    color = MaterialTheme.colorScheme.primary
                )
            },
            text = {
                // BackHandler
                BackHandler {
                    DialogState.add(false)
                    controller.popBackStack("SeasonPeakList", inclusive = false, saveState = false)
                    SeriesSnapshotStateList.clear()
                    SeriesLinkSnapshotStateList.clear()
                    seasonList.value = mutableListOf()
                }

                NavHost(navController = controller, startDestination = "SeasonPeakList") {
                    composable("SeasonPeakList") {
                        SeasonPeakList(
                            controller,
                            seasonList.value,
                            seasonLink.value,
                            seriesList.value,
                            userAgent,
                            viewLifecycleOwner,
//                            exceptionState
                        )
                    }
                    composable("SeriesPeakList") {
                        SeriesPeakList(
                            controller,
                            seriesList.value,
//                            checkedList,
                            seriesLink.value,
                            userAgent,
                        )
                    }
                    composable("ResolutionPeakList") {
                        ResolutionPeakList(
                            controller = controller,
                            SeriesLinkSnapshotStateList,
                            userAgent,
                            viewLifecycleOwner,
                            resList.value,
                            context,
//                            specificLink = specificLink.value
                        )
                    }
                }

            },
            confirmButton = {

                when (destinationChangedListener.value == "SeriesPeakList"
                        && SeriesSnapshotStateList.isNotEmpty()
                ) {
                    true -> TextButton(onClick = {
                        val url = SeriesLinkSnapshotStateList.first()
                        viewModel.getResolution(url, userAgent)

                        controller.navigate("ResolutionPeakList")
                    }) {
                        Text(text = "Confirm")
                    }
                    else -> Text(text = "")
                }
            },
            dismissButton = {
                TextButton(onClick = {

                    DialogState.add(false)
                    controller.popBackStack("SeasonPeakList", inclusive = false, saveState = false)
                    SeriesSnapshotStateList.clear()
                    SeriesLinkSnapshotStateList.clear()
                    seasonList.value = mutableListOf()

                }) {
                    Text(text = "Dismiss")
                }
            }
        )
    }
}