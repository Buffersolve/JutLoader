package com.buffersolve.jutloader.domain.downloader

interface Downloader {

    fun download(
        userAgent: String,
        linkOfConcreteSeries: List<String>,
        names: MutableList<String>
    ): Long
}