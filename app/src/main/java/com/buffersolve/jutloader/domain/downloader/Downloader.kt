package com.buffersolve.jutloader.domain.downloader

interface Downloader {

    fun download(
        userAgent: String,
        linkOfConcreteSeria: List<String>,
        names: MutableList<String>
    ): Long
}