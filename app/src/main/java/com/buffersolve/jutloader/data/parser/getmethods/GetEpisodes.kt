package com.buffersolve.jutloader.data.parser.getmethods

import android.util.Log
import com.buffersolve.jutloader.data.parser.Parser
import com.buffersolve.jutloader.domain.model.Episodes
import org.jsoup.select.Elements
import kotlin.math.log

class GetEpisodes {

    fun execute(url: String, userAgent: String): Episodes {

        val doc = Parser.execute(
            url = url,
            userAgent = userAgent
        )

        if (doc.isSuccess) {
            val episodeParse = doc.getOrThrow()
                .select("a[class=\"short-btn black video the_hildi\"]")
                .textNodes().map { it.toString() }
            val episodeParseGreen = doc.getOrThrow()
                .select("a[class=\"short-btn green video the_hildi\"]")
                .textNodes().map { it.toString() }

            val listOfEpisodes = mutableListOf<String>().apply {
                addAll(episodeParse)
                addAll(episodeParseGreen)
            }

            val elementsEpisodeLink: Elements =
                doc.getOrThrow().select("a[class=\"short-btn black video the_hildi\"]")
                    .select("a")
            val elementsEpisodeLinkGreen: Elements =
                doc.getOrThrow().select("a[class=\"short-btn green video the_hildi\"]")
                    .select("a")

            val elementsEpisodeLinkList = mutableListOf<String>()

            elementsEpisodeLink.forEach {
                val attr = it.attr("href")
                elementsEpisodeLinkList.add(attr)
            }

            elementsEpisodeLinkGreen.forEach {
                val attr = it.attr("href")
                elementsEpisodeLinkList.add(attr)
            }

            val episodeHashMap = listOfEpisodes.zip(elementsEpisodeLinkList).toMap(LinkedHashMap())


//            Log.d("GetEpisodes", "execute: $episodeHashMap")

//            return Episodes(list, elementsEpisodeLinkList)
            return Episodes(episodeHashMap)
        } else {
            return Episodes(hashMapOf())
        }

    }

}