package com.go.musteatplace.search.domain.repository

import KeywordResponse
import com.go.musteatplace.keyword.domain.repository.SearchHistoryRepositoryCustom
import com.go.musteatplace.search.domain.SearchHistory
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface SearchHistoryRepository : JpaRepository<SearchHistory, Long>, SearchHistoryRepositoryCustom {
  fun findByKeyword(keyword: String): String?
  fun save(result: SearchHistory)

  override fun findTop10PopularKeywords(): List<KeywordResponse>
}
