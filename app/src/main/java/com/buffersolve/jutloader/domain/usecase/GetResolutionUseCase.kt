package com.buffersolve.jutloader.domain.usecase

import com.buffersolve.jutloader.domain.model.Resolution
import com.buffersolve.jutloader.domain.repository.Repository

class GetResolutionUseCase(
    private val repository: Repository
) {

    fun execute(url: String, userAgent: String): Resolution {
        return repository.getResolutionUseCase(url, userAgent)
    }

}