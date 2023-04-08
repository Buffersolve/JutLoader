package com.buffersolve.jutloader.data.parser.getmethods

import com.buffersolve.jutloader.data.parser.Parser
import com.buffersolve.jutloader.domain.model.SpecificEpisode

class GetSpecificEpisodesLink {

    fun execute(
        listOfLinks: List<String>,
        userAgent: String,
        resolution: String
    ): SpecificEpisode {

//        val linkToSpecificSeries = mutableListOf<String>()
//        val listOfSeriesName = mutableListOf<String>()

//        val specificEpisodeNameList = hashMapOf<String, String>()
        val specificEpisodeNameList = mutableListOf<String>()
        val specificEpisodeLinkList = mutableListOf<String>()


        for (url in listOfLinks) {
            val doc = Parser.execute(
                url = url,
                userAgent = userAgent
            )

            if (doc.isSuccess) {
                val name =
                    doc.getOrThrow()
                        .select("h1[class=\"header_video allanimevideo the_hildi anime_padding_for_title_post\"]")
                        .eachText()
                        .map { it.replaceFirst("Смотреть", "") }
                        .map { it.replaceFirst(" ", "") }
                specificEpisodeNameList.addAll(name)
//                specificEpisodeHashMap.keys.addAll(name)

                val resSelect = doc.getOrThrow().select("source[res=\"${resolution}\"]")
                    .attr("src")
//                specificEpisodeHashMap.entries.add(resSelect)
                specificEpisodeLinkList.add(resSelect)


            } else {

                specificEpisodeNameList.toList()
                specificEpisodeLinkList.toList()

            }

        }
        return SpecificEpisode(specificEpisodeNameList, specificEpisodeLinkList)
    }
}