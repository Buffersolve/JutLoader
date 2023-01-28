package com.buffersolve.jutloader.domain.usecase

import com.buffersolve.jutloader.domain.model.SpecificSeries
import com.buffersolve.jutloader.domain.repository.Repository

class GetSpecificSeriesLinkUseCase(
    private val repository: Repository
) {

    fun execute(listOfLinks: List<String>, userAgent: String, resolution: String): SpecificSeries {
        return repository.getSpecificSeriesLinkUseCase(listOfLinks, userAgent, resolution)
    }

}