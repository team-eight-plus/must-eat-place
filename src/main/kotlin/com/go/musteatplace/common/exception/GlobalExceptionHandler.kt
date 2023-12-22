package com.go.musteatplace.common.exception

import com.go.musteatplace.search.presentation.dto.ApiResponse
import com.go.musteatplace.search.presentation.dto.createApiResponse
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ControllerAdvice
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus

@ControllerAdvice
class GlobalExceptionHandler {

  @ExceptionHandler(RuntimeException::class)
  @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
  fun handleRuntimeException(ex: RuntimeException): ApiResponse {
    return createApiResponse<Unit>(status = 500, message = ex.message ?: "Internal Server Error")
  }
}
