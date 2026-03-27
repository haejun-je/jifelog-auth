package com.jifelog.auth.presentation.response

data class ApiResponse<T>(
    val data: T,
) {
    companion object {
        fun <T> of(data: T): ApiResponse<T> = ApiResponse(data)
        fun empty(): ApiResponse<Empty> = ApiResponse(Empty)
    }
}

object Empty