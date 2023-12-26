package com.go.musteatplace.search

import com.fasterxml.jackson.databind.ObjectMapper
import com.go.musteatplace.search.application.SearchService
import io.mockk.every
import io.mockk.mockk
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.http.RequestEntity
import org.springframework.web.client.RestClientException
import org.springframework.web.client.RestTemplate


class SearchServiceTest {

  private val objectMapper = mockk<ObjectMapper>()
  private val restTemplate = mockk<RestTemplate>()
//  private val searchService = SearchService(objectMapper)

  @Test
  fun `naverSearchResults handles RestClientException`() {
    val encodedKeyword = "encodedKeyword"
    val failMessage = """{"errorMessage":"Not Exist Client ID : Authentication failed. (인증에 실패했습니다.)","errorCode":"024"}"""
    val expectedExceptionMessage = "401 Unauthorized: \"$failMessage\""


    every {
      restTemplate.exchange(any<RequestEntity<String>>(), eq(String::class.java))
    } throws RestClientException(expectedExceptionMessage)

    val exception = assertThrows<RestClientException> {
//      searchService.naverSearchResults(encodedKeyword)
    }

    assertEquals(expectedExceptionMessage, exception.message)
  }

  @Test
  fun `kakaoSearchResults handles RestClientException`() {
    val encodedKeyword = "encodedKeyword"
    val failMessage = """{"errorType":"AccessDeniedError","message":"wrong appKey(null) format"}"""
    val expectedExceptionMessage = "401 Unauthorized: \"$failMessage\""


    every {
      restTemplate.exchange(any<RequestEntity<String>>(), eq(String::class.java))
    } throws RestClientException(expectedExceptionMessage)

    val exception = assertThrows<RestClientException> {
//      searchService.kakaoSearchResults(encodedKeyword)
    }

    assertEquals(expectedExceptionMessage, exception.message)
  }
}
