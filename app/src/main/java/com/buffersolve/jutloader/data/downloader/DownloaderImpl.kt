package com.buffersolve.jutloader.data.downloader

import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.activity.ComponentActivity
import com.buffersolve.jutloader.data.util.CreateDirectory
import com.buffersolve.jutloader.domain.downloader.Downloader
import java.io.File

class DownloaderImpl(
    context: Context
) : Downloader {

    // DM
    private val downloadManager: DownloadManager = context.getSystemService(
        ComponentActivity.DOWNLOAD_SERVICE
    ) as DownloadManager

    override fun download(
//        url: List<String>,
        userAgent: String,
        linkOfConcreteSeries: List<String>,
        names: MutableList<String>
    ): Long {
        val directory = CreateDirectory().createDirectory()

        val deleteDuplicateNames = LinkedHashSet(names).toMutableList()
        val deleteDuplicateLinks = LinkedHashSet(linkOfConcreteSeries).toMutableList()

        val requestList = mutableListOf<Long>()
        var requestLong = 1L

        for (url in deleteDuplicateLinks) {
            Log.d("NAMEFILE", deleteDuplicateNames[deleteDuplicateLinks.indexOf(url)])
            val request = DownloadManager.Request(Uri.parse(url))
                .addRequestHeader("User-Agent", userAgent)
                .setAllowedOverMetered(true)
                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                .setDestinationUri(
                    Uri.fromFile(
                        File(
                            directory,
                            "${deleteDuplicateNames[deleteDuplicateLinks.indexOf(url)]}.mp4"
                        )
                    )
                )

//            val enqueue = downloadManager.enqueue(request)
            requestLong = downloadManager.enqueue(request)
//            requestList.add(enqueue)
//            return enqueue
        }

        return requestLong
    }



}