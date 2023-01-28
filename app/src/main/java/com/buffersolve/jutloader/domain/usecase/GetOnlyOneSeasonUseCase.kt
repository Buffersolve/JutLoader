package com.buffersolve.jutloader.domain.usecase

import com.buffersolve.jutloader.domain.model.Season
import com.buffersolve.jutloader.domain.repository.Repository

class GetOnlyOneSeasonUseCase(
    private val repository: Repository
) {

    fun execute(url: String, userAgent: String): Season {
        return repository.getOnlyOneSeasonUseCase(url, userAgent)
    }

}