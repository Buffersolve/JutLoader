package com.buffersolve.jutloader.presentation.ui

import android.app.DownloadManager
import android.content.ContentResolver
import android.content.Context
import android.content.Context.DOWNLOAD_SERVICE
import org.junit.jupiter.api.Test
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Handler
import androidx.activity.ComponentActivity
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.buffersolve.jutloader.Constants.Companion.USER_AGENT
import com.buffersolve.jutloader.data.provider.DownloadProgressObserver
import com.buffersolve.jutloader.domain.model.*
import com.buffersolve.jutloader.domain.usecase.*
import io.mockk.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.take
import kotlinx.coroutines.newSingleThreadContext
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Rule
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach

class JutLoaderViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    // private val mockConnectivityManager: ConnectivityManager = mockk(relaxed = true)
    private val mockConnectivityManager: ConnectivityManager = mockk()
    private val mockGetSeasonUseCase: GetSeasonUseCase = mockk()
    private val mockGetSeriesUseCase: GetSeriesUseCase = mockk()
    private val mockGetOnlyOneSeasonsUseCase: GetOnlyOneSeasonUseCase = mockk()
    private val mockGetOnlyOneSeriesUseCase: GetOnlyOneSeriesUseCase = mockk()
    private val mockGetResolutionUseCase: GetResolutionUseCase = mockk()
    private val mockGetSpecificSeriesLinkUseCase: GetSpecificSeriesLinkUseCase = mockk()
    private val mockIsOnlyOneSeasonUseCase: IsOnlyOneSeasonUseCase = mockk()
    private val mockDownloadUseCase = mockk<DownloadUseCase>()

    private lateinit var viewModel: JutLoaderViewModel

    private val mainThreadSurrogate = newSingleThreadContext("UI thread")

    @BeforeEach
    fun setUp() {

        Dispatchers.setMain(mainThreadSurrogate)

        MockKAnnotations.init(this)
        viewModel = JutLoaderViewModel(
            mockConnectivityManager,
            mockGetSeasonUseCase,
            mockGetSeriesUseCase,
            mockGetOnlyOneSeasonsUseCase,
            mockGetOnlyOneSeriesUseCase,
            mockGetResolutionUseCase,
            mockGetSpecificSeriesLinkUseCase,
            mockIsOnlyOneSeasonUseCase,
            mockDownloadUseCase
        )

        val mockNetwork: Network = mockk()
        val mockNetworkCapabilities: NetworkCapabilities = mockk()

        every { mockConnectivityManager.activeNetwork } returns mockNetwork
        every { mockConnectivityManager.getNetworkCapabilities(mockNetwork) } returns mockNetworkCapabilities
        every { mockNetworkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) } returns true

    }

    @After
    fun tearDown() = unmockkAll()

    @Test
    fun `getSeasons emits result when getSeasonUseCase returns valid result`() = runBlocking {

        val url = "https://example.com"
        val userAgent = USER_AGENT
        val seasons = Season(listOf("Season 1"), mutableListOf("Episode 1", "Episode 2"))
        val expected = Season(listOf("Season 1"), mutableListOf("Episode 1", "Episode 2"))
        every { mockGetSeasonUseCase.execute(url, userAgent) } returns seasons

        viewModel.getSeasons(url, userAgent)

        viewModel.season.take(1).collect { result ->
            assertEquals(expected, result)
        }
    }

    @Test
    fun `getSeries emits result when getSeriesUseCase returns valid result`() = runBlocking {

        val url = "https://example.com"
        val userAgent = USER_AGENT
        val series = Series(listOf("Episode 1"), mutableListOf("/Episode 1/", "/Episode 2/"))
        val expected = Series(listOf("Episode 1"), mutableListOf("/Episode 1/", "/Episode 2/"))
        every { mockGetSeriesUseCase.execute(url, userAgent) } returns series

        viewModel.getSeries(url, userAgent)

        viewModel.series.take(1).collect { result ->
            assertEquals(expected, result)
        }
    }

    @Test
    fun `getOnlyOneSeasons emits result when getOnlyOneSeasonsUseCase returns valid result`() =
        runBlocking {

            val url = "https://example.com"
            val userAgent = USER_AGENT
            val seasons = Season(listOf("Season 1"), mutableListOf("Episode 1", "Episode 2"))
            val expected = Season(listOf("Season 1"), mutableListOf("Episode 1", "Episode 2"))
            every { mockGetOnlyOneSeasonsUseCase.execute(url, userAgent) } returns seasons

            viewModel.getOnlyOneSeasons(url, userAgent)

            viewModel.season.take(1).collect { result ->
                assertEquals(expected, result)
            }
        }

    @Test
    fun `getOnlyOneSeries emits result when getOnlyOneSeriesUseCase returns valid result`() =
        runBlocking {

            val url = "https://example.com"
            val userAgent = USER_AGENT
            val series = Series(listOf("Episode 1"), mutableListOf("/Episode 1/", "/Episode 2/"))
            val expected = Series(listOf("Episode 1"), mutableListOf("/Episode 1/", "/Episode 2/"))
            every { mockGetOnlyOneSeriesUseCase.execute(url, userAgent) } returns series

            viewModel.getOnlyOneSeries(url, userAgent)

            viewModel.series.take(1).collect { result ->
                assertEquals(expected, result)
            }
        }

    @Test
    fun `getResolution emits result when getResolutionUseCase returns valid result`() =
        runBlocking {

            val url = "https://testJutSu.com"
            val userAgent = USER_AGENT
            val resolution = Resolution(listOf("1080p", "720p"))
            val expected = Resolution(listOf("1080p", "720p"))
            every { mockGetResolutionUseCase.execute(url, userAgent) } returns resolution

            viewModel.getResolution(url, userAgent)

            viewModel.resolution.take(1).collect { result ->
                assertEquals(expected, result)
            }
        }

    @Test
    fun `getSpecificLinkSeries emits result when getSpecificLinkSeriesUseCase returns valid result`() =
        runBlocking {

            val listOfLinks = listOf("https://testJutSu.com", "https://testJutSu.com2")
            val userAgent = USER_AGENT
            val resolution = "1080p"
            val specificSeries = SpecificSeries(
                listOf("https://testJutSu.com", "https://testJutSu.com2"),
                listOf("Episode 1", "Episode 2")
            )

            val expected = SpecificSeries(
                listOf("https://testJutSu.com", "https://testJutSu.com2"),
                listOf("Episode 1", "Episode 2")
            )
            every {
                mockGetSpecificSeriesLinkUseCase.execute(
                    listOfLinks,
                    userAgent,
                    resolution
                )
            } returns specificSeries

            viewModel.getSpecificLinkSeries(listOfLinks, userAgent, resolution)

            viewModel.specificLinks.take(1).collect { result ->
                assertEquals(expected, result)
            }
        }

    @Test
    fun `isOnlyOneSeason emits result when isOnlyOneSeasonUseCase returns valid result`() =
        runBlocking {

            val url = "https://testJutSu.com"
            val userAgent = USER_AGENT

            val oneSeason = OneSeason(true, null)
            val expected = OneSeason(true, null)

            every { mockIsOnlyOneSeasonUseCase.execute(url, userAgent) } returns oneSeason

            viewModel.isOnlyOneSeason(url, userAgent)

            viewModel.isOnlyOneSeason.take(1).collect { result ->
                assertEquals(expected, result)
            }
        }

    @Test
    fun downloadTest() {
        val userAgent = USER_AGENT
        val linkOfConcreteSeries =
            mutableListOf("https://example.com/series/1", "https://example.com/series/2")
        val names = mutableListOf("Series 1", "Series 2")

        val expectedReturnValue = 100L
        every {
            mockDownloadUseCase.execute(
                userAgent,
                linkOfConcreteSeries,
                names
            )
        } returns expectedReturnValue
        val result = viewModel.download(userAgent, linkOfConcreteSeries, names)

        assertEquals(expectedReturnValue, result)
        verify { mockDownloadUseCase.execute(userAgent, linkOfConcreteSeries, names) }
    }

    // Try RoboElectric
