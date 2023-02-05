package com.buffersolve.jutloader.data.provider

import android.app.DownloadManager
import android.content.Context
import android.database.ContentObserver
import android.os.Handler
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class DownloadProgressObserver(
    context: Context,
    handler: Handler,
    private val downloadId: Long
) : ContentObserver(handler) {

    private val _progress: MutableLiveData<Long> = MutableLiveData()
    val progress: LiveData<Long>
        get() = _progress

    private val downloadManager: DownloadManager = context.getSystemService(
        ComponentActivity.DOWNLOAD_SERVICE
    ) as DownloadManager

    override fun onChange(selfChange: Boolean) {
        super.onChange(selfChange)

        val query = DownloadManager.Query().setFilterById(downloadId)
        val cursor = downloadManager.query(query)

        if (cursor != null && cursor.moveToFirst()) {
            val bytesDownloaded =
                cursor.getLong(cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR))
            val bytesTotal =
                cursor.getLong(cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_TOTAL_SIZE_BYTES))

            val progress: Long = (bytesDownloaded * 100 / bytesTotal)

            _progress.postValue(progress)

            cursor.close()
        }
    }


}