package com.jifelog.auth.presentation.request

import jakarta.validation.constraints.NotBlank

data class LoginRequest(
    @NotBlank(message = "Username is required")
    val loginId: String,
    @NotBlank(message = "Password is required")
    val password: String
)
