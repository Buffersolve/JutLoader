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

    private val _hasOnlyOneSeasonToken: MutableLiveData<Boolean> = MutableLiveData()
    val hasOnlyOneSeasonToken: LiveData<Boolean>
        get() = _hasOnlyOneSeasonToken

//    private val _events = MutableSharedFlow<MutableList<String>>() // private mutable shared flow
//    val events = _events.asSharedFlow()

    fun networking(url: String, context: Context, userAgent: String) = viewModelScope.launch(Dispatchers.IO) {

        val doc: Document = Jsoup.connect(url)
            .userAgent(userAgent)
            .get()

        // Кількість сезонів

        if (doc.select("div[class=\"the_invis\"]").size > 0) {

            val isHasOnlyOneSeasonToken = false
            _hasOnlyOneSeasonToken.postValue(isHasOnlyOneSeasonToken)

            val seasons = doc.select("h2[class=\"b-b-title the-anime-season center\"]").eachText()
            val seasonsLinkS = doc.select("div[class=\"the_invis\"]")
            val elements: Elements = seasonsLinkS.select("a")

            val elList = mutableListOf<String>()

            elements.forEach {
                val attr = it.attr("href")
                elList.add(attr)
            }
            val season = Season(seasons, elList)
            _season.postValue(season)

        } else if (doc.select("div[class=\"the_invis\"]").size < 1) {

            val isHasOnlyOneSeasonToken = true
            _hasOnlyOneSeasonToken.postValue(isHasOnlyOneSeasonToken)

            val season = doc.select("h1[class=\"header_video allanimevideo anime_padding_for_title\"]").eachText()
            val seasonSeries = doc.select("a[class=\"short-btn black video the_hildi\"]")
                .textNodes().map { it.toString() }
            val seasonSeriesGreen = doc.select("a[class=\"short-btn green video the_hildi\"]")
                .textNodes().map { it.toString() }

            val listSeries = mutableListOf<String>()
            listSeries.addAll(seasonSeries)
            listSeries.addAll(seasonSeriesGreen)
            listSeries.toList()
            Log.d("SEASOLINK1", listSeries.toString())

            // Links
            val seasonSeriesLink = doc.select("a[class=\"short-btn black video the_hildi\"]")
            val elements: Elements = seasonSeriesLink.select("a")


            val elList = mutableListOf<String>()

            elements.forEach {
                val attr = it.attr("href")
                elList.add(attr)
            }

            val seasonOne = Season(season, elList)
            val seriesOne = Seria(seria = listSeries, seriaLink = elList)

            _season.postValue(seasonOne)
            _seria.postValue(seriesOne)
            Log.d("SEASOLINK1", _seria.value.toString())
//            Log.d("SEASOLINK1", _season.value.toString())

        }

//        val films =
//            doc.select("h2[class=\"b-b-title the-anime-season center films_title\"]").eachText()
//        seasons.addAll(films)

        ////

        // Посилання на сезон


        //


        ////

        // LiveData


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
        val listOfSeriesName = mutableListOf<String>()

        for (url in listOfLinks) {
            val doc3: Document = Jsoup.connect("https://jut.su" + url)
                .userAgent(userAgent)
                .get()

//            val name = doc3.select("span[itemprop=\"name\"]").eachText()
            val name = doc3.select("h1[class=\"header_video allanimevideo the_hildi anime_padding_for_title_post\"]")
                .eachText().map { it.replaceFirst("Смотреть", "") }.map { it.replaceFirst(" ", "") }

            listOfSeriesName.addAll(name)
            Log.d("NAMEOFSER", listOfSeriesName.toString())

            val resSelect = doc3.select("source[res=\"$hardcodeRes\"]")
                .attr("src")

            linkOfConcreteSeria.add(resSelect)

            Log.d("RESELECT", resSelect)

//            Log.d("NAMELIST", listOfSeriesName.toString())

        }

        Log.d("NAMELIST", listOfSeriesName.toString())

        downloadManager(
            context = context,
            userAgent = userAgent,
            linkOfConcreteSeria,
            listOfSeriesName
        )

    }

    fun downloadManager(
        context: Context,
        userAgent: String,
        linkOfConcreteSeria: MutableList<String>,
        names: MutableList<String>
        ) {
        val downloadManager: DownloadManager = context.getSystemService(
            ComponentActivity.DOWNLOAD_SERVICE
        ) as DownloadManager

//        Log.d("URLTAG", docEl.value)

        for (url in linkOfConcreteSeria) {
                val request = DownloadManager.Request(Uri.parse(url))
                    .addRequestHeader("User-Agent", userAgent)
                    .setAllowedOverMetered(true)
//                    .setTitle("Downloading File")
//                    .setDescription("Download in progress")
                    .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE)
                    .setDestinationInExternalPublicDir(
                        Environment.DIRECTORY_DOWNLOADS,
                        "${names[linkOfConcreteSeria.indexOf(url)]}.mp4"
                    )

                downloadManager.enqueue(request)
        }
    }

}