package com.go.musteatplace.search.presentation

import com.go.musteatplace.search.application.SearchService
import com.go.musteatplace.search.presentation.dto.SearchRequest
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
    ) {
      return searchService.getSearchInfos(searchParam.keyword)
    }
}
