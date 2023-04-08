package com.buffersolve.jutloader.domain.repository

import com.buffersolve.jutloader.domain.model.*

interface Repository {

    fun getSeasonUseCase(url: String, userAgent: String): Season

    fun getSeriesUseCase(url: String, userAgent: String): Episodes

    fun getOnlyOneSeasonUseCase(url: String, userAgent: String): Season

    fun getOnlyOneSeriesUseCase(url: String, userAgent: String): Episodes

    fun isOnlyOneSeasonUseCase(url: String, userAgent: String): OneSeason

    fun getResolutionUseCase(url: String, userAgent: String): Resolution

    fun getSpecificSeriesLinkUseCase(listOfLinks: List<String>, userAgent: String, resolution: String): SpecificEpisode

//    fun getDownloadProgressObserverProgress(context: Context, handler: Handler, downloadId: Long) : DownloadProgressObserver

}