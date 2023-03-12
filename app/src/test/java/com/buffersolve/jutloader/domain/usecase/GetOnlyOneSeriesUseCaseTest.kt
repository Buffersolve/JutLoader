package com.buffersolve.jutloader.domain.usecase

import com.buffersolve.jutloader.Constants.Companion.USER_AGENT
import com.buffersolve.jutloader.domain.model.Resolution
import com.buffersolve.jutloader.domain.model.Season
import com.buffersolve.jutloader.domain.model.Series
import com.buffersolve.jutloader.domain.repository.Repository
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class GetOnlyOneSeriesUseCaseTest {

    @MockK
    private lateinit var mockRepository: Repository

    private lateinit var useCase: GetOnlyOneSeriesUseCase

    @BeforeEach
    fun setUp() {
        MockKAnnotations.init(this)
        useCase = GetOnlyOneSeriesUseCase(mockRepository)
    }

    @Test
    fun `execute should return expected Series`() {
        // given
        val expectedSeries = Series(listOf("Series 1"), mutableListOf("/Series 1/"))
        val url = "https://example.com"
        val userAgent = USER_AGENT
        every { mockRepository.getOnlyOneSeriesUseCase(url, userAgent) } returns expectedSeries

        // when
        val actualSeries = useCase.execute(url, userAgent)

        // then
        assertEquals(expectedSeries, actualSeries)
    }

}