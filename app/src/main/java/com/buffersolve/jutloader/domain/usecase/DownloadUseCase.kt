package com.buffersolve.jutloader.domain.usecase

import com.buffersolve.jutloader.domain.downloader.Downloader

class DownloadUseCase(
    private val downloader: Downloader
) {

    fun execute(userAgent: String, linkOfConcreteSeria: List<String>, names: MutableList<String>) {
        return downloader.download(userAgent, linkOfConcreteSeria, names)
    }

}