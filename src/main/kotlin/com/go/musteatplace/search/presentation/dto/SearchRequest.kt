package com.go.musteatplace.search.presentation.dto

import jakarta.validation.constraints.NotNull

data class SearchRequest(
  @field:NotNull(message = "keyword는 필수 입력값 입니다.")
  val keyword: String
)
