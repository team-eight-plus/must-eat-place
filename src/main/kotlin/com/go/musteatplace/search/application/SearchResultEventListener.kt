package com.go.musteatplace.search.application

import com.go.musteatplace.search.domain.SearchHistory
import com.go.musteatplace.search.presentation.dto.SearchResponse
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component

@Component
class SearchResultEventListener(
  private val searchAppender: SearchAppender,
) {

  data class SearchResultEvent(
    val result: SearchResponse,
  )
  @EventListener
  fun saveResult(event: SearchResultEvent) {
    try {
      searchAppender.save(result = SearchHistory.getSearchItems(event.result))
    } catch (e: Throwable) {
//      Logger.e("SearchResult Save Exception : {}", e, e.message)
    }
  }
}
