package com.buffersolve.jutloader.domain.usecase

import com.buffersolve.jutloader.domain.model.Seria
import com.buffersolve.jutloader.domain.repository.Repository

class GetOnlyOneSeriesUseCase (
    private val repository: Repository
) {

    fun execute(url: String, userAgent: String): Seria {
        return repository.getOnlyOneSeriesUseCase(url, userAgent)
    }

}