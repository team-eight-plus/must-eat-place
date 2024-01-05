package com.go.musteatplace.search.application

import com.go.musteatplace.search.presentation.dto.*
import org.springframework.stereotype.Service

@Service
class SearchService(
  private val searchManager: SearchWorker
) {
  fun search(request: SearchRequest): List<SearchResultsDto>? {
    val res = searchManager.searchActual(request) ?: throw RuntimeException("Fail to get search result")
    return res?.let { searchManager.parseSearchResults(it) }
  }
}
