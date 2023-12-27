package com.go.musteatplace.search.application

import com.go.musteatplace.search.domain.SearchKeyword
import com.go.musteatplace.search.domain.repository.SearchKeywordRepository
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service

@Service
class SearchKeywordService(
  private val searchKeywordRepository: SearchKeywordRepository
) {
  @Transactional
  fun updateSearchKeywordCount(keyword: String) {
    val searchKeyword: SearchKeyword = searchKeywordRepository.findByKeyword(keyword)
      ?: SearchKeyword(0L, keyword)

    searchKeyword.count += 1
    searchKeywordRepository.save(searchKeyword)
  }
}
