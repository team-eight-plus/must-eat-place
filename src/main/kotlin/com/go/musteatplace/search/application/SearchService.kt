package com.go.musteatplace.search.application

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.go.musteatplace.search.presentation.dto.*
import org.hibernate.service.spi.ServiceException
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.WebClientResponseException
import org.springframework.web.util.UriComponentsBuilder
import reactor.core.publisher.Mono
import java.net.URI
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@Service
class SearchService(
  private val objectMapper: ObjectMapper,
  private val webClient: WebClient,
) {
  fun getSearchResults(searchParam: SearchRequest): Mono<SearchResponse> {
    val (keyword, sort) = searchParam
    val encodedKeyword = URLEncoder.encode(keyword, StandardCharsets.UTF_8.toString())

    return naverSearchResults(encodedKeyword, sort)
      .flatMap { json -> processSearchResults(json, "NAVER", keyword) }
      .onErrorResume { e ->
        if (e is WebClientResponseException && e.statusCode.is5xxServerError) {
          kakaoSearchResults(encodedKeyword)
            .flatMap { json -> processSearchResults(json, "KAKAO", keyword) }
        } else {
          Mono.error(e)
        }
      }
      .onErrorMap { e -> ServiceException("Failed to fetch search results", e) }
  }

  fun naverSearchResults(encodedKeyword: String, sort: String): Mono<String> {
    val url = buildUri("https://openapi.naver.com/v1/search/local.json") {
      queryParam("query", encodedKeyword)
      queryParam("display", 5)
      queryParam("start", 1)
      queryParam("sort", sort)
    }

    val headers = mapOf(
      "X-Naver-Client-Id" to System.getenv("NAVER_CLIENT_ID"),
      "X-Naver-Client-Secret" to System.getenv("NAVER_CLIENT_SECRET")
    )

    return webClient.get()
      .uri(url)
      .headers { httpHeaders -> headers.forEach { httpHeaders.add(it.key, it.value) } }
      .retrieve()
      .bodyToMono(String::class.java)
  }

  fun kakaoSearchResults(encodedKeyword: String): Mono<String> {
    val uri = buildUri("https://dapi.kakao.com/v2/local/search/keyword.json") {
      queryParam("query", encodedKeyword)
    }

    val headers = mapOf(
      "Authorization" to "KakaoAK ${System.getenv("KAKAO_REST_API_KEY")}"
    )

    return webClient.get()
      .uri(uri)
      .headers { httpHeaders -> headers.forEach { httpHeaders.add(it.key, it.value) } }
      .retrieve()
      .bodyToMono(String::class.java)
  }

  fun buildUri(baseUrl: String, uriBuilderAction: UriComponentsBuilder.() -> Unit): URI {
    return UriComponentsBuilder
      .fromUriString(baseUrl)
      .apply(uriBuilderAction)
      .encode()
      .build()
      .toUri()
  }

  fun processSearchResults(json: String, serviceType: String, keyword: String): Mono<SearchResponse> {
    return Mono.justOrEmpty(parseSearchResults(json, serviceType))
      .map { searchResults -> SearchResponse(keyword, searchResults) }
      .defaultIfEmpty(SearchResponse(keyword, emptyList()))
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
