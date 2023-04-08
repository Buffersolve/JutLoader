package com.buffersolve.jutloader.domain.usecase

import com.buffersolve.jutloader.Constants.Companion.USER_AGENT
import com.buffersolve.jutloader.domain.model.SpecificEpisode
import com.buffersolve.jutloader.domain.repository.Repository
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class GetSpecificEpisodesLinkUseCaseTest {

    @MockK
    private lateinit var mockRepository: Repository

    private lateinit var useCase: GetSpecificEpisodesLinkUseCase

    @BeforeEach
    fun setUp() {
        MockKAnnotations.init(this)
        useCase = GetSpecificEpisodesLinkUseCase(mockRepository)
    }

    @Test
    fun `execute should return expected SpecificSeries`() {
        val expectedSpecificEpisodes = SpecificEpisode(listOf("/series1, series2"), listOf("Episodes 1", "Episodes 2"))
        val listOfLinks = listOf("https://example.com/1", "https://example.com/2")
        val userAgent = USER_AGENT
        val resolution = "720p"
        every { mockRepository.getSpecificSeriesLinkUseCase(listOfLinks, userAgent, resolution) } returns expectedSpecificEpisodes

        val actualSpecificSeries = useCase.execute(listOfLinks, userAgent, resolution)

        assertEquals(expectedSpecificEpisodes, actualSpecificSeries)
    }
}