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
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleCoroutineScope
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.buffersolve.jutloader.presentation.ui.*
import com.buffersolve.jutloader.presentation.ui.compose.dialog.lists.ResolutionPeakList
import com.buffersolve.jutloader.presentation.ui.compose.dialog.lists.SeasonPeakList
import com.buffersolve.jutloader.presentation.ui.compose.dialog.lists.SeriesPeakList
import kotlinx.coroutines.launch

@Composable
fun NavigationDialog(
    viewLifecycleOwner: LifecycleOwner,
    userAgent: String,
    context: Context,
    viewModel: JutLoaderViewModel,
    lifecycleScope: LifecycleCoroutineScope,
) {

    // Lists
    val seasonList = remember { mutableStateOf(listOf<String>()) }
    val seasonLink = remember { mutableStateOf(listOf<String>()) }

//    val seasons = remember { mutableStateOf(hashMapOf()) }

    // Episodes
    val seriesList = remember { mutableStateOf(listOf<String>()) }
    val seriesLink = remember { mutableStateOf(listOf<String>()) }

    // Resolution
    val resList = remember { mutableStateOf(listOf<String>()) }

    // Exception
    LaunchedEffect(key1 = 1) {

        // Exception
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.isOnlyOneSeason.collect {
//                    Log.d("EXCEPTIONNNN22", it.toString())
                    ExceptionState.add(it.exception.toString())
                    if (it.exception == true) seasonList.value = listOf("Error, try a different name")
//                    Log.d("EXCEPTIONNNN22", ExceptionState.last().toString())

                }
            }
        }

        // Season
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.season.collect { season ->
//                    Log.d("SEASONVALUE", season.toString())
                    seasonList.value = season.season.keys.toList()
                    seasonLink.value = season.season.entries.map { it.value }
                }
            }
        }

        // Episodes
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.episodes.collect { episodes ->
//                    Log.d("SEASONVALUE", it.toString())
                    seriesList.value = episodes.episodes.keys.toList()
                    seriesLink.value = episodes.episodes.entries.map { it.value }
                }
            }
        }

        // Resolution
        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.resolution.collect {
                    Log.d("SEASONVALUE", it.toString())
                    resList.value = it.res

                }
            }
        }


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
                            viewModel,
                            lifecycleScope
                        )
                    }
                    composable("SeriesPeakList") {
                        SeriesPeakList(
                            controller,
                            seriesList.value,
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
                            viewModel,
                            lifecycleScope
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