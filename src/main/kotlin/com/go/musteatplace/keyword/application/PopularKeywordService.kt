package com.go.musteatplace.keyword.application

import KeywordResponse
import com.go.musteatplace.search.domain.repository.SearchHistoryRepository
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class PopularKeywordService(
  private val repository: SearchHistoryRepository,
) {
  fun getTop10PopularKeywords(): List<KeywordResponse> {
//    val updatedDateTime = LocalDateTime.now().minusDays(3)
    return repository.findTop10PopularKeywords()
  }
}
