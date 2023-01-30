package com.buffersolve.jutloader.presentation.ui

import android.app.Application
import android.net.ConnectivityManager
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

@Suppress("UNCHECKED_CAST")
class JutLoaderViewModelFactory(
    private val app: Application,
    private val connectivityManager: ConnectivityManager
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return JutLoaderViewModel(app, connectivityManager) as T
    }
}