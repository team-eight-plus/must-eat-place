package com.go.musteatplace.search.application

import com.go.musteatplace.search.presentation.dto.*
import org.springframework.stereotype.Service

@Service
class SearchService(
  private val searchClient: SearchClient
) {
  fun getSearchResults(searchParam: SearchRequest): List<SearchResultsDto>? {
    val res = searchClient.search(searchParam)
    return res?.let { searchClient.parseSearchResults(it) }
  }
}
