package com.buffersolve.jutloader.presentation.ui

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities.*
import android.net.Uri
import android.os.Handler
import androidx.lifecycle.*
import com.buffersolve.jutloader.data.contentprovider.DownloadProgressObserver
import com.buffersolve.jutloader.data.downloader.DownloaderImpl
import com.buffersolve.jutloader.data.parser.getmethods.onlyone.IsOnlyOneSeasons
import com.buffersolve.jutloader.data.repository.RepositoryImpl
import com.buffersolve.jutloader.domain.model.Resolution
import com.buffersolve.jutloader.domain.model.Season
import com.buffersolve.jutloader.domain.model.Series
import com.buffersolve.jutloader.domain.model.SpecificSeries
import com.buffersolve.jutloader.domain.usecase.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class JutLoaderViewModel(
    app: Application,
    private val connectivityManager: ConnectivityManager,
    private val getSeasonUseCase: GetSeasonUseCase,
    private val getSeriesUseCase: GetSeriesUseCase,
    private val getOnlyOneSeasonsUseCase: GetOnlyOneSeasonUseCase,
    private val getOnlyOneSeriesUseCase: GetOnlyOneSeriesUseCase,
    private val getResolutionUseCase: GetResolutionUseCase,
    private val getSpecificSeriesLinkUseCase: GetSpecificSeriesLinkUseCase,
    private val isOnlyOneSeasonUseCase: IsOnlyOneSeasonUseCase,
) : AndroidViewModel(app) {

    private val _season: MutableLiveData<Season> = MutableLiveData()
    val season: LiveData<Season>
        get() = _season

    private val _series: MutableLiveData<Series> = MutableLiveData()
    val series: LiveData<Series>
        get() = _series

    private val _specificLinks: MutableLiveData<SpecificSeries> = MutableLiveData()
    val specificLinks: LiveData<SpecificSeries>
        get() = _specificLinks

    private val _resolution: MutableLiveData<Resolution> = MutableLiveData()
    val resolution: LiveData<Resolution>
        get() = _resolution

    private val _isOnlyOneSeason: MutableLiveData<Boolean> = MutableLiveData()
    val isOnlyOneSeason: LiveData<Boolean>
        get() = _isOnlyOneSeason

    private val _progress: MutableLiveData<Long> = MutableLiveData()
    val progress: LiveData<Long>
        get() = _progress

    // Seasons List
    fun getSeasons(
        url: String,
        userAgent: String
    ) = viewModelScope.launch(Dispatchers.IO) {
        if (checkInternetConnection()) {
            val seasons = getSeasonUseCase.execute(url, userAgent)
            _season.postValue(seasons)
        } else {
            _season.postValue(Season(listOf("No internet connection"), mutableListOf()))
        }
    }

    // Series List
    fun getSeries(
        url: String,
        userAgent: String
    ) = viewModelScope.launch(Dispatchers.IO) {
        if (checkInternetConnection()) {
            val series = getSeriesUseCase.execute(url, userAgent)
            _series.postValue(series)
        } else {
            _season.postValue(Season(listOf("No internet connection"), mutableListOf()))
        }
    }

    fun getOnlyOneSeasons(
        url: String,
        userAgent: String
    ) = viewModelScope.launch(Dispatchers.IO) {
        if (checkInternetConnection()) {
            val seasons = getOnlyOneSeasonsUseCase.execute(url, userAgent)
            _season.postValue(seasons)
        } else {
            _season.postValue(Season(listOf("No internet connection"), mutableListOf()))
        }
    }

    // Series List
    fun getOnlyOneSeries(
        url: String,
        userAgent: String
    ) = viewModelScope.launch(Dispatchers.IO) {
        if (checkInternetConnection()) {
            val series = getOnlyOneSeriesUseCase.execute(url, userAgent)
            _series.postValue(series)
        } else {
            _series.postValue(Series(listOf("No internet connection"), mutableListOf()))
        }
    }

    // Resolution List
    fun getResolution(
        url: String,
        userAgent: String
    ) = viewModelScope.launch(Dispatchers.IO) {
        if (checkInternetConnection()) {
            val res = getResolutionUseCase.execute(url, userAgent)
            _resolution.postValue(res)
        } else {
            _resolution.postValue(Resolution(listOf("No internet connection")))
        }
    }

    // Specific Link Series List
    fun getSpecificLinkSeries(
        listOfLinks: List<String>,
        userAgent: String,
        resolution: String
    ) = viewModelScope.launch(Dispatchers.IO) {
        if (checkInternetConnection()) {
            val specificLinks = getSpecificSeriesLinkUseCase.execute(listOfLinks, userAgent, resolution)
            _specificLinks.postValue(specificLinks)
        } else {
            _specificLinks.postValue(SpecificSeries(listOf("No internet connection"), mutableListOf()))
        }
    }

    fun isOnlyOneSeason(
        url: String,
        userAgent: String
    ) = viewModelScope.launch(Dispatchers.IO) {
        if (checkInternetConnection()) {
            val isOnlyOneSeason = isOnlyOneSeasonUseCase.execute(url, userAgent)
            _isOnlyOneSeason.postValue(isOnlyOneSeason)
        } else {
            _season.postValue(Season(listOf("No internet connection"), mutableListOf()))
        }
    }

    fun download(
        context: Context,
        userAgent: String,
        linkOfConcreteSeries: MutableList<String>,
        names: MutableList<String>
    ) : Long {
        return DownloadUseCase(DownloaderImpl(context)).execute(
            userAgent = userAgent,
            linkOfConcreteSeria = linkOfConcreteSeries,
            names = names
        )
    }

    fun progressObserve(
        viewLifecycleOwner: LifecycleOwner,
        context: Context,
        handler: Handler,
        downloadId: Long
    ) {
        val downloadObserver = DownloadProgressObserver(context, handler, downloadId)
        val contentResolver = context.contentResolver
        contentResolver.registerContentObserver(
            Uri.parse("content://downloads/all_downloads/$downloadId"),
            true,
            downloadObserver
        )

        downloadObserver.progress.observe(viewLifecycleOwner) {
            _progress.postValue(it)
        }

    }

    private fun checkInternetConnection(): Boolean {
        return connectivityManager
            .getNetworkCapabilities(connectivityManager.activeNetwork)
            ?.hasCapability(NET_CAPABILITY_INTERNET) == true
    }

}