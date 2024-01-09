package com.go.musteatplace.search.application

import com.go.musteatplace.search.presentation.dto.*
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service

@Service
class SearchService(
  private val searchManager: SearchWorker
) {
  companion object {
    private val logger = LoggerFactory.getLogger(SearchService::class.java)
  }

  fun search(request: SearchRequest): List<SearchResultsDto>? {
    try {
      val res = searchManager.searchActual(request) ?: throw RuntimeException("Fail to get search result")
      return res?.let { searchManager.parseSearchResults(it) }
    } catch (e: RuntimeException) {
      logger.error("Error occurred during search: ${e.message}", e)
      throw e
    }
  }
}
