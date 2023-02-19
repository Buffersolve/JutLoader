package com.buffersolve.jutloader.presentation.ui

import android.app.DownloadManager
import android.content.ContentResolver
import android.content.ContentUris
import android.content.ContentValues
import android.content.Context
import android.database.ContentObserver
import org.junit.jupiter.api.Test
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Handler
import android.os.Looper
import androidx.activity.ComponentActivity
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import com.buffersolve.jutloader.Constants.Companion.USER_AGENT
import com.buffersolve.jutloader.data.provider.DownloadProgressObserver
import com.buffersolve.jutloader.domain.model.*
import com.buffersolve.jutloader.domain.usecase.*
import io.mockk.*
import junit.framework.TestCase
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.test.TestCoroutineScope
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.shadows.ShadowSystemServiceRegistry

@OptIn(ExperimentalCoroutinesApi::class)

class JutLoaderViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

//    @get:Rule
//    val mainDispatcherRule = MainDispatcherRule()

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

    @AfterEach
    fun tearDown() = unmockkAll()

    @Test
    fun `getSeasons emits result when getSeasonUseCase returns valid result`() = runTest {

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
    fun `getSeries emits result when getSeriesUseCase returns valid result`() = runTest {

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
        runTest {

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
        runTest {

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
        runTest {

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
        runTest {

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
        runTest {

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

    @Test
    fun `test progress observe`() = runTest {

        val context: Context = mockk()
//        val dm: DownloadManager = mockk(relaxed = true)
        val handler: Handler = mockk(relaxed = true)
        val downloadId = 1L
//        val downloadObserver = mockk<DownloadProgressObserver>()
        val downloadObserver = DownloadProgressObserver(context, handler, downloadId)
        val contentResolver = mockk<ContentResolver>(relaxed = true)
        val mockUri: Uri = mockk()
        mockkStatic(Uri::class)
        every { Uri.parse("content://downloads/all_downloads/$downloadId") } returns mockUri
        every {
            context.contentResolver
        } returns contentResolver

        every {
            contentResolver.registerContentObserver(
                mockUri,
                true,
                downloadObserver
            )
        } returns Unit
        val progressFlow = MutableStateFlow(0L)
        viewModel.progressObserve(context, handler, downloadId).join()
        progressFlow.emit(10)

        verify {
            contentResolver.registerContentObserver(
                mockUri,
                true,
                downloadObserver
            )
            downloadObserver.progress
        }
    }

}




