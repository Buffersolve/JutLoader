package com.buffersolve.jutloader.data.parser

import com.buffersolve.jutloader.data.util.Constants.Companion.BASE_URL
import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import kotlin.runCatching

object Parser {

    fun execute(url: String, userAgent: String): Result<Document> {

        val result = runCatching {
            Jsoup.connect(BASE_URL + url)
                .userAgent(userAgent)
                .get()
        }

        return result

    }
}