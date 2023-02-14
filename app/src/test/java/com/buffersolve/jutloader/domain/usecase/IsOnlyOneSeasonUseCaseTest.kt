package com.buffersolve.jutloader.domain.usecase

import com.buffersolve.jutloader.Constants.Companion.USER_AGENT
import com.buffersolve.jutloader.domain.model.OneSeason
import com.buffersolve.jutloader.domain.repository.Repository
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class IsOnlyOneSeasonUseCaseTest {

    @MockK
    private lateinit var mockRepository: Repository
    private lateinit var useCase: IsOnlyOneSeasonUseCase

    @BeforeEach
    fun setUp() {
        MockKAnnotations.init(this)
        useCase = IsOnlyOneSeasonUseCase(mockRepository)
    }

    @Test
    fun `execute should return expected OneSeason`() {

        val expectedOneSeason = OneSeason(isOnlyOneSeason = false, exception = false)
//        val url = "id-invaded"
        val url = "berserk"
        val userAgent = USER_AGENT
        every { mockRepository.isOnlyOneSeasonUseCase(url, userAgent) } returns expectedOneSeason

        val actualOneSeason = useCase.execute(url, userAgent)

        assertEquals(expectedOneSeason, actualOneSeason)
    }



}