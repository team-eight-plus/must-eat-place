package com.go.musteatplace.search.application

import org.springframework.http.RequestEntity
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import org.springframework.web.util.UriComponentsBuilder
import java.net.URLEncoder
import java.nio.charset.StandardCharsets


@Service
class SearchService(
//    private val searchRepository: SearchRepository,
) {
    fun getSearchInfos(keyword: String) {
        println(keyword)
        val naverOpenApiId = System.getenv("NAVER_CLIENT_ID")
        val naverOpenApiSecret = System.getenv("NAVER_CLIENT_SECRET")

        val encodedKeyword = URLEncoder.encode(keyword, StandardCharsets.UTF_8.toString())

        val uri = UriComponentsBuilder
            .fromUriString("https://openapi.naver.com")
            .path("/v1/search/local.json")
            .queryParam("query", encodedKeyword)
            .queryParam("display", 5)
            .queryParam("start", 1)
            .queryParam("sort", "random")
            .encode()
            .build()
            .toUri()

        val restTemplate = RestTemplate()

        val req = RequestEntity
            .get(uri)
            .header("X-Naver-Client-Id", naverOpenApiId)
            .header("X-Naver-Client-Secret", naverOpenApiSecret)
            .build()

        val res= restTemplate.exchange(req, String::class.java)
        val searchResult = res.body
        println(searchResult)

        return
    }
}
