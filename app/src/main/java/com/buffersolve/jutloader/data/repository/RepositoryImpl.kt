package com.buffersolve.jutloader.data.repository

import com.buffersolve.jutloader.data.parser.getmethods.*
import com.buffersolve.jutloader.data.parser.getmethods.onlyone.GetOnlyOneSeason
import com.buffersolve.jutloader.data.parser.getmethods.onlyone.GetOnlyOneSeasonSeries
import com.buffersolve.jutloader.data.parser.getmethods.onlyone.IsOnlyOneSeasons
import com.buffersolve.jutloader.domain.model.*
import com.buffersolve.jutloader.domain.repository.Repository

class RepositoryImpl : Repository {

    override fun getSeasonUseCase(url: String, userAgent: String): Season {
        return GetSeasons().execute(url, userAgent)
    }

    override fun getSeriesUseCase(url: String, userAgent: String): Series {
        return GetSeries().execute(url, userAgent)
    }

    override fun getOnlyOneSeasonUseCase(url: String, userAgent: String): Season {
        return GetOnlyOneSeason().execute(url, userAgent)
    }

    override fun getOnlyOneSeriesUseCase(url: String, userAgent: String): Series {
        return GetOnlyOneSeasonSeries().execute(url, userAgent)
    }
    override fun isOnlyOneSeasonUseCase(url: String, userAgent: String): OneSeason {
        return IsOnlyOneSeasons().execute(url, userAgent)
    }

    override fun getResolutionUseCase(url: String, userAgent: String): Resolution {
        return GetResolution().execute(url, userAgent)
    }

    override fun getSpecificSeriesLinkUseCase(listOfLinks: List<String>, userAgent: String, resolution: String): SpecificSeries {
        return GetSpecificSeriesLink().execute(listOfLinks, userAgent, resolution)
    }
}