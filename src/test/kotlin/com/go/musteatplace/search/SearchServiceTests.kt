package com.go.musteatplace.search

import org.springframework.http.*

class SearchServiceTest {

//  private val objectMapper = mockk<ObjectMapper>(relaxed = true)
//  private val searchKeywordRepository = mockk<SearchKeywordRepository>()
//  private val webClient = mockk<WebClient>()
//  private val requestHeadersUriSpec = mockk<WebClient.RequestHeadersUriSpec<*>>()
//  private val requestHeadersSpec = mockk<WebClient.RequestHeadersSpec<*>>()
//  private val responseSpec = mockk<WebClient.ResponseSpec>()
//
//  private val searchService = SearchService(objectMapper, webClient, searchKeywordRepository)
//
//  init {
//    every { webClient.get() } returns requestHeadersUriSpec
//    every { requestHeadersUriSpec.uri(any<URI>()) } returns requestHeadersSpec
//    every { requestHeadersSpec.headers(any()) } returns requestHeadersSpec
//    every { requestHeadersSpec.retrieve() } returns responseSpec
//  }
//
//  @Test
//  fun `getSearchResults handles failures from both APIs`() {
//    val searchParam = SearchRequest("burger", "random")
//
//    // TODO: type 에러 해결 필요
//    //    every { searchKeywordRepository.findByKeyword("burger") } returns null
//    //    every { searchKeywordRepository.save(any()) } answers { firstArg() }
//
//    every { responseSpec.bodyToMono(String::class.java) } returnsMany listOf(
//      Mono.error(WebClientResponseException(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Internal Server Error", HttpHeaders.EMPTY, null, null)),
//      Mono.error(WebClientResponseException(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Internal Server Error", HttpHeaders.EMPTY, null, null))
//    )
//
//    StepVerifier.create(searchService.getSearchResults(searchParam))
//      .expectErrorMatches { throwable ->
//        throwable is ServiceException && throwable.message == "Failed to fetch search results"
//      }
//      .verify()
//  }
}
