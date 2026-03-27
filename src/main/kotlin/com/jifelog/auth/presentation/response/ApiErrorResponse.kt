package com.jifelog.auth.presentation.response

import com.jifelog.auth.common.exception.ErrorCode

data class ApiErrorResponse(
    val data: ErrorBody,
) {
    data class ErrorBody(
        val errorCode: String,
        val message: String,
        val details: List<Any>?,
    )

    companion object {
        fun of(errorCode: ErrorCode, details: List<Any>? = null): ApiErrorResponse =
            ApiErrorResponse(ErrorBody(errorCode.name, errorCode.defaultMessage, details))
    }
}
