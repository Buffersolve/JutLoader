package com.buffersolve.jutloader.data.parser.getmethods

import com.buffersolve.jutloader.data.parser.Parser
import com.buffersolve.jutloader.domain.model.Resolution

class GetResolution {

    fun execute(url: String, userAgent: String): Resolution {

            val doc = Parser.execute(
                url = url,
                userAgent = userAgent
            )

            val res = doc.select("source")
                .eachAttr("res").map { it + "p" }

        return Resolution(res)

    }

}