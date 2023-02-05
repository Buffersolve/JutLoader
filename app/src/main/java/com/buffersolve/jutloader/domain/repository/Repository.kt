package com.buffersolve.jutloader.domain.repository

import com.buffersolve.jutloader.domain.model.Resolution
import com.buffersolve.jutloader.domain.model.Season
import com.buffersolve.jutloader.domain.model.Series
import com.buffersolve.jutloader.domain.model.SpecificSeries

interface Repository {

    fun getSeasonUseCase(url: String, userAgent: String): Season

    fun getSeriesUseCase(url: String, userAgent: String): Series

    fun getOnlyOneSeasonUseCase(url: String, userAgent: String): Season

    fun getOnlyOneSeriesUseCase(url: String, userAgent: String): Series

    fun isOnlyOneSeasonUseCase(url: String, userAgent: String): Boolean

    fun getResolutionUseCase(url: String, userAgent: String): Resolution

    fun getSpecificSeriesLinkUseCase(listOfLinks: List<String>, userAgent: String, resolution: String): SpecificSeries

}