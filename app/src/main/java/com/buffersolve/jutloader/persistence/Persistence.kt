package com.buffersolve.jutloader.persistence

import android.app.Activity
import android.app.Application
import android.app.DownloadManager
import android.content.Context
import android.net.Uri

class Persistence {

//    val downloadManager by lazy {
//        Context.getD
//        Application.DOWNLOAD_SERVICE as DownloadManager
//    }

    val url = "https://r280201.kujo-jotaro.com/berserk/1/1.720.b8d9630f91a6c533.mp4?hash1=0ce202dacb6329ad649d378fa5b22b86&hash2=48f43a507912c154a84ac2f333899680"

    val downloadRequest = DownloadManager.Request(Uri.parse(url))
        .setAllowedOverMetered(true)
        .setTitle("Download")
        .setNotificationVisibility(
            DownloadManager.Request.VISIBILITY_VISIBLE
        )


//val model = PersistenceModel(downloadId)

}

