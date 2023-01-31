package com.buffersolve.jutloader.presentation.ui

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.net.NetworkCapabilities.*
import android.util.Log
import androidx.lifecycle.*
import com.buffersolve.jutloader.JutLoaderApplication
import com.buffersolve.jutloader.data.downloader.DownloaderImpl
import com.buffersolve.jutloader.data.parser.getmethods.onlyone.IsOnlyOneSeasons
import com.buffersolve.jutloader.data.repository.RepositoryImpl
import com.buffersolve.jutloader.domain.model.Resolution
import com.buffersolve.jutloader.domain.model.Season
import com.buffersolve.jutloader.domain.model.Series
import com.buffersolve.jutloader.domain.model.SpecificSeries
import com.buffersolve.jutloader.domain.usecase.*
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.IOException

class JutLoaderViewModel(
    app: Application,
    private val connectivityManager: ConnectivityManager
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

    private val repository = RepositoryImpl()
//    private val repository = DownloaderImpl()

//    val coroutineExceptionHandler = CoroutineExceptionHandler{_, throwable ->
//        throwable.printStackTrace()
//    }

//    init {
//        checkInternetConnection()
//
//    }


    // Seasons List
    fun getSeasons(
        url: String,
        userAgent: String
    ) = viewModelScope.launch(Dispatchers.IO) {
//        safeGetSeasons(url, userAgent)

        if (checkInternetConnection()) {
            val seasons = GetSeasonUseCase(repository).execute(url, userAgent)
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
            val series = GetSeriesUseCase(repository).execute(url, userAgent)
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
            val seasons = GetOnlyOneSeasonUseCase(repository).execute(url, userAgent)
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
            val series = GetOnlyOneSeriesUseCase(repository).execute(url, userAgent)
            _series.postValue(series)
        } else {
            _season.postValue(Season(listOf("No internet connection"), mutableListOf()))
        }
    }

    // Resolution List
    fun getResolution(
        url: String,
        userAgent: String
    ) = viewModelScope.launch(Dispatchers.IO) {

        if (checkInternetConnection()) {
            val res = GetResolutionUseCase(repository).execute(url, userAgent)
            _resolution.postValue(res)
        } else {
            _season.postValue(Season(listOf("No internet connection"), mutableListOf()))
        }

    }

    // Specific Link Series List
    fun getSpecificLinkSeries(
        listOfLinks: List<String>,
        userAgent: String,
        resolution: String
    ) = viewModelScope.launch(Dispatchers.IO) {

        if (checkInternetConnection()) {
            val specificLinks =
                GetSpecificSeriesLinkUseCase(repository).execute(listOfLinks, userAgent, resolution)
            _specificLinks.postValue(specificLinks)
        } else {
            _season.postValue(Season(listOf("No internet connection"), mutableListOf()))
        }

    }

    fun isOnlyOneSeason(
        url: String,
        userAgent: String
    ) = viewModelScope.launch(Dispatchers.IO) {

        if (checkInternetConnection()) {
            val isOnlyOneSeason = IsOnlyOneSeasons().execute(url, userAgent)
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
    ) {
        DownloadUseCase(DownloaderImpl(context)).execute(
            userAgent = userAgent,
            linkOfConcreteSeria = linkOfConcreteSeries,
            names = names
        )
    }

    private fun checkInternetConnection(): Boolean {
        return connectivityManager
            .getNetworkCapabilities(connectivityManager.activeNetwork)
            ?.hasCapability(NET_CAPABILITY_INTERNET) == true
    }

}