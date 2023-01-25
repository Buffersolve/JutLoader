package com.buffersolve.jutloader.data.parser.getmethods

import android.util.Log
import com.buffersolve.jutloader.data.parser.Parser
import com.buffersolve.jutloader.domain.model.Resolution
import com.buffersolve.jutloader.domain.model.Season
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.select.Elements

class GetResolution {

    fun execute(url: String, userAgent: String): Resolution {

        val linkOfConcreteSeries = mutableListOf<String>()
        val listOfSeriesName = mutableListOf<String>()


            val doc = Parser.execute(
                url = url,
                userAgent = userAgent
            )

//            val name = doc3.select("span[itemprop=\"name\"]").eachText()
//            val name =
//                doc3.select("h1[class=\"header_video allanimevideo the_hildi anime_padding_for_title_post\"]")
//                    .eachText().map { it.replaceFirst("Смотреть", "") }
//                    .map { it.replaceFirst(" ", "") }
//
//            listOfSeriesName.addAll(name)
//            Log.d("NAMEOFSER", listOfSeriesName.toString())

            val res = doc.select("source")
                .eachAttr("res")


//            linkOfConcreteSeries.add(res)

//            Log.d("RESELECT", res.toString())

//            Log.d("NAMELIST", listOfSeriesName.toString())


        return Resolution(res)

    }

}