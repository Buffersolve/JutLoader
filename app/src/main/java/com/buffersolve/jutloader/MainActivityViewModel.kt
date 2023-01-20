package com.buffersolve.jutloader

import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.Environment
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.buffersolve.jutloader.model.Season
import com.buffersolve.jutloader.model.Seria
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import org.jsoup.select.Elements

class MainActivityViewModel : ViewModel() {

    private val _season: MutableLiveData<Season> = MutableLiveData()
    val season: LiveData<Season>
        get() = _season

    private val _seria: MutableLiveData<Seria> = MutableLiveData()
    val seria: LiveData<Seria>
        get() = _seria

//    private val _events = MutableSharedFlow<MutableList<String>>() // private mutable shared flow
//    val events = _events.asSharedFlow()

    fun networking(context: Context, userAgent: String) = viewModelScope.launch(Dispatchers.IO) {

        val doc: Document = Jsoup.connect(url2)
            .userAgent(userAgent)
            .get()

        // Кількість сезонів
        val seasons = doc.select("h2[class=\"b-b-title the-anime-season center\"]").eachText()
        val films =
            doc.select("h2[class=\"b-b-title the-anime-season center films_title\"]").eachText()
        seasons.addAll(films)

        ////

        // Посилання на сезон
        val seasonsLinkS = doc.select("div[class=\"the_invis\"]")
        val elements: Elements = seasonsLinkS.select("a")

        val elList = mutableListOf<String>()

        elements.forEach {
            val attr = it.attr("href")
            elList.add(attr)
        }

        //

        val season = Season(seasons, elList)

        ////

        // LiveData
        _season.postValue(season)


//        _events.emit(seasons)


    }

    fun networkSeries(userAgent: String, urlForSeries: String) =
        viewModelScope.launch(Dispatchers.IO) {

            val doc2: Document = Jsoup.connect("https://jut.su" + urlForSeries)
                .userAgent(userAgent)
                .get()

            // Seria
            val seriaParce = doc2.select("a[class=\"short-btn black video the_hildi\"]")
                .textNodes().map { it.toString() }
            val seriaParceGreen = doc2.select("a[class=\"short-btn green video the_hildi\"]")
                .textNodes().map { it.toString() }

            val list = mutableListOf<String>()
            list.addAll(seriaParce)
            list.addAll(seriaParceGreen)
            list.toList()

            val elementsSeriaLink: Elements =
                doc2.select("a[class=\"short-btn black video the_hildi\"]")
                    .select("a")
            val elementsSeriaLinkGreen: Elements =
                doc2.select("a[class=\"short-btn green video the_hildi\"]")
                    .select("a")

//        val listLinks = mutableListOf<String>()
//        listLinks.addAll(elementsSeriaLink)
//        listLinks.addAll(elementsSeriaLinkGreen)
//        listLinks.toList()

            val elementsSeriaLinkList = mutableListOf<String>()

            elementsSeriaLink.forEach {
                val attr = it.attr("href")
                elementsSeriaLinkList.add(attr)
            }

            elementsSeriaLinkGreen.forEach {
                val attr = it.attr("href")
                elementsSeriaLinkList.add(attr)
            }

            ////

            val seria = Seria(list, elementsSeriaLinkList)

            // Live Data
            _seria.postValue(seria)
        }

    fun networkingResolutionAndFinal(
        context: Context,
        userAgent: String,
        listOfSeries: List<String>,
        listOfLinks: List<String>
    ) = viewModelScope.launch(Dispatchers.IO) {

        val hardcodeRes = 720

        val linkOfConcreteSeria = mutableListOf<String>()

        for (url in listOfLinks) {
            val doc3: Document = Jsoup.connect("https://jut.su" + url)
                .userAgent(userAgent)
                .get()

            val resSelect = doc3.select("source[res=\"$hardcodeRes\"]")
                .attr("src")

            linkOfConcreteSeria.add(resSelect)

            Log.d("RESELECT", resSelect)

        }

        downloadManager(
            context = context,
            userAgent = userAgent,
            linkOfConcreteSeria)

    }

    fun downloadManager(
        context: Context,
        userAgent: String,
        linkOfConcreteSeria: MutableList<String>
        ) {
        val downloadManager: DownloadManager = context.getSystemService(
            ComponentActivity.DOWNLOAD_SERVICE
        ) as DownloadManager

//        Log.d("URLTAG", docEl.value)

        for (url in linkOfConcreteSeria) {

            val request = DownloadManager.Request(Uri.parse(url))
                .addRequestHeader("User-Agent", userAgent)
                .setAllowedOverMetered(true)
                .setTitle("Download")
                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE)
                .setDestinationInExternalPublicDir(
                    Environment.DIRECTORY_DOWNLOADS,
                    "downloadfileName.mp4"
                )

        downloadManager.enqueue(request)

        }
    }

}