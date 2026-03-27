package com.jifelog.auth.common.exception

class AuthException(
    val errorCode: ErrorCode,
    message: String = errorCode.defaultMessage,
    val details: List<Any>? = emptyList(),
) : RuntimeException(message)
