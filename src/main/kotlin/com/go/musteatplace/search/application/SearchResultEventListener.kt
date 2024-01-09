package com.go.musteatplace.search.application

import ch.qos.logback.classic.Logger
import com.go.musteatplace.search.domain.SearchHistory
import com.go.musteatplace.search.presentation.dto.SearchResponse
import org.slf4j.LoggerFactory
import org.springframework.context.event.EventListener
import org.springframework.stereotype.Component

@Component
class SearchResultEventListener(
  private val searchAppender: SearchAppender,
) {

  data class SearchResultEvent(
    val result: SearchResponse,
  )

  companion object {
    private val logger = LoggerFactory.getLogger(SearchResultEventListener::class.java)
  }

  @EventListener
  fun saveResult(event: SearchResultEvent) {
    try {
      searchAppender.save(result = SearchHistory.getSearchItems(event.result))
    } catch (e: Throwable) {
      logger.error("Error occurred during save search history: ${e.message}", e)
    }
  }
}
