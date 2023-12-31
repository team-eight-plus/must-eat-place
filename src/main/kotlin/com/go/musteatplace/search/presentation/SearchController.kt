package com.go.musteatplace.search.presentation

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
  @GetMapping("/place")
  fun getSearchInfos(
    @Valid @ModelAttribute searchParam: SearchRequest
  ): List<ApiResponse>? {
    return searchService.getSearchResults(searchParam)
        ?.map { result -> createApiResponse(data = result) }
  }
}
