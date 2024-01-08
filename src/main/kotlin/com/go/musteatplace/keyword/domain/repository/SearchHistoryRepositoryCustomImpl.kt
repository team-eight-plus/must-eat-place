package com.go.musteatplace.keyword.domain.repository

import KeywordResponse
import com.go.musteatplace.search.domain.QSearchHistory
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.beans.factory.annotation.Qualifier
import java.time.LocalDateTime

class SearchHistoryRepositoryCustomImpl(
  @Qualifier("mainJpaQueryFactory")
  private val queryFactory: JPAQueryFactory
) : SearchHistoryRepositoryCustom {

  override fun getPopularKeywords(): List<KeywordResponse> {
    val searchHistory = QSearchHistory.searchHistory
    val now = LocalDateTime.now()

    val results = queryFactory
      .select(searchHistory.keyword, searchHistory.keyword.count())
      .from(searchHistory)
      .where(
        searchHistory.updatedDateTime.goe(now.minusDays(7)),
        searchHistory.updatedDateTime.lt(now)
      )
      .groupBy(searchHistory.keyword)
      .orderBy(searchHistory.keyword.count().desc())
      .limit(10)
      .fetch()

    return results.mapNotNull {
      val keyword = it.get(searchHistory.keyword)
      val count = it.get(searchHistory.keyword.count())
      if (keyword != null && count != null) KeywordResponse(keyword, count) else null
    }
  }
}
