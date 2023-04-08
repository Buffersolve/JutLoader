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

class GetOnlyOneEpisodesUseCaseTest {

    @MockK
    private lateinit var mockRepository: Repository

    private lateinit var useCase: GetOnlyOneEpisodeUseCase

    @BeforeEach
    fun setUp() {
        MockKAnnotations.init(this)
        useCase = GetOnlyOneEpisodeUseCase(mockRepository)
    }

    @Test
    fun `execute should return expected Series`() {
        // given
        val expectedEpisodes = Episodes(listOf("Episodes 1"), mutableListOf("/Episodes 1/"))
        val url = "https://example.com"
        val userAgent = USER_AGENT
        every { mockRepository.getOnlyOneSeriesUseCase(url, userAgent) } returns expectedEpisodes

        // when
        val actualSeries = useCase.execute(url, userAgent)

        // then
        assertEquals(expectedEpisodes, actualSeries)
    }

}