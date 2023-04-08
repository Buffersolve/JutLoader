package com.buffersolve.jutloader.di

import android.content.Context
import android.net.ConnectivityManager
import com.buffersolve.jutloader.domain.downloader.Downloader
import com.buffersolve.jutloader.domain.repository.Repository
import com.buffersolve.jutloader.domain.usecase.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext

@Module
@InstallIn(ViewModelComponent::class)
class DomainModule {

    @Provides
    fun provideConnectivityManager(@ApplicationContext context: Context): ConnectivityManager {
        return context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    }

    @Provides
    fun provideGetSeasonUseCase(repository: Repository) : GetSeasonUseCase {
        return GetSeasonUseCase(repository = repository)
    }

    @Provides
    fun provideGetSeriesUseCase(repository: Repository) : GetEpisodesUseCase {
        return GetEpisodesUseCase(repository = repository)
    }

    @Provides
    fun provideGetOnlyOneSeasonUseCase(repository: Repository) : GetOnlyOneSeasonUseCase {
        return GetOnlyOneSeasonUseCase(repository = repository)
    }

    @Provides
    fun provideGetOnlyOneSeriesUseCase(repository: Repository) : GetOnlyOneEpisodeUseCase {
        return GetOnlyOneEpisodeUseCase(repository = repository)
    }

    @Provides
    fun provideGetResolutionUseCase(repository: Repository) : GetResolutionUseCase {
        return GetResolutionUseCase(repository = repository)
    }

    @Provides
    fun provideGetSpecificSeriesLinkUseCase(repository: Repository) : GetSpecificEpisodesLinkUseCase {
        return GetSpecificEpisodesLinkUseCase(repository = repository)
    }

    @Provides
    fun provideIsOnlyOneSeasonUseCase(repository: Repository) : IsOnlyOneSeasonUseCase {
        return IsOnlyOneSeasonUseCase(repository = repository)
    }

    @Provides
    fun provideDownloadUseCase(downloader: Downloader) : DownloadUseCase {
        return DownloadUseCase(downloader = downloader)
    }

//    @Provides
//    fun provideGetProgress(repository: Repository) : GetDownloadProgressObserverUseCase {
//        return GetDownloadProgressObserverUseCase(repository = repository)
//    }

}