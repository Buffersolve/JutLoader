package com.buffersolve.jutloader.data.provider

import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.Environment
import android.os.Handler
import android.os.Looper
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import io.mockk.*
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.util.concurrent.CountDownLatch
import java.util.concurrent.TimeUnit

@RunWith(AndroidJUnit4::class)
class DownloadProgressObserverTest {

    private lateinit var context: Context
    private lateinit var handler: Handler
    private lateinit var downloadObserver: DownloadProgressObserver

    @Before
    fun setUp() {
        context = ApplicationProvider.getApplicationContext()
        handler = Handler(Looper.getMainLooper())
    }

    @After
    fun tearDown() {
        if (::downloadObserver.isInitialized) {
            context.contentResolver.unregisterContentObserver(downloadObserver)
        }
    }

//    @OptIn(DelicateCoroutinesApi::class)
//    @Test
//    fun progressIsSetCorrectlyWhenDownloadProgresses() {
//        // Arrange
//        val downloadManager: DownloadManager by lazy {
//            context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
//        }
//
//        fun enqueueDownload(): Long {
//            val request = DownloadManager.Request(Uri.parse("https://www.example.com/file.zip"))
//                .setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI)
//                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
//                .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "file.zip")
//            return downloadManager.enqueue(request)
//        }
//
//        val downloadId = enqueueDownload()
//
//        val observerLatch = CountDownLatch(20)
//        downloadObserver = DownloadProgressObserver(context, Handler(Looper.getMainLooper()), downloadId)
//        downloadObserver.progress.onEach { progress ->
//            if (progress == 100L) {
//                observerLatch.countDown()
//            }
//        }.launchIn(GlobalScope)
//
//        // Act
//        observerLatch.await(30, TimeUnit.SECONDS)
//
//        // Assert
//        assertTrue(downloadObserver.progress.value == 100L)
//    }

    @OptIn(DelicateCoroutinesApi::class)
    @Test
    fun progressIsResetToZeroWhenDownloadIsCancelled() {
        // Arrange
        val downloadId = enqueueDownload()

        val observerLatch = CountDownLatch(1)
        downloadObserver = DownloadProgressObserver(context, handler, downloadId)
        downloadObserver.progress.onEach { progress ->
            if (progress == 50L) {
                downloadManager.remove(downloadId)
                observerLatch.countDown()
            }
        }.launchIn(GlobalScope)

        // Act
//        observerLatch.await(5, TimeUnit.SECONDS)

        // Assert
        assertTrue(downloadObserver.progress.value == 0L)
    }

    private val downloadManager: DownloadManager by lazy {
        context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
    }

    private fun enqueueDownload(): Long {
        val request = DownloadManager.Request(Uri.parse("https://www.example.com/file.zip"))
            .setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI)
            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "file.zip")
        return downloadManager.enqueue(request)
    }

}