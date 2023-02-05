package com.buffersolve.jutloader.di

import android.content.Context
import com.buffersolve.jutloader.data.downloader.DownloaderImpl
import com.buffersolve.jutloader.data.repository.RepositoryImpl
import com.buffersolve.jutloader.domain.downloader.Downloader
import com.buffersolve.jutloader.domain.repository.Repository
import com.buffersolve.jutloader.domain.usecase.*
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class DataModule {

    @Provides
    @Singleton
    fun provideRepository() : Repository {
        return RepositoryImpl()
    }

    @Provides
    @Singleton
    fun provideDownloader(@ApplicationContext context: Context) : Downloader {
        return DownloaderImpl(context)
    }

}