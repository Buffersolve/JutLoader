package com.buffersolve.jutloader.presentation.ui.compose.dialog.lists

import android.app.DownloadManager
import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.util.Log
import androidx.activity.ComponentActivity
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
import com.buffersolve.jutloader.data.contentprovider.DownloadProgressObserver
import com.buffersolve.jutloader.data.downloader.DownloaderImpl
import com.buffersolve.jutloader.data.receiver.DownloadCompletedReceiver
import com.buffersolve.jutloader.presentation.ui.DialogState
import com.buffersolve.jutloader.presentation.ui.DownloadProgressBar
import com.buffersolve.jutloader.presentation.ui.viewModel

@Composable
fun ResolutionPeakList(
    controller: NavHostController,
    linkSeriesList: List<String>,
    userAgent: String,
    viewLifecycleOwner: LifecycleOwner,
    resList: List<String>,
    context: Context,
//    specificLink: List<String>
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
//                                val id = viewModel.download(
//                                    context = context,
//                                    userAgent = userAgent,
//                                    linkOfConcreteSeries = specificLink.toMutableList(),
//                                    names = specificName.toMutableList()
//                                )

                                val id = DownloaderImpl(context).download (
                                    userAgent = userAgent,
                                    linkOfConcreteSeria = specificLink.toMutableList(),
                                    names = specificName.toMutableList()
                                )

                                val handler = Handler(Looper.getMainLooper())
                                viewModel.progressObserve(
                                    viewLifecycleOwner, context, handler, id
                                )

//                                DownloadProgressObserver(context, handler, id).onChange()

//                                val downloadManager: DownloadManager = context.getSystemService(
//                                    ComponentActivity.DOWNLOAD_SERVICE
//                                ) as DownloadManager
//
//                                val uri = downloadManager.getUriForDownloadedFile(id)

                                val downloadObserver = DownloadProgressObserver(context, handler, id)
                                val contentResolver = context.contentResolver

                                contentResolver.registerContentObserver(Uri.parse("content://downloads/all_downloads/$id"), true, downloadObserver)


                            } else {

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