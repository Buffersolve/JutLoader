package com.buffersolve.jutloader.data.repository

import com.buffersolve.jutloader.Constants.Companion.USER_AGENT
import com.buffersolve.jutloader.data.parser.getmethods.GetResolution
import com.buffersolve.jutloader.data.parser.getmethods.GetSeasons
import com.buffersolve.jutloader.data.parser.getmethods.GetEpisodes
import com.buffersolve.jutloader.data.parser.getmethods.GetSpecificEpisodesLink
import com.buffersolve.jutloader.data.parser.getmethods.onlyone.GetOnlyOneSeason
import com.buffersolve.jutloader.data.parser.getmethods.onlyone.GetOnlyOneSeasonEpisodes
import com.buffersolve.jutloader.data.parser.getmethods.onlyone.IsOnlyOneSeasons
import com.buffersolve.jutloader.domain.model.*
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test

class RepositoryImplTest {

    private val mockGetSeasons = mockk<GetSeasons>(relaxed = true)
    private val mockGetEpisodes = mockk<GetEpisodes>()
    private val mockGetOnlyOneSeason = mockk<GetOnlyOneSeason>()
    private val mockGetOnlyOneSeasonEpisodes = mockk<GetOnlyOneSeasonEpisodes>()
    private val mockIsOnlyOneSeasons = mockk<IsOnlyOneSeasons>()
    private val mockGetResolution = mockk<GetResolution>()
    private val mockGetSpecificEpisodesLink = mockk<GetSpecificEpisodesLink>()

    private val repository = RepositoryImpl()

    @Test
    fun `getSeasonUseCase should return expected Season object`() {
        val url = "https://example.com"
        val userAgent = USER_AGENT
        val expectedSeason = Season(listOf(), mutableListOf())

        every { mockGetSeasons.execute(url, userAgent) } returns expectedSeason

        val result = repository.getSeasonUseCase(url, userAgent)

        assertEquals(expectedSeason, result)
    }

    @Test
    fun `getSeriesUseCase should return expected Series object`() {
        val url = "https://example.com"
        val userAgent = USER_AGENT
        val expectedEpisodes = Episodes(listOf(), mutableListOf())

        every { mockGetEpisodes.execute(url, userAgent) } returns expectedEpisodes

        val result = repository.getSeriesUseCase(url, userAgent)

        assertEquals(expectedEpisodes, result)
    }

    @Test
    fun `getOnlyOneSeasonUseCase should return expected Series object`() {
        val url = "https://example.com"
        val userAgent = USER_AGENT
        val expectedSeason = Season(listOf(), mutableListOf())

        every { mockGetOnlyOneSeason.execute(url, userAgent) } returns expectedSeason

        val result = repository.getOnlyOneSeasonUseCase(url, userAgent)

        assertEquals(expectedSeason, result)
    }

    @Test
    fun `getOnlyOneSeriesUseCase should return expected Series object`() {
        val url = "https://example.com"
        val userAgent = USER_AGENT
        val expectedEpisodes = Episodes(listOf(), mutableListOf())

        every { mockGetOnlyOneSeasonEpisodes.execute(url, userAgent) } returns expectedEpisodes

        val result = repository.getOnlyOneSeriesUseCase(url, userAgent)

        assertEquals(expectedEpisodes, result)
    }

    @Test
    fun `isOnlyOneSeasonUseCase should return expected Series object`() {
        val url = "https://example.com"
        val userAgent = USER_AGENT
        val expectedOneSeason = OneSeason(isOnlyOneSeason = null, exception = true)

        every { mockIsOnlyOneSeasons.execute(url, userAgent) } returns expectedOneSeason

        val result = repository.isOnlyOneSeasonUseCase(url, userAgent)

        assertEquals(expectedOneSeason, result)
    }

    @Test
    fun `getResolutionUseCase should return expected Series object`() {
        val url = "https://example.com"
        val userAgent = USER_AGENT
        val expectedResolution = Resolution(listOf())

        every { mockGetResolution.execute(url, userAgent) } returns expectedResolution

        val result = repository.getResolutionUseCase(url, userAgent)

        assertEquals(expectedResolution, result)
    }

    @Test
    fun `getSpecificSeriesLinkUseCase should return expected Series object`() {
        val listOfLinks = listOf("/example/")
        val userAgent = USER_AGENT
        val resolution = "1080p"
        val expectedSpecificLink = SpecificEpisode(listOf(), listOf())

        every { mockGetSpecificEpisodesLink.execute(listOfLinks, userAgent, resolution) } returns expectedSpecificLink

        val result = repository.getSpecificSeriesLinkUseCase(listOfLinks, userAgent, resolution)

        assertEquals(expectedSpecificLink, result)
    }


}