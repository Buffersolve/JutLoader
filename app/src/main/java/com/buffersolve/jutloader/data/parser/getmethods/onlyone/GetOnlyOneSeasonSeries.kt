package com.buffersolve.jutloader.data.parser.getmethods.onlyone

import com.buffersolve.jutloader.data.parser.Parser
import com.buffersolve.jutloader.domain.model.Series
import org.jsoup.select.Elements

class GetOnlyOneSeasonSeries {

    fun execute(url: String, userAgent: String): Series {

        val doc = Parser.execute(
            url = url,
            userAgent = userAgent
        )

        if (doc.isSuccess) {
            val seasonSeriesGreen = doc.getOrThrow().select("a[class=\"short-btn green video the_hildi\"]")
                .textNodes().map { it.toString() }
            val seasonSeriesBlack = doc.getOrThrow().select("a[class=\"short-btn black video the_hildi\"]")
                .textNodes().map { it.toString() }


            val listSeries = mutableListOf<String>()
            listSeries.addAll(seasonSeriesGreen)
            listSeries.addAll(seasonSeriesBlack)
            listSeries.toList()
//        Log.d("SEASOLINK1111", listSeries.toString())

            // Links
            val elementsGreen: Elements = doc.getOrThrow().select("a[class=\"short-btn green video the_hildi\"]")
                .select("a")
            val elementsBlack: Elements = doc.getOrThrow().select("a[class=\"short-btn black video the_hildi\"]")
                .select("a")

            val elList = mutableListOf<String>()

            elementsGreen.forEach {
                val attr = it.attr("href")
                elList.add(attr)
            }

            elementsBlack.forEach {
                val attr = it.attr("href")
                elList.add(attr)
            }

            return Series(seria = listSeries, seriaLink = elList)
        } else {
            return Series(listOf(), mutableListOf())
        }

//        val seasonSeriesGreen = doc.select("a[class=\"short-btn green video the_hildi\"]")
//            .textNodes().map { it.toString() }
//        val seasonSeriesBlack = doc.select("a[class=\"short-btn black video the_hildi\"]")
//            .textNodes().map { it.toString() }
//
//
//        val listSeries = mutableListOf<String>()
//        listSeries.addAll(seasonSeriesGreen)
//        listSeries.addAll(seasonSeriesBlack)
//        listSeries.toList()
////        Log.d("SEASOLINK1111", listSeries.toString())
//
//        // Links
//        val elementsGreen: Elements = doc.select("a[class=\"short-btn green video the_hildi\"]")
//            .select("a")
//        val elementsBlack: Elements = doc.select("a[class=\"short-btn black video the_hildi\"]")
//            .select("a")
//
//        val elList = mutableListOf<String>()
//
//        elementsGreen.forEach {
//            val attr = it.attr("href")
//            elList.add(attr)
//        }
//
//        elementsBlack.forEach {
//            val attr = it.attr("href")
//            elList.add(attr)
//        }
//
//        return Series(seria = listSeries, seriaLink = elList)


    }

}