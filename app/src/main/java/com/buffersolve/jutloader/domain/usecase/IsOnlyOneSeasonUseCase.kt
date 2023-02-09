package com.buffersolve.jutloader.domain.usecase

import com.buffersolve.jutloader.domain.repository.Repository

class IsOnlyOneSeasonUseCase(
    private val repository: Repository
) {

    fun execute(url: String, userAgent: String): Boolean {
        return repository.isOnlyOneSeasonUseCase(url, userAgent)
    }

}