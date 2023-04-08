package com.buffersolve.jutloader.domain.usecase

import com.buffersolve.jutloader.domain.model.SpecificEpisode
import com.buffersolve.jutloader.domain.repository.Repository

class GetSpecificEpisodesLinkUseCase(
    private val repository: Repository
) {

    fun execute(listOfLinks: List<String>, userAgent: String, resolution: String): SpecificEpisode {
        return repository.getSpecificSeriesLinkUseCase(listOfLinks, userAgent, resolution)
    }

}