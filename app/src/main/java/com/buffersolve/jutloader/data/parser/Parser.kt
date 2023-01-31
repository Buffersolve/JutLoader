package com.buffersolve.jutloader.data.parser

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import org.jsoup.HttpStatusException
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import java.net.SocketTimeoutException

object Parser {

    private val exceptionPrivate: MutableLiveData<String> = MutableLiveData()
    val exception: LiveData<String>
        get() = exceptionPrivate

    fun execute(url: String, userAgent: String): Document {
        return try {
            val doc = Jsoup.connect("https://jut.su/$url")
                .userAgent(userAgent)
                .get()
            exceptionPrivate.postValue("")
            doc
        } catch (e: HttpStatusException) {
            exceptionPrivate.postValue(e.message)
            Document("")
        } catch (e: SocketTimeoutException) {
            exceptionPrivate.postValue(e.message)
            Document("")
        }
    }

}