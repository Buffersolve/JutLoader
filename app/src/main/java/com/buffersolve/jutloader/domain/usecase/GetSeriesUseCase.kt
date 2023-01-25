package com.buffersolve.jutloader.domain.usecase

import com.buffersolve.jutloader.domain.model.Season
import com.buffersolve.jutloader.domain.model.Seria
import com.buffersolve.jutloader.domain.repository.Repository

class GetSeriesUseCase(
    private val repository: Repository
) {

    fun execute(url: String, userAgent: String): Seria {
        return repository.getSeriesUseCase(url, userAgent)
    }

}