package com.buffersolve.jutloader.data.downloader

import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import junit.framework.TestCase.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class DownloaderImplTest {

    private lateinit var context: Context
    private lateinit var downloader: DownloaderImpl

    @Before
    fun setUp() {
        context = InstrumentationRegistry.getInstrumentation().targetContext
        downloader = DownloaderImpl(context)
    }

    @Test
    fun enqueuesDownloadRequests() {
        // Mock the necessary data
        val userAgent = "Test User Agent"
        val linkOfConcreteSeries = listOf("https://example.com/video1.mp4", "https://example.com/video2.mp4")
        val names = mutableListOf("Video 1", "Video 2")

        // Call the method being tested
        val requestId = downloader.download(userAgent, linkOfConcreteSeries, names)

        // Verify that the download requests were enqueued
        val query = DownloadManager.Query().setFilterById(requestId)

        // TODO Reflection REWRITE!
        val downloadManagerField = downloader.javaClass.getDeclaredField("downloadManager")
        downloadManagerField.isAccessible = true
        val downloadManager = downloadManagerField.get(downloader) as DownloadManager

        val cursor = downloadManager.query(query)
        val count = cursor.count
        assertTrue(count > 0)

        // Clean up the downloaded files
//        for (i in 0 until count) {
//            cursor.moveToNext()
//            val localUri = cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI))
//            val file = File(Uri.parse(localUri).path!!)
//            file.delete()
//        }
    }

    @Test
    fun returnsRequestId() {
        // Mock the necessary data
        val userAgent = "Test User Agent"
        val linkOfConcreteSeries = listOf("https://example.com/video1.mp4", "https://example.com/video2.mp4")
        val names = mutableListOf("Video 1", "Video 2")

        // Call the method being tested
        val requestId = downloader.download(userAgent, linkOfConcreteSeries, names)

        // Verify that the returned request ID is valid
        assertTrue(requestId > 0)

        // Clean up the downloaded files
//        val downloadManagerField = downloader.javaClass.getDeclaredField("downloadManager")
//        downloadManagerField.isAccessible = true
//        val downloadManager = downloadManagerField.get(downloader) as DownloadManager
//
//        val query = DownloadManager.Query().setFilterById(requestId)
//        val cursor = downloadManager.query(query)
//        cursor.moveToFirst()
//        val localUri = cursor.getString(cursor.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI))
//        val file = File(Uri.parse(localUri).path!!)
//        file.delete()
    }


}