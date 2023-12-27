package com.go.musteatplace.search.application

import com.fasterxml.jackson.core.JsonProcessingException
import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.go.musteatplace.search.presentation.dto.*
import org.hibernate.service.spi.ServiceException
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClientResponseException
import reactor.core.publisher.Mono

@Service
class SearchService(
  private val objectMapper: ObjectMapper,
  private val searchKeywordService: SearchKeywordService,
  private val searchClient: SearchClient
  ) {

  fun getSearchResults(searchParam: SearchRequest): Mono<SearchResponse> {
    val (keyword, sort) = searchParam

    searchKeywordService.updateSearchKeywordCount(keyword)

    return searchClient.naverSearchResults(keyword, sort)
      .flatMap { json -> processSearchResults(json, "NAVER", keyword) }
      .onErrorResume(WebClientResponseException::class.java) {
        searchClient.kakaoSearchResults(keyword)
          .flatMap { json -> processSearchResults(json, "KAKAO", keyword) }
      }
      .onErrorMap { e -> ServiceException("Failed to fetch search results", e) }
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
