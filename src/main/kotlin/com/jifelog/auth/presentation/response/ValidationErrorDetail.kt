package com.jifelog.auth.presentation.response

data class ValidationErrorDetail(
    val field: String,
    val message: String,
)
