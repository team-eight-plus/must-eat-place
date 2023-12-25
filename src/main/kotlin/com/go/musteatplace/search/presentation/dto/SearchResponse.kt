package com.go.musteatplace.search.presentation.dto

data class NaverSearchResponse(
  val lastBuildDate: String,
  val total: Int,
  val start: Int,
  val display: Int,
  val items: List<NaverSearchResultsDto>
)

data class KakaoSearchResponse(
  val documents: List<KakaoSearchResultsDto>
)

data class NaverSearchResultsDto(
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

data class KakaoSearchResultsDto(
  val address_name: String,
  val category_group_code: String,
  val category_group_name: String,
  val category_name: String,
  val distance: String,
  val id: String,
  val phone: String,
  val place_name: String,
  val place_url: String,
  val road_address_name: String,
  val x: String,
  val y: String,
)

data class SearchResponse(
  val keyword: String,
  val searchResults: List<NaverSearchResponse>
)

