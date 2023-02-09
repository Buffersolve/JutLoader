package com.buffersolve.jutloader.data.parser.getmethods

import com.buffersolve.jutloader.data.parser.Parser
import com.buffersolve.jutloader.domain.model.Series
import org.jsoup.select.Elements

class GetSeries {

    fun execute(url: String, userAgent: String): Series {

        val doc = Parser.execute(
            url = url,
            userAgent = userAgent
        )

        // Seria
        val seriaParce = doc.select("a[class=\"short-btn black video the_hildi\"]")
            .textNodes().map { it.toString() }
        val seriaParceGreen = doc.select("a[class=\"short-btn green video the_hildi\"]")
            .textNodes().map { it.toString() }

        val list = mutableListOf<String>()
        list.addAll(seriaParce)
        list.addAll(seriaParceGreen)
        list.toList()

        val elementsSeriaLink: Elements =
            doc.select("a[class=\"short-btn black video the_hildi\"]")
                .select("a")
        val elementsSeriaLinkGreen: Elements =
            doc.select("a[class=\"short-btn green video the_hildi\"]")
                .select("a")

        val elementsSeriaLinkList = mutableListOf<String>()

        elementsSeriaLink.forEach {
            val attr = it.attr("href")
            elementsSeriaLinkList.add(attr)
        }

        elementsSeriaLinkGreen.forEach {
            val attr = it.attr("href")
            elementsSeriaLinkList.add(attr)
        }

        return Series(list, elementsSeriaLinkList)

    }

}