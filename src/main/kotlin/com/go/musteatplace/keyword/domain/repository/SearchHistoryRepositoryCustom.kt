package com.go.musteatplace.keyword.domain.repository

import KeywordResponse
import java.time.LocalDateTime

interface SearchHistoryRepositoryCustom {
  fun findTop10PopularKeywords(): List<KeywordResponse>
}
