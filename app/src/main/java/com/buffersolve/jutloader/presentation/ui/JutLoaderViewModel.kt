package com.buffersolve.jutloader.presentation.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.buffersolve.jutloader.data.repository.RepositoryImpl
import com.buffersolve.jutloader.domain.model.Resolution
import com.buffersolve.jutloader.domain.model.Season
import com.buffersolve.jutloader.domain.model.Seria
import com.buffersolve.jutloader.domain.usecase.GetResolutionUseCase
import com.buffersolve.jutloader.domain.usecase.GetSeasonUseCase
import com.buffersolve.jutloader.domain.usecase.GetSeriesUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class JutLoaderViewModel: ViewModel() {

    private val _season: MutableLiveData<Season> = MutableLiveData()
    val season: LiveData<Season>
        get() = _season

    private val _seria: MutableLiveData<Seria> = MutableLiveData()
    val seria: LiveData<Seria>
        get() = _seria

    private val _resolution: MutableLiveData<Resolution> = MutableLiveData()
    val resolution: LiveData<Resolution>
        get() = _resolution

    private val repository = RepositoryImpl()

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
        _seria.postValue(series)

    }

    // Resolution List
    fun getResolution(
        url: String,
        userAgent: String
    ) = viewModelScope.launch(Dispatchers.IO)  {

        val res = GetResolutionUseCase(repository).execute(url, userAgent)
        _resolution.postValue(res)

    }

}