package com.go.musteatplace

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.go.musteatplace.search.application.SearchService
import com.go.musteatplace.search.presentation.SearchController
import com.go.musteatplace.search.presentation.dto.ApiResponse
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Test
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.setup.MockMvcBuilders

class SearchControllerTests {

  private val searchService = mockk<SearchService>()
  private val searchController = SearchController(searchService)
  private val mockMvc: MockMvc = MockMvcBuilders.standaloneSetup(searchController).build()
  private val objectMapper = jacksonObjectMapper()

  @Test
  fun `when SearchService throws exception, should return error ApiResponse`() {
  }
}
