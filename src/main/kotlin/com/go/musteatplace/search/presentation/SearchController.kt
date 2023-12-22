package com.go.musteatplace.search.presentation

import com.go.musteatplace.common.exception.EmptyResultException
import com.go.musteatplace.search.application.SearchService
import com.go.musteatplace.search.presentation.dto.ApiResponse
import com.go.musteatplace.search.presentation.dto.SearchRequest
import com.go.musteatplace.search.presentation.dto.createApiResponse
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/search")
class SearchController(
    val searchService: SearchService
) {
    @GetMapping("/naver")
    fun getSearchInfos(
        @Valid @ModelAttribute searchParam: SearchRequest
    ): ApiResponse {
      val result = searchService.getSearchResults(searchParam)
      if (result.searchResults.isEmpty()) {
        throw EmptyResultException("No search results found for the keyword: ${searchParam.keyword}")
      }
      return createApiResponse(data = result)
    }
}
