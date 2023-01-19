package com.buffersolve.jutloader

import android.content.Context
import android.util.Log
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
        val films = doc.select("h2[class=\"b-b-title the-anime-season center films_title\"]").eachText()
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




        /// resolution

        val hardcodeRes = 720

        val doc3: Document = Jsoup.connect(url5)
            .userAgent(userAgent)
            .get()

        val resSelect = doc3.select("source[res=\"$hardcodeRes\"]")
            .attr("src")
//        val resElement: Elements = resSelect.select("source")



        // LiveData
        _season.postValue(season)



//        _events.emit(seasons)


//        println(_season.value)
//        Log.d("SEASON_TAG", seasons.toString())
        Log.d("SEASON_TAG", resSelect.toString())



        ////////////
        // DownloadManager

//        val downloadManager: android.app.DownloadManager = context.getSystemService(
//                    ComponentActivity.DOWNLOAD_SERVICE) as android.app.DownloadManager
//
//        Log.d("URLTAG", docEl.value)
//
//        val request = android.app.DownloadManager.Request(Uri.parse(docEl.value))
//            .addRequestHeader("User-Agent", userAgent)
//            .setAllowedOverMetered(true)
//            .setTitle("Download")
//            .setNotificationVisibility(android.app.DownloadManager.Request.VISIBILITY_VISIBLE)
//            .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "downloadfileName")
//
//        downloadManager.enqueue(request)

    }

    fun networkSeries (userAgent: String, urlForSeries: String) = viewModelScope.launch(Dispatchers.IO) {

        val doc2: Document = Jsoup.connect("https://jut.su" + urlForSeries)
            .userAgent(userAgent)
            .get()

        // Seria
        val seriaParce = doc2.select("a[class=\"short-btn black video the_hildi\"]")
            .textNodes().map {it.toString()}
        val seriaParceGreen = doc2.select("a[class=\"short-btn green video the_hildi\"]")
            .textNodes().map {it.toString()}

        val list = mutableListOf<String>()
        list.addAll(seriaParce)
        list.addAll(seriaParceGreen)
        list.toList()

        val elementsSeriaLink: Elements = doc2.select("a[class=\"short-btn black video the_hildi\"]")
            .select("a")
        val elementsSeriaLinkGreen: Elements = doc2.select("a[class=\"short-btn green video the_hildi\"]")
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

}