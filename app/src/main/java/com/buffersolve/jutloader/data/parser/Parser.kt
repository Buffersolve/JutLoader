package com.buffersolve.jutloader.data.parser

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import org.jsoup.HttpStatusException
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import java.net.SocketTimeoutException

object Parser {

    private val _exception: MutableLiveData<Exception> = MutableLiveData()
    val exception: LiveData<Exception>
        get() = _exception

    fun execute(url: String, userAgent: String): Document {
        return try {
            Jsoup.connect("https://jut.su/$url")
                .userAgent(userAgent)
                .get()
        } catch (e: HttpStatusException) {
            _exception.postValue(e)
            Document("")
        } catch (e: SocketTimeoutException) {
            _exception.postValue(e)
            Document("")
        }
    }

}