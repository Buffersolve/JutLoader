package com.buffersolve.jutloader.domain.repository

import android.content.Context
import android.os.Handler
import com.buffersolve.jutloader.data.provider.DownloadProgressObserver
import com.buffersolve.jutloader.domain.model.*
import kotlinx.coroutines.flow.StateFlow

interface Repository {

    fun getSeasonUseCase(url: String, userAgent: String): Season

    fun getSeriesUseCase(url: String, userAgent: String): Series

    fun getOnlyOneSeasonUseCase(url: String, userAgent: String): Season

    fun getOnlyOneSeriesUseCase(url: String, userAgent: String): Series

    fun isOnlyOneSeasonUseCase(url: String, userAgent: String): OneSeason

    fun getResolutionUseCase(url: String, userAgent: String): Resolution

    fun getSpecificSeriesLinkUseCase(listOfLinks: List<String>, userAgent: String, resolution: String): SpecificSeries

//    fun getDownloadProgressObserverProgress(context: Context, handler: Handler, downloadId: Long) : DownloadProgressObserver

}