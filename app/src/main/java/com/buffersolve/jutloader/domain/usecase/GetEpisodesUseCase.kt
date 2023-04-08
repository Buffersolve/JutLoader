package com.buffersolve.jutloader.domain.usecase

import com.buffersolve.jutloader.domain.model.Episodes
import com.buffersolve.jutloader.domain.repository.Repository

class GetEpisodesUseCase(
    private val repository: Repository
) {

    fun execute(url: String, userAgent: String): Episodes {
        return repository.getSeriesUseCase(url, userAgent)
    }

}