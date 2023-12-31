package com.go.musteatplace.search.application

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.go.musteatplace.search.presentation.dto.*
import org.hibernate.service.spi.ServiceException
import org.springframework.http.RequestEntity
import org.springframework.stereotype.Component
import org.springframework.web.client.RestTemplate
import org.springframework.web.util.UriComponentsBuilder

interface KakaoSearchClient : SearchClient {
  @Component
  class KakaoSearchClientImpl(
    private val objectMapper: ObjectMapper
  ) : KakaoSearchClient {
    override fun search(request: SearchRequest): String? {
      val kakaoApiKey = System.getenv("KAKAO_REST_API_KEY")

      val uri = UriComponentsBuilder
        .fromUriString("https://dapi.kakao.com")
        .path("/v2/local/search/keyword.json")
        .queryParam("query", request.keyword)
        .encode()
        .build()
        .toUri()

      val restTemplate = RestTemplate()

      val req = RequestEntity
        .get(uri)
        .header("Authorization", "KakaoAK $kakaoApiKey")
        .build()

      val res = restTemplate.exchange(req, String::class.java)

      return res.body
    }

    override fun parseSearchResults(res: String): List<SearchResultsDto> {
      try {
        val kakaoSearchResponse = objectMapper.readValue<KakaoSearchResponse>(res)
        return kakaoSearchResponse.documents.map { KakaoSearchResultsAdapter(it) }
      } catch (e: JsonProcessingException) {
        throw ServiceException("Error parsing search results", e)
      }
    }
  }
}
