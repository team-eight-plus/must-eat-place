package com.go.musteatplace.search.application

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.go.musteatplace.search.presentation.dto.*
import org.hibernate.service.spi.ServiceException
import org.springframework.context.annotation.Primary
import org.springframework.http.RequestEntity
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate
import org.springframework.web.util.UriComponentsBuilder

interface NaverSearchClient : SearchClient {
  @Component @Primary
  class NaverSearchClientImpl(
    private val objectMapper: ObjectMapper
  ) : NaverSearchClient {
    override fun search(request: SearchRequest): String? {
      val naverOpenApiId = System.getenv("NAVER_CLIENT_ID")
      val naverOpenApiSecret = System.getenv("NAVER_CLIENT_SECRET")

      val uri = UriComponentsBuilder
        .fromUriString("https://openapi.naver.com")
        .path("/v1/search/local.json")
        .queryParam("query", request.keyword)
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

      val res = restTemplate.exchange(req, String::class.java)
      return res.body
    }

    override fun parseSearchResults(res: String): List<SearchResultsDto>? {
      try {
        val naverSearchResponse = objectMapper.readValue<NaverSearchResponse>(res)
        return naverSearchResponse.items.map { NaverSearchResultsAdapter(it) }
      } catch (e: JsonProcessingException) {
        throw ServiceException("Error parsing search results", e)
      }
    }
  }
}
