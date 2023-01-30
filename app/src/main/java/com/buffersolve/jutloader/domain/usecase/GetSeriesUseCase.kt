package com.buffersolve.jutloader.domain.usecase

import com.buffersolve.jutloader.domain.model.Series
import com.buffersolve.jutloader.domain.repository.Repository

class GetSeriesUseCase(
    private val repository: Repository
) {

    fun execute(url: String, userAgent: String): Series {
        return repository.getSeriesUseCase(url, userAgent)
    }

}