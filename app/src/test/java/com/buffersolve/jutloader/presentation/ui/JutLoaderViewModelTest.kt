package com.buffersolve.jutloader.presentation.ui

import android.content.ContentResolver
import android.content.Context
import org.junit.jupiter.api.Test
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.Uri
import android.os.Handler
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.buffersolve.jutloader.Constants.Companion.USER_AGENT
import com.buffersolve.jutloader.data.provider.DownloadProgressObserver
import com.buffersolve.jutloader.domain.model.*
import com.buffersolve.jutloader.domain.usecase.*
import io.mockk.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Rule
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach

@OptIn(ExperimentalCoroutinesApi::class)
class JutLoaderViewModelTest {

    @get:Rule
    val rule = InstantTaskExecutorRule()

    private val mockConnectivityManager: ConnectivityManager = mockk()
    private val mockGetSeasonUseCase: GetSeasonUseCase = mockk()
    private val mockGetEpisodesUseCase: GetEpisodesUseCase = mockk()
    private val mockGetOnlyOneSeasonsUseCase: GetOnlyOneSeasonUseCase = mockk()
    private val mockGetOnlyOneEpisodeUseCase: GetOnlyOneEpisodeUseCase = mockk()
    private val mockGetResolutionUseCase: GetResolutionUseCase = mockk()
    private val mockGetSpecificEpisodesLinkUseCase: GetSpecificEpisodesLinkUseCase = mockk()
    private val mockIsOnlyOneSeasonUseCase: IsOnlyOneSeasonUseCase = mockk()
    private val mockDownloadUseCase = mockk<DownloadUseCase>()
    private val mockNetwork: Network = mockk()
    private val mockNetworkCapabilities: NetworkCapabilities = mockk()

    @OptIn(DelicateCoroutinesApi::class)
    private val mainThreadSurrogate = newSingleThreadContext("UI thread")

    private lateinit var viewModel: JutLoaderViewModel

    @BeforeEach
    fun setUp() {

        Dispatchers.setMain(mainThreadSurrogate)

        MockKAnnotations.init(this)
        viewModel = JutLoaderViewModel(
            connectivityManager = mockConnectivityManager,
            getSeasonUseCase = mockGetSeasonUseCase,
            getEpisodesUseCase = mockGetEpisodesUseCase,
            getOnlyOneSeasonsUseCase = mockGetOnlyOneSeasonsUseCase,
            getOnlyOneEpisodeUseCase = mockGetOnlyOneEpisodeUseCase,
            getResolutionUseCase = mockGetResolutionUseCase,
            getSpecificEpisodesLinkUseCase = mockGetSpecificEpisodesLinkUseCase,
            isOnlyOneSeasonUseCase = mockIsOnlyOneSeasonUseCase,
            downloadUseCase = mockDownloadUseCase
        )

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
    fun `getSeasons emits no internet`() = runTest {

        every { mockConnectivityManager.activeNetwork } returns mockNetwork
        every { mockConnectivityManager.getNetworkCapabilities(mockNetwork) } returns mockNetworkCapabilities
        every { mockNetworkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) } returns false
        val url = "https://example.com"
        val userAgent = USER_AGENT

        viewModel.getSeasons(url, userAgent)
        val expected = Season(listOf("No internet connection"), mutableListOf())

        viewModel.season.take(1).collect { result ->
            assertEquals(expected, result)
        }
    }

    @Test
    fun `getSeries emits result when getSeriesUseCase returns valid result`() = runTest {

        val url = "https://example.com"
        val userAgent = USER_AGENT
        val episodes = Episodes(listOf("Episode 1"), mutableListOf("/Episode 1/", "/Episode 2/"))
        val expected = Episodes(listOf("Episode 1"), mutableListOf("/Episode 1/", "/Episode 2/"))
        every { mockGetEpisodesUseCase.execute(url, userAgent) } returns episodes

        viewModel.getSeries(url, userAgent)

        viewModel.episodes.take(1).collect { result ->
            assertEquals(expected, result)
        }
    }

    @Test
    fun `getSeries emits no internet`() = runTest {

        every { mockConnectivityManager.activeNetwork } returns mockNetwork
        every { mockConnectivityManager.getNetworkCapabilities(mockNetwork) } returns mockNetworkCapabilities
        every { mockNetworkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) } returns false
        val url = "https://example.com"
        val userAgent = USER_AGENT

        viewModel.getSeries(url, userAgent)
        val expected = Episodes(listOf("No internet connection"), mutableListOf())

