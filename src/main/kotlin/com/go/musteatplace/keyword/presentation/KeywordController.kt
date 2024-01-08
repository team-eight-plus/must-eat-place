package com.go.musteatplace.keyword.presentation

import com.go.musteatplace.keyword.application.PopularKeywordService
import com.go.musteatplace.search.application.SearchService
import com.go.musteatplace.search.presentation.dto.ApiResponse
import com.go.musteatplace.search.presentation.dto.SearchRequest
import com.go.musteatplace.search.presentation.dto.createApiResponse
import jakarta.validation.Valid
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/keyword")
class KeywordController(
  val popularKeywordService: PopularKeywordService
) {
  @GetMapping("/rank")
  fun getKeywordRanks(): List<ApiResponse>? {
    return popularKeywordService.getRank()
      ?.map { result -> createApiResponse(data = result) }
  }
}
