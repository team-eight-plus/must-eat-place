package com.go.musteatplace.search.application

import com.go.musteatplace.search.domain.repository.SearchHistoryRepository
import com.go.musteatplace.search.presentation.dto.SearchResponse
import org.springframework.stereotype.Component

@Component
class SearchReader(
  private val searchHistoryRepository: SearchHistoryRepository,
) {
  fun getByKeyword(keyword: String): String? {
    return searchHistoryRepository.findByKeyword(keyword)
  }
}
