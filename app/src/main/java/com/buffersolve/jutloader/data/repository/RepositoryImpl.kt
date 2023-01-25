package com.buffersolve.jutloader.data.repository

import com.buffersolve.jutloader.data.parser.getmethods.GetResolution
import com.buffersolve.jutloader.data.parser.getmethods.GetSeasons
import com.buffersolve.jutloader.data.parser.getmethods.GetSeries
import com.buffersolve.jutloader.domain.model.Resolution
import com.buffersolve.jutloader.domain.model.Season
import com.buffersolve.jutloader.domain.model.Seria
import com.buffersolve.jutloader.domain.repository.Repository

class RepositoryImpl : Repository {

    override fun getSeasonUseCase(url: String, userAgent: String): Season {
        return GetSeasons().execute(url, userAgent)
    }

    override fun getSeriesUseCase(url: String, userAgent: String): Seria {
        return GetSeries().execute(url, userAgent)
    }

    override fun getResolutionUseCase(url: String, userAgent: String): Resolution {
        return GetResolution().execute(url, userAgent)
    }

}