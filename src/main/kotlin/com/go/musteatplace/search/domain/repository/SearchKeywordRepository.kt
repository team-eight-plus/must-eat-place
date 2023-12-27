package com.go.musteatplace.search.domain.repository

import com.go.musteatplace.search.domain.SearchKeyword
import org.springframework.data.jpa.repository.JpaRepository


interface SearchKeywordRepository : JpaRepository<SearchKeyword, Long> {
  fun findByKeyword(keyword: String): SearchKeyword?

}
