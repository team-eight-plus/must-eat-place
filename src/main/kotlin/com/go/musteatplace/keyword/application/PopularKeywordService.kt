package com.go.musteatplace.keyword.application

import KeywordResponse
import com.go.musteatplace.search.domain.repository.SearchHistoryRepository
import org.springframework.stereotype.Service

@Service
class PopularKeywordService(
  private val repository: SearchHistoryRepository,
) {
  fun getRank(): List<KeywordResponse> {
    return repository.getPopularKeywords()
  }
}
