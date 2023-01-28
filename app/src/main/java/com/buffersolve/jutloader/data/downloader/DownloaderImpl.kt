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
        linkOfConcreteSeria: List<String>,
        names: MutableList<String>
    ) {
        val directory = CreateDirectory().createDirectory()

        val deleteDuplicateNames = LinkedHashSet(names).toMutableList()
        val deleteDuplicateLinks = LinkedHashSet(linkOfConcreteSeria).toMutableList()

        for (url in deleteDuplicateLinks) {
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

            downloadManager.enqueue(request)
        }

    }

}