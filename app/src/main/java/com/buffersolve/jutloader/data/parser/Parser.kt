package com.buffersolve.jutloader.data.parser

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.buffersolve.jutloader.data.util.Constants.Companion.BASE_URL
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import kotlin.runCatching

object Parser {

    private val exceptionPrivateS = MutableStateFlow("")
    val exceptionS: StateFlow<String> = exceptionPrivateS.asStateFlow()

//    private val exceptionPrivate: MutableLiveData<String> = MutableLiveData()
//    val exception: LiveData<String>
//        get() = exceptionPrivate

    fun execute(url: String, userAgent: String): Document {

        val result = runCatching {
            Jsoup.connect(BASE_URL + url)
                .userAgent(userAgent)
                .get()
        }

        return if (result.isSuccess) {
            val res = result.getOrDefault(Document(BASE_URL + url))
//            Log.d("RESULT111", res.toString())
//            exceptionPrivate.postValue("")
            exceptionPrivateS.value = ""
            res
        } else {
//            exceptionPrivate.postValue("ERROR")
            exceptionPrivateS.value = "ERROR"
            Document(BASE_URL + url)
        }

//        if (result.isSuccess) {
//
//        }

//        return try {
//
//        } catch (e: HttpStatusException) {
//            exceptionPrivate.postValue(e.message)
//            Document("")
//        } catch (e: SocketTimeoutException) {
//            exceptionPrivate.postValue(e.message)
//            Document("")
//        }
//        return Document("")

    }

}