package com.buffersolve.jutloader.data.parser.getmethods.onlyone

import com.buffersolve.jutloader.data.parser.Parser
import com.buffersolve.jutloader.domain.model.OneSeason

class IsOnlyOneSeasons {

    fun execute(url: String, userAgent: String): OneSeason {

        val doc = Parser.execute(
            url = url,
            userAgent = userAgent
        )

        if (doc.isSuccess) {
            val isHasOnlyOneSeason: Boolean =
                when (doc.getOrThrow().select("div[class=\"the_invis\"]").size > 0) {
                    true -> false
                    false -> true
                }
            return OneSeason(isHasOnlyOneSeason, null)
        } else {
//            return OneSeason(null, Exception("Error"))
            return OneSeason(null, true)
        }

//        val isHasOnlyOneSeason: Boolean =
//            when (doc.select("div[class=\"the_invis\"]").size > 0) {
//                true -> false
//                false -> true
//            }
//
//        return isHasOnlyOneSeason

    }

}