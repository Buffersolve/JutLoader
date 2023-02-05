package com.buffersolve.jutloader.data.parser.getmethods

import com.buffersolve.jutloader.data.parser.Parser
import com.buffersolve.jutloader.domain.model.Resolution
import com.buffersolve.jutloader.domain.model.SpecificSeries

class GetSpecificSeriesLink {

    fun execute(
//        listOfSeries: List<String>,
        listOfLinks: List<String>,
        userAgent: String,
        resolution: String
    ): SpecificSeries {

        val linkToSpecificSeries = mutableListOf<String>()
        val listOfSeriesName = mutableListOf<String>()


        for (url in listOfLinks) {
            val doc = Parser.execute(
                url = url,
                userAgent = userAgent
            )

//            val name = doc.select("span[itemprop=\"name\"]").eachText()
            val name =
                doc.select("h1[class=\"header_video allanimevideo the_hildi anime_padding_for_title_post\"]")
                    .eachText()
                    .map { it.replaceFirst("Смотреть", "") }
                    .map { it.replaceFirst(" ", "") }
            listOfSeriesName.addAll(name)


            val resSelect = doc.select("source[res=\"${resolution}\"]")
                .attr("src")
            linkToSpecificSeries.add(resSelect)

        }

        return SpecificSeries(linkToSpecificSeries, listOfSeriesName)

    }

}