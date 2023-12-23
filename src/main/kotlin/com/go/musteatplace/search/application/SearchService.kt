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
class SearchService(private val objectMapper: ObjectMapper) {
  fun getSearchResults(searchParam: SearchRequest): List<Any>? {
    val (keyword) = searchParam
    val encodedKeyword = URLEncoder.encode(keyword, StandardCharsets.UTF_8.toString())

    try {
      val searchResults = naverSearchResults(encodedKeyword)
      if (searchResults != null) {
        return searchResults
      }
      return kakaoSearchResults(encodedKeyword)
    } catch (e: RestClientException) {
      throw ServiceException("Failed to fetch search results", e)
    }
  }

  fun naverSearchResults(encodedKeyword: String): List<Any>? {
    val naverOpenApiId = System.getenv("NAVER_CLIENT_ID")
    val naverOpenApiSecret = System.getenv("NAVER_CLIENT_SECRET")

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
    val res = restTemplate.exchange(req, String::class.java)
    return parseSearchResults(res.body, "NAVER")
  }

  fun kakaoSearchResults(encodedKeyword: String): List<Any>? {
    val kakaoApiKey = System.getenv("KAKAO_REST_API_KEY")

    val uri = UriComponentsBuilder
      .fromUriString("https://dapi.kakao.com")
      .path("/v2/local/search/keyword.json")
      .queryParam("query", encodedKeyword)
      .encode()
      .build()
      .toUri()

    val restTemplate = RestTemplate()

    val req = RequestEntity
      .get(uri)
      .header("Authorization", "KakaoAK ${kakaoApiKey}")
      .build()
    val res = restTemplate.exchange(req, String::class.java)
    return parseSearchResults(res.body, "KAKAO")
  }

  fun parseSearchResults(json: String?, serviceType: String): List<Any>? {
    json ?: return emptyList()
    println(json)
    try {
      return when (serviceType) {
        "NAVER" -> {
          val naverSearchResponse = objectMapper.readValue<NaverSearchResponse>(json)
          naverSearchResponse.items
        }
        else -> { // "KAKAO"
          val kakaoSearchResponse = objectMapper.readValue<KakaoSearchResponse>(json)
          kakaoSearchResponse.documents
        }
      }
    } catch (e: JsonProcessingException) {
      throw ServiceException("Error parsing search results", e)
    }
  }
}
