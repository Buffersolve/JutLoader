package com.buffersolve.jutloader.data.parser.getmethods.onlyone

import com.buffersolve.jutloader.data.parser.Parser

class IsOnlyOneSeasons {

    fun execute(url: String, userAgent: String): Boolean {

        val doc = Parser.execute(
            url = url,
            userAgent = userAgent
        )

        val isHasOnlyOneSeason: Boolean =
            when (doc.select("div[class=\"the_invis\"]").size > 0) {
                true -> false
                false -> true
            }

        return isHasOnlyOneSeason

    }

}