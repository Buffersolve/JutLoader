package com.buffersolve.jutloader.domain.usecase

import com.buffersolve.jutloader.domain.model.Season
import com.buffersolve.jutloader.domain.repository.Repository

class GetSeasonUseCase(
    private val repository: Repository
) {

    fun execute(url: String, userAgent: String): Season {
        return repository.getSeasonUseCase(url, userAgent)
    }

}