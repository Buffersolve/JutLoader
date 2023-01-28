package com.buffersolve.jutloader.data.parser.getmethods.onlyone

import android.util.Log
import com.buffersolve.jutloader.data.parser.Parser
import com.buffersolve.jutloader.domain.model.Season
import com.buffersolve.jutloader.domain.model.Seria
import org.jsoup.select.Elements

class GetOnlyOneSeason {

    fun execute(url: String, userAgent: String): Season {

        val doc = Parser.execute(
            url = url,
            userAgent = userAgent
        )

        val season =
            doc.select("h1[class=\"header_video allanimevideo anime_padding_for_title\"]")
                .eachText()

        return Season(season, mutableListOf(""))

//        return Season(list, elementsSeriaLinkList)

    }

}