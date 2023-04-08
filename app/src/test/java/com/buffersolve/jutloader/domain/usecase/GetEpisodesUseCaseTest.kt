package com.buffersolve.jutloader.domain.usecase

import com.buffersolve.jutloader.Constants.Companion.USER_AGENT
import com.buffersolve.jutloader.domain.model.Episodes
import com.buffersolve.jutloader.domain.repository.Repository
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class GetEpisodesUseCaseTest {

    @MockK
    private lateinit var mockRepository: Repository

    private lateinit var useCase: GetEpisodesUseCase

    @BeforeEach
    fun setUp() {
        MockKAnnotations.init(this)
        useCase = GetEpisodesUseCase(mockRepository)
    }

    @Test
    fun `execute should return expected Series`() {
        val expectedEpisodes = Episodes(listOf("Episodes 1"), mutableListOf("/series1/"))
        val url = "https://example.com"
        val userAgent = USER_AGENT
        every { mockRepository.getSeriesUseCase(url, userAgent) } returns expectedEpisodes

        val actualSeries = useCase.execute(url, userAgent)

        assertEquals(expectedEpisodes, actualSeries)
    }
}