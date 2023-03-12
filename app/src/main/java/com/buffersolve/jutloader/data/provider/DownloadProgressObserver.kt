package com.buffersolve.jutloader.data.provider

import android.app.DownloadManager
import android.content.Context
import android.database.ContentObserver
import android.os.Handler
import androidx.activity.ComponentActivity
import kotlinx.coroutines.flow.*

data class DownloadProgressObserver (
    private val context: Context,
    private val handler: Handler,
    private val downloadId: Long
) : ContentObserver(handler) {

    private val _progress = MutableStateFlow(0L)
    val progress: StateFlow<Long> = _progress.asStateFlow()

    override fun onChange(selfChange: Boolean) {
        super.onChange(selfChange)

        val downloadManager: DownloadManager = context.getSystemService(
            ComponentActivity.DOWNLOAD_SERVICE
        ) as DownloadManager

        val query = DownloadManager.Query().setFilterById(downloadId)
        val cursor = downloadManager.query(query)

        if (cursor != null && cursor.moveToFirst()) {
            val bytesDownloaded =
                cursor.getLong(cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR))
            val bytesTotal =
                cursor.getLong(cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_TOTAL_SIZE_BYTES))

            val progress: Long = (bytesDownloaded * 100 / bytesTotal)

            _progress.value = progress

            cursor.close()
        }
    }
}