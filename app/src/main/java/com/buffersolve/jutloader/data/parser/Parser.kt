package com.buffersolve.jutloader.data.parser

import org.jsoup.Jsoup
import org.jsoup.nodes.Document

object Parser {

    fun execute(url: String, userAgent: String): Document {
        return Jsoup.connect("https://jut.su/$url")
            .userAgent(userAgent)
            .get()
    }

}