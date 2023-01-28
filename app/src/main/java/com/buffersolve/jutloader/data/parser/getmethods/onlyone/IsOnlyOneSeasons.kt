package com.buffersolve.jutloader.data.parser.getmethods.onlyone

import android.util.Log
import com.buffersolve.jutloader.data.parser.Parser
import com.buffersolve.jutloader.domain.model.Season
import com.buffersolve.jutloader.domain.model.Seria
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.select.Elements

class IsOnlyOneSeasons {

    fun execute(url: String, userAgent: String): Boolean {

        val doc = Parser.execute(
            url = url,
            userAgent = userAgent
        )

        val isHasOnlyOneSeasonToken: Boolean =
            when (doc.select("div[class=\"the_invis\"]").size > 0) {
            true -> false
            false -> true
        }

            return isHasOnlyOneSeasonToken

    }

}