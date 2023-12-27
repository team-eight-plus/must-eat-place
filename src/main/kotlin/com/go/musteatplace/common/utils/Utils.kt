package com.go.musteatplace.common.utils

import org.springframework.web.util.UriComponentsBuilder
import java.net.URI

object UriUtils {
  fun buildUri(baseUrl: String, uriBuilderAction: UriComponentsBuilder.() -> Unit): URI {
    return UriComponentsBuilder
      .fromUriString(baseUrl)
      .apply(uriBuilderAction)
      .encode()
      .build()
      .toUri()
  }
}
