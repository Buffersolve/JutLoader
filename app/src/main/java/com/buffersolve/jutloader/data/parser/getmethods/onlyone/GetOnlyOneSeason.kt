package com.buffersolve.jutloader.data.parser.getmethods.onlyone

import com.buffersolve.jutloader.data.parser.Parser
import com.buffersolve.jutloader.domain.model.Season

class GetOnlyOneSeason {

    fun execute(url: String, userAgent: String): Season {

        val doc = Parser.execute(
            url = url,
            userAgent = userAgent
        )

        if (doc.isSuccess) {
            val season =
                doc.getOrThrow().select("h1[class=\"header_video allanimevideo anime_padding_for_title\"]")
                    .eachText()

            return Season(season, mutableListOf(""))
        } else {
            return Season(listOf(), mutableListOf())
        }


//        return Season(list, elementsSeriaLinkList)

    }

}