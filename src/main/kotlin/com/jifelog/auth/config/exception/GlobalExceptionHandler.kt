package com.jifelog.auth.config.exception

import com.jifelog.auth.common.exception.AuthException
import com.jifelog.auth.common.exception.ErrorCode
import com.jifelog.auth.presentation.response.ApiErrorResponse
import com.jifelog.auth.presentation.response.ValidationErrorDetail
import org.slf4j.LoggerFactory
import org.springframework.http.ResponseEntity
import org.springframework.http.converter.HttpMessageNotReadableException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class GlobalExceptionHandler {

    private val log = LoggerFactory.getLogger(this::class.java)

    @ExceptionHandler(AuthException::class)
    fun handleAuthException(ex: AuthException): ResponseEntity<ApiErrorResponse> =
        // TODO: 에러 상세 값 추가
        //ex.details

        ResponseEntity
            .status(ex.errorCode.httpStatus)
            .body(ApiErrorResponse.of(ex.errorCode))

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleValidation(ex: MethodArgumentNotValidException): ResponseEntity<ApiErrorResponse> {
        val details = ex.bindingResult.fieldErrors.map { fe ->
            ValidationErrorDetail(fe.field, fe.defaultMessage ?: "Invalid value")
        }
        return ResponseEntity
            .badRequest()
            .body(ApiErrorResponse.of(ErrorCode.EB_00_001, details))
    }

    @ExceptionHandler(HttpMessageNotReadableException::class)
    fun handleNotReadable(ex: HttpMessageNotReadableException): ResponseEntity<ApiErrorResponse> {
        log.warn("DataIntegrityViolationException", ex)

        return ResponseEntity
            .badRequest()
            .body(ApiErrorResponse.of(ErrorCode.EB_00_002))
    }


    @ExceptionHandler(Exception::class)
    fun handleAll(ex: Exception): ResponseEntity<ApiErrorResponse> {
        log.error("Unhandled exception", ex)
        return ResponseEntity
            .internalServerError()
            .body(ApiErrorResponse.of(ErrorCode.ES_00_001))
    }
}