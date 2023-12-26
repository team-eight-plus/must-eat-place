package com.go.musteatplace.search.application

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.go.musteatplace.search.presentation.dto.*
import org.hibernate.service.spi.ServiceException
import org.springframework.http.RequestEntity
import org.springframework.stereotype.Service
import org.springframework.web.client.RestClientException
import org.springframework.web.client.RestTemplate
import org.springframework.web.util.UriComponentsBuilder
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@Service
class SearchService(
  private val objectMapper: ObjectMapper,
  private val restTemplate: RestTemplate,
) {
  fun getSearchResults(searchParam: SearchRequest): SearchResponse {
    val (keyword, sort) = searchParam
    val encodedKeyword = URLEncoder.encode(keyword, StandardCharsets.UTF_8.toString())

    try {
      val searchResultsJson = naverSearchResults(restTemplate, encodedKeyword, sort) ?: kakaoSearchResults(restTemplate, encodedKeyword)
      val searchResults = parseSearchResults(searchResultsJson, if (searchResultsJson == naverSearchResults(restTemplate, encodedKeyword, sort)) "NAVER" else "KAKAO")
      return SearchResponse(keyword, searchResults ?: emptyList())
    } catch (e: RestClientException) {
      throw ServiceException("Failed to fetch search results", e)
    }
  }

  fun naverSearchResults(restTemplate: RestTemplate, encodedKeyword: String, sort: String): String? {
    val naverOpenApiId = System.getenv("NAVER_CLIENT_ID")
    val naverOpenApiSecret = System.getenv("NAVER_CLIENT_SECRET")

    val uri = UriComponentsBuilder
      .fromUriString("https://openapi.naver.com")
      .path("/v1/search/local.json")
      .queryParam("query", encodedKeyword)
      .queryParam("display", 5)
      .queryParam("start", 1)
      .queryParam("sort", sort)
      .encode()
      .build()
      .toUri()

    val req = RequestEntity
      .get(uri)
      .header("X-Naver-Client-Id", naverOpenApiId)
      .header("X-Naver-Client-Secret", naverOpenApiSecret)
      .build()
    return restTemplate.exchange(req, String::class.java).body
  }

  fun kakaoSearchResults(restTemplate: RestTemplate, encodedKeyword: String): String? {
    val kakaoApiKey = System.getenv("KAKAO_REST_API_KEY")

    val uri = UriComponentsBuilder
      .fromUriString("https://dapi.kakao.com")
      .path("/v2/local/search/keyword.json")
      .queryParam("query", encodedKeyword)
      .encode()
      .build()
      .toUri()

    val req = RequestEntity
      .get(uri)
      .header("Authorization", "KakaoAK ${kakaoApiKey}")
      .build()
    return restTemplate.exchange(req, String::class.java).body
  }

  fun parseSearchResults(json: String?, serviceType: String): List<SearchResultsDto>? {
    json ?: return emptyList()

    try {
      return when (serviceType) {
        "NAVER" -> {
          val naverSearchResponse = objectMapper.readValue<NaverSearchResponse>(json)
          naverSearchResponse.items.map { NaverSearchResultsAdapter(it) }
        }
        else -> { // "KAKAO"
          val kakaoSearchResponse = objectMapper.readValue<KakaoSearchResponse>(json)
          kakaoSearchResponse.documents.map { KakaoSearchResultsAdapter(it) }
        }
      }
    } catch (e: JsonProcessingException) {
      throw ServiceException("Error parsing search results", e)
    }
  }
}
