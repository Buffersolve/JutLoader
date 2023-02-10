package com.buffersolve.jutloader.data.parser.getmethods

import com.buffersolve.jutloader.data.parser.Parser
import com.buffersolve.jutloader.domain.model.Season
import org.jsoup.select.Elements

class GetSeasons {

    fun execute(url: String, userAgent: String): Season {
        val doc = Parser.execute(
            url = url,
            userAgent = userAgent
        )

        if (doc.isSuccess) {
            val seasons =
                doc.getOrThrow().select("h2[class=\"b-b-title the-anime-season center\"]").eachText()
            val seasonsLinkS = doc.getOrThrow().select("div[class=\"the_invis\"]")
            val elements: Elements = seasonsLinkS.select("a")

            val elList = mutableListOf<String>()

            elements.forEach {
                val attr = it.attr("href")
                elList.add(attr)
            }

            return Season(seasons, elList)
        } else {
            return Season(listOf(), mutableListOf())
        }

//        val seasons =
//            doc.select("h2[class=\"b-b-title the-anime-season center\"]").eachText()
//        val seasonsLinkS = doc.select("div[class=\"the_invis\"]")
//        val elements: Elements = seasonsLinkS.select("a")
//
//        val elList = mutableListOf<String>()
//
//        elements.forEach {
//            val attr = it.attr("href")
//            elList.add(attr)
//        }
//
//        return Season(seasons, elList)

    }

}