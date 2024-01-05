package com.go.musteatplace.search.domain.repository

import com.go.musteatplace.search.domain.SearchHistory
import com.go.musteatplace.search.presentation.dto.SearchResponse
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface SearchHistoryRepository: JpaRepository<SearchHistory, Long> {
  fun findByKeyword(keyword: String): String?
  fun save(result: SearchHistory)
}
