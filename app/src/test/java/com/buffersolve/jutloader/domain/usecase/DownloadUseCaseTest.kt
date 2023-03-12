package com.buffersolve.jutloader.domain.usecase

import com.buffersolve.jutloader.Constants.Companion.USER_AGENT
import com.buffersolve.jutloader.domain.downloader.Downloader
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test

class DownloadUseCaseTest {

    @MockK
    private lateinit var mockDownloader: Downloader

    private lateinit var useCase: DownloadUseCase

    @BeforeEach
    fun setUp() {
        MockKAnnotations.init(this)
        useCase = DownloadUseCase(mockDownloader)
    }

    @Test
    fun `execute should return expected download size`() {
        val expectedSize = 100L
        val userAgent = USER_AGENT
        val linkOfConcreteSeria =
            listOf("https://example.com/episode1.mp4", "https://example.com/episode2.mp4")
        val names = mutableListOf("Episode 1", "Episode 2")
        every {
            mockDownloader.download(
                userAgent,
                linkOfConcreteSeria,
                names
            )
        } returns expectedSize

        val actualSize = useCase.execute(userAgent, linkOfConcreteSeria, names)

        assertEquals(expectedSize, actualSize)
    }

}