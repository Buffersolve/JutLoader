package com.buffersolve.jutloader.domain.usecase

import com.buffersolve.jutloader.Constants.Companion.USER_AGENT
import com.buffersolve.jutloader.domain.model.Season
import com.buffersolve.jutloader.domain.repository.Repository
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class GetSeasonUseCaseTest {

    @MockK
    private lateinit var mockRepository: Repository

    private lateinit var useCase: GetSeasonUseCase

    @BeforeEach
    fun setUp() {
        MockKAnnotations.init(this)
        useCase = GetSeasonUseCase(mockRepository)
    }

    @Test
    fun `execute should return expected Season`() {
        val expectedSeason = Season(listOf("Season 1", "Season 2"), mutableListOf("/Season 1/", "/Season 2/"))
        val url = "https://example.com"
        val userAgent = USER_AGENT
        every { mockRepository.getSeasonUseCase(url, userAgent) } returns expectedSeason

        val actualSeason = useCase.execute(url, userAgent)

        assertEquals(expectedSeason, actualSeason)
    }

}