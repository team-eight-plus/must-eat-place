package com.go.musteatplace.keyword.domain.repository

import KeywordResponse

interface SearchHistoryRepositoryCustom {
  fun getPopularKeywords(): List<KeywordResponse>
}

