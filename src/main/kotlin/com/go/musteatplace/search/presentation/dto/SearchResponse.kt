package com.go.musteatplace.search.presentation.dto

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

//class SearchResponse {
//  val keyword: String,
//  val searchResults: List<SearchResultsDto>,
//}
