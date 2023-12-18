package com.go.musteatplace.search.presentation.dto

data class NaverSearchResponse(
  val lastBuildDate: String,
  val total: Int,
  val start: Int,
  val display: Int,
  val items: List<SearchResultsDto>
)
data class SearchResultsDto(
  val title: String,
  val link: String,
  val category: String,
  val description: String,
  val telephone: String,
  val address: String,
  val roadAddress: String,
  val mapx: String,
  val mapy: String,
)

data class SearchResponse(
  val keyword: String,
  val searchResults: List<SearchResultsDto>
)
