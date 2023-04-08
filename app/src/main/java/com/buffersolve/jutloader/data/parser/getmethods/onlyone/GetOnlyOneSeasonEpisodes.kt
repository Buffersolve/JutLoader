package com.buffersolve.jutloader.data.parser.getmethods.onlyone

import com.buffersolve.jutloader.data.parser.Parser
import com.buffersolve.jutloader.domain.model.Episodes
import com.buffersolve.jutloader.domain.model.Season
import org.jsoup.select.Elements

class GetOnlyOneSeasonEpisodes {

    fun execute(url: String, userAgent: String): Episodes {

        val doc = Parser.execute(
            url = url,
            userAgent = userAgent
        )

        if (doc.isSuccess) {
            val seasonSeriesGreen = doc.getOrThrow().select("a[class=\"short-btn green video the_hildi\"]")
                .textNodes().map { it.toString() }
            val seasonSeriesBlack = doc.getOrThrow().select("a[class=\"short-btn black video the_hildi\"]")
                .textNodes().map { it.toString() }

            val listSeries = mutableListOf<String>().apply {
                addAll(seasonSeriesGreen)
                addAll(seasonSeriesBlack)
            }

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

            // HashMap of series and links
            val seasonEpisodesHashMap = listSeries.zip(elList).toMap(LinkedHashMap())

            return Episodes(seasonEpisodesHashMap)
        } else {
            return Episodes(hashMapOf())
        }

    }

}