//package com.buffersolve.jutloader.data.downloader
//
////import android.app.DownloadManager
////import android.content.Context
////import androidx.test.core.app.ApplicationProvider
////import com.buffersolve.jutloader.Constants.Companion.USER_AGENT
////import io.mockk.every
////import io.mockk.mockk
////import junit.framework.TestCase.assertEquals
////import org.junit.Before
////import org.junit.Test
////import org.junit.runner.RunWith
////import org.junit.runners.JUnit4
////
////@RunWith(JUnit4::class)
////class DownloaderImplTest {
////
////    private lateinit var downloader: DownloaderImpl
////    private lateinit var context: Context
////    private lateinit var downloadManager: DownloadManager
////
////    @Before
////    fun setUp() {
////        context = ApplicationProvider.getApplicationContext()
////        downloadManager = mockk(relaxed = true)
////        every {
////            context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
////        } returns downloadManager
////        downloader = DownloaderImpl(context)
////    }
////
////    @Test
////    fun testDownload() {
////        val userAgent = USER_AGENT
////        val links = listOf("http://example.com/1.mp4", "http://example.com/2.mp4")
////        val names = mutableListOf("video1", "video2")
////        every { downloadManager.enqueue(any()) } returns 1L
////        val result = downloader.download(userAgent, links, names)
//////        println(result)
////
////        assertEquals(1L, result)
////    }
////
////}
//
//import android.app.DownloadManager
//import android.content.Context
//import android.net.Uri
//import android.os.Environment
//import androidx.activity.ComponentActivity
//import androidx.arch.core.executor.testing.InstantTaskExecutorRule
//import io.mockk.every
//import io.mockk.mockk
//import io.mockk.mockkStatic
//import org.junit.Rule
//import org.junit.jupiter.api.Assertions.assertEquals
//import org.junit.jupiter.api.BeforeEach
//import org.junit.jupiter.api.Test
//import java.io.File
//
//class DownloaderImplTest {
//
////    @get:Rule
////    val rule = InstantTaskExecutorRule()
//
//    private lateinit var mockContext: Context
//    private lateinit var mockDownloadManager: DownloadManager
//
//    @BeforeEach
//    fun setUp() {
//        mockContext = mockk(relaxed = true)
//        mockDownloadManager = mockk(relaxed = true)
//        every {
//            mockContext.getSystemService(ComponentActivity.DOWNLOAD_SERVICE)
//        } returns mockDownloadManager
//
//
//
//    }
//
//    @Test
//    fun `test download method`() {
//        // Given
//        val downloader = DownloaderImpl(mockContext)
//        val userAgent = "test-user-agent"
//        val links = listOf("http://example.com/video1.mp4", "http://example.com/video2.mp4")
//        val names = mutableListOf("video1", "video2")
//
/////
//        mockkStatic(Uri::class)
////        val env: Environment = mockk(relaxed = true)
//        mockkStatic(Environment::class)
////        every { Environment.getExternalStorageDirectory() }
/////
//
//        // When
//        downloader.download(userAgent, links, names)
//
//        // Then
//        links.forEachIndexed { index, link ->
//            val name = names[index]
//            val expectedUri = Uri.fromFile(File("$name.mp4"))
//            val expectedRequest = DownloadManager.Request(Uri.parse(link))
//                .addRequestHeader("User-Agent", userAgent)
//                .setAllowedOverMetered(true)
//                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
//                .setDestinationUri(expectedUri)
//            every {
//                mockDownloadManager.enqueue(expectedRequest)
//            } returns index.toLong()
//        }
//    }
//
//    @Test
//    fun `test download method returns correct result`() {
//        // Given
//        val downloader = DownloaderImpl(mockContext)
//        val userAgent = "test-user-agent"
//        val links = listOf("http://example.com/video1.mp4", "http://example.com/video2.mp4")
//        val names = mutableListOf("video1", "video2")
//
//        links.forEachIndexed { index, link ->
//            val name = names[index]
//            val expectedUri = Uri.fromFile(File("$name.mp4"))
//            val expectedRequest = DownloadManager.Request(Uri.parse(link))
//                .addRequestHeader("User-Agent", userAgent)
//                .setAllowedOverMetered(true)
//                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
//                .setDestinationUri(expectedUri)
//            every {
//                mockDownloadManager.enqueue(expectedRequest)
//            } returns index.toLong()
//        }
//
//        // When
//        val result = downloader.download(userAgent, links, names)
//
//        // Then
//        assertEquals(links.lastIndex.toLong(), result)
//    }
//}