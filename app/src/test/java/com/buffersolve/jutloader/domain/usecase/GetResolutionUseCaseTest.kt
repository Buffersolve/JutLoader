package com.buffersolve.jutloader.domain.usecase

import com.buffersolve.jutloader.Constants.Companion.USER_AGENT
import com.buffersolve.jutloader.domain.model.Resolution
import com.buffersolve.jutloader.domain.model.Season
import com.buffersolve.jutloader.domain.repository.Repository
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class GetResolutionUseCaseTest {

    @MockK
    private lateinit var mockRepository: Repository

    private lateinit var useCase: GetResolutionUseCase

    @BeforeEach
    fun setUp() {
        MockKAnnotations.init(this)
        useCase = GetResolutionUseCase(mockRepository)
    }

    @Test
    fun `execute should return expected Resolution`() {
        val expectedResolution = Resolution(listOf("1080p", "720p"))
        val url = "https://example.com"
        val userAgent = USER_AGENT
        every { mockRepository.getResolutionUseCase(url, userAgent) } returns expectedResolution

        val actualResolution = useCase.execute(url, userAgent)

        assertEquals(expectedResolution, actualResolution)
    }

}