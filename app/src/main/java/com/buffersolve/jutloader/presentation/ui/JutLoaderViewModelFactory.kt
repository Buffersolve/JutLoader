package com.buffersolve.jutloader.presentation.ui

import android.app.Application
import android.net.ConnectivityManager
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.buffersolve.jutloader.data.repository.RepositoryImpl
import com.buffersolve.jutloader.domain.usecase.*

@Suppress("UNCHECKED_CAST")
class JutLoaderViewModelFactory(
    private val app: Application,
    private val connectivityManager: ConnectivityManager
) : ViewModelProvider.Factory {

    private val repository by lazy { RepositoryImpl() }

    private val getSeasonUseCase by lazy { GetSeasonUseCase(repository = repository) }
    private val getSeriesUseCase by lazy { GetSeriesUseCase(repository = repository) }
    private val getOnlyOneSeasonsUseCase by lazy { GetOnlyOneSeasonUseCase(repository = repository) }
    private val getOnlyOneSeriesUseCase by lazy { GetOnlyOneSeriesUseCase(repository = repository) }
    private val getResolutionUseCase by lazy { GetResolutionUseCase(repository = repository) }
    private val getSpecificSeriesLinkUseCase by lazy { GetSpecificSeriesLinkUseCase(repository = repository) }
    private val isOnlyOneSeasonUseCase by lazy { IsOnlyOneSeasonUseCase(repository = repository) }

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return JutLoaderViewModel(
            app = app,
            connectivityManager = connectivityManager,
            getSeasonUseCase = getSeasonUseCase,
            getSeriesUseCase = getSeriesUseCase,
            getOnlyOneSeasonsUseCase = getOnlyOneSeasonsUseCase,
            getOnlyOneSeriesUseCase = getOnlyOneSeriesUseCase,
            getResolutionUseCase = getResolutionUseCase,
            getSpecificSeriesLinkUseCase = getSpecificSeriesLinkUseCase,
            isOnlyOneSeasonUseCase = isOnlyOneSeasonUseCase,
        ) as T
    }
}