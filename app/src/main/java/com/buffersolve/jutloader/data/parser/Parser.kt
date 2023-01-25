package com.buffersolve.jutloader.data.parser

import org.jsoup.Jsoup
import org.jsoup.nodes.Document

object Parser {

    fun execute(url: String, userAgent: String): Document {
        return Jsoup.connect(url)
            .userAgent(userAgent)
            .get()
    }

}