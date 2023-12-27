package com.go.musteatplace.search.application

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.go.musteatplace.search.domain.SearchKeyword
import com.go.musteatplace.search.domain.repository.SearchKeywordRepository
import com.go.musteatplace.search.presentation.dto.*
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker
import jakarta.transaction.Transactional
import org.hibernate.service.spi.ServiceException
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import org.springframework.web.reactive.function.client.WebClientResponseException
import org.springframework.web.util.UriComponentsBuilder
import reactor.core.publisher.Mono
import java.net.URI

@Service
class SearchService(
  private val objectMapper: ObjectMapper,
  private val webClient: WebClient,
  private val searchKeywordRepository: SearchKeywordRepository
  ) {

  @Transactional
  fun updateSearchKeywordCount(keyword: String) {
    val searchKeyword: SearchKeyword = searchKeywordRepository.findByKeyword(keyword)
      ?: SearchKeyword(0L, keyword)

    searchKeyword.count += 1
    searchKeywordRepository.save(searchKeyword)
  }
  fun getSearchResults(searchParam: SearchRequest): Mono<SearchResponse> {
    val (keyword, sort) = searchParam

    updateSearchKeywordCount(keyword)

    return naverSearchResults(keyword, sort)
      .flatMap { json -> processSearchResults(json, "NAVER", keyword) }
      .onErrorResume(WebClientResponseException::class.java) {
        kakaoSearchResults(keyword)
          .flatMap { json -> processSearchResults(json, "KAKAO", keyword) }
      }
      .onErrorMap { e -> ServiceException("Failed to fetch search results", e) }
  }

  @CircuitBreaker(name = "kakaoSearch", fallbackMethod = "fallback")
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

  @CircuitBreaker(name = "naverSearch", fallbackMethod = "fallback")
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
