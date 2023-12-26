package com.go.musteatplace.common.configuration

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.web.client.RestTemplate

@Configuration
class Configuration {
  @Bean
  fun restTemplate(): RestTemplate {
    return RestTemplate()
  }
}
