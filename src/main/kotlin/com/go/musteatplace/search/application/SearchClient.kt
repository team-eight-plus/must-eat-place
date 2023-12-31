package com.go.musteatplace.search.application

import com.go.musteatplace.search.presentation.dto.SearchRequest
import com.go.musteatplace.search.presentation.dto.SearchResultsDto
import org.springframework.stereotype.Component

@Component
interface SearchClient {
  fun search(searchParam: SearchRequest): String?

  fun parseSearchResults(res: String): List<SearchResultsDto>?
}
