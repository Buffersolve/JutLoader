package com.buffersolve.jutloader.domain.usecase

import com.buffersolve.jutloader.domain.downloader.Downloader
import com.buffersolve.jutloader.domain.model.Resolution
import com.buffersolve.jutloader.domain.model.Season
import com.buffersolve.jutloader.domain.model.Seria
import com.buffersolve.jutloader.domain.model.SpecificSeries
import com.buffersolve.jutloader.domain.repository.Repository

class DownloadUseCase(
    private val downloader: Downloader
) {

    fun execute(userAgent: String, linkOfConcreteSeria: List<String>, names: MutableList<String>) {
        return downloader.download(userAgent, linkOfConcreteSeria, names)
    }

}