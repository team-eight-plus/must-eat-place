package com.go.musteatplace.search.application

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.go.musteatplace.search.presentation.dto.NaverSearchResponse
import com.go.musteatplace.search.presentation.dto.SearchRequest
import com.go.musteatplace.search.presentation.dto.SearchResponse
import com.go.musteatplace.search.presentation.dto.SearchResultsDto
import org.hibernate.service.spi.ServiceException
import org.springframework.http.RequestEntity
import org.springframework.stereotype.Service
import org.springframework.web.client.RestClientException
import org.springframework.web.client.RestTemplate
import org.springframework.web.util.UriComponentsBuilder
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@Service
class SearchService(private val objectMapper: ObjectMapper) {
  fun getSearchResults(searchParam: SearchRequest): SearchResponse {
    val naverOpenApiId = System.getenv("NAVER_CLIENT_ID")
    val naverOpenApiSecret = System.getenv("NAVER_CLIENT_SECRET")

    val (keyword) = searchParam
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

    try {
      val res = restTemplate.exchange(req, String::class.java)
      val searchResults = parseSearchResults(res.body)

      return SearchResponse(keyword, searchResults)
    } catch (e: RestClientException) {
      throw ServiceException("Failed to fetch search results", e)
    }
  }

  fun parseSearchResults(json: String?): List<SearchResultsDto> {
    json ?: return emptyList()

    try {
      val naverSearchResponse = objectMapper.readValue<NaverSearchResponse>(json)
      return naverSearchResponse.items
    } catch (e: JsonProcessingException) {
      throw ServiceException("Error parsing search results", e)
    }
  }
}
