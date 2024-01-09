package com.go.musteatplace.keyword.domain.repository

import KeywordResponse

interface SearchHistoryRepositoryCustom {
  fun findTop10PopularKeywords(): List<KeywordResponse>
}
