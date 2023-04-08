package com.buffersolve.jutloader.domain.usecase

import com.buffersolve.jutloader.domain.model.Episodes
import com.buffersolve.jutloader.domain.repository.Repository

class GetOnlyOneEpisodeUseCase (
    private val repository: Repository
) {

    fun execute(url: String, userAgent: String): Episodes {
        return repository.getOnlyOneSeriesUseCase(url, userAgent)
    }

}