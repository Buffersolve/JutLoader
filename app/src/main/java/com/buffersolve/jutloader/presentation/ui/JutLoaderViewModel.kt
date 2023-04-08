package com.buffersolve.jutloader.presentation.ui

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities.*
import android.net.Uri
import android.os.Handler
import androidx.lifecycle.*
import com.buffersolve.jutloader.data.provider.DownloadProgressObserver
import com.buffersolve.jutloader.domain.model.*
import com.buffersolve.jutloader.domain.usecase.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class JutLoaderViewModel @Inject constructor(
    private val connectivityManager: ConnectivityManager,
    private val getSeasonUseCase: GetSeasonUseCase,
    private val getEpisodesUseCase: GetEpisodesUseCase,
    private val getOnlyOneSeasonsUseCase: GetOnlyOneSeasonUseCase,
    private val getOnlyOneEpisodeUseCase: GetOnlyOneEpisodeUseCase,
    private val getResolutionUseCase: GetResolutionUseCase,
    private val getSpecificEpisodesLinkUseCase: GetSpecificEpisodesLinkUseCase,
    private val isOnlyOneSeasonUseCase: IsOnlyOneSeasonUseCase,
    private val downloadUseCase: DownloadUseCase,
) : ViewModel() {

    // Flow
    private val _season = MutableSharedFlow<Season>(replay = 1)
    val season: SharedFlow<Season> = _season.asSharedFlow()

    private val _episodes = MutableSharedFlow<Episodes>(replay = 1)
    val episodes: SharedFlow<Episodes> = _episodes.asSharedFlow()

    private val _specificLinks = MutableSharedFlow<SpecificEpisode>(replay = 1)
    val specificLinks: SharedFlow<SpecificEpisode> = _specificLinks.asSharedFlow()

    private val _resolution = MutableSharedFlow<Resolution>(replay = 1)
    val resolution: SharedFlow<Resolution> = _resolution.asSharedFlow()

    private val _isOnlyOneSeason = MutableSharedFlow<OneSeason>(replay = 1)
    val isOnlyOneSeason: SharedFlow<OneSeason> = _isOnlyOneSeason.asSharedFlow()

    private val _progress = MutableSharedFlow<Long>(replay = 1)
    var progress: SharedFlow<Long> = _progress.asSharedFlow()

    private val errorHashMap = hashMapOf("No internet connection" to "No internet connection")

    // Seasons List
    fun getSeasons(
        url: String,
        userAgent: String
    ) = viewModelScope.launch(Dispatchers.IO) {
        if (checkInternetConnection()) {
            val seasons = getSeasonUseCase.execute(url, userAgent)
            _season.emit(seasons)
        } else {
            _season.emit(Season(errorHashMap))
        }
    }

    // Episodes List
    fun getSeries(
        url: String,
        userAgent: String
    ) = viewModelScope.launch(Dispatchers.IO) {
        if (checkInternetConnection()) {
            val series = getEpisodesUseCase.execute(url, userAgent)
            _episodes.emit(series)
        } else {
            _episodes.emit(Episodes(errorHashMap))
        }
    }

    fun getOnlyOneSeasons(
        url: String,
        userAgent: String
    ) = viewModelScope.launch(Dispatchers.IO) {
        if (checkInternetConnection()) {
            val seasons = getOnlyOneSeasonsUseCase.execute(url, userAgent)
            _season.emit(seasons)
        } else {
            _season.emit(Season(errorHashMap))
//            _season.value = Season(listOf("No internet connection"), mutableListOf())

        }
    }

    // Episodes List
    fun getOnlyOneSeries(
        url: String,
        userAgent: String
    ) = viewModelScope.launch(Dispatchers.IO) {
        if (checkInternetConnection()) {
            val series = getOnlyOneEpisodeUseCase.execute(url, userAgent)
            _episodes.emit(series)
        } else {
            _episodes.emit(Episodes(errorHashMap))
        }
    }

    // Resolution List
    fun getResolution(
        url: String,
        userAgent: String
    ) = viewModelScope.launch(Dispatchers.IO) {
        if (checkInternetConnection()) {
            val res = getResolutionUseCase.execute(url, userAgent)
            _resolution.emit(res)
        } else {
            _resolution.emit(Resolution(listOf("No internet connection")))
        }
    }

    // Specific Link Episodes List
    fun getSpecificLinkSeries(
        listOfLinks: List<String>,
        userAgent: String,
        resolution: String
    ) = viewModelScope.launch(Dispatchers.IO) {
        if (checkInternetConnection()) {
            val specificLinks =
                getSpecificEpisodesLinkUseCase.execute(listOfLinks, userAgent, resolution)
            _specificLinks.emit(specificLinks)
        } else {
            _specificLinks.emit(SpecificEpisode(listOf("No internet connection"), mutableListOf()))
        }
    }

    fun isOnlyOneSeason(
        url: String,
        userAgent: String
    ) = viewModelScope.launch(Dispatchers.IO) {
        if (checkInternetConnection()) {
            val isOnlyOneSeason = isOnlyOneSeasonUseCase.execute(url, userAgent)
            _isOnlyOneSeason.emit(isOnlyOneSeason)
        } else {
            _season.emit(Season(errorHashMap))
        }
    }

    fun download(
        userAgent: String,
        linkOfConcreteSeries: MutableList<String>,
        names: MutableList<String>
    ): Long {
        return downloadUseCase.execute(
            userAgent = userAgent,
            linkOfConcreteSeria = linkOfConcreteSeries,
            names = names
        )
    }

    fun progressObserve(
        context: Context,
        handler: Handler,
        downloadId: Long
    ) = viewModelScope.launch(Dispatchers.Unconfined) {

        val downloadObserver = DownloadProgressObserver(context, handler, downloadId)
        val contentResolver = context.contentResolver
        contentResolver.registerContentObserver(
            Uri.parse("content://downloads/all_downloads/$downloadId"),
            true,
            downloadObserver
        )

        /////

        viewModelScope.launch {
            downloadObserver.progress.collect {
                _progress.emit(it)
            }
        }

    }

    private fun checkInternetConnection(): Boolean {
        return connectivityManager
            .getNetworkCapabilities(connectivityManager.activeNetwork)
            ?.hasCapability(NET_CAPABILITY_INTERNET) == true
    }

}