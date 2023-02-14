package com.buffersolve.jutloader.domain.usecase

import com.buffersolve.jutloader.Constants.Companion.USER_AGENT
import com.buffersolve.jutloader.domain.model.Series
import com.buffersolve.jutloader.domain.repository.Repository
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class GetSeriesUseCaseTest {

    @MockK
    private lateinit var mockRepository: Repository

    private lateinit var useCase: GetSeriesUseCase

    @BeforeEach
    fun setUp() {
        MockKAnnotations.init(this)
        useCase = GetSeriesUseCase(mockRepository)
    }

    @Test
    fun `execute should return expected Series`() {
        val expectedSeries = Series(listOf("Series 1"), mutableListOf("/series1/"))
        val url = "https://example.com"
        val userAgent = USER_AGENT
        every { mockRepository.getSeriesUseCase(url, userAgent) } returns expectedSeries

        val actualSeries = useCase.execute(url, userAgent)

        assertEquals(expectedSeries, actualSeries)
    }
}