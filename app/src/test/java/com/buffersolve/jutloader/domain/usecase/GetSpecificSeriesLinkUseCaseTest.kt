package com.buffersolve.jutloader.domain.usecase

import com.buffersolve.jutloader.Constants.Companion.USER_AGENT
import com.buffersolve.jutloader.domain.model.SpecificSeries
import com.buffersolve.jutloader.domain.repository.Repository
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class GetSpecificSeriesLinkUseCaseTest {

    @MockK
    private lateinit var mockRepository: Repository

    private lateinit var useCase: GetSpecificSeriesLinkUseCase

    @BeforeEach
    fun setUp() {
        MockKAnnotations.init(this)
        useCase = GetSpecificSeriesLinkUseCase(mockRepository)
    }

    @Test
    fun `execute should return expected SpecificSeries`() {
        val expectedSpecificSeries = SpecificSeries(listOf("/series1, series2"), listOf("Series 1", "Series 2"))
        val listOfLinks = listOf("https://example.com/1", "https://example.com/2")
        val userAgent = USER_AGENT
        val resolution = "720p"
        every { mockRepository.getSpecificSeriesLinkUseCase(listOfLinks, userAgent, resolution) } returns expectedSpecificSeries

        val actualSpecificSeries = useCase.execute(listOfLinks, userAgent, resolution)

        assertEquals(expectedSpecificSeries, actualSpecificSeries)
    }
}