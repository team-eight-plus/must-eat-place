package com.go.musteatplace.search.application

import com.go.musteatplace.common.utils.UriUtils.buildUri
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker
import org.hibernate.query.sqm.tree.SqmNode.log
import org.springframework.stereotype.Service
import org.springframework.web.reactive.function.client.WebClient
import reactor.core.publisher.Mono

@Service
class SearchClient(
  private val webClient: WebClient
) {
  @CircuitBreaker(name = "kakaoSearch", fallbackMethod = "fallback")
  fun naverSearchResults(encodedKeyword: String, sort: String): Mono<String> {
    val url = buildUri("https://openapi.naver.com/v1/search/local.json") {
      queryParam("query", encodedKeyword)
      queryParam("display", 5)
      queryParam("start", 1)
      queryParam("sort", sort)
    }

    val headers = mapOf(
      "X-Naver-Client-Id" to System.getenv("NAVER_CLIENT_ID"),
      "X-Naver-Client-Secret" to System.getenv("NAVER_CLIENT_SECRET")
    )

    return webClient.get()
      .uri(url)
      .headers { httpHeaders -> headers.forEach { httpHeaders.add(it.key, it.value) } }
      .retrieve()
      .bodyToMono(String::class.java)
  }

  @CircuitBreaker(name = "naverSearch", fallbackMethod = "fallback")
  fun kakaoSearchResults(encodedKeyword: String): Mono<String> {
    val uri = buildUri("https://dapi.kakao.com/v2/local/search/keyword.json") {
      queryParam("query", encodedKeyword)
    }

    val headers = mapOf(
      "Authorization" to "KakaoAK ${System.getenv("KAKAO_REST_API_KEY")}"
    )

    return webClient.get()
      .uri(uri)
      .headers { httpHeaders -> headers.forEach { httpHeaders.add(it.key, it.value) } }
      .retrieve()
      .bodyToMono(String::class.java)
  }

  fun fallback(keyword: String, e: Throwable): Mono<String> {
    // TODO: 로직 추가
    log.error("Search request failed for keyword: $keyword", e)
    return Mono.just("Fallback response")
  }
}
