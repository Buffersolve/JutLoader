package com.buffersolve.jutloader.data.repository

import com.buffersolve.jutloader.data.parser.getmethods.*
import com.buffersolve.jutloader.data.parser.getmethods.onlyone.GetOnlyOneSeason
import com.buffersolve.jutloader.data.parser.getmethods.onlyone.GetOnlyOneSeasonSeries
import com.buffersolve.jutloader.domain.model.Resolution
import com.buffersolve.jutloader.domain.model.Season
import com.buffersolve.jutloader.domain.model.Seria
import com.buffersolve.jutloader.domain.model.SpecificSeries
import com.buffersolve.jutloader.domain.repository.Repository

class RepositoryImpl : Repository {

    override fun getSeasonUseCase(url: String, userAgent: String): Season {
        return GetSeasons().execute(url, userAgent)
    }

    override fun getSeriesUseCase(url: String, userAgent: String): Seria {
        return GetSeries().execute(url, userAgent)
    }

    override fun getOnlyOneSeasonUseCase(url: String, userAgent: String): Season {
        return GetOnlyOneSeason().execute(url, userAgent)
    }

    override fun getOnlyOneSeriesUseCase(url: String, userAgent: String): Seria {
        return GetOnlyOneSeasonSeries().execute(url, userAgent)
    }

    override fun getResolutionUseCase(url: String, userAgent: String): Resolution {
        return GetResolution().execute(url, userAgent)
    }

    override fun getSpecificSeriesLinkUseCase(listOfLinks: List<String>, userAgent: String, resolution: String): SpecificSeries {
        return GetSpecificSeriesLink().execute(listOfLinks, userAgent, resolution)
    }
}