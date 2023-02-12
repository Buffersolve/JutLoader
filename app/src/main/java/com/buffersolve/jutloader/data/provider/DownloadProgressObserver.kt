package com.buffersolve.jutloader.data.provider

import android.app.DownloadManager
import android.content.Context
import android.database.ContentObserver
import android.os.Handler
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import kotlinx.coroutines.flow.*

class DownloadProgressObserver (
    context: Context,
    handler: Handler,
    private val downloadId: Long
) : ContentObserver(handler) {

    private val _progress = MutableStateFlow(0L)
    val progress: StateFlow<Long> = _progress.asStateFlow()

//    var progressPublic: Long = 0L

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

            _progress.value = progress
//            val callback: () -> Long = { progress }
//            setProgress(callback)

            cursor.close()
        }
    }

    //    private fun setProgress(callback: () -> Long) {
//        progressPublic = callback()
////        Log.d("ProgressDebug", progressPublic.toString())
//    }
//
//    fun getProgress(): StateFlow<Long> {
//        val progress: StateFlow<Long> = _progress.asStateFlow()
//        return progress
//    }


}