//    @Test
//    fun progressObserveTest() = runBlocking {
//
//        // Mock objects
//        val context: Context = mockk()
//        val handler: Handler = mockk()
//        val contentResolver: ContentResolver = mockk()
//        val observer: DownloadProgressObserver = mockk()
////        var progress: Flow<Int> = flowOf(50)
//
//        val mockDownloadManager: DownloadManager = mockk()
//
//        // Uri mock
//        mockkStatic(Uri::class)
//        val mockUri: Uri = mockk()
//        every { Uri.parse(any()) } returns mockUri
//
//        // Context
//        every { context.getSystemService(DOWNLOAD_SERVICE) } returns mockDownloadManager
//        every { context.contentResolver } returns contentResolver
//        every { contentResolver.registerContentObserver(mockUri, false, observer) }
//
//
//
//
//        // Mock the registerContentObserver method
//        every {
//            contentResolver.registerContentObserver(
//                any(),
//                any(),
//                observer
//            )
//        } just Runs
//
//        // Mock the collect method of the progress Flow
////        coEvery { observer.progress.collect() }
//
//        val downloadId = 123L
//
//        // Act
//        viewModel.progressObserve(context, handler, downloadId).join()
//
//        // Assert
//        verify {
//            contentResolver.registerContentObserver(
////                mockUri.path,
////                Uri.parse("content://downloads/all_downloads/$downloadId"),
//                mockUri,
//                true,
//                observer
//            )
//        }
//
//        viewModel.progress.take(1).collect { result ->
//            assertEquals(50, result)
//        }
//    }

}


