package com.buffersolve.jutloader.presentation.ui

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.buffersolve.jutloader.data.downloader.DownloaderImpl
import com.buffersolve.jutloader.data.parser.getmethods.onlyone.IsOnlyOneSeasons
import com.buffersolve.jutloader.data.repository.RepositoryImpl
import com.buffersolve.jutloader.domain.downloader.Downloader
import com.buffersolve.jutloader.domain.model.Resolution
import com.buffersolve.jutloader.domain.model.Season
import com.buffersolve.jutloader.domain.model.Seria
import com.buffersolve.jutloader.domain.model.SpecificSeries
import com.buffersolve.jutloader.domain.usecase.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class JutLoaderViewModel: ViewModel() {

    private val _season: MutableLiveData<Season> = MutableLiveData()
    val season: LiveData<Season>
        get() = _season

    private val _series: MutableLiveData<Seria> = MutableLiveData()
    val series: LiveData<Seria>
        get() = _series

    private val _resolution: MutableLiveData<Resolution> = MutableLiveData()
    val resolution: LiveData<Resolution>
        get() = _resolution

    private val _specificLinks: MutableLiveData<SpecificSeries> = MutableLiveData()
    val specificLinks: LiveData<SpecificSeries>
        get() = _specificLinks

    private val _isOnlyOneSeason: MutableLiveData<Boolean> = MutableLiveData()
    val isOnlyOneSeason: LiveData<Boolean>
        get() = _isOnlyOneSeason

    private val repository = RepositoryImpl()
//    private val repository = DownloaderImpl()

    // Seasons List
    fun getSeasons(
        url: String,
        userAgent: String
    ) = viewModelScope.launch(Dispatchers.IO) {

        val seasons = GetSeasonUseCase(repository).execute(url, userAgent)
        _season.postValue(seasons)

    }

    // Series List
    fun getSeries(
        url: String,
        userAgent: String
    ) = viewModelScope.launch(Dispatchers.IO)  {

        val series = GetSeriesUseCase(repository).execute(url, userAgent)
        _series.postValue(series)

    }

    fun getOnlyOneSeasons(
        url: String,
        userAgent: String
    ) = viewModelScope.launch(Dispatchers.IO) {

        val seasons = GetOnlyOneSeasonUseCase(repository).execute(url, userAgent)
        _season.postValue(seasons)

    }

    // Series List
    fun getOnlyOneSeries(
        url: String,
        userAgent: String
    ) = viewModelScope.launch(Dispatchers.IO)  {

        val series = GetOnlyOneSeriesUseCase(repository).execute(url, userAgent)
        _series.postValue(series)

    }

    // Resolution List
    fun getResolution(
        url: String,
        userAgent: String
    ) = viewModelScope.launch(Dispatchers.IO)  {

        val res = GetResolutionUseCase(repository).execute(url, userAgent)
        _resolution.postValue(res)

    }

    // Specific Link Series List
    fun getSpecificLinkSeries(
        listOfLinks: List<String>,
        userAgent: String,
        resolution: String
    ) = viewModelScope.launch(Dispatchers.IO)  {

        val specificLinks = GetSpecificSeriesLinkUseCase(repository).execute(listOfLinks, userAgent, resolution)
        _specificLinks.postValue(specificLinks)

    }

    fun isOnlyOneSeason(
        url: String,
        userAgent: String
    ) = viewModelScope.launch(Dispatchers.IO) {
        val isOnlyOneSeason = IsOnlyOneSeasons().execute(url, userAgent)
        _isOnlyOneSeason.postValue(isOnlyOneSeason)
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

}