        viewModel.episodes.take(1).collect { result ->
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
    fun `getOnlyOneSeasons emits no internet`() = runTest {

        every { mockConnectivityManager.activeNetwork } returns mockNetwork
        every { mockConnectivityManager.getNetworkCapabilities(mockNetwork) } returns mockNetworkCapabilities
        every { mockNetworkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) } returns false
        val url = "https://example.com"
        val userAgent = USER_AGENT

        viewModel.getOnlyOneSeasons(url, userAgent)
        val expected = Season(listOf("No internet connection"), mutableListOf())

        viewModel.season.take(1).collect { result ->
            assertEquals(expected, result)
        }
    }

    @Test
    fun `getOnlyOneSeries emits result when getOnlyOneSeriesUseCase returns valid result`() =
        runTest {

            val url = "https://example.com"
            val userAgent = USER_AGENT
            val episodes = Episodes(listOf("Episode 1"), mutableListOf("/Episode 1/", "/Episode 2/"))
            val expected = Episodes(listOf("Episode 1"), mutableListOf("/Episode 1/", "/Episode 2/"))
            every { mockGetOnlyOneEpisodeUseCase.execute(url, userAgent) } returns episodes

            viewModel.getOnlyOneSeries(url, userAgent)

            viewModel.episodes.take(1).collect { result ->
                assertEquals(expected, result)
            }
        }

    @Test
    fun `getOnlyOneSeries emits no internet`() = runTest {

        every { mockConnectivityManager.activeNetwork } returns mockNetwork
        every { mockConnectivityManager.getNetworkCapabilities(mockNetwork) } returns mockNetworkCapabilities
        every { mockNetworkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) } returns false
        val url = "https://example.com"
        val userAgent = USER_AGENT

        viewModel.getOnlyOneSeries(url, userAgent)
        val expected = Episodes(listOf("No internet connection"), mutableListOf())

        viewModel.episodes.take(1).collect { result ->
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
    fun `getResolution emits no internet`() = runTest {

        every { mockConnectivityManager.activeNetwork } returns mockNetwork
        every { mockConnectivityManager.getNetworkCapabilities(mockNetwork) } returns mockNetworkCapabilities
        every { mockNetworkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) } returns false
        val url = "https://example.com"
        val userAgent = USER_AGENT

        viewModel.getResolution(url, userAgent)
        val expected = Resolution(listOf("No internet connection"))

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
            val specificEpisode = SpecificEpisode(
                listOf("https://testJutSu.com", "https://testJutSu.com2"),
                listOf("Episode 1", "Episode 2")
            )

            val expected = SpecificEpisode(
                listOf("https://testJutSu.com", "https://testJutSu.com2"),
                listOf("Episode 1", "Episode 2")
            )
            every {
                mockGetSpecificEpisodesLinkUseCase.execute(
                    listOfLinks,
                    userAgent,
                    resolution
                )
            } returns specificEpisode

            viewModel.getSpecificLinkSeries(listOfLinks, userAgent, resolution)

            viewModel.specificLinks.take(1).collect { result ->
                assertEquals(expected, result)
            }
        }

    @Test
    fun `getSpecificLinkSeries emits no internet`() = runTest {

        every { mockConnectivityManager.activeNetwork } returns mockNetwork
        every { mockConnectivityManager.getNetworkCapabilities(mockNetwork) } returns mockNetworkCapabilities
        every { mockNetworkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) } returns false
        val listOfLinks = listOf("https://testJutSu.com", "https://testJutSu.com2")
        val userAgent = USER_AGENT
        val resolution = "1080p"

        viewModel.getSpecificLinkSeries(listOfLinks, userAgent, resolution)
        val expected = SpecificEpisode(listOf("No internet connection"), mutableListOf())

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
    fun `isOnlyOneSeason emits no internet`() = runTest {

        every { mockConnectivityManager.activeNetwork } returns mockNetwork
        every { mockConnectivityManager.getNetworkCapabilities(mockNetwork) } returns mockNetworkCapabilities
        every { mockNetworkCapabilities.hasCapability(NetworkCapabilities.NET_CAPABILITY_INTERNET) } returns false
        val url = "https://testJutSu.com"
        val userAgent = USER_AGENT

        viewModel.isOnlyOneSeason(url, userAgent)
        val expected = Season(listOf("No internet connection"), mutableListOf())

        viewModel.season.take(1).collect { result ->
            assertEquals(expected, result)
        }
    }

    @Test
    fun downloadTest() {
        val userAgent = USER_AGENT
        val linkOfConcreteSeries =
            mutableListOf("https://example.com/series/1", "https://example.com/series/2")
        val names = mutableListOf("Episodes 1", "Episodes 2")

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
        val handler: Handler = mockk(relaxed = true)
        val downloadId = 1L
        val downloadObserver = DownloadProgressObserver(context, handler, downloadId)
        val contentResolver = mockk<ContentResolver>(relaxed = true)
        val mockUri: Uri = mockk()
        val progressFlow = MutableStateFlow(0L)
